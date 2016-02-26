package com.poomoo.ohmygod.listeners;

import android.widget.ImageView;

/**
 * 闹钟监听回调
 * <p/>
 * 作者: 李苜菲
 * 日期: 2015/11/11 11:11.
 */
public interface AlarmtListener {
    void setAlarm(String title, int activeId, String startDt, String endDt, boolean flag);
}
