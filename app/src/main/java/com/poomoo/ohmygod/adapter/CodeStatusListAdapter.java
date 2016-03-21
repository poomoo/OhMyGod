/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.model.CodeBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;

/**
 * 活动中奖者适配器
 * 作者: 李苜菲
 * 日期: 2015/11/24 11:06.
 */
public class CodeStatusListAdapter extends MyBaseAdapter<CodeBO> {
    private final DisplayImageOptions defaultOptions;
    private CodeBO codeBO;

    public CodeStatusListAdapter(Context context) {
        super(context);
        defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.drawable.ic_avatar) //
                .showImageOnFail(R.drawable.ic_avatar) //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_code_status, null);
            viewHolder.avatarImg = (ImageView) convertView.findViewById(R.id.img_avatar);
            viewHolder.codeTxt = (TextView) convertView.findViewById(R.id.txt_code);
            viewHolder.nameTxt = (TextView) convertView.findViewById(R.id.txt_nickName);
            viewHolder.telTxt = (TextView) convertView.findViewById(R.id.txt_tel);
            viewHolder.activeNameTxt = (TextView) convertView.findViewById(R.id.txt_activeName);
            viewHolder.dateTimeTxt = (TextView) convertView.findViewById(R.id.txt_time);
            viewHolder.statusTxt = (TextView) convertView.findViewById(R.id.txt_status);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        codeBO = itemList.get(position);
        ImageLoader.getInstance().displayImage(codeBO.getHeadPic(), viewHolder.avatarImg, defaultOptions);
        viewHolder.nameTxt.setText(codeBO.getNickName());
//        viewHolder.telTxt.setText(codeBO.get);
        viewHolder.activeNameTxt.setText(codeBO.getActiveName());
        viewHolder.dateTimeTxt.setText(codeBO.getPlay_dt());
        if (codeBO.getIsGot() == 0)//未领取
        {
            viewHolder.codeTxt.setText(MyUtil.hiddenCode(codeBO.getWinNumber()));
            viewHolder.statusTxt.setText("未验证");
            viewHolder.statusTxt.setTextColor(Color.parseColor("#828282"));
        } else {//已领取
            viewHolder.codeTxt.setText(codeBO.getWinNumber());
            viewHolder.statusTxt.setText("已验证");
            viewHolder.statusTxt.setTextColor(Color.parseColor("#E81540"));
        }

        return convertView;
    }

    class ViewHolder {
        private ImageView avatarImg;
        private TextView codeTxt;
        private TextView nameTxt;
        private TextView telTxt;
        private TextView activeNameTxt;
        private TextView dateTimeTxt;
        private TextView statusTxt;
    }
}
