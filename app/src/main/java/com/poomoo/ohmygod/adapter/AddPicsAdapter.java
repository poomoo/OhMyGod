/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.picUtils.Bimp;
import com.poomoo.ohmygod.utils.picUtils.FileUtils;

import java.io.IOException;

/**
 * 选择上传图片适配器
 * 作者: 李苜菲
 * 日期: 2015/11/23 15:30.
 */
public class AddPicsAdapter extends MyBaseAdapter {
    public AddPicsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        int size = Bimp.bmp.size();
        if (size == 0)
            return 1;
        else if (size == 9)
            return 9;
        else
            return size + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_grid_pics, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.img_grid);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == Bimp.bmp.size()) {
            if (position == 9) {
                holder.image.setVisibility(View.GONE);
            } else
                holder.image
                        .setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_addpic_unfocused));
        } else {
            holder.image.setImageBitmap(Bimp.bmp.get(position));
        }
        return convertView;
    }

    public class ViewHolder {
        public ImageView image;
    }

    Handler handler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    AddPicsAdapter.this.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void update() {
        loading();
    }

    public void loading() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    if (Bimp.max == Bimp.drr.size()) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                        break;
                    } else {
                        try {
                            String path = Bimp.drr.get(Bimp.max);
                            System.out.println(path);
                            Bitmap bm = Bimp.revitionImageSize(path);
                            Bimp.bmp.add(bm);
                            String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
                            FileUtils.saveBitmap(bm, "" + newStr);
                            Bimp.max += 1;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
