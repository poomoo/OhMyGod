package com.poomoo.ohmygod.database;

import java.util.ArrayList;
import java.util.List;

import org.litepal.crud.DataSupport;

/**
 * 
 * @ClassName CityInfo
 * @Description TODO 城市模型
 * @author 李苜菲
 * @date 2015年8月16日 下午10:50:54
 */
public class CityInfo extends DataSupport{
	private String city_id = "";
	private String city_name = "";
	private ProvinceInfo provinceInfo;
	private List<AreaInfo> areaInfoList = new ArrayList<AreaInfo>();

	public String getCity_id() {
		return this.city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getCity_name() {
		return this.city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public ProvinceInfo getProvinceInfo() {
		return this.provinceInfo;
	}

	public void setProvinceInfo(ProvinceInfo provinceInfo) {
		this.provinceInfo = provinceInfo;
	}

	public List<AreaInfo> getAreaInfoList() {
		return this.areaInfoList;
	}

	public void setAreaInfoList(List<AreaInfo> areaInfoList) {
		this.areaInfoList = areaInfoList;
	}

	@Override
	public String toString() {
		return "CityInfo{" +
				"city_id='" + city_id + '\'' +
				", city_name='" + city_name + '\'' +
				", provinceInfo=" + provinceInfo +
				", areaInfoList=" + areaInfoList +
				'}';
	}
}
