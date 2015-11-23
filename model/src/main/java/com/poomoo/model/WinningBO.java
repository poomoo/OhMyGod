/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

/**
 * 中奖记录的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/11/23 13:51.
 */
public class WinningBO {
    private String dateTime;//获奖时间
    private String endDateTime;//截止时间

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }
}
