/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;

/**
 * 设置
 * 作者: 李苜菲
 * 日期: 2015/11/24 15:03.
 */
public class SettingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    protected void initView() {
        initTitleBar();
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_setting);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 个人资料
     *
     * @param view
     */
    public void toUserInfo(View view) {
        openActivity(UserInfoActivity.class);
    }

    /**
     * 用户帮助
     *
     * @param view
     */
    public void toUserHelp(View view) {
        MyUtil.showToast(getApplicationContext(), "用户帮助");
    }

    /**
     * 关于天呐
     *
     * @param view
     */
    public void toAbout(View view) {
        MyUtil.showToast(getApplicationContext(), "关于天呐");
    }

    /**
     * 分享软件
     *
     * @param view
     */
    public void toShareSoft(View view) {
        MyUtil.showToast(getApplicationContext(), "分享软件");
    }

    /**
     * 意见反馈
     *
     * @param view
     */
    public void toFeedBack(View view) {
        MyUtil.showToast(getApplicationContext(), "意见反馈");

    }

    /**
     * 引导页
     *
     * @param view
     */
    public void toGuidePage(View view) {
        MyUtil.showToast(getApplicationContext(), "引导页");
    }

    /**
     * 检查更新
     *
     * @param view
     */
    public void toCheckUpdate(View view) {
        MyUtil.showToast(getApplicationContext(), "检查更新");
    }

    /**
     * 退出登录
     *
     * @param view
     */
    public void toLogOut(View view) {
        MyUtil.showToast(getApplicationContext(), "退出登录");
    }
}
