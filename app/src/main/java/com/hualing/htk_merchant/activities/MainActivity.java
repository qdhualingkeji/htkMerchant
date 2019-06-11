package com.hualing.htk_merchant.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.adapter.DeliveryAdapter;
import com.hualing.htk_merchant.adapter.FinishedAdapter;
import com.hualing.htk_merchant.adapter.MyPagerAdapter;
import com.hualing.htk_merchant.adapter.NewOrderAdapter;
import com.hualing.htk_merchant.aframework.yoni.YoniClient;
import com.hualing.htk_merchant.entity.LoginUserEntity;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.global.TheApplication;
import com.hualing.htk_merchant.util.AllActivitiesHolder;
import com.hualing.htk_merchant.util.IntentUtil;
import com.hualing.htk_merchant.util.JPushUtil;
import com.hualing.htk_merchant.util.SharedPreferenceUtil;
import com.hualing.htk_merchant.utils.AsynClient;
import com.hualing.htk_merchant.utils.GsonHttpResponseHandler;
import com.hualing.htk_merchant.utils.ImageLoadManager;
import com.hualing.htk_merchant.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

public class MainActivity extends BaseActivity {

    @BindView(R.id.shopName_tv)
    TextView mShopNameTV;
    @BindView(R.id.state_tv)
    TextView mStateTV;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.toolBar)
    Toolbar mToolBar;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.dot1)
    Button mDot1;
    @BindView(R.id.dot2)
    Button mDot2;
    @BindView(R.id.dot3)
    Button mDot3;
    @BindView(R.id.portrait)
    SimpleDraweeView mPortrait;
    private MyPagerAdapter mPagerAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    // 要申请的权限
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private AlertDialog dialog;

    public ViewPager getmViewPager() {
        return mViewPager;
    }

    public void setmViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 敲黑板:黑代码解决Android 7.0 调用系统通知无法播放声音的问题
         */
        grantUriPermission("com.android.systemui", Uri.parse("android.resource://" + getPackageName() + "/" +R.raw.notification),
                Intent.FLAG_GRANT_READ_URI_PERMISSION);


        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                showDialogTipUserRequestPermission();
            }
        }
    }

     // 提示用户该请求权限的弹出框
     private void showDialogTipUserRequestPermission() {
         new AlertDialog.Builder(this)
        .setTitle("存储权限不可用")
        .setMessage("由于支付宝需要获取存储空间，为你存储个人信息；\n否则，您将无法正常使用支付宝")
        .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 startRequestPermission();
             }
         })
         .setNegativeButton("取消", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                  finish();
              }
          }).setCancelable(false).show();
     }

      // 开始提交请求权限
      private void startRequestPermission() {
         ActivityCompat.requestPermissions(this, permissions, 321);
      }

      // 用户权限 申请 的回调方法
      @Override
      public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if (requestCode == 321) {
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                 if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                     // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                     boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                     if (!b) {
                             // 用户还是想用我的 APP 的
                             // 提示用户去应用设置界面手动开启权限
                             showDialogTipUserGoToAppSettting();
                         } else
                             finish();
                 } else {
                     showMessage("权限获取成功");
                 }
             }
         }
     }

     // 提示用户去应用设置界面手动开启权限
      private void showDialogTipUserGoToAppSettting() {
         dialog = new AlertDialog.Builder(this)
         .setTitle("存储权限不可用")
         .setMessage("请在-应用设置-权限-中，允许支付宝使用存储权限来保存用户数据")
         .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                 // 跳转到应用设置界面
                 goToAppSetting();
             }
          })
         .setNegativeButton("取消", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 finish();
             }
          })
          .setCancelable(false).show();
     }

     // 跳转到当前应用的设置界面
     private void goToAppSetting() {
         Intent intent = new Intent();
         intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
         Uri uri = Uri.fromParts("package", getPackageName(), null);
         intent.setData(uri);
         startActivityForResult(intent, 123);
     }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if (requestCode == 123) {
             if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                 // 检查该权限是否已经获取
                 int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                 // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                 if (i != PackageManager.PERMISSION_GRANTED) {
                     // 提示用户应该去应用设置界面手动开启权限
                     showDialogTipUserGoToAppSettting();
                 }
                 else {
                     if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                         }
                     showMessage("权限获取成功");
                 }
             }
         }
    }

    @Override
    protected void initLogic() {
        if(GlobalData.userID==null){
            toLogin();
        }
        else{
            initData();
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
        getScreenSize();
        initMyPagerAdapter();
        mToolBar.setTitle(getResources().getString(R.string.app_name));//设置Toolbar标题
        mToolBar.setTitleTextColor(Color.parseColor("#ffffff")); //设置标题颜色
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int width = (int) (TheApplication.getScreenWidth()/3);
        int height=100;
        mDot1.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        mDot2.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        mDot3.setLayoutParams(new LinearLayout.LayoutParams(width,height));

        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolBar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        //ImageLoadManager.getInstance().setImage("http://img1.imgtn.bdimg.com/it/u=1282290067,2091746976&fm=26&gp=0.jpg", mPortrait);
        Uri uri = Uri.parse("http://120.27.5.36:8080/htkApp/"+GlobalData.avatarImg);
        mPortrait.setImageURI(uri);
        mShopNameTV.setText(GlobalData.shopName);
        mStateTV.setText(GlobalData.state==1?"营业中":"休息中");
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.dot1,R.id.dot2,R.id.dot3,R.id.goAddProduct_layout,R.id.goProductList_layout,R.id.goBillRecord_layout,R.id.goBalance_layout,R.id.exit_but})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dot1:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.dot2:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.dot3:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.goAddProduct_layout:
                IntentUtil.openActivity(this, AddProductActivity.class);
                break;
            case R.id.goProductList_layout:
                IntentUtil.openActivity(this, ProductListActivity.class);
                break;
            case R.id.goBillRecord_layout:
                IntentUtil.openActivity(this, BillRecordActivity.class);
                break;
            case R.id.goBalance_layout:
                IntentUtil.openActivity(this, BalanceActivity.class);
                break;
            case R.id.exit_but:
                GlobalData.userID = 0;
                //之后获取和用户相关的服务就不需要额外传userId了
                YoniClient.getInstance().setUser(GlobalData.userID+"");
                //清除本地密码
                SharedPreferenceUtil.logout();
                AllActivitiesHolder.finishAllAct();
                IntentUtil.openActivity(this, LoginActivity.class);
                break;
        }
    }

    /**
     * 改变选卡
     * */
    private void changeDot(int position){
        //首先全部置为未选中
        mDot1.setSelected(false);
        mDot2.setSelected(false);
        mDot3.setSelected(false);
        //其次单独设置选中的
        switch (position) {
            case 0:
                Log.e("position1========",""+position);
                NewOrderAdapter newOrderAdapter = mPagerAdapter.getmAdapter1();
                newOrderAdapter.setNewData();
                newOrderAdapter.notifyDataSetChanged();
                mDot1.setSelected(true);
                break;
            case 1:
                Log.e("position2========",""+position);
                DeliveryAdapter deliveryAdapter = mPagerAdapter.getmAdapter2();
                deliveryAdapter.setNewData();
                deliveryAdapter.notifyDataSetChanged();
                mDot2.setSelected(true);
                break;
            case 2:
                Log.e("position3========",""+position);
                FinishedAdapter finishedAdapter = mPagerAdapter.getmAdapter3();
                finishedAdapter.setNewData();
                finishedAdapter.notifyDataSetChanged();
                mDot3.setSelected(true);
                break;
        }
    }

    private void getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        TheApplication.setScreenHeight(display.getHeight());
        TheApplication.setScreenWidth(display.getWidth());
    }

    public void initMyPagerAdapter(){
        mPagerAdapter = new MyPagerAdapter(MainActivity.this,mDot1,mDot2,mDot3);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mDot1.setSelected(true);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            //totalHeight += listItem.getMeasuredHeight();
            totalHeight += 80;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        //params.height = 400;
        listView.setLayoutParams(params);
    }

}
