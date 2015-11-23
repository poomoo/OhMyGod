/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.model.ImageItem;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.picUtils.Bimp;
import com.poomoo.ohmygod.utils.picUtils.BitmapCache;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/23 16:13.
 */
public class ImageGridAdapter extends MyBaseAdapter<ImageItem> {
    final String TAG = getClass().getSimpleName();

    private TextCallback textcallback = null;
    public Map<String, String> map = new HashMap<>();
    private BitmapCache cache;
    private Handler mHandler;
    private int selectTotal = 0;
    BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap,
                              Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals(imageView.getTag())) {
                    (imageView).setImageBitmap(bitmap);
                } else {
                    Log.e(TAG, "callback, bmp not match");
                }
            } else {
                Log.e(TAG, "callback, bmp null");
            }
        }
    };

    public ImageGridAdapter(Context context, Handler mHandler) {
        super(context);
        cache = new BitmapCache();
        this.mHandler = mHandler;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (itemList != null) {
            count = itemList.size();
        }
        return count;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;

        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_grid_image, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.img_grid_image);
            holder.selected = (ImageView) convertView
                    .findViewById(R.id.img_grid_image_isselected);
            holder.text = (TextView) convertView
                    .findViewById(R.id.txt_grid_image);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final ImageItem item = itemList.get(position);

        holder.iv.setTag(item.imagePath);
        cache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath,
                callback);
        if (item.isSelected) {
            holder.selected.setImageResource(R.drawable.ic_image_selected);
            holder.text.setBackgroundResource(R.drawable.bg_relatly_line);
        } else {
            holder.selected.setImageResource(-1);
            holder.text.setBackgroundColor(0x00000000);
        }
        holder.iv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String path = itemList.get(position).imagePath;

                if ((Bimp.drr.size() + selectTotal) < 9) {
                    item.isSelected = !item.isSelected;
                    if (item.isSelected) {
                        holder.selected
                                .setImageResource(R.drawable.ic_image_selected);
                        holder.text.setBackgroundResource(R.drawable.bg_relatly_line);
                        selectTotal++;
                        if (textcallback != null)
                            textcallback.onListen(selectTotal);
                        map.put(path, path);

                    } else if (!item.isSelected) {
                        holder.selected.setImageResource(-1);
                        holder.text.setBackgroundColor(0x00000000);
                        selectTotal--;
                        if (textcallback != null)
                            textcallback.onListen(selectTotal);
                        map.remove(path);
                    }
                } else if ((Bimp.drr.size() + selectTotal) >= 9) {
                    if (item.isSelected == true) {
                        item.isSelected = !item.isSelected;
                        holder.selected.setImageResource(-1);
                        holder.text.setBackgroundColor(0x00000000);
                        selectTotal--;
                        map.remove(path);
                    } else {
                        Message message = Message.obtain(mHandler, 0);
                        message.sendToTarget();
                    }
                }
            }
        });

        return convertView;
    }

    class Holder {
        private ImageView iv;
        private ImageView selected;
        private TextView text;
    }

    public void setTextCallback(TextCallback listener) {
        textcallback = listener;
    }

    public static interface TextCallback {
        public void onListen(int count);
    }
}
