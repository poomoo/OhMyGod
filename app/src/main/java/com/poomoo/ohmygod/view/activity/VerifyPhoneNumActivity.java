/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.service.Get_UserInfo_Service;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.TimeCountDownUtil;

/**
 * 验证手机号
 * 作者: 李苜菲
 * 日期: 2015/11/24 10:33.
 */
public class VerifyPhoneNumActivity extends BaseActivity {
    private TextView remindTxt;
    private EditText codeEdt;
    private Button getCodeBtn;
    private double money;//提现

    private String PARENT;//父activity
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_num);

        initView();
    }

    protected void initView() {
        initTitleBar();

        PARENT = getIntent().getStringExtra(getString(R.string.intent_parent));
        remindTxt = (TextView) findViewById(R.id.txt_remind);
        codeEdt = (EditText) findViewById(R.id.edt_code);
        getCodeBtn = (Button) findViewById(R.id.btn_getCode);

        CharSequence text = getString(R.string.label_verify_phone_num, application.getTel());
        remindTxt.setText(text);
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_verify_phone_num);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 获取验证码
     *
     * @param view
     */
    public void toGetCode(View view) {
        TimeCountDownUtil timeCountDownUtil = new TimeCountDownUtil(MyConfig.SMSCOUNTDOWNTIME, MyConfig.COUNTDOWNTIBTERVAL, getCodeBtn, null);
        timeCountDownUtil.start();
        getCode();
    }

    /**
     * 下一步
     *
     * @param view
     */
    public void toNext(View view) {
        if (PARENT.equals("ChangeIdCardInfoActivity")) {
            openActivity(IdCardInfoActivity.class);
            finish();
        }
        if (PARENT.equals("ChangeBankCardInfoActivity")) {
            openActivity(BankCardActivity.class);
            finish();
        }

        //提现
        if (PARENT.equals("WithdrawDepositActivity")) {
            checkCode();
        }
    }

    private void getCode() {
        this.appAction.getCode(application.getTel(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                MyUtil.showToast(getApplicationContext(), "发送验证码成功");
            }

            @Override
            public void onFailure(int errorCode, String message) {
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    private void checkCode() {
        code = codeEdt.getText().toString().trim();
        showProgressDialog("请稍后...");
        this.appAction.checkCode(application.getTel(), code, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                money = getIntent().getDoubleExtra(getString(R.string.intent_value), 0);
                withDrawDeposit();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    /**
     * 提现
     */
    private void withDrawDeposit() {
        this.appAction.withDrawDeposit(application.getUserId(), money + "", new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                Bundle pBundle = new Bundle();
                pBundle.putDouble(getString(R.string.intent_value), money);
                openActivity(WithdrawDepositDetailsActivity.class, pBundle);
                startService(new Intent(VerifyPhoneNumActivity.this, Get_UserInfo_Service.class));
                finish();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                finish();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }
}
