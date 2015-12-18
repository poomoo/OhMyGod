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
import android.widget.TextView;

import com.poomoo.model.CommentBO;
import com.poomoo.model.ReplyBO;
import com.poomoo.model.ShowBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.listeners.LongClickListener;
import com.poomoo.ohmygod.listeners.ReplyListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 回复
 * 作者: 李苜菲
 * 日期: 2015/11/20 11:49.
 */
public class ReplyAdapter extends MyBaseAdapter<ReplyBO> {
    private SpannableString ss;
    private ReplyBO replyBO = new ReplyBO();//回复
    private String replyName;//回复人
    private String toName;//被回复的人
    private String content;//内容
    private String commentId;//评论的ID
    private ReplyListener listener;
    private int selectPosition;
    private int commentPostion;
    private ShowBO showBO;
    private CommentBO commentBO;
    private List<CommentBO> commentBOList;
    private List<ReplyBO> replyBOList;
    private LongClickListener longClickListener;

    public ReplyAdapter(Context context, ReplyListener listener, LongClickListener longClickListener, int selectPosition, String commentId, int commentPostion) {
        super(context);
        this.listener = listener;
        this.longClickListener = longClickListener;
        this.selectPosition = selectPosition;
        this.commentPostion = commentPostion;
        this.commentId = commentId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_reply, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.txt_reply_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        replyBO = itemList.get(position);


        replyBOList = new ArrayList<>();
        replyBOList.add(replyBO);
        commentBO = new CommentBO();
        commentBO.setCommentId(commentId);
        commentBO.setReplies(replyBOList);
        commentBOList = new ArrayList<>();
        commentBOList.add(commentBO);
        showBO = new ShowBO();
        showBO.setComments(commentBOList);


        replyName = replyBO.getFromNickName();
        toName = replyBO.getToNickName();
        content = replyBO.getContent();

        //用来标识在 Span 范围内的文本前后输入新的字符时是否把它们也应用这个效果
        //Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括)
        //Spanned.SPAN_INCLUSIVE_EXCLUSIVE(前面包括，后面不包括)
        //Spanned.SPAN_EXCLUSIVE_INCLUSIVE(前面不包括，后面包括)
        //Spanned.SPAN_INCLUSIVE_INCLUSIVE(前后都包括)

        ss = new SpannableString(replyName + "回复" + toName
                + "：" + content);

        //为回复的人昵称添加点击事件
        ss.setSpan(new TextClick(true, replyName, showBO), 0,
                replyName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //为评论的人的添加点击事件
        ss.setSpan(new TextClick(false, toName, showBO), replyName.length() + 2,
                replyName.length() + toName.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体颜色
        ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.reply)), 0,
                replyName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.reply)), replyName.length() + 2,
                replyName.length() + toName.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        viewHolder.textView.setText(ss);
        //添加点击事件时，必须设置
        viewHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());

        viewHolder.textView.setOnLongClickListener(new LongClick(content));
        return convertView;
    }

    class ViewHolder {
        public TextView textView;
    }

    public final class TextClick extends ClickableSpan {
        private String name;
        private boolean status;
        private ShowBO showBO;


        public TextClick(boolean status, String name, ShowBO showBO) {
            super();
            this.status = status;
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
            if (status) {
//                MyUtil.showToast(context, "点击" + name);
                listener.onResult(name, selectPosition, v, showBO, commentPostion);
            } else {
//                MyUtil.showToast(context, "点击" + name);
                listener.onResult(name, selectPosition, v, showBO, commentPostion);
            }

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
