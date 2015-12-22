/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;

/**
 * 作者: 李苜菲
 * 日期: 2015/12/22 14:41.
 */
public class UpdateMemberInfoActivity extends BaseActivity {
    private EditText nameEdt;
    private EditText genderEdt;
    private EditText ageEdt;
    private EditText telEdt;
    private EditText addressEdt;
    private EditText idCardNumEdt;

    private String name;
    private String gender;
    private String age;
    private String tel;
    private String address;
    private String idCardNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_member_information);

        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();

        nameEdt = (EditText) findViewById(R.id.edt_memberInfo_name);
        genderEdt = (EditText) findViewById(R.id.edt_memberInfo_gender);
        ageEdt = (EditText) findViewById(R.id.edt_memberInfo_age);
        telEdt = (EditText) findViewById(R.id.edt_memberInfo_tel);
        addressEdt = (EditText) findViewById(R.id.edt_memberInfo_address);
        idCardNumEdt = (EditText) findViewById(R.id.edt_memberInfo_idCardNum);
    }

    @Override
    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_updateMember);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 提交
     * @param view
     */
    public void toUpdateMemberInfo(View view) {
        MyUtil.showToast(getApplicationContext(),"提交");
    }
}
