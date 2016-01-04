/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.model.ShowBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.listeners.ActivityListener;
import com.poomoo.ohmygod.listeners.LongClickListener;
import com.poomoo.ohmygod.listeners.ReplyListener;
import com.poomoo.ohmygod.listeners.ShareListener;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.view.activity.CommodityInformation2Activity;
import com.poomoo.ohmygod.view.custom.NoScrollGridView;

/**
 * 晒适配器
 * 作者: 李苜菲
 * 日期: 2015/11/20 10:39.
 */
public class ShowAdapter extends MyBaseAdapter<ShowBO> {
    private final DisplayImageOptions defaultOptions;
    private final DisplayImageOptions defaultOptions1;
    private String TAG = "ShowAdapter";
    private PicsGridAdapter picsGridAdapter;
    private CommentAdapter commentAdapter;
    private ShowBO showBO;
    private ReplyListener listener;
    private ShareListener shareListener;
    private LongClickListener longClickListener;
    private ActivityListener activityListener;

    public ShowAdapter(Context context, ReplyListener listener, ShareListener shareListener, LongClickListener longClickListener, ActivityListener activityListener) {
        super(context);
        this.listener = listener;
        this.shareListener = shareListener;
        this.longClickListener = longClickListener;
        this.activityListener = activityListener;
        defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.drawable.ic_avatar) //
                .showImageOnFail(R.drawable.ic_avatar) //
                .cacheInMemory(true) //
                .cacheOnDisk(false) //
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//
        defaultOptions1 = new DisplayImageOptions.Builder() //
                .cacheInMemory(true) //
                .cacheOnDisk(false) //
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        showBO = itemList.get(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_show, null);
            viewHolder.avatarImg = (ImageView) convertView.findViewById(R.id.img_show_avatar);
            viewHolder.shareImg = (ImageView) convertView.findViewById(R.id.img_share);
            viewHolder.smallImg = (ImageView) convertView.findViewById(R.id.img_small);
            viewHolder.nameTxt = (TextView) convertView.findViewById(R.id.txt_show_userName);
            viewHolder.dateTimeTxt = (TextView) convertView.findViewById(R.id.txt_show_dateTime);
            viewHolder.contentTxt = (TextView) convertView.findViewById(R.id.txt_show_content);
            viewHolder.titleTxt = (TextView) convertView.findViewById(R.id.txt_show_title);
            viewHolder.gridView = (NoScrollGridView) convertView.findViewById(R.id.grid_show);
            viewHolder.listView = (ListView) convertView.findViewById(R.id.list_reply);
            viewHolder.commentEdt = (EditText) convertView.findViewById(R.id.edt_comment);
            viewHolder.replyEdt = (EditText) convertView.findViewById(R.id.edt_reply);
            viewHolder.commentBtn = (Button) convertView.findViewById(R.id.btn_comment);
            viewHolder.replyBtn = (Button) convertView.findViewById(R.id.btn_reply);
            viewHolder.commentLlayout = (LinearLayout) convertView.findViewById(R.id.llayout_comment);
            viewHolder.activeInfoLlayout = (LinearLayout) convertView.findViewById(R.id.llayout_activeInfo);
            viewHolder.replyImg = (ImageView) convertView.findViewById(R.id.img_reply);
            viewHolder.picsGridAdapter = new PicsGridAdapter(context);
            viewHolder.gridView.setAdapter(viewHolder.picsGridAdapter);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //屏蔽掉gridview的点击事件，保持listview的点击事件
//        holderView.gridView.setClickable(false);
//        holderView.gridView.setPressed(false);
//        holderView.gridView.setEnabled(false);

