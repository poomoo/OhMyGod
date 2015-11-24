package com.poomoo.model; /**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */

/**
 * 站内信息的业务类模型
 * 作者: 李苜菲
 * 日期: 2015/11/24 11:08.
 */
public class InStationMessagesBO {
    private String url;//图片地址
    private String title;//消息头
    private String dateTime;//日期时间
    private String content;//消息内容

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
