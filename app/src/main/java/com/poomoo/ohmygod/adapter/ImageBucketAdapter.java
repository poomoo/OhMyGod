/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.model.ImageBucket;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.picUtils.BitmapCache;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/23 15:49.
 */
public class ImageBucketAdapter extends MyBaseAdapter<ImageBucket> {
    private final String TAG = getClass().getSimpleName();
    private BitmapCache cache;
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


    public ImageBucketAdapter(Context context) {
        super(context);
        cache = new BitmapCache();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder ViewHolder;
        if (convertView == null) {
            ViewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_grid_image_bucket, null);
            ViewHolder.iv = (ImageView) convertView.findViewById(R.id.img_bucket_pic);
            ViewHolder.selected = (ImageView) convertView.findViewById(R.id.img_bucket_isselected);
            ViewHolder.nameTxt = (TextView) convertView.findViewById(R.id.txt_bucket_name);
            ViewHolder.countTxt = (TextView) convertView.findViewById(R.id.txt_bucket_count);
            convertView.setTag(ViewHolder);
        } else {
            ViewHolder = (ViewHolder) convertView.getTag();
        }
        ImageBucket item = itemList.get(position);
        ViewHolder.countTxt.setText("" + item.count);
        ViewHolder.nameTxt.setText(item.bucketName);
        ViewHolder.selected.setVisibility(View.GONE);
        if (item.imageList != null && item.imageList.size() > 0) {
            String thumbPath = item.imageList.get(0).thumbnailPath;
            String sourcePath = item.imageList.get(0).imagePath;
            ViewHolder.iv.setTag(sourcePath);
            cache.displayBmp(ViewHolder.iv, thumbPath, sourcePath, callback);
        } else {
            ViewHolder.iv.setImageBitmap(null);
            Log.e(TAG, "no images in bucket " + item.bucketName);
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView iv;
        private ImageView selected;
        private TextView nameTxt;
        private TextView countTxt;
    }
}
