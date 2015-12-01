package com.poomoo.api;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.poomoo.model.AdBO;
import com.poomoo.model.CommodityBO;
import com.poomoo.model.GrabBO;
import com.poomoo.model.GrabResultBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.UserBO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Api实现类
 * 作者: 李苜菲
 * 日期: 2015/11/11 11:06.
 */
public class ApiImpl implements Api {

    private HttpEngine httpEngine;

    public ApiImpl() {
        httpEngine = HttpEngine.getInstance();
    }

    @Override
    public ResponseBO login(String phoneNum, String passWord) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.LOGIN);
        paramMap.put("tel", phoneNum);
        paramMap.put("password", passWord);

//        Type type = new TypeToken<ResponseBO<Void>>() {
//        }.getType();
        try {
            return httpEngine.postHandle(paramMap, UserBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO getCode(String phoneNum) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.CODE);
        paramMap.put("tel", phoneNum);


        try {
            return httpEngine.postHandle(paramMap, null);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO register(String phoneNum, String passWord, String code, String age, String sex, String channelId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.REGISTER);
        paramMap.put("tel", phoneNum);
        paramMap.put("password", passWord);
        paramMap.put("code", code);
        paramMap.put("age", age);
        paramMap.put("sex", sex);
        paramMap.put("channelId", channelId);
        paramMap.put("deviceType", "1");//--设备类型，1android，2ios，3PC(参数非空)

        try {
            return httpEngine.postHandle(paramMap, null);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<AdBO> getAdvertisement() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.PUBACTION);
        paramMap.put("method", Config.AD);

        try {
            return httpEngine.postHandle(paramMap, AdBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<GrabBO> getGrabList(String cityName) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.ACTIVITYACTION);
        paramMap.put("method", Config.ACTIVITYLIST);
        paramMap.put("cityName", cityName);

        try {
            return httpEngine.postHandle(paramMap, GrabBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<CommodityBO> getCommodityInformation(String userId, String activeId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.ACTIVITYACTION);
        paramMap.put("method", Config.ACTIVITY);
        paramMap.put("userId", userId);
        paramMap.put("activeId", activeId);

        try {
            return httpEngine.postHandle(paramMap, CommodityBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<GrabResultBO> putGrabInfo(String activeId, String userId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.ACTIVITYACTION);
        paramMap.put("method", Config.SUBMIT);
        paramMap.put("activeId", activeId);
        paramMap.put("userId", userId);

        try {
            return httpEngine.postHandle(paramMap, GrabResultBO.class);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }
}
