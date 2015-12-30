/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.model.MessageBO;
import com.poomoo.model.WinnerListBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;

/**
 * 活动中奖者适配器
 * 作者: 李苜菲
 * 日期: 2015/11/24 11:06.
 */
public class ActivityWinnerListAdapter extends MyBaseAdapter<WinnerListBO> {
    private final DisplayImageOptions defaultOptions;
    private WinnerListBO winnerListBO;

    public ActivityWinnerListAdapter(Context context) {
        super(context);
        defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.drawable.ic_avatar) //
                .showImageOnFail(R.drawable.ic_avatar) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_activity_winner, null);
            viewHolder.avatarImg = (ImageView) convertView.findViewById(R.id.img_activityWinnerAvatar);
            viewHolder.nameTxt = (TextView) convertView.findViewById(R.id.txt_activityWinnerNickName);
            viewHolder.telTxt = (TextView) convertView.findViewById(R.id.txt_activityWinnerTel);
            viewHolder.goodNameTxt = (TextView) convertView.findViewById(R.id.txt_activityWinnerGoodName);
            viewHolder.dateTimeTxt = (TextView) convertView.findViewById(R.id.txt_activityWinnerDate);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        winnerListBO = itemList.get(position);
        ImageLoader.getInstance().displayImage(winnerListBO.getHeadPic(), viewHolder.avatarImg, defaultOptions);
        viewHolder.nameTxt.setText(winnerListBO.getNickName());
        viewHolder.telTxt.setText(MyUtil.hiddenTel(winnerListBO.getTel()));
        viewHolder.goodNameTxt.setText(winnerListBO.getGoodsName());
        viewHolder.dateTimeTxt.setText(winnerListBO.getPlayDt());

        return convertView;
    }

    class ViewHolder {
        private ImageView avatarImg;
        private TextView nameTxt;
        private TextView telTxt;
        private TextView goodNameTxt;
        private TextView dateTimeTxt;
    }
}
