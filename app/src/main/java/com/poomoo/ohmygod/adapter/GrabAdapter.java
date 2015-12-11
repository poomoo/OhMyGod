/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poomoo.model.GrabBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.other.CountDownListener;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.TimeCountDownUtil;

/**
 * 抢
 * 作者: 李苜菲
 * 日期: 2015/11/30 14:49.
 */
public class GrabAdapter extends MyBaseAdapter<GrabBO> {
    private GrabBO grabBO = new GrabBO();
    private TimeCountDownUtil timeCountDownUtil;
    private static SparseArray<TimeCountDownUtil> countDownUtils;
    private static SparseArray<RelativeLayout> layoutSparseArray;

    public GrabAdapter(Context context) {
        super(context);
        countDownUtils = new SparseArray<>();
        layoutSparseArray = new SparseArray<>();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_grab, null);
            viewHolder.rlayout = (RelativeLayout) convertView.findViewById(R.id.rlayout_grab);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.img_grab_bg);
            viewHolder.txt = (TextView) convertView.findViewById(R.id.txt_grab_countDown);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
//        viewHolder.rlayout.setClickable(false);
        grabBO = itemList.get(position);

//        viewHolder.rlayout.setTag(grabBO.getPicture());
//        viewHolder.image.setTag(grabBO.getPicture());

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.drawable.bg_snatch_record) //
                .showImageOnFail(R.drawable.bg_snatch_record) //
                .cacheInMemory(true) //
                .cacheOnDisk(false) //
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//
        ImageLoader.getInstance().displayImage(grabBO.getPicture(), viewHolder.image, defaultOptions);
//        layoutSparseArray.put(position, viewHolder.rlayout);


//        if (viewHolder.txt.getTag() == null) {
        viewHolder.txt.setTag(grabBO.getPicture());
        timeCountDownUtil = null;
        timeCountDownUtil = new TimeCountDownUtil(grabBO.getStartCountdown(), 1000, viewHolder.txt, viewHolder.rlayout);
        timeCountDownUtil.start();
        getCountDownUtils().put(position, timeCountDownUtil);
//        }

        return convertView;
    }

    class ViewHolder {
        private RelativeLayout rlayout;
        private ImageView image;
        private TextView txt;
    }

    public SparseArray<TimeCountDownUtil> getCountDownUtils() {
        return countDownUtils;
    }

    public static SparseArray<RelativeLayout> getLayoutSparseArray() {
        return layoutSparseArray;
    }
}
