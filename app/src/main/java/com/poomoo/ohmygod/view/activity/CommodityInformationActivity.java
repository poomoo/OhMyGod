package com.poomoo.ohmygod.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.CommodityBO;
import com.poomoo.model.GrabResultBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.listeners.AdvertisementListener;
import com.poomoo.ohmygod.other.CountDownListener;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.TimeCountDownUtil;
import com.poomoo.ohmygod.view.bigimage.ImagePagerActivity;
import com.poomoo.ohmygod.view.custom.SlideShowView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 商品详情
 * <p/>
 * 作者: 李苜菲
 * 日期: 2015/11/13 16:15.
 */
public class CommodityInformationActivity extends BaseActivity {
    private SlideShowView slideShowView;
    private TextView nameTxt;//商品名称
    private TextView priceTxt;//商品价格
    private TextView startDate;//开抢时间
    private TextView headTimeCountdownTxt;//头部倒计时控件
    private TextView middleTimeCountdownTxt;//中部部倒计时控件
    private WebView commodityWeb;//商品详情
    private WebView activityWeb;//活动声明
    private TextView openActivityTxt;//确认开启活动按钮
    private Button grabBtn;
    private LinearLayout llayout_head_timeCountDown;//顶部倒计时layout
    private LinearLayout llayout_openActivity;//开启活动layout
    private LinearLayout llayout_bottom;//底部倒计时layout
    private LinearLayout llayout_anim;//显示动画
    private ImageView animImg;//动画
    private TextView percentTxt;//百分比
    private ScrollView scrollView;//

    //    private ProgressSeekBar seek;
    private TimeCountDownUtil headTimeCountDownUtil;
    private List<TextView> textViewList;
    private CommodityBO commodityBO;

    //    private static final long TIME = 2 * 24 * 60 * 60 * 1000 + 5 * 60 * 60 * 1000 + 57 * 60 * 1000 + 23 * 1000;//System.currentTimeMillis()+30*1000*24*24 2 * 5 * 57 * 21 * 1000
    private static final long TIME = 10 * 1000;
    private boolean firstFlag = true;
    private long countDownTime;//倒计时时间
    private boolean isOpen = false;//是否开启活动
    private boolean isBegin = false;//活动是否开始
    private int activeId;//--活动编号
    private Timer timer;
    private String PARENT;

