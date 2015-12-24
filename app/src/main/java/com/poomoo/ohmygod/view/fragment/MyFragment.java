/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.PersonalCenterAdapter;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;
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
    private TextView dialTxt;
    private ImageView settingImg;
    private GridView gridView;
    private ImageView avatarImg;
    private TextView nickNameTxt;
    private TextView balanceTxt;
    private ImageView genderImg;
    private PersonalCenterAdapter personalCenterAdapter;
    private static final Class[] menu = {SnatchRecordActivity.class, WinningRecordActivity.class, MyWithdrawDepositActivity.class
            , WithdrawDepositActivity.class, InStationMessagesActivity.class, MyShowActivity.class};

    private String headPic;

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

        dialTxt = (TextView) getActivity().findViewById(R.id.txt_dial);
        settingImg = (ImageView) getActivity().findViewById(R.id.img_personal_setting);
        avatarImg = (ImageView) getActivity().findViewById(R.id.img_personal_avatar);
        genderImg = (ImageView) getActivity().findViewById(R.id.img_personal_gender);
        nickNameTxt = (TextView) getActivity().findViewById(R.id.txt_personal_nickName);
        balanceTxt = (TextView) getActivity().findViewById(R.id.txt_personal_walletBalance);

        gridView = (GridView) getActivity().findViewById(R.id.grid_personal_center);
        personalCenterAdapter = new PersonalCenterAdapter(getActivity(), gridView);
        gridView.setAdapter(personalCenterAdapter);
        gridView.setOnItemClickListener(this);

        dialTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getString(R.string.dial)));
                startActivity(intent_dial);
            }
        });

        settingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SettingActivity.class);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 4) {
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.intent_parent), getString(R.string.intent_innerMessage));
            openActivity(menu[position], bundle);
        } else
            openActivity(menu[position]);
    }

    @Override
    public void onResume() {
        super.onResume();
        headPic = (String) SPUtils.get(getActivity().getApplicationContext(), getString(R.string.sp_headPicBitmap), "");
        LogUtils.i(TAG, "headPic:" + headPic);
        if (!TextUtils.isEmpty(headPic))
            avatarImg.setImageDrawable(MyUtil.loadDrawable(getActivity().getApplicationContext()));
        else if (!TextUtils.isEmpty(application.getHeadPic())) {
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                    .showImageForEmptyUri(R.drawable.ic_avatar) //
                    .showImageOnFail(R.drawable.ic_avatar) //
                    .cacheInMemory(true) //
                    .cacheOnDisk(true) //
                    .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                    .build();//
            ImageLoader.getInstance().loadImage(application.getHeadPic(), defaultOptions, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    avatarImg.setImageBitmap(loadedImage);
                    SPUtils.put(getActivity().getApplicationContext(), getString(R.string.sp_headPic), MyUtil.saveDrawable(loadedImage));
                }
            });
        }
        nickNameTxt.setText(application.getNickName());
        if (TextUtils.isEmpty(application.getSex()))
            genderImg.setVisibility(View.GONE);
        else if (application.getSex().equals("1"))
            genderImg.setImageResource(R.drawable.ic_gender_man);
        else
            genderImg.setImageResource(R.drawable.ic_gender_woman);
        balanceTxt.setText(application.getCurrentFee());

    }
}
