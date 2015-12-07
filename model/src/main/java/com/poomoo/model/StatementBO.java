/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

/**
 * 声明信息的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/12/3 11:44.
 */
public class StatementBO {
    private String content;//--返回内容
    private String title;//--列表标题
    private String statementId;//--编号

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }


}
