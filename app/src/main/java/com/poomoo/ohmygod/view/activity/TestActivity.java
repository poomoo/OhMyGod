/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.CommentBO;
import com.poomoo.model.GrabBO;
import com.poomoo.model.ReplyBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.ShowBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.GrabAdapter;
import com.poomoo.ohmygod.adapter.ShowAdapter;
import com.poomoo.ohmygod.alarm.CallAlarm;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.database.ActivityInfo;
import com.poomoo.ohmygod.listeners.AlarmtListener;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.custom.NoScrollListView;
import com.poomoo.ohmygod.view.custom.pullable.PullToRefreshLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/26 10:22.
 */
public class TestActivity extends BaseActivity implements AlarmtListener {

    private EditText replyEdt;
    private Button replyBtn;
    private LinearLayout replyLlayout;
    private LinearLayout rootView;
    private ListView list;
    private ShowAdapter showAdapter;
    private ShowBO showBO;
    private CommentBO commentBO;
    private ReplyBO replyBO;
    private List<ShowBO> showBOList = new ArrayList<>();
    private List<ReplyBO> replyBOList = new ArrayList<>();
    private List<CommentBO> commentBOList = new ArrayList<>();
    private ArrayList<String> picList = new ArrayList<>();
    private int selectPosition;
    private int screenHeight;
    private int keyBoardHeight;
    private View view;
    private int viewTop;
    private int itemTop;
    private int y1;//键盘弹出后剩余的界面底部y坐标
    private int y2;//点击的view距离当前item顶端的距离
    private boolean isKeyBoardShow = false;
    private int commentPosition;

    private ImageView imageView;
    private static final String url = "http://zgqg.91jiaoyou.cn/zgqg/upload/active/1449467937354.jpg";
    private int index = 0;
    private Timer timer;
    private boolean firstFlag = true;
    private TextView percentTxt;
    private int percent = 0;
    private WebView webView;
    //    private static final String content = "<p>\n" + "<img src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451098807631.jpg\" >\n" + "</p>\n";
//    private static final String content = "<meta name=\"viewport\" content=\"width=1200, initial-scale=1.0\"><img src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893944698.jpg\" _src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893944698.jpg\" /><img src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893949589.jpg\" _src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893949589.jpg\"/><img src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893952256.jpg\" _src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893952256.jpg\"/><img src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893954099.jpg\" _src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893954099.jpg\"/><img src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893955909.jpg\" _src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893955909.jpg\"/><img src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893958402.jpg\" _src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893958402.jpg\"/><img src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893962925.jpg\" _src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893962925.jpg\"/><img src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893969978.jpg\" _src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893969978.jpg\"/><img src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893973474.jpg\" _src=\"http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451893973474.jpg\"/>";

    private MyCustomView mView;
    private Movie mMovie;
    private long mMovieStart;

    // 首先在您的Activity中添加如下成员变量
    public static final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
    private String content = "天呐" + "\n" + "http://www.baidu.com";
    private String website = "http://zgqg.91jiaoyou.cn/zgqg/weixin/shareFromApp/share.htm?dynamicId=";
    private String title = "天呐";
    private String picUrl = "asdf";
    private int activeId[] = {111, 222, 333};
    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";
    private Uri queryUri;

    private NoScrollListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        listView = (NoScrollListView) findViewById(R.id.list_test);
        adapter = new GrabAdapter(this, this);
        listView.setAdapter(adapter);
//        mView = new MyCustomView(this);
//        webView = (WebView) findViewById(R.id.web_test);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        webView.getSettings().setPluginsEnabled(true);//可以使用插件
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.getSettings().setAllowFileAccess(true);
//        webView.getSettings().setDefaultTextEncodingName("UTF-8");
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.setVisibility(View.VISIBLE);
//        webView.loadUrl("http://v.qq.com/page/w/0/6/w01802c0ov6.html");//http://player.youku.com/embed/XNTM5MTUwNDA0 http://v.youku.com/v_show/id_XMTQ5NTE0OTQ3Ng
        //html自适应
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setDefaultTextEncodingName("UTF-8");
//        webView.loadUrl("http://static.video.qq.com/TPout.swf?vid=w01802c0ov6&auto=0");
//        webView.loadData(content, "text/html; charset=UTF-8", null);// 这种写法可以正确解码

//        imageView = (ImageView) findViewById(R.id.img_test);
//        percentTxt = (TextView) findViewById(R.id.txt_percent);
//        LogUtils.i(TAG, "前:" + "width:" + imageView.getWidth() + "--height:" + imageView.getHeight());
//
//        ImageLoader.getInstance().displayImage(url,imageView);
//        ImageLoader.getInstance().loadImage(url,  new SimpleImageLoadingListener() {
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                LogUtils.i(TAG, "后:" + "width:" + imageView.getWidth() + "--height:" + imageView.getHeight());
//                imageView.setImageBitmap(loadedImage);
//            }
//        });

//        initView();

