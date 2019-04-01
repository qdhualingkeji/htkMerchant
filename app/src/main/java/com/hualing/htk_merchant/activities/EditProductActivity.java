package com.hualing.htk_merchant.activities;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.entity.ProductDetailEntity;
import com.hualing.htk_merchant.entity.ReturnCategoryAndProductEntity;
import com.hualing.htk_merchant.entity.TakeoutProductEntity;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.model.TakeoutCategory;
import com.hualing.htk_merchant.model.TakeoutProduct;
import com.hualing.htk_merchant.utils.AsynClient;
import com.hualing.htk_merchant.utils.GsonHttpResponseHandler;
import com.hualing.htk_merchant.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

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
        integralET.setText(String.valueOf(takeoutProduct.getIntegral()));
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
