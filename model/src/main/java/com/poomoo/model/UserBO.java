/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.model;

/**
 * 用户的业务模型类
 * 作者: 李苜菲
 * 日期: 2015/11/30 17:34.
 */
public class UserBO {
    private String userId = "";
    private String tel = "";//--用户手机号码
    private String nickName = "";//--用户昵称
    private String realName = "";
    private String headPic = "";
    private String currentFee = "";//--用户余额
    private String realNameAuth = "";
    private String idCardNum = "";
    private String sex = "";
    private String age = "";
    private String idFrontPic = "";//--正面照
    private String idOpsitePic = "";//--反面照
    private String bankCardNum = "";//--银行卡号码
    private String bankName = "";//--开户行名称
    private String isActiveWarm = "";//--活动警告1开启，0未开启
    private String address = "";//收货地址
    private String isAdvancedUser = "";//是否为高级会员  默认值是0，1表示已经成为高级会员
    private String channelId = "";//设备ID,登录时后台返回
    private int userType;//1:商户，2普通用户
    private int appVersion;//版本号

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getCurrentFee() {
        return currentFee;
    }

    public void setCurrentFee(String currentFee) {
        this.currentFee = currentFee;
    }

    public String getRealNameAuth() {
        return realNameAuth;
    }

    public void setRealNameAuth(String realNameAuth) {
        this.realNameAuth = realNameAuth;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getIdFrontPic() {
        return idFrontPic;
    }

    public void setIdFrontPic(String idFrontPic) {
        this.idFrontPic = idFrontPic;
    }

    public String getIdOpsitePic() {
        return idOpsitePic;
    }

    public void setIdOpsitePic(String idOpsitePic) {
        this.idOpsitePic = idOpsitePic;
    }

    public String getBankCardNum() {
        return bankCardNum;
    }

    public void setBankCardNum(String bankCardNum) {
        this.bankCardNum = bankCardNum;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIsActiveWarm() {
        return isActiveWarm;
    }

    public void setIsActiveWarm(String isActiveWarm) {
        this.isActiveWarm = isActiveWarm;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIsAdvancedUser() {
        return isAdvancedUser;
    }

    public void setIsAdvancedUser(String isAdvancedUser) {
        this.isAdvancedUser = isAdvancedUser;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }


    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    @Override
    public String toString() {
        return "UserBO{" +
                "userId='" + userId + '\'' +
                ", tel='" + tel + '\'' +
                ", nickName='" + nickName + '\'' +
                ", realName='" + realName + '\'' +
                ", headPic='" + headPic + '\'' +
                ", currentFee='" + currentFee + '\'' +
                ", realNameAuth='" + realNameAuth + '\'' +
                ", idCardNum='" + idCardNum + '\'' +
                ", sex='" + sex + '\'' +
                ", age='" + age + '\'' +
                ", idFrontPic='" + idFrontPic + '\'' +
                ", idOpsitePic='" + idOpsitePic + '\'' +
                ", bankCardNum='" + bankCardNum + '\'' +
                ", bankName='" + bankName + '\'' +
                ", isActiveWarm='" + isActiveWarm + '\'' +
                ", address='" + address + '\'' +
                ", isAdvancedUser='" + isAdvancedUser + '\'' +
                ", channelId='" + channelId + '\'' +
                ", userType=" + userType +
                ", appVersion=" + appVersion +
                '}';
    }
}
