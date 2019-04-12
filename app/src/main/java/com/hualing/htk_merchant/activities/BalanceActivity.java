package com.hualing.htk_merchant.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.model.CommonMsg;
import com.hualing.htk_merchant.utils.AsynClient;
import com.hualing.htk_merchant.utils.GsonHttpResponseHandler;
import com.hualing.htk_merchant.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import java.util.Map;

import butterknife.BindView;

public class BalanceActivity extends BaseActivity {

    @BindView(R.id.aliPayAccount_tv)
    TextView aliPayAccountTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        initData();
    }

    private void initData() {
        RequestParams params = AsynClient.getRequestParams();
        params.put("accountToken", GlobalData.token);

        AsynClient.post(MyHttpConfing.getBalance, BalanceActivity.this, params, new GsonHttpResponseHandler() {
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
                CommonMsg commonMsg = gson.fromJson(rawJsonResponse, CommonMsg.class);
                if (commonMsg.getCode() == 100) {
                    Map<String, Object> balanceMap = (Map<String, Object>) commonMsg.getData();
                    aliPayAccountTV.setText(balanceMap.get("aliPayAccount").toString());
                }
                else{
                    showMessage(commonMsg.getMessage());
                }
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
        return R.layout.activity_balance;
    }
}
