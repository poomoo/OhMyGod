/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.RebateBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.WithdrawDepositBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.MyRebateAdapter;
import com.poomoo.ohmygod.adapter.MyWithdrawDepositAdapter;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 返现记录
 * 作者: 李苜菲
 * 日期: 2015/11/23 17:17.
 */
public class RebateRecordsActivity extends BaseActivity {
    private ListView listView;
    private List<RebateBO> rebateBOList;
    private MyRebateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rebate_records);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.themeRed), 0);
        addActivityToArrayList(this);
        initView();
        getData();
    }

    protected void initView() {
        initTitleBar();

        listView = (ListView) findViewById(R.id.list_myRebateRecords);
        adapter = new MyRebateAdapter(this);
        listView.setAdapter(adapter);
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_my_rebate_records);
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
        this.appAction.getRebateInfo(application.getUserId(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                rebateBOList = new ArrayList<>();
                rebateBOList = data.getObjList();
                if (rebateBOList != null && rebateBOList.size() > 0)
                    adapter.setItems(rebateBOList);
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

}
