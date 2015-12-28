/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poomoo.ohmygod.R;

/**
 * 作者: 李苜菲
 * 日期: 2015/12/28 15:42.
 */
public class TimeAdapter extends MyBaseAdapter<Integer> {
    public TimeAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_time, null);
            viewHolder.txt = (TextView) convertView.findViewById(R.id.txt_time);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.txt.setText(itemList.get(position) + "分钟");
        return convertView;
    }

    class ViewHolder {
        private TextView txt;
    }
}
