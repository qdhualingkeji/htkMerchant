package com.hualing.htk_merchant.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import com.hualing.htk_merchant.widget.PropertyGridView;
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
    private List<ProductProperty> propertyList;
    private AddProductPropertyAdapter addProductPropertyAdapter;
    @BindView(R.id.property_gv)
    PropertyGridView propertyGV;
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
    @BindView(R.id.addPro_but)
    public Button addProBut;
    private Integer categoryId;
    private String tempPhotoPath;
    private boolean reload;
    private UploadTypeOnClick uploadTypeOnClick=new UploadTypeOnClick(0);

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
            showLoadingDialog(AddProductActivity.this,"加载中");
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
            priceET.setText(String.valueOf(productJO.getDouble("price")));
            priceCanheET.setText(String.valueOf(productJO.getDouble("priceCanhe")));
            inventoryET.setText(String.valueOf(productJO.getInt("inventory")));
            inventoryCountET.setText(String.valueOf(productJO.getInt("inventoryCount")));

            tempPhotoPath=getIntent().getStringExtra("tempPhotoPath");
            if(TextUtils.isEmpty(tempPhotoPath)) {
                imgUrlSDV.setImageResource(R.drawable.uppic);
            }
            else {
                Bitmap bm = BitmapFactory.decodeFile(tempPhotoPath);
                imgUrlSDV.setImageBitmap(bm);
                imgUrlSDV.setTag(tempPhotoPath);
            }

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

    @OnClick({R.id.addPro_but,R.id.imgUrl_sdv,R.id.save_but})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.addPro_but:
                addProperty();
                break;
            case R.id.imgUrl_sdv:
                uploadPhoto();
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


    private void uploadPhoto() {
        String[] items={"从相册上传","拍照上传"};
        new AlertDialog.Builder(AddProductActivity.this)
                .setTitle("请选择上传方式")
                .setSingleChoiceItems(items, 0, uploadTypeOnClick)
                .setPositiveButton("确定", uploadTypeOnClick)
                .setNegativeButton("取消", uploadTypeOnClick)
                .show();
    }

    /**
     * 选择上传图片方式的监听类
     * **/
    class UploadTypeOnClick implements DialogInterface.OnClickListener{
        private int index;
        public UploadTypeOnClick(int index){
            this.index=index;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            if(which>=0)
                index=which;
            else if(which==DialogInterface.BUTTON_POSITIVE){
                try {
                    Intent intent=new Intent(AddProductActivity.this,UploadPhotoActivity.class);
                    intent.putExtra("productParamsJOStr", initProductParamsJOStr());
                    intent.putExtra("productPropertyJAStr",initProductPropertyJAStr());
                    intent.putExtra("activityFrom",ImageUtil.ADDFROM);
                    intent.putExtra("uploadFrom",index);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                index=0;
            }
            else if(which==DialogInterface.BUTTON_NEGATIVE)
                showMessage("你选择了取消操作");
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
        params.put("takeoutProductPropertyJAStr",initProductPropertyJAStr());

        //params.put("imgFile", new File("/mnt/m_external_sd/DCIM/Camera/zhoukaixiang.jpg"));
        Object imgUrlTag = imgUrlSDV.getTag();
        if(imgUrlTag!=null)
            params.put("imgFile", new File(imgUrlTag.toString()));
        params.put("userId", GlobalData.userID);

        showLoadingDialog(AddProductActivity.this,"上传中");
        AsynClient.post(MyHttpConfing.addProduct, AddProductActivity.this, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("rawJsonData======",""+rawJsonData);
                hideLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("rawJsonResponse======",""+rawJsonResponse);

                Gson gson = new Gson();
                SuccessEntity successEntity = gson.fromJson(rawJsonResponse, SuccessEntity.class);
                if (successEntity.getCode() == 100){
                    showMessage(successEntity.getMessage());

                    Intent intent = new Intent(AddProductActivity.this, ProductListActivity.class);
                    startActivity(intent);
                    AllActivitiesHolder.removeAct(AddProductActivity.this);
                    hideLoadingDialog();
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

    private String initProductParamsJOStr() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("productName",productNameET.getText().toString());
        jo.put("categoryId",categoryId);
        jo.put("description",descriptionET.getText().toString());
        String priceStr = priceET.getText().toString();
        if(TextUtils.isEmpty(priceStr))
            priceStr="0.0";
        jo.put("price",priceStr);
        String priceCanheStr = priceCanheET.getText().toString();
        if(TextUtils.isEmpty(priceCanheStr))
            priceCanheStr="0.0";
        jo.put("priceCanhe",priceCanheStr);
        String inventoryStr = inventoryET.getText().toString();
        if(TextUtils.isEmpty(inventoryStr))
            inventoryStr="0";
        jo.put("inventory",inventoryStr);
        String inventoryCountStr = inventoryCountET.getText().toString();
        if(TextUtils.isEmpty(inventoryCountStr))
            inventoryCountStr="0";
        jo.put("inventoryCount",inventoryCountStr);
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
}
