/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poomoo.model.GrabBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.TimeCountDownUtil;

/**
 * 抢
 * 作者: 李苜菲
 * 日期: 2015/11/30 14:49.
 */
public class GrabAdapter extends MyBaseAdapter<GrabBO> {
    private GrabBO grabBO = new GrabBO();
    private TimeCountDownUtil headTimeCountDownUtil;

    public GrabAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_grab, null);
            viewHolder.rlayout = (RelativeLayout) convertView.findViewById(R.id.rlayout_grab);
            viewHolder.txt = (TextView) convertView.findViewById(R.id.txt_grab_countDown);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        grabBO = itemList.get(position);

        viewHolder.rlayout.setTag(grabBO.getPicture());

        if (position == 1)
            grabBO.setPicture("1");


        ImageLoader.getInstance().loadImage(grabBO.getPicture(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Drawable drawable = new BitmapDrawable(context.getResources(), loadedImage);
                // 通过 tag 来防止图片错位
                if (viewHolder.rlayout.getTag() != null && viewHolder.rlayout.getTag().equals(imageUri)) {
                    viewHolder.rlayout.setBackground(drawable);
                }
            }
        });

        headTimeCountDownUtil = null;
        headTimeCountDownUtil = new TimeCountDownUtil(grabBO.getCountdown(), 1000, viewHolder.txt, null);
        headTimeCountDownUtil.start();
        return convertView;
    }

    class ViewHolder {
        private RelativeLayout rlayout;
        private TextView txt;
    }
}
