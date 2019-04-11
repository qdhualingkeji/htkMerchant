package com.hualing.htk_merchant.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.activities.MainActivity;
import com.hualing.htk_merchant.util.JPushUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * @author 马鹏昊
 * @date {date}
 * @des
 * @updateAuthor
 * @updateDate
 * @updateDes
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";

    private NotificationManager nm;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("zzzzzzzzz","zzzzzzzzz");
        if (null == nm) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Bundle bundle = intent.getExtras();
        //        Logger.d(TAG, "onReceive - " + intent.getAction() + ", extras: " + AndroidUtil.printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            Log.e(TAG, "JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "接受到推送下来的自定义消息");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "接受到推送下来的通知");

            //改变未读标志
            /*
            SharedPreferenceUtil.saveIfHasUnreadMsg(true);
            if (EmployeeMainActivity.sHandler != null) {
                EmployeeMainActivity.sHandler.sendEmptyMessage(0);
            }
            */
            //清除通知
            //JPushInterface.clearAllNotifications(TheApplication.getContext());

            receivingNotification(context, bundle);
            String actionName = null;//bundle.getString("actionName");
            try {
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                JSONObject extrasJO = new JSONObject(extras);
                actionName = extrasJO.getString("actionName");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (actionName){
                case JPushUtil.NEW_ORDER:
                case JPushUtil.CANNEL_ORDER:
                    JPushUtil.mAdapter1.setNewData();
                    break;
                case JPushUtil.CONFIRM_ORDER:
                    JPushUtil.mAdapter1.setNewData();
                    JPushUtil.mAdapter2.setNewData();
                    break;
                case JPushUtil.RECEIPT_ORDER:
                    JPushUtil.mAdapter2.setNewData();
                    JPushUtil.mAdapter3.setNewData();
                    break;
                case JPushUtil.DELETE_ORDER:
                    JPushUtil.mAdapter3.setNewData();
                    break;
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.e(TAG, "用户点击打开了通知");

            openNotification(context, bundle);

        } else {
            Log.e(TAG, "Unhandled intent - " + intent.getAction());
        }
    }

    private void receivingNotification(Context context, Bundle bundle) {
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        Log.e(TAG, " title : " + title);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        Log.e(TAG, "message : " + message);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.e(TAG, "extras : " + extras);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
        //这一步必须要有而且setSmallIcon也必须要，没有就会设置自定义声音不成功
        notification.setAutoCancel(true).setSmallIcon(R.mipmap.ic_launcher);
        String alert = bundle.getString(JPushInterface.EXTRA_ALERT);
        //Toast.makeText(context,"alert==="+alert,Toast.LENGTH_LONG).show();
        if (alert!=null&&!alert.equals("")){
            //Toast.makeText(context,"播没播放声音？？？",Toast.LENGTH_LONG).show();
            notification.setSound(
                    Uri.parse("android.resource://" + context.getPackageName() + "/" +R.raw.notification));
        }

        //最后刷新notification是必须的
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification.build());
    }

    private void openNotification(Context context, Bundle bundle) {
//        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        String myValue = "";
//        try {
//            JSONObject extrasJson = new JSONObject(extras);
//            myValue = extrasJson.optString("myKey");
//        } catch (Exception e) {
//            Logger.w(TAG, "Unexpected: extras is not a valid json", e);
//            return;
//        }
        //这里不需要打开相应的页，点击通知栏都打开待处理页
        //        if (TYPE_THIS.equals(myValue)) {
        //            Intent mIntent = new Intent(context, ThisActivity.class);
        //            mIntent.putExtras(bundle);
        //            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //            context.startActivity(mIntent);
        //        } else if (TYPE_ANOTHER.equals(myValue)){
        //            Intent mIntent = new Intent(context, AnotherActivity.class);
        //            mIntent.putExtras(bundle);
        //            mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //            context.startActivity(mIntent);
        //        }

        Intent mIntent = new Intent(context, MainActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mIntent);

    }
}
