/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

/**
 * 倒计时活动的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/11/30 14:27.
 */
public class GrabBO {
    private int activeId;//--活动编号
    private String title;//--活动标题
    private String goodsName;//--商品名称
    //    private long countdown;//--表示剩余时间
    private String picture;//--活动图片
    private long startCountdown;//--开始时间倒计时
    private long endCountdown;//--结束时间倒计时
    private int typeId;//活动分类 1-房子 2-车子 3-装修 4-其他


    public int getActiveId() {
        return activeId;
    }

    public void setActiveId(int activeId) {
        this.activeId = activeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }


    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public long getStartCountdown() {
        return startCountdown;
    }

    public void setStartCountdown(long startCountdown) {
        this.startCountdown = startCountdown;
    }

    public long getEndCountdown() {
        return endCountdown;
    }

    public void setEndCountdown(long endCountdown) {
        this.endCountdown = endCountdown;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @Override
    public String toString() {
        return "GrabBO{" +
                "activeId=" + activeId +
                ", title='" + title + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", picture='" + picture + '\'' +
                ", startCountdown=" + startCountdown +
                ", endCountdown=" + endCountdown +
                ", typeId=" + typeId +
                '}';
    }
}
