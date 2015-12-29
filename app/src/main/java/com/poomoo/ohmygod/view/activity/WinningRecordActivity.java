/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.WinningRecordsBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.WinningRecordAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.custom.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 中奖记录
 * 作者: 李苜菲
 * 日期: 2015/11/23 13:40.
 */
public class WinningRecordActivity extends BaseActivity implements OnItemClickListener, RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener {
    private RefreshLayout refreshLayout;
    private ListView listView;
    private TextView textView;
    private WinningRecordAdapter adapter;

    private List<WinningRecordsBO> winningBOList;
    private WinningRecordsBO winningBO;
    private SpannableString spannableString;
    private int currPage = 1;
    private boolean isLoad = false;//true 加载 false刷新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_record);
        initView();
        showProgressDialog(getString(R.string.dialog_message));
        getData();
    }

    protected void initView() {
        initTitleBar();

        refreshLayout = (RefreshLayout) findViewById(R.id.refresh_winningRecord);
        listView = (ListView) findViewById(R.id.list_winning);
        textView = (TextView) findViewById(R.id.txt_award_count);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);

        adapter = new WinningRecordAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

//        testData();
    }

    private void testData() {
        winningBOList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            winningBO = new WinningRecordsBO();
            winningBO.setPlayDt("2015-12-12");
            winningBO.setIsGot("1");
            winningBOList.add(winningBO);
        }
        adapter.setItems(winningBOList);

        spannableString = new SpannableString("恭喜您已获得" + 3 + "个宝物");
        Log.i(TAG, spannableString + ":" + spannableString.length());
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.themeRed)), 6,
                7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_winning_record);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
    }

    private void getData() {
        this.appAction.getWinningList(this.application.getUserId(), "2", currPage, MyConfig.PAGESIZE, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                LogUtils.i(TAG, "中奖记录:" + isLoad + "list:" + data.getObjList().toString());
                closeProgressDialog();
                // 更新完后调用该方法结束刷新
                winningBOList = new ArrayList<>();
                if (isLoad) {
                    refreshLayout.setLoading(false);
                    int len = data.getObjList().size();
                    for (int i = 0; i < len; i++) {
                        winningBOList.add((WinningRecordsBO) data.getObjList().get(i));
                    }
                    if (len > 0) {
                        currPage++;
                        adapter.addItems(winningBOList);
                    }
                } else {
                    refreshLayout.setRefreshing(false);
                    currPage++;
                    winningBOList = data.getObjList();
                    adapter.setItems(winningBOList);
                    LogUtils.i(TAG, "中奖记录:" + winningBOList);
                    spannableString = new SpannableString("恭喜您已获得" + data.getTotalCount() + "个宝物");
                    spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.themeRed)), 6,
                            7 + String.valueOf(data.getTotalCount()).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setText(spannableString);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle pBundle = new Bundle();
        pBundle.putSerializable(getString(R.string.intent_value), (WinningRecordsBO) adapter.getItem(position));
        openActivity(WinningRecord2Activity.class, pBundle);
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
