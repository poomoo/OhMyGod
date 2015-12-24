/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

/**
 * 活动中奖者的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/12/24 17:07.
 */
public class WinnerListBO {
    private String headPic;
    private String playDt;
    private String nickName;
    private String tel;
    private String goodsName;
    private String winMsg;

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getPlayDt() {
        return playDt;
    }

    public void setPlayDt(String playDt) {
        this.playDt = playDt;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getWinMsg() {
        return winMsg;
    }

    public void setWinMsg(String winMsg) {
        this.winMsg = winMsg;
    }
}
