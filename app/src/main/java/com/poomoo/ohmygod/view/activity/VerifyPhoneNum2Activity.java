/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;
import com.poomoo.ohmygod.utils.TimeCountDownUtil;

/**
 * 换绑手机1
 * 作者: 李苜菲
 * 日期: 2015/11/24 16:30.
 */
public class VerifyPhoneNum2Activity extends BaseActivity {
    private TextView telTxt;
    private TextView
            codeTxt;
    private EditText codeEdt;

    private String PARENT;//父activity
    private String title;
    private String code;
    private String key;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_num2);

        PARENT = getIntent().getStringExtra(getString(R.string.intent_parent));
        if (PARENT.equals(getString(R.string.intent_phone)))
            title = getString(R.string.title_changePhone);
        if (PARENT.equals(getString(R.string.intent_forgetPassWord)) || PARENT.equals(getString(R.string.intent_changePassWord)) || PARENT.equals(getString(R.string.intent_bankCard))) {
            title = getString(R.string.title_safe);
        }

        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();

        telTxt = (TextView) findViewById(R.id.txt_verify_phoneNum);
        codeTxt = (TextView) findViewById(R.id.txt_verify_code);
        codeEdt = (EditText) findViewById(R.id.edt_verify_code);

        telTxt.setText(MyUtil.hiddenTel(application.getTel()));
        if (PARENT.equals(getString(R.string.intent_forgetPassWord)) || PARENT.equals(getString(R.string.intent_changePassWord)) || PARENT.equals(getString(R.string.intent_bankCard))) {
            getCode();
        }
    }

    @Override
    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(title);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
    }

    /**
     * 获取验证码
     *
     * @param view
     */

    public void toGetCode(View view) {
        getCode();
    }

    public void getCode() {
        codeTxt.setTag("TextView");
        TimeCountDownUtil timeCountDownUtil = new TimeCountDownUtil(MyConfig.SMSCOUNTDOWNTIME, MyConfig.COUNTDOWNTIBTERVAL, codeTxt);
        timeCountDownUtil.start();
        this.appAction.getCode(application.getTel(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                MyUtil.showToast(getApplicationContext(), "发送验证码成功");
            }

            @Override
            public void onFailure(int errorCode, String message) {
                MyUtil.showToast(getApplicationContext(), "发送验证码失败:" + message);
            }
        });
    }

    /**
     * 下一步
     *
     * @param view
     */
    public void toNext(View view) {
        if (PARENT.equals(getString(R.string.intent_phone))) {
            openActivity(ChangePhone2Activity.class);
            finish();
            getActivityOutToRight();
        }

        if (PARENT.equals(getString(R.string.intent_forgetPassWord)) || PARENT.equals(getString(R.string.intent_changePassWord)) || PARENT.equals(getString(R.string.intent_bankCard))) {
            checkCode();
        }
    }

    private void checkCode() {
        code = codeEdt.getText().toString().trim();
        showProgressDialog("请稍后...");
        this.appAction.checkCode(application.getTel(), code, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                if (PARENT.equals(getString(R.string.intent_forgetPassWord)) || PARENT.equals(getString(R.string.intent_changePassWord))) {
                    closeProgressDialog();
                    Bundle bundle = new Bundle();
                    bundle.putString(getString(R.string.intent_parent), PARENT);
                    openActivity(ResetPassWordActivity.class);
                    finish();
                    getActivityOutToRight();
                } else {
                    toSubmit();
                }

            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    /**
     * 提交
     */
    private void toSubmit() {
        key = "bankCardNum";
        value = getIntent().getStringExtra(getString(R.string.intent_value));

        this.appAction.changePersonalInfo(this.application.getUserId(), key, value, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                application.setBankCardNum(value);
                SPUtils.put(getApplicationContext(), getString(R.string.sp_bankCardNum), value);
                MyUtil.showToast(getApplicationContext(), "修改银行卡成功");
                finish();
                getActivityOutToRight();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }
}