        // 配置需要分享的相关平台
//        configPlatforms();
        // 设置分享内容
//        shareContent();
        getGrabList(true);

    }

    @Override
    public void setAlarm(String title, int activeId, String startDt, String endDt, ImageView imageView) {
    }

    private List<GrabBO> grabBOList = new ArrayList<>();
    private int currPage = 1;//当前页
    private GrabAdapter adapter;

    private void getGrabList(final boolean isRefreshable) {
        LogUtils.i(TAG, "getGrabList:" + currPage);
        this.appAction.getGrabList("贵阳市", currPage, 15, new ActionCallbackListener() {
                    @Override
                    public void onSuccess(ResponseBO data) {
                        if (isRefreshable) {
                            grabBOList = new ArrayList<>();
                            grabBOList = data.getObjList();
                            for (int i = 0; i < 3; i++) {
                                GrabBO grabBO = new GrabBO();
                                grabBO.setActiveId(1000 + i);
                                grabBO.setStatus(1);
                                grabBO.setTotalWinNum(10 + i);
                                grabBO.setCurrWinNum(1 + i);
                                grabBO.setPicture(i + "");
                                grabBOList.add(grabBO);
                            }

                            int len = grabBOList.size();
                            if (len > 0) {
                                currPage++;
                                for (int i = 0; i < len; i++) {
                                    grabBOList.get(i).setStartCountdown(15 * 1000 + i * 5 * 1000);
                                    LogUtils.i("test", "i:" + i + "len:" + len);
                                }

                                adapter.setItems(grabBOList);
                            }
                        } else {
                            grabBOList = data.getObjList();
                            int len = grabBOList.size();
                            if (len > 0) {
                                currPage++;
                                adapter.addItems(grabBOList);
                            }
                        }

                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                    }
                }
        );
    }

    public void toOk(View view) {
//        setAlarm();
        insertCalendar();
    }

    public void toCancel(View view) {
//        cancelTip();
        delete();
    }


    private void setAlarm() {
        long time;
        // 获得日历实例
        Calendar calendar = Calendar.getInstance();
        int nYear = calendar.get(Calendar.YEAR);
        int nMonth = calendar.get(Calendar.MONTH);
        int nDayofMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        calendar.set(nYear, nMonth, nDayofMonth, hour, minute, second);
//        calendar.setTimeInMillis(System.currentTimeMillis());
        long currTime = calendar.getTimeInMillis();
        long tipTime = 0;
        for (int i = 0; i < 3; i++) {
            tipTime += 30 * 1000;
            LogUtils.i(TAG, "i:" + i + "当前时间:" + currTime);
            LogUtils.i(TAG, "i:" + i + "定时时间:" + tipTime);

            /* 建立Intent和PendingIntent，来调用目标组件 */
            Intent intent = new Intent(this, CallAlarm.class);
            intent.setAction(activeId[i] + "");
            intent.setType(activeId[i] + "");
            intent.setData(Uri.EMPTY);
            intent.addCategory(activeId[i] + "");
            intent.setClass(this, CallAlarm.class);
            intent.putExtra("_id", 0);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, activeId[i], intent, 0);
            AlarmManager am;
            /* 获取闹钟管理的实例 */
            am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
            time = currTime + tipTime;
            calendar.setTimeInMillis(time);
            LogUtils.i(TAG, "提醒时间:" + calendar.getTime());
            /* 设置闹钟 */
            am.set(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(), pendingIntent);
        }
        MyUtil.showToast(getApplicationContext(), "设置成功");
    }

    private void cancelTip() {
        for (int i = 0; i < 3; i++) {
                /* 建立Intent和PendingIntent，来调用目标组件 */
            Intent intent = new Intent(this, CallAlarm.class);
            intent.setAction(activeId[i] + "");
            intent.setType(activeId[i] + "");
            intent.setData(Uri.EMPTY);
            intent.addCategory(activeId[i] + "");
            intent.setClass(this, CallAlarm.class);
            intent.putExtra("_id", 0);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, activeId[i], intent, 0);
            AlarmManager am;
                /* 获取闹钟管理的实例 */
            am = (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
                /* 取消闹钟 */
            am.cancel(pendingIntent);
        }
        MyUtil.showToast(getApplicationContext(), "取消成功");

    }


    private void insertCalendar() {
        String calId = "";
//        queryUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
        Cursor userCursor = getContentResolver().query(Uri.parse(calanderURL), null, Calendars.ACCOUNT_NAME + "='ohmygod@gmail.com'", null, null);

        if (userCursor.getCount() > 0) {
            userCursor.moveToFirst();
            calId = userCursor.getString(userCursor.getColumnIndex("_id"));
        } else
            initCalendars();
        LogUtils.i(TAG, "calId:" + calId);
        ContentValues event = new ContentValues();
        event.put("title", "天呐开抢");
        event.put("description", "快要开始抢购了");
        event.put("calendar_id", calId);

        Calendar mCalendar = Calendar.getInstance();
        long start = 0;
        long end = 0;
        try {
            mCalendar.setTime(MyUtil.ConverToDate("2016-01-14 15:50:00"));
            start = mCalendar.getTime().getTime();
            mCalendar.setTime(MyUtil.ConverToDate("2016-01-14 16:00:00"));
            end = mCalendar.getTime().getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        event.put("dtstart", start);
        event.put("dtend", end);
        event.put("hasAlarm", 1);
        event.put("eventTimezone", TimeZone.getDefault().getID().toString());  //这个是时区，必须有，

        Uri newEvent = getContentResolver().insert(Uri.parse(calanderEventURL), event);

        long id = Long.parseLong(newEvent.getLastPathSegment());

        ContentValues values = new ContentValues();

        values.put(CalendarContract.Reminders.EVENT_ID, id);
        values.put(CalendarContract.Reminders.MINUTES, 5);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        getContentResolver().insert(Uri.parse(calanderRemiderURL), values);
        MyUtil.showToast(getApplicationContext(), "设置成功");
    }

    private void delete() {
        getContentResolver().delete(Uri.parse(calanderURL), Calendars.ACCOUNT_NAME + "='ohmygod@gmail.com'", null);
        MyUtil.showToast(getApplicationContext(), "取消成功");
    }


    //添加账户
    private void initCalendars() {

        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(Calendars.NAME, "ohmygod");

        value.put(Calendars.ACCOUNT_NAME, "ohmygod@gmail.com");
        value.put(Calendars.ACCOUNT_TYPE, "com.android.exchange");
        value.put(Calendars.CALENDAR_DISPLAY_NAME, "ohmygod");
        value.put(Calendars.VISIBLE, 1);
        value.put(Calendars.CALENDAR_COLOR, -9206951);
        value.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER);
        value.put(Calendars.SYNC_EVENTS, 1);
        value.put(Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(Calendars.OWNER_ACCOUNT, "ohmygod@gmail.com");
        value.put(Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Calendars.CONTENT_URI;
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(Calendars.ACCOUNT_NAME, "ohmygod@gmail.com")
                .appendQueryParameter(Calendars.ACCOUNT_TYPE, "com.android.exchange")
                .build();

        getContentResolver().insert(calendarUri, value);
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
        UMImage localImage = new UMImage(this, picUrl);

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
//        String appId = "wx55e834ca0a0327a6";
//        String appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.i(TAG, "onActivityResult");
        /** 使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
    //    @Override
//    protected void initView() {
//        startTxt = (TextView) findViewById(R.id.txt_start);
//        endTxt = (TextView) findViewById(R.id.txt_end);
//        bezierView = (BezierView) findViewById(R.id.BezierView);
//    }

    //自定义一个类，继承View
    class MyCustomView extends View {

        public MyCustomView(Context context) {
            super(context);
            //以文件流的方式读取文件
            mMovie = Movie.decodeStream(getResources().openRawResource(R.raw.gif1));
        }

        @Override
        protected void onDraw(Canvas canvas) {

            long curTime = android.os.SystemClock.uptimeMillis();
            //第一次播放
            if (mMovieStart == 0) {
                mMovieStart = curTime;
            }

            if (mMovie != null) {
                int duration = mMovie.duration();

                int relTime = (int) ((curTime - mMovieStart) % duration);
                mMovie.setTime(relTime);
                mMovie.draw(canvas, 0, 0);

                //强制重绘
                invalidate();

            }
            super.onDraw(canvas);
        }
    }

    protected void initView() {
//        rootView = (LinearLayout) findViewById(R.id.rootView);
        replyLlayout = (LinearLayout) findViewById(R.id.rlayout_reply);
        replyEdt = (EditText) findViewById(R.id.edt_reply);
        replyBtn = (Button) findViewById(R.id.btn_reply);
        list = (ListView) findViewById(R.id.list_show);
        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtil.showToast(getApplicationContext(), "点击回复按钮");
            }
        });

        replyLlayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
//                replyLlayout.getWindowebViewisibleDisplayFrame(r);
                screenHeight = replyLlayout.getRootView().getHeight();
                keyBoardHeight = screenHeight - (r.bottom - r.top);
                boolean visible = keyBoardHeight > screenHeight / 3;
//                LogUtils.i(TAG, "键盘不可见:" + "screenHeight:" + screenHeight + "r.bottom:" + r.bottom + "r.top:" + r.top + "keyBoardHeight" + keyBoardHeight);
                if (visible) {
                    moveList(selectPosition);
//                    replyLlayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    LogUtils.i(TAG, "键盘可见:" + "screenHeight:" + screenHeight + "r.bottom:" + r.bottom + "r.top:" + r.top + "keyBoardHeight" + keyBoardHeight);
                }
            }
        });


//        showAdapter = new ShowAdapter(this, new ReplyListener() {
//            @Override
//            public void onResult(String name, int position, View v, ShowBO showBO, int replyPos) {
//                MyUtil.showToast(getApplication(), "onResult 点击:" + name);
//                selectPosition = position;
//                view = v;
//                commentPosition = replyPos;
//                viewTop = view.getTop();
//                LogUtils.i(TAG, "item-->" + "y:" + viewTop);
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//                replyLlayout.setVisibility(View.VISIBLE);
//                MyUtil.showToast(getApplication(), "replyRlayout状态:" + replyLlayout.getVisibility());
//                replyEdt.setFocusable(true);
//                replyEdt.setFocusableInTouchMode(true);
//                replyEdt.requestFocus();
//                isKeyBoardShow = true;
//            }
//        });
        list.setAdapter(showAdapter);

        replyLlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.i(TAG, "点击屏幕");
                if (replyLlayout.getVisibility() == View.VISIBLE) {
                    replyLlayout.setVisibility(View.GONE);
                }
                //隐藏键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                isKeyBoardShow = false;
            }
        });

        testData();
    }

    private void testData() {
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

//        for(int i=0;i<showBOList.size();i++){
//            LogUtils.i(TAG,"i:"+i+"itemList:"+showBOList.get(i).getComments());
//        }
        showAdapter.setItems(showBOList);
    }

    private void moveList(int selectPosition) {
        if (showAdapter == null)
            return;

        LogUtils.i(TAG, "selectPosition:" + selectPosition);
        if (isKeyBoardShow) {
            LogUtils.i(TAG, "moveList:" + selectPosition + "count:" + showAdapter.getCount());
            LogUtils.i(TAG, "screenHeight:" + screenHeight + "keyBoardHeight:" + keyBoardHeight);

            y1 = screenHeight - keyBoardHeight;
            itemTop = list.getChildAt(selectPosition).getTop();
            LogUtils.i(TAG, "viewTop:" + viewTop + "list.getChildAt(selectPosition).getTop():" + itemTop);
            y2 = viewTop - list.getChildAt(selectPosition).getTop();
            int off = y1 - y2;
            LogUtils.i(TAG, "off:" + off + "y1:" + y1 + "y2:" + y2);

            list.setSelectionFromTop(selectPosition, off);
        }
        isKeyBoardShow = false;

    }
//    public void toTest(View view) {
//        int[] location = new int[2];
//        startTxt.getLocationOnScreen(location);
//
//        start = new Point();
//        start.x = location[0];
//        start.y = location[1];
//
//        location = new int[2];
//        endTxt.getLocationOnScreen(location);
//
//        end = new Point();
//        end.x = location[0];
//        end.y = location[1];
//
////        bezierView.reDraw(start, end);
//        Log.i("lmf", "start:" + start + "end" + end);
//    }

    public void toTest(View view) {
        // 是否只有已登录用户才能打开分享选择页
        mController.getConfig().setPlatforms(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT, SHARE_MEDIA.SMS);
        mController.openShare(this, false);
//        if (firstFlag) {
//            decrease();
//            firstFlag = false;
//        }
//        percentTxt.setText(percent + "%");
//        if (percent < 100) {
//            percent++;
//            if (percent % (100 / MyConfig.house.length) == 0) {
//                imageView.setImageResource(MyConfig.house[index++]);
//            }
//        } else if (percent == 100) {
//            MyUtil.showToast(getApplicationContext(), "抢购完成");
//        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (percent < 100) {
                        percentTxt.setText(percent + "%");
                        percent--;
                        if (percent % (100 / MyConfig.house.length) == 0) {
                            if (index > 0)
                                imageView.setImageResource(MyConfig.house[--index]);
                        }
                    }

                    break;
            }
        }
    };

    private void decrease() {
        TimerTask t = new TimerTask() {
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        timer = new Timer();
        timer.schedule(t, 1000, 1000);
    }
}
