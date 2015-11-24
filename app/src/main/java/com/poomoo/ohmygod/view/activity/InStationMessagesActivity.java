/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.poomoo.model.InStationMessagesBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.InStationMessagesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 站内信息
 * 作者: 李苜菲
 * 日期: 2015/11/24 11:03.
 */
public class InStationMessagesActivity extends BaseActivity {
    private ListView listView;
    private InStationMessagesAdapter adapter;
    private List<InStationMessagesBO> inStationMessagesBOList;
    private InStationMessagesBO inStationMessagesBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_station_messages);

        initView();
    }

    private void initView() {
        initTitleBar();

        listView = (ListView) findViewById(R.id.list_in_station_messages);

        adapter = new InStationMessagesAdapter(this);
        listView.setAdapter(adapter);
        initTestData();

    }

    private void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_in_station_messages);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initTestData() {
        inStationMessagesBOList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            inStationMessagesBO = new InStationMessagesBO();
            inStationMessagesBOList.add(inStationMessagesBO);
        }
        adapter.setItems(inStationMessagesBOList);
    }
}
