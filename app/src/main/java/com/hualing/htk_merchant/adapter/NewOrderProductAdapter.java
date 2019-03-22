package com.hualing.htk_merchant.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class NewOrderProductAdapter extends BaseAdapter {

    private List<String> mData;
    private MainActivity activity;

    public void setNewData(){
        mData=new ArrayList<String>();
        mData.add("aaa");
        mData.add("bbb");
        mData.add("ccc");
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
            convertView = activity.getLayoutInflater().inflate(R.layout.item_new_order_product,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder {

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
