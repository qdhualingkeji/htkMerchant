package com.hualing.htk_merchant.global;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hualing.htk_merchant.R;

import java.io.File;

/**
 * @author 马鹏昊
 * @date {2016-10-28}
 * @des 获取全局对象
 * @updateAuthor
 * @updateDate
 * @updateDes
 */
public class TheApplication extends Application {


    //全局Context
    private static Context mContext;

    //屏幕宽度
    public static int screenWidth;
    //屏幕高度
    public static int screenHeight;

    //SharedPreference
    private static SharedPreferences sSharedPreferences;

    //此App一些信息的存储文件夹路径
    private static final String appDirectoryPath = File.separator + "mnt" + File.separator + "sdcard" + File.separator + "qrcodetracker";
    //此App一些信息的存储文件夹
    private static File appRootDirectory;


    public static String ImagePath;

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        TheApplication.screenWidth = screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        TheApplication.screenHeight = screenHeight;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        sSharedPreferences = mContext.getSharedPreferences("loginInfo", MODE_PRIVATE);

        GlobalData.Load(mContext);//获取手机跟app信息及初始化url

        //设置启动页的时间和图片
        GlobalData.setLaunchDelayDuration(2000);
//        GlobalData.setLaunchDrawableId(R.drawable.launch);

        //初始化Fresco
        Fresco.initialize(this);

        appRootDirectory = new File(appDirectoryPath);
        if (appRootDirectory.exists() && appRootDirectory.isDirectory()) {
            appRootDirectory.delete();
        }
        appRootDirectory.mkdirs();

    }

    //外部得到全局context的接口
    public static Context getContext() {
        return mContext;
    }

    //外部得到SharedPreference的接口
    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    //外部得到App根目录文件夹路径
    public static String getAppRootDirectory() {
        return appDirectoryPath;
    }

    /*
        得到dp转化成的px
    */
    public static int getPxFromDp(float dip) {
        float result = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, mContext.getResources().getDisplayMetrics());
        return (int) result;
    }
    /*
        得到dp转化成的px
    */
    public static float getPxFromSp(float sp) {
        float result = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, mContext.getResources().getDisplayMetrics());
        return result;
    }

}
