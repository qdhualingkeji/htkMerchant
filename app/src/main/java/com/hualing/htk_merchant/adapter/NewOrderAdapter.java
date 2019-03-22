package com.hualing.htk_merchant.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hualing.htk_merchant.activities.MainActivity;

public class NewOrderAdapter extends BaseAdapter {

    private MainActivity context;

    public NewOrderAdapter(MainActivity context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
