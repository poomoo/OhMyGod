package com.poomoo.api;

import com.poomoo.model.FileBO;
import com.poomoo.model.ResponseBO;

/**
 * Api接口
 * <p>
 * 作者: 李苜菲
 * 日期: 2015/11/11 11:06.
 */
public interface Api {

    ResponseBO login(String phoneNum, String passWord, String channelId);

    ResponseBO getCode(String phoneNum);

    ResponseBO checkCode(String tel, String code);

    ResponseBO register(String phoneNum, String passWord, String code, String age, String sex, String channelId);

    ResponseBO getAdvertisement(String cityName, int type);

    ResponseBO getGrabList(String cityName, int currPage, int pageSize);

    ResponseBO getCommodityInformation(String userId, String activeId);

    ResponseBO putGrabInfo(String activeId, String userId);

    ResponseBO getWinnerList(String cityName);

    ResponseBO uploadPics(FileBO fileBO);

    ResponseBO putPersonalInfo(String userId, String realName, String idCardNum, String address);

    ResponseBO putMemberInfo(String userId, String bankName, String realName, String bankCardNum, String idFrontPic, String idOpsitePic);

    ResponseBO changePersonalInfo(String userId, String key, String value);

    ResponseBO toSigned(String userId);

    ResponseBO getSignedList(String userId, String year, String month);

    ResponseBO getStatement(String type);

    ResponseBO getWinningList(String userId, String flag, int currPage, int pageSize);

    ResponseBO getMyWithdrawDepositList(String userId);

    ResponseBO putShow(String userId, String activeId, String content, String pictures);

    ResponseBO getShowList(int flag, String userId, int currPage, int pageSize);

    ResponseBO putComment(String userId, String content, String dynamicId);

    ResponseBO putReply(String fromUserId, String toUserId, String content, String commentId);

    ResponseBO withDrawDeposit(String userId, String drawFee);

    ResponseBO getUserInfo(String userId);

    ResponseBO getWithDrawDepositFee(String userId, String drawFee);

    ResponseBO getMessages(String type, int currPage, int pageSize);

    ResponseBO getMessageInfo(String statementId);

    ResponseBO changePassWord(String tel, String passWord);

    ResponseBO putFeedBack(String userId, String content, String contact);

    ResponseBO getCitys();

    ResponseBO getWinningInfo(String cityName, int currPage, int pageSize);

    ResponseBO putAdvanceInfo(String userId, String realName, String sex, String age, String tel, String address, String idCardNum, String xueli, String zhiye, String ysr, String sfgfyx, String gfxzfs, String gfxzyy, String gfxzmj, String gfxzjw, String gfxzqy, String gfkzwt);

    ResponseBO getRebate(String userId);

    ResponseBO getRebateInfo(String userId);

    ResponseBO getActivityWinnerList(String activeId, int currPage, int pageSize);

    ResponseBO checkStatus(String userId, String channelId);

    ResponseBO getMerchantInfo(String userId, int activeId, String playDt, String winNumber, String isGot, int currPage, int pageSize);

    ResponseBO checkWinNum(String userId, String winNumber);

    ResponseBO getBootPics(int type);

    ResponseBO getAd(int advId);

    ResponseBO getActivityType();

    ResponseBO getActivityById(String cityName, int cateId, int currPage, int pageSize);

}
