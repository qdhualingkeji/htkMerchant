package com.hualing.htk_merchant.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.adapter.CategoryAdapter;
import com.hualing.htk_merchant.adapter.EditProductPropertyAdapter;
import com.hualing.htk_merchant.entity.ProductDetailEntity;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.model.TakeoutCategory;
import com.hualing.htk_merchant.model.TakeoutProduct;
import com.hualing.htk_merchant.util.AllActivitiesHolder;
import com.hualing.htk_merchant.utils.AsynClient;
import com.hualing.htk_merchant.utils.GsonHttpResponseHandler;
import com.hualing.htk_merchant.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
    private List<String> propertyList;
    @BindView(R.id.property_gv)
    GridView propertyGV;
    @BindView(R.id.integral_et)
    EditText integralET;
    private Integer id;
    private Integer categoryId;
    private File imgFile;

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
        id=takeoutProduct.getId();
    }

    private void initPropertyGV(String property) {
        String[] propertyArr = property.split(",");
        propertyList = new ArrayList<>();
        for (String pro : propertyArr){
            propertyList.add(pro);
        }
        EditProductPropertyAdapter adapter = new EditProductPropertyAdapter(EditProductActivity.this, propertyList);
        propertyGV.setAdapter(adapter);
    }

    private void initCategorySpinner(List<TakeoutCategory> categoryList) {
        final CategoryAdapter adapter = new CategoryAdapter(EditProductActivity.this, categoryList);
        categorySpinner.setAdapter(adapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TakeoutCategory tc = (TakeoutCategory)adapter.getItem(i);
                categoryId=tc.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @OnClick({R.id.save_but,R.id.cancel_but})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.save_but:
                try {
                    saveProduct();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.cancel_but:
                AllActivitiesHolder.removeAct(EditProductActivity.this);
                break;
        }
    }

    private void saveProduct() throws FileNotFoundException, JSONException {
        RequestParams params = AsynClient.getRequestParams();
        params.put("takeoutProductJOStr", initProductParamsJOStr());
        params.put("takeoutProductPropertyJAStr",initProductPropertyJAStr());
        params.put("imgFile", new File("/mnt/m_external_sd/DCIM/Camera/zhoukaixiang.jpg"));

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

    private String initProductPropertyJAStr() throws JSONException {
        JSONArray ja = new JSONArray();
        propertyList.size();
        for(String property : propertyList){
            JSONObject jo = new JSONObject();
            jo.put("propertyE",property);
            ja.put(jo);
        }
        return ja.toString();
    }

    private String initProductParamsJOStr() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("productName",productNameET.getText().toString());
        jo.put("categoryId",categoryId);
        jo.put("description  ",descriptionET.getText().toString());
        jo.put("price",priceET.getText().toString());
        jo.put("priceCanhe",priceCanheET.getText().toString());
        jo.put("inventory",inventoryET.getText().toString());
        jo.put("inventoryCount",inventoryCountET.getText().toString());
        jo.put("integral",integralET.getText().toString());
        jo.put("id",id);
        return jo.toString();
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
