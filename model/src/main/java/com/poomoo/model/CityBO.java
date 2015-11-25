/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

/**
 * 城市的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/11/25 15:55.
 */
public class CityBO {
    public String name;
    public String pinyi;

    public CityBO(String name, String pinyi) {
        super();
        this.name = name;
        this.pinyi = pinyi;
    }

    public CityBO() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyi() {
        return pinyi;
    }

    public void setPinyi(String pinyi) {
        this.pinyi = pinyi;
    }

}
