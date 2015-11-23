/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.poomoo.model.WithdrawDepositBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.MyWithdrawDepositAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的提现
 * 作者: 李苜菲
 * 日期: 2015/11/23 17:17.
 */
public class MyWithdrawDepositActivity extends BaseActivity {
    private ListView listView;
    private List<WithdrawDepositBO> withdrawDepositBOList;
    private MyWithdrawDepositAdapter adapter;
    private WithdrawDepositBO withdrawDepositBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_withdraw_deposit);

        initView();
    }

    private void initView() {
        initTitleBar();

        listView = (ListView) findViewById(R.id.list_my_withdraw_deposit);
        adapter = new MyWithdrawDepositAdapter(this);
        listView.setAdapter(adapter);

        initTestData();

    }

    private void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_my_withdraw_deposit);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initTestData() {
        withdrawDepositBOList = new ArrayList<>();
        withdrawDepositBO = new WithdrawDepositBO();
        withdrawDepositBO.setDateTime("2015年09月15日");
        withdrawDepositBO.setStatus("已提");
        withdrawDepositBO.setAccount("￥10.00元");
        withdrawDepositBOList.add(withdrawDepositBO);

        withdrawDepositBO = new WithdrawDepositBO();
        withdrawDepositBO.setDateTime("2015年08月20日");
        withdrawDepositBO.setStatus("已提");
        withdrawDepositBO.setAccount("￥10.00元");
        withdrawDepositBOList.add(withdrawDepositBO);

        withdrawDepositBO = new WithdrawDepositBO();
        withdrawDepositBO.setDateTime("2015年08月03日");
        withdrawDepositBO.setStatus("已提");
        withdrawDepositBO.setAccount("￥10.00元");
        withdrawDepositBOList.add(withdrawDepositBO);

        adapter.setItems(withdrawDepositBOList);
    }


}
