/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.CommentBO;
import com.poomoo.model.ReplyBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.ShowBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.listeners.ActivityListener;
import com.poomoo.ohmygod.listeners.LongClickListener;
import com.poomoo.ohmygod.listeners.ReplyListener;
import com.poomoo.ohmygod.adapter.ShowAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.listeners.ShareListener;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.activity.CommodityInformation2Activity;
import com.poomoo.ohmygod.view.activity.MainFragmentActivity;
import com.poomoo.ohmygod.view.custom.RefreshLayout;
import com.poomoo.ohmygod.view.custom.RefreshLayout.OnLoadListener;
import com.poomoo.ohmygod.view.popupwindow.CopyPopupWindow;
import com.poomoo.ohmygod.view.popupwindow.GenderPopupWindow;
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
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import java.util.ArrayList;

import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

import java.util.List;

/**
 * 晒
 * 作者: 李苜菲
 * 日期: 2015/11/20 09:50.
 */
public class ShowFragment extends BaseFragment implements OnRefreshListener, OnLoadListener, ReplyListener, ShareListener, LongClickListener, ActivityListener {
    private RefreshLayout refreshLayout;
    private EditText replyEdt;
    private Button replyBtn;
    private LinearLayout fragmentView;
    private LinearLayout replyRlayout;
    private LinearLayout editLlayout;
    private ListView list;
    private ShowAdapter adapter;
    private ShowBO showBO;
    private CommentBO commentBO;
    private ReplyBO replyBO;
    private List<ShowBO> showBOList = new ArrayList<>();
    private List<ReplyBO> replyBOList = new ArrayList<>();
    private List<CommentBO> commentBOList = new ArrayList<>();
    private List<String> picList = new ArrayList<>();
    private int screenHeight;
    private int keyBoardHeight;
    private int itemPosition;//item的位置
    private int commentPosition;//评论的位置
    private int replyPosition;//回复评论的位置
    private boolean isKeyBoardShow = false;
    private View view;
    private int viewTop;
    private int itemTop;
    private int y1;//键盘弹出后剩余的界面底部y坐标
    private int y2;//点击的view距离当前item顶端的距离
    private String replyContent;
    private boolean isComment = false;//评论
    private boolean isReply = false;//回复
    private boolean isReplyComment = false;//true-回复评论 false-回复回复列表里面的评论
    private String toNickName;
    private String toUserId;
    private boolean isLoad = false;//true 加载 false刷新
    private int currPage = 1;

    // 首先在您的Activity中添加如下成员变量
    public static final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    private String content = "天呐" + "\n" + "http://www.baidu.com";
    private String website = "http://zgqg.91jiaoyou.cn/zgqg/weixin/shareFromApp/share.htm?dynamicId=";
    private String title = "天呐";
    private String dynamicId = "";
    private String picUrl;
    private CopyPopupWindow copyPopupWindow;
    private String copyContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        showProgressDialog(getString(R.string.dialog_message));
        getData();

    }

    /**
     * 回复
     *
     * @param name
     * @param itemPosition
     * @param v
     * @param show
     * @param commentPosition
     */
    @Override
    public void onResult(String name, View v, ShowBO show, int itemPosition, int commentPosition) {
        if (!MyUtil.isLogin(getActivity()))
            return;
        toNickName = name;
        view = v;
        this.itemPosition = itemPosition;
        this.commentPosition = commentPosition;
//        replyPosition = replyPosition;
        viewTop = view.getTop();
        showBO = show;
//        LogUtils.i(TAG, "showBO:" + showBO);
        LogUtils.i(TAG, "回复评论:" + "评论的位置:" + commentPosition + " 回复评论的位置:" + replyPosition);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        MainFragmentActivity.instance.invisible();
        replyRlayout.setVisibility(View.VISIBLE);
//        LogUtils.i(TAG, "replyRlayout可见");
        replyEdt.setFocusable(true);
        replyEdt.setFocusableInTouchMode(true);
        replyEdt.requestFocus();
        isKeyBoardShow = true;
        //名字为空则是评论
        if (TextUtils.isEmpty(toNickName)) {
            replyEdt.setHint("");
            isComment = true;
            isReply = false;
        }
        //否则为回复
        else {
            replyEdt.setHint("回复" + name);
            isComment = false;
            isReply = true;
        }
    }

    /**
     * 分享
     *
     * @param title
     * @param content
     * @param picUrl
     */
    @Override
    public void onResult(String title, String content, String picUrl, String dynamicId) {
        if (!MyUtil.isLogin(getActivity()))
            return;
        this.title = title;
        this.content = content;
        this.picUrl = picUrl;
        this.dynamicId = dynamicId;
        website += dynamicId;
        // 配置需要分享的相关平台
        configPlatforms();
        // 设置分享内容
        shareContent();
        // 是否只有已登录用户才能打开分享选择页
        mController.getConfig().setPlatforms(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT, SHARE_MEDIA.SMS);
        mController.openShare(getActivity(), false);
    }

    private void initView() {
        refreshLayout = (RefreshLayout) getActivity().findViewById(R.id.refresh_show);
        fragmentView = (LinearLayout) getActivity().findViewById(R.id.llayout_show);
        replyRlayout = (LinearLayout) getActivity().findViewById(R.id.rlayout_reply);
        editLlayout = (LinearLayout) getActivity().findViewById(R.id.layout_edittext);
        replyEdt = (EditText) getActivity().findViewById(R.id.edt_reply);
        replyBtn = (Button) getActivity().findViewById(R.id.btn_reply);
        list = (ListView) getActivity().findViewById(R.id.list_show);

        refreshLayout.setOnLoadListener(this);
        refreshLayout.setOnRefreshListener(this);

        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyContent = replyEdt.getText().toString().trim();
                if (TextUtils.isEmpty(replyContent))
                    MyUtil.showToast(application.getApplicationContext(), "请输入内容");
                else {
                    hiddenReply(v);
                    replyEdt.setText("");
                    if (isComment)
                        putComment();
                    if (isReply)
                        putReply();
                }

            }
        });
        fragmentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                fragmentView.getWindowVisibleDisplayFrame(r);
                screenHeight = fragmentView.getRootView().getHeight();
                keyBoardHeight = screenHeight - (r.bottom - r.top);
                boolean visible = keyBoardHeight > screenHeight / 3;
