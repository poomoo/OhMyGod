/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
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
import com.poomoo.ohmygod.utils.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 中奖记录
 * 作者: 李苜菲
 * 日期: 2015/11/23 13:40.
 */
public class WinningRecordActivity extends BaseActivity implements OnItemClickListener {
    private ListView listView;
    private TextView textView;
    private WinningRecordAdapter adapter;

    private List<WinningRecordsBO> winningBOList;
    private WinningRecordsBO winningBO;
    private SpannableString spannableString;
    private int currPage = 1;
    private int PAGESIZE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_record);

        initView();
        getData();
    }

    protected void initView() {
        initTitleBar();

        listView = (ListView) findViewById(R.id.list_winning);
        textView = (TextView) findViewById(R.id.txt_award_count);
        adapter = new WinningRecordAdapter(this);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

//        testData();
    }

    private void testData() {
        winningBOList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            winningBO = new WinningRecordsBO();
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
            }
        });
    }

    private void getData() {
        showProgressDialog("查询中...");
        this.appAction.getWinningList(this.application.getUserId(), "2", currPage, PAGESIZE, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                winningBOList = data.getObjList();
                adapter.setItems(winningBOList);
                winningBO = (WinningRecordsBO) data.getObj();
                spannableString = new SpannableString("恭喜您已获得" + winningBO.getTotalCount() + "个宝物");
                spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.themeRed)), 6,
                        7 + winningBO.getTotalCount().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textView.setText(spannableString);
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle pBundle = new Bundle();
        pBundle.putSerializable(getString(R.string.intent_value), winningBOList.get(position));
        openActivity(WinningRecord2Activity.class, pBundle);
        finish();
    }
}
