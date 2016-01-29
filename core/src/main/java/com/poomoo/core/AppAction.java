package com.poomoo.core;

import com.poomoo.model.FileBO;

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
    void logIn(String phoneNum, String passWord,String channelId , ActionCallbackListener listener);

    /**
     * 获取验证码
     *
     * @param phoneNum
     * @param listener
     */
    void getCode(String phoneNum, ActionCallbackListener listener);

    /**
     * 校验验证码
     *
     * @param tel
     * @param code
     * @param listener
     */
    void checkCode(String tel, String code, ActionCallbackListener listener);

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
    void getAdvertisement(String cityName, ActionCallbackListener listener);

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

    /**
     * 提交抢单数据
     *
     * @param activeId
     * @param userId
     * @param listener
     */
    void putGrab(String activeId, String userId, ActionCallbackListener listener);

    /**
     * 获取中奖人列表
     *
     * @param cityName
     * @param listener
     */
    void getWinnerList(String cityName, ActionCallbackListener listener);

    /**
     * 上传图片
     *
     * @param fileBO
     * @param listener
     */
    void uploadPics(FileBO fileBO, ActionCallbackListener listener);

    /**
     * 上传个人资料
     *
     * @param userId
     * @param realName
     * @param idCardNum
     * @param listener
     */
    void putPersonalInfo(String userId, String realName, String idCardNum, String address, ActionCallbackListener listener);

    /**
     * 完善升级会员资料
     *
     * @param userId
     * @param realName
     * @param bankCardNum
     * @param idFrontPic
     * @param idOpsitePic
     * @param listener
     */
    void putMemberInfo(String userId, String bankName, String realName, String bankCardNum, String idFrontPic, String idOpsitePic, ActionCallbackListener listener);

    /**
     * 修改用户信息接口（单独修改用户某个字段信息，如昵称）
     *
     * @param userId
     * @param key--修改字段的key
     * @param value--修改字段的value
     * @param listener
     */
    void changePersonalInfo(String userId, String key, String value, ActionCallbackListener listener);

    /**
     * 签到
     *
     * @param userId
     * @param listener
     */
    void toSign(String userId, ActionCallbackListener listener);

    /**
     * 获取签到列表
     *
     * @param userId
     * @param year
     * @param month
     * @param listener
     */
    void getSignedList(String userId, String year, String month, ActionCallbackListener listener);

    /**
     * 查询声明信息
     *
     * @param type --1：注册声明，2：游戏规则声明，3返现声明，4提现帮助，5公共消息,6签到声明,7关于,8站内消息,9用户帮助
     */
    void getStatement(String type, ActionCallbackListener listener);

    /**
     * 查询我的中奖列表（分页）
     *
     * @param userId
     * @param flag
     * @param currPage --1：我的夺宝记录，2：我的中奖记录
     * @param pageSize --当前页号码
     * @param listener --页面大小
     */
    void getWinningList(String userId, String flag, int currPage, int pageSize, ActionCallbackListener listener);

    /**
     * 查询我的提现
     *
     * @param userId
     * @param listener
     */
    void getMyWithdrawDepositList(String userId, ActionCallbackListener listener);

    /**
     * 晒单分享接口
     *
     * @param userId
     * @param activeId--活动编号
     * @param content--发表内容
     * @param pictures--图片地址
     * @param listener
     */
    void putShow(String userId, String activeId, String content, String pictures, ActionCallbackListener listener);

    /**
     * 查询晒单分享列表
     *
     * @param flag--接口标识，1表示晒单分享列表，2表示我的晒单列表
     * @param userId
     * @param currPage
     * @param pageSize
     * @param listener
     */
    void getShowList(int flag, String userId, int currPage, int pageSize, ActionCallbackListener listener);

    /**
     * 评论动态
     *
     * @param userId
     * @param content
     * @param dynamicId
     * @param listener
     */
    void putComment(String userId, String content, String dynamicId, ActionCallbackListener listener);

    /**
     * 回复评论
     *
     * @param fromUserId--回复人编号
     * @param toUserId--被回复人编号
     * @param content--评论内容
     * @param commentId--评论编号
     * @param listener
     */
    void putReply(String fromUserId, String toUserId, String content, String commentId, ActionCallbackListener listener);

    /**
     * 提现
     *
     * @param userId
     * @param drawFee
     * @param listener
     */
    void withDrawDeposit(String userId, String drawFee, ActionCallbackListener listener);

    /**
     * 同步用户信息
     *
     * @param userId
     * @param listener
     */
    void getUserInfo(String userId, ActionCallbackListener listener);

    /**
     * 提现手续费查询
     *
     * @param userId
     * @param drawFee
     * @param listener
     */
    void getWithDrawDepositFee(String userId, String drawFee, ActionCallbackListener listener);

    /**
     * 获取消息
     *
     * @param type
     * @param currPage
     * @param pageSize
     * @param listener
     */
    void getMessages(String type, int currPage, int pageSize, ActionCallbackListener listener);

    /**
     * 获取消息详情
     *
     * @param statementId
     * @param listener
     */
    void getMessageInfo(String statementId, ActionCallbackListener listener);

    /**
     * 修改密码
     *
     * @param tel
     * @param passWord1
     * @param passWord2
     * @param listener
     */
    void changePassWord(String tel, String passWord1, String passWord2, ActionCallbackListener listener);

    /**
     * 提交反馈
     *
     * @param userId
     * @param content
     * @param contact
     * @param listener
     */
    void putFeedBack(String userId, String content, String contact, ActionCallbackListener listener);

    /**
     * 获取城市列表
     *
     * @param listener
     */
    void getCitys(ActionCallbackListener listener);

    /**
     * 查询活动中奖列表（分页）
     *
     * @param cityName
     * @param currPage
     * @param pageSize
     * @param listener
     */
    void getWinningInfo(String cityName, int currPage, int pageSize, ActionCallbackListener listener);

    /**
     * 升级会员资料提交
     *
     * @param userId
     * @param realName
     * @param sex
     * @param age
     * @param tel
     * @param address
     * @param idCardNum
     * @param listener
     */
    void putAdvanceInfo(String userId, String realName, String sex, String age, String tel, String address, String idCardNum, String xueli, String zhiye, String ysr, String sfgfyx, String gfxzfs, String gfxzyy, String gfxzmj, String gfxzjw, String gfxzqy, String gfkzwt, ActionCallbackListener listener);

    /**
     * 查询返现
     *
     * @param userId
     * @param listener
     */
    void getRebate(String userId, ActionCallbackListener listener);

    /**
     * 返现记录
     *
     * @param userId
     * @param listener
     */
    void getRebateInfo(String userId, ActionCallbackListener listener);

    /**
     * 获取活动中奖列表
     *
     * @param activeId
     * @param listener
     */
    void getActivityWinnerList(String activeId,  int currPage, int pageSize,ActionCallbackListener listener);

    /**
     * 检查登录状态
     *
     * @param userId
     * @param channelId
     * @param listener
     */
    void checkStatus(String userId, String channelId, ActionCallbackListener listener);
}
