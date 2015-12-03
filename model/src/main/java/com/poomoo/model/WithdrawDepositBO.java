/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

/**
 * 提现的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/11/23 17:15.
 */
public class WithdrawDepositBO {
    private String drawDt;//提现日期
    private String status;//提现状态
    private String drawFee;//提现金额

    public String getDrawDt() {
        return drawDt;
    }

    public void setDrawDt(String drawDt) {
        this.drawDt = drawDt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDrawFee() {
        return drawFee;
    }

    public void setDrawFee(String drawFee) {
        this.drawFee = drawFee;
    }
}
