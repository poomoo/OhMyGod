/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poomoo.ohmygod.R;

/**
 * 个人中心适配
 * 作者: 李苜菲
 * 日期: 2015/11/20 16:19.
 */
public class PersonalCenterAdapter extends MyBaseAdapter {
    private static final int[] name = {R.string.label_snatch, R.string.label_winning, R.string.label_my_withdraw_deposit, R.string.label_withdraw_deposit_now, R.string.label_inner_information, R.string.label_my_show};
    private static final int[] ic = {R.drawable.ic_snatch, R.drawable.ic_winning, R.drawable.ic_my_withdraw_deposit,
            R.drawable.ic_withdraw_deposit_now, R.drawable.ic_inner_information, R.drawable.ic_my_show};
    private GridView gridView;

    public PersonalCenterAdapter(Context context, GridView gridView) {
        super(context);
        this.gridView = gridView;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_grid_personal_center, parent,
                    false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.img_personal_center_ic);
            viewHolder.textView = (TextView) convertView
                    .findViewById(R.id.txt_personal_center_name);
            viewHolder.linearLayout = (LinearLayout) convertView
                    .findViewById(R.id.item_home_layout);
            viewHolder.bottomLineView = convertView
                    .findViewById(R.id.bottomline);
            viewHolder.rightLineView = convertView
                    .findViewById(R.id.rightline);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(name[position]);
        viewHolder.imageView.setImageResource(ic[position]);
        // 设置layout大小，以免出现下边框不显示的情况
        viewHolder.linearLayout
                .setLayoutParams(new RelativeLayout.LayoutParams(
                        AbsListView.LayoutParams.MATCH_PARENT,
                        (gridView.getHeight() - 3) / 2));

        // 最右边就隐藏右边线
        if (position == 2 || position == 5) {
            viewHolder.rightLineView.setVisibility(View.GONE);
        }
        // 最下边隐藏下边线
        if (position == 3 || position == 4 || position == 5) {
            viewHolder.bottomLineView.setVisibility(View.GONE);
        }

        // 设置gridview的item的高度,与屏幕适配
        AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                gridView.getHeight() / 2);
        convertView.setLayoutParams(param);

        return convertView;
    }

    private class ViewHolder {

        private TextView textView;
        private ImageView imageView;
        private View rightLineView, bottomLineView;
        private LinearLayout linearLayout;
    }
}
