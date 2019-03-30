package com.hualing.htk_merchant.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import butterknife.OnClick;

public class ProductListActivity extends BaseActivity {

    @BindView(R.id.on_but)
    Button onBut;
    @BindView(R.id.off_but)
    Button offBut;
    @BindView(R.id.act_but)
    Button actBut;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        productAdapter = new ProductAdapter(ProductListActivity.this);
        productAdapter.setNewData();
        productLV.setAdapter(productAdapter);
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

    @OnClick({R.id.on_but,R.id.off_but,R.id.act_but})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.on_but:
                productAdapter.takeOn();
                break;
            case R.id.off_but:
                productAdapter.takeOff();
                break;
            case R.id.act_but:
                if(onBut.getVisibility()==Button.INVISIBLE)
                    onBut.setVisibility(Button.VISIBLE);
                else
                    onBut.setVisibility(Button.INVISIBLE);

                if(offBut.getVisibility()==Button.INVISIBLE)
                    offBut.setVisibility(Button.VISIBLE);
                else
                    offBut.setVisibility(Button.INVISIBLE);

                productAdapter.showCheckGoodsCB();
                productAdapter.notifyDataSetChanged();
                break;
        }
    }
}
