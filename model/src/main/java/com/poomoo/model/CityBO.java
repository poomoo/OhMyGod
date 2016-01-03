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
    private String cityName;//--城市名称
    private String pinyin;//--拼音
    private String isHot;//--是否为热门城市，0否，1是

    public CityBO(String cityName, String pinyin) {
        super();
        this.cityName = cityName;
        this.pinyin = pinyin;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getIsHot() {
        return isHot;
    }

    public void setIsHot(String isHot) {
        this.isHot = isHot;
    }

    @Override
    public String toString() {
        return "CityBO{" +
                "cityName='" + cityName + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", isHot='" + isHot + '\'' +
                '}';
    }
}
