/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;

import com.poomoo.ohmygod.R;

/**
 * 换绑手机1
 * 作者: 李苜菲
 * 日期: 2015/11/24 16:30.
 */
public class VerifyPhoneNum2Activity extends BaseActivity {
    private String PARENT;//父activity
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_num2);

        PARENT = getIntent().getStringExtra(getString(R.string.intent_parent));
        if (PARENT.equals(getString(R.string.intent_phone)))
            title = getString(R.string.title_changePhone);
        if (PARENT.equals(getString(R.string.intent_passWord)))
            title = getString(R.string.title_safe);
        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();
    }

    @Override
    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(title);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 下一步
     *
     * @param view
     */
    public void toNext(View view) {
        if (PARENT.equals(getString(R.string.intent_phone)))
            openActivity(ChangePhone2Activity.class);
        if (PARENT.equals(getString(R.string.intent_passWord)))
            openActivity(ResetPassWordActivity.class);

        finish();
    }
}
