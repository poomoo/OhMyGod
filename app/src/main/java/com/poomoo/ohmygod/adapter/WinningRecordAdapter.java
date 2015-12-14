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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poomoo.model.WinningRecordsBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.LogUtils;

/**
 * 中奖记录
 * 作者: 李苜菲
 * 日期: 2015/11/23 13:43.
 */
public class WinningRecordAdapter extends MyBaseAdapter<WinningRecordsBO> {
    private WinningRecordsBO winningRecordsBO;

    public WinningRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_winning_record, null);

            viewHolder.bgImg = (ImageView) convertView.findViewById(R.id.img_winningRecordBg);
            viewHolder.statusImg = (ImageView) convertView.findViewById(R.id.img_status);
            viewHolder.dateTimeTxt = (TextView) convertView.findViewById(R.id.txt_winning_dateTime);
            viewHolder.endDateTimeTxt = (TextView) convertView.findViewById(R.id.txt_winning_endTime);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();


        winningRecordsBO = itemList.get(position);
//        LogUtils.i("MyBaseAdapter","winningRecordsBO:"+winningRecordsBO+":"+position);
        if (winningRecordsBO.getIsGot().equals("1"))
            viewHolder.statusImg.setImageResource(R.drawable.ic_yes);
        else
            viewHolder.statusImg.setImageResource(R.drawable.ic_no);

        viewHolder.dateTimeTxt.setText(winningRecordsBO.getPlayDt());
        viewHolder.endDateTimeTxt.setText(winningRecordsBO.getGetEndDt());
        ImageLoader.getInstance().displayImage(winningRecordsBO.getPicture(), viewHolder.bgImg);

//        ImageLoader.getInstance().loadImage(winningRecordsBO.getPicture(), new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                Drawable drawable = new BitmapDrawable(context.getResources(), loadedImage);
//                // 通过 tag 来防止图片错位
//                if (viewHolder.fLayout.getTag() != null && viewHolder.fLayout.getTag().equals(imageUri)) {
//                    viewHolder.fLayout.setBackground(drawable);
//                }
//            }
//        });
        return convertView;
    }

    class ViewHolder {
        private ImageView bgImg;//背景
        private ImageView statusImg;//奖品状态
        private TextView dateTimeTxt;//获奖时间
        private TextView endDateTimeTxt;//截止时间
    }
}
