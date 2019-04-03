package com.hualing.htk_merchant.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.adapter.AddProductPropertyAdapter;
import com.hualing.htk_merchant.adapter.CategoryAdapter;
import com.hualing.htk_merchant.adapter.PriceInventoryAdapter;
import com.hualing.htk_merchant.entity.TakeoutCategoryEntity;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.model.TakeoutCategory;
import com.hualing.htk_merchant.model.TakeoutProduct;
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

public class AddProductActivity extends BaseActivity {

    @BindView(R.id.productName_et)
    EditText productNameET;
    @BindView(R.id.category_spinner)
    Spinner categorySpinner;
    private List<TakeoutProduct> priceInventoryList;
    private PriceInventoryAdapter priceInventoryAdapter;
    @BindView(R.id.priceInventory_lv)
    ListView priceInventoryLV;
    private List<String> propertyList;
    private AddProductPropertyAdapter addProductPropertyAdapter;
    @BindView(R.id.property_gv)
    GridView propertyGV;
    @BindView(R.id.description_et)
    EditText descriptionET;
    @BindView(R.id.integral_et)
    EditText integralET;
    @BindView(R.id.addGG_but)
    public Button addGGBut;
    @BindView(R.id.addPro_but)
    public Button addProBut;
    private Integer categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        initCategorySpinner();
        initPriceInventory();
        initPropertyGV();
    }

    private void initPropertyGV() {
        propertyList = new ArrayList<>();
        addProductPropertyAdapter = new AddProductPropertyAdapter(AddProductActivity.this, propertyList);
        addProductPropertyAdapter.addProperty();
        addProductPropertyAdapter.addProperty();
        propertyGV.setAdapter(addProductPropertyAdapter);
    }

    private void initPriceInventory() {
        priceInventoryList=new ArrayList<TakeoutProduct>();
        priceInventoryAdapter = new PriceInventoryAdapter(AddProductActivity.this, priceInventoryList);
        priceInventoryAdapter.addGG();
        priceInventoryLV.setAdapter(priceInventoryAdapter);
    }

    private void initCategorySpinner() {
        RequestParams params = AsynClient.getRequestParams();
        params.put("userId", GlobalData.userID);

        AsynClient.post(MyHttpConfing.getCategoryListById, AddProductActivity.this, params, new GsonHttpResponseHandler() {
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
                TakeoutCategoryEntity takeoutCategoryEntity = gson.fromJson(rawJsonResponse, TakeoutCategoryEntity.class);
                if (takeoutCategoryEntity.getCode() == 100) {
                    List<TakeoutCategoryEntity.DataBean> tcList = takeoutCategoryEntity.getData();
                    List<TakeoutCategory> categoryList = new ArrayList<TakeoutCategory>();
                    for (TakeoutCategoryEntity.DataBean tc : tcList){
                        TakeoutCategory category = new TakeoutCategory();
                        category.setId(tc.getId());
                        category.setCategoryName(tc.getCategoryName());
                        categoryList.add(category);
                    }
                    final CategoryAdapter adapter = new CategoryAdapter(AddProductActivity.this, categoryList);
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
                else{
                    showMessage(takeoutCategoryEntity.getMessage());
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
        return R.layout.activity_add_product;
    }

    @OnClick({R.id.addGG_but,R.id.addPro_but,R.id.save_but})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.addGG_but:
                addGG();
                break;
            case R.id.addPro_but:
                addProperty();
                break;
            case R.id.save_but:
                try {
                    addProduct();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void addProduct() throws JSONException, FileNotFoundException {
        RequestParams params = AsynClient.getRequestParams();
        params.put("takeoutProductJOStr", initProductParamsJOStr());
        params.put("takeoutProductJAStr", initProductParamsJAStr());
        params.put("takeoutProductPropertyJAStr",initProductPropertyJAStr());
        params.put("imgFile", new File("/mnt/m_external_sd/DCIM/Camera/zhoukaixiang.jpg"));
        params.put("userId", GlobalData.userID);

        AsynClient.post(MyHttpConfing.addProduct, AddProductActivity.this, params, new GsonHttpResponseHandler() {
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

    private String initProductParamsJAStr() throws JSONException {
        JSONArray ja = new JSONArray();
        for (TakeoutProduct priceInventory : priceInventoryList){
            JSONObject jo = new JSONObject();
            jo.put("price",priceInventory.getPrice());
            jo.put("priceCanhe",priceInventory.getPriceCanhe());
            jo.put("inventory",priceInventory.getInventory());
            jo.put("inventoryCount",priceInventory.getInventoryCount());
            ja.put(jo);
        }
        return ja.toString();
    }

    private String initProductParamsJOStr() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("productName",productNameET.getText().toString());
        jo.put("categoryId",categoryId);
        jo.put("description  ",descriptionET.getText().toString());
        jo.put("integral",integralET.getText().toString());
        return jo.toString();
    }

    private void addProperty() {

        List<String> mData = addProductPropertyAdapter.getmData();
        int size = mData.size();
        if(size<5) {
            addProductPropertyAdapter.addProperty();
        }
        if(size>=4) {
            addProBut.setVisibility(Button.GONE);
        }
    }

    private void addGG() {
        List<TakeoutProduct> mData = priceInventoryAdapter.getmData();
        int size = mData.size();
        if(size<6) {
            priceInventoryAdapter.addGG();
        }
        if(size>=5) {
            addGGBut.setVisibility(Button.GONE);
        }
    }
}
