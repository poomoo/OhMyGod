/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;

/**
 * 重置密码
 * 作者: 李苜菲
 * 日期: 2015/11/25 11:58.
 */
public class ResetPassWordActivity extends BaseActivity {
    private EditText passWordEdt;
    private EditText passWordAgainEdt;

    private String passWord;
    private String passWordAgain;
    private String PARENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_passwrod);

        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();

        passWordEdt = (EditText) findViewById(R.id.edt_newPassWord);
        passWordAgainEdt = (EditText) findViewById(R.id.edt_newPassWordAgain);

        PARENT = getIntent().getStringExtra(getString(R.string.intent_parent));
    }

    @Override
    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_resetPassWord);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 完成
     *
     * @param view
     */
    public void toFinish(View view) {
        changePassWord();
    }

    private void changePassWord() {
        passWord = passWordEdt.getText().toString().trim();
        passWordAgain = passWordAgainEdt.getText().toString().trim();
        showProgressDialog("请稍后...");
        this.appAction.changePassWord(application.getTel(), passWord, passWordAgain, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                if (PARENT.equals(getString(R.string.intent_forgetPassWord))) {
                    MyUtil.showToast(getApplicationContext(), "密码重置成功");
                    finish();
                }
                if (PARENT.equals(getString(R.string.intent_changePassWord))) {
                    MyUtil.showToast(getApplicationContext(), "密码重置成功,请重新登录");
                    finish();
                    openActivity(LogInActivity.class);
                    UserInfoActivity.instance.finish();
                    SettingActivity.instance.finish();
                    MainFragmentActivity.instance.finish();
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }
}
