/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.model.GrabBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.listeners.AlarmtListener;
import com.poomoo.ohmygod.other.CountDownListener;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.TimeCountDownUtil;
import com.poomoo.ohmygod.view.custom.ProgressSeekBar;

import java.util.Timer;

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
    private AlarmtListener alarmtListener;

    public GrabAdapter(Context context, AlarmtListener alarmtListener) {
        super(context);
        countDownUtils = new SparseArray<>();
        layoutSparseArray = new SparseArray<>();
        this.alarmtListener = alarmtListener;
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
        viewHolder = new ViewHolder();
        convertView = inflater.inflate(R.layout.item_list_grab, null);
        viewHolder.llayout_remind = (LinearLayout) convertView.findViewById(R.id.llayout_remind);
        viewHolder.img_bg_remind = (ImageView) convertView.findViewById(R.id.img_bg_alarm);
        viewHolder.image = (ImageView) convertView.findViewById(R.id.img_grab_bg);
        viewHolder.txt = (TextView) convertView.findViewById(R.id.txt_grab_countDown);
        viewHolder.progressBar = (ProgressSeekBar) convertView.findViewById(R.id.my_progress);
        grabBO = itemList.get(position);

        ImageLoader.getInstance().displayImage(grabBO.getPicture(), viewHolder.image, defaultOptions);
        LogUtils.i(TAG, "活动状态:" + grabBO.getStatus() + " position:" + position);

        if (grabBO.getStatus() == 1) {

            if (viewHolder.txt.getTag() == null) {
                viewHolder.txt.setTag(grabBO.getPicture());
                TimeCountDownUtil timeCountDownUtil = new TimeCountDownUtil(grabBO.getStartCountdown(), 1000, viewHolder.txt, "1", viewHolder.img_bg_remind);
                LogUtils.i(TAG, "tag:" + viewHolder.txt.getTag() + " pic:" + grabBO.getPicture());
                timeCountDownUtil.start();
                getCountDownUtils().put(position, timeCountDownUtil);
            }
            LogUtils.i(TAG, "活动已开始" + " position:" + position + " 内容:" + viewHolder.txt.getText().toString());
            viewHolder.progressBar.setVisibility(View.VISIBLE);
            viewHolder.progressBar.setVisibility(View.VISIBLE);
            viewHolder.progressBar.setProgress(grabBO.getCurrWinNum());
            viewHolder.progressBar.setMax(grabBO.getTotalWinNum());

            viewHolder.llayout_remind.setVisibility(View.VISIBLE);
            LogUtils.i(TAG, grabBO.getActiveId() + ":" + "MyUtil.isRemind(grabBO.getActiveId())" + MyUtil.isRemind(grabBO.getActiveId()));
            if (MyUtil.isRemind(grabBO.getActiveId())) {
                viewHolder.llayout_remind.setOnClickListener(new alarmClikListener(grabBO.getTitle(), grabBO.getActiveId(), grabBO.getStartDt(), grabBO.getEndDt(), false, viewHolder.img_bg_remind));
                viewHolder.img_bg_remind.setImageResource(R.drawable.ic_grab_tip_yes);
            } else {
                viewHolder.llayout_remind.setOnClickListener(new alarmClikListener(grabBO.getTitle(), grabBO.getActiveId(), grabBO.getStartDt(), grabBO.getEndDt(), true, viewHolder.img_bg_remind));
                viewHolder.img_bg_remind.setImageResource(R.drawable.ic_grab_tip_no);
            }
        } else {
            viewHolder.progressBar.setVisibility(View.GONE);
            viewHolder.txt.setText("活动已结束");
            viewHolder.txt.setTextColor(Color.parseColor("#E81540"));
            viewHolder.llayout_remind.setVisibility(View.GONE);
        }

        return convertView;
    }


    class ViewHolder {
        private LinearLayout llayout_remind;
        private ImageView image;
        private ImageView img_bg_remind;
        private TextView txt;
        private ProgressSeekBar progressBar;
    }

    public class alarmClikListener implements View.OnClickListener {
        String title;
        int activeId;
        String startDt;
        String endDt;
        boolean flag;
        ImageView img_bg_remind;

        public alarmClikListener(String title, int activeId, String startDt, String endDt, boolean flag, ImageView img_bg_remind) {
            this.title = title;
            this.activeId = activeId;
            this.startDt = startDt;
            this.endDt = endDt;
            this.flag = flag;
            this.img_bg_remind = img_bg_remind;
        }

        @Override
        public void onClick(View v) {
            alarmtListener.setAlarm(title, activeId, startDt, endDt, flag);
            if (flag) {
                img_bg_remind.setImageResource(R.drawable.ic_grab_tip_yes);
                flag = false;
            } else {
                img_bg_remind.setImageResource(R.drawable.ic_grab_tip_no);
                flag = true;
            }

        }
    }

    public SparseArray<TimeCountDownUtil> getCountDownUtils() {
        return countDownUtils;
    }

    public static SparseArray<RelativeLayout> getLayoutSparseArray() {
        return layoutSparseArray;
    }
}
