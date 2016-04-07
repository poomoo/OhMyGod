/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;
import com.poomoo.ohmygod.utils.StatusBarUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.SmsShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 设置
 * 作者: 李苜菲
 * 日期: 2015/11/24 15:03.
 */
public class SettingActivity extends BaseActivity {
    public static SettingActivity instance;

    // 首先在您的Activity中添加如下成员变量
    public static final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    private String content = "天呐APP下载";
    //    private String website = "http://zgqg.91jiaoyou.cn/zgqg/weixin/shareFromApp/companyDesc.htm";
    private String website = "http://a.app.qq.com/o/simple.jsp?pkgname=com.poomoo.ohmygod";
    private String title = "天呐APP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.themeRed), 0);
        instance = this;
        addActivityToArrayList(this);
        initView();
    }

    protected void initView() {
        initTitleBar();
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_setting);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
    }

    /**
     * 个人资料
     *
     * @param view
     */
    public void toUserInfo(View view) {
        if (application.getIsAdvancedUser().equals("0"))              //普通用户
            openActivity(UserInfoActivity.class);
        else if (application.getIsAdvancedUser().equals("1"))         //升级会员
            openActivity(MemberInfoActivity.class);
    }

    /**
     * 用户帮助
     *
     * @param view
     */
    public void toUserHelp(View view) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_userHelp));
        openActivity(WebViewActivity.class, pBundle);
    }

    /**
     * 关于天呐
     *
     * @param view
     */
    public void toAbout(View view) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_about));
        openActivity(WebViewActivity.class, pBundle);
    }

    /**
     * 分享软件
     *
     * @param view
     */
    public void toShareSoft(View view) {
        // 配置需要分享的相关平台
        configPlatforms();
        // 设置分享内容
        shareContent();
        // 是否只有已登录用户才能打开分享选择页
        mController.getConfig().setPlatforms(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT, SHARE_MEDIA.SMS);
        mController.openShare(this, false);
    }

    /**
     * 意见反馈
     *
     * @param view
     */
    public void toFeedBack(View view) {
        openActivity(FeedBackActivity.class);
    }

    /**
     * 引导页
     *
     * @param view
     */
    public void toGuidePage(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.intent_parent), "guide");
        openActivity(IndexViewPagerActivity.class, bundle);
    }

    /**
     * 检查更新
     *
     * @param view
     */
    public void toCheckUpdate(View view) {
        if (MyUtil.isUpdate(this, application)) {
            AlertDialog dialog = new AlertDialog.Builder(this).setMessage("检测到新版本,请到相应的市场进行更新!").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Uri uri = Uri.parse("http://www.leyidao.com/yidao/appDownLoad/wap.html");
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
                }
            }).create();
            dialog.show();
        } else
            MyUtil.showToast(getApplicationContext(), "恭喜,已经是最新版本");
    }

    /**
     * 退出登录
     *
     * @param view
     */
    public void toLogOut(View view) {
        Dialog dialog = new AlertDialog.Builder(this).setMessage("确认退出?").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainFragmentActivity.instance.finish();
                SPUtils.put(getApplicationContext(), getString(R.string.sp_isLogin), false);
                finish();
                openActivity(LogInActivity.class);
            }
        }).create();
        dialog.show();
    }

    /**
     * 配置分享平台参数</br>
     */
    private void configPlatforms() {
        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        // 添加QQ、QZone平台
        addQQQZonePlatform();

        // 添加微信、微信朋友圈平台
        addWXPlatform();

        // 添加短信平台
        addSMS();
    }

    public void shareContent() {
        // 本地图片
        UMImage localImage = new UMImage(this, R.drawable.ic_logo);

        // 配置SSO
        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        // QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
        // "100424468", "c7394704798a158208a74ab60104f0ba");

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(content);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(website);
        weixinContent.setShareMedia(localImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(content);
        circleMedia.setTitle(title);
        circleMedia.setShareMedia(localImage);
        circleMedia.setTargetUrl(website);
        mController.setShareMedia(circleMedia);

        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(content);
        qzone.setTargetUrl(website);
        qzone.setTitle(title);
        qzone.setShareMedia(localImage);
        mController.setShareMedia(qzone);

        // 设置QQ分享内容
        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(content);
        qqShareContent.setTitle(title);
        qqShareContent.setShareMedia(localImage);
        qqShareContent.setTargetUrl(website);
        mController.setShareMedia(qqShareContent);

        // 设置短信分享内容
        SmsShareContent sms = new SmsShareContent();
        sms.setShareContent(content);
        sms.setShareImage(localImage);
        mController.setShareMedia(sms);

        // 设置新浪微博分享内容
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(content);
        sinaContent.setShareImage(localImage);
        mController.setShareMedia(sinaContent);

    }

    /**
     * @return
     * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
     * image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
     * 要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
     * : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
     */
    private void addQQQZonePlatform() {
        String appId = "1105034933";
        String appKey = "EjvftRWhsTXReJug";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, appId, appKey);
        qqSsoHandler.setTargetUrl(website);
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }

    /**
     * @return
     * @功能描述 : 添加微信平台分享
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wx49a72bd8d7b71519";
        String appSecret = "584a22fe3611fe843f8486827f8a68ba";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this, appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this, appId, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    /**
     * 添加短信平台
     */
    private void addSMS() {
        // 添加短信
        SmsHandler smsHandler = new SmsHandler();
        smsHandler.addToSocialSDK();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mController.getConfig().cleanListeners();
    }
}
