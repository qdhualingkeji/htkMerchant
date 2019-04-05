package com.hualing.htk_merchant.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.adapter.AddProductPropertyAdapter;
import com.hualing.htk_merchant.adapter.CategoryAdapter;
import com.hualing.htk_merchant.adapter.PriceInventoryAdapter;
import com.hualing.htk_merchant.entity.LoginUserEntity;
import com.hualing.htk_merchant.entity.SuccessEntity;
import com.hualing.htk_merchant.entity.TakeoutCategoryEntity;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.model.ProductProperty;
import com.hualing.htk_merchant.model.TakeoutCategory;
import com.hualing.htk_merchant.model.TakeoutProduct;
import com.hualing.htk_merchant.util.AllActivitiesHolder;
import com.hualing.htk_merchant.util.ImageUtil;
import com.hualing.htk_merchant.util.IntentUtil;
import com.hualing.htk_merchant.util.SharedPreferenceUtil;
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
    private CategoryAdapter categoryAdapter;
    @BindView(R.id.category_spinner)
    Spinner categorySpinner;
    @BindView(R.id.imgUrl_sdv)
    SimpleDraweeView imgUrlSDV;
    private List<TakeoutProduct> priceInventoryList;
    private PriceInventoryAdapter priceInventoryAdapter;
    @BindView(R.id.priceInventory_lv)
    ListView priceInventoryLV;
    private List<ProductProperty> propertyList;
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
    private String tempPhotoPath;
    private boolean reload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {

        Log.e("zzzzzzzz",""+GlobalData.userID);
        reload = getIntent().getBooleanExtra("reload", false);
        if(GlobalData.userID==null){
            toLogin();
        }
        else{
            initData();
        }

        if(reload) {
            Log.e("bbbbbbbb", "bbbbbbbbbb");
            showLoadingDialog(AddProductActivity.this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initProductData();
                }
            }, 3000);
        }
    }


    private void initProductData() {
        try {
            String productParamsJOStr = getIntent().getStringExtra("productParamsJOStr");
            JSONObject productJO = new JSONObject(productParamsJOStr);
            productNameET.setText(productJO.getString("productName"));
            categoryId= productJO.getInt("categoryId");
            for (int i=0;i<categoryAdapter.getCount();i++) {
                TakeoutCategory tc = (TakeoutCategory) categoryAdapter.getItem(i);
                if(tc.getId()==categoryId){
                    categorySpinner.setSelection(i);
                    break;
                }
            }
            descriptionET.setText(productJO.getString("description"));
            integralET.setText(String.valueOf(productJO.getInt("integral")));

            tempPhotoPath=getIntent().getStringExtra("tempPhotoPath");
            Bitmap bm = BitmapFactory.decodeFile(tempPhotoPath);
            imgUrlSDV.setImageBitmap(bm);
            imgUrlSDV.setTag(tempPhotoPath);

            String productParamsJAStr = getIntent().getStringExtra("productParamsJAStr");
            JSONArray productJA = new JSONArray(productParamsJAStr);
            for (int i=0;i<productJA.length();i++){
                TakeoutProduct priceInventory=new TakeoutProduct();
                JSONObject priceInventoryJO = (JSONObject)productJA.get(i);
                priceInventory.setPrice(priceInventoryJO.getDouble("price"));
                priceInventory.setPriceCanhe(priceInventoryJO.getDouble("priceCanhe"));
                priceInventory.setInventory(priceInventoryJO.getInt("inventory"));
                priceInventory.setInventoryCount(priceInventoryJO.getInt("inventoryCount"));
                priceInventoryList.add(priceInventory);
            }
            priceInventoryAdapter.notifyDataSetChanged();

            String productPropertyJAStr = getIntent().getStringExtra("productPropertyJAStr");
            Log.e("productPropertyJAStr===",""+productPropertyJAStr);
            JSONArray productPropertyJA = new JSONArray(productPropertyJAStr);
            for(int i=0;i<productPropertyJA.length();i++){
                ProductProperty productProperty=new ProductProperty();
                JSONObject propertyJO = (JSONObject)productPropertyJA.get(i);
                productProperty.setPropertyName(propertyJO.getString("propertyE"));
                propertyList.add(productProperty);
            }
            addProductPropertyAdapter.notifyDataSetChanged();;
            hideLoadingDialog();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void toLogin(){
        Log.e("aaaaaaaaaa","aaaaaaaaaa");
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

                    initData();
                }
                else {
                    showMessage(loginUserEntity.getMessage());
                }
            }
        });
    }

    private void initData(){
        initCategorySpinner();
        initPriceInventory();
        initPropertyGV();
    }

    private void initPropertyGV() {
        propertyList = new ArrayList<ProductProperty>();
        addProductPropertyAdapter = new AddProductPropertyAdapter(AddProductActivity.this, propertyList);
        if(!reload) {
            addProductPropertyAdapter.addProperty();
            addProductPropertyAdapter.addProperty();
        }
        propertyGV.setAdapter(addProductPropertyAdapter);
    }

    private void initPriceInventory() {
        priceInventoryList=new ArrayList<TakeoutProduct>();
        priceInventoryAdapter = new PriceInventoryAdapter(AddProductActivity.this, priceInventoryList);
        if(!reload)
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
                    categoryAdapter = new CategoryAdapter(AddProductActivity.this, categoryList);
                    categorySpinner.setAdapter(categoryAdapter);
                    categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            TakeoutCategory tc = (TakeoutCategory)categoryAdapter.getItem(i);
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

    @OnClick({R.id.addGG_but,R.id.addPro_but,R.id.imgUrl_sdv,R.id.save_but})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.addGG_but:
                addGG();
                break;
            case R.id.addPro_but:
                addProperty();
                break;
            case R.id.imgUrl_sdv:
                try {
                    Intent intent=new Intent(AddProductActivity.this,UploadPhotoActivity.class);
                    intent.putExtra("productParamsJOStr", initProductParamsJOStr());
                    intent.putExtra("productParamsJAStr", initProductParamsJAStr());
                    intent.putExtra("productPropertyJAStr",initProductPropertyJAStr());
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍摄图片返回情况
        if(resultCode==RESULT_OK){
            if(requestCode==ImageUtil.FROMALBUM)
                tempPhotoPath=ImageUtil.getPhotoPath(data,AddProductActivity.this,ImageUtil.FROMALBUM);
            else if(requestCode==ImageUtil.FROMTAKE)
                tempPhotoPath=ImageUtil.getPhotoPath(data,AddProductActivity.this,ImageUtil.FROMTAKE);
            Bitmap bm = BitmapFactory.decodeFile(tempPhotoPath);
            imgUrlSDV.setImageBitmap(bm);
            imgUrlSDV.setTag(tempPhotoPath);
        }
    }
    */

    private void addProduct() throws JSONException, FileNotFoundException {
        RequestParams params = AsynClient.getRequestParams();
        params.put("takeoutProductJOStr", initProductParamsJOStr());
        params.put("takeoutProductJAStr", initProductParamsJAStr());
        params.put("takeoutProductPropertyJAStr",initProductPropertyJAStr());

        //params.put("imgFile", new File("/mnt/m_external_sd/DCIM/Camera/zhoukaixiang.jpg"));
        params.put("imgFile", new File(imgUrlSDV.getTag().toString()));
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

                Gson gson = new Gson();
                SuccessEntity successEntity = gson.fromJson(rawJsonResponse, SuccessEntity.class);
                if (successEntity.getCode() == 100){
                    showMessage(successEntity.getMessage());

                    Intent intent = new Intent(AddProductActivity.this, MainActivity.class);
                    startActivity(intent);
                    AllActivitiesHolder.removeAct(AddProductActivity.this);
                }
            }
        });
    }

    private String initProductPropertyJAStr() throws JSONException {
        JSONArray ja = new JSONArray();
        for(ProductProperty property : propertyList){
            JSONObject jo = new JSONObject();
            jo.put("propertyE",property.getPropertyName());
            ja.put(jo);
        }
        return ja.toString();
    }

    private String initProductParamsJAStr() throws JSONException {
        JSONArray ja = new JSONArray();
        for (TakeoutProduct priceInventory:priceInventoryList){
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
        jo.put("description",descriptionET.getText().toString());
        String integralStr = integralET.getText().toString();
        jo.put("integral",Integer.valueOf(TextUtils.isEmpty(integralStr)?"0":integralStr));
        jo.put("time","all,");//全时段售卖
        return jo.toString();
    }

    private void addProperty() {

        List<ProductProperty> mData = addProductPropertyAdapter.getmData();
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
