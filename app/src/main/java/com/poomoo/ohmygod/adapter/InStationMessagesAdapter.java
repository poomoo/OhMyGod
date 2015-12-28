/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.model.MessageBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;

/**
 * 站内信息适配器
 * 作者: 李苜菲
 * 日期: 2015/11/24 11:06.
 */
public class InStationMessagesAdapter extends MyBaseAdapter<MessageBO> {
    private MessageBO messageBO;

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
            viewHolder.statusImg = (ImageView) convertView.findViewById(R.id.img_inStationMessageStatus);
            viewHolder.titleTxt = (TextView) convertView.findViewById(R.id.txt_message_title);
            viewHolder.dateTimeTxt = (TextView) convertView.findViewById(R.id.txt_message_datetime);
            viewHolder.conentTxt = (TextView) convertView.findViewById(R.id.txt_message_content);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        messageBO = itemList.get(position);
        if (!MyUtil.isRead(messageBO.getStatementId()))
            viewHolder.statusImg.setVisibility(View.VISIBLE);
        else
            viewHolder.statusImg.setVisibility(View.GONE);

        viewHolder.titleTxt.setText(messageBO.getTitle());
//        viewHolder.conentTxt.setText(statementBO.getContent());
        viewHolder.dateTimeTxt.setText(messageBO.getInsertDt());
        return convertView;
    }

    class ViewHolder {
        private ImageView avatarImg;
        private ImageView statusImg;
        private TextView titleTxt;
        private TextView dateTimeTxt;
        private TextView conentTxt;
    }
}
