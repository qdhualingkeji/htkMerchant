package com.hualing.htk_merchant.util;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.hualing.htk_merchant.global.TheApplication;
import com.hualing.htk_merchant.global.TheApplication;


/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class SharedPreferenceUtil {

    /**
     * 记住是什么身份使用程序
     */
    public static void setUserType(int userType){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("userType",userType);
        editor.commit();
    }

    /**
     * 记住商户号、密码
     * @param phone
     * @param password
     */
    public static void rememberMerchant(String phone, String password){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName",phone);
        editor.putString("password",password);
        editor.commit();
    }

    public static boolean ifHasLocalMerchantInfo(){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        if (TextUtils.isEmpty(preferences.getString("userName",null))||TextUtils.isEmpty(preferences.getString("password",null))) {
            return false;
        }
        return true;
    }

    /**
     * 获取上次登陆的商户信息
     * @return
     */
    public static String[] getMerchantInfo(){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        String userName = preferences.getString("userName",null);
        String password = preferences.getString("password",null);
        return new String[]{userName,password};
    }

    /**
     * 注销登录
     */
    public static void logout(){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName","");
        editor.putString("password","");
        editor.commit();
    }

    /**
     * 获取加载页地址
     */
    public static String getLoadPageUrl(){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        return preferences.getString("loadPageUrl",null);
    }

    /**
     * 获取加载页地址
     */
    public static void setLoadPageUrl(String url){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("loadPageUrl", url);
        editor.commit();
    }

    /**
     * 保存是否有未读消息
     */
    public static void saveIfHasUnreadMsg(boolean flag){
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("ifHasUnreadMsg", flag);
        editor.commit();
    }

    /**
     * 查看是否有未读消息
     */
    public static boolean checkIfHasUnreadMsg() {
        SharedPreferences preferences = TheApplication.getSharedPreferences() ;
        return preferences.getBoolean("ifHasUnreadMsg",false);
    }
}