        viewHolder.nameTxt.setText(showBO.getNickName());
        viewHolder.dateTimeTxt.setText(showBO.getDynamicDt());
        viewHolder.contentTxt.setText(showBO.getContent());
        viewHolder.titleTxt.setText(showBO.getTitle());
        LogUtils.i(TAG, "activeId:" + showBO.getActiveId());
        viewHolder.activeInfoLlayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.i(TAG, "onClick activeId:" + Integer.parseInt(showBO.getActiveId()));
                activityListener.onClick(showBO.getTitle(), Integer.parseInt(showBO.getActiveId()));
            }
        });

        ImageLoader.getInstance().displayImage(showBO.getShowPic(), viewHolder.smallImg, defaultOptions1);

        if (showBO.getComments() != null && showBO.getComments().size() > 0) {
            viewHolder.listView.setVisibility(View.VISIBLE);
            commentAdapter = new CommentAdapter(context, listener, longClickListener, position);
            viewHolder.listView.setAdapter(commentAdapter);
            commentAdapter.setItems(showBO.getComments());
        } else
            viewHolder.listView.setVisibility(View.GONE);


        viewHolder.replyImg.setOnClickListener(new imgClickListener(position, showBO));
        viewHolder.shareImg.setOnClickListener(new shareClickListener(showBO.getTitle(), showBO.getContent(), showBO.getPicList().size() > 0 ? showBO.getPicList().get(0) : "", showBO.getDynamicId()));


        // 给 ImageView 设置一个 tag
        viewHolder.avatarImg.setTag(showBO.getHeadPic());
        // 预设一个图片
        viewHolder.avatarImg.setImageResource(R.drawable.ic_avatar);

        // 通过 tag 来防止图片错位
        if (viewHolder.avatarImg.getTag() != null && viewHolder.avatarImg.getTag().equals(showBO.getHeadPic())) {
            ImageLoader.getInstance().displayImage(showBO.getHeadPic(), viewHolder.avatarImg, defaultOptions);
        }

        viewHolder.gridView.setTag(showBO.getActiveId());
        LogUtils.i(TAG, "position:" + position + " size:" + showBO.getPicList().size() + "");
        if (showBO.getPicList() != null && showBO.getPicList().size() > 0) {
            viewHolder.gridView.setVisibility(View.VISIBLE);
            if (viewHolder.gridView.getTag() != null && viewHolder.gridView.getTag().equals(showBO.getActiveId())) {
                viewHolder.picsGridAdapter.setItems(showBO.getPicList());
//            picsGridAdapter = new PicsGridAdapter(context);
//            picsGridAdapter.setItems(showBO.getPicList());
                LogUtils.i(TAG, "position:" + position + "有图片");
            }
        } else {
            viewHolder.gridView.setVisibility(View.GONE);
        }


        return convertView;
    }

    class ViewHolder {
        public ImageView avatarImg;
        public ImageView shareImg;
        public ImageView smallImg;
        public TextView nameTxt;
        public TextView dateTimeTxt;
        public TextView contentTxt;
        public TextView titleTxt;
        public NoScrollGridView gridView;
        public ListView listView;
        public EditText commentEdt;
        public EditText replyEdt;
        public Button commentBtn;
        public Button replyBtn;
        public LinearLayout commentLlayout;
        public LinearLayout activeInfoLlayout;
        public ImageView replyImg;
        public PicsGridAdapter picsGridAdapter;
    }

    public class imgClickListener implements OnClickListener {
        int position;
        ShowBO showBO;

        public imgClickListener(int position, ShowBO showBO) {
            this.position = position;
            this.showBO = showBO;
        }

        @Override
        public void onClick(View v) {
            listener.onResult("", v, showBO, position, 0);
        }
    }

    public class shareClickListener implements OnClickListener {
        private String title;
        private String content;
        private String picUrl;
        private String dynamicId;

        public shareClickListener(String title, String content, String picUrl, String dynamicId) {
            this.title = title;
            this.content = content;
            this.picUrl = picUrl;
            this.dynamicId = dynamicId;
        }

        @Override
        public void onClick(View v) {
            shareListener.onResult(title, content, picUrl,dynamicId);
        }
    }

}
