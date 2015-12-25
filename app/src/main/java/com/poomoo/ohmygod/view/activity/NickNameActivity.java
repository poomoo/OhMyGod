/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;

/**
 * 修改昵称
 * 作者: 李苜菲
 * 日期: 2015/11/24 15:57.
 */
public class NickNameActivity extends BaseActivity {
    private EditText contentEdt;
    private String PARENT;
    private String title;
    private String key;
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name);
        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();

        contentEdt = (EditText) findViewById(R.id.edt_inPut);
        if (PARENT.equals(getString(R.string.intent_nickName))) {
            contentEdt.setHint(getString(R.string.hint_input_nickName));
        }

        if (PARENT.equals(getString(R.string.intent_age))) {
            contentEdt.setHint(getString(R.string.hint_input_age));
            contentEdt.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
        }
        if (PARENT.equals(getString(R.string.intent_openBank))) {
            contentEdt.setHint(getString(R.string.hint_input_openBank));
        }

    }

    @Override
    protected void initTitleBar() {
        PARENT = getIntent().getStringExtra(getString(R.string.intent_parent));
        if (PARENT.equals(getString(R.string.intent_nickName)))
            title = getString(R.string.title_nickName);
        if (PARENT.equals(getString(R.string.intent_age)))
            title = getString(R.string.title_age);
        if (PARENT.equals(getString(R.string.intent_openBank)))
            title = getString(R.string.title_openBank);
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

    /**
     * 提交
     *
     * @param view
     */
    public void toSubmit(View view) {
        if (PARENT.equals(getString(R.string.intent_nickName)))
            key = "nickName";
        if (PARENT.equals(getString(R.string.intent_age)))
            key = "age";
        if (PARENT.equals(getString(R.string.intent_openBank)))
            key = "bankName";
        value = contentEdt.getText().toString().trim();

        showProgressDialog(getString(R.string.dialog_message));
        this.appAction.changePersonalInfo(this.application.getUserId(), key, value, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();

                if (PARENT.equals(getString(R.string.intent_nickName))) {
                    application.setNickName(value);
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_nickName), value);
                    MyUtil.showToast(getApplicationContext(), "修改昵称成功");
                }

                if (PARENT.equals(getString(R.string.intent_age))) {
                    application.setAge(value);
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_age), value);
                    MyUtil.showToast(getApplicationContext(), "修改年龄成功");
                }

                if (PARENT.equals(getString(R.string.intent_openBank))) {
                    application.setBankName(value);
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_bankName), value);
                    MyUtil.showToast(getApplicationContext(), "修改开户银行成功");
                }
                finish();
                getActivityOutToRight();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

}
