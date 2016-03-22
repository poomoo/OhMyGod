package com.poomoo.ohmygod.view.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.AdBO;
import com.poomoo.model.GrabBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.GrabAdapter;
import com.poomoo.ohmygod.database.ActivityInfo;
import com.poomoo.ohmygod.listeners.AdvertisementListener;
import com.poomoo.ohmygod.listeners.AlarmtListener;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.activity.CommodityInformation2Activity;
import com.poomoo.ohmygod.view.activity.CommodityInformationActivity;
import com.poomoo.ohmygod.view.activity.WebViewActivity;
import com.poomoo.ohmygod.view.custom.NoScrollListView;
import com.poomoo.ohmygod.view.custom.SlideShowView;
import com.poomoo.ohmygod.view.custom.pullable.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/11 16:26.
 */
public class CommodityFragment extends BaseFragment implements OnItemClickListener, AlarmtListener, PullToRefreshLayout.OnRefreshListener, OnClickListener {
    private PullToRefreshLayout fragment_commodity_layout;
    private ImageView backImg;
    private LinearLayout toServiceLlayout;
    private NoScrollListView listView;
    private SlideShowView slideShowView;
    public static GrabAdapter adapter;
    private String[] urls;
    private AdBO adBO;
    private List<AdBO> adBOList = new ArrayList<>();
    public static List<GrabBO> grabBOList = new ArrayList<>();

    private ActivityInfo activityInfo;
    private List<ActivityInfo> activityInfos = new ArrayList<>();
    public static CommodityFragment instance;
    private String eventId;
    private int currPage = 1;//当前页
    private boolean isFirst = true;
    private String currCity = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_commodity, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        instance = this;
        initView();
    }

    private void initView() {
        fragment_commodity_layout = (PullToRefreshLayout) getActivity().findViewById(R.id.fragment_commodity_layout);

        backImg = (ImageView) getActivity().findViewById(R.id.img_commodity_back);
        toServiceLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_toService);
        listView = (NoScrollListView) getActivity().findViewById(R.id.list_commodity);
        slideShowView = (SlideShowView) getActivity().findViewById(R.id.flipper_ad_commodity);

        fragment_commodity_layout.setOnRefreshListener(this);
        backImg.setOnClickListener(this);
        toServiceLlayout.setOnClickListener(this);

        slideShowView.setVisibility(View.GONE);

        adapter = new GrabAdapter(getActivity(), this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        getAd();
        getGrabList(true);
    }

    private void getAd() {
        this.appAction.getAdvertisement(application.getCurrCity(), 2, new ActionCallbackListener() {
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
        LogUtils.i(TAG, "currPage:" + currPage);
        this.appAction.getActivityById(application.getCurrCity(), 1, currPage, 15, new ActionCallbackListener() {
                    @Override
                    public void onSuccess(ResponseBO data) {
                        if (isRefreshable) {
                            fragment_commodity_layout.refreshFinish(PullToRefreshLayout.SUCCEED);
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
                        } else {
                            fragment_commodity_layout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
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
                        slideShowView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        if (errorCode == -3) {
                            MyUtil.showToast(getActivity().getApplicationContext(), message);
                            if (isRefreshable)
                                fragment_commodity_layout.refreshFinish(PullToRefreshLayout.FAIL);
                            else
                                fragment_commodity_layout.loadmoreFinish(PullToRefreshLayout.FAIL);
                            return;
                        }
                        if (isRefreshable) {
                            fragment_commodity_layout.refreshFinish(PullToRefreshLayout.FAIL);
                            slideShowView.setVisibility(View.GONE);
                            MyUtil.showToast(application.getApplicationContext(), "当前城市:" + application.getCurrCity() + " 没有开启活动");
                        } else {
                            if (message.contains("无数据"))
                                fragment_commodity_layout.loadmoreFinish(PullToRefreshLayout.NOMORE);
                            else
                                fragment_commodity_layout.loadmoreFinish(PullToRefreshLayout.FAIL);
                        }
                    }
                }
        );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
    public void onRefresh() {
        currPage = 1;
        getGrabList(true);
    }

    @Override
    public void onLoadMore() {
        getGrabList(false);
    }

    private static String calanderURL = "content://com.android.calendar/calendars";
    private static String calanderEventURL = "content://com.android.calendar/events";
    private static String calanderRemiderURL = "content://com.android.calendar/reminders";

    @Override
    public void setAlarm(String title, int activeId, String startDt, String endDt, boolean flag) {
        if (flag)
            insertCalendar(activeId, title, startDt, endDt, flag);
        else
            delete(activeId);
    }

    private void insertCalendar(int activeId, String title, String startDt, String endDt, boolean flag) {
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
        values.put(CalendarContract.Reminders.MINUTES, 5);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        getActivity().getContentResolver().insert(Uri.parse(calanderRemiderURL), values);
        MyUtil.showToast(getActivity().getApplicationContext(), "设置成功,将在活动开抢前5分钟提醒您!");
        MyUtil.updateActivityInfo(activeId, flag, eventId);//更新活动状态
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_commodity_back:
                switchFragment(GrabFragment.instance);
                break;

            case R.id.llayout_toService:
                if (ServiceFragment.instance == null)
                    ServiceFragment.instance = new ServiceFragment();
                switchFragment(ServiceFragment.instance);
                break;
        }
    }

    /**
     * 切换fragment
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
        if (!isFirst) {
            if (!currCity.equals(application.getCurrCity())) {
                grabBOList = new ArrayList<>();
                adapter.setItems(grabBOList);
                currPage = 1;
                getGrabList(true);
                getAd();
            }
            currCity = application.getCurrCity();
        }
        isFirst = false;
    }
}
