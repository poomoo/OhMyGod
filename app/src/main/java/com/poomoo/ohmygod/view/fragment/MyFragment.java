/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.PersonalCenterAdapter;
import com.poomoo.ohmygod.view.activity.InStationMessagesActivity;
import com.poomoo.ohmygod.view.activity.MyShowActivity;
import com.poomoo.ohmygod.view.activity.MyWithdrawDepositActivity;
import com.poomoo.ohmygod.view.activity.SnatchRecordActivity;
import com.poomoo.ohmygod.view.activity.WinningRecordActivity;
import com.poomoo.ohmygod.view.activity.WithdrawDepositActivity;

/**
 * 我
 * 作者: 李苜菲
 * 日期: 2015/11/20 15:25.
 */
public class MyFragment extends BaseFragment implements OnItemClickListener {
    private GridView gridView;
    private PersonalCenterAdapter personalCenterAdapter;
    private static final Class[] menu = {SnatchRecordActivity.class, WinningRecordActivity.class, MyWithdrawDepositActivity.class
            , WithdrawDepositActivity.class, InStationMessagesActivity.class, MyShowActivity.class};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        initTitleBar();

        gridView = (GridView) getActivity().findViewById(R.id.grid_personal_center);
        personalCenterAdapter = new PersonalCenterAdapter(getActivity(), gridView);
        gridView.setAdapter(personalCenterAdapter);
        gridView.setOnItemClickListener(this);
    }

    private void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_personal_center);
        headerViewHolder.backImg.setVisibility(View.GONE);
        headerViewHolder.rightImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_seeting));
        headerViewHolder.rightImg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        openActivity(menu[position]);
    }
}
