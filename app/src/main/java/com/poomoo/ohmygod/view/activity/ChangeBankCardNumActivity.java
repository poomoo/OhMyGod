/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;

/**
 * 修改银行卡号
 * 作者: 李苜菲
 * 日期: 2015/11/25 10:46.
 */
public class ChangeBankCardNumActivity extends BaseActivity {
    private EditText bankCardNumEdt;//银行卡号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_bankcard_num);

        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();

        bankCardNumEdt = (EditText) findViewById(R.id.edt_bankCardNum);
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
            }
        });
    }

    /**
     * 下一步
     *
     * @param view
     */
    public void toNext(View view) {
        openActivity(ChangeBankCardInfoActivity.class);
        finish();
    }
}
