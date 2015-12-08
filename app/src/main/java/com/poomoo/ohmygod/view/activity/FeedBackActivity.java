/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;

/**
 * 新反馈
 * 作者: 李苜菲
 * 日期: 2015/12/8 15:28.
 */
public class FeedBackActivity extends BaseActivity {
    private EditText contentEdt;
    private EditText contactEdt;

    private String content;
    private String contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();

        contentEdt = (EditText) findViewById(R.id.edt_feedBack_content);
        contactEdt = (EditText) findViewById(R.id.edt_feedBack_contact);
    }

    @Override
    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_feedBack);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerViewHolder.rightTxt.setVisibility(View.VISIBLE);
        headerViewHolder.rightTxt.setText("提交");
        headerViewHolder.rightTxt.setTextColor(getResources().getColor(R.color.themeRed));
        headerViewHolder.rightTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toSubmit();
            }
        });
    }

    private void toSubmit() {
        content = contentEdt.getText().toString().trim();
        contact = contactEdt.getText().toString().trim();
        showProgressDialog("请稍后...");
        this.appAction.putFeedBack(application.getUserId(), content, contact, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), "提交成功");
                finish();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }
}
