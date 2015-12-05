/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.api;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/27 11:22.
 */
public class Config {
    public final static int TIME_OUT_EVENT = -3;//超时
    public final static String TIME_OUT_EVENT_MSG = "连接服务器失败";

    //ACTION 10000
    public final static String USERACTION = "10000";//用户相关接口统一业务编号
    public final static String CODE = "10002";//获取验证码
    public final static String REGISTER = "10004";//注册
    public final static String LOGIN = "10005";//登陆
    public final static String CHANGE = "10006";//修改用户信息接口（单独修改用户某个字段信息，如昵称）
    public final static String PERSONALINFO = "10007";//编辑个人资料页面接口（实名认证接口）
    public final static String SIGNEDLIST = "10010";//签到列表
    public final static String SIGNED = "10011";//保存签到接口
    public final static String WITHDRAWDEPOSIT = "10012";//保存签到接口

    //ACTION 20000
    public final static String ACTIVITYACTION = "20000";//活动接口统一业务编号
    public final static String ACTIVITYLIST = "20001";//活动列表
    public final static String ACTIVITY = "20002";//活动详情
    public final static String SUBMIT = "20003";//提交申请
    public final static String WINNERLIST = "20004";//中奖信息
    public final static String RECORDS = "20006";//中奖记录

    //ACTION 30000
    public final static String SHOWANDSHARE = "30000";//发表动态统一业务编号
    public final static String SHOW = "30001";//晒单分享接口
    public final static String SHOWLIST = "30003";//查询晒单分享列表
    public final static String COMMENT = "30004";//评论动态
    public final static String REPLY = "30006";//回复评论

    //ACTION 40000
    public final static String PUBACTION = "40000";//公共接口统一业务编号
    public final static String AD = "40001";//广告
    public final static String STATEMENT = "40002";//查询声明信息接口
}
