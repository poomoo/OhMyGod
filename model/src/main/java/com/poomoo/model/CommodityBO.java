/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

import java.util.List;

/**
 * 活动详情的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/11/30 17:22.
 */
public class CommodityBO {
    private List<String> picList;//--图片集合
    private String goodsName;//--商品名称
    private String price;//--价格
    private String startDt;//	--活动开始时间
    private String endDt;//--活动结束时间
    private String content;//--商品详情介绍内容（html富文本）
    private String statement;//--活动声明（html富文本）
    private String playFlag;//--当前用户是否参与过此活动，0未参加，1参加
    //    private long countdown;//--表示剩余时间
    private long startCountdown;//--开始时间倒计时
    private long endCountdown;//--结束时间倒计时

    public List<String> getPicList() {
        return picList;
    }

    public void setPicList(List<String> picList) {
        this.picList = picList;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getPlayFlag() {
        return playFlag;
    }

    public void setPlayFlag(String playFlag) {
        this.playFlag = playFlag;
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

    @Override
    public String toString() {
        return "CommodityBO{" +
                "picList=" + picList +
                ", goodsName='" + goodsName + '\'' +
                ", price='" + price + '\'' +
                ", endDt='" + endDt + '\'' +
                ", content='" + content + '\'' +
                ", statement='" + statement + '\'' +
                ", playFlag='" + playFlag + '\'' +
                ", startCountdown=" + startCountdown +
                ", endCountdown=" + endCountdown +
                '}';
    }
}
