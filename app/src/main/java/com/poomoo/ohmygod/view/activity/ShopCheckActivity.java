/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.poomoo.model.CodeBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.CodeStatusListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 商家验证
 * 作者: 李苜菲
 * 日期: 2016/3/1 15:16.
 */
public class ShopCheckActivity extends BaseActivity {
    private ListView listView;

    private CodeStatusListAdapter adapter;
    private List<CodeBO> codeBOList;
    private CodeBO codeBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_check);
        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();

        listView = (ListView) findViewById(R.id.list_code_status);
        adapter = new CodeStatusListAdapter(this);
        listView.setAdapter(adapter);
        initTestData();
    }

    private void initTestData() {
        codeBOList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            codeBO = new CodeBO();
            codeBO.setActiveName("测试活动" + (i + 1));
            codeBO.setWinNumber("123 456 789");
            if (i % 2 == 0)
                codeBO.setIsGot(1);
            else
                codeBO.setIsGot(0);
            codeBO.setNickName("哇哈哈");
            codeBOList.add(codeBO);
        }
        adapter.setItems(codeBOList);
    }

    @Override
    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_shopCheck);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
    }
}
