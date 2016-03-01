/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

/**
 * 验证码
 * 作者: 李苜菲
 * 日期: 2016/3/1 17:04.
 */
public class CodeBO {
    private String play_dt = "";//中奖时间
    private String nickName = "";//昵称
    private int activeId;//活动编号，扩展字段，保留
    private int userId;//用户编号，保留便于扩展
    private String activeName = "";//活动名称
    private String realName = "";//活动名称
    private String headPic = "";//头像，无则取默认值
    private int isGot;//0:未领取，1已经消费领取
    private String winNumber = "";//中奖号码

    public String getPlay_dt() {
        return play_dt;
    }

    public void setPlay_dt(String play_dt) {
        this.play_dt = play_dt;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getActiveId() {
        return activeId;
    }

    public void setActiveId(int activeId) {
        this.activeId = activeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getActiveName() {
        return activeName;
    }

    public void setActiveName(String activeName) {
        this.activeName = activeName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public int getIsGot() {
        return isGot;
    }

    public void setIsGot(int isGot) {
        this.isGot = isGot;
    }

    public String getWinNumber() {
        return winNumber;
    }

    public void setWinNumber(String winNumber) {
        this.winNumber = winNumber;
    }

    @Override
    public String toString() {
        return "CodeBO{" +
                "play_dt='" + play_dt + '\'' +
                ", nickName='" + nickName + '\'' +
                ", activeId=" + activeId +
                ", userId=" + userId +
                ", activeName='" + activeName + '\'' +
                ", realName='" + realName + '\'' +
                ", headPic='" + headPic + '\'' +
                ", isGot=" + isGot +
                ", winNumber='" + winNumber + '\'' +
                '}';
    }
}
