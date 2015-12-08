package com.poomoo.ohmygod.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.AdBO;
import com.poomoo.model.GrabBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.WinnerBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.GrabAdapter;
import com.poomoo.ohmygod.view.activity.CityListActivity;
import com.poomoo.ohmygod.view.activity.CommodityInformationActivity;
import com.poomoo.ohmygod.view.custom.SlideShowView;
import com.poomoo.ohmygod.view.custom.UpMarqueeTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/11 16:26.
 */
public class GrabFragment extends BaseFragment implements OnItemClickListener, OnClickListener {
    private LinearLayout remindLlayout;
    private LinearLayout currCityLlayout;
    private TextView currCityTxt;
    private UpMarqueeTextView marqueeTextView;
    private ListView listView;
    private SlideShowView slideShowView;
    private GrabAdapter adapter;
    private String[] urls;
    private AdBO adBO;
    private List<GrabBO> grabBOList = new ArrayList<>();
    private List<WinnerBO> winnerBOList = new ArrayList<>();
    private int index = 0;

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
        currCityLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_currCity);
        currCityTxt = (TextView) getActivity().findViewById(R.id.txt_currCity);
        marqueeTextView = (UpMarqueeTextView) getActivity().findViewById(R.id.txt_winnerInfo);
        listView = (ListView) getActivity().findViewById(R.id.list_grab);
        slideShowView = (SlideShowView) getActivity().findViewById(R.id.flipper_ad);
        remindLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_remind);


        currCityLlayout.setOnClickListener(this);
        adapter = new GrabAdapter(getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        getAd();
        getGrabList();
        getWinnerList();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    SpannableString spannableString = new SpannableString("获奖用户: " + winnerBOList.get(index).getWinNickName() + "  获奖时间: " + winnerBOList.get(index).getPlayDt() + "  商品名称:" + winnerBOList.get(index).getGoodsName());
                    marqueeTextView.setText(spannableString + "");
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

    private void getGrabList() {
        Log.i("lmf", "getGrabList");
        this.appAction.getGrabList("贵阳市", new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                Log.i("lmf", "data:" + data.toString());
                grabBOList = data.getObjList();
                adapter.setItems(grabBOList);
            }

            @Override
            public void onFailure(int errorCode, String message) {

            }
        });
    }

    private void getWinnerList() {
        this.appAction.getWinnerList("贵阳市", new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_activeId), grabBOList.get(position).getActiveId());
        pBundle.putLong(getString(R.string.intent_countDownTime), adapter.getCountDownUtils().get(position).getMillisUntilFinished());
        openActivity(CommodityInformationActivity.class, pBundle);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_currCity:
                openActivity(CityListActivity.class);
                break;
        }
    }
}
