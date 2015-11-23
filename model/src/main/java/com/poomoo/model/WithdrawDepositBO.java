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
    private String dateTime;//提现日期
    private String status;//提现状态
    private String account;//提现金额

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
