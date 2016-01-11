package com.poomoo.ohmygod.view.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.AdBO;
import com.poomoo.model.GrabBO;
import com.poomoo.model.MessageBO;
import com.poomoo.model.MessageInfoBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.WinnerBO;
import com.poomoo.ohmygod.adapter.TimeAdapter;
import com.poomoo.ohmygod.alarm.CallAlarm;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.GrabAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.database.MessageInfo;
import com.poomoo.ohmygod.listeners.AdvertisementListener;
import com.poomoo.ohmygod.listeners.AlarmtListener;
import com.poomoo.ohmygod.utils.DateTimePickDialogUtil;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;
import com.poomoo.ohmygod.view.activity.CityListActivity;
import com.poomoo.ohmygod.view.activity.CommodityInformation2Activity;
import com.poomoo.ohmygod.view.activity.CommodityInformationActivity;
import com.poomoo.ohmygod.view.activity.MainFragmentActivity;
import com.poomoo.ohmygod.view.activity.WinInformationActivity;
import com.poomoo.ohmygod.view.custom.NoScrollListView;
import com.poomoo.ohmygod.view.custom.SlideShowView;
import com.poomoo.ohmygod.view.custom.UpMarqueeTextView;
import com.poomoo.ohmygod.view.custom.pullDownScrollView.PullDownElasticImp;
import com.poomoo.ohmygod.view.custom.pullDownScrollView.PullDownScrollView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/11 16:26.
 */
public class GrabFragment extends BaseFragment implements OnItemClickListener, OnClickListener, PullDownScrollView.RefreshListener {
    private View mMenuView;
    private ListView timeList;
    private PullDownScrollView refreshableView;
    private LinearLayout remindLlayout;
    //    private LinearLayout remindLlayout2;
    private LinearLayout middleLlayout;
    private LinearLayout currCityLlayout;
    //    private LinearLayout noWinningInfoLlayout;
    private TextView noWinningInfoTxt;
    private RelativeLayout avatarRlayout;
    private RelativeLayout winnerRlayout;
    private ImageView avatarImg;
    private TextView currCityTxt;
    private TextView countTxt;
    private TextView tipTxt;
    private UpMarqueeTextView marqueeTextView;
    private NoScrollListView listView;
    private SlideShowView slideShowView;
    public static GrabAdapter adapter;
    private String[] urls;
    private AdBO adBO;
    private List<AdBO> adBOList = new ArrayList<>();
    public static List<GrabBO> grabBOList = new ArrayList<>();
    private List<WinnerBO> winnerBOList = new ArrayList<>();
    private int index = 0;
    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;

    private String currCity = "";
    public int informCount = 0;//通知的数量
    private List<MessageBO> messageBOList = new ArrayList<>();
    private boolean isFirst = true;//true第一次进入
    private boolean isWinListFirst = true;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");//下拉时间格式
    private DisplayImageOptions defaultOptions;
    private MessageInfo messageInfo;
    private List<MessageInfo> infoList = new ArrayList<>();
    public static GrabFragment instance;
    private PopupWindow timePopupWindow;
    private TimeAdapter timeAdapter;
    private boolean isShow = false;//提醒按钮 true-展开  false-隐藏
    private boolean tipFlag = true;//true-提醒 false-取消提醒
    private boolean existCountDown = false;//true-有倒计时

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grab, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        instance = this;
        initView();
    }

    private void initView() {
        mMenuView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_time, null);
        timeList = (ListView) mMenuView.findViewById(R.id.list_time);
        refreshableView = (PullDownScrollView) getActivity().findViewById(R.id.refresh_grab);
        avatarRlayout = (RelativeLayout) getActivity().findViewById(R.id.rlayout_grab_avatar);
        winnerRlayout = (RelativeLayout) getActivity().findViewById(R.id.rlayout_grab_winner);
        currCityLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_currCity);
//        noWinningInfoLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_noWinningInfo);
        noWinningInfoTxt = (TextView) getActivity().findViewById(R.id.txt_noWinningInfo);
        currCityTxt = (TextView) getActivity().findViewById(R.id.txt_currCity);
        countTxt = (TextView) getActivity().findViewById(R.id.txt_inform_count);
        tipTxt = (TextView) getActivity().findViewById(R.id.txt_tip);
        avatarImg = (ImageView) getActivity().findViewById(R.id.img_grab_winner);
        marqueeTextView = (UpMarqueeTextView) getActivity().findViewById(R.id.txt_winnerInfo);
        listView = (NoScrollListView) getActivity().findViewById(R.id.list_grab);
        slideShowView = (SlideShowView) getActivity().findViewById(R.id.flipper_ad);
        remindLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_remind);
        middleLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_middle);


        //初始化下拉刷新
        refreshableView.setRefreshListener(this);
        refreshableView.setPullDownElastic(new PullDownElasticImp(getActivity()));

        timeAdapter = new TimeAdapter(getActivity());
        List<Integer> integerList = new ArrayList<>();
        int len = MyConfig.time.length;
        for (int i = 0; i < len; i++)
            integerList.add(MyConfig.time[i]);
        timeAdapter.setItems(integerList);
        timeList.setAdapter(timeAdapter);
        timeList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                long currTime = calendar.getTimeInMillis();
