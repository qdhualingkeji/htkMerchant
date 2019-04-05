package com.hualing.htk_merchant.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.util.AllActivitiesHolder;
import com.hualing.htk_merchant.util.ImageUtil;

import butterknife.OnClick;

public class UploadPhotoActivity extends BaseActivity {

    private UploadTypeOnClick uploadTypeOnClick=new UploadTypeOnClick(0);
    private String productParamsJOStr;
    private String productParamsJAStr;
    private String productPropertyJAStr;
    private String tempPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        /*
        ImageUtil upload = new ImageUtil();
        //File file = new File("/mnt/m_external_sd/DCIM/Camera/zhoukaixiang.jpg");
        boolean bool = upload.upLoading("/mnt/m_external_sd/DCIM/Camera/zhoukaixiang.jpg", "D:/Resource/htkApp/upload/shop/takeout/", "aaaaa.jpg");
        Log.e("bool===",""+bool);
        */

    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_upload_phone;
    }

    @OnClick({R.id.uploadPhoto_but})
    public void onViewClicked(View v){
        switch (v.getId()){
            case R.id.uploadPhoto_but:
                uploadPhoto();
                break;
        }
    }

    private void uploadPhoto() {
        String[] items={"从相册上传","拍照上传"};
        new AlertDialog.Builder(UploadPhotoActivity.this)
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
                switch (index) {
                    case ImageUtil.FROMALBUM:
                        uploadFromAlbum();
                        index=0;
                        break;
                    case ImageUtil.FROMTAKE:
                        uploadFromTake();
                        index=0;
                        break;
                }
            }
            else if(which==DialogInterface.BUTTON_NEGATIVE)
                showMessage("你选择了取消操作");
        }

    }

    /**
     * 从相册上传
     * **/
    public void uploadFromAlbum() {
        // TODO Auto-generated method stub
        Intent intent=new Intent();
        intent.setType("image/");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, ImageUtil.FROMALBUM);
    }

    /**
     * 拍照上传
     * **/
    public void uploadFromTake() {
        // TODO Auto-generated method stub
        //下面是调用系统的照相机拍照
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //启动拍照设备，等待处理拍照结果
        startActivityForResult(intent,  ImageUtil.FROMTAKE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍摄图片返回情况
        if(resultCode==RESULT_OK){
            if(requestCode==ImageUtil.FROMALBUM)
                tempPhotoPath=ImageUtil.getPhotoPath(data,UploadPhotoActivity.this,ImageUtil.FROMALBUM);
            else if(requestCode==ImageUtil.FROMTAKE)
                tempPhotoPath=ImageUtil.getPhotoPath(data,UploadPhotoActivity.this,ImageUtil.FROMTAKE);
            Bitmap bm = BitmapFactory.decodeFile(tempPhotoPath);

            Intent intent=new Intent(UploadPhotoActivity.this,AddProductActivity.class);
            productParamsJOStr = getIntent().getStringExtra("productParamsJOStr");
            productParamsJAStr = getIntent().getStringExtra("productParamsJAStr");
            productPropertyJAStr = getIntent().getStringExtra("productPropertyJAStr");
            intent.putExtra("productParamsJOStr", productParamsJOStr);
            intent.putExtra("productParamsJAStr", productParamsJAStr);
            intent.putExtra("productPropertyJAStr", productPropertyJAStr);
            intent.putExtra("tempPhotoPath", tempPhotoPath);
            intent.putExtra("reload",true);
            startActivity(intent);
            finish();
        }
    }
}
