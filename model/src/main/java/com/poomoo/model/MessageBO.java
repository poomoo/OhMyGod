/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

import java.io.Serializable;

/**
 * 作者: 李苜菲
 * 日期: 2015/12/7 13:51.
 */
public class MessageBO implements Serializable {
    private int statementId;//--编号
    private String title;//--列表标题
    private String insertDt;
    private int isTop;


    public int getStatementId() {
        return statementId;
    }

    public void setStatementId(int statementId) {
        this.statementId = statementId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public String getInsertDt() {
        return insertDt;
    }

    public void setInsertDt(String insertDt) {
        this.insertDt = insertDt;
    }

    @Override
    public String toString() {
        return "MessageBO{" +
                "statementId=" + statementId +
                ", title='" + title + '\'' +
                ", insertDt='" + insertDt + '\'' +
                ", isTop=" + isTop +
                '}';
    }
}
