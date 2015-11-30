/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;

/**
 * 登陆
 * 作者: 李苜菲
 * 日期: 2015/11/25 14:28.
 */
public class LogInActivity extends BaseActivity {
    private EditText phoneNumEdt;
    private EditText passWordEdt;
    private CheckBox rememberPassWordChk;

    private String phoneNum;
    private String passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initView();
    }

    @Override
    protected void initView() {
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
        showProgressDialog("登陆中...");
        this.appAction.logIn(phoneNum, passWord, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                SPUtils.put(getApplicationContext(), getString(R.string.sp_phoneNum), phoneNum);
                if (rememberPassWordChk.isChecked()) {
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_rememberPassWord), true);
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_passWord), passWord);
                }
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

    /**
     * 注册
     *
     * @param view
     */
    public void toRegister(View view) {
        openActivity(RegisterActivity.class);
    }

    /**
     * 忘记密码
     *
     * @param view
     */
    public void toForgetPassWord(View view) {
        MyUtil.showToast(getApplicationContext(), "忘记密码");
    }
}
