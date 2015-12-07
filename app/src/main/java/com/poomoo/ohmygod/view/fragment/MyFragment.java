/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.fragment;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.PersonalCenterAdapter;
import com.poomoo.ohmygod.view.activity.InStationMessagesActivity;
import com.poomoo.ohmygod.view.activity.MyShowActivity;
import com.poomoo.ohmygod.view.activity.MyWithdrawDepositActivity;
import com.poomoo.ohmygod.view.activity.SettingActivity;
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
    private ImageView avatarImg;
    private TextView nickNameTxt;
    private TextView balanceTxt;
    private ImageView genderImg;
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

        avatarImg = (ImageView) getActivity().findViewById(R.id.img_personal_avatar);
        genderImg = (ImageView) getActivity().findViewById(R.id.img_personal_gender);
        nickNameTxt = (TextView) getActivity().findViewById(R.id.txt_personal_nickName);
        balanceTxt = (TextView) getActivity().findViewById(R.id.txt_personal_walletBalance);


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
        headerViewHolder.rightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SettingActivity.class);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        openActivity(menu[position]);
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.drawable.ic_avatar) //
                .showImageOnFail(R.drawable.ic_avatar) //
                .cacheInMemory(true) //
                .cacheOnDisk(false) //
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//
        ImageLoader.getInstance().displayImage(application.getHeadPic(), avatarImg, defaultOptions);
        nickNameTxt.setText(application.getNickName());
        if (application.getSex().equals("1"))
            genderImg.setImageResource(R.drawable.ic_gender_man);
        else
            genderImg.setImageResource(R.drawable.ic_gender_woman);
        balanceTxt.setText(application.getCurrentFee());

    }
}
