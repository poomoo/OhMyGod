/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.model.ActiveWinInfoBO;
import com.poomoo.model.WinnerListBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.custom.NoScrollListView;

/**
 * 活动中奖者适配器
 * 作者: 李苜菲
 * 日期: 2015/11/24 11:06.
 */
public class ActiveWinnerInfoListAdapter extends MyBaseAdapter<ActiveWinInfoBO> {
    private final DisplayImageOptions defaultOptions;
    private ActiveWinInfoBO activeWinInfoBO;
    private ActivityWinnerListAdapter activityWinnerListAdapter;

    public ActiveWinnerInfoListAdapter(Context context) {
        super(context);
        defaultOptions = new DisplayImageOptions.Builder() //
//                .showImageForEmptyUri(R.drawable.ic_avatar) //
//                .showImageOnFail(R.drawable.ic_avatar) //
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
            convertView = inflater.inflate(R.layout.item_list_active_winner_info, null);
            viewHolder.picImg = (ImageView) convertView.findViewById(R.id.img_active);
            viewHolder.nameTxt = (TextView) convertView.findViewById(R.id.txt_activeName);
            viewHolder.remarkTxt = (TextView) convertView.findViewById(R.id.txt_remark);
            viewHolder.listView = (NoScrollListView) convertView.findViewById(R.id.list_winner);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        activeWinInfoBO = itemList.get(position);
        ImageLoader.getInstance().displayImage(activeWinInfoBO.getPicture(), viewHolder.picImg, defaultOptions);
        viewHolder.nameTxt.setText(activeWinInfoBO.getGoodsName());
        viewHolder.remarkTxt.setText(activeWinInfoBO.getPrizeNumber() + "个奖品 已抢" + activeWinInfoBO.getWinNumber() + "个");

        if (activeWinInfoBO.getWinList() != null && activeWinInfoBO.getWinList().size() > 0) {
            viewHolder.listView.setVisibility(View.VISIBLE);
            activityWinnerListAdapter = new ActivityWinnerListAdapter(context,activeWinInfoBO.getGoodsName());
            viewHolder.listView.setAdapter(activityWinnerListAdapter);
            activityWinnerListAdapter.setItems(activeWinInfoBO.getWinList());
        } else
            viewHolder.listView.setVisibility(View.GONE);

        return convertView;
    }

    class ViewHolder {
        private ImageView picImg;
        private TextView nameTxt;
        private TextView remarkTxt;
        private NoScrollListView listView;
    }
}
