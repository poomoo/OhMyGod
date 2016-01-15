package com.poomoo.ohmygod.service;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.core.AppAction;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.UserBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.application.MyApplication;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.SPUtils;
import com.poomoo.ohmygod.view.activity.LogInActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Check_Status_Service extends Service {
    private MyApplication application;
    private AppAction appAction;
    private String TAG = "Check_Status_Service";
    private Timer timerSyncNef = null;// 定时
    private TimerTask syncNef = null;
    public static AlertDialog dialog;

    public void onCreate() {
        super.onCreate();
        LogUtils.i(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        application = (MyApplication) getApplication();
        appAction = application.getAppAction();
        syncNef = new TimerTask() {
            @Override
            public void run() {
                try {
                    checkStatus();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timerSyncNef = new Timer(true);
        timerSyncNef.schedule(syncNef, 1, 60 * 1000);//

        return super.onStartCommand(intent, flags, startId);
    }

//    public void onStart(Intent intent, int startId) {
//        application = (MyApplication) getApplication();
//        appAction = application.getAppAction();
//        getUserInfoData();
//    }

    private void checkStatus() {
        LogUtils.i(TAG, "checkStatus");
        appAction.checkStatus(application.getUserId(), application.getChannelId(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
            }

            @Override
            public void onFailure(int errorCode, String message) {
                LogUtils.i(TAG, "该账号已登录" + "errorCode" + errorCode);
                if (errorCode == -1) {
                    timerSyncNef.cancel();
                    stopSelf();
                    dialog = new AlertDialog.Builder(getApplicationContext()).setTitle("该账号已在其他设备登录").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            application.clearAllActivity();
                            Intent login = new Intent(getBaseContext(), LogInActivity.class);
                            login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplication().startActivity(login);
                        }
                    }).create();
                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK)
                                return true;
                            return false;
                        }
                    });
                    dialog.show();
                    LogUtils.i(TAG, "弹出对话框");
                }
            }
        });

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO 自动生成的方法存根
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO 自动生成的方法存根
        super.onDestroy();
        timerSyncNef.cancel();
        stopSelf();
        LogUtils.i(TAG, "stopSelf");
    }
}
