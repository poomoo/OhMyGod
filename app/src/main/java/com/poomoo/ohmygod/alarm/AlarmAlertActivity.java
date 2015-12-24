package com.poomoo.ohmygod.alarm;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.view.activity.BaseActivity;
import com.poomoo.ohmygod.view.activity.MainFragmentActivity;

import java.util.Date;

public class AlarmAlertActivity extends BaseActivity {
    private static final String TAG = "AlarmAlert";

    public static final String STOP_ALARM = "com.way.note.STOP_ALARM";
    public static final String ALARM_DONE = "com.android.deskclock.ALARM_DONE";
    int TIMEOUT = 30;// 闹铃响时长（s）
    private String action;

    private long mStartTime;
    private Handler mHandler = new Handler();
    private NotificationManager mNotificationManager;
    private static final int NOTIFICATION_ID = 100000010;
    private PowerManager.WakeLock mWakeLock;

    KeyguardManager mKeyguardManager = null;// 声明键盘管理器

    private KeyguardLock mKeyguardLock = null; // 声明键盘锁

    private TextView mContentTV;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.dialog_alarm_alert);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
//                R.layout.dialog_alarm_alert_title);

        initViews();
        setWakeLock();
        registerStopReceiver();
        setNotication();
        setMuteOnTel();
        setAutoStop();
        stopWhenGoFM();
    }

    private void stopWhenGoFM() {
        /**
         * add for bug : when play fm ,need to stop the alarm.^M Intent i = new
         * Intent("com.android.fm.stopmusicservice"); i.putExtra("playingfm",
         * true); this.sendBroadcast(i);
         */
        IntentFilter stopFromFM = new IntentFilter(
                "com.android.fm.stopmusicservice");
        this.registerReceiver(stopFromFmReceiver, stopFromFM);
    }

    private BroadcastReceiver stopFromFmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Boolean isPlaying = intent.getExtras().getBoolean("playingfm");
            if (intent.getAction().equals("com.android.fm.stopmusicservice")) {
                if (isPlaying != null && isPlaying) {
                    closeMediaPlayer();
                }
            }
        }
    };

    private void setAutoStop() {
        mHandler.postDelayed(stopTask, TIMEOUT * 1000);
    }

    private Runnable stopTask = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "auto stop task run");
            closeMediaPlayer();
        }
    };

    private void initViews() {
//        TextView alarmTimeTV = (TextView) findViewById(R.id.title_right);
//        alarmTimeTV.setText(DateFormat.getTimeFormat(this).format(new Date()));
//        mContentTV = (TextView) findViewById(R.id.tv_note_content);
        Button cancelBtn = (Button) findViewById(R.id.cancel_alarm);
        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMediaPlayer();
                cancleNoticafation();
                openActivity(MainFragmentActivity.class);

            }
        });
    }

    private void registerStopReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(STOP_ALARM);
        // 防止在灭屏时不能接收停止闹钟的广播，所以用静态注册。
        this.registerReceiver(this.receiver, filter);
    }

    private void setMuteOnTel() {
        // 通知背景播放音乐mp3暂停，这个action被mp3注册
        Intent alarm_alert = new Intent("com.android.deskclock.ALARM_ALERT");
        this.sendBroadcast(alarm_alert);

        // 获得来电状态，如果正在通话或者来电，闹铃时间到不响闹铃，只是通知。
        TelephonyManager mTelephonyMgr = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
        int callstate = mTelephonyMgr.getCallState();
        if (callstate == 0) {
            // 调用服务，播放闹铃
            Intent intent2 = new Intent(this, AlarmService.class);
            intent2.putExtra(DBOpenHelper.RINGTONE_URI, "defalut");
            intent2.putExtra(DBOpenHelper.ISVIBRATE, "true");
            intent2.putExtra(DBOpenHelper.RINGTONE_NAME, "defalut");

            startService(intent2);
            // 音量控制
            setVolumeControlStream(AudioManager.STREAM_ALARM);
            mStartTime = System.currentTimeMillis();
            Log.v(TAG, "AlarmAlert:mStartTime=" + mStartTime);
        } else {
            String displayTime = "闹铃关闭";
            Toast.makeText(AlarmAlertActivity.this, displayTime,
                    Toast.LENGTH_LONG).show();
            this.finish();
            this.unregisterReceiver(this.receiver);
        }
    }

    private void setNotication() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 设置显示提示信息，该信息也会在状态栏显示
        String tickerText = "抢购时间到了";
        Notification.Builder builder = new Notification.Builder(this).setTicker(tickerText).setSmallIcon(R.drawable.clock);
        Notification note = new Notification();
        note.flags = Notification.FLAG_ONGOING_EVENT;

        //通过Intent，使得点击Notification之后会启动新的Activity
        Intent i = new Intent(this, MainFragmentActivity.class);
        //该标志位表示如果Intent要启动的Activity在栈顶，则无须创建新的实例
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 100, i, PendingIntent.FLAG_UPDATE_CURRENT);

        note = builder.setContentIntent(pendingIntent).setContentText("抢购时间到了,点击跳转").build();
        note.flags |= Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_ONGOING_EVENT;
        mNotificationManager.notify(NOTIFICATION_ID, note);
    }

    private void setWakeLock() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        // 获取系统服务
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);


        if (!pm.isScreenOn()) {
            // 点亮屏
            mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
            mWakeLock.acquire();
            if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
                // 初始化键盘锁，可以锁定或解开键盘锁
                mKeyguardLock = mKeyguardManager.newKeyguardLock("");
                // 禁用显示键盘锁定
                mKeyguardLock.disableKeyguard();
            }
        } else if (pm.isScreenOn()) {
            if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
                // 初始化键盘锁，可以锁定或解开键盘锁
                mKeyguardLock = mKeyguardManager.newKeyguardLock("");
                // 禁用显示键盘锁定
                mKeyguardLock.disableKeyguard();
            }
        }
    }

    @Override
    public void onBackPressed() {
        closeMediaPlayer();
        cancleNoticafation();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    public void onStop() {
        Log.v(TAG, "onStop");
        if (mWakeLock != null) {
            mWakeLock.release();
        }
        if (mKeyguardLock != null) {
            mKeyguardLock.reenableKeyguard();
        }
        super.onStop();
    }

    // 用于接受广播，停止震动
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (action != null && action.equals("com.way.note.STOP_ALARM")) {
                closeMediaPlayer();
            }
        }
    };

    public void closeMediaPlayer() {
        mHandler.removeCallbacks(stopTask);
        Intent intent3 = new Intent(this, AlarmService.class);
        Log.v(TAG, "closeMediaPlayer()");
        stopService(intent3);

        // 恢复音量控制状态
        setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);

        // 通知背景播放音乐mp3再次启动
        Intent alarm_done = new Intent(ALARM_DONE);
        this.sendBroadcast(alarm_done);

        mHandler.post(mDisplayToast);
        // 修改鬧鈴数据库，使得“开启鬧鈴”状态为否

        this.unregisterReceiver(this.receiver);
        this.unregisterReceiver(stopFromFmReceiver);
        this.finish();
    }

    private void cancleNoticafation() {
        if (mNotificationManager != null) {
            mNotificationManager.cancel(NOTIFICATION_ID);
        }
    }

    Runnable mDisplayToast = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            String displayTime = "闹铃关闭";
            Toast.makeText(AlarmAlertActivity.this, displayTime,
                    Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // mp.stop();
        }
        super.onConfigurationChanged(newConfig);
    }

}