//                LogUtils.i(TAG, "键盘不可见:" + "screenHeight:" + screenHeight + "r.bottom:" + r.bottom + "r.top:" + r.top + "keyBoardHeight" + keyBoardHeight);
                if (visible)
                    moveList(itemPosition);
//                LogUtils.i(TAG, "键盘可见:" + "screenHeight:" + screenHeight + "r.bottom:" + r.bottom + "r.top:" + r.top + "keyBoardHeight" + keyBoardHeight);

//                    Rect fragmentWindowRect = new Rect();
//                    Rect fragmentLocalRect = new Rect();
//                    Rect inputViewRect = new Rect();
//                    //显示Fragment的可见区域
//                    //若键盘在隐藏状态，会返回全屏幕的尺寸
//                    //若键盘在显示状态，会返回除键盘以外的尺寸，这是我想要的
//                    fragmentView.getWindowVisibleDisplayFrame(fragmentWindowRect);
//                    //获取Fragment在activity上显示区域
//                    //无论键盘是否显示，值都一样
//                    fragmentView.getLocalVisibleRect(fragmentLocalRect);
//                    //获取输入界面的位置和大小
//                    editLlayout.getLocalVisibleRect(inputViewRect);
//
//                    int inputViewHeight = inputViewRect.bottom - inputViewRect.top;
//                    LogUtils.i(TAG, "inputViewHeight:" + inputViewHeight);
////                    int targetBottom = Math.min(fragmentLocalRect.bottom, fragmentWindowRect.bottom);
//                    int targetBottom = fragmentWindowRect.bottom;
//                    LogUtils.i(TAG, "fragmentLocalRect.bottom:" + fragmentLocalRect.bottom + "fragmentWindowRect.bottom:" + fragmentWindowRect.bottom);
//                    int top = targetBottom - inputViewHeight;
//                    LogUtils.i(TAG, "top:" + top + "right:" + inputViewRect.right + "bottom:" + targetBottom);
//                    editLlayout.layout(0, top, inputViewRect.right, targetBottom);
//                }
            }
        });

        adapter = new ShowAdapter(getActivity(), this, this, this, this);
        list.setAdapter(adapter);

        replyRlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.i(TAG, "点击屏幕");
                hiddenReply(view);
            }
        });
//        testData();
    }


    public void hiddenReply(View view) {
        MainFragmentActivity.instance.visible();
        if (replyRlayout.getVisibility() == View.VISIBLE) {
            replyRlayout.setVisibility(View.GONE);
        }
        //隐藏键盘
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        isKeyBoardShow = false;
    }

