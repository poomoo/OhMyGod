package com.poomoo.ohmygod.database;

import org.litepal.crud.DataSupport;

/**
 * @author 李苜菲
 * @ClassName AreaInfo
 * @Description TODO 消息模型
 * @date 2015年8月16日 下午10:45:03
 */
public class HistoryCityInfo extends DataSupport {
    private String cityName;//--城市名称
    private String pinyin;//--拼音

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

    @Override
    public String toString() {
        return "HistoryCityInfo{" +
                "cityName='" + cityName + '\'' +
                ", pinyin='" + pinyin + '\'' +
                '}';
    }
}
