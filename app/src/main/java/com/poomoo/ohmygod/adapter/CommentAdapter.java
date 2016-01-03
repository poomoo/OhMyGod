/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.poomoo.model.CommentBO;
import com.poomoo.model.ShowBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.listeners.LongClickListener;
import com.poomoo.ohmygod.listeners.ReplyListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论
 * 作者: 李苜菲
 * 日期: 2015/12/4 10:11.
 */
public class CommentAdapter extends MyBaseAdapter<CommentBO> {
    private ReplyAdapter replyAdapter;
    private CommentBO commentBO = new CommentBO();//评论
    private String commentName;//评论人
    private String content;//内容
    private SpannableString ss;
    private ReplyListener listener;
    private int itemPosition;
    private ShowBO showBO;
    private List<CommentBO> list;
    private LongClickListener longClickListener;

    public CommentAdapter(Context context, ReplyListener listener, LongClickListener longClickListener, int itemPosition) {
        super(context);
        this.listener = listener;
        this.itemPosition = itemPosition;
        this.longClickListener = longClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_comment, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.txt_comment_content);
            viewHolder.listView = (ListView) convertView.findViewById(R.id.list_reply);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        commentBO = itemList.get(position);
        commentName = commentBO.getNickName();
        content = commentBO.getContent();
        ss = new SpannableString(commentName + ":" + content);
        //为回复的人昵称添加点击事件
        showBO = new ShowBO();
        list = new ArrayList<>();
        list.add(commentBO);
        showBO.setComments(list);
        ss.setSpan(new TextClick(commentName, showBO, position), 0, commentName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.themeRed)), 0, commentName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.textView.setText(ss);
        //添加点击事件时，必须设置
        viewHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());

        viewHolder.textView.setOnLongClickListener(new LongClick(content));

        replyAdapter = new ReplyAdapter(context, listener, longClickListener, commentBO.getCommentId(), itemPosition, position);
        viewHolder.listView.setAdapter(replyAdapter);
        replyAdapter.setItems(commentBO.getReplies());
        return convertView;
    }

    class ViewHolder {
        public TextView textView;
        public ListView listView;
    }

    public final class TextClick extends ClickableSpan {
        private String name;
        private ShowBO showBO;
        private int commentPosition;

        public TextClick(String name, ShowBO showBO, int commentPosition) {
            super();
            this.name = name;
            this.showBO = showBO;
            this.commentPosition = commentPosition;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);//取消下划线
        }

        @Override
        public void onClick(View v) {
            listener.onResult(name, v, showBO, itemPosition, commentPosition);
        }
    }

    private final class LongClick implements View.OnLongClickListener {
        private String content;

        public LongClick(String content) {
            this.content = content;
        }

        @Override
        public boolean onLongClick(View v) {
            longClickListener.onResult(content);
            return true;
        }
    }


}
