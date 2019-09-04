package com.zc.canvasdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "zhangchao";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(MainActivity.this,SeActivity.class);
//                MainActivity.this.startActivity(intent);
//                MainActivity.this.finish();
//            }
//        },8000);

        // 通过WindowManager获取
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Log.i("zhangchao","width-display1 :" + dm.widthPixels);
        Log.i("zhangchao","heigth-display1 :" + dm.heightPixels);

        // 通过Resources获取
        DisplayMetrics dm2 = getResources().getDisplayMetrics();
        Log.i("zhangchao","width-display2 :" + dm2.widthPixels);
        Log.i("zhangchao","heigth-display2 :" + dm2.heightPixels);

        // 获取屏幕的默认分辨率
        Display display = getWindowManager().getDefaultDisplay();
        Log.i("zhangchao","width-display3 :" + display.getWidth());
        Log.i("zhangchao","heigth-display3 :" + display.getHeight());

        int navigationBarHeight = getNavigationBarHeight(this);
        Log.i("zhangchao", "navigationBarHeight: "+navigationBarHeight);
    }
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    public void SplashView(View view) {
        Log.i(TAG, "SplashView: ");
    }

    public void image(View view) {
        Log.i(TAG, "image: ");
    }
}
