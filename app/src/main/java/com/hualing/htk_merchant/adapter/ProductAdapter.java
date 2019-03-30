package com.hualing.htk_merchant.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.activities.ProductListActivity;
import com.hualing.htk_merchant.entity.ReturnCategoryAndProductEntity;
import com.hualing.htk_merchant.entity.TakeoutProductEntity;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.model.TakeoutCategory;
import com.hualing.htk_merchant.model.TakeoutProduct;
import com.hualing.htk_merchant.utils.AsynClient;
import com.hualing.htk_merchant.utils.GsonHttpResponseHandler;
import com.hualing.htk_merchant.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductAdapter extends SectionedBaseAdapter {

    private ProductListActivity context;
    private List<ReturnCategoryAndProductEntity.DataBean> mData;

    public ProductAdapter(ProductListActivity context, List<ReturnCategoryAndProductEntity.DataBean> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public Object getItem(int section, int position) {
        return mData.get(section).getTakeoutProductList().get(position);
    }

    @Override
    public long getItemId(int section, int position) {
        return position;
    }

    @Override
    public int getSectionCount() {
        return mData.size();
    }

    @Override
    public int getCountForSection(int section) {
        List<TakeoutProduct> list = mData.get(section).getTakeoutProductList();
        int size=0;
        if(list!=null)
            size = list.size();
        return size;
    }

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = context.getLayoutInflater().inflate(R.layout.item_product,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        TakeoutProduct dataBean = mData.get(section).getTakeoutProductList().get(position);
        String imgUrl = dataBean.getImgUrl();
        Log.e("imgUrl===",""+imgUrl);
        if(!TextUtils.isEmpty(imgUrl)) {
            Uri uri = Uri.parse(imgUrl);
            holder.imgUrlSDV.setImageURI(uri);
        }

        return convertView;
    }

    @Override
    public View getSectionHeaderView(int section, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = (LinearLayout) inflator.inflate(R.layout.header_item, null);
        } else {
            layout = (LinearLayout) convertView;
        }
        layout.setClickable(false);
        ((TextView) layout.findViewById(R.id.textItem)).setText(mData.get(section).getCategoryName());
        return layout;
    }

    class ViewHolder {

        @BindView(R.id.imgUrl_sdv)
        SimpleDraweeView imgUrlSDV;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
