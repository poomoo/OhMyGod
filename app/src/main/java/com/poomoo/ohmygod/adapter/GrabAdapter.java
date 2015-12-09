/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poomoo.model.GrabBO;
import com.poomoo.ohmygod.R;
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

    public GrabAdapter(Context context) {
        super(context);
        countDownUtils = new SparseArray<>();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_grab, null);
            viewHolder.rlayout = (RelativeLayout) convertView.findViewById(R.id.rlayout_grab);
//            viewHolder.image = (ImageView) convertView.findViewById(R.id.img_grab_background);
            viewHolder.txt = (TextView) convertView.findViewById(R.id.txt_grab_countDown);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        grabBO = itemList.get(position);

        viewHolder.rlayout.setTag(grabBO.getPicture());
//        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
//                .imageScaleType(ImageScaleType.NONE_SAFE)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .displayer(new FadeInBitmapDisplayer(300))
//                .build();//
//        ImageLoader.getInstance().displayImage(grabBO.getPicture(), viewHolder.image, defaultOptions);

        ImageLoader.getInstance().loadImage(grabBO.getPicture(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Drawable drawable = new BitmapDrawable(context.getResources(), loadedImage);
                // 通过 tag 来防止图片错位
                LogUtils.i("GrabAdapter", viewHolder.rlayout.getTag() + "");
                if (viewHolder.rlayout.getTag() != null && viewHolder.rlayout.getTag().equals(imageUri)) {
                    viewHolder.rlayout.setBackground(drawable);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                LogUtils.i("GrabAdapter", "加载失败:" + imageUri + " reason:" + failReason);
            }
        });


        if (viewHolder.txt.getTag() == null) {
            viewHolder.txt.setTag(grabBO.getPicture());
            timeCountDownUtil = null;
            timeCountDownUtil = new TimeCountDownUtil(grabBO.getStartCountdown(), 1000, viewHolder.txt, null);
            timeCountDownUtil.start();
            getCountDownUtils().put(position, timeCountDownUtil);
        }

        return convertView;
    }

    class ViewHolder {
        private RelativeLayout rlayout;
        //        private ImageView image;
        private TextView txt;
    }

    public SparseArray<TimeCountDownUtil> getCountDownUtils() {
        return countDownUtils;
    }
}
