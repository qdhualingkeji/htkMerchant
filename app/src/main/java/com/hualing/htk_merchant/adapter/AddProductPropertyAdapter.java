package com.hualing.htk_merchant.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.activities.AddProductActivity;
import com.hualing.htk_merchant.model.ProductProperty;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddProductPropertyAdapter extends BaseAdapter {

    private List<ProductProperty> mData;

    public List<ProductProperty> getmData() {
        return mData;
    }

    public void setmData(List<ProductProperty> mData) {
        this.mData = mData;
    }

    private AddProductActivity context;

    public AddProductPropertyAdapter(AddProductActivity context, List<ProductProperty> mData){
        this.context=context;
        this.mData=mData;
    }

    public void addProperty(){
        ProductProperty productProperty=new ProductProperty();
        productProperty.setPropertyName("");
        mData.add(productProperty);
        notifyDataSetChanged();
    }

    private void removeProperty(int position){
        mData.remove(position);
        notifyDataSetChanged();
        if(mData.size()<5){
            context.addProBut.setVisibility(Button.VISIBLE);
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        //if(convertView==null){
            convertView = context.getLayoutInflater().inflate(R.layout.item_add_product_property,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            /*
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        */

        final EditText propertyNameET = holder.propertyNameET;
        final ProductProperty productProperty = mData.get(position);
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
        holder.removeProBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeProperty(position);
            }
        });

        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.property_name_et)
        EditText propertyNameET;
        @BindView(R.id.removePro_but)
        Button removeProBut;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
