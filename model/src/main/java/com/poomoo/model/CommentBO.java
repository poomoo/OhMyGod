/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

import java.util.List;

/**
 * 评论的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/12/4 09:51.
 */
public class CommentBO {
    private String commentDt;//--评论时间
    private String commentId;//--评论编号
    private String content;//--评论内容
    private String dynamicId;//--动态编号
    private String nickName;//--评论人昵称
    private List<ReplyBO> replies;//--回复集合

    public String getCommentDt() {
        return commentDt;
    }

    public void setCommentDt(String commentDt) {
        this.commentDt = commentDt;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDynamicId() {
        return dynamicId;
    }

    public void setDynamicId(String dynamicId) {
        this.dynamicId = dynamicId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<ReplyBO> getReplies() {
        return replies;
    }

    public void setReplies(List<ReplyBO> replies) {
        this.replies = replies;
    }
}
