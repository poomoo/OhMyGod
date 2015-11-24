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
import android.widget.ListView;
import android.widget.TextView;

import com.poomoo.model.WinningBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.WinningRecordAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 中奖记录
 * 作者: 李苜菲
 * 日期: 2015/11/23 13:40.
 */
public class WinningRecordActivity extends BaseActivity {
    private ListView listView;
    private TextView textView;
    private WinningRecordAdapter winningRecordAdapter;

    private List<WinningBO> winningBOList;
    private WinningBO winningBO;
    private SpannableString spannableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_record);

        initView();
    }

    protected void initView() {
        initTitleBar();

        listView = (ListView) findViewById(R.id.list_winning);
        textView = (TextView) findViewById(R.id.txt_award_count);
        winningRecordAdapter = new WinningRecordAdapter(this);
        listView.setAdapter(winningRecordAdapter);

        winningBOList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            winningBO = new WinningBO();
            winningBOList.add(winningBO);
        }
        winningRecordAdapter.setItems(winningBOList);

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
}
