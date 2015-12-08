package com.poomoo.ohmygod.application;

import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.poomoo.core.AppAction;
import com.poomoo.core.AppActionImpl;
import com.poomoo.ohmygod.R;

/**
 * 自定义Application
 * 作者: 李苜菲
 * 日期: 2015/11/11 11:26.
 */
public class MyApplication extends Application {
    private AppAction appAction;
    // 用户信息
    private String userId;
    private String tel;//--用户手机号码
    private String nickName;//--用户昵称
    private String realName;
    private String headPic;
    private String currentFee;//--用户余额
    private String realNameAuth;
    private String idCardNum;
    private String sex;//1：男，2：女
    private String age;
    private String idFrontPic;//--正面照
    private String idOpsitePic;//--反面照
    private String bankCardNum;//--银行卡号码
    private String bankName;//--开户行名称
    private String isActiveWarm;//--活动警告1开启，0未开启
    private String currCity;//当前城市
    private String localCity;//定位城市

    @Override
    public void onCreate() {
        super.onCreate();
        appAction = new AppActionImpl(this);

        initImageLoader();
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.mipmap.ic_launcher) //
                .showImageOnFail(R.mipmap.ic_launcher) //
                .cacheInMemory(true) //
                .cacheOnDisk(false) //
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//
        ImageLoaderConfiguration config = new ImageLoaderConfiguration//
                .Builder(getApplicationContext())//
                .defaultDisplayImageOptions(defaultOptions)//
                .writeDebugLogs()//
                .build();//
        ImageLoader.getInstance().init(config);
    }

    public AppAction getAppAction() {
        return appAction;
    }

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

    public void setAppAction(AppAction appAction) {
        this.appAction = appAction;
    }

    public String getCurrCity() {
        return currCity;
    }

    public void setCurrCity(String currCity) {
        this.currCity = currCity;
    }

    public String getLocalCity() {
        return localCity;
    }

    public void setLocalCity(String localCity) {
        this.localCity = localCity;
    }
}
