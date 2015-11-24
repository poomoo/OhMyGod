/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;

/**
 * 编辑个人信息
 * 作者: 李苜菲
 * 日期: 2015/11/17 14:18.
 */
public class EditPersonalInformationActivity extends BaseActivity {
    private EditText realNameEdt;
    private EditText idCardNumEdt;
    private EditText celPhoneEdt;
    private EditText bankNumEdt;
    private ImageView frontIdCardImg;
    private ImageView backIdCardImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_information);
        initView();
    }

    protected void initView() {
        initTitleBar();
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_edit_personal_information);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerViewHolder.rightTxt.setText(R.string.btn_save);
        headerViewHolder.rightTxt.setTextColor(getResources().getColor(R.color.themeRed));
        headerViewHolder.rightTxt.setVisibility(View.VISIBLE);
    }

    /**
     * 保存
     *
     * @param view
     */
    public void toDo(View view) {
        MyUtil.showToast(this, "点击保存");
    }
}