/*    private void testData() {
        int len = MyConfig.testUrls.length;
        for (int i = 0; i < len; i++)
            picList.add(MyConfig.testUrls[i]);

        showBO = new ShowBO();
        showBO.setPicList(picList);
        showBO.setNickName("十年九梦你");
        showBO.setDynamicDt((new Date()).toString());
        showBO.setContent("货已经收到了，东西不错");
        showBO.setTitle("(第123期)电脑疯抢玩命中...");

        commentBO = new CommentBO();
        commentBO.setNickName("糊涂图");
        commentBO.setContent("你运气真好");

        replyBO = new ReplyBO();
        replyBO.setFromNickName("十年九梦你");
        replyBO.setToNickName("糊涂图");
        replyBO.setContent("是的");
        replyBOList.add(replyBO);

        commentBO.setReplies(replyBOList);
        commentBOList.add(commentBO);
        showBO.setComments(commentBOList);

        showBOList.add(showBO);


        showBO = new ShowBO();
        showBO.setPicList(picList);
        showBO.setNickName("跑马安卓小飞");
        showBO.setDynamicDt((new Date()).toString());
        showBO.setContent("什么玩意儿");
        showBO.setTitle("(第124期)疯狂搬砖中...");

        commentBO = new CommentBO();
        commentBO.setNickName("马云");
        commentBO.setContent("小伙子好好干");

        replyBO = new ReplyBO();
        replyBO.setFromNickName("跑马安卓小飞");
        replyBO.setToNickName("马云");
        replyBO.setContent("好的");
        replyBOList = new ArrayList<>();
        replyBOList.add(replyBO);

        commentBO.setReplies(replyBOList);
        commentBOList = new ArrayList<>();
        commentBOList.add(commentBO);
        showBO.setComments(commentBOList);

        showBOList.add(showBO);

        showBO = new ShowBO();
        showBO.setPicList(picList);
        showBO.setNickName("劉強東");
        showBO.setDynamicDt((new Date()).toString());
        showBO.setContent("大愛奶茶妹");
        showBO.setTitle("(第125期)愛愛愛...");

        commentBO = new CommentBO();
        commentBO.setNickName("劉強東");
        commentBO.setContent("我媳婦是奶茶妹 ");

        replyBO = new ReplyBO();
        replyBO.setFromNickName("奶茶妹");
        replyBO.setToNickName("劉強東");
        replyBO.setContent("老公我愛你");
        replyBOList = new ArrayList<>();
        replyBOList.add(replyBO);

        commentBO.setReplies(replyBOList);
        commentBOList = new ArrayList<>();
        commentBOList.add(commentBO);
        showBO.setComments(commentBOList);

        showBOList.add(showBO);
        adapter.setItems(showBOList);
    }*/

    private void getData() {
        //1表示晒单分享列表，2表示我的晒单列表
        this.appAction.getShowList(1, application.getUserId(), currPage, MyConfig.PAGESIZE, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                LogUtils.i(TAG, data.getObjList().toString());
                closeProgressDialog();
                // 更新完后调用该方法结束刷新
                showBOList = new ArrayList<>();
                if (isLoad) {
                    refreshLayout.setLoading(false);
                    int len = data.getObjList().size();
                    for (int i = 0; i < len; i++)
                        showBOList.add((ShowBO) data.getObjList().get(i));
                    if (len > 0) {
                        currPage++;
                        adapter.addItems(showBOList);
                    }
                } else {
                    currPage++;
                    refreshLayout.setRefreshing(false);
                    showBOList = data.getObjList();
                    if (showBOList.size() > 0)
                        adapter.setItems(showBOList);
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                if (isLoad)
                    refreshLayout.setLoading(false);
                else
                    refreshLayout.setRefreshing(false);
                MyUtil.showToast(getActivity().getApplicationContext(), message);
            }
        });
    }

    /**
     * 评论
     */
    private void putComment() {
        showProgressDialog(getString(R.string.dialog_message));
        this.appAction.putComment(application.getUserId(), replyContent, showBO.getDynamicId(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                commentBO = (CommentBO) data.getObj();
                commentBO.setNickName(application.getNickName());
                commentBO.setContent(replyContent);
                commentBO.setDynamicId(showBO.getDynamicId());
                commentBO.setUserId(application.getUserId());
                replyBOList = new ArrayList<>();
                commentBO.setReplies(replyBOList);
                showBOList.get(itemPosition).getComments().add(commentBO);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(application.getApplicationContext(), message);
            }
        });
    }

    /**
     * 回复
     */
    private void putReply() {
        showProgressDialog(getString(R.string.dialog_message));
        final String fromUserId = application.getUserId();
        //回复评论
        if (!TextUtils.isEmpty(showBO.getComments().get(0).getUserId())) {
            isReplyComment = true;
            toUserId = showBO.getComments().get(0).getUserId();
        }
        //回复回复列表里面的评论
        if (showBO.getComments().get(0).getReplies() != null && showBO.getComments().get(0).getReplies().size() > 0) {
            isReplyComment = false;
            toUserId = showBO.getComments().get(0).getReplies().get(0).getToUserId();
        }

        if (isReplyComment)
            LogUtils.i(TAG, "回复评论");
        else
            LogUtils.i(TAG, "回复回复列表里面的评论");

        final String commentId = showBO.getComments().get(0).getCommentId();
        LogUtils.i(TAG, "showBO:" + showBO);
        this.appAction.putReply(fromUserId, toUserId, replyContent, commentId, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                replyBO = new ReplyBO();
                replyBO.setFromUserId(fromUserId);
                replyBO.setToUserId(toUserId);
                replyBO.setContent(replyContent);
                replyBO.setCommentId(commentId);
                replyBO.setToNickName(toNickName);
                replyBO.setFromNickName(application.getNickName());
                if (isReplyComment) {
                    replyBOList = new ArrayList<>();
                    replyBOList.add(replyBO);
                    showBOList.get(itemPosition).getComments().get(commentPosition).setReplies(replyBOList);
                } else {
                    showBOList.get(itemPosition).getComments().get(commentPosition).getReplies().add(replyBO);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(application.getApplicationContext(), message);
            }
        });
    }

    private void moveList(int itemPosition) {
        if (adapter == null)
            return;

        LogUtils.i(TAG, "itemPosition:" + itemPosition);
        if (isKeyBoardShow) {
//            LogUtils.i(TAG, "moveList:" + itemPosition + "count:" + showAdapter.getCount());
//            LogUtils.i(TAG, "screenHeight:" + screenHeight + "keyBoardHeight:" + keyBoardHeight);
//
//            y1 = screenHeight - keyBoardHeight;
//            itemTop = list.getChildAt(itemPosition).getTop();
//            LogUtils.i(TAG, "viewTop:" + viewTop + "list.getChildAt(itemPosition).getTop():" + itemTop);
//            y2 = viewTop - list.getChildAt(itemPosition).getTop();
//            int off = y1 - y2;
//            LogUtils.i(TAG, "off:" + off + "y1:" + y1 + "y2:" + y2);
            list.setSelectionFromTop(itemPosition, 0);
        }
        isKeyBoardShow = false;
    }

    @Override
    public void onLoad() {
        isLoad = true;
        refreshLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据
                getData();
            }
        }, 0);
    }

    @Override
    public void onRefresh() {
        currPage = 1;
        isLoad = false;
        refreshLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据
                getData();
            }
        }, 0);
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
        UMImage localImage = new UMImage(getActivity(), picUrl);

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
        String appId = "100424468";
        String appKey = "c7394704798a158208a74ab60104f0ba";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), appId, appKey);
        qqSsoHandler.setTargetUrl(website);
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(getActivity(), appId, appKey);
        qZoneSsoHandler.addToSocialSDK();
    }

    /**
     * @return
     * @功能描述 : 添加微信平台分享
     */
    private void addWXPlatform() {
        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        String appId = "wx55e834ca0a0327a6";
        String appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(getActivity(), appId, appSecret);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appId, appSecret);
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

    /**
     * 长按复制
     *
     * @param content
     */
    @Override
    public void onResult(String content) {
        copyContent = content;
        copy();
    }

    private void copy() {
        // 实例化
        copyPopupWindow = new CopyPopupWindow(getActivity(), itemsOnClick);
        // 显示窗口
        copyPopupWindow.showAtLocation(getActivity().findViewById(R.id.llayout_show),
                Gravity.CENTER, 0, 0); // 设置layout在genderWindow中显示的位置
    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            copyPopupWindow.dismiss();
            switch (view.getId()) {
                case R.id.llayout_copy:
                    ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(copyContent.trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
//                    cmb.getText();//获取粘贴信息
                    MyUtil.showToast(application.getApplicationContext(), "复制 " + copyContent + " 成功");
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** 使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(String activeName, int activeId) {
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.intent_activeId), activeId);
        bundle.putString(getString(R.string.intent_activityName), activeName);
        openActivity(CommodityInformation2Activity.class, bundle);
    }
}
