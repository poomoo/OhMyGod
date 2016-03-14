/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.WinnerListBO;
import com.poomoo.model.WinningRecordsBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.ActivityWinnerListAdapter;
import com.poomoo.ohmygod.adapter.ActivityWinnerListAdapterOld;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.custom.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2015/12/24 17:00.
 */
public class WinnerListActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    private TextView titleTxt;
    private TextView statementTxt;
    private RefreshLayout refreshLayout;
    private ListView listView;

    private ActivityWinnerListAdapterOld adapter;
    private List<WinnerListBO> winnerListBOList = new ArrayList<>();
    private WinnerListBO winnerListBO = new WinnerListBO();
    private int activeId;
    private String activityName;
    private int currPage = 1;
    private boolean isLoad = false;//true 加载 false刷新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winnner_list);
        addActivityToArrayList(this);
        initView();
        activeId = getIntent().getIntExtra(getString(R.string.intent_activeId), 0);
        activityName = getIntent().getStringExtra(getString(R.string.intent_activityName));
        showProgressDialog(getString(R.string.dialog_message));
        getData();
    }

    @Override
    protected void initView() {
        initTitleBar();

        titleTxt = (TextView) findViewById(R.id.txt_winnerListTitle);
        statementTxt = (TextView) findViewById(R.id.txt_winnerListStatement);
        listView = (ListView) findViewById(R.id.list_winner);
        refreshLayout = (RefreshLayout) findViewById(R.id.refresh_winnerList);

        refreshLayout.setOnLoadListener(this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setRefreshing(false);

        titleTxt.setText(activityName);

        adapter = new ActivityWinnerListAdapterOld(this);
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
        this.appAction.getActivityWinnerList(activeId + "", currPage, MyConfig.PAGESIZE, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                winnerListBOList = new ArrayList<>();
                if (isLoad) {
                    refreshLayout.setLoading(false);
                    int len = data.getObjList().size();
                    for (int i = 0; i < len; i++) {
                        winnerListBOList.add((WinnerListBO) data.getObjList().get(i));
                    }
                    if (len > 0) {
                        currPage++;
                        adapter.addItems(winnerListBOList);
                    }
                } else {
                    refreshLayout.setRefreshing(false);
                    currPage++;
                    winnerListBOList = data.getObjList();
                    winnerListBO = (WinnerListBO) data.getObj();
                    adapter.setItems(winnerListBOList);
                    statementTxt.setText(winnerListBO.getWinMsg());
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                if (isLoad)
                    refreshLayout.setLoading(false);
                else
                    refreshLayout.setRefreshing(false);
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    @Override
    public void onLoad() {
        isLoad = true;
        refreshLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据
                getData();
            }
        }, 0);
    }

    @Override
    public void onRefresh() {
        isLoad = false;
        currPage = 1;
        refreshLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据
                getData();
            }
        }, 0);
    }
}
