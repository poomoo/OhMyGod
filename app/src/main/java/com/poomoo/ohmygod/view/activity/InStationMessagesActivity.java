/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.MessageBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.InStationMessagesAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.custom.RefreshLayout;
import com.poomoo.ohmygod.view.custom.RefreshLayout.OnLoadListener;
import com.poomoo.ohmygod.view.fragment.GrabFragment;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 站内信息
 * 作者: 李苜菲
 * 日期: 2015/11/24 11:03.
 */
public class InStationMessagesActivity extends BaseActivity implements OnLoadListener, OnRefreshListener, OnItemClickListener {
    private RefreshLayout refreshLayout;
    private ListView listView;
    private InStationMessagesAdapter adapter;
    private List<MessageBO> messageBOList;
    private MessageBO messageBO;
    private boolean isLoad = false;//true 加载 false刷新
    private int currPage = 1;
    private String PARENT;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_station_messages);

        initView();

    }

    protected void initView() {
        initTitleBar();
        refreshLayout = (RefreshLayout) findViewById(R.id.refresh_inStationMessage);
        listView = (ListView) findViewById(R.id.list_in_station_messages);

        adapter = new InStationMessagesAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
//        initTestData();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);

        PARENT = getIntent().getStringExtra(getString(R.string.intent_parent));
        if (PARENT.equals(getString(R.string.intent_pubMessage))) {
            type = "5";
            currPage = 2;
            messageBOList = (ArrayList) getIntent().getSerializableExtra(getString(R.string.intent_value));
            adapter.setItems(messageBOList);
        }
        if (PARENT.equals(getString(R.string.intent_innerMessage))) {
            type = "8";
            showProgressDialog("请稍后...");
            getData();
        }

    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_in_station_messages);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
    }

    private void initTestData() {
        messageBOList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            messageBO = new MessageBO();
            messageBOList.add(messageBO);
        }
        adapter.setItems(messageBOList);
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
        refreshLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据
                getData();
            }
        }, 0);
    }

    private void getData() {
        //--1：注册声明，2：游戏规则声明，3返现声明，4提现帮助，5公共消息,6签到声明,7关于,8站内消息,9用户帮助
        this.appAction.getMessages(type, currPage, MyConfig.PAGESIZE, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                currPage++;
                // 更新完后调用该方法结束刷新
                if (isLoad) {
                    refreshLayout.setLoading(false);
                    for (int i = 0; i < data.getObjList().size(); i++)
                        messageBOList.add((MessageBO) data.getObjList().get(i));
                    adapter.addItems(messageBOList);
                } else {
                    currPage = 1;
                    currPage++;
                    refreshLayout.setRefreshing(false);
                    messageBOList = new ArrayList<>();
                    messageBOList = data.getObjList();
                    adapter.setItems(messageBOList);
                }
                MyUtil.showToast(getApplicationContext(), data.getMsg());
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int statementId = messageBOList.get(position).getStatementId();
        if (!MyUtil.isRead(statementId)) {
            MyUtil.updateMessageInfo(statementId);
            GrabFragment.instance.updateInfoCount();
        }
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.intent_parent), getString(R.string.intent_message));
        bundle.putInt(getString(R.string.intent_value), messageBOList.get(position).getStatementId());
        bundle.putString(getString(R.string.intent_title), messageBOList.get(position).getTitle());
        openActivity(WebViewActivity.class, bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
