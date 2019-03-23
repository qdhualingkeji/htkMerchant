package com.hualing.htk_merchant.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.activities.MainActivity;
import com.hualing.htk_merchant.model.OrderProduct;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewOrderProductAdapter extends BaseAdapter {

    private List<OrderProduct> mData;

    public List<OrderProduct> getmData() {
        return mData;
    }

    public void setmData(List<OrderProduct> mData) {
        this.mData = mData;
    }

    private MainActivity context;

    public NewOrderProductAdapter(MainActivity context) {
        this.context = context;
    }

    public void setNewData(List<OrderProduct> mData){
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = context.getLayoutInflater().inflate(R.layout.item_new_order_product,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        OrderProduct orderProduct = mData.get(position);
        holder.productNameTV.setText(orderProduct.getProductName());
        holder.quantityTV.setText("*"+orderProduct.getQuantity());
        holder.priceTV.setText("￥"+orderProduct.getPrice());
        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.productName_tv)
        TextView productNameTV;
        @BindView(R.id.quantity_tv)
        TextView quantityTV;
        @BindView(R.id.price_tv)
        TextView priceTV;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
