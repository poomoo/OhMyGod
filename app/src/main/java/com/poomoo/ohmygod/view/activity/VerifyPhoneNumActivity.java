/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.poomoo.ohmygod.R;

/**
 * 验证手机号
 * 作者: 李苜菲
 * 日期: 2015/11/24 10:33.
 */
public class VerifyPhoneNumActivity extends BaseActivity {
    private String PARENT;//父activity
    private TextView remindTxt;

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
        } else
            openActivity(WithdrawDepositDetailsActivity.class);
    }
}
