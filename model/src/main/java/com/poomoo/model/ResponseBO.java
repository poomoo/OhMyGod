/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 返回参数的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/11/27 09:28.
 */
public class ResponseBO<T> {
    private String jsonData = "";    // "jsonData":"请求的结果集，主要正对查询功能"
    private String otherData = "";    // "otherData":"扩展结果集"
    private String msg = "";    // "msg":"请求成功",
    private int rsCode = 0;    // "rsCode":"1" 1:成功，-1：失败，-2：必要参数为空
    private T obj;
    private List<T> objList = new ArrayList<>();
    private int totalCount;

    public ResponseBO(int rsCode, String msg) {
        this.msg = msg;
        this.rsCode = rsCode;
    }

    public boolean isSuccess() {
        return rsCode == 1;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getOtherData() {
        return otherData;
    }

    public void setOtherData(String otherData) {
        this.otherData = otherData;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getRsCode() {
        return rsCode;
    }

    public void setRsCode(int rsCode) {
        this.rsCode = rsCode;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public List<T> getObjList() {
        return objList;
    }

    public void setObjList(List<T> objList) {
        this.objList = objList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "ResponseBO{" +
                "jsonData='" + jsonData + '\'' +
                ", otherData='" + otherData + '\'' +
                ", msg='" + msg + '\'' +
                ", rsCode=" + rsCode +
                ", obj=" + obj +
                ", objList=" + objList +
                ", totalCount=" + totalCount +
                '}';
    }
}
