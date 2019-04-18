package com.hualing.htk_merchant.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Spinner;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.adapter.CategoryAdapter;
import com.hualing.htk_merchant.adapter.EditProductPropertyAdapter;
import com.hualing.htk_merchant.entity.LoginUserEntity;
import com.hualing.htk_merchant.entity.ProductDetailEntity;
import com.hualing.htk_merchant.entity.SuccessEntity;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.model.ProductProperty;
import com.hualing.htk_merchant.model.TakeoutCategory;
import com.hualing.htk_merchant.model.TakeoutProduct;
import com.hualing.htk_merchant.util.AllActivitiesHolder;
import com.hualing.htk_merchant.util.ImageUtil;
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

public class EditProductActivity extends BaseActivity {

    @BindView(R.id.productName_et)
    EditText productNameET;
    private CategoryAdapter categoryAdapter;
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
    private List<ProductProperty> propertyList;
    private EditProductPropertyAdapter editProductPropertyAdapter;
    @BindView(R.id.property_gv)
    PropertyGridView propertyGV;
    @BindView(R.id.integral_et)
    EditText integralET;
    private Integer id;
    private Integer categoryId;
    private File imgFile;
    private String tempPhotoPath;
    private boolean reload;
    private UploadTypeOnClick uploadTypeOnClick=new UploadTypeOnClick(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        if(GlobalData.userID==null){
            toLogin();
        }
        else{
            initData();
        }

        reload = getIntent().getBooleanExtra("reload", false);
        if(reload){
            showLoadingDialog(EditProductActivity.this,"加载中");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initProductData();
                }
            },3000);
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
            id=productJO.getInt("id");

            tempPhotoPath=getIntent().getStringExtra("tempPhotoPath");
            if(!TextUtils.isEmpty(tempPhotoPath)) {
                Bitmap bm = BitmapFactory.decodeFile(tempPhotoPath);
                imgUrlSDV.setImageBitmap(bm);
                imgUrlSDV.setTag(tempPhotoPath);
            }

            propertyList.clear();
            String productPropertyJAStr = getIntent().getStringExtra("productPropertyJAStr");
            Log.e("productPropertyJAStr===",""+productPropertyJAStr);
            JSONArray productPropertyJA = new JSONArray(productPropertyJAStr);
            for(int i=0;i<productPropertyJA.length();i++){
                ProductProperty productProperty=new ProductProperty();
                JSONObject propertyJO = (JSONObject)productPropertyJA.get(i);
                productProperty.setPropertyName(propertyJO.getString("propertyE"));
                propertyList.add(productProperty);
            }
            editProductPropertyAdapter.notifyDataSetChanged();;
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

    private void initData() {
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
                    TakeoutProduct takeoutProduct = productDetail.getDataPro();
                    productNameET.setText(takeoutProduct.getProductName());
                    initCategorySpinner(productDetail.getData());
                    Uri uri = Uri.parse(takeoutProduct.getImgUrl());
                    imgUrlSDV.setImageURI(uri);
                    descriptionET.setText(takeoutProduct.getDescription());
                    priceET.setText(String.valueOf(takeoutProduct.getPrice()));
                    priceCanheET.setText(String.valueOf(takeoutProduct.getPriceCanhe()));
                    inventoryET.setText(String.valueOf(takeoutProduct.getInventory()));
                    inventoryCountET.setText(String.valueOf(takeoutProduct.getInventoryCount()));
                    initPropertyGV(takeoutProduct.getProperty());
                    integralET.setText(String.valueOf(takeoutProduct.getIntegral()));
                    id=takeoutProduct.getId();
                }
                else{
                    showMessage(productDetailEntity.getMessage());
                }
            }
        });
    }

    private void initPropertyGV(String property) {
        String[] propertyArr = property.split(",");
        propertyList = new ArrayList<ProductProperty>();
        for (String pro : propertyArr){
            ProductProperty pp=new ProductProperty();
            pp.setPropertyName(pro);
            propertyList.add(pp);
        }
        editProductPropertyAdapter = new EditProductPropertyAdapter(EditProductActivity.this, propertyList);
        propertyGV.setAdapter(editProductPropertyAdapter);
    }

    private void initCategorySpinner(List<TakeoutCategory> categoryList) {
        categoryAdapter = new CategoryAdapter(EditProductActivity.this, categoryList);
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

    @OnClick({R.id.imgUrl_sdv,R.id.save_but,R.id.cancel_but})
    public void onViewClicked(View v) {
        switch (v.getId()){
            case R.id.imgUrl_sdv:
                uploadPhoto();
                break;
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

    private void uploadPhoto() {
        String[] items={"从相册上传","拍照上传"};
        new AlertDialog.Builder(EditProductActivity.this)
                .setTitle("请选择上传方式")
                .setSingleChoiceItems(items,0,uploadTypeOnClick)
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
                    Intent intent=new Intent(EditProductActivity.this,UploadPhotoActivity.class);
                    intent.putExtra("productParamsJOStr", initProductParamsJOStr());
                    intent.putExtra("productPropertyJAStr",initProductPropertyJAStr());
                    intent.putExtra("productId",id);
                    intent.putExtra("activityFrom",ImageUtil.EDITFROM);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍摄图片返回情况
        if(resultCode==RESULT_OK){
            if(requestCode==ImageUtil.FROMALBUM)
                tempPhotoPath=ImageUtil.getPhotoPath(data,EditProductActivity.this,ImageUtil.FROMALBUM);
            else if(requestCode==ImageUtil.FROMTAKE)
                tempPhotoPath=ImageUtil.getPhotoPath(data,EditProductActivity.this,ImageUtil.FROMTAKE);
            Bitmap bm = BitmapFactory.decodeFile(tempPhotoPath);
            imgUrlSDV.setImageBitmap(bm);
            imgUrlSDV.setTag(tempPhotoPath);
        }
    }

    private void saveProduct() throws FileNotFoundException, JSONException {
        RequestParams params = AsynClient.getRequestParams();
        params.put("takeoutProductJOStr", initProductParamsJOStr());
        params.put("takeoutProductPropertyJAStr",initProductPropertyJAStr());
        Object imgUrlTag = imgUrlSDV.getTag();
        if(imgUrlTag!=null)
            params.put("imgFile", new File(imgUrlTag.toString()));

        showLoadingDialog(EditProductActivity.this,"上传中");
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

                Gson gson = new Gson();
                SuccessEntity successEntity = gson.fromJson(rawJsonResponse, SuccessEntity.class);
                if (successEntity.getCode() == 100){
                    showMessage(successEntity.getMessage());

                    Intent intent = new Intent(EditProductActivity.this, ProductListActivity.class);
                    startActivity(intent);
                    AllActivitiesHolder.removeAct(EditProductActivity.this);
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
        jo.put("price",priceET.getText().toString());
        jo.put("priceCanhe",priceCanheET.getText().toString());
        jo.put("inventory",inventoryET.getText().toString());
        jo.put("inventoryCount",inventoryCountET.getText().toString());
        String integralStr = integralET.getText().toString();
        jo.put("integral",Integer.valueOf(TextUtils.isEmpty(integralStr)?"0":integralStr));
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
