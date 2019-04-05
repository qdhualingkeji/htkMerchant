package com.hualing.htk_merchant.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.activities.EditProductActivity;
import com.hualing.htk_merchant.model.ProductProperty;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProductPropertyAdapter extends BaseAdapter {

    private List<ProductProperty> mData;
    private EditProductActivity context;

    public EditProductPropertyAdapter(EditProductActivity context, List<ProductProperty> mData){
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
        //if(convertView==null){
            convertView = context.getLayoutInflater().inflate(R.layout.item_edit_product_property,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            /*
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        */

        final ProductProperty productProperty = mData.get(position);
        final EditText propertyNameET = holder.propertyNameET;
        propertyNameET.setText(productProperty.getPropertyName());
        propertyNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                productProperty.setPropertyName(propertyNameET.getText().toString());
                notifyDataSetChanged();
            }
        });

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
