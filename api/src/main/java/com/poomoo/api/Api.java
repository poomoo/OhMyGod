package com.poomoo.api;

import com.poomoo.model.FileBO;
import com.poomoo.model.ResponseBO;

/**
 * Api接口
 * <p/>
 * 作者: 李苜菲
 * 日期: 2015/11/11 11:06.
 */
public interface Api {

    ResponseBO login(String phoneNum, String passWord);

    ResponseBO getCode(String phoneNum);

    ResponseBO register(String phoneNum, String passWord, String code, String age, String sex, String channelId);

    ResponseBO getAdvertisement();

    ResponseBO getGrabList(String cityName);

    ResponseBO getCommodityInformation(String userId, String activeId);

    ResponseBO putGrabInfo(String activeId, String userId);

    ResponseBO getWinnerList(String cityName);

    ResponseBO uploadPics(FileBO fileBO);

    ResponseBO putPersonalInfo(String userId, String realName, String idCardNum, String bankCardNum, String idFrontPic, String idOpsitePic);

    ResponseBO changePersonalInfo(String userId, String key, String value);
}
