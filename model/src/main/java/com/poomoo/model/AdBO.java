/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

/**
 * 广告的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/11/27 16:22.
 */
public class AdBO {
    private String picture;
    private String url;
    private String title;
    private int activeId;
    private int advId;//广告编号

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

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

    public int getActiveId() {
        return activeId;
    }

    public void setActiveId(int activeId) {
        this.activeId = activeId;
    }

    public int getAdvId() {
        return advId;
    }

    public void setAdvId(int advId) {
        this.advId = advId;
    }

    @Override
    public String toString() {
        return "AdBO{" +
                "picture='" + picture + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", activeId=" + activeId +
                ", advId=" + advId +
                '}';
    }
}
