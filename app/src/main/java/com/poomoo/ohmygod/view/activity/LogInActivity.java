/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.core.ErrorEvent;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.UserBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 登陆
 * 作者: 李苜菲
 * 日期: 2015/11/25 14:28.
 */
public class LogInActivity extends BaseActivity implements View.OnClickListener {
    private TextView registerTxt;
    private TextView forgetPassWordTxt;
    private EditText phoneNumEdt;
    private EditText passWordEdt;
    private CheckBox rememberPassWordChk;

    private String phoneNum;
    private String passWord;

    public static LogInActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        instance = this;
        addActivityToArrayList(this);
        initView();
    }

    @Override
    protected void initView() {
        registerTxt = (TextView) findViewById(R.id.txt_register);
        forgetPassWordTxt = (TextView) findViewById(R.id.txt_forgetPassWord);

        registerTxt.setOnClickListener(this);
        forgetPassWordTxt.setOnClickListener(this);
        phoneNumEdt = (EditText) findViewById(R.id.edt_login_phoneNum);
        passWordEdt = (EditText) findViewById(R.id.edt_login_passWord);
        rememberPassWordChk = (CheckBox) findViewById(R.id.chk_rememberPassWord);

        phoneNumEdt.setText((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_phoneNum), ""));
        if ((boolean) SPUtils.get(getApplicationContext(), getString(R.string.sp_rememberPassWord), false)) {
            rememberPassWordChk.setChecked(true);
            passWordEdt.setText((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_passWord), ""));
        }
    }

    /**
     * 登陆
     *
     * @param view
     */
    public void toLogin(View view) {
        phoneNum = phoneNumEdt.getText().toString().trim();
        passWord = passWordEdt.getText().toString().trim();
        showProgressDialog("登录中...");
        this.appAction.logIn(phoneNum, passWord, application.getChannelId(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                UserBO userBO = (UserBO) data.getObj();
                SPUtils.put(getApplicationContext(), getString(R.string.sp_isLogin), true);
                if (rememberPassWordChk.isChecked()) {
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_rememberPassWord), true);
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_passWord), passWord);
                }
                Log.i(TAG, "data:" + userBO);
                setAppInfo(userBO);
                openActivity(MainFragmentActivity.class);
                finish();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_register:
                toRegister();
                break;
            case R.id.txt_forgetPassWord:
                toForgetPassWord();
                break;
        }
    }

    /**
     * 注册
     */
    private void toRegister() {
        openActivity(RegisterActivity.class);
    }

    /**
     * 忘记密码
     */
    private void toForgetPassWord() {
        phoneNum = phoneNumEdt.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            MyUtil.showToast(getApplicationContext(), "请输入手机号");
            return;
        }
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(phoneNum);
        if (!matcher.matches()) {
            MyUtil.showToast(getApplicationContext(), "手机号不正确");
            return;
        }

        application.setTel(phoneNum);
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.intent_parent), getString(R.string.intent_forgetPassWord));
        openActivity(VerifyPhoneNum2Activity.class, bundle);
    }

    private void setAppInfo(UserBO userBO) {
        this.application.setUserId(userBO.getUserId());
        this.application.setTel(userBO.getTel());
        this.application.setNickName(userBO.getNickName());
        this.application.setRealName(userBO.getRealName());
        this.application.setHeadPic(userBO.getHeadPic());
        this.application.setCurrentFee(userBO.getCurrentFee());
        this.application.setRealNameAuth(userBO.getRealNameAuth());
        this.application.setIdCardNum(userBO.getIdCardNum());
        this.application.setSex(userBO.getSex());
        this.application.setAge(userBO.getAge());
        this.application.setIdFrontPic(userBO.getIdFrontPic());
        this.application.setIdOpsitePic(userBO.getIdOpsitePic());
        this.application.setBankCardNum(userBO.getBankCardNum());
        this.application.setBankName(userBO.getBankName());
        this.application.setAddress(userBO.getAddress());
        this.application.setIsAdvancedUser(userBO.getIsAdvancedUser());
        this.application.setChannelId(userBO.getChannelId());
        this.application.setUserType(userBO.getUserType());

//        LogUtils.i(TAG, "userBO.getTel():" + userBO.getTel() + " phoneNum:" + SPUtils.get(getApplicationContext(), getString(R.string.sp_phoneNum), ""));
        if (!userBO.getTel().equals(SPUtils.get(getApplicationContext(), getString(R.string.sp_phoneNum), ""))) {
            SPUtils.put(getApplicationContext(), getString(R.string.sp_headPicBitmap), "");
            LogUtils.i(TAG, "电话不一样，清除头像缓存:" + SPUtils.get(getApplicationContext(), getString(R.string.sp_headPicBitmap), "123"));
        }
        SPUtils.put(getApplicationContext(), getString(R.string.sp_userId), userBO.getUserId());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_phoneNum), userBO.getTel());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_nickName), userBO.getNickName());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_realName), userBO.getRealName());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_headPic), userBO.getHeadPic());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_currentFee), userBO.getCurrentFee());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_realNameAuth), userBO.getRealNameAuth());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_idCardNum), userBO.getIdCardNum());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_sex), userBO.getSex());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_age), userBO.getAge());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_idFrontPic), userBO.getIdFrontPic());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_idOpsitePic), userBO.getIdOpsitePic());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_bankCardNum), userBO.getBankCardNum());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_bankName), userBO.getBankName());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_address), userBO.getAddress());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_isAdvancedUser), userBO.getIsAdvancedUser());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_channelId), userBO.getChannelId());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_userType), userBO.getUserType());
    }
}
