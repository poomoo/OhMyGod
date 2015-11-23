/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.model.WinningBO;
import com.poomoo.ohmygod.R;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/23 13:43.
 */
public class WinningRecordAdapter extends MyBaseAdapter<WinningBO> {
    private WinningBO winningBO;

    public WinningRecordAdapter(Context context) {
        super(context);
        winningBO = new WinningBO();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_winning_record, null);
            viewHolder = new ViewHolder();
            viewHolder.fLayout = (FrameLayout) convertView.findViewById(R.id.flayout_winning_record);
            viewHolder.statusImg = (ImageView) convertView.findViewById(R.id.img_status);
            viewHolder.dateTimeTxt = (TextView) convertView.findViewById(R.id.txt_winning_dateTime);
            viewHolder.endDateTimeTxt = (TextView) convertView.findViewById(R.id.txt_winning_endTime);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        winningBO = itemList.get(position);
        return convertView;
    }

    class ViewHolder {
        private FrameLayout fLayout;
        private ImageView statusImg;//奖品状态
        private TextView dateTimeTxt;//获奖时间
        private TextView endDateTimeTxt;//截止时间
    }
}
