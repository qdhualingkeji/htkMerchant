package com.hualing.htk_merchant.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.adapter.ProductAdapter;
import com.hualing.htk_merchant.entity.ReturnCategoryAndProductEntity;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.utils.AsynClient;
import com.hualing.htk_merchant.utils.GsonHttpResponseHandler;
import com.hualing.htk_merchant.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ProductListActivity extends BaseActivity {

    @BindView(R.id.product_lv)
    ListView productLV;
    private ProductAdapter productAdapter;
    public List<ReturnCategoryAndProductEntity.DataBean> getmData() {
        return mData;
    }

    public void setmData(List<ReturnCategoryAndProductEntity.DataBean> mData) {
        this.mData = mData;
    }

    private List<ReturnCategoryAndProductEntity.DataBean> mData;


    public void initData(){
        RequestParams params = AsynClient.getRequestParams();
        params.put("actionName", "getData");
        params.put("userId", GlobalData.userID);

        AsynClient.post(MyHttpConfing.getProductData, ProductListActivity.this, params, new GsonHttpResponseHandler() {
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
                ReturnCategoryAndProductEntity rcapEntity = gson.fromJson(rawJsonResponse, ReturnCategoryAndProductEntity.class);
                if (rcapEntity.getCode() == 0) {
                    mData = rcapEntity.getData();
                    productAdapter = new ProductAdapter(ProductListActivity.this,mData);
                    productLV.setAdapter(productAdapter);
                }
                else{
                    showMessage(rcapEntity.getMessage());
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        initData();
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_product_list;
    }
}
