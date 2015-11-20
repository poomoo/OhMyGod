/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

/**
 * 晒单的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/11/20 10:43.
 */
public class ShowBO {
    private String avatar;//头像
    private String userName;//用户名
    private String dateTime;//日期时间
    private String content;//内容
    private String title;//标题
    private String[] pics;//图片地址
    private ReplyBO replyBO;//评论


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getPics() {
        return pics;
    }

    public void setPics(String[] pics) {
        this.pics = pics;
    }

    public ReplyBO getReplyBO() {
        return replyBO;
    }

    public void setReplyBO(ReplyBO replyBO) {
        this.replyBO = replyBO;
    }
}
