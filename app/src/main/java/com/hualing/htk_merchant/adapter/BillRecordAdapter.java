package com.hualing.htk_merchant.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.activities.BillRecordActivity;
import com.hualing.htk_merchant.entity.BillRecordEntity;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.utils.AsynClient;
import com.hualing.htk_merchant.utils.GsonHttpResponseHandler;
import com.hualing.htk_merchant.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BillRecordAdapter extends BaseAdapter {

    public List<BillRecordEntity.DataBean> getmData() {
        return mData;
    }

    public void setmData(List<BillRecordEntity.DataBean> mData) {
        this.mData = mData;
    }

    private List<BillRecordEntity.DataBean> mData;
    private BillRecordActivity context;

    public BillRecordAdapter(BillRecordActivity context){
        this.context = context;
        mData=new ArrayList<BillRecordEntity.DataBean>();
    }

    public void setNewData(){
        RequestParams params = AsynClient.getRequestParams();
        params.put("accountToken", GlobalData.token);

        AsynClient.post(MyHttpConfing.getBillRecord, context, params, new GsonHttpResponseHandler() {
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
                BillRecordEntity billRecordEntity = gson.fromJson(rawJsonResponse, BillRecordEntity.class);
                if (billRecordEntity.getCode() == 100) {
                    mData = billRecordEntity.getData();
                }
                else{
                    mData.clear();
                }
                notifyDataSetChanged();
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
            convertView = context.getLayoutInflater().inflate(R.layout.item_bill_record,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        BillRecordEntity.DataBean billRecord = mData.get(position);
        holder.serialNumberTV.setText(String.valueOf(billRecord.getSerialNumber()));
        holder.orderNumberTV.setText(billRecord.getOrderNumber());
        holder.statusTV.setText(billRecord.getStatus() == 1 ? "未入账" : "已入账");
        holder.orderIncomeTV.setText("+"+String.valueOf(billRecord.getOrderIncome()));
        holder.spendingOnOrderTV.setText("-"+String.valueOf(billRecord.getSpendingOnOrder()));
        holder.amountTV.setText("+"+String.valueOf(billRecord.getAmount()));

        return convertView;
    }

    class ViewHolder{

        @BindView(R.id.serialNumber_tv)
        TextView serialNumberTV;
        @BindView(R.id.orderNumber_tv)
        TextView orderNumberTV;
        @BindView(R.id.status_tv)
        TextView statusTV;
        @BindView(R.id.orderIncome_tv)
        TextView orderIncomeTV;
        @BindView(R.id.spendingOnOrder_tv)
        TextView spendingOnOrderTV;
        @BindView(R.id.amount_tv)
        TextView amountTV;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
