/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.poomoo.ohmygod.R;

/**
 * webview
 * 作者: 李苜菲
 * 日期: 2015/11/19 16:59.
 */
public class WebViewActivity extends BaseActivity {
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_webview);

        initView();
    }

    private void initView() {
        initTitleBar();
    }

    private void initTitleBar() {
        title = getIntent().getStringExtra(getString(R.string.intent_title));
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(title);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
