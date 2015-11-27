package com.poomoo.api;

import com.google.gson.reflect.TypeToken;
import com.poomoo.model.AdBO;
import com.poomoo.model.ResponseBO;

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
    public ResponseBO<Void> login(String phoneNum, String passWord) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.LOGIN);
        paramMap.put("tel", phoneNum);
        paramMap.put("password", passWord);

        Type type = new TypeToken<ResponseBO<Void>>() {
        }.getType();
        try {
            return httpEngine.postHandle(paramMap, type);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<Void> getCode(String phoneNum) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.USERACTION);
        paramMap.put("method", Config.CODE);
        paramMap.put("tel", phoneNum);

        Type type = new TypeToken<ResponseBO<Void>>() {
        }.getType();
        try {
            return httpEngine.postHandle(paramMap, type);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<Void> register(String phoneNum, String passWord, String code, String age, String sex, String channelId) {
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

        Type type = new TypeToken<ResponseBO<Void>>() {
        }.getType();
        try {
            return httpEngine.postHandle(paramMap, type);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }

    @Override
    public ResponseBO<List<AdBO>> getAdvertisement() {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("bizName", Config.PUBACTION);
        paramMap.put("method", Config.AD);

        Type type = new TypeToken<ResponseBO<Void>>() {
        }.getType();
        try {
            return httpEngine.postHandle(paramMap, type);
        } catch (IOException e) {
            return new ResponseBO(Config.TIME_OUT_EVENT, Config.TIME_OUT_EVENT_MSG);
        }
    }
}
