/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.poomoo.model.CommentBO;
import com.poomoo.model.ReplyBO;
import com.poomoo.model.ShowBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.ReplyListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 晒适配器
 * 作者: 李苜菲
 * 日期: 2015/11/20 10:39.
 */
public class ShowAdapter extends MyBaseAdapter<ShowBO> {
    private PicsGridAdapter picsGridAdapter;
    private CommentAdapter commentAdapter;
    private ShowBO showBO;
    private CommentBO commentBO;
    private List<String> urlList;
    private ReplyListener listener;

    public ShowAdapter(Context context, ReplyListener listener) {
        super(context);
        this.picsGridAdapter = new PicsGridAdapter(context);
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_show, null);
            viewHolder.avatarImg = (ImageView) convertView.findViewById(R.id.img_show_avatar);
            viewHolder.nameTxt = (TextView) convertView.findViewById(R.id.txt_show_userName);
            viewHolder.dateTimeTxt = (TextView) convertView.findViewById(R.id.txt_show_dateTime);
            viewHolder.contentTxt = (TextView) convertView.findViewById(R.id.txt_show_content);
            viewHolder.titleTxt = (TextView) convertView.findViewById(R.id.txt_show_title);
            viewHolder.gridView = (GridView) convertView.findViewById(R.id.grid_show);
            viewHolder.listView = (ListView) convertView.findViewById(R.id.list_reply);
            viewHolder.commentEdt = (EditText) convertView.findViewById(R.id.edt_comment);
            viewHolder.replyEdt = (EditText) convertView.findViewById(R.id.edt_reply);
            viewHolder.commentBtn = (Button) convertView.findViewById(R.id.btn_comment);
            viewHolder.replyBtn = (Button) convertView.findViewById(R.id.btn_reply);
            viewHolder.commentLlayout = (LinearLayout) convertView.findViewById(R.id.llayout_comment);
//            viewHolder.replyLlayout = (RelativeLayout) convertView.findViewById(R.id.llayout_reply);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //屏蔽掉gridview的点击事件，保持listview的点击事件
//        holderView.gridView.setClickable(false);
//        holderView.gridView.setPressed(false);
//        holderView.gridView.setEnabled(false);

        showBO = itemList.get(position);
        viewHolder.nameTxt.setText(showBO.getNickName());
        viewHolder.dateTimeTxt.setText(showBO.getDynamicDt());
        viewHolder.contentTxt.setText(showBO.getContent());
        viewHolder.titleTxt.setText(showBO.getTitle());

        viewHolder.gridView.setAdapter(picsGridAdapter);
        picsGridAdapter.setItems(showBO.getPicList());


        commentAdapter = new CommentAdapter(context, listener);
        viewHolder.listView.setAdapter(commentAdapter);
        commentAdapter.setItems(showBO.getComments());

        return convertView;
    }

    class ViewHolder {
        public ImageView avatarImg;
        public TextView nameTxt;
        public TextView dateTimeTxt;
        public TextView contentTxt;
        public TextView titleTxt;
        public GridView gridView;
        public ListView listView;
        public EditText commentEdt;
        public EditText replyEdt;
        public Button commentBtn;
        public Button replyBtn;
        //        public RelativeLayout replyLlayout;
        public LinearLayout commentLlayout;
    }
}
