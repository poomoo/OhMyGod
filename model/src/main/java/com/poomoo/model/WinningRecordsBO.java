/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

import java.io.Serializable;

/**
 * 中奖记录的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/11/23 13:51.
 */
public class WinningRecordsBO implements Serializable {
    private String picture;
    private String goodsId;
    private String title;
    private String status;//--活动状态：0未开启，1开启（正在进行），2结束
    private int activeId;
    private String userId;
    private String getEndDt;
    private String playDt;
    private int isGot;//	--是否领取（0未，1已经领取）
    private int isShare;//	--是否已经晒单分享（0未，1已经分享）
    private String goodsName;//--奖品名称
    private String getAddress;//--领取地址
    private String getRequire;//--领取要求

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getActiveId() {
        return activeId;
    }

    public void setActiveId(int activeId) {
        this.activeId = activeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGetEndDt() {
        return getEndDt;
    }

    public void setGetEndDt(String getEndDt) {
        this.getEndDt = getEndDt;
    }

    public String getPlayDt() {
        return playDt;
    }

    public void setPlayDt(String playDt) {
        this.playDt = playDt;
    }

    public int getIsGot() {
        return isGot;
    }

    public void setIsGot(int isGot) {
        this.isGot = isGot;
    }

    public int getIsShare() {
        return isShare;
    }

    public void setIsShare(int isShare) {
        this.isShare = isShare;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGetAddress() {
        return getAddress;
    }

    public void setGetAddress(String getAddress) {
        this.getAddress = getAddress;
    }

    public String getGetRequire() {
        return getRequire;
    }

    public void setGetRequire(String getRequire) {
        this.getRequire = getRequire;
    }

    @Override
    public String toString() {
        return "WinningRecordsBO{" +
                "picture='" + picture + '\'' +
                ", goodsId='" + goodsId + '\'' +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", activeId=" + activeId +
                ", userId='" + userId + '\'' +
                ", getEndDt='" + getEndDt + '\'' +
                ", playDt='" + playDt + '\'' +
                ", isGot=" + isGot +
                ", isShare=" + isShare +
                ", goodsName='" + goodsName + '\'' +
                ", getAddress='" + getAddress + '\'' +
                ", getRequire='" + getRequire + '\'' +
                '}';
    }
}
