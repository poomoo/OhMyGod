/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.model.ShowBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.view.bigimage.ImagePagerActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/20 10:51.
 */
public class PicsGridAdapter extends BaseAdapter {
    private String TAG = "PicsGridAdapter";
    protected Context context;
    protected LayoutInflater inflater;
    protected ArrayList<String> list = new ArrayList<>();

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

    public void setItems(ArrayList<String> itemList) {
        this.list = itemList;
        notifyDataSetChanged();
    }

    //item_grid_pics  img_grid
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_grid_pics, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_grid);
        String url = list.get(position) + "";
        LogUtils.i("position", "gridview url:" + url);
//        viewHolder.imageView.setTag(url);
        // 通过 tag 来防止图片错位
//        if (viewHolder.imageView.getTag() != null && viewHolder.imageView.getTag().equals(url))
        ImageLoader.getInstance().displayImage(url, imageView);

        imageView.setOnClickListener(new imgClickListener(position, list));

        return view;
    }

    public class imgClickListener implements View.OnClickListener {
        int position;
        ArrayList<String> list = new ArrayList<>();

        public imgClickListener(int position, ArrayList<String> list) {
            this.position = position;
            this.list = list;
        }

        @Override
        public void onClick(View v) {
            imageBrowse(position, list);
        }
    }

    protected void imageBrowse(int position, ArrayList<String> urls2) {
        LogUtils.i(TAG, "position:" + position + " size:" + urls2.size());
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        context.startActivity(intent);
    }

}
