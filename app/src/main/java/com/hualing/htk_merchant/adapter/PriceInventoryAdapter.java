package com.hualing.htk_merchant.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.activities.AddProductActivity;
import com.hualing.htk_merchant.model.TakeoutProduct;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PriceInventoryAdapter extends BaseAdapter {

    private List<TakeoutProduct> mData;
    private AddProductActivity context;

    public List<TakeoutProduct> getmData() {
        return mData;
    }

    public void setmData(List<TakeoutProduct> mData) {
        this.mData = mData;
    }

    public PriceInventoryAdapter(AddProductActivity context, List<TakeoutProduct> mData){
        this.context=context;
        this.mData=mData;
    }

    public void addGG(){
        TakeoutProduct tp = new TakeoutProduct();
        tp.setPrice(0.00);
        tp.setPriceCanhe(0.00);
        tp.setInventory(10000);
        tp.setInventoryCount(10000);
        mData.add(tp);
        notifyDataSetChanged();
    }

    private void removeGG(int position){
        mData.remove(position);
        notifyDataSetChanged();
        if(mData.size()<6){
            context.addGGBut.setVisibility(Button.VISIBLE);
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
        if(convertView==null){
            convertView=context.getLayoutInflater().inflate(R.layout.item_price_inventory,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }

        TakeoutProduct takeoutProduct = mData.get(position);
        holder.priceET.setText(String.valueOf(takeoutProduct.getPrice()));
        holder.priceCanheET.setText(String.valueOf(takeoutProduct.getPriceCanhe()));
        holder.inventoryET.setText(String.valueOf(takeoutProduct.getInventory()));
        holder.inventoryCountET.setText(String.valueOf(takeoutProduct.getInventoryCount()));
        holder.removeGGBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeGG(position);
            }
        });

        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.price_et)
        EditText priceET;
        @BindView(R.id.priceCanhe_et)
        EditText priceCanheET;
        @BindView(R.id.inventory_et)
        EditText inventoryET;
        @BindView(R.id.inventoryCount_et)
        EditText inventoryCountET;
        @BindView(R.id.removeGG_but)
        Button removeGGBut;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
