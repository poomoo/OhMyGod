/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

/**
 * 回复的业务类模型
 * 作者: 李苜菲
 * 日期: 2015/11/20 11:51.
 */
public class ReplyBO {
    private String commentId;
    private String content;//--回复内容
    private String fromNickName;//--回复人昵称
    private String fromUserId;//--回复人编号
    private String replyDt;//--回复时间
    private String replyId;// --回复编号
    private String toNickName;//--被回复人昵称
    private String toUserId;//--被回复人编号

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

    public String getFromNickName() {
        return fromNickName;
    }

    public void setFromNickName(String fromNickName) {
        this.fromNickName = fromNickName;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getReplyDt() {
        return replyDt;
    }

    public void setReplyDt(String replyDt) {
        this.replyDt = replyDt;
    }

    public String getReplyId() {
        return replyId;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getToNickName() {
        return toNickName;
    }

    public void setToNickName(String toNickName) {
        this.toNickName = toNickName;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    @Override
    public String toString() {
        return "ReplyBO{" +
                "commentId='" + commentId + '\'' +
                ", content='" + content + '\'' +
                ", fromNickName='" + fromNickName + '\'' +
                ", fromUserId='" + fromUserId + '\'' +
                ", replyDt='" + replyDt + '\'' +
                ", replyId='" + replyId + '\'' +
                ", toNickName='" + toNickName + '\'' +
                ", toUserId='" + toUserId + '\'' +
                '}';
    }
}
