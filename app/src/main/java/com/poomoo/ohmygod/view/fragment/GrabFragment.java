package com.poomoo.ohmygod.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.poomoo.model.ResponseBO;
import com.poomoo.model.WinnerBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.GrabAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.activity.CityListActivity;
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
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/11 16:26.
 */
public class GrabFragment extends BaseFragment implements OnItemClickListener, OnClickListener, PullDownScrollView.RefreshListener {
    private PullDownScrollView refreshableView;
    private LinearLayout remindLlayout;
    private LinearLayout currCityLlayout;
    private RelativeLayout avatarRlayout;
    private RelativeLayout winnerRlayout;
    private ImageView avatarImg;
    private TextView currCityTxt;
    private TextView countTxt;
    private UpMarqueeTextView marqueeTextView;
    private NoScrollListView listView;
    private SlideShowView slideShowView;
    private GrabAdapter adapter;
    private String[] urls;
    private AdBO adBO;
    private List<GrabBO> grabBOList = new ArrayList<>();
    private List<WinnerBO> winnerBOList = new ArrayList<>();
    private int index = 0;
    private LocationClient mLocationClient;
    private MyLocationListener mMyLocationListener;

    private String currCity = "";
    private int informCount = 0;//通知的数量
    private List<MessageBO> messageBOList = new ArrayList<>();
    private boolean isFirst = true;//true第一次进入
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");//下拉时间格式

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grab, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {
        refreshableView = (PullDownScrollView) getActivity().findViewById(R.id.refresh_grab);
//        refreshableView.setListViewPosition(4);
        avatarRlayout = (RelativeLayout) getActivity().findViewById(R.id.rlayout_grab_avatar);
        winnerRlayout = (RelativeLayout) getActivity().findViewById(R.id.rlayout_grab_winner);
        currCityLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_currCity);
        currCityTxt = (TextView) getActivity().findViewById(R.id.txt_currCity);
        countTxt = (TextView) getActivity().findViewById(R.id.txt_inform_count);
        avatarImg = (ImageView) getActivity().findViewById(R.id.img_grab_winner);
        marqueeTextView = (UpMarqueeTextView) getActivity().findViewById(R.id.txt_winnerInfo);
        listView = (NoScrollListView) getActivity().findViewById(R.id.list_grab);
        slideShowView = (SlideShowView) getActivity().findViewById(R.id.flipper_ad);
        remindLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_remind);

        //下拉刷新
