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
 * 换绑手机3
 * 作者: 李苜菲
 * 日期: 2015/11/24 16:30.
 */
public class ChangePhone3Activity extends BaseActivity {
    private TextView tipTxt;
    private TextView codeTxt;
    private EditText codeEdt;

    private String tel;
    private String code;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone3);

        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();

        tipTxt = (TextView) findViewById(R.id.txt_tip);
        codeTxt = (TextView) findViewById(R.id.txt_code);
        codeEdt = (EditText) findViewById(R.id.edt_code);

        tel = getIntent().getStringExtra(getString(R.string.intent_value));
        String msg = String.format(getResources().getString(R.string.label_newCode), tel);
        tipTxt.setText(msg);

        getCode();
    }

    @Override
    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_changePhone);
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
        TimeCountDownUtil timeCountDownUtil = new TimeCountDownUtil(MyConfig.SMSCOUNTDOWNTIME, MyConfig.COUNTDOWNTIBTERVAL, codeTxt,"2");
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
     * 完成
     *
     * @param view
     */
    public void toFinish(View view) {
        checkCode();
    }


    private void checkCode() {
        code = codeEdt.getText().toString().trim();
        showProgressDialog(getString(R.string.dialog_message));
        this.appAction.checkCode(application.getTel(), code, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                toSubmit();
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
        key = "tel";

        this.appAction.changePersonalInfo(this.application.getUserId(), key, tel, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                application.setTel(tel);
                SPUtils.put(getApplicationContext(), getString(R.string.sp_phoneNum), tel);
                MyUtil.showToast(getApplicationContext(), "修改手机成功,请重新登录");
                openActivity(LogInActivity.class);
                finish();
                UserInfoActivity.instance.finish();
                SettingActivity.instance.finish();
                MainFragmentActivity.instance.finish();

            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }
}
