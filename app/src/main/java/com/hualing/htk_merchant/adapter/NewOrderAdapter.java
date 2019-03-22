package com.hualing.htk_merchant.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class NewOrderAdapter extends BaseAdapter {

    public List<String> getmData() {
        return mData;
    }

    public void setmData(List<String> mData) {
        this.mData = mData;
    }

    private List<String> mData;
    private MainActivity context;

    public NewOrderAdapter(MainActivity context){
        this.context = context;
        mData=new ArrayList<String>();
    }

    public void setNewData(){
        mData.add("aaa");
        mData.add("bbb");
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = context.getLayoutInflater().inflate(R.layout.item_new_order,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
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
