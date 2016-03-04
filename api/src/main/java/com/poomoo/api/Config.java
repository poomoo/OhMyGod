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
    public final static String TIME_OUT_EVENT_MSG = "当前网络无法使用";

    //ACTION 10000
    public final static String USERACTION = "10000";//用户相关接口统一业务编号
    public final static String CODE = "10002";//获取验证码
    public final static String CHECK = "10003";//校验验证码
    public final static String REGISTER = "10004";//注册
    public final static String LOGIN = "10005";//登陆
    public final static String CHANGE = "10006";//修改用户信息接口（单独修改用户某个字段信息，如昵称）
    public final static String PERSONALINFO = "10007";//编辑个人资料页面接口（实名认证接口）
    public final static String FEE = "10008";//查询提现费用提现信息
    public final static String SIGNEDLIST = "10010";//签到列表
    public final static String WITHDRAWDEPOSIT = "10009";//提现
    public final static String SIGNED = "10011";//保存签到接口
    public final static String WITHDRAWDEPOSITLIST = "10012";//保存签到接口
    public final static String SYNC = "10013";//同步用户信息统一接口
    public final static String PASSWORD = "10014";//重置帐号登录密码
    public final static String ADVANCE = "10015";//升级会员
    public final static String REBATEIINFO = "10016";//我的返现
    public final static String CHECKSTATUS = "10017";//检查登录状态

    //ACTION 20000
    public final static String ACTIVITYACTION = "20000";//活动接口统一业务编号
    public final static String ACTIVITYLIST = "20001";//活动列表
    public final static String ACTIVITY = "20002";//活动详情
    public final static String SUBMIT = "20003";//提交申请
    public final static String WINNERLIST = "20004";//中奖信息
    public final static String WININFOLIST = "20005";//查询活动中奖列表（分页）
    public final static String RECORDS = "20006";//中奖记录
    public final static String ACTIVITYWINNERLIST = "20007";//活动中奖人列表
    public final static String MERCHANTINFO = "20009";//商家信息
    public final static String CHECKWINNUM = "20010";//商家信息

    //ACTION 30000
    public final static String SHOWANDSHARE = "30000";//发表动态统一业务编号
    public final static String SHOW = "30001";//晒单分享接口
    public final static String SHOWLIST = "30003";//查询晒单分享列表
    public final static String COMMENT = "30004";//评论动态
    public final static String REPLY = "30006";//回复评论

    //ACTION 40000
    public final static String PUBACTION = "40000";//公共接口统一业务编号
    public final static String AD = "40001";//广告
    public final static String STATEMENT = "40002";//查询声明信息
    public final static String INFO = "40003";//查询声明列表详情信息
    public final static String CITYS = "40004";//查询城市
    public final static String FEEDBACK = "40005";//查询声明列表详情信息
    public final static String BOOTPICS = "40006";//查询app加载页面和引导页面
}
