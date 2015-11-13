package com.poomoo.model;

/**
 * 中奖信息的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/11/13 11:20.
 */
public class WinInformationBO {
    public String title = "";//标题
    public String winner = "";//中奖者
    public String win_date = "";//获奖日期
    public String address = "";//领奖地址
    public String end_date = "";//领奖截止日期
    public String reason = "";//其他原因
    public String picture_urls = "";//图片URL

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getWin_date() {
        return win_date;
    }

    public void setWin_date(String win_date) {
        this.win_date = win_date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPicture_urls() {
        return picture_urls;
    }

    public void setPicture_urls(String picture_urls) {
        this.picture_urls = picture_urls;
    }
}
