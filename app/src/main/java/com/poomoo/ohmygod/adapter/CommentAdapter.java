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
import com.poomoo.ohmygod.ReplyListener;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;

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
    private int selectPosition;
    private ShowBO showBO;
    private List<CommentBO> list;

    public CommentAdapter(Context context, ReplyListener listener, int selectPosition) {
        super(context);
        this.listener = listener;
        this.selectPosition = selectPosition;
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
        ss.setSpan(new TextClick(commentName, showBO), 0,
                commentName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.reply)), 0,
                commentName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.textView.setText(ss);
        //添加点击事件时，必须设置
        viewHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());

        replyAdapter = new ReplyAdapter(context, listener, selectPosition, commentBO.getCommentId(), position);
        viewHolder.listView.setAdapter(replyAdapter);
        replyAdapter.setItems(commentBO.getReplies());
        return convertView;
    }

    public final class TextClick extends ClickableSpan {
        private String name;
        private ShowBO showBO;

        public TextClick(String name, ShowBO showBO) {
            super();
            this.name = name;
            this.showBO = showBO;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);//取消下划线
        }

        @Override
        public void onClick(View v) {
//            MyUtil.showToast(context, "点击" + name);
            listener.onResult(name, selectPosition, v, showBO, 0);


//            viewHolder.commentLlayout.setVisibility(View.INVISIBLE);
//            viewHolder.replyEdt.setHint("@" + this.name);
//            viewHolder.replyEdt.setHintTextColor(Color.GRAY);
//            viewHolder.replyEdt.setFocusable(true);
//            viewHolder.replyEdt.requestFocus();
//            InputMethodManager inputManager =
//                    (InputMethodManager) viewHolder.replyEdt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputManager.showSoftInput(viewHolder.replyEdt, 0);
        }
    }

    class ViewHolder {
        public TextView textView;
        public ListView listView;
    }
}
