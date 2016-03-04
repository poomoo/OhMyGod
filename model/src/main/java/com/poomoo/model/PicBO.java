/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

/**
 * 启动页和引导页图片
 * 作者: 李苜菲
 * 日期: 2016/3/3 16:27.
 */
public class PicBO {
    private String picture = "";
    private int guideId;
    private int type;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getGuideId() {
        return guideId;
    }

    public void setGuideId(int guideId) {
        this.guideId = guideId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PicBO{" +
                "picture='" + picture + '\'' +
                ", guideId=" + guideId +
                ", type=" + type +
                '}';
    }
}
