package com.poomoo.ohmygod.view.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.CalendarContract;
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
import com.poomoo.model.ActivityTypeBO;
import com.poomoo.model.AdBO;
import com.poomoo.model.GrabBO;
import com.poomoo.model.MessageBO;
import com.poomoo.model.MessageInfoBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.WinnerBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.GrabAdapter;
import com.poomoo.ohmygod.adapter.TimeAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.database.ActivityInfo;
import com.poomoo.ohmygod.database.MessageInfo;
import com.poomoo.ohmygod.listeners.AdvertisementListener;
import com.poomoo.ohmygod.listeners.AlarmtListener;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.activity.CityListActivity;
import com.poomoo.ohmygod.view.activity.CommodityInformation2Activity;
import com.poomoo.ohmygod.view.activity.CommodityInformationActivity;
import com.poomoo.ohmygod.view.activity.MainFragmentActivity;
import com.poomoo.ohmygod.view.activity.WebViewActivity;
import com.poomoo.ohmygod.view.activity.WinInformationActivity;
import com.poomoo.ohmygod.view.custom.NoScrollListView;
import com.poomoo.ohmygod.view.custom.SlideShowView;
import com.poomoo.ohmygod.view.custom.UpMarqueeTextView;
import com.poomoo.ohmygod.view.custom.pullable.PullToRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/11 16:26.
 */
public class GrabFragment extends BaseFragment implements OnItemClickListener, OnClickListener, AlarmtListener, PullToRefreshLayout.OnRefreshListener {
    private View mMenuView;
    private ListView timeList;
    private LinearLayout viewsLlayout;
    private PullToRefreshLayout fragment_grab_layout;
    //    private LinearLayout typeLlayout;
    private LinearLayout currCityLlayout;
    private RelativeLayout avatarRlayout;
    private RelativeLayout winnerRlayout;
    private ImageView avatarImg;
    private TextView currCityTxt;
    private TextView countTxt;
    private TextView browseTxt;
    private UpMarqueeTextView marqueeTextView;
    private NoScrollListView listView;
    private SlideShowView slideShowView;
    private ImageView commodityImg;
    private ImageView serviceImg;

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
    private DisplayImageOptions defaultOptions;
    private MessageInfo messageInfo;
    private List<MessageInfo> infoList = new ArrayList<>();
    private ActivityInfo activityInfo;
    private List<ActivityInfo> activityInfos = new ArrayList<>();
    private List<ActivityTypeBO> activityTypeBOs = new ArrayList<>();
    public static GrabFragment instance;
    private String eventId;
    private String browseNum = "";//总浏览量
    private int currPage = 1;//当前页
    private TimeAdapter timeAdapter;
    private PopupWindow timePopupWindow;
    //设置闹钟需要的参数
    private int activeId;
    private String title;
    private String startDt;
    private String endDt;
    private ImageView imageView;

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
        fragment_grab_layout = (PullToRefreshLayout) getActivity().findViewById(R.id.fragment_grab_layout);

        avatarRlayout = (RelativeLayout) getActivity().findViewById(R.id.rlayout_grab_avatar);
        winnerRlayout = (RelativeLayout) getActivity().findViewById(R.id.rlayout_grab_winner);
        currCityLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_currCity);
        currCityTxt = (TextView) getActivity().findViewById(R.id.txt_currCity);
        countTxt = (TextView) getActivity().findViewById(R.id.txt_inform_count);
        browseTxt = (TextView) getActivity().findViewById(R.id.txt_view);//总浏览量
        avatarImg = (ImageView) getActivity().findViewById(R.id.img_grab_winner);
        marqueeTextView = (UpMarqueeTextView) getActivity().findViewById(R.id.txt_winnerInfo);
        listView = (NoScrollListView) getActivity().findViewById(R.id.list_grab);
        slideShowView = (SlideShowView) getActivity().findViewById(R.id.flipper_ad);
        viewsLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_views);
//        typeLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_type);
        commodityImg = (ImageView) getActivity().findViewById(R.id.img_commodity);
        serviceImg = (ImageView) getActivity().findViewById(R.id.img_service);
        fragment_grab_layout.setOnRefreshListener(this);
        commodityImg.setOnClickListener(this);
        serviceImg.setOnClickListener(this);

        slideShowView.setVisibility(View.GONE);
        winnerRlayout.setVisibility(View.GONE);
        viewsLlayout.setVisibility(View.GONE);
