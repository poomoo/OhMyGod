/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.poomoo.model.WinningRecordsBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.view.custom.RefreshLayout;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/23 15:09.
 */
public class WinningRecord2Activity extends BaseActivity {

    private TextView titleTxt;
    private TextView addressTxt;
    private TextView dateTxt;
    private TextView requirementTxt;
    private Button showBtn;
    private Button completeInfoBtn;
    private WinningRecordsBO winningRecordsBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_record2);

        initView();
    }

    protected void initView() {
        initTitleBar();

        titleTxt = (TextView) findViewById(R.id.txt_getRewardTitle);
        addressTxt = (TextView) findViewById(R.id.txt_getRewardAddress);
        dateTxt = (TextView) findViewById(R.id.txt_getRewardEndDate);
        requirementTxt = (TextView) findViewById(R.id.txt_getRewardRequirement);
        showBtn = (Button) findViewById(R.id.btn_show);
        completeInfoBtn = (Button) findViewById(R.id.btn_completeInfo);


        winningRecordsBO = (WinningRecordsBO) getIntent().getSerializableExtra(getString(R.string.intent_value));

        titleTxt.setText(winningRecordsBO.getTitle());
        addressTxt.setText(winningRecordsBO.getGetAddress());
        dateTxt.setText(winningRecordsBO.getGetEndDt());
        requirementTxt.setText(winningRecordsBO.getGetRequire());
//        if (winningRecordsBO.getIsShare().equals("1")) {
//            showBtn.setText(getString(R.string.btn_showed));
//            showBtn.setBackgroundResource(R.drawable.bg_open_activity_pressed);
//            showBtn.setClickable(false);
//        }
        if (!(TextUtils.isEmpty(application.getRealName()) && TextUtils.isEmpty(application.getIdCardNum())))
            completeInfoBtn.setVisibility(View.GONE);


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

    public void toShow(View view) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_title), winningRecordsBO.getTitle());
        pBundle.putInt(getString(R.string.intent_activeId), winningRecordsBO.getActiveId());
        openActivity(ShowAndShareActivity.class, pBundle);
        finish();
    }

    public void toCompletedUserInfo(View view) {
        openActivity(EditPersonalInformationActivity.class);
    }
}
