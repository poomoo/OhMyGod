/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poomoo.model.GrabBO;
import com.poomoo.model.WinningRecordsBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.TimeCountDownUtil;

/**
 * 夺宝记录
 * 作者: 李苜菲
 * 日期: 2015/11/30 14:49.
 */
public class SnatchAdapter extends MyBaseAdapter<WinningRecordsBO> {
    private WinningRecordsBO winningRecordsBO;

    public SnatchAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_snatch, null);
            viewHolder.snatchImg = (ImageView) convertView.findViewById(R.id.img_snatch);
            viewHolder.snatchNameTxt = (TextView) convertView.findViewById(R.id.txt_snatchName);
            viewHolder.snatchTimeTxt = (TextView) convertView.findViewById(R.id.txt_snatchTime);
            viewHolder.snatchStatusTxt = (TextView) convertView.findViewById(R.id.txt_snatchStatus);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        winningRecordsBO = itemList.get(position);
        viewHolder.snatchNameTxt.setText(winningRecordsBO.getTitle());
        viewHolder.snatchTimeTxt.setText(winningRecordsBO.getPlayDt());
        if (winningRecordsBO.getIsWin() == 0)//未中奖
            viewHolder.snatchStatusTxt.setText("未中奖");
        else
            viewHolder.snatchStatusTxt.setText("恭喜中奖");

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.drawable.bg_snatch_record) //
                .showImageOnFail(R.drawable.bg_snatch_record) //
                .cacheInMemory(true) //
                .cacheOnDisk(false) //
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//
        ImageLoader.getInstance().displayImage(winningRecordsBO.getPicture(), viewHolder.snatchImg, defaultOptions);
//        viewHolder.rlayout.setT   ag(winningRecordsBO.getPicture());

//        ImageLoader.getInstance().loadImage(winningRecordsBO.getPicture(), new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                Drawable drawable = new BitmapDrawable(context.getResources(), loadedImage);
//                // 通过 tag 来防止图片错位
//                if (viewHolder.rlayout.getTag() != null && viewHolder.rlayout.getTag().equals(imageUri)) {
//                    viewHolder.rlayout.setBackground(drawable);
//                }
//            }
//        });

        return convertView;
    }

    class ViewHolder {
        private ImageView snatchImg;
        private TextView snatchNameTxt;
        private TextView snatchTimeTxt;
        private TextView snatchStatusTxt;
    }
}
