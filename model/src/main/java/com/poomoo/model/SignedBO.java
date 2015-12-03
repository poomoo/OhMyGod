/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

import java.util.Date;
import java.util.List;

/**
 * 签到的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/12/3 09:27.
 */
public class SignedBO {
    private String totalCashFee;//--总的资金池金额
    private String countUsed;//--总的已返金额
    private String countUse;//--总的未反金额
    private List<String> mySignRecords;//--签到时间（天）记录集合

    public String getTotalCashFee() {
        return totalCashFee;
    }

    public void setTotalCashFee(String totalCashFee) {
        this.totalCashFee = totalCashFee;
    }

    public String getCountUsed() {
        return countUsed;
    }

    public void setCountUsed(String countUsed) {
        this.countUsed = countUsed;
    }

    public String getCountUse() {
        return countUse;
    }

    public void setCountUse(String countUse) {
        this.countUse = countUse;
    }

    public List<String> getMySignRecords() {
        return mySignRecords;
    }

    public void setMySignRecords(List<String> mySignRecords) {
        this.mySignRecords = mySignRecords;
    }

}