//        refreshableView.setOnRefreshListener(new PullToRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getGrabList(true);
//            }
//        }, 0);

        //初始化下拉刷新
        refreshableView.setRefreshListener(this);
        refreshableView.setPullDownElastic(new PullDownElasticImp(getActivity()));

        currCityLlayout.setOnClickListener(this);
        winnerRlayout.setOnClickListener(this);
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
            currCityTxt.setText(application.getLocateCity());
            application.setLocateCity(application.getLocateCity());
            application.setCurrCity(application.getLocateCity());
            getAd();
            getInform();
            getGrabList(false);
            getWinnerList();
        }


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    SpannableString spannableString = new SpannableString("获奖用户: " + winnerBOList.get(index).getWinNickName() + "  获奖时间: " + winnerBOList.get(index).getPlayDt() + "  商品名称:" + winnerBOList.get(index).getGoodsName());
                    marqueeTextView.setText(spannableString + "");
                    avatarImg.setImageResource(R.drawable.ic_avatar);
                    LogUtils.i(TAG, "head:" + winnerBOList.get(index).getHeadPic());
                    DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                            .showImageForEmptyUri(R.drawable.ic_avatar) //
                            .showImageOnFail(R.drawable.ic_avatar) //
                            .cacheInMemory(true) //
                            .cacheOnDisk(false) //
                            .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                            .build();//
                    ImageLoader.getInstance().displayImage(winnerBOList.get(index).getHeadPic(), avatarImg, defaultOptions);
                    index++;
                    if (index == winnerBOList.size())
                        index = 0;
                    break;
            }
        }
    };

    private void getAd() {
        this.appAction.getAdvertisement(new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                Log.i(TAG, "data:" + data.getObjList().toString());
                int len = data.getObjList().size();
                urls = new String[len];
                for (int i = 0; i < len; i++) {
                    adBO = new AdBO();
                    adBO = (AdBO) data.getObjList().get(i);
                    urls[i] = adBO.getPicture();
                    Log.i(TAG, urls[i]);
                }
                slideShowView.setPics(urls);
            }

            @Override
            public void onFailure(int errorCode, String message) {

            }
        });
    }

    private void getGrabList(final boolean isRefreshable) {
        Log.i(TAG, "getGrabList:" + application.getCurrCity());
        this.appAction.getGrabList(application.getCurrCity(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                if (isRefreshable) {
//                    refreshableView.finishRefreshing();
                    refreshableView.finishRefresh(format.format(new Date(System.currentTimeMillis())));
                }
                grabBOList = data.getObjList();
//                initTestData(grabBOList);
                adapter.setItems(grabBOList);
            }

            @Override
            public void onFailure(int errorCode, String message) {
                if (isRefreshable) {
//                    refreshableView.finishRefreshing();
                    refreshableView.finishRefresh(format.format(new Date(System.currentTimeMillis())));
                }
                MyUtil.showToast(application.getApplicationContext(), "当前城市:" + application.getCurrCity() + " 没有开启活动");
            }
        });
    }

    private void getWinnerList() {
        winnerRlayout.setClickable(false);
        avatarRlayout.setVisibility(View.GONE);
        this.appAction.getWinnerList(application.getCurrCity(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                winnerRlayout.setClickable(true);
                avatarRlayout.setVisibility(View.VISIBLE);
                winnerBOList = data.getObjList();
                TimerTask t = new TimerTask() {
                    public void run() {
                        handler.sendEmptyMessage(1);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(t, 0, 15 * 1000);
            }

            @Override
            public void onFailure(int errorCode, String message) {

            }
        });
    }

    private void getInform() {
        //--1：注册声明，2：游戏规则声明，3返现声明，4提现帮助，5公共消息,6签到声明,7关于,8站内消息,9用户帮助
        this.appAction.getMessages("5", 1, MyConfig.PAGESIZE, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                LogUtils.i(TAG, "getInform成功:" + data.getObjList());
                messageBOList = data.getObjList();
                informCount = messageBOList.size();
                if (informCount > 0) {
                    countTxt.setVisibility(View.VISIBLE);
                    countTxt.setText(informCount + "");

                }
                MainFragmentActivity.messageBOList = messageBOList;
            }

            @Override
            public void onFailure(int errorCode, String message) {
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        long time = adapter.getCountDownUtils().get(position).getMillisUntilFinished();
//        LogUtils.i(TAG, "剩余时间:" + time);
        if (!application.getLocateCity().equals(application.getCurrCity())) {
            MyUtil.showToast(getActivity().getApplicationContext(), application.getLocateCity() + "不能参加" + application.getCurrCity() + "的活动!");
            return;
        }
//        if (time > 0) {
//            MyUtil.showToast(getActivity().getApplicationContext(), "活动还没开始");
//        } else {
        Bundle pBundle = new Bundle();
        pBundle.putInt(getString(R.string.intent_activeId), grabBOList.get(position).getActiveId());
        pBundle.putLong(getString(R.string.intent_countDownTime), adapter.getCountDownUtils().get(position).getMillisUntilFinished());
        pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_grab));
        openActivity(CommodityInformationActivity.class, pBundle);
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_currCity:
                openActivity(CityListActivity.class);
                break;

            case R.id.rlayout_grab_winner:
                openActivity(WinInformationActivity.class);
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
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            LogUtils.i("location", "location.getCity():" + location.getCity());
            if (!TextUtils.isEmpty(location.getCity())) {
                currCityTxt.setText(location.getCity());
                application.setLocateCity(location.getCity());
                application.setCurrCity(location.getCity());
                getGrabList(false);
                getWinnerList();
            } else
                MyUtil.showToast(getActivity().getApplicationContext(), "定位失败");
            mLocationClient.unRegisterLocationListener(mMyLocationListener);
        }
    }

    private void initTestData(List<GrabBO> grabBOList) {
        for (int i = 0; i < 10; i++) {
            GrabBO grabBO = new GrabBO();
            grabBO.setStartCountdown((i + 1) * 60 * 60 * 1000);
            grabBO.setPicture(i + "");
            grabBOList.add(grabBO);
        }
    }
}
