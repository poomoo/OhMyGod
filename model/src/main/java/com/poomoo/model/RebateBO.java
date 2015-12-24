/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

/**
 * 返现的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/12/24 11:29.
 */
public class RebateBO {
    private String gotDt;
    private String statusName;
    private String gotFee;

    public String getGotDt() {
        return gotDt;
    }

    public void setGotDt(String gotDt) {
        this.gotDt = gotDt;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getGotFee() {
        return gotFee;
    }

    public void setGotFee(String gotFee) {
        this.gotFee = gotFee;
    }
}
