/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.WinnerListBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.ActivityWinnerListAdapter;
import com.poomoo.ohmygod.utils.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2015/12/24 17:00.
 */
public class WinnerListActivity extends BaseActivity {
    private TextView titleTxt;
    private TextView statementTxt;
    private ListView listView;

    private ActivityWinnerListAdapter adapter;
    private List<WinnerListBO> winnerListBOList = new ArrayList<>();
    private WinnerListBO winnerListBO = new WinnerListBO();
    private int activeId;
    private String activityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winnner_list);

        initView();
        activeId = getIntent().getIntExtra(getString(R.string.intent_activeId), 0);
        activityName = getIntent().getStringExtra(getString(R.string.intent_activityName));
        getData();
    }

    @Override
    protected void initView() {
        initTitleBar();

        titleTxt = (TextView) findViewById(R.id.txt_winnerListTitle);
        statementTxt = (TextView) findViewById(R.id.txt_winnerListStatement);
        listView = (ListView) findViewById(R.id.list_winner);

        titleTxt.setText(activityName);

        adapter = new ActivityWinnerListAdapter(this);
        listView.setAdapter(adapter);
    }

    @Override
    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_winner_list);
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
        this.appAction.getActivityWinnerList(activeId + "", new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                winnerListBOList = data.getObjList();
                winnerListBO = (WinnerListBO) data.getObj();
                adapter.setItems(winnerListBOList);
                statementTxt.setText(winnerListBO.getWinMsg());
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }
}
