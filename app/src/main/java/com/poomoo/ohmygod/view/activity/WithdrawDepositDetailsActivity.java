/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.poomoo.ohmygod.R;

/**
 * 提现详情
 * 作者: 李苜菲
 * 日期: 2015/11/24 10:56.
 */
public class WithdrawDepositDetailsActivity extends BaseActivity {
    private TextView cardInfoTxt;
    private TextView moneyTxt;
    private double money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_deposit_details);

        initView();
    }

    protected void initView() {
        initTitleBar();

        cardInfoTxt = (TextView) findViewById(R.id.txt_cardInfo);
        moneyTxt = (TextView) findViewById(R.id.txt_withdraw_deposit_money);

        money = getIntent().getDoubleExtra(getString(R.string.intent_value), 0);
        moneyTxt.setText(money + "");
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_withdraw_deposit_details);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
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
        getActivityOutToRight();
    }
}
