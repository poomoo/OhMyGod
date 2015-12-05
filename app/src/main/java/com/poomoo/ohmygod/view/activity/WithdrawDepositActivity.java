/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.LogUtils;

/**
 * 提现
 * 作者: 李苜菲
 * 日期: 2015/11/24 09:30.
 */
public class WithdrawDepositActivity extends BaseActivity {
    private TextView balanceTxt;
    private TextView isEnoughTxt;
    private EditText moneyEdt;
    private Button button;
    private double balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_deposit);

        initView();
    }

    protected void initView() {
        initTitleBar();

        balanceTxt = (TextView) findViewById(R.id.txt_account_balance);
        isEnoughTxt = (TextView) findViewById(R.id.txt_balanceIsEnough);
        moneyEdt = (EditText) findViewById(R.id.edt_withdraw_deposit_money);
        button = (Button) findViewById(R.id.btn_withDrawDeposit);

        button.setBackgroundResource(R.drawable.bg_open_activity_pressed);
        balanceTxt.setText("￥" + application.getCurrentFee());
//        balance = Double.parseDouble(application.getCurrentFee());

        balance = 10;
        moneyEdt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO 自动生成的方法存根
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO 自动生成的方法存根

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO 自动生成的方法存根
                String temp = s.toString();
                int posDot = temp.indexOf(".");
                if (posDot > 0)
                    if (temp.length() - posDot - 1 > 2)
                        s.delete(posDot + 3, posDot + 4);


                if (s.length() == 0) {
                    isEnoughTxt.setVisibility(View.GONE);
                    button.setClickable(false);
                    button.setBackgroundResource(R.drawable.bg_open_activity_pressed);
                    return;
                }

                double money = Double.parseDouble(s.toString().trim());
                if (money == 0) {
                    isEnoughTxt.setVisibility(View.GONE);
                    button.setClickable(false);
                    button.setBackgroundResource(R.drawable.bg_open_activity_pressed);
                    return;
                }


                if (money > balance) {
                    isEnoughTxt.setVisibility(View.VISIBLE);
                    isEnoughTxt.setText("余额不足");
                    button.setClickable(false);
                    button.setBackgroundResource(R.drawable.bg_open_activity_pressed);
                }
//                else if (money > allow_fee) {
//                    isEnoughTxt.setVisibility(View.VISIBLE);
//                    isEnoughTxt.setText(eDaoClientConfig.moreBalance);
//                    button.setClickable(false);
//                    button.setBackgroundResource(R.drawable.style_btn_no_background);
//                }
                else {
                    isEnoughTxt.setVisibility(View.GONE);
                    button.setClickable(true);
                    button.setBackgroundResource(R.drawable.selector_grab_button_open_activity);
                }
            }
        });
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
        pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_withDrawDeposit));
        openActivity(WebViewActivity.class, pBundle);
    }
}
