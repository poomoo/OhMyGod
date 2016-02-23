/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.model.GrabBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.TimeCountDownUtil;
import com.poomoo.ohmygod.view.custom.ProgressSeekBar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 抢
 * 作者: 李苜菲
 * 日期: 2015/11/30 14:49.
 */
public class GrabAdapter extends MyBaseAdapter<GrabBO> {
    private final DisplayImageOptions defaultOptions;
    private String TAG = "GrabAdapter";
    private GrabBO grabBO = new GrabBO();
    private static SparseArray<TimeCountDownUtil> countDownUtils;
    private static SparseArray<RelativeLayout> layoutSparseArray;
    private Timer timer;
    private ProgressSeekBar progressSeekBar;

    public GrabAdapter(Context context) {
        super(context);
        countDownUtils = new SparseArray<>();
        layoutSparseArray = new SparseArray<>();
        defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.drawable.bg_snatch_record) //
                .showImageOnFail(R.drawable.bg_snatch_record) //
                .cacheInMemory(true) //
                .cacheOnDisk(false) //
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
//        if (convertView == null) {
        viewHolder = new ViewHolder();
        convertView = inflater.inflate(R.layout.item_list_grab, null);
        viewHolder.rlayout = (RelativeLayout) convertView.findViewById(R.id.rlayout_grab);
        viewHolder.image = (ImageView) convertView.findViewById(R.id.img_grab_bg);
        viewHolder.txt = (TextView) convertView.findViewById(R.id.txt_grab_countDown);
        viewHolder.progressBar = (ProgressSeekBar) convertView.findViewById(R.id.my_progress);
//            convertView.setTag(viewHolder);
//        } else
//            viewHolder = (ViewHolder) convertView.getTag();
        grabBO = itemList.get(position);

        ImageLoader.getInstance().displayImage(grabBO.getPicture(), viewHolder.image, defaultOptions);
        LogUtils.i(TAG, "活动状态:" + grabBO.getStatus() + " position:" + position);


        if (grabBO.getStatus() == 1) {

            if (viewHolder.txt.getTag() == null) {
                viewHolder.txt.setTag(grabBO.getPicture());
                TimeCountDownUtil timeCountDownUtil = new TimeCountDownUtil(grabBO.getStartCountdown(), 1000, viewHolder.txt, "1");
                LogUtils.i(TAG, "tag:" + viewHolder.txt.getTag() + " pic:" + grabBO.getPicture());
                timeCountDownUtil.start();
                getCountDownUtils().put(position, timeCountDownUtil);
            }
            LogUtils.i(TAG, "活动已开始" + " position:" + position + " 内容:" + viewHolder.txt.getText().toString());
        } else {
            viewHolder.progressBar.setVisibility(View.GONE);
            viewHolder.txt.setText("活动已结束");
            viewHolder.txt.setTextColor(Color.parseColor("#E81540"));
        }
        viewHolder.progressBar.setVisibility(View.VISIBLE);
        viewHolder.progressBar.setProgress(position);
        viewHolder.progressBar.setMax(40);
//        if (position == 0) {
//            grabBO.setStatus(1);
//            grabBO.setStartCountdown((position + 1) * 5 * 60 * 1000);
//            progressSeekBar = viewHolder.progressBar;
//            decrease();
//        }
        return convertView;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LogUtils.i(TAG, "progressSeekBar.getProgress():" + progressSeekBar.getProgress());
                    progressSeekBar.setProgress(progressSeekBar.getProgress() - 1);
                    break;
            }
        }
    };

    private void decrease() {
        TimerTask t = new TimerTask() {
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        timer = new Timer();
        timer.schedule(t, 1000, 1000);
    }

    class ViewHolder {
        private RelativeLayout rlayout;
        private ImageView image;
        private TextView txt;
        private ProgressSeekBar progressBar;
    }

    public SparseArray<TimeCountDownUtil> getCountDownUtils() {
        return countDownUtils;
    }

    public static SparseArray<RelativeLayout> getLayoutSparseArray() {
        return layoutSparseArray;
    }
}
