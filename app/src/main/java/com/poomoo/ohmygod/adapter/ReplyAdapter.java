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

import com.poomoo.model.ReplyBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.ReplyListener;
import com.poomoo.ohmygod.utils.MyUtil;

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
    private ReplyListener listener;
    private int selectPosition;

    public ReplyAdapter(Context context, ReplyListener listener,int selectPosition) {
        super(context);
        this.listener = listener;
        this.selectPosition=selectPosition;
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
        ss.setSpan(new TextClick(true, replyName), 0,
                replyName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //为评论的人的添加点击事件
        ss.setSpan(new TextClick(false, toName), replyName.length() + 2,
                replyName.length() + toName.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置字体颜色
        ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.themeYellow)), 0,
                replyName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.themeYellow)), replyName.length() + 2,
                replyName.length() + toName.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        viewHolder.textView.setText(ss);
        //添加点击事件时，必须设置
        viewHolder.textView.setMovementMethod(LinkMovementMethod.getInstance());
        return convertView;
    }

    public final class TextClick extends ClickableSpan {
        private String name;
        private boolean status;

        public TextClick(boolean status, String name) {
            super();
            this.status = status;
            this.name = name;
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
                listener.onResult(name,selectPosition);
            } else {
//                MyUtil.showToast(context, "点击" + name);
                listener.onResult(name,selectPosition);
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

    class ViewHolder {
        public TextView textView;
    }

}
