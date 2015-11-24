/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.model.InStationMessagesBO;
import com.poomoo.ohmygod.R;

/**
 * 站内信息适配器
 * 作者: 李苜菲
 * 日期: 2015/11/24 11:06.
 */
public class InStationMessagesAdapter extends MyBaseAdapter<InStationMessagesBO> {
    public InStationMessagesAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_in_station_messages, null);
            viewHolder.avatarImg = (ImageView) convertView.findViewById(R.id.img_message_avatar);
            viewHolder.titleTxt = (TextView) convertView.findViewById(R.id.txt_message_title);
            viewHolder.dateTimeTxt = (TextView) convertView.findViewById(R.id.txt_message_datetime);
            viewHolder.conentTxt = (TextView) convertView.findViewById(R.id.txt_message_content);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        return convertView;
    }

    class ViewHolder {
        private ImageView avatarImg;
        private TextView titleTxt;
        private TextView dateTimeTxt;
        private TextView conentTxt;
    }
}
