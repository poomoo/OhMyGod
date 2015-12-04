/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

import java.util.List;

/**
 * 晒单的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/11/20 10:43.
 */
public class ShowBO {
    private String activeId;//--活动编号
    private List<CommentBO> comments;//--评论集合
    private String content;//--动态内容
    private String dynamicDt;//--动态时间
    private String dynamicId;//--动态编号
    private String headPic;//--分享人头部图片
    private String nickName;//--分享人昵称
    private List<String> picList;//--分享的图片集合
    private String title;//--活动标题
    private String userId;//--分享人编号

    public String getActiveId() {
        return activeId;
    }

    public void setActiveId(String activeId) {
        this.activeId = activeId;
    }

    public List<CommentBO> getComments() {
        return comments;
    }

    public void setComments(List<CommentBO> comments) {
        this.comments = comments;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDynamicDt() {
        return dynamicDt;
    }

    public void setDynamicDt(String dynamicDt) {
        this.dynamicDt = dynamicDt;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "ShowBO{" +
                "activeId='" + activeId + '\'' +
                ", comments=" + comments +
                ", content='" + content + '\'' +
                ", dynamicDt='" + dynamicDt + '\'' +
                ", dynamicId='" + dynamicId + '\'' +
                ", headPic='" + headPic + '\'' +
                ", nickName='" + nickName + '\'' +
                ", picList=" + picList +
                ", title='" + title + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
