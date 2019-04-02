package com.hualing.htk_merchant.activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.adapter.ProductPropertyAdapter;
import com.hualing.htk_merchant.entity.ProductDetailEntity;
import com.hualing.htk_merchant.entity.ReturnCategoryAndProductEntity;
import com.hualing.htk_merchant.entity.TakeoutProductEntity;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.model.TakeoutCategory;
import com.hualing.htk_merchant.model.TakeoutProduct;
import com.hualing.htk_merchant.util.AllActivitiesHolder;
import com.hualing.htk_merchant.util.IntentUtil;
import com.hualing.htk_merchant.utils.AsynClient;
import com.hualing.htk_merchant.utils.GsonHttpResponseHandler;
import com.hualing.htk_merchant.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class EditProductActivity extends BaseActivity {

    @BindView(R.id.productName_et)
    EditText productNameET;
    @BindView(R.id.category_spinner)
    Spinner categorySpinner;
    @BindView(R.id.imgUrl_sdv)
    SimpleDraweeView imgUrlSDV;
    @BindView(R.id.description_et)
    EditText descriptionET;
    @BindView(R.id.price_et)
    EditText priceET;
    @BindView(R.id.priceCanhe_et)
    EditText priceCanheET;
    @BindView(R.id.inventory_et)
    EditText inventoryET;
    @BindView(R.id.inventoryCount_et)
    EditText inventoryCountET;
    @BindView(R.id.property_gv)
    GridView propertyGV;
    @BindView(R.id.integral_et)
    EditText integralET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        int productId = getIntent().getIntExtra("productId", 0);
        
        RequestParams params = AsynClient.getRequestParams();
        params.put("userId", GlobalData.userID);
        params.put("productId", productId);

        AsynClient.post(MyHttpConfing.editProduct, EditProductActivity.this, params, new GsonHttpResponseHandler() {
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
                ProductDetailEntity productDetailEntity = gson.fromJson(rawJsonResponse, ProductDetailEntity.class);
                if (productDetailEntity.getCode() == 100) {
                    ProductDetailEntity.ProductDetail productDetail = productDetailEntity.getData();
                    initData(productDetail);
                }
                else{
                    showMessage(productDetailEntity.getMessage());
                }
            }
        });
    }

    private void initData(ProductDetailEntity.ProductDetail productDetail) {
        TakeoutProduct takeoutProduct = productDetail.getDataPro();
        productNameET.setText(takeoutProduct.getProductName());
        initCategorySpinner(productDetail.getData());
        Uri uri = Uri.parse(takeoutProduct.getImgUrl());
        imgUrlSDV.setImageURI(uri);
        descriptionET.setText(takeoutProduct.getDescription());
        priceET.setText("￥"+takeoutProduct.getPrice());
        priceCanheET.setText("￥"+takeoutProduct.getPriceCanhe());
        inventoryET.setText(String.valueOf(takeoutProduct.getInventory()));
        inventoryCountET.setText(String.valueOf(takeoutProduct.getInventoryCount()));
        initPropertyGV(takeoutProduct.getProperty());
        integralET.setText(String.valueOf(takeoutProduct.getIntegral()));
    }

    private void initPropertyGV(String property) {
        String[] propertyArr = property.split(",");
        List<String> propertyList = new ArrayList<>();
        for (String pro : propertyArr){
            propertyList.add(pro);
        }
        ProductPropertyAdapter adapter = new ProductPropertyAdapter(EditProductActivity.this, propertyList);
        propertyGV.setAdapter(adapter);
    }

    private void initCategorySpinner(List<TakeoutCategory> categoryList) {
        List<String> categoryNameList = new ArrayList<>();
        for (TakeoutCategory tc : categoryList){
            categoryNameList.add(tc.getCategoryName());
        }
        // 为下拉列表定义一个适配器，使用到上面定义的categoryList
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditProductActivity.this, android.R.layout.simple_spinner_item, categoryNameList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    @OnClick({R.id.save_but,R.id.cancel_but})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.save_but:
                try {
                    saveProduct();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.cancel_but:
                AllActivitiesHolder.removeAct(EditProductActivity.this);
                break;
        }
    }

    private void saveProduct() throws FileNotFoundException {
        RequestParams params = AsynClient.getRequestParams();
        TakeoutProduct tp = new TakeoutProduct();
        tp.setProductName("pppppppp");
        //params.put("takeoutProduct", tp);
        params.put("avaImgFile", new File("/mnt/m_external_sd/DCIM/Camera/zhoukaixiang.jpg"));

        AsynClient.post(MyHttpConfing.saveProduct, EditProductActivity.this, params, new GsonHttpResponseHandler() {
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
        return R.layout.activity_edit_product;
    }
}
