package com.hualing.htk_merchant.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hualing.htk_merchant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/13.
 */

public class MyApplication extends Application {

    private static Context mContext;

    //屏幕的宽和高
    private static int screenWidth;
    private static int screenHeight;


    public   static   int  shopId  = -1;

    public static MyApplication get(Context context) {

        return (MyApplication) context.getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }

    public static int getScreenWidth(){
        return screenWidth;
    }
    public static int getScreenHeight(){
        return screenHeight;
    }

    public static void setScreenWidth(int screenWidth) {
        MyApplication.screenWidth = screenWidth;
    }

    public static void setScreenHeight(int screenHeight) {
        MyApplication.screenHeight = screenHeight;
    }


}
