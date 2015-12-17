package com.poomoo.ohmygod.database;

import org.litepal.crud.DataSupport;

/**
 * 
 * @ClassName AreaInfo
 * @Description TODO 区域模型
 * @author 李苜菲
 * @date 2015年8月16日 下午10:45:03
 */
public class AreaInfo extends DataSupport{
	private String area_id = "";
	private String area_name = "";
	private CityInfo cityInfo;

	public String getArea_id() {
		return this.area_id;
	}

	public void setArea_id(String area_id) {
		this.area_id = area_id;
	}

	public String getArea_name() {
		return this.area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public CityInfo getCityInfo() {
		return this.cityInfo;
	}

	public void setCityInfo(CityInfo cityInfo) {
		this.cityInfo = cityInfo;
	}

	@Override
	public String toString() {
		return "AreaInfo{" +
				"area_id='" + area_id + '\'' +
				", area_name='" + area_name + '\'' +
				", cityInfo=" + cityInfo +
				'}';
	}
}
