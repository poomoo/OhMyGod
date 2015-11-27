/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.utils.MyUtil;

/**
 * 注册
 * 作者: 李苜菲
 * 日期: 2015/11/25 14:52.
 */
public class RegisterActivity extends BaseActivity {
    private EditText phoneNumEdt;
    private EditText codeEdt;
    private EditText passWordEdt;
    private CheckBox agreeChk;
    private Button codeBtn;
    private TextView ageTxt;
    private TextView genderTxt;

    private String phoneNum;
    private String passWord;
    private String code;
    private String age;
    private String sex;
    private String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    @Override
    protected void initView() {
        phoneNumEdt = (EditText) findViewById(R.id.edt_register_phoneNum);
        codeEdt = (EditText) findViewById(R.id.edt_register_code);
        passWordEdt = (EditText) findViewById(R.id.edt_register_passWord);
        codeBtn = (Button) findViewById(R.id.btn_register_code);
        ageTxt = (TextView) findViewById(R.id.txt_age);
        genderTxt = (TextView) findViewById(R.id.txt_gender);
        agreeChk = (CheckBox) findViewById(R.id.chk_agree);
    }

    /**
     * @param view
     */
    public void toCode(View view) {
        phoneNum = phoneNumEdt.getText().toString().trim();

        this.appAction.getCode(phoneNum, new ActionCallbackListener<ResponseBO>() {
            @Override
            public void onSuccess(ResponseBO data) {
                MyUtil.showToast(getApplicationContext(), data.getMsg());
            }

            @Override
            public void onFailure(int errorCode, String message) {
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
        if (!agreeChk.isChecked()) {
            MyUtil.showToast(getApplicationContext(), "请同意注册协议");
            return;
        }
        phoneNum = phoneNumEdt.getText().toString().trim();
        code = codeEdt.getText().toString().trim();
        age = ageTxt.getText().toString().trim();
        sex = genderTxt.getText().toString().trim();
        passWord=passWordEdt.getText().toString().trim();
        if (sex.equals("男"))
            sex = "1";
        else
            sex = "2";
        imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();

        showProgressDialog("注册中...");
        this.appAction.register(phoneNum, passWord, code, age, sex, imei, new ActionCallbackListener<Void>() {
            @Override
            public void onSuccess(Void data) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), "注册成功");
                finish();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    /**
     * 选择年龄
     *
     * @param view
     */
    public void toAge(View view) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_age));
        openActivityForResult(SelectActivity.class, pBundle, MyConfig.AGE);
    }

    /**
     * 选择性别
     *
     * @param view
     */
    public void toGender(View view) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_gender));
        openActivityForResult(SelectActivity.class, pBundle, MyConfig.GENDER);
    }

    /**
     * 协议
     *
     * @param view
     */
    public void toProtocol(View view) {
        MyUtil.showToast(getApplicationContext(), "协议");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyConfig.AGE && resultCode == MyConfig.AGE) {
            ageTxt.setText(data.getStringExtra(getString(R.string.intent_age)));
        }
        if (requestCode == MyConfig.GENDER && resultCode == MyConfig.GENDER) {
            genderTxt.setText(data.getStringExtra(getString(R.string.intent_gender)));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
