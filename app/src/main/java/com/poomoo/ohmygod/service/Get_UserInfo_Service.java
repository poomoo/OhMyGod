package com.poomoo.ohmygod.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.core.AppAction;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.UserBO;
import com.poomoo.ohmygod.application.MyApplication;
import com.poomoo.ohmygod.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

public class Get_UserInfo_Service extends Service {
    private MyApplication application;
    private AppAction appAction;

    public void onCreate() {
        super.onCreate();
    }

    public void onStart(Intent intent, int startId) {
        application = (MyApplication) getApplication();
        appAction = application.getAppAction();
        getUserInfoData();
    }

    private void getUserInfoData() {
        Map<String, String> data = new HashMap<String, String>();
        data.put("bizName", "10000");
        data.put("method", "10013");
        data.put("userId", application.getUserId());

        appAction.getUserInfo(application.getUserId(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                UserBO userBO = (UserBO) data.getObj();
                application.setTel(userBO.getTel());
                application.setNickName(userBO.getNickName());
                application.setRealName(userBO.getRealName());
                application.setHeadPic(userBO.getHeadPic());
                application.setCurrentFee(userBO.getCurrentFee());
                application.setRealNameAuth(userBO.getRealNameAuth());
                application.setIdCardNum(userBO.getIdCardNum());
                application.setSex(userBO.getSex());
                application.setAge(userBO.getAge());
                application.setIdFrontPic(userBO.getIdFrontPic());
                application.setBankCardNum(userBO.getBankCardNum());
                application.setBankName(userBO.getBankName());
                SPUtils.put(application.getApplicationContext(), "userId", application.getUserId());
                SPUtils.put(application.getApplicationContext(), "tel", application.getTel());
                SPUtils.put(application.getApplicationContext(), "nickName", application.getNickName());
                SPUtils.put(application.getApplicationContext(), "realName", application.getRealName());
                SPUtils.put(application.getApplicationContext(), "headPic", application.getHeadPic());
                SPUtils.put(application.getApplicationContext(), "currentFee", application.getCurrentFee());
                SPUtils.put(application.getApplicationContext(), "realNameAuth", application.getRealNameAuth());
                SPUtils.put(application.getApplicationContext(), "idCardNum", application.getIdCardNum());
                SPUtils.put(application.getApplicationContext(), "sex", application.getSex());
                SPUtils.put(application.getApplicationContext(), "age", application.getAge());
                SPUtils.put(application.getApplicationContext(), "idFrontPic", application.getIdFrontPic());
                SPUtils.put(application.getApplicationContext(), "idOpsitePic", application.getIdOpsitePic());
                SPUtils.put(application.getApplicationContext(), "bankCardNum", application.getBankCardNum());
                SPUtils.put(application.getApplicationContext(), "bankName", application.getBankName());
                stopSelf();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                stopSelf();
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
    }
}
