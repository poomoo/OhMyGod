/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.WithdrawDepositBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.MyWithdrawDepositAdapter;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.StatusBarUtil;

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
        StatusBarUtil.setColor(this, getResources().getColor(R.color.themeRed), 0);
        addActivityToArrayList(this);
        initView();
        getData();
    }

    protected void initView() {
        initTitleBar();

        listView = (ListView) findViewById(R.id.list_my_withdraw_deposit);
        adapter = new MyWithdrawDepositAdapter(this);
        listView.setAdapter(adapter);

        withdrawDepositBOList = new ArrayList<>();

    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_my_withdraw_deposit);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
    }

    private void getData() {
        showProgressDialog(getString(R.string.dialog_message));
        this.appAction.getMyWithdrawDepositList(application.getUserId(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                withdrawDepositBOList = data.getObjList();
                if (withdrawDepositBOList != null)
                    adapter.setItems(withdrawDepositBOList);
                MyUtil.showToast(getApplicationContext(), data.getMsg());
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    private void initTestData() {

        withdrawDepositBO = new WithdrawDepositBO();
        withdrawDepositBO.setDrawDt("2015年09月15日");
        withdrawDepositBO.setStatus("已提");
        withdrawDepositBO.setDrawFee("￥10.00元");
        withdrawDepositBOList.add(withdrawDepositBO);

        withdrawDepositBO = new WithdrawDepositBO();
        withdrawDepositBO.setDrawDt("2015年08月20日");
        withdrawDepositBO.setStatus("已提");
        withdrawDepositBO.setDrawFee("￥10.00元");
        withdrawDepositBOList.add(withdrawDepositBO);

        withdrawDepositBO = new WithdrawDepositBO();
        withdrawDepositBO.setDrawDt("2015年08月03日");
        withdrawDepositBO.setStatus("已提");
        withdrawDepositBO.setDrawFee("￥10.00元");
        withdrawDepositBOList.add(withdrawDepositBO);

        adapter.setItems(withdrawDepositBOList);
    }


}
