/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;

import com.poomoo.ohmygod.R;

/**
 * 修改银行卡信息
 * 作者: 李苜菲
 * 日期: 2015/11/25 11:14.
 */
public class ChangeBankCardInfoActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bankcard_info);

        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();
    }

    @Override
    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_changeBankCardInfo);
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
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_parent), getClass().getSimpleName());
        openActivity(VerifyPhoneNumActivity.class, pBundle);
        finish();
    }
}
