/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.model.WinningRecordsBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;

/**
 * 中奖记录
 * 作者: 李苜菲
 * 日期: 2015/11/23 13:43.
 */
public class WinningRecordAdapter extends MyBaseAdapter<WinningRecordsBO> {
    private static final java.lang.String TAG = "WinningRecordAdapter";
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

            viewHolder.bgImg = (ImageView) convertView.findViewById(R.id.img_winPic);
            viewHolder.statusImg = (ImageView) convertView.findViewById(R.id.img_getStatus);
            viewHolder.titleTxt = (TextView) convertView.findViewById(R.id.txt_winName);
            viewHolder.codeTxt = (TextView) convertView.findViewById(R.id.txt_winCode);
            viewHolder.dateTxt = (TextView) convertView.findViewById(R.id.txt_winDate);
            viewHolder.addressTxt = (TextView) convertView.findViewById(R.id.txt_winAddress);
            viewHolder.endDateTimeTxt = (TextView) convertView.findViewById(R.id.txt_winEndDate);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();


        winningRecordsBO = itemList.get(position);
        LogUtils.i("WinningRecordAdapter", "winningRecordsBO:" + winningRecordsBO.getIsGot() + ":" + position);
        if (winningRecordsBO.getIsGot() == 1)
            viewHolder.statusImg.setImageResource(R.drawable.ic_get);
        else
            viewHolder.statusImg.setImageResource(R.drawable.ic_unget);
        viewHolder.titleTxt.setText(winningRecordsBO.getTitle());
        viewHolder.codeTxt.setText(MyUtil.addSpaceBy4(winningRecordsBO.getWinNumber()));
        viewHolder.dateTxt.setText(winningRecordsBO.getPlayDt());
        viewHolder.addressTxt.setText(winningRecordsBO.getGetAddress());
        viewHolder.endDateTimeTxt.setText(winningRecordsBO.getGetEndDt());
        ImageLoader.getInstance().displayImage(winningRecordsBO.getPicture(), viewHolder.bgImg);
        LogUtils.i(TAG, winningRecordsBO.getPicture());
        viewHolder.statusImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return convertView;
    }

    class ViewHolder {
        private ImageView bgImg;//背景
        private ImageView statusImg;//奖品状态
        private TextView codeTxt;//获奖编码
        private TextView titleTxt;//获奖时间
        private TextView dateTxt;//获奖时间
        private TextView addressTxt;//获奖时间
        private TextView endDateTimeTxt;//截止时间
    }
}
