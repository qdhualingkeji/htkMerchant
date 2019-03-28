package com.hualing.htk_merchant.activities;

import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.adapter.DeliveryAdapter;
import com.hualing.htk_merchant.adapter.FinishedAdapter;
import com.hualing.htk_merchant.adapter.MyPagerAdapter;
import com.hualing.htk_merchant.adapter.NewOrderAdapter;
import com.hualing.htk_merchant.aframework.yoni.YoniClient;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.global.TheApplication;
import com.hualing.htk_merchant.util.AllActivitiesHolder;
import com.hualing.htk_merchant.util.IntentUtil;
import com.hualing.htk_merchant.util.SharedPreferenceUtil;
import com.hualing.htk_merchant.utils.ImageLoadManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
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

    @OnClick({R.id.exit_but,R.id.dot1,R.id.dot2,R.id.dot3})
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
        mPagerAdapter = new MyPagerAdapter(MainActivity.this);
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

}
