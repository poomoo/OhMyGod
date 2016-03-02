/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.SelectAdapter;
import com.poomoo.ohmygod.config.MyConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 选择属性公共界面
 * 作者: 李苜菲
 * 日期: 2015/11/27 14:46.
 */
public class SelectActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private SelectAdapter adapter;

    private String PARENT;
    private String title;
    private List<String> strings;
    private List<HashMap<String, String>> hashMapList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();

        listView = (ListView) findViewById(R.id.list_select);
        adapter = new SelectAdapter(this);
        listView.setAdapter(adapter);

        strings = new ArrayList<>();
        if (PARENT.equals(getString(R.string.intent_age))) {
            for (int i = 0; i < 50; i++)
                strings.add(i + 18 + "");
        }

        if (PARENT.equals(getString(R.string.intent_gender))) {
            strings.add(MyConfig.genders[0]);
            strings.add(MyConfig.genders[1]);
        }

        if (PARENT.equals(getString(R.string.intent_activeName))) {
            hashMapList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("activeList");
            int len = hashMapList.size();
            for (int i = 0; i < len; i++)
                strings.add(hashMapList.get(i).get("activeName") + "#" + hashMapList.get(i).get("activeId"));
        }

        adapter.setItems(strings);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void initTitleBar() {
        PARENT = getIntent().getStringExtra(getString(R.string.intent_parent));
        if (PARENT.equals(getString(R.string.intent_age)))
            title = getString(R.string.title_selectAge);
        if (PARENT.equals(getString(R.string.intent_gender)))
            title = getString(R.string.title_selectGender);
        if (PARENT.equals(getString(R.string.intent_activeName)))
            title = getString(R.string.title_selectActive);

        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(title);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = getIntent();
        if (PARENT.equals(getString(R.string.intent_age))) {
            intent.putExtra(getString(R.string.intent_age), strings.get(position));
            setResult(MyConfig.AGE, intent);
        }

        if (PARENT.equals(getString(R.string.intent_gender))) {
            intent.putExtra(getString(R.string.intent_gender), strings.get(position));
            setResult(MyConfig.GENDER, intent);
        }

        if (PARENT.equals(getString(R.string.intent_activeName))) {
            intent.putExtra(getString(R.string.intent_activeName), strings.get(position));
            setResult(MyConfig.ACTIVE, intent);
        }
        finish();
        getActivityOutToRight();
    }
}
