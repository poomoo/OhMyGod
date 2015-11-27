package com.poomoo.api;

import com.poomoo.model.AdBO;
import com.poomoo.model.ResponseBO;

import java.util.List;

/**
 * Api接口
 * <p/>
 * 作者: 李苜菲
 * 日期: 2015/11/11 11:06.
 */
public interface Api {

    ResponseBO<Void> login(String phoneNum, String passWord);

    ResponseBO<Void> getCode(String phoneNum);

    ResponseBO<Void> register(String phoneNum, String passWord, String code, String age, String sex, String channelId);

    ResponseBO<List<AdBO>> getAdvertisement();
}
