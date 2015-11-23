/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poomoo.ohmygod.R;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/19 10:26.
 */
public class MySpinnerAdapter extends MyBaseAdapter<List<String>> {
    public MySpinnerAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder ;
        if (view == null) {
            view = inflater.inflate(R.layout.myspinner_dropdown_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.layout = (LinearLayout) view
                    .findViewById(R.id.myspinner_dropdown_layout);
            viewHolder.textView = (TextView) view
                    .findViewById(R.id.myspinner_dropdown_textView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        return view;
    }

    public class ViewHolder {
        LinearLayout layout;
        TextView textView;
    }
}
