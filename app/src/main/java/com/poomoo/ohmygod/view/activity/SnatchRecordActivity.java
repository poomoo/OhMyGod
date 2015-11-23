/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.view.fragment.AllFragment;
import com.poomoo.ohmygod.view.fragment.AlreadyFragment;

/**
 * 夺宝记录
 * 作者: 李苜菲
 * 日期: 2015/11/23 09:23.
 */
public class SnatchRecordActivity extends BaseActivity {
    private Fragment curFragment;
    private AllFragment allFragment;
    private AlreadyFragment alreadyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snatch_record);

        initView();
    }

    private void initView() {
        initTitleBar();
        setDefaultFragment();
    }

    private void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_snatch_record);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setDefaultFragment() {
        // TODO 自动生成的方法存根
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        allFragment = new AllFragment();
        curFragment = allFragment;
        fragmentTransaction.add(R.id.flayout_snatch_record, allFragment);
        fragmentTransaction.commit();
    }

    public void switchToAll(View view) {
        if (allFragment == null)
            allFragment = new AllFragment();
        switchFragment(allFragment);
        curFragment = allFragment;
    }

    public void switchToAlready(View view) {
        if (alreadyFragment == null)
            alreadyFragment = new AlreadyFragment();
        switchFragment(alreadyFragment);
        curFragment = alreadyFragment;
    }

    /**
     * 切换fragment
     *
     * @param to
     */
    public void switchFragment(Fragment to) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!to.isAdded()) { // 先判断是否被add过
            fragmentTransaction.hide(curFragment).add(R.id.flayout_snatch_record, to); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            fragmentTransaction.hide(curFragment).show(to); // 隐藏当前的fragment，显示下一个
        }
        fragmentTransaction.commit();
    }
}
