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
import com.hualing.htk_merchant.utils.AsynClient;
import com.hualing.htk_merchant.utils.GsonHttpResponseHandler;
import com.hualing.htk_merchant.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        params.put("userId", GlobalData.userID);
        params.put("statusCode", 0);

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = context.getLayoutInflater().inflate(R.layout.item_new_order,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final OrderRecordEntity.DataBean orderRecord = mData.get(position);
        holder.orderNumberTV.setText("订单:"+orderRecord.getOrderNumber());
        holder.statusCodeTV.setText("订单状态（待接单）");
        holder.orderTimeTV.setText(orderRecord.getOrderTime());
        holder.receiptNameTV.setText(orderRecord.getReceiptName().substring(0,1)+(orderRecord.getSex()==0?"女士":"先生"));
        holder.receivingCallTV.setText(orderRecord.getReceivingCall());
        holder.shippingAddressTV.setText(orderRecord.getShippingAddress());

        NewOrderProductAdapter opAdapter = holder.orderProductAdapter;
        opAdapter = new NewOrderProductAdapter(context);
        opAdapter.setNewData(orderRecord.getProductLists());
        opAdapter.notifyDataSetChanged();
        holder.orderProductLV.setAdapter(opAdapter);

        holder.jiSuanPaid(opAdapter.getmData());

        holder.acceptBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmOrder(orderRecord.getOrderNumber(),position);
            }
        });
        holder.refuseBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelOrder(orderRecord.getOrderNumber(),position);
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
        @BindView(R.id.paid_tv)
        TextView paidTV;
        @BindView(R.id.accept_but)
        Button acceptBut;
        @BindView(R.id.refuse_but)
        Button refuseBut;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void jiSuanPaid(List<OrderProduct> opList){
            double sumPrice=0;
            for(OrderProduct op:opList){
                sumPrice+=op.getPrice();
            }
            paidTV.setText("（已支付）￥"+sumPrice);
        }
    }

    /**
     *  确认订单
     * @param orderNumber
     * @param position
     */
    private void confirmOrder(String orderNumber, final int position){
        RequestParams params = AsynClient.getRequestParams();
        params.put("orderNumber", orderNumber);
        AsynClient.post(MyHttpConfing.confirmTheOrder, context, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                //Log.e("rawJsonData===",""+rawJsonData);

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
                }

                context.showMessage(commonMsg.getMessage());

            }
        });
    }

    /**
     * 取消订单
     * @param orderNumber
     * @param position
     */
    private void cancelOrder(String orderNumber, final int position) {
        RequestParams params = AsynClient.getRequestParams();
        params.put("orderNumber", orderNumber);
        params.put("content", "其他原因");
        params.put("mark", 0);
        AsynClient.post(MyHttpConfing.cancelOrder, context, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                //Log.e("rawJsonData===",""+rawJsonData);

            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                //Log.e("rawJsonResponse===",""+rawJsonResponse);
                Log.i(MyHttpConfing.tag, rawJsonResponse);

                Gson gson = new Gson();
                CommonMsg commonMsg = gson.fromJson(rawJsonResponse, CommonMsg.class);
                mData.remove(position);
                notifyDataSetChanged();
                context.showMessage(commonMsg.getMessage());

            }
        });

    }
}
