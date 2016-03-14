/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

import java.util.List;

/**
 * 活动中奖者的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/12/24 17:07.
 */
public class ActiveWinInfoBO {
    private int winNumber;//当前中奖的人数
    private String picture;//商品图片
    private int goodsId;//商品编号（扩展字段，点击可进入详情）
    private int prizeNumber;//奖品数量
    private int activeId;//活动编号
    private String goodsName;//商品名称（显示在商品图片下边）
    protected List<WinnerListBO> winList;

    public int getWinNumber() {
        return winNumber;
    }

    public void setWinNumber(int winNumber) {
        this.winNumber = winNumber;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getPrizeNumber() {
        return prizeNumber;
    }

    public void setPrizeNumber(int prizeNumber) {
        this.prizeNumber = prizeNumber;
    }

    public int getActiveId() {
        return activeId;
    }

    public void setActiveId(int activeId) {
        this.activeId = activeId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public List<WinnerListBO> getWinList() {
        return winList;
    }

    public void setWinList(List<WinnerListBO> winList) {
        this.winList = winList;
    }
}
