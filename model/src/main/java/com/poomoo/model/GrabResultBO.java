/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

/**
 * 抢单结果的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/12/1 15:31.
 */
public class GrabResultBO {
    private String isWin;//"true/false"
    private String winFee;

    public String getIsWin() {
        return isWin;
    }

    public void setIsWin(String isWin) {
        this.isWin = isWin;
    }

    public String getWinFee() {
        return winFee;
    }

    public void setWinFee(String winFee) {
        this.winFee = winFee;
    }
}
