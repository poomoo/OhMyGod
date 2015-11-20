/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.ohmygod.R;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/20 10:51.
 */
public class PicsGridAdapter extends MyBaseAdapter<String> {
    public PicsGridAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView;
        if (convertView == null) {
            holderView = new HolderView();
            convertView = inflater.inflate(R.layout.item_grid_pics, null);
            holderView.imageView = (ImageView) convertView.findViewById(R.id.img_grid);

            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        String url = itemList.get(position) + "";
        ImageLoader.getInstance().displayImage(url, holderView.imageView);
        return convertView;
    }

    class HolderView {
        public ImageView imageView;
    }

}
