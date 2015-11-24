/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.ohmygod.R;

/**
 * 提现
 * 作者: 李苜菲
 * 日期: 2015/11/24 09:30.
 */
public class WithdrawDepositActivity extends BaseActivity {
    private TextView balanceTxt;
    private EditText moneyEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_deposit);

        initView();
    }

    protected void initView() {
        initTitleBar();

        balanceTxt = (TextView) findViewById(R.id.txt_account_balance);
        moneyEdt = (EditText) findViewById(R.id.edt_withdraw_deposit_money);
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_withdraw_deposit);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 提现
     *
     * @param view
     */
    public void toWithdrawDeposit(View view) {
        openActivity(VerifyPhoneNumActivity.class);
    }

    /**
     * 提现帮助
     *
     * @param view
     */
    public void toHelp(View view) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_title), getString(R.string.title_withdraw_deposit_help));
        openActivity(WebViewActivity.class, pBundle);
    }
}
