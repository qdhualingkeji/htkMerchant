package com.hualing.htk_merchant.activities;

import android.graphics.Color;
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

import butterknife.BindView;

import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.adapter.MyPagerAdapter;
import com.hualing.htk_merchant.adapter.NewOrderAdapter;
import com.hualing.htk_merchant.global.TheApplication;

public class MainActivity extends BaseActivity {

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
    @BindView(R.id.dot4)
    Button mDot4;
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

        int width = (int) (TheApplication.getScreenWidth()/4);
        int height=100;
        mDot1.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        mDot2.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        mDot3.setLayoutParams(new LinearLayout.LayoutParams(width,height));
        mDot4.setLayoutParams(new LinearLayout.LayoutParams(width,height));

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
                //newOrderAdapter.setNewData();
                newOrderAdapter.notifyDataSetChanged();
                mDot1.setSelected(true);
                break;
                /*
            case 1:
                Log.e("position2========",""+position);
                DaiQuHuoAdapter daiQuHuoAdapter = mPagerAdapter.getmAdapter2();
                daiQuHuoAdapter.setNewData();
                daiQuHuoAdapter.notifyDataSetChanged();
                mDot2.setSelected(true);
                break;
            case 2:
                Log.e("position3========",""+position);
                mPagerAdapter.getmAdapter3().notifyDataSetChanged();
                mDot3.setSelected(true);
                break;
                */
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