//        typeLlayout.setVisibility(View.GONE);

        winnerRlayout.setOnClickListener(this);
        currCityLlayout.setOnClickListener(this);
        adapter = new GrabAdapter(getActivity(), this);
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
            getMessage();
            getGrabList(true);
            getWinnerList();
        }

        defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.drawable.ic_avatar) //
                .showImageOnFail(R.drawable.ic_avatar) //
                .cacheInMemory(true) //
                .cacheOnDisk(false) //
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//

        mMenuView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_time, null);
        timeList = (ListView) mMenuView.findViewById(R.id.list_time);
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
                timePopupWindow.dismiss();
                insertCalendar(activeId, title, startDt, endDt, MyConfig.time[position]);
            }
        });
        initPopWindow();
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

                    LogUtils.i(TAG, "滚动:" + spannableString);
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
        this.appAction.getAdvertisement(application.getCurrCity(), 1, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                Log.i(TAG, "广告返回data:" + data.getObjList().toString());
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
                        int activeId;
                        int advId;
                        String title;
                        activeId = adBOList.get(position).getActiveId();
                        advId = adBOList.get(position).getAdvId();
                        title = adBOList.get(position).getTitle();
                        if (activeId <= 0) {
                            Bundle pBundle = new Bundle();
                            pBundle.putInt(getString(R.string.intent_value), advId);
                            pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_ad));
                            pBundle.putString(getString(R.string.intent_title), title);
                            openActivity(WebViewActivity.class, pBundle);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putInt(getString(R.string.intent_activeId), activeId);
                            bundle.putString(getString(R.string.intent_parent), getString(R.string.intent_ad));
                            openActivity(CommodityInformationActivity.class, bundle);
                        }
                    }
                });
            }

            @Override
            public void onFailure(int errorCode, String message) {

            }
        });
    }

    private void getGrabList(final boolean isRefreshable) {
        LogUtils.i(TAG, "getGrabList:" + currPage);
        this.appAction.getGrabList(application.getCurrCity(), currPage, 15, new ActionCallbackListener() {
                    @Override
                    public void onSuccess(ResponseBO data) {
                        if (isRefreshable) {
                            fragment_grab_layout.refreshFinish(PullToRefreshLayout.SUCCEED);
                            grabBOList = new ArrayList<>();
                            grabBOList = data.getObjList();
                            int len = grabBOList.size();
                            if (len > 0) {
                                currPage++;
                                adapter.setItems(grabBOList);
                                for (int i = 0; i < len; i++) {
                                    activityInfo = new ActivityInfo();
                                    activityInfo.setActiveId(grabBOList.get(i).getActiveId());
                                    activityInfo.setFlag(false);
                                    activityInfo.setEventId("");
                                    activityInfos.add(activityInfo);
                                    MyUtil.insertActivityInfo(activityInfos);//活动列表
                                }
                            }
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(data.getOtherData());
                                browseNum = jsonObject.getString("browseNum");
                                browseTxt.setText(browseNum);
                                LogUtils.i(TAG, "browseNum:" + browseNum);
                            } catch (JSONException e) {
                            }
                        } else {
                            fragment_grab_layout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                            grabBOList = data.getObjList();
                            int len = grabBOList.size();
                            if (len > 0) {
                                currPage++;
                                adapter.addItems(grabBOList);
                                for (int i = 0; i < len; i++) {
                                    activityInfo = new ActivityInfo();
                                    activityInfo.setActiveId(grabBOList.get(i).getActiveId());
                                    activityInfo.setFlag(false);
                                    activityInfo.setEventId("");
                                    activityInfos.add(activityInfo);
                                    MyUtil.insertActivityInfo(activityInfos);//活动列表
                                }
                            }
                        }

                        winnerRlayout.setVisibility(View.VISIBLE);
                        viewsLlayout.setVisibility(View.VISIBLE);
                        slideShowView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        if (errorCode == -3) {
                            MyUtil.showToast(getActivity().getApplicationContext(), message);
                            if (isRefreshable)
                                fragment_grab_layout.refreshFinish(PullToRefreshLayout.FAIL);
                            else
                                fragment_grab_layout.loadmoreFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        if (isRefreshable) {
                            fragment_grab_layout.refreshFinish(PullToRefreshLayout.FAIL);
                            slideShowView.setVisibility(View.GONE);
                            winnerRlayout.setVisibility(View.GONE);
//                            typeLlayout.setVisibility(View.GONE);
                            viewsLlayout.setVisibility(View.GONE);
                            MyUtil.showToast(application.getApplicationContext(), "当前城市:" + application.getCurrCity() + " 没有开启活动");
                        } else {
                            if (message.contains("无数据"))
                                fragment_grab_layout.loadmoreFinish(PullToRefreshLayout.NOMORE);
                            else
                                fragment_grab_layout.loadmoreFinish(PullToRefreshLayout.FAIL);
                        }
                    }
                }
        );
    }

    private void getWinnerList() {
        winnerRlayout.setClickable(false);
        avatarRlayout.setVisibility(View.GONE);
        this.appAction.getWinnerList(application.getCurrCity(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                winnerRlayout.setClickable(true);
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

    /**
     * 获取活动类型
     */
//    private void getActivityType() {
//        typeLlayout.setVisibility(View.GONE);
//        this.appAction.getActivityType(new ActionCallbackListener() {
//            @Override
//            public void onSuccess(ResponseBO data) {
//                activityTypeBOs = data.getObjList();
//                typeLlayout.setVisibility(View.VISIBLE);
//                ImageLoader.getInstance().displayImage(activityTypeBOs.get(0).getPicture(), commodityImg);
//                ImageLoader.getInstance().displayImage(activityTypeBOs.get(1).getPicture(), serviceImg);
//            }
//
//            @Override
//            public void onFailure(int errorCode, String message) {
//
//            }
//        });
//    }

    /**
     * 获取公共消息
     */
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
                    for (int i = 0; i < len; i++) {
                        messageInfo = new MessageInfo();
                        messageInfo.setStatementId(messageBOList.get(i).getStatementId());
                        messageInfo.setStatus(false);
                        infoList.add(messageInfo);
                        MyUtil.insertMessageInfo(infoList, 1);//公共消息
                    }
                }
                updateInfoCount();
                MainFragmentActivity.pubMessageBOList = messageBOList;
                getInfo(messageBOList.get(0).getStatementId());
            }

            @Override
            public void onFailure(int errorCode, String message) {
            }
        });
    }

    /**
     * 获取站内信息
     */
    private void getMessage() {
        //--1：注册声明，2：游戏规则声明，3返现声明，4提现帮助，5公共消息,6签到声明,7关于,8站内消息,9用户帮助
        LogUtils.i(TAG, "getMessage");
        this.appAction.getMessages("8", 1, MyConfig.PAGESIZE, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {

                messageBOList = data.getObjList();
                int len = messageBOList.size();
                LogUtils.i(TAG, "getMessage成功:" + data.getObjList() + "  informCount:" + informCount);
                if (len > 0) {
                    for (int i = 0; i < len; i++) {
                        messageInfo = new MessageInfo();
                        messageInfo.setStatementId(messageBOList.get(i).getStatementId());
                        messageInfo.setStatus(false);
                        infoList.add(messageInfo);
                        MyUtil.insertMessageInfo(infoList, 2);//站内消息
                    }
                }
                MainFragmentActivity.innerMessageBOList = messageBOList;
            }

            @Override
            public void onFailure(int errorCode, String message) {
            }
        });
    }

    public void updateInfoCount() {
        informCount = MyUtil.getUnReadInfoCount(1);
        LogUtils.i(TAG, "未读取公共消息条数:" + informCount);
        if (informCount == 0)
            countTxt.setVisibility(View.GONE);
        else {
            countTxt.setVisibility(View.VISIBLE);
            countTxt.setText(informCount + "");
        }

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
        LogUtils.i(TAG, "onItemClick");
        if (!MyUtil.isLogin(getActivity()))
            return;

        if (adapter.getItemList().get(position).getStatus() == 1) {
            Bundle pBundle = new Bundle();
            pBundle.putInt(getString(R.string.intent_activeId), adapter.getItemList().get(position).getActiveId());
            pBundle.putInt(getString(R.string.intent_typeId), adapter.getItemList().get(position).getTypeId());
            pBundle.putInt(getString(R.string.intent_position), position);
            pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_info));
            pBundle.putString(getString(R.string.intent_activityName), adapter.getItemList().get(position).getTitle());
            openActivity(CommodityInformationActivity.class, pBundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.intent_activeId), adapter.getItemList().get(position).getActiveId());
            bundle.putString(getString(R.string.intent_activityName), adapter.getItemList().get(position).getTitle());
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

