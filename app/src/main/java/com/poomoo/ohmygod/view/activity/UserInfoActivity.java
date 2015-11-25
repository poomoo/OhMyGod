/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;

/**
 * 个人资料
 * 作者: 李苜菲
 * 日期: 2015/11/24 15:37.
 */
public class UserInfoActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();
    }

    protected void initView() {
        initTitleBar();
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_userInfo);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 头像
     *
     * @param view
     */
    public void toAvatar(View view) {
        MyUtil.showToast(getApplicationContext(), "头像");
    }

    /**
     * 昵称
     *
     * @param view
     */
    public void toNickName(View view) {
        MyUtil.showToast(getApplicationContext(), "昵称");
    }

    /**
     * 性别
     *
     * @param view
     */
    public void toGender(View view) {
        MyUtil.showToast(getApplicationContext(), "性别");
    }

    /**
     * 年龄
     *
     * @param view
     */
    public void toAge(View view) {
        MyUtil.showToast(getApplicationContext(), "年龄");
    }

    /**
     * 手机号码
     *
     * @param view
     */
    public void toCelPhone(View view) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_phone));
        openActivity(VerifyPhoneNum2Activity.class, pBundle);
    }

    /**
     * 身份证号
     *
     * @param view
     */
    public void toIdCardNum(View view) {
        openActivity(ChangeIdCardInfoActivity.class);
    }

    /**
     * 银行卡
     *
     * @param view
     */
    public void toBankCard(View view) {
        openActivity(BankCardActivity.class);
    }

    /**
     * 修改密码
     *
     * @param view
     */
    public void toChangePassWord(View view) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_passWord));
        openActivity(VerifyPhoneNum2Activity.class, pBundle);
    }
}
