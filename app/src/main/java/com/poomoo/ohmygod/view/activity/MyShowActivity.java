/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.poomoo.model.ReplyBO;
import com.poomoo.model.ShowBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.ShowAdapter;
import com.poomoo.ohmygod.config.MyConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的晒单
 * 作者: 李苜菲
 * 日期: 2015/11/24 11:38.
 */
public class MyShowActivity extends BaseActivity {
    private RelativeLayout replyRlayout;
    private ListView list;
    private ShowAdapter showAdapter;
    private ShowBO showBO;
    private ReplyBO replyBO;
    private List<ShowBO> showBOList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_show);
        
        initView();
    }

    protected void initView() {
        initTitleBar();

        replyRlayout = (RelativeLayout) findViewById(R.id.rlayout_reply);
        list = (ListView) findViewById(R.id.list_show);
        showAdapter = new ShowAdapter(this);
        list.setAdapter(showAdapter);

        showBO = new ShowBO();
        showBO.setPics(MyConfig.testUrls);
        replyBO = new ReplyBO();
        replyBO.setContent("测试测试");
        replyBO.setFloor_user_name("十年九梦你");
        replyBO.setRevert_user_name("糊涂图");
        showBO.setReplyBO(replyBO);

        showBOList = new ArrayList<>();
        showBOList.add(showBO);
        showAdapter.setItems(showBOList);

        replyRlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (replyRlayout.getVisibility() == View.VISIBLE) {
                    replyRlayout.setVisibility(View.GONE);
                }
//                if (comment_layout.getVisibility() == View.INVISIBLE)
//                    comment_layout.setVisibility(View.VISIBLE);
                //隐藏键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_my_show);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
