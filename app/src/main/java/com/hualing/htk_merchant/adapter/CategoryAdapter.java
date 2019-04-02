package com.hualing.htk_merchant.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.activities.EditProductActivity;
import com.hualing.htk_merchant.model.TakeoutCategory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryAdapter extends BaseAdapter {

    private List<TakeoutCategory> mData;
    private EditProductActivity context;

    public CategoryAdapter(EditProductActivity context,List<TakeoutCategory> mData){
        this.context=context;
        this.mData=mData;
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
            convertView = context.getLayoutInflater().inflate(R.layout.item_category,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        TakeoutCategory takeoutCategory = mData.get(position);
        holder.categoryNameTV.setText(takeoutCategory.getCategoryName());
        holder.id=takeoutCategory.getId();

        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.category_name_tv)
        TextView categoryNameTV;
        Integer id;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
