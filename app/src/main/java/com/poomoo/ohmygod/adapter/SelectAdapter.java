/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poomoo.ohmygod.R;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/27 14:48.
 */
public class SelectAdapter extends MyBaseAdapter<String> {
    public SelectAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_select, null);
            viewHolder.contentTxt = (TextView) convertView.findViewById(R.id.txt_select);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        String content = itemList.get(position);

        viewHolder.contentTxt.setText(content);
        return convertView;
    }

    class ViewHolder {
        TextView contentTxt;
    }
}
