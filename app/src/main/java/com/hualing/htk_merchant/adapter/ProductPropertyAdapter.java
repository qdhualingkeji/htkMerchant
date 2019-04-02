package com.hualing.htk_merchant.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.activities.EditProductActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductPropertyAdapter extends BaseAdapter {

    private List<String> mData;
    private EditProductActivity context;

    public ProductPropertyAdapter(EditProductActivity context,List<String> mData){
        this.context=context;
        this.mData=mData;
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
            convertView = context.getLayoutInflater().inflate(R.layout.item_product_property,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.propertyNameET.setText(mData.get(position));

        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.property_name_et)
        EditText propertyNameET;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