//            case R.id.img_commodity:
//                if (CommodityFragment.instance == null)
//                    CommodityFragment.instance = new CommodityFragment();
//                switchFragment(CommodityFragment.instance);
//                break;
//
//            case R.id.img_service:
//                if (ServiceFragment.instance == null)
//                    ServiceFragment.instance = new ServiceFragment();
//                switchFragment(ServiceFragment.instance);
//                break;
        }
    }

    /**
     * 切换fragment
     *
     * @param to
     */
    public void switchFragment(Fragment to) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!to.isAdded()) { // 先判断是否被add过
            fragmentTransaction.hide(this).add(R.id.fragment_manage_frameLayout, to); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            fragmentTransaction.hide(this).show(to); // 隐藏当前的fragment，显示下一个
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateInfoCount();
        if (!isFirst) {
            LogUtils.i(TAG, "currCity:" + currCity + "application.getCurrCity():" + application.getCurrCity());
            if (!currCity.equals(application.getCurrCity())) {
                currCityTxt.setText(application.getCurrCity());
                grabBOList = new ArrayList<>();
                adapter.setItems(grabBOList);
                currPage = 1;
                LogUtils.i(TAG, "onResume  getGrabList");
                getGrabList(true);
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
    public void onRefresh() {
        currPage = 1;
        getGrabList(true);
        getWinnerList();
    }

    @Override
    public void onLoadMore() {
        getGrabList(false);
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
                currPage = 1;
                getGrabList(true);
                getWinnerList();
                getAd();
                getInform();
                getMessage();
            } else
                MyUtil.showToast(getActivity().getApplicationContext(), "定位失败");
            mLocationClient.unRegisterLocationListener(mMyLocationListener);
        }
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

    /*设置提醒*/
    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";

    @Override
    public void setAlarm(String title, int activeId, String startDt, String endDt, ImageView imageView) {
        this.title = title;
        this.activeId = activeId;
        this.startDt = startDt;
        this.endDt = endDt;
        this.imageView = imageView;
        if (!MyUtil.isRemind(activeId))
            setDate();
        else
            delete(activeId);
    }

    private void insertCalendar(int activeId, String title, String startDt, String endDt, int mins) {
        String calId = "";
        Cursor userCursor = getActivity().getContentResolver().query(Uri.parse(calanderURL), null, CalendarContract.Calendars.ACCOUNT_NAME + "='ohmygod@gmail.com'", null, null);
        LogUtils.i(TAG, "userCursor.getCount():" + userCursor.getCount());
        if (userCursor.getCount() > 0) {
            userCursor.moveToFirst();
            calId = userCursor.getString(userCursor.getColumnIndex("_id"));
        } else {
            initCalendars();
            userCursor = getActivity().getContentResolver().query(Uri.parse(calanderURL), null, CalendarContract.Calendars.ACCOUNT_NAME + "='ohmygod@gmail.com'", null, null);
            userCursor.moveToFirst();
            calId = userCursor.getString(userCursor.getColumnIndex("_id"));
        }

        LogUtils.i(TAG, "calId:" + calId);
        ContentValues event = new ContentValues();
        event.put("title", title);
        event.put("description", "快要开始抢购了");
        event.put("calendar_id", calId);

        Calendar mCalendar = Calendar.getInstance();
        long start = 0;
        long end = 0;
        try {
            mCalendar.setTime(MyUtil.ConverToDate(startDt));
            start = mCalendar.getTime().getTime();
            mCalendar.setTime(MyUtil.ConverToDate(endDt));
            end = mCalendar.getTime().getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        event.put("dtstart", start);
        event.put("dtend", end);
        event.put("hasAlarm", 1);
        event.put("eventTimezone", TimeZone.getDefault().getID().toString());  //这个是时区，必须有，

        Uri newEvent = getActivity().getContentResolver().insert(Uri.parse(calanderEventURL), event);
        LogUtils.i(TAG, title + ":" + newEvent);

        long id = Long.parseLong(newEvent.getLastPathSegment());
        eventId = Long.toString(id);
        ContentValues values = new ContentValues();

        values.put(CalendarContract.Reminders.EVENT_ID, id);
        values.put(CalendarContract.Reminders.MINUTES, mins);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        getActivity().getContentResolver().insert(Uri.parse(calanderRemiderURL), values);
        MyUtil.showToast(getActivity().getApplicationContext(), "设置成功,将在活动开抢前" + mins + "分钟提醒您!");
        MyUtil.updateActivityInfo(activeId, true, eventId);//更新活动状态
        imageView.setImageResource(R.drawable.ic_grab_tip_yes);
    }

    //添加账户
    private void initCalendars() {
        LogUtils.i(TAG, "添加账户");
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, "ohmygod");

        value.put(CalendarContract.Calendars.ACCOUNT_NAME, "ohmygod@gmail.com");
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, "com.android.exchange");
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "ohmygod");
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, -9206951);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, "ohmygod@gmail.com");
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = CalendarContract.Calendars.CONTENT_URI;
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "ohmygod@gmail.com")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, "com.android.exchange")
                .build();

        getActivity().getContentResolver().insert(calendarUri, value);
    }

    private void delete(int activeId) {
        getActivity().getContentResolver().delete(Uri.parse(calanderEventURL), CalendarContract.Events._ID + "='" + MyUtil.getEventId(activeId) + "'", null);
        MyUtil.showToast(getActivity().getApplicationContext(), "取消提醒成功!");
        MyUtil.updateActivityInfo(activeId, false, "");
        imageView.setImageResource(R.drawable.ic_grab_tip_no);
    }
}
