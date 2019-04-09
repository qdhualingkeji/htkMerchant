package com.hualing.htk_merchant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.entity.LoginUserEntity;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.util.AllActivitiesHolder;
import com.hualing.htk_merchant.util.IntentUtil;
import com.hualing.htk_merchant.util.JPushUtil;
import com.hualing.htk_merchant.util.SharedPreferenceUtil;
import com.hualing.htk_merchant.utils.AsynClient;
import com.hualing.htk_merchant.utils.GsonHttpResponseHandler;
import com.hualing.htk_merchant.utils.MyApplication;
import com.hualing.htk_merchant.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

public class LaunchActivity extends BaseActivity {

    private static final long DELAY = 3000;
    private static final int JPUSH_ONE = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (SharedPreferenceUtil.ifHasLocalMerchantInfo()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toLogin();
                        }
                    });
                }else {
                    IntentUtil.openActivity(LaunchActivity.this,LoginActivity.class);
                    AllActivitiesHolder.removeAct(LaunchActivity.this);
                }
            }
        },DELAY);
    }

    private void toLogin(){
        RequestParams params = AsynClient.getRequestParams();
        params.put("userName", SharedPreferenceUtil.getMerchantInfo()[0]);
        params.put("password", SharedPreferenceUtil.getMerchantInfo()[1]);

        AsynClient.post(MyHttpConfing.login, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {

            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("rawJsonResponse======",""+rawJsonResponse);

                Gson gson = new Gson();
                LoginUserEntity loginUserEntity = gson.fromJson(rawJsonResponse, LoginUserEntity.class);
                if (loginUserEntity.getCode() == 100) {
                    LoginUserEntity.DataBean loginUserData = loginUserEntity.getData();
                    GlobalData.userID = loginUserData.getUserId();
                    GlobalData.userName = loginUserData.getUserName();
                    GlobalData.password = loginUserData.getPassword();
                    GlobalData.avatarImg = loginUserData.getAvatarImg();
                    GlobalData.shopName = loginUserData.getShopName();
                    GlobalData.state = loginUserData.getState();

                    //登录完毕,设置JPUSH的Alias识别
                    //String alias = loginUserData.getToken().toString().replaceAll("-", "");
                    String alias = "f8030831-996d-4a42-8ac3-df1b3793de19".replaceAll("-", "");
                    alias = alias.trim();
                    Log.e("alias===",""+alias);
                    JPushInterface.setAlias(LaunchActivity.this , JPUSH_ONE  , GlobalData.userName);
                    //设置JPush别名
                    JPushUtil.setAlias(LaunchActivity.this,alias);

                    Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    showMessage(loginUserEntity.getMessage());
                    IntentUtil.openActivity(LaunchActivity.this,LoginActivity.class);
                }
                AllActivitiesHolder.removeAct(LaunchActivity.this);

            }
        });
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_launch;
    }
}
