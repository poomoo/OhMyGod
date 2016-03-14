/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动详情的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/11/30 17:22.
 */
public class CommodityBO {
    private ArrayList<String> picList;//--图片集合
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
    private int typeId;
    private int status;//--活动状态：0未开启，1开启（正在进行），2结束
    private String smallPic = "";//
    private String getRequire = "";//
    private String getEndDt = "";//
    private String getAddress = "";//

    private String shopsName="";//店铺名称
    private String shopsUserId="";//商家编号
    private String shopsCityName="";//商家城市名称
    private String shopsAddress="";//商家地址
    private String shopsLat="";//商家地址纬度
    private String shopsLng="";//商家地址经度
    private String shopsRealName="";//商家姓名
    private String shopsTel="";//商家电话号码

    public ArrayList<String> getPicList() {
        return picList;
    }

    public void setPicList(ArrayList<String> picList) {
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

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public String getGetRequire() {
        return getRequire;
    }

    public void setGetRequire(String getRequire) {
        this.getRequire = getRequire;
    }

    public String getGetEndDt() {
        return getEndDt;
    }

    public void setGetEndDt(String getEndDt) {
        this.getEndDt = getEndDt;
    }

    public String getGetAddress() {
        return getAddress;
    }

    public void setGetAddress(String getAddress) {
        this.getAddress = getAddress;
    }

    public String getShopsName() {
        return shopsName;
    }

    public void setShopsName(String shopsName) {
        this.shopsName = shopsName;
    }

    public String getShopsUserId() {
        return shopsUserId;
    }

    public void setShopsUserId(String shopsUserId) {
        this.shopsUserId = shopsUserId;
    }

    public String getShopsCityName() {
        return shopsCityName;
    }

    public void setShopsCityName(String shopsCityName) {
        this.shopsCityName = shopsCityName;
    }

    public String getShopsAddress() {
        return shopsAddress;
    }

    public void setShopsAddress(String shopsAddress) {
        this.shopsAddress = shopsAddress;
    }

    public String getShopsLat() {
        return shopsLat;
    }

    public void setShopsLat(String shopsLat) {
        this.shopsLat = shopsLat;
    }

    public String getShopsLng() {
        return shopsLng;
    }

    public void setShopsLng(String shopsLng) {
        this.shopsLng = shopsLng;
    }

    public String getShopsRealName() {
        return shopsRealName;
    }

    public void setShopsRealName(String shopsRealName) {
        this.shopsRealName = shopsRealName;
    }

    public String getShopsTel() {
        return shopsTel;
    }

    public void setShopsTel(String shopsTel) {
        this.shopsTel = shopsTel;
    }

    @Override
    public String toString() {
        return "CommodityBO{" +
                "picList=" + picList +
                ", goodsName='" + goodsName + '\'' +
                ", price='" + price + '\'' +
                ", startDt='" + startDt + '\'' +
                ", endDt='" + endDt + '\'' +
                ", content='" + content + '\'' +
                ", statement='" + statement + '\'' +
                ", playFlag='" + playFlag + '\'' +
                ", startCountdown=" + startCountdown +
                ", endCountdown=" + endCountdown +
                ", typeId=" + typeId +
                ", status=" + status +
                ", smallPic='" + smallPic + '\'' +
                ", getRequire='" + getRequire + '\'' +
                ", getEndDt='" + getEndDt + '\'' +
                ", getAddress='" + getAddress + '\'' +
                ", shopsName='" + shopsName + '\'' +
                ", shopsUserId='" + shopsUserId + '\'' +
                ", shopsCityName='" + shopsCityName + '\'' +
                ", shopsAddress='" + shopsAddress + '\'' +
                ", shopsLat='" + shopsLat + '\'' +
                ", shopsLng='" + shopsLng + '\'' +
                ", shopsRealName='" + shopsRealName + '\'' +
                ", shopsTel='" + shopsTel + '\'' +
                '}';
    }
}
