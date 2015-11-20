/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

/**
 * 回复评论业务类模型
 * 作者: 李苜菲
 * 日期: 2015/11/20 11:51.
 */
public class ReplyBO {
    private String floor_user_name = "";//回复人名称
    private String floor_user_id = "";
    private String revert_user_name = "";//被回复人的名称
    private String revert_user_id = "";
    private String content = "";//回复的内容

    public String getFloor_user_name() {
        return floor_user_name;
    }

    public void setFloor_user_name(String floor_user_name) {
        this.floor_user_name = floor_user_name;
    }

    public String getFloor_user_id() {
        return floor_user_id;
    }

    public void setFloor_user_id(String floor_user_id) {
        this.floor_user_id = floor_user_id;
    }

    public String getRevert_user_name() {
        return revert_user_name;
    }

    public void setRevert_user_name(String revert_user_name) {
        this.revert_user_name = revert_user_name;
    }

    public String getRevert_user_id() {
        return revert_user_id;
    }

    public void setRevert_user_id(String revert_user_id) {
        this.revert_user_id = revert_user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
