/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.UserBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;

import java.util.HashMap;
import java.util.Map;

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
    private double money;
    private Dialog dialog;

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
        balance = Double.parseDouble(application.getCurrentFee());

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

                money = Double.parseDouble(s.toString().trim());
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
//        LogUtils.i(TAG, "RealNameAuth:" + application.getRealNameAuth());
//        if (TextUtils.isEmpty(application.getRealNameAuth())) {
//            MyUtil.showToast(getApplicationContext(), "请进行实名认证");
//            openActivity(EditPersonalInformationActivity.class);
//        } else
//            queryFee();
        if (TextUtils.isEmpty(application.getBankCardNum())) {
            MyUtil.showToast(getApplicationContext(), "请绑定银行卡");
            openActivity(ChangeBankCardNumActivity.class);
            return;
        }
        getUserInfoData();
    }

    private void getUserInfoData() {
        showProgressDialog("请稍后...");
        Map<String, String> data = new HashMap<>();
        data.put("bizName", "10000");
        data.put("method", "10013");
        data.put("userId", application.getUserId());

        appAction.getUserInfo(application.getUserId(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                UserBO userBO = (UserBO) data.getObj();
                if (TextUtils.isEmpty(userBO.getRealNameAuth()) || !userBO.getRealNameAuth().equals("1")) {
                    closeProgressDialog();
                    MyUtil.showToast(getApplicationContext(), "请进行实名认证");
                    openActivity(EditPersonalInformationActivity.class);
                } else
                    queryFee();
            }

            @Override
            public void onFailure(int errorCode, String message) {

            }
        });
    }


    private void queryFee() {
//        showProgressDialog("请稍后...");
        this.appAction.getWithDrawDepositFee(application.getUserId(), money + "", new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                String message = data.getMsg();
                dialog = new AlertDialog.Builder(WithdrawDepositActivity.this).setMessage(message).setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle pBundle = new Bundle();
                        pBundle.putString(getString(R.string.intent_parent), TAG);
                        pBundle.putDouble(getString(R.string.intent_value), money);
                        openActivity(VerifyPhoneNumActivity.class, pBundle);
                        finish();

                    }
                }).setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
                dialog.show();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
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
