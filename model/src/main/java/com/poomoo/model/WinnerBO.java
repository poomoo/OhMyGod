package com.poomoo.model;

import java.io.Serializable;

/**
 * 中奖者的业务模型类
 * Created by 李苜菲 on 2015/12/1.
 */
public class WinnerBO implements Serializable {
    private String headPic;//--头部图像
    private String title;//--活动标题
    private String winTel;//--中奖人号码（无昵称时显示部分电话号码）
    private String winNickName;//--中奖人昵称
    private String userId;//--用户编号
    private String playDt;//--中奖时间
    private String goodsName;//--奖品名称


    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWinTel() {
        return winTel;
    }

    public void setWinTel(String winTel) {
        this.winTel = winTel;
    }

    public String getWinNickName() {
        return winNickName;
    }

    public void setWinNickName(String winNickName) {
        this.winNickName = winNickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlayDt() {
        return playDt;
    }

    public void setPlayDt(String playDt) {
        this.playDt = playDt;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
