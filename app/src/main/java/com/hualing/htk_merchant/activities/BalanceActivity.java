package com.hualing.htk_merchant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.entity.SuccessEntity;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.model.CommonMsg;
import com.hualing.htk_merchant.util.AllActivitiesHolder;
import com.hualing.htk_merchant.util.SharedPreferenceUtil;
import com.hualing.htk_merchant.utils.AsynClient;
import com.hualing.htk_merchant.utils.GsonHttpResponseHandler;
import com.hualing.htk_merchant.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class BalanceActivity extends BaseActivity {

    @BindView(R.id.aliPayAccount_tv)
    TextView aliPayAccountTV;
    @BindView(R.id.availableBalance_tv)
    TextView availableBalanceTV;
    @BindView(R.id.withdrawDepositAmount_et)
    EditText withdrawDepositAmountET;
    @BindView(R.id.finalMoney_tv)
    TextView finalMoneyTV;
    @BindView(R.id.confirmTheAmount_but)
    Button confirmTheAmountBut;
    @BindView(R.id.updateAmount_but)
    Button updateAmountBut;
    @BindView(R.id.toConfirmWithdrawal_but)
    Button toConfirmWithdrawalBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        initView();
        initData();
    }

    private void initView() {
        withdrawDepositAmountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String value = editable.toString();
                value = value.replaceAll("/[^\\d.]/g", "");  //清除“数字”和“.”以外的字符
                value = value.replace("/\\.{2,}/g", "."); //只保留第一个. 清除多余的
                value = value.replace(".", "$#$").replace("/\\./g", "").replace("$#$", ".");
                value = value.replace("/^(\\-)*(\\d+)\\.(\\d\\d).*$/", "$1$2.$3");//只能输入两个小数
                if (value.indexOf(".") < 0 && value != "") {//以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
                    float valueFloat = Float.parseFloat(value);
                    Log.e("valueFloat===",""+valueFloat);
                }

                if(value.length()>0){
                    confirmTheAmountBut.setEnabled(true);
                }
                else{
                    confirmTheAmountBut.setEnabled(false);
                }
            }
        });
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
                    availableBalanceTV.setText(balanceMap.get("availableBalance").toString());
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

    @OnClick({R.id.confirmTheAmount_but,R.id.updateAmount_but,R.id.toConfirmWithdrawal_but})
    public void onViewClicked(View v){
        switch (v.getId()){
            case R.id.confirmTheAmount_but:
                confirmTheAmount();
                break;
            case R.id.updateAmount_but:
                updateAmount();
                break;
            case R.id.toConfirmWithdrawal_but:
                toConfirmWithdrawal();
                break;
        }
    }

    /**
     * 确认提现
     */
    private void toConfirmWithdrawal() {
        showLoadingDialog(BalanceActivity.this,"转账中");
        RequestParams params = AsynClient.getRequestParams();
        params.put("userId", GlobalData.userID);
        params.put("money", finalMoneyTV.getText().toString());

        AsynClient.post(MyHttpConfing.withdraw, this, params, new GsonHttpResponseHandler() {
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
                SuccessEntity successEntity = gson.fromJson(rawJsonResponse, SuccessEntity.class);
                hideLoadingDialog();
                showMessage(successEntity.getMessage());
                if (successEntity.getCode() == 0) {
                    AllActivitiesHolder.removeAct(BalanceActivity.this);
                }
            }
        });
    }

    /**
     * 修改金额
     */
    private void updateAmount() {
        finalMoneyTV.setVisibility(View.GONE);
        updateAmountBut.setVisibility(View.GONE);
        withdrawDepositAmountET.setVisibility(View.VISIBLE);
        confirmTheAmountBut.setVisibility(View.VISIBLE);
        toConfirmWithdrawalBut.setEnabled(false);
    }

    /**
     * 确认金额
     */
    private void confirmTheAmount() {
        float usableBalance = Float.parseFloat(availableBalanceTV.getText().toString());
        String withdrawDepositAmountStr = withdrawDepositAmountET.getText().toString();
        float withdrawDepositAmount = Float.parseFloat(withdrawDepositAmountStr);
        if (withdrawDepositAmount > 1 && withdrawDepositAmount <= usableBalance) {
            int ind = withdrawDepositAmountStr.indexOf(".");
            if (ind > 0) {
                //输入了小数点,判断小数点后面有几位数
                String subStrVal = withdrawDepositAmountStr.substring(ind + 1, withdrawDepositAmountStr.length());
                if (subStrVal == "") {
                    //小数点后没有值
                    withdrawDepositAmountStr += "00";
                } else if (subStrVal.length() == 1) {
                    withdrawDepositAmountStr += "0";
                } else {
                    withdrawDepositAmountStr += "";
                }
            } else {
                //没有小数点,加小数点
                withdrawDepositAmountStr += ".00";
            }
            finalMoneyTV.setVisibility(EditText.VISIBLE);
            finalMoneyTV.setText(withdrawDepositAmountStr);
            updateAmountBut.setVisibility(EditText.VISIBLE);
            withdrawDepositAmountET.setVisibility(EditText.GONE);
            confirmTheAmountBut.setVisibility(EditText.GONE);
            //置确认提现按钮为可点击状态
            toConfirmWithdrawalBut.setEnabled(true);
        } else if (withdrawDepositAmount <= 1) {
            //输入数字无效
            showMessage("提现金额必须大于1元!");
            //置确认提现按钮为不可点击状态
            toConfirmWithdrawalBut.setEnabled(false);
        } else if (withdrawDepositAmount > usableBalance) {
            //输入金额大于现有可用余额
            showMessage("输入金额大于现有可用余额,请重新输入!");
            //置确认提现按钮为不可点击状态
            toConfirmWithdrawalBut.setEnabled(false);
        }

    }
}
