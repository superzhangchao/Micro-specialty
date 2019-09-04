package com.zc.canvas.bezier;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PathView extends View {
    private Path mPath = new Path();
    private Paint mPaint = new Paint();
    public PathView(Context context) {
        super(context);
        this.setBackgroundColor(Color.GREEN);
        mPaint.setColor(Color.RED);

        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);

    }

    public PathView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        mPaint.setStyle(Paint.Style.FILL);
        //一阶贝塞尔曲线，表示的是一条直线
//        mPath.moveTo(100, 70); //移动
//        mPath.lineTo(140, 800);//连线

        //等同于上一行代码效果
//        mPath.rLineTo(40,730);
//        mPath.lineTo(250, 600);
//        mPath.close();//设置曲线是否闭合

        //        //添加子图形addXXX
        //添加弧形

        mPath.addArc(200, 200, 400, 400, -225, 225);
        canvas.drawPath(mPath, mPaint);
    }
}
