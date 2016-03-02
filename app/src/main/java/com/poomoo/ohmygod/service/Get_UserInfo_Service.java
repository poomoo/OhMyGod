package com.poomoo.ohmygod.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.core.AppAction;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.UserBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.application.MyApplication;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.SPUtils;

import java.util.HashMap;
import java.util.Map;

public class Get_UserInfo_Service extends Service {
    private MyApplication application;
    private AppAction appAction;
    private String TAG = "Get_UserInfo_Service";

    public void onCreate() {
        super.onCreate();
        LogUtils.i(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        application = (MyApplication) getApplication();
        appAction = application.getAppAction();
        getUserInfoData();
        return super.onStartCommand(intent, flags, startId);
    }

//    public void onStart(Intent intent, int startId) {
//        application = (MyApplication) getApplication();
//        appAction = application.getAppAction();
//        getUserInfoData();
//    }

    private void getUserInfoData() {
        LogUtils.i(TAG, "getUserInfoData");
        Map<String, String> data = new HashMap<>();
        data.put("bizName", "10000");
        data.put("method", "10013");
        data.put("userId", application.getUserId());

        appAction.getUserInfo(application.getUserId(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                UserBO userBO = (UserBO) data.getObj();
                LogUtils.i(TAG, "同步成功:" + userBO.toString());
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
                application.setAddress(userBO.getAddress());
                application.setIsAdvancedUser(userBO.getIsAdvancedUser());
                application.setUserType(userBO.getUserType());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_userId), application.getUserId());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_phoneNum), application.getTel());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_nickName), application.getNickName());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_realName), application.getRealName());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_headPic), application.getHeadPic());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_currentFee), application.getCurrentFee());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_realNameAuth), application.getRealNameAuth());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_idCardNum), application.getIdCardNum());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_sex), application.getSex());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_age), application.getAge());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_idFrontPic), application.getIdFrontPic());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_idOpsitePic), application.getIdOpsitePic());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_bankCardNum), application.getBankCardNum());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_bankName), application.getBankName());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_address), application.getAddress());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_isAdvancedUser), application.getIsAdvancedUser());
                SPUtils.put(application.getApplicationContext(), getString(R.string.sp_userType), application.getUserType());
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
