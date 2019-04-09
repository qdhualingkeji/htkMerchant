package com.hualing.htk_merchant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.entity.LoginUserEntity;
import com.hualing.htk_merchant.entity.SuccessEntity;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.util.AllActivitiesHolder;
import com.hualing.htk_merchant.util.SharedPreferenceUtil;
import com.hualing.htk_merchant.utils.AsynClient;
import com.hualing.htk_merchant.utils.GsonHttpResponseHandler;
import com.hualing.htk_merchant.utils.MyApplication;
import com.hualing.htk_merchant.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.userNameValue)
    EditText userNameValue;
    @BindView(R.id.passwordValue)
    EditText passwordValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void initLogic() {

    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.loginBtn})
    public void onViewClicked(View v){
        switch (v.getId()){
            case R.id.loginBtn:
                String userName = userNameValue.getText().toString();
                String password = passwordValue.getText().toString();
                if(TextUtils.isEmpty(userName)){
                    showMessage("请输入商户号");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    showMessage("请输入密码");
                    return;
                }
                login(userName,password);
                break;
        }
    }

    /***
     * 商户登录
     * @param userName
     * @param password
     */
    private void login(final String userName, final String password){
        RequestParams params = AsynClient.getRequestParams();
        params.put("userName", userName);
        params.put("password", password);

        AsynClient.post(MyHttpConfing.login, this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("rawJsonData======",""+rawJsonData);
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

                    SharedPreferenceUtil.rememberMerchant(userName, password);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    AllActivitiesHolder.removeAct(LoginActivity.this);
                }
                else {
                    showMessage(loginUserEntity.getMessage());
                }

            }
        });
    }
}
