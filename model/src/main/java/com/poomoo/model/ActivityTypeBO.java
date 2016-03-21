/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

/**
 * 活动类型的业务模型类
 * 作者: 李苜菲
 * 日期: 2016/3/21 16:43.
 */
public class ActivityTypeBO {
    int cateId;//--类别编号（1：商品区，2服务区）
    String cateName = "";//--类别名称，可不使用
    String picture = "";//--显示的图片

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