//                LogUtils.i(TAG, "当前时间:" + currTime);
                long tipTime = MyConfig.time[position] * 60 * 1000;
                long leftTime;
                int len = GrabFragment.adapter.getCountDownUtils().size();
                if (len > 0) {
                    for (int i = 0; i < len; i++) {
                        long countDownTime = GrabFragment.adapter.getCountDownUtils().get(i).getMillisUntilFinished();
                        if (countDownTime > 0) {
                            existCountDown = true;
                            leftTime = countDownTime - tipTime;
                            LogUtils.i(TAG, "i:" + i + "当前时间:" + currTime);
                            LogUtils.i(TAG, "i:" + i + "倒计时间:" + countDownTime);
                            LogUtils.i(TAG, "i:" + i + "提醒时间:" + tipTime);
                            LogUtils.i(TAG, "i:" + i + "剩余时间:" + leftTime);


                            /* 建立Intent和PendingIntent，来调用目标组件 */
                            Intent intent = new Intent(getActivity(), CallAlarm.class);
                            intent.setAction(i + "");
                            intent.setType(i + "");
                            intent.setData(Uri.EMPTY);
                            intent.addCategory(i + "");
                            intent.setClass(getActivity(), CallAlarm.class);
                            intent.putExtra("_id", 0);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), i, intent, 0);
                            AlarmManager am;
                            /* 获取闹钟管理的实例 */
                            am = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
                            time = currTime + leftTime;
                            LogUtils.i(TAG, "提醒时间:" + time);
                            calendar.setTimeInMillis(time);
                            /* 设置闹钟 */
                            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                            LogUtils.i(TAG, "calendar:" + calendar.getTime());
                        }
                    }

                    if (existCountDown)
                        MyUtil.showToast(getActivity().getApplicationContext(), "亲，我将在每次活动开枪前" + MyConfig.time[position] + "分钟提醒您！");
                    else {
                        time = currTime + tipTime;
                        calendar.setTimeInMillis(time);
                        LogUtils.i(TAG, "calendar:" + calendar.getTime());
                            /* 建立Intent和PendingIntent，来调用目标组件 */
                        Intent intent = new Intent(getActivity(), CallAlarm.class);
                        intent.setAction("only");
                        intent.setType("only");
                        intent.setData(Uri.EMPTY);
                        intent.addCategory("only");
                        intent.setClass(getActivity(), CallAlarm.class);
                        intent.putExtra("_id", 0);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
                        AlarmManager am;
                            /* 获取闹钟管理的实例 */
                        am = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
                            /* 设置闹钟 */
                        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        MyUtil.showToast(getActivity().getApplicationContext(), "亲，我将在每次活动开枪前" + MyConfig.time[position] + "分钟提醒您！");
                    }
                    tipFlag = false;
                    setTipText(tipFlag);
                } else
                    MyUtil.showToast(getActivity().getApplicationContext(), "当前没有活动需要提醒");

                timePopupWindow.dismiss();
            }
        });
        initPopWindow();

        winnerRlayout.setOnClickListener(GrabFragment.this);
        currCityLlayout.setOnClickListener(this);
        remindLlayout.setOnClickListener(this);
        adapter = new GrabAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        LogUtils.i(TAG, "定位城市:" + application.getLocateCity());
        if (TextUtils.isEmpty(application.getLocateCity())) {
            mLocationClient = new LocationClient(getActivity().getApplicationContext());
            mMyLocationListener = new MyLocationListener();
            mLocationClient.registerLocationListener(mMyLocationListener);
            initLocation();
            mLocationClient.start();
        } else {
            currCity = application.getLocateCity();
            currCityTxt.setText(application.getLocateCity());
            application.setLocateCity(application.getLocateCity());
            application.setCurrCity(application.getLocateCity());
            getAd();
            getInform();
            getGrabList(false);
            getWinnerList();
        }

        defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.drawable.ic_avatar) //
                .showImageOnFail(R.drawable.ic_avatar) //
                .cacheInMemory(true) //
                .cacheOnDisk(false) //
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//

        tipFlag = (boolean) SPUtils.get(getActivity().getApplicationContext(), getString(R.string.sp_tipFlag), true);
        setTipText(tipFlag);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    SpannableString spannableString;
                    if (winnerBOList.size() == 0)
                        return;
                    if (index >= winnerBOList.size())
                        index = 0;
                    if (!TextUtils.isEmpty(winnerBOList.get(index).getWinNickName()))
                        spannableString = new SpannableString("获奖用户: " + winnerBOList.get(index).getWinNickName() + "  获奖时间: " + winnerBOList.get(index).getPlayDt() + "  商品名称:" + winnerBOList.get(index).getGoodsName() + "  电话:" + MyUtil.hiddenTel(winnerBOList.get(index).getWinTel()));
                    else
                        spannableString = new SpannableString("  获奖时间: " + winnerBOList.get(index).getPlayDt() + "  商品名称:" + winnerBOList.get(index).getGoodsName() + "  电话:" + MyUtil.hiddenTel(winnerBOList.get(index).getWinTel()));

                    marqueeTextView.setText(spannableString + "");
                    avatarImg.setImageResource(R.drawable.ic_avatar);
                    ImageLoader.getInstance().displayImage(winnerBOList.get(index).getHeadPic(), avatarImg, defaultOptions);
                    index++;
                    if (index == winnerBOList.size())
                        index = 0;
                    break;
            }
        }
    };

    private void getAd() {
        this.appAction.getAdvertisement(application.getCurrCity(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                Log.i(TAG, "data:" + data.getObjList().toString());
                adBOList = data.getObjList();
                int len = adBOList.size();
                urls = new String[len];
                for (int i = 0; i < len; i++) {
                    adBO = new AdBO();
                    adBO = adBOList.get(i);
                    urls[i] = adBO.getPicture();
                    Log.i(TAG, urls[i]);
                }
                slideShowView.setPics(urls, new AdvertisementListener() {
                    @Override
                    public void onResult(int position) {
                        if (!MyUtil.isLogin(getActivity()))
                            return;
                        int activeId = adBOList.get(position).getActiveId();
                        int typeId = 4;
                        LogUtils.i(TAG, "activeId:" + activeId);
                        LogUtils.i(TAG, "typeId:" + typeId);
                        Bundle bundle = new Bundle();
                        bundle.putInt(getString(R.string.intent_activeId), activeId);
                        bundle.putString(getString(R.string.intent_parent), getString(R.string.intent_ad));
                        openActivity(CommodityInformationActivity.class, bundle);
                    }
                });
            }

            @Override
            public void onFailure(int errorCode, String message) {

            }
        });
    }

    private void getGrabList(final boolean isRefreshable) {
        this.appAction.getGrabList(application.getCurrCity(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                if (isRefreshable)
                    refreshableView.finishRefresh(format.format(new Date(System.currentTimeMillis())));

                winnerRlayout.setVisibility(View.VISIBLE);
                remindLlayout.setVisibility(View.VISIBLE);
                slideShowView.setVisibility(View.VISIBLE);
//                hideFloatingActionButton();
                grabBOList = data.getObjList();
                int len = grabBOList.size();
                for (int i = 0; i < len; i++)
                    LogUtils.i("lmf", grabBOList.get(i).getGoodsName() + "的倒计时时间:" + grabBOList.get(i).getStartCountdown());
                if (len > 0)
                    adapter.setItems(grabBOList);
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (isRefreshable)
                    refreshableView.finishRefresh(format.format(new Date(System.currentTimeMillis())));

                slideShowView.setVisibility(View.GONE);
                winnerRlayout.setVisibility(View.GONE);
//                noWinningInfoLlayout.setVisibility(View.GONE);
                remindLlayout.setVisibility(View.GONE);
                MyUtil.showToast(application.getApplicationContext(), "当前城市:" + application.getCurrCity() + " 没有开启活动");
            }
        });
    }

    private void getWinnerList() {
        winnerRlayout.setClickable(false);
        avatarRlayout.setVisibility(View.GONE);
        noWinningInfoTxt.setVisibility(View.VISIBLE);
        this.appAction.getWinnerList(application.getCurrCity(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                winnerRlayout.setClickable(true);
                noWinningInfoTxt.setVisibility(View.GONE);
                avatarRlayout.setVisibility(View.VISIBLE);
                marqueeTextView.setVisibility(View.VISIBLE);
                winnerBOList = data.getObjList();
                if (isWinListFirst) {
                    TimerTask t = new TimerTask() {
                        public void run() {
                            handler.sendEmptyMessage(1);
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(t, 0, 15 * 1000);
                    isWinListFirst = false;
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                marqueeTextView.setText("今日没有中奖信息");
            }
        });
    }

    private void getInform() {
        //--1：注册声明，2：游戏规则声明，3返现声明，4提现帮助，5公共消息,6签到声明,7关于,8站内消息,9用户帮助
        LogUtils.i(TAG, "getInform");
        this.appAction.getMessages("5", 1, MyConfig.PAGESIZE, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {

                messageBOList = data.getObjList();
                int len = messageBOList.size();
                LogUtils.i(TAG, "getInform成功:" + data.getObjList() + "  informCount:" + informCount);
                if (len > 0) {
                    countTxt.setVisibility(View.VISIBLE);
                    for (int i = 0; i < len; i++) {
                        messageInfo = new MessageInfo();
                        messageInfo.setStatementId(messageBOList.get(i).getStatementId());
                        messageInfo.setStatus(false);
                        infoList.add(messageInfo);
                        MyUtil.insertMessageInfo(infoList);
                    }
                    updateInfoCount();
                }
                MainFragmentActivity.messageBOList = messageBOList;
                getInfo(messageBOList.get(0).getStatementId());
            }

            @Override
            public void onFailure(int errorCode, String message) {
            }
        });
    }

    public void updateInfoCount() {
        informCount = MyUtil.getUnReadInfoCount();
        if (informCount == 0)
            countTxt.setVisibility(View.GONE);
        else
            countTxt.setText(informCount + "");
    }

    private void getInfo(int statementId) {
        this.appAction.getMessageInfo(statementId + "", new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                LogUtils.i(TAG, "getInfo成功");
                MessageInfoBO messageInfoBO = (MessageInfoBO) data.getObj();
                MainFragmentActivity.messageInfoBO = messageInfoBO;
            }

            @Override
            public void onFailure(int errorCode, String message) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!MyUtil.isLogin(getActivity()))
            return;

//        if (!application.getLocateCity().equals(application.getCurrCity())) {
//            MyUtil.showToast(getActivity().getApplicationContext(), application.getLocateCity() + "不能参加" + application.getCurrCity() + "的活动!");
//            return;
//        }
        if (grabBOList.get(position).getStatus() == 1) {
//            LogUtils.i("lmf", "首页时间:" + adapter.getCountDownUtils().get(position).getMillisUntilFinished() + "");
            Bundle pBundle = new Bundle();
            pBundle.putInt(getString(R.string.intent_activeId), grabBOList.get(position).getActiveId());
            pBundle.putInt(getString(R.string.intent_typeId), grabBOList.get(position).getTypeId());
            pBundle.putInt(getString(R.string.intent_position), position);
            pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_info));
            pBundle.putString(getString(R.string.intent_activityName), grabBOList.get(position).getTitle());
            openActivity(CommodityInformationActivity.class, pBundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.intent_activeId), grabBOList.get(position).getActiveId());
            bundle.putString(getString(R.string.intent_activityName), grabBOList.get(position).getTitle());
            openActivity(CommodityInformation2Activity.class, bundle);
        }

    }

    @Override
    public void onClick(View v) {
        if (!MyUtil.isLogin(getActivity()))
            return;
        switch (v.getId()) {
            case R.id.llayout_currCity:
                openActivity(CityListActivity.class);
                break;

            case R.id.rlayout_grab_winner:
                openActivity(WinInformationActivity.class);
                break;

            case R.id.llayout_remind:
//                if (!isShow) {
//                    showFloatingActionButton();//展开
//                } else {
//                    setDate();
//                }
                if (tipFlag)//提醒
                    setDate();
                else//取消提醒
                    cancelTip();
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFirst) {
            LogUtils.i(TAG, "currCity:" + currCity + "application.getCurrCity():" + application.getLocateCity());
            if (!currCity.equals(application.getCurrCity())) {
                currCityTxt.setText(application.getCurrCity());
                grabBOList = new ArrayList<>();
                adapter.setItems(grabBOList);
                getGrabList(false);
                getAd();
                getWinnerList();
            }
            currCity = application.getCurrCity();
        }
        isFirst = false;
    }

    /**
     * 实现实位回调监听
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系，
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setPriority(LocationClientOption.GpsFirst);    // 当gps可用，而且获取了定位结果时，不再发起网络请求，直接返回给用户坐标。这个选项适合希望得到准确坐标位置的用户。如果gps不可用，再发起网络请求，进行定位。
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onRefresh(PullDownScrollView view) {
        getGrabList(true);
        getWinnerList();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            LogUtils.i("location", "location.getCity():" + location.getCity());
            if (!TextUtils.isEmpty(location.getCity())) {
                currCity = location.getCity();
                currCityTxt.setText(location.getCity());
                application.setLocateCity(location.getCity());
                application.setCurrCity(location.getCity());
                getGrabList(false);
                getWinnerList();
                getAd();
                getInform();
            } else
                MyUtil.showToast(getActivity().getApplicationContext(), "定位失败");
            mLocationClient.unRegisterLocationListener(mMyLocationListener);
        }
    }


//    public void hideFloatingActionButton() {
//        isShow = false;
//        float width = remindLlayout.getWidth();
//        float off = middleLlayout.getWidth() - width / 3f;
//        LogUtils.i(TAG, "隐藏:" + width + " " + off);
//        ObjectAnimator translationLeft = ObjectAnimator.ofFloat(remindLlayout, "X", off);
//        AnimatorSet as = new AnimatorSet();
//        as.play(translationLeft);
//        as.setDuration(1000);
//        as.start();
//    }

    public void showFloatingActionButton() {
        isShow = true;
        float width = remindLlayout.getWidth();
        float off = middleLlayout.getWidth() - width;
        ObjectAnimator translationRight = ObjectAnimator.ofFloat(remindLlayout, "X", off);
        AnimatorSet as = new AnimatorSet();
        as.play(translationRight);
        as.setDuration(1000);
        as.start();
    }


    /**
     * 设置闹钟时间
     */
    private void setDate() {
        // 显示窗口
        timePopupWindow.showAtLocation(getActivity().findViewById(R.id.llayout_main),
                Gravity.CENTER, 0, 0); // 设置layout在genderWindow中显示的位置
    }

    private void initPopWindow() {
        timePopupWindow = new PopupWindow(mMenuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        timePopupWindow.setFocusable(true);
        timePopupWindow.setFocusable(true);

        mMenuView.setFocusable(true);
        mMenuView.setFocusableInTouchMode(true);
        mMenuView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                LogUtils.i(TAG, "点击返回键");
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (timePopupWindow != null) {
                        timePopupWindow.dismiss();
                    }
                }
                return true;
            }
        });
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height_top = mMenuView.findViewById(R.id.popup_time_layout).getTop();
                int height_bottom = mMenuView.findViewById(R.id.popup_time_layout).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height_top || y > height_bottom) {
                        timePopupWindow.dismiss();
                    }
                }
                return true;
            }
        });
    }

    /**
     * 取消提醒
     */
    private void cancelTip() {
        int len = GrabFragment.adapter.getCountDownUtils().size();
        for (int i = 0; i < len; i++) {
            long countDownTime = GrabFragment.adapter.getCountDownUtils().get(i).getMillisUntilFinished();
            if (countDownTime > 0) {
                existCountDown = true;
                /* 建立Intent和PendingIntent，来调用目标组件 */
                Intent intent = new Intent(getActivity(), CallAlarm.class);
                intent.setAction(i + "");
                intent.setType(i + "");
                intent.setData(Uri.EMPTY);
                intent.addCategory(i + "");
                intent.setClass(getActivity(), CallAlarm.class);
                intent.putExtra("_id", 0);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), i, intent, 0);
                AlarmManager am;
                /* 获取闹钟管理的实例 */
                am = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
                /* 取消闹钟 */
                am.cancel(pendingIntent);
            }
        }
        if (!existCountDown) {
            /* 建立Intent和PendingIntent，来调用目标组件 */
            Intent intent = new Intent(getActivity(), CallAlarm.class);
            intent.setAction("only");
            intent.setType("only");
            intent.setData(Uri.EMPTY);
            intent.addCategory("only");
            intent.setClass(getActivity(), CallAlarm.class);
            intent.putExtra("_id", 0);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
            AlarmManager am;
            /* 获取闹钟管理的实例 */
            am = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
            /* 取消闹钟 */
            am.cancel(pendingIntent);
        }
        MyUtil.showToast(getActivity().getApplicationContext(), "取消提醒成功");
        tipFlag = true;
        setTipText(tipFlag);
    }

    private void setTipText(boolean flag) {
        if (flag)//提醒
            tipTxt.setText(getString(R.string.label_grab_tip));
        else//取消提醒
            tipTxt.setText(getString(R.string.label_grab_cancelTip));
    }
}
