package com.hualing.htk_merchant.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.activities.MainActivity;
import com.hualing.htk_merchant.entity.OrderRecordEntity;
import com.hualing.htk_merchant.global.GlobalData;
import com.hualing.htk_merchant.model.CommonMsg;
import com.hualing.htk_merchant.model.OrderProduct;
import com.hualing.htk_merchant.util.JPushUtil;
import com.hualing.htk_merchant.utils.AsynClient;
import com.hualing.htk_merchant.utils.GsonHttpResponseHandler;
import com.hualing.htk_merchant.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FinishedAdapter extends BaseAdapter {

    public List<OrderRecordEntity.DataBean> getmData() {
        return mData;
    }

    public void setmData(List<OrderRecordEntity.DataBean> mData) {
        this.mData = mData;
    }

    private List<OrderRecordEntity.DataBean> mData;
    private MainActivity context;
    private Button mDot3;

    public FinishedAdapter(MainActivity context,Button mDot3){
        this.context = context;
        this.mDot3=mDot3;
        mData=new ArrayList<OrderRecordEntity.DataBean>();
    }

    public void setNewData(){
        RequestParams params = AsynClient.getRequestParams();
        params.put("userId", GlobalData.userID);
        params.put("statusCode", 3);

        AsynClient.post(MyHttpConfing.getFinishedOrderList, context, params, new GsonHttpResponseHandler() {
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
                }
                else {
                    mData.clear();
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        int size = mData.size();
        mDot3.setText("已完成("+size+")");
        return size;
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
            convertView = context.getLayoutInflater().inflate(R.layout.item_finished_order,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final OrderRecordEntity.DataBean orderRecord = mData.get(position);
        holder.orderNumberTV.setText("订单:"+orderRecord.getOrderNumber());
        holder.statusCodeTV.setText("订单状态（已完成）");
        holder.orderTimeTV.setText(orderRecord.getOrderTime());
        holder.receiptNameTV.setText(orderRecord.getReceiptName().substring(0,1)+(orderRecord.getSex()==0?"女士":"先生"));
        holder.receivingCallTV.setText(orderRecord.getReceivingCall());
        holder.shippingAddressTV.setText(orderRecord.getShippingAddress());

        NewOrderProductAdapter opAdapter = holder.orderProductAdapter;
        opAdapter = new NewOrderProductAdapter(context);
        opAdapter.setNewData(orderRecord.getProductLists());
        opAdapter.notifyDataSetChanged();
        holder.orderProductLV.setAdapter(opAdapter);
        context.setListViewHeightBasedOnChildren(holder.orderProductLV);
        holder.priceCanheTV.setText(orderRecord.getPriceCanhe()+"￥");
        holder.deliveryFeeTV.setText(orderRecord.getDeliveryFee()+"￥");
        holder.paidTV.setText("（已支付）￥"+orderRecord.getOrderAmount());

        holder.confirmBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comfirm(orderRecord.getOrderNumber(),position);
            }
        });
        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.orderNumber_tv)
        TextView orderNumberTV;
        @BindView(R.id.statusCode_tv)
        TextView statusCodeTV;
        @BindView(R.id.orderTime_tv)
        TextView orderTimeTV;
        @BindView(R.id.receiptName_tv)
        TextView receiptNameTV;
        @BindView(R.id.receivingCall_tv)
        TextView receivingCallTV;
        @BindView(R.id.shippingAddress_tv)
        TextView shippingAddressTV;
        @BindView(R.id.orderProduct_lv)
        ListView orderProductLV;
        private NewOrderProductAdapter orderProductAdapter;
        @BindView(R.id.priceCanhe_tv)
        TextView priceCanheTV;
        @BindView(R.id.deliveryFee_tv)
        TextView deliveryFeeTV;
        @BindView(R.id.paid_tv)
        TextView paidTV;
        @BindView(R.id.confirm_but)
        Button confirmBut;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 确认订单
     * @param orderNumber
     * @param position
     */
    private void comfirm(String orderNumber, final int position){
        context.showLoadingDialog(context,"删除中");
        RequestParams params = AsynClient.getRequestParams();
        params.put("orderNumber", orderNumber);
        AsynClient.post(MyHttpConfing.confirmFinishedOrder, context, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                //Log.e("rawJsonData===",""+rawJsonData);
                context.hideLoadingDialog();
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                //Log.e("rawJsonResponse===",""+rawJsonResponse);
                Log.i(MyHttpConfing.tag, rawJsonResponse);

                Gson gson = new Gson();
                CommonMsg commonMsg = gson.fromJson(rawJsonResponse, CommonMsg.class);
                if(commonMsg.getCode()==0){
                    mData.remove(position);
                    notifyDataSetChanged();
                    JPushUtil.sendNotification(context, GlobalData.userName, "商家接单app", "删除订单成功", JPushUtil.DELETE_ORDER);
                }

                context.showMessage(commonMsg.getMessage());
                context.hideLoadingDialog();
            }
        });
    }
}
