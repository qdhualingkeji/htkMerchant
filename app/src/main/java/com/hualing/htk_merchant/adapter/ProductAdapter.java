package com.hualing.htk_merchant.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
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
import butterknife.OnClick;

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
        holder.ifCanBuyTV.setText(dataBean.getIfCanBuy()==1?"已上架":"已下架");
        if(dataBean.isShowCB())
            holder.checkGoodsCB.setVisibility(CheckBox.VISIBLE);
        else
            holder.checkGoodsCB.setVisibility(CheckBox.INVISIBLE);
        String imgUrl = dataBean.getImgUrl();
        //Log.e("imgUrl===",""+imgUrl);
        if(!TextUtils.isEmpty(imgUrl)) {
            Uri uri = Uri.parse(imgUrl);
            holder.imgUrlSDV.setImageURI(uri);
        }
        holder.productNameTV.setText(dataBean.getProductName());
        holder.priceTV.setText("￥"+dataBean.getPrice());
        holder.numTV.setText(dataBean.getInventory()+"/"+dataBean.getInventoryCount());
        holder.editBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

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

    public void showCheckGoodsCB(){
        for(int i=0;i<getSectionCount();i++) {
            for(int j=0;j<getCountForSection(i);j++) {
                TakeoutProduct takeoutProduct = mData.get(i).getTakeoutProductList().get(j);
                if(takeoutProduct.isShowCB())
                    takeoutProduct.setShowCB(false);
                else
                    takeoutProduct.setShowCB(true);
            }
        }
        notifyDataSetChanged();
    }

    class ViewHolder {

        @BindView(R.id.ifCanBuy_tv)
        TextView ifCanBuyTV;
        @BindView(R.id.checkGoods_cb)
        CheckBox checkGoodsCB;
        @BindView(R.id.imgUrl_sdv)
        SimpleDraweeView imgUrlSDV;
        @BindView(R.id.productName_tv)
        TextView productNameTV;
        @BindView(R.id.price_tv)
        TextView priceTV;
        @BindView(R.id.num_tv)
        TextView numTV;
        @BindView(R.id.edit_but)
        Button editBut;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
