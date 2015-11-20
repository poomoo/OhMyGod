/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.poomoo.model.ReplyBO;
import com.poomoo.ohmygod.R;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/20 11:49.
 */
public class ReplyAdapter extends MyBaseAdapter<ReplyBO> {
    private SpannableString ss;
    private String replyName;
    private String replyContentStr;
    private String commentName;
    private ShowAdapter.ViewHolder viewHolder;

    public ReplyAdapter(Context context, ShowAdapter.ViewHolder viewHolder) {
        super(context);
        this.viewHolder = viewHolder;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holderView;
        if (convertView == null) {
            holderView = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_list_reply, null);
            holderView.textView = (TextView) convertView.findViewById(R.id.txt_reply_content);
            convertView.setTag(holderView);
        } else {
            holderView = (ViewHolder) convertView.getTag();
        }
        ReplyBO replyBO = itemList.get(position);

        replyName = replyBO.getRevert_user_name();
        commentName = replyBO.getFloor_user_name();
        replyContentStr = replyBO.getContent();
        //用来标识在 Span 范围内的文本前后输入新的字符时是否把它们也应用这个效果
        //Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(前后都不包括)
        //Spanned.SPAN_INCLUSIVE_EXCLUSIVE(前面包括，后面不包括)
        //Spanned.SPAN_EXCLUSIVE_INCLUSIVE(前面不包括，后面包括)
        //Spanned.SPAN_INCLUSIVE_INCLUSIVE(前后都包括)
        if (TextUtils.isEmpty(replyName)) {
            ss = new SpannableString(commentName + ":" + replyContentStr);
            ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.themeYellow)), 0,
                    commentName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ss = new SpannableString(replyName + "回复" + commentName
                    + "：" + replyContentStr);
            ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.themeYellow)), 0,
                    replyName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.themeYellow)), replyName.length() + 2,
                    replyName.length() + commentName.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            commentName = replyName;
        }
        holderView.textView.setText(ss);
        //添加点击事件时，必须设置
        holderView.textView.setMovementMethod(LinkMovementMethod.getInstance());
        holderView.textView.setOnClickListener(new TextClick(commentName, viewHolder));
        return convertView;
    }

    public final class TextClick implements View.OnClickListener {
        private String name;
        private ShowAdapter.ViewHolder viewHolder;

        public TextClick(String name, ShowAdapter.ViewHolder viewHolder) {
            super();
            this.name = name;
            this.viewHolder = viewHolder;
        }

        @Override
        public void onClick(View v) {
            Log.i("lmf", "name:" + this.name);
            viewHolder.commentLlayout.setVisibility(View.INVISIBLE);
            viewHolder.replyEdt.setHint("@" + this.name);
//            commentName = this.name;
            viewHolder.replyEdt.setHintTextColor(Color.GRAY);
            viewHolder.replyEdt.setFocusable(true);
            viewHolder.replyEdt.requestFocus();
            InputMethodManager inputManager =
                    (InputMethodManager) viewHolder.replyEdt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(viewHolder.replyEdt, 0);
        }
    }

    class ViewHolder {
        public TextView textView;
    }

}
