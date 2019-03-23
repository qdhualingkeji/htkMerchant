package com.hualing.htk_merchant.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.gson.Gson;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.activities.MainActivity;
import com.hualing.htk_merchant.entity.OrderRecordEntity;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.utils.AsynClient;
import com.hualing.htk_merchant.utils.GsonHttpResponseHandler;
import com.hualing.htk_merchant.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class NewOrderAdapter extends BaseAdapter {

    public List<OrderRecordEntity.DataBean> getmData() {
        return mData;
    }

    public void setmData(List<OrderRecordEntity.DataBean> mData) {
        this.mData = mData;
    }

    private List<OrderRecordEntity.DataBean> mData;
    private MainActivity context;

    public NewOrderAdapter(MainActivity context){
        this.context = context;
        mData=new ArrayList<OrderRecordEntity.DataBean>();
    }

    public void setNewData(){
        RequestParams params = AsynClient.getRequestParams();
        params.put("userId", 90);
        params.put("statusCode", 3);

        AsynClient.post(MyHttpConfing.getNewOrderList, context, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("rawJsonData======",""+rawJsonData);
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("rawJsonResponse======",""+rawJsonResponse);

                Gson gson = new Gson();
                OrderRecordEntity orderRecordEntity = gson.fromJson(rawJsonResponse, OrderRecordEntity.class);
                if (orderRecordEntity.getCode() == 100) {
                    mData = orderRecordEntity.getData();
                    notifyDataSetChanged();
                }
            }
        });
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
