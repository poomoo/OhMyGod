package com.poomoo.ohmygod.alarm;

import java.util.Calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.activity.MainFragmentActivity;

/**
 * @author way
 */
public class CallAlarm extends BroadcastReceiver {
    private static final String TAG = "CallAlarm";

    private Context mContext;
    private static final String DEFAULT_SNOOZE = "10";
    int mNoteID;
    int tag;
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 100000010;
    private static final String CLEAR_NOTI_ACTION = "com.sec.android.app.simrecord.CLEAR_NOTI_ACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        LogUtils.i(TAG, "onCreate()  intent-->" + intent + "  data-->" + (intent == null ? "" : intent.getExtras()));
        // 接受其他闹钟事件，电话事件，短信事件等，进行交互处理
        String action = intent.getAction();
        if (action != null && action.equals("android.intent.action.PHONE_STATE")) {
            LogUtils.i(TAG, "onReceive:action.PHONE_STATE");
            snooze();
        } else if (action != null && action.equals("android.provider.Telephony.SMS_RECEIVED")) {
            LogUtils.i(TAG, "onReceive:Telephony.SMS_RECEIVED");
            snooze();
        } else if (action != null && action.equals(CLEAR_NOTI_ACTION)) {
            NotificationManager mNotifiManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotifiManager.cancel(NOTIFICATION_ID);
        } else {
            tag = intent.getIntExtra("_id", -1);
            if (tag == -1) {
                return;
            }
            setNotification();
        }
    }

    // Attempt to snooze this alert.
    private void snooze() {
        // Do not snooze if the snooze button is disabled.
        final String snooze = DEFAULT_SNOOZE;
        int snoozeMinutes = Integer.parseInt(snooze);
        final long snoozeTime = System.currentTimeMillis()
                + (1000 * 60 * snoozeMinutes);

        // Get the display time for the snooze and update the notification.
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(snoozeTime);
        // Append (snoozed) to the label.
        String label = "闹铃";
        label = mContext.getResources().getString(
                R.string.song_pause, label);

        // Notify the user that the alarm has been snoozed.
        Intent cancelSnooze = new Intent();
        cancelSnooze.setAction("com.way.note.STOP_ALARM");
        cancelSnooze.putExtra(DBOpenHelper.ID, mNoteID);
        mContext.sendBroadcast(cancelSnooze);
//        PendingIntent broadcast = PendingIntent.getBroadcast(mContext, mNoteID,
//                cancelSnooze, 0);
//        Notification n = new Notification(R.drawable.stat_notify_alarm, label,
//                0);
//        n.setLatestEventInfo(
//                mContext,
//                label,
//                mContext.getResources().getString(
//                        R.string.click_cancel,
//                        (String) DateFormat.format("kk:mm", c)), broadcast);
//        n.flags |= Notification.FLAG_AUTO_CANCEL
//                | Notification.FLAG_ONGOING_EVENT;
    }

    private void setNotification() {
        LogUtils.i(TAG, "setNotification:" + MyUtil.appIsRunning(mContext, mContext.getPackageName()));
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // 设置显示提示信息，该信息也会在状态栏显示
        String tickerText = "抢购时间到了";
        Notification.Builder builder = new Notification.Builder(mContext).setTicker(tickerText).setSmallIcon(R.drawable.ic_logo).setDefaults(Notification.DEFAULT_SOUND).setContentText("抢购时间到了").setAutoCancel(true);
        Notification note;
        if (!MyUtil.appIsRunning(mContext, mContext.getPackageName())) {
            //通过Intent，使得点击Notification之后会启动新的Activity
            Intent i = new Intent(mContext, MainFragmentActivity.class);
            //该标志位表示如果Intent要启动的Activity在栈顶，则无须创建新的实例
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 100, i, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
        } else {
            Intent resultIntent = new Intent();
            resultIntent.setAction(CLEAR_NOTI_ACTION);
            PendingIntent resultPendingIntent = PendingIntent.getBroadcast(mContext, 0, resultIntent, 0);
            builder.setContentIntent(resultPendingIntent);
        }
        note = builder.build();
        mNotificationManager.notify(NOTIFICATION_ID, note);
    }
}