package com.hualing.htk_merchant.activities;

import android.os.Bundle;
import android.util.Log;

import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.util.ImageUtil;

public class UploadPhoneActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        ImageUtil upload = new ImageUtil();
        //File file = new File("/mnt/m_external_sd/DCIM/Camera/zhoukaixiang.jpg");
        boolean bool = upload.upLoading("/mnt/m_external_sd/DCIM/Camera/zhoukaixiang.jpg", "D:/Resource/htkApp/upload/shop/takeout/", "aaaaa.jpg");
        Log.e("bool===",""+bool);
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
}
