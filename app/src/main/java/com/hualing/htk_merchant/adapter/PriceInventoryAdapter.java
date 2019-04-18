package com.hualing.htk_merchant.adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.activities.AddProductActivity;
import com.hualing.htk_merchant.model.TakeoutProduct;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 这个适配器是之前一个商品对应多种规格时加的，现在改成每次添加商品只能有一种规格了，这个类已经没用了
 */
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
            //context.addGGBut.setVisibility(Button.VISIBLE);
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        //if(convertView==null){
            convertView=context.getLayoutInflater().inflate(R.layout.item_price_inventory,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
            /*
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }
        */

        final TakeoutProduct takeoutProduct = mData.get(position);
        final EditText priceET = holder.priceET;
        priceET.setText(String.valueOf(takeoutProduct.getPrice()));
        priceET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Log.e("111111","1111111111");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Log.e("22222222","22222222");
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Log.e("3333333",""+editable.toString());
                String priceStr = priceET.getText().toString();
                if(TextUtils.isEmpty(priceStr)) {
                    priceStr = "0.0";
                    priceET.setText(priceStr);
                }
                takeoutProduct.setPrice(Double.valueOf(priceStr));
            }
        });
        /*
        priceET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //Log.e("4444", "edtUserName获取到焦点了。。。。。。");
                } else {
                    //Log.e("Price===", ""+((TakeoutProduct)PriceInventoryAdapter.this.getItem(position)).getPrice());
                    //notifyDataSetChanged();
                }
            }
        });
        */

        final EditText priceCanheET = holder.priceCanheET;
        priceCanheET.setText(String.valueOf(takeoutProduct.getPriceCanhe()));
        priceCanheET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String priceCanheStr = priceCanheET.getText().toString();
                if(TextUtils.isEmpty(priceCanheStr)) {
                    priceCanheStr = "0.0";
                    priceCanheET.setText(priceCanheStr);
                }
                takeoutProduct.setPriceCanhe(Double.valueOf(priceCanheStr));
            }
        });
        /*
        priceCanheET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //Log.e("4444", "edtUserName获取到焦点了。。。。。。");
                } else {
                    //Log.e("PriceCanhe===", ""+((TakeoutProduct)PriceInventoryAdapter.this.getItem(position)).getPriceCanhe());
                    //notifyDataSetChanged();
                }
            }
        });
        */

        final EditText inventoryET = holder.inventoryET;
        inventoryET.setText(String.valueOf(takeoutProduct.getInventory()));
        inventoryET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inventoryStr = inventoryET.getText().toString();
                if(TextUtils.isEmpty(inventoryStr)) {
                    inventoryStr = "0";
                    inventoryET.setText(inventoryStr);
                }
                takeoutProduct.setInventory(Integer.valueOf(inventoryStr));
            }
        });
        final EditText inventoryCountET = holder.inventoryCountET;
        inventoryCountET.setText(String.valueOf(takeoutProduct.getInventoryCount()));
        inventoryCountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String inventoryCountStr = inventoryCountET.getText().toString();
                if(TextUtils.isEmpty(inventoryCountStr)) {
                    inventoryCountStr = "0";
                    inventoryET.setText(inventoryCountStr);
                }
                takeoutProduct.setInventoryCount(Integer.valueOf(inventoryCountStr));
            }
        });
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
