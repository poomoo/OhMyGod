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
    private int status;//--活动状态：0未开启，1开启（正在进行），2结束
    private String startDt;//活动开始时间
    private String endDt;//活动结束时间
    private int totalWinNum;//总的奖品名额
    private int currWinNum;//已经中奖的数量


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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStartDt() {
        return startDt;
    }

    public void setStartDt(String startDt) {
        this.startDt = startDt;
    }

    public String getEndDt() {
        return endDt;
    }

    public void setEndDt(String endDt) {
        this.endDt = endDt;
    }


    public int getTotalWinNum() {
        return totalWinNum;
    }

    public void setTotalWinNum(int totalWinNum) {
        this.totalWinNum = totalWinNum;
    }

    public int getCurrWinNum() {
        return currWinNum;
    }

    public void setCurrWinNum(int currWinNum) {
        this.currWinNum = currWinNum;
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
                ", status=" + status +
                ", startDt='" + startDt + '\'' +
                ", endDt='" + endDt + '\'' +
                ", totalWinNum=" + totalWinNum +
                ", currWinNum=" + currWinNum +
                '}';
    }
}
