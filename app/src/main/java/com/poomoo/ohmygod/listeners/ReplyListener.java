package com.poomoo.ohmygod.listeners;

import android.view.View;

import com.poomoo.model.ShowBO;

/**
 * 评论监听回调
 * <p/>
 * 作者: 李苜菲
 * 日期: 2015/11/11 11:11.
 */
public interface ReplyListener {
    void onResult(String name, View view, ShowBO showBO, int itemPosition, int commentPosition);

}
