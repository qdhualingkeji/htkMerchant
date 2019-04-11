package com.hualing.htk_merchant.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hualing.htk_merchant.adapter.DeliveryAdapter;
import com.hualing.htk_merchant.adapter.FinishedAdapter;
import com.hualing.htk_merchant.adapter.MyPagerAdapter;
import com.hualing.htk_merchant.adapter.NewOrderAdapter;
import com.hualing.htk_merchant.global.TheApplication;
import com.hualing.htk_merchant.model.CommonMsg;
import com.hualing.htk_merchant.utils.AsynClient;
import com.hualing.htk_merchant.utils.GsonHttpResponseHandler;
import com.hualing.htk_merchant.utils.MyHttpConfing;
import com.loopj.android.http.RequestParams;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class JPushUtil {

    private static final String TAG = "JPush";
    public static NewOrderAdapter mAdapter1;
    public static DeliveryAdapter mAdapter2;
    public static FinishedAdapter mAdapter3;
    public static final String NEW_ORDER="1";
    public static final String CONFIRM_ORDER="2";
    public static final String CANNEL_ORDER="3";
    public static final String RECEIPT_ORDER="4";
    public static final String DELETE_ORDER="5";

    // 这是来自 JPush Example 的设置别名的 Activity 里的代码。一般 App 的设置的调用入口，在任何方便的地方调用都可以。
    public static void setAlias(Context context,String alias) {
        if (TextUtils.isEmpty(alias)) {
            Toast.makeText(context,"JPush别名为空", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (!ExampleUtil.isValidTagAndAlias(alias)) {
//            Toast.makeText(context,R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
//            return;
//        }

        // 调用 Handler 来异步设置别名
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    public static final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i(TAG, logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i(TAG, logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e(TAG, logs);
            }
        }
    };
    public static final int MSG_SET_ALIAS = 1001;
    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d(TAG, "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(TheApplication.getContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i(TAG, "Unhandled msg - " + msg.what);
            }
        }
    };

    public static void initAdapter(NewOrderAdapter mAdapter1, DeliveryAdapter mAdapter2, FinishedAdapter mAdapter3){
        JPushUtil.mAdapter1=mAdapter1;
        JPushUtil.mAdapter2=mAdapter2;
        JPushUtil.mAdapter3=mAdapter3;
    }

    public static void sendNotification(final Activity context, String mobilePhone, String title, String content, String actionName){
        RequestParams params = AsynClient.getRequestParams();

        params.put("mobilePhone", mobilePhone);
        params.put("title", title);
        params.put("content", content);
        params.put("actionName", actionName);

        AsynClient.post(MyHttpConfing.sendNotification, context, params, new GsonHttpResponseHandler() {
            @Override
            protected Object parseResponse(String rawJsonData) throws Throwable {
                return null;
            }

            @Override
            public void onFailure(int statusCode, String rawJsonData, Object errorResponse) {
                Log.e("rawJsonData===",""+rawJsonData);
                Toast.makeText(context, "推送失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, String rawJsonResponse, Object response) {
                Log.e("rawJsonResponse===",""+rawJsonResponse);

                Gson gson = new Gson();
                CommonMsg commonMsg = gson.fromJson(rawJsonResponse, CommonMsg.class);
                if (commonMsg.getCode() == 100) {

                } else {

                }
            }
        });
    }
}
