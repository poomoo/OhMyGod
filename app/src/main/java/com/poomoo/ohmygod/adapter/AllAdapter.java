/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poomoo.model.SnatchBO;
import com.poomoo.ohmygod.R;

/**
 * 夺宝记录全部
 * 作者: 李苜菲
 * 日期: 2015/11/23 10:18.
 */
public class AllAdapter extends MyBaseAdapter<SnatchBO> {
    public AllAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_list_snatch_record,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.rlayout = (RelativeLayout) convertView
                    .findViewById(R.id.rlayout_snatch_record);
            viewHolder.txt = (TextView) convertView
                    .findViewById(R.id.txt_snatch_record);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SnatchBO snatchBO = itemList.get(position);
//        ImageLoader.getInstance().loadImage(snatchBO.getUrl(), new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                Drawable drawable = new BitmapDrawable(context.getResources(), loadedImage);
//                viewHolder.rlayout.setBackground(drawable);
//            }
//        });
        return convertView;
    }

    class ViewHolder {
        private RelativeLayout rlayout;
        private TextView txt;
    }
}
