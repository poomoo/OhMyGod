/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/20 10:51.
 */
public class PicsGridAdapter extends BaseAdapter {
    protected Context context;
    protected LayoutInflater inflater;
    protected List<String> list = new ArrayList<>();

    public PicsGridAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setItems(List<String> itemList) {
        this.list = itemList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_grid_pics, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.img_grid);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String url = list.get(position) + "";
        LogUtils.i("position", "gridview url:" + url);
        viewHolder.imageView.setTag(url);
        // 通过 tag 来防止图片错位
        if (viewHolder.imageView.getTag() != null && viewHolder.imageView.getTag().equals(url))
            ImageLoader.getInstance().displayImage(url, viewHolder.imageView);

        return convertView;
    }

    class ViewHolder {
        public ImageView imageView;
    }

}
