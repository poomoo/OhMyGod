package com.poomoo.ohmygod;

import android.view.View;

import com.poomoo.model.ShowBO;

/**
 * 评论监听回调
 * <p/>
 * 作者: 李苜菲
 * 日期: 2015/11/11 11:11.
 */
public interface ReplyListener {
    public void onResult(String name, int selectPosition, View view, ShowBO showBO, int replyPosition);

}
