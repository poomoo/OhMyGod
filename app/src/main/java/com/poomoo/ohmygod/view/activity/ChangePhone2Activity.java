/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.poomoo.core.ErrorEvent;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.StatusBarUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 换绑手机2
 * 作者: 李苜菲
 * 日期: 2015/11/24 16:30.
 */
public class ChangePhone2Activity extends BaseActivity {
    private EditText telEdt;
    private String tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone2);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.themeRed), 0);
        addActivityToArrayList(this);
        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();

        telEdt = (EditText) findViewById(R.id.edt_newPhone);
    }

    @Override
    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_changePhone);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
    }

    /**
     * 下一步
     *
     * @param view
     */
    public void toNext(View view) {
        tel = telEdt.getText().toString().trim();
        if (TextUtils.isEmpty(tel)) {
            MyUtil.showToast(getApplicationContext(), "手机号为空");
            return;
        }
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(tel);
        if (!matcher.matches()) {
            MyUtil.showToast(getApplicationContext(), "手机号不正确");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.intent_value), tel);
        openActivity(ChangePhone3Activity.class, bundle);
        finish();
    }
}
