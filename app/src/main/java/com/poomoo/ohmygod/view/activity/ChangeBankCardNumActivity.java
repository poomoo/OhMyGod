/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;

/**
 * 修改银行卡号
 * 作者: 李苜菲
 * 日期: 2015/11/25 10:46.
 */
public class ChangeBankCardNumActivity extends BaseActivity {
    private TextView backCardNameTxt;//持卡人姓名
    private EditText bankCardNumEdt;//银行卡号
    private String bankCardNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bankcard_num);
        addActivityToArrayList(this);
        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();

        backCardNameTxt = (TextView) findViewById(R.id.txt_bankCardName);
        bankCardNumEdt = (EditText) findViewById(R.id.edt_bankCardNum);

        backCardNameTxt.setText(application.getRealName());
        bankCardNumEdt.setText(application.getBankCardNum());
        MyUtil.fortmatCardNum(bankCardNumEdt);
    }

    @Override
    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_changeBankCardNum);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
    }

    /**
     * 下一步
     *
     * @param view
     */
    public void toNext(View view) {
        if (checkInput()) {
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.intent_value), bankCardNum);
            bundle.putString(getString(R.string.intent_parent), getString(R.string.intent_bankCard));
            openActivity(VerifyPhoneNum2Activity.class, bundle);
            finish();
            getActivityInFromRight();
        }

    }

    public boolean checkInput() {
        bankCardNum = MyUtil.trimAll(bankCardNumEdt.getText().toString());
        if (TextUtils.isEmpty(bankCardNum)) {
            MyUtil.showToast(getApplicationContext(), "请填写银行卡号");
            return false;
        }
        if (bankCardNum.length() != 19) {
            bankCardNumEdt.setFocusable(true);
            bankCardNumEdt.requestFocus();
            MyUtil.showToast(getApplicationContext(), "请输入19位有效卡号");
            return false;
        }
        return true;
    }
}