    private boolean isSuccess = false;//抢单结果
    private double percent = 0;
    private int index = 0;
    private double animLen;//动画数组大小
    private boolean isScroll = false;
    private double step;//切换图片的步长
    private DecimalFormat df = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_information);

        initView();
        getData();
    }


    protected void initView() {
        initTitleBar();

        nameTxt = (TextView) findViewById(R.id.txt_commodityName);
        priceTxt = (TextView) findViewById(R.id.txt_price);
        startDate = (TextView) findViewById(R.id.txt_startDate);
        slideShowView = (SlideShowView) findViewById(R.id.flipper_commodity);
        headTimeCountdownTxt = (TextView) findViewById(R.id.txt_head_timeCountDown);
        middleTimeCountdownTxt = (TextView) findViewById(R.id.txt_middle_timeCountDown);
        openActivityTxt = (TextView) findViewById(R.id.txt_openActivity);
        commodityWeb = (WebView) findViewById(R.id.web_commodity);
        activityWeb = (WebView) findViewById(R.id.web_activity);
        grabBtn = (Button) findViewById(R.id.btn_grab);
        llayout_head_timeCountDown = (LinearLayout) findViewById(R.id.llayout_head_timeCountDown);
        llayout_openActivity = (LinearLayout) findViewById(R.id.llayout_openActivity);
        llayout_bottom = (LinearLayout) findViewById(R.id.llayout_grab_bottom);
        llayout_anim = (LinearLayout) findViewById(R.id.llayout_anim);
        animImg = (ImageView) findViewById(R.id.img_anim);
        percentTxt = (TextView) findViewById(R.id.txt_percent);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        llayout_openActivity.setVisibility(View.GONE);
        llayout_bottom.setVisibility(View.GONE);


        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return isScroll;
            }
        });

    }

    private void getData() {
//        PARENT = getIntent().getStringExtra(getString(R.string.intent_parent));
//        if (PARENT.equals(getString(R.string.intent_grab))) {
        countDownTime = getIntent().getLongExtra(getString(R.string.intent_countDownTime), 0);
        initCountDown();
//        } else
//            hidden();

        activeId = getIntent().getIntExtra(getString(R.string.intent_activeId), 0);

        showProgressDialog(getString(R.string.dialog_message));
        LogUtils.i(TAG, "activeId:" + activeId);
        this.appAction.getCommodityInformation(application.getUserId(), activeId + "", new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                commodityBO = (CommodityBO) data.getObj();
                initData();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
                finish();
            }
        });
    }

    private void initData() {
        nameTxt.setText(commodityBO.getGoodsName());
        priceTxt.setText("￥" + commodityBO.getPrice());
        startDate.setText(commodityBO.getStartDt());

        int len = commodityBO.getPicList().size();
        String[] urls = new String[len];
        for (int i = 0; i < len; i++) {
            urls[i] = commodityBO.getPicList().get(i);
            LogUtils.i(TAG, "url:" + urls[i]);
        }
        slideShowView.setPics(urls, new AdvertisementListener() {
            @Override
            public void onResult(int position) {
                imageBrowse(position, commodityBO.getPicList());
            }
        });

        //商品详情
        commodityWeb.getSettings().setDefaultTextEncodingName("UTF-8");
        commodityWeb.loadData(commodityBO.getContent(), "text/html; charset=UTF-8", null);// 这种写法可以正确解码
        //活动声明
//        activityWeb.getSettings().setDefaultTextEncodingName("UTF-8");
//        activityWeb.loadData(commodityBO.getStatement(), "text/html; charset=UTF-8", null);// 这种写法可以正确解码

        WebSettings sws = commodityWeb.getSettings();
        sws.setSupportZoom(true);
        sws.setBuiltInZoomControls(true);

        commodityWeb.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
// Activity和Webview根据加载程度决定进度条的进度大小
// 当加载到100%的时候 进度条自动消失
//WebViewProgressActivity.this.setTitle("Loading...");
//WebViewProgressActivity.this.setProgress(progress * 100);
                if (progress == 100) {
                    llayout_openActivity.setVisibility(View.VISIBLE);
                    llayout_bottom.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void initCountDown() {
        textViewList = new ArrayList<>();
        textViewList.add(headTimeCountdownTxt);
        textViewList.add(middleTimeCountdownTxt);
        headTimeCountDownUtil = new TimeCountDownUtil(countDownTime, 1000, textViewList, new CountDownListener() {
            @Override
            public void onFinish(int result) {
                begin();
            }
        });
        headTimeCountDownUtil.start();
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_commodity_information);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    private void hidden() {
//        openActivityTxt.setVisibility(View.GONE);
//        llayout_head_timeCountDown.setVisibility(View.GONE);
//        llayout_bottom.setVisibility(View.GONE);
//    }

    /**
     * 确认开启活动
     *
     * @param view
     */
    public void toOpen(View view) {
        isOpen = true;
        openActivityTxt.setVisibility(View.GONE);
        middleTimeCountdownTxt.setVisibility(View.VISIBLE);
        if (isBegin) {
            grabBtn.setBackgroundResource(R.drawable.selector_grab_button_grab);
            grabBtn.setClickable(true);
//            seek.setThumb(getResources().getDrawable(R.drawable.ic_progressbar_selected));
//            seek.setThumbOffset(0);
        }
    }

    public void begin() {
        isBegin = true;
        if (isOpen) {
            grabBtn.setBackgroundResource(R.drawable.selector_grab_button_grab);
            grabBtn.setClickable(true);
//            seek.setThumb(getResources().getDrawable(R.drawable.ic_progressbar_selected));
//            seek.setThumbOffset(0);
        }

    }

    /**
     * 点击增加进度条进度
     *
     * @param view
     */
    public void toGrab(View view) {
        if (firstFlag) {
            decrease();
            llayout_anim.setVisibility(View.VISIBLE);
            isScroll = true;//禁止scrollview滚动
            animLen = MyConfig.house.length;
            step = Double.parseDouble(df.format(100 / animLen));
            LogUtils.i(TAG, "step:" + step);
            firstFlag = false;
        }
        LogUtils.i(TAG, "点击:" + percent + " index:" + index);
        if (percent == 0) {
            LogUtils.i(TAG, "点击0:" + percent);
            timer.cancel();
            decrease();
            percentTxt.setText(0 + "%");
        }
        LogUtils.i(TAG, "MyUtil.sub(percent, 100):" + MyUtil.sub(percent, 100));
        if (MyUtil.sub(percent, 100) >= 0) {
            LogUtils.i(TAG, "抢购完成:" + df.format(percent));
            percentTxt.setText(100 + "%");
            stop();
        }
        if (MyUtil.sub(percent, 100) < 0) {
            LogUtils.i(TAG, "抢购未完成:" + df.format(percent));

            percentTxt.setText(df.format(percent) + "%");
            percent = MyUtil.add(percent, step);

            if (index < animLen)
                animImg.setImageResource(MyConfig.house[index++]);

        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (percent >= 0 && percent < 100) {
                        if (percent == 0.00) {
                            LogUtils.i(TAG, "时间归0:" + percent);
                            percent = 0;
                            percentTxt.setText(0 + "%");
                            timer.cancel();
                        } else {
                            LogUtils.i(TAG, "percent:" + percent);
                            percentTxt.setText(df.format(percent) + "%");
                        }

                        if (percent > 0.00)
                            percent = MyUtil.sub(percent, step);

                        if (index > 0) {
                            animImg.setImageResource(MyConfig.house[--index]);
                        } else
                            animImg.setImageResource(MyConfig.house[index]);
                    } else if (percent >= 100) {
                        percentTxt.setText(100 + "%");
                        stop();
                    } else
                        percentTxt.setText(0 + "%");

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

    private void stop() {
        animImg.setImageResource(R.drawable.housefailed);
        timer.cancel();
        grabBtn.setBackgroundResource(R.drawable.bg_btn_grab_normal);
        grabBtn.setClickable(false);
    }

    private void submit() {
        showProgressDialog("提交申请中...");
        this.appAction.putGrab(activeId + "", this.application.getUserId(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                GrabResultBO grabResultBO = (GrabResultBO) data.getObj();
                String message;

                if (grabResultBO.getIsWin().equals("true")) {
                    isSuccess = true;
                    message = "恭喜中奖";
//                    MyUtil.showToast(getApplicationContext(), "抢单成功!");
//                    openActivity(EditPersonalInformationActivity.class);
                } else if (grabResultBO.getIsWin().equals("false")) {
                    isSuccess = false;
                    message = "很遗憾,没有中奖,请再接再厉";
//                    MyUtil.showToast(getApplicationContext(), "抢单失败!");
                } else {
                    message = data.getMsg();
//                    MyUtil.showToast(getApplicationContext(), data.getMsg());
                }
                Dialog dialog = new AlertDialog.Builder(CommodityInformationActivity.this).setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isSuccess) {
                            if (!application.getRealNameAuth().equals("1"))
                                openActivity(EditPersonalInformationActivity.class);
                        }
                        finish();
                    }
                }).create();
                dialog.show();

            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
                finish();
            }
        });
    }

    protected void imageBrowse(int position, ArrayList<String> urls2) {
        LogUtils.i(TAG, "position:" + position + " size:" + urls2.size());
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }
}
