package com.poomoo.core;

import com.poomoo.model.AdBO;
import com.poomoo.model.ResponseBO;

import java.util.List;

/**
 * 接收app层的各种Action
 * 作者: 李苜菲
 * 日期: 2015/11/11 11:13.
 */
public interface AppAction {
    /**
     * 登陆
     *
     * @param phoneNum 手机号
     * @param passWord 密码
     * @param listener 回调监听器
     */
    void logIn(String phoneNum, String passWord, ActionCallbackListener listener);

    /**
     * 获取验证码
     *
     * @param phoneNum
     * @param listener
     */
    void getCode(String phoneNum, ActionCallbackListener listener);

    /**
     * 注册
     *
     * @param phoneNum
     * @param passWord
     * @param code      --验证码(参数非空)
     * @param age       --年龄（可选）
     * @param sex       --性别（可选；1：男，2：女）
     * @param channelId --设备编号
     */
    void register(String phoneNum, String passWord, String code, String age, String sex, String channelId, ActionCallbackListener listener);

    /**
     * 获取广告
     *
     * @param listener
     */
    void getAdvertisement(ActionCallbackListener listener);

    /**
     * 获取活动列表
     *
     * @param cityName
     * @param listener
     */
    void getGrabList(String cityName, ActionCallbackListener listener);

    /**
     * 活动详情
     *
     * @param userId
     * @param activeId
     * @param listener
     */
    void getCommodityInformation(String userId, String activeId, ActionCallbackListener listener);
}
