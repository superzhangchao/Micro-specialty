package com.zc.canvasdemo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;


/**
 * @author zhangchao
 * @date 2019/5/20
 */
public class SplashView extends View {
    private static final String TAG = "zhangchao";

    //旋转圆
    private Paint mPaint;
    //扩散圆
    private Paint mHolePaint;

    private int  mBackgroundColor = Color.WHITE;
    //旋转圆的圆心坐标
    private float mCenterX;
    private float mCenterY;

    //小球的半径
    private float mCircleRadius = 19;
    //小球的的颜色
    private int [] mCircleColors;
    //旋转的圆的最大半径
    private float mRotateRadius = 90;
    //旋转圆的当前旋转角
    private float mCurrentRotateAngle = 0F;
    //旋转圆的当前半径
    private float mCurrentRotateRadius = mRotateRadius;

    //扩散圆半径
    private float mCurrentHoleRadius = 0F;
    //斜对角线，扩散圆的最大的半径
    private float mDistance;
    //属性动画
    private ValueAnimator mValueAnimator;
    //旋转时间
    private int mRotateDuration = 1200;
    public SplashView(Context context) {
        this(context, null);
    }

    public SplashView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHolePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHolePaint.setStyle(Paint.Style.STROKE);
        mHolePaint.setColor(mBackgroundColor);

        mCircleColors = context.getResources().getIntArray(R.array.splash_circle_colors);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w*1f/2;
        mCenterY = h*1f/2;
        mDistance = (float) (Math.hypot(w,h)/2);
        Log.i(TAG, "onSizeChanged: "+w+"---"+h);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "onMeasure: "+widthMeasureSpec+"---"+heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.i(TAG, "onLayout: ");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i(TAG, "onDraw: ");
        if (splashState ==null){
            splashState = new RotateState();
        }
        splashState.drawState(canvas);
    }

    private SplashState splashState;

    private abstract class SplashState{
        abstract void drawState(Canvas canvas);
    }

    private class RotateState extends SplashState{
        private RotateState(){
            mValueAnimator = ValueAnimator.ofFloat(0, (float) (Math.PI*2));
            mValueAnimator.setRepeatCount(2);
            mValueAnimator.setDuration(mRotateDuration);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotateAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.start();
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    splashState = new MerginState();
                }
            });
        }

        @Override
        void drawState(Canvas canvas) {
                drawBackground(canvas);
                drawCircles(canvas);
        }

    }

    private class MerginState extends SplashState{
        private MerginState(){
            mValueAnimator = ValueAnimator.ofFloat(mCircleRadius,mRotateRadius);
//            mValueAnimator.setRepeatCount(2);
            mValueAnimator.setInterpolator(new OvershootInterpolator(10f));
            mValueAnimator.setDuration(mRotateDuration);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotateRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    splashState = new ExpandState();
                }
            });
            mValueAnimator.reverse();
        }
        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
            drawCircles(canvas);

        }
    }

    private class ExpandState extends SplashState{
        private ExpandState(){
            mValueAnimator = ValueAnimator.ofFloat(0f,mDistance);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.setDuration(mRotateDuration);
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentHoleRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
//                    SplashView.this.setVisibility(GONE);
                    SplashView.this.setVisibility(INVISIBLE);
                    SplashView.this.setVisibility(VISIBLE);
                }
            });
            mValueAnimator.start();
        }


        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
        }
    }


    private void drawCircles(Canvas canvas){
        float rotateAngle = (float) (Math.PI * 2 / mCircleColors.length);
        for (int i = 0; i < mCircleColors.length; i++) {
            //x = r * cosa +centX
            //y = r * sina +centY;
            float angle = i* rotateAngle + mCurrentRotateAngle;
            float cx = (float) (mCurrentRotateRadius*Math.cos(angle)+ mCenterX);
            float cy = (float) (mCurrentRotateRadius*Math.sin(angle)+ mCenterY);
            mPaint.setColor(mCircleColors[i]);
            canvas.drawCircle(cx,cy,mCircleRadius,mPaint);
        }
    }

    private void drawBackground(Canvas canvas){
        if (mCurrentHoleRadius>0){
            //扩散圆
            float strokWidth = mDistance - mCurrentHoleRadius;
            float radius = strokWidth / 2 + mCurrentHoleRadius;
            mHolePaint.setStrokeWidth(strokWidth);
            canvas.drawCircle(mCenterX,mCenterY,radius,mHolePaint);
        }else{
            canvas.drawColor(mBackgroundColor);
        }
    }
}
