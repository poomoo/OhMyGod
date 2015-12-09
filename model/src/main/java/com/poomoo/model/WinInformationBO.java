package com.poomoo.model;

/**
 * 中奖信息的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/11/13 11:20.
 */
public class WinInformationBO {
    private String transferMsg;//--转让信息，为空表示不转让，不为空表示已经转让给某人，列表中显示起信息
    private String picture;//
    private String goodsId;//
    private String title;//
    private String activeId;//
    private String winTel;//--中奖人昵称
    private String winNickName;//--中奖人昵称
    private String userId; //
    private String playDt;//
    private String goodsName;//
    private String getEndDt;
    private String getAddress;

    public String getTransferMsg() {
        return transferMsg;
    }

    public void setTransferMsg(String transferMsg) {
        this.transferMsg = transferMsg;
    }

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

    public String getActiveId() {
        return activeId;
    }

    public void setActiveId(String activeId) {
        this.activeId = activeId;
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
}
