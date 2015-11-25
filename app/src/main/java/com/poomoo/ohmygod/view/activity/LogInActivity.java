/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;

/**
 * 登陆
 * 作者: 李苜菲
 * 日期: 2015/11/25 14:28.
 */
public class LogInActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        initView();
    }

    @Override
    protected void initView() {
    }

    /**
     * 登陆
     *
     * @param view
     */
    public void toLogin(View view) {
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
