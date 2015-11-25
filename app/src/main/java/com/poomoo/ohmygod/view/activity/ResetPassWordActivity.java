/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;

import com.poomoo.ohmygod.R;

/**
 * 重置密码
 * 作者: 李苜菲
 * 日期: 2015/11/25 11:58.
 */
public class ResetPassWordActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_passwrod);

        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();
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
        finish();
    }
}
