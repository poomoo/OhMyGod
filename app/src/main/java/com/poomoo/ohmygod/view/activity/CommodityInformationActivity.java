package com.poomoo.ohmygod.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.CommodityBO;
import com.poomoo.model.GrabResultBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.WinningRecordsBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.listeners.AdvertisementListener;
import com.poomoo.ohmygod.other.CountDownListener;
import com.poomoo.ohmygod.utils.Code;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SoundUtil;
import com.poomoo.ohmygod.utils.TimeCountDownUtil;
import com.poomoo.ohmygod.view.bigimage.ImagePagerActivity;
import com.poomoo.ohmygod.view.custom.SlideShowView;
import com.poomoo.ohmygod.view.fragment.GrabFragment;

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
    private View mMenuView;
    private SlideShowView slideShowView;
    private TextView nameTxt;//商品名称
    private TextView priceTxt;//商品价格
    private TextView startDate;//开抢时间
    private TextView headTimeCountdownTxt;//头部倒计时控件
    private TextView middleTimeCountdownTxt;//中部部倒计时控件
    private WebView commodityWeb;//商品详情
    private TextView openActivityTxt;//确认开启活动按钮
    private TextView signInTxt;//我要报名按钮
    private Button grabBtn;
    private LinearLayout llayout_head_timeCountDown;//顶部倒计时layout
    private LinearLayout llayout_openActivity;//开启活动layout
    private LinearLayout llayout_bottom;//底部倒计时layout
    private LinearLayout llayout_anim;//显示动画
    private LinearLayout llayout_cat;//查看手气
    private RelativeLayout rlayout_clickToComplete;//完善资料
    private ImageView animImg;//动画
    private TextView percentTxt;//百分比
    private ScrollView scrollView;//
    private ImageView detailsImg;

    private TimeCountDownUtil headTimeCountDownUtil;
    private List<TextView> textViewList;
    private CommodityBO commodityBO;

    private boolean firstFlag = true;
    private long countDownTime;//倒计时时间
    private boolean isOpen = false;//是否开启活动
    private boolean isBegin = false;//活动是否开始
    private int activeId;//--活动编号
    private String activityName;//活动名字
    private int typeId;//活动分类 1-房子 2-车子 3-装修 4-其他
    private Timer timer;
    private boolean isGrab = false;//是否已经参与了该活动
    private int[] succeedAnim;//成功动画
    private int[] failedAnim;//失败动画

    private double percent = 0;
    private int index = 0;
    private int[] anim;//动画
    private double animLen;//动画数组大小
    private int len = 0;//抢完后的动画数组长度
    private boolean isScroll = false;
    private double step;//切换图片的步长
    private DecimalFormat df = new DecimalFormat("0.00");
    private int animSound;

    private PopupWindow codePopupWindow;
    private boolean isCode = false;//是否通过验证码

    private TextView changeTxt;
    private ImageView codeImg;
    private EditText codeEdt;
    //产生的验证码
    private String realCode;

    private int position;
    private String PARENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_information);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        addActivityToArrayList(this);
        PARENT = getIntent().getStringExtra(getString(R.string.intent_parent));
        LogUtils.i(TAG, "PARENT:" + PARENT);
        activeId = getIntent().getIntExtra(getString(R.string.intent_activeId), 0);
        if (PARENT.equals(getString(R.string.intent_info)))
            position = getIntent().getIntExtra(getString(R.string.intent_position), 0);

        initView();
        getData();

    }


    protected void initView() {
        initTitleBar();

        nameTxt = (TextView) findViewById(R.id.txt_commodityStatement);
        priceTxt = (TextView) findViewById(R.id.txt_price);
        startDate = (TextView) findViewById(R.id.txt_startDate);
        slideShowView = (SlideShowView) findViewById(R.id.flipper_commodity);
        headTimeCountdownTxt = (TextView) findViewById(R.id.txt_head_timeCountDown);
        middleTimeCountdownTxt = (TextView) findViewById(R.id.txt_middle_timeCountDown);
        signInTxt = (TextView) findViewById(R.id.txt_signIn);
        openActivityTxt = (TextView) findViewById(R.id.txt_openActivity);
        commodityWeb = (WebView) findViewById(R.id.web_commodity);
        grabBtn = (Button) findViewById(R.id.btn_grab);
        llayout_head_timeCountDown = (LinearLayout) findViewById(R.id.llayout_head_timeCountDown);
        llayout_openActivity = (LinearLayout) findViewById(R.id.llayout_openActivity);
        llayout_bottom = (LinearLayout) findViewById(R.id.llayout_grab_bottom);
        llayout_anim = (LinearLayout) findViewById(R.id.llayout_anim);
        llayout_cat = (LinearLayout) findViewById(R.id.llayout_catWinnerList);
        animImg = (ImageView) findViewById(R.id.img_anim);
        percentTxt = (TextView) findViewById(R.id.txt_percent);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        rlayout_clickToComplete = (RelativeLayout) findViewById(R.id.rlayout_clickToComplete);
        detailsImg = (ImageView) findViewById(R.id.img_commodityDetail);


        mMenuView = LayoutInflater.from(this).inflate(R.layout.popupwindow_code, null);
        changeTxt = (TextView) mMenuView.findViewById(R.id.txt_change);
        codeEdt = (EditText) mMenuView.findViewById(R.id.et_phoneCodes);
        codeImg = (ImageView) mMenuView.findViewById(R.id.img_showCode);

        //html自适应
        WebSettings webSettings = commodityWeb.getSettings();
//        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setJavaScriptEnabled(true);

        //将验证码用图片的形式显示出来
        codeImg.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode();

        changeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeImg.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode();
            }
        });

        codeEdt.addTextChangedListener(new TextWatcher() {
            int len;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                len = s.length();
                LogUtils.i("code", "len:" + len + " s:" + s.toString());
                if (len == 4) {
                    LogUtils.i("code", "realCode:" + realCode + " s:" + s.toString());
                    if (s.toString().equals(realCode)) {
                        codePopupWindow.dismiss();
                        isCode = true;
                        scrollView.post(new Runnable() {
                            public void run() {
                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);          //滚动到底部
                            }
                        });
                    } else
                        MyUtil.showToast(context, "验证码不对");
                }
            }
        });
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return isScroll;
            }
        });
        initPopWindow();
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_commodity_information);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
    }

    /**
     * 判断是否为升级会员
     */
    private void isMember() {
        typeId = getIntent().getIntExtra(getString(R.string.intent_typeId), -1);
        LogUtils.i(TAG, "isMember  typeId:" + typeId + " 升级会员:" + application.getIsAdvancedUser());
        if (!isOpen)
            if (typeId != 4) {
                if (application.getIsAdvancedUser().equals("1")) {//升级会员
                    signInTxt.setVisibility(View.GONE);
                    openActivityTxt.setVisibility(View.VISIBLE);
                } else {//普通用户
                    signInTxt.setVisibility(View.VISIBLE);
                    openActivityTxt.setVisibility(View.GONE);
                }
            } else {
                signInTxt.setVisibility(View.GONE);
                openActivityTxt.setVisibility(View.VISIBLE);
            }
    }

    private void getData() {
        showProgressDialog(getString(R.string.dialog_message));
        LogUtils.i(TAG, "activeId:" + activeId);
        this.appAction.getCommodityInformation(application.getUserId(), activeId + "", new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                commodityBO = (CommodityBO) data.getObj();
                if (commodityBO.getStatus() == 2) {              //活动已结束
                    isGrab = false;
                    llayout_cat.setVisibility(View.VISIBLE);
                    GrabFragment.grabBOList.get(position).setStatus(2);
                    GrabFragment.adapter.notifyDataSetChanged();
                } else if (commodityBO.getStatus() == 1) {//活动已开始
                    //已参加
                    if (commodityBO.getPlayFlag().equals("1"))
                        isGrab = false;
                    else
                        isGrab = true;
                }
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
        LogUtils.i(TAG, "isGrab:" + isGrab);
        if (isGrab) {
            isMember();
            //房子
            if (typeId == 1) {
                anim = MyConfig.house;
                succeedAnim = MyConfig.houseSuccess;
                failedAnim = MyConfig.houseFailed;
                animSound = SoundUtil.HOUSE;
            }

            //车子
            if (typeId == 2) {
                anim = MyConfig.car;
                succeedAnim = MyConfig.carSuccess;
                failedAnim = MyConfig.carFailed;
                animSound = SoundUtil.CAR;
            }

            //装修
            if (typeId == 3) {
                anim = MyConfig.decorate;
                succeedAnim = MyConfig.decorateSuccess;
                failedAnim = MyConfig.decorateFailed;
                animSound = SoundUtil.DRAW;
            }

            //其他
            if (typeId == 4) {
                anim = MyConfig.box;
                succeedAnim = MyConfig.boxSuccess;
                failedAnim = MyConfig.boxFailed;
                animSound = SoundUtil.OTHER;
            }
        } else {
            llayout_openActivity.setVisibility(View.VISIBLE);
            llayout_bottom.setVisibility(View.VISIBLE);
        }
        initCountDown();
        nameTxt.setText(commodityBO.getGoodsName());
        priceTxt.setText("￥" + commodityBO.getPrice());
        startDate.setText(commodityBO.getStartDt());
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//
        ImageLoader.getInstance().displayImage(commodityBO.getSmallPic(), detailsImg, defaultOptions);

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
        commodityWeb.loadData(commodityBO.getContent(), "text/html; charset=UTF-8", null);// 这种写法可以正确解码
        // 添加js交互接口类，并起别名 imagelistner
        commodityWeb.addJavascriptInterface(new JavascriptInterface(), "imagelistner");
        commodityWeb.setWebViewClient(new MyWebViewClient());
        commodityWeb.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    if (isGrab) {
                        llayout_openActivity.setVisibility(View.VISIBLE);
                        llayout_bottom.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void initCountDown() {
        textViewList = new ArrayList<>();
        textViewList.add(headTimeCountdownTxt);
        textViewList.add(middleTimeCountdownTxt);
        if (PARENT.equals(getString(R.string.intent_info))) {
            LogUtils.i(TAG, "详情页进入");

            headTimeCountDownUtil = GrabFragment.adapter.getCountDownUtils().get(position);
            headTimeCountDownUtil.setTextViewList(textViewList, new CountDownListener() {
                @Override
                public void onFinish(int result) {
                    LogUtils.i(TAG, "倒计时结束");
                    begin();
                }
            });
            if (headTimeCountDownUtil.getMillisUntilFinished() == 0) {
                LogUtils.i(TAG, "倒计时结束2");
                for (TextView textView : textViewList)
                    textView.setText("活动已开始");
                begin();
            }

        } else {
//            for (TextView textView : textViewList)
//                textView.setText("活动已开始");
            headTimeCountDownUtil = new TimeCountDownUtil(commodityBO.getStartCountdown(), MyConfig.COUNTDOWNTIBTERVAL, textViewList, new CountDownListener() {
                @Override
                public void onFinish(int result) {
                    begin();
                }
            });
            headTimeCountDownUtil.start();
        }
    }


    /**
     * 我要报名
     *
     * @param view
     */
    public void toSignIn(View view) {
        openActivity(UpdateMemberInfoActivity.class);
    }

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
        }
    }

    public void begin() {
        isBegin = true;
        if (isOpen) {
            grabBtn.setBackgroundResource(R.drawable.selector_grab_button_grab);
            grabBtn.setClickable(true);
        }

    }

    /**
     * 点击增加进度条进度
     *
     * @param view
     */
    public void toGrab(View view) {
//        if (1 == 1) {
//            llayout_anim.setVisibility(View.VISIBLE);
//            succeedAnim = MyConfig.houseFailed;
//            showSuccessAnim();
//            return;
//        }
//            customDialog customDialog = new customDialog(this);
//            customDialog.showDialog(new DialogResultListener() {
//                @Override
//                public void onFinishDialogResult(int result) {
//                    if (result == 1)
//                        MyUtil.showToast(getApplicationContext(), "点击确定");
//                }
//            });

//        }
        if (!isGrab) {
            Dialog dialog = new AlertDialog.Builder(CommodityInformationActivity.this).setMessage("不能重复参加活动").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }
            ).create();
            dialog.show();
            return;
        }

        if (!isCode) {
            code();
            return;
        }

//        playSound();
        if (firstFlag) {
            decrease();
            llayout_anim.setVisibility(View.VISIBLE);
            isScroll = true;//禁止scrollview滚动
            animLen = anim.length;
            step = Double.parseDouble(df.format(100 / animLen));
//            LogUtils.i(TAG, "step:" + step);
            firstFlag = false;
        }
//        LogUtils.i(TAG, "点击:" + percent + " index:" + index);
        if (percent == 0) {
//            LogUtils.i(TAG, "点击0:" + percent);
            timer.cancel();
            decrease();
            percentTxt.setText(0 + "%");
        }
//        LogUtils.i(TAG, "MyUtil.sub(percent, 100):" + MyUtil.sub(percent, 100));
        if (MyUtil.sub(percent, 100) >= 0) {
//            LogUtils.i(TAG, "抢购完成:" + df.format(percent));
            percentTxt.setText(100 + "%");
            stop();
        }
        if (MyUtil.sub(percent, 100) < 0) {
//            LogUtils.i(TAG, "抢购未完成:" + df.format(percent));

            percentTxt.setText(df.format(percent) + "%");
            percent = MyUtil.add(percent, step);

            if (index < animLen)
                animImg.setImageResource(anim[index++]);

        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (percent >= 0 && percent < 100) {
                        if (percent == 0.00) {
//                            LogUtils.i(TAG, "时间归0:" + percent);
                            percent = 0;
                            percentTxt.setText(0 + "%");
                            timer.cancel();
                        } else {
//                            LogUtils.i(TAG, "percent:" + percent);
                            percentTxt.setText(df.format(percent) + "%");
                        }

                        if (percent > 0.00)
                            percent = MyUtil.sub(percent, step);

                        if (index > 0) {
                            animImg.setImageResource(anim[--index]);
                        } else
                            animImg.setImageResource(anim[index]);
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
        timer.cancel();
        grabBtn.setBackgroundResource(R.drawable.bg_btn_grab_normal);
        grabBtn.setClickable(false);
        submit();
    }

    private void submit() {
        showProgressDialog(getString(R.string.dialog_message));
        this.appAction.putGrab(activeId + "", this.application.getUserId(), new ActionCallbackListener() {
                    @Override
                    public void onSuccess(ResponseBO data) {
                        closeProgressDialog();
                        GrabResultBO grabResultBO = (GrabResultBO) data.getObj();
                        String message;
                        if (grabResultBO.getIsWin().equals("true")) {
                            showSuccessAnim();
                            showToast(true);
                        } else if (grabResultBO.getIsWin().equals("false")) {
                            showToast(false);
                            showFailedAnim();
                            message = "很遗憾,没有中奖,请再接再厉";
                            MyUtil.showToast(getApplicationContext(), message);
                        } else {
                            message = data.getMsg();
                            MyUtil.showToast(getApplicationContext(), message);
                        }
                    }

                    @Override
                    public void onFailure(int errorCode, String message) {
                        closeProgressDialog();
                        MyUtil.showToast(getApplicationContext(), message);
                        finish();
                    }
                }

        );
    }

    protected void imageBrowse(int position, ArrayList<String> urls2) {
        LogUtils.i(TAG, "position:" + position + " size:" + urls2.size());
        Intent intent = new Intent(context, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isMember();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null)
            timer.cancel();
    }

    private void code() {
        // 显示窗口
        codePopupWindow.showAtLocation(this.findViewById(R.id.llayout_commodity),
                Gravity.CENTER, 0, 0); // 设置layout在genderWindow中显示的位置
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        codeEdt.setFocusable(true);
        codeEdt.setFocusableInTouchMode(true);
        codeEdt.requestFocus();

        codeEdt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                LogUtils.i(TAG, "点击返回键");
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (codePopupWindow != null) {
                        codePopupWindow.dismiss();
                        finish();
                        codePopupWindow = null;
                    }
                }
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    return false;
                }
                if (keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_1 || keyCode == KeyEvent.KEYCODE_2 || keyCode == KeyEvent.KEYCODE_3 || keyCode == KeyEvent.KEYCODE_4 || keyCode == KeyEvent.KEYCODE_5 || keyCode == KeyEvent.KEYCODE_6 || keyCode == KeyEvent.KEYCODE_7 || keyCode == KeyEvent.KEYCODE_8 || keyCode == KeyEvent.KEYCODE_9)
                    return false;
                return true;
            }
        });

    }

    private void initPopWindow() {
        codePopupWindow = new PopupWindow(mMenuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        codePopupWindow.setFocusable(true);
        codePopupWindow.setFocusable(true);
    }

    private void showToast(boolean flag) {
        if (flag) {//成功
            rlayout_clickToComplete.setVisibility(View.VISIBLE);
            TextView textView = (TextView) findViewById(R.id.txt_clickToCompleteInfo);
            ImageView imageView = (ImageView) findViewById(R.id.img_close);
            WinningRecordsBO winningRecordsBO = new WinningRecordsBO();
            winningRecordsBO.setTitle(commodityBO.getGoodsName());
            winningRecordsBO.setGetAddress(commodityBO.getGetAddress());
            winningRecordsBO.setGetEndDt(commodityBO.getEndDt());
            winningRecordsBO.setGetRequire(commodityBO.getGetRequire());
            winningRecordsBO.setActiveId(activeId);//不传活动ID不能晒单
            final Bundle bundle = new Bundle();
            bundle.putSerializable(getString(R.string.intent_value), winningRecordsBO);
            if (typeId == 4) {//小宗商品中奖后需要判断是否完善资料
                if (application.getIsAdvancedUser().equals("1")) {//小宗商品中奖后如果是升级会员则不需要完善资料
                    textView.setText(getString(R.string.label_clickToWinningRecords));
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openActivity(WinningRecord2Activity.class, bundle);
                            finish();
                        }
                    });
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rlayout_clickToComplete.setVisibility(View.GONE);
                        }
                    });
                } else {
                    if (MyUtil.isNeedCompleteInfo(application)) {
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                if (application.getIsAdvancedUser().equals("0"))
                                openActivity(CompleteUserInformationActivity.class, bundle);
//                                else
//                                    openActivity(CompleteMemberInformationActivity.class, bundle);
                                finish();
                            }
                        });
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rlayout_clickToComplete.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        textView.setText(getString(R.string.label_clickToWinningRecords));
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openActivity(WinningRecord2Activity.class, bundle);
                                finish();
                            }
                        });
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                rlayout_clickToComplete.setVisibility(View.GONE);
                            }
                        });
                    }
                }

            } else {//大宗商品中奖后直接跳转到中奖记录
                textView.setText(getString(R.string.label_clickToWinningRecords));
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openActivity(WinningRecord2Activity.class, bundle);
                        finish();
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rlayout_clickToComplete.setVisibility(View.GONE);
                    }
                });
            }

        } else {//失败
            rlayout_clickToComplete.setVisibility(View.VISIBLE);
            TextView textView = (TextView) findViewById(R.id.txt_clickToCompleteInfo);
            textView.setText(getString(R.string.label_clickToReturn2));
            ImageView imageView = (ImageView) findViewById(R.id.img_close);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rlayout_clickToComplete.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 查看中奖列表
     *
     * @param view
     */
    public void catWinnerList(View view) {
        activityName = getIntent().getStringExtra(getString(R.string.intent_activityName));
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.intent_activityName), activityName);
        bundle.putInt(getString(R.string.intent_activeId), activeId);
        openActivity(WinnerListActivity.class, bundle);
    }

    // 注入js函数监听
    private void addImageClickListner() {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        commodityWeb.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "var temp='';" +
                "for(var i=0;i<objs.length;i++)  " +
                "{" + "temp+=objs[i].src+';'" + "}" +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(temp,this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    // js通信接口
    class JavascriptInterface {

        @android.webkit.JavascriptInterface
        public void openImage(String imgList, String src) {
            LogUtils.i(TAG, "imgList" + imgList);
            int position = 0;
            String img = src;
            String[] temp = imgList.split(";");
            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i < temp.length; i++) {
                if (img.equals(temp[i]))
                    position = i;
                arrayList.add(temp[i]);
            }
            imageBrowse(position, arrayList);
        }
    }

    // 监听
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            view.getSettings().setJavaScriptEnabled(true);

            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
            addImageClickListner();

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);

        }
    }

    private void showSuccessAnim() {
        LogUtils.i(TAG, "showSucceessAnim");
        index = 0;
        len = succeedAnim.length;
        TimerTask t = new TimerTask() {
            public void run() {
                myHandler.sendEmptyMessage(1);
            }
        };
        timer = new Timer();
        timer.schedule(t, 1000, 200);
    }

    private void showFailedAnim() {
        index = 0;
        len = failedAnim.length;
        TimerTask t = new TimerTask() {
            public void run() {
                myHandler.sendEmptyMessage(2);
            }
        };
        timer = new Timer();
        timer.schedule(t, 1000, 200);
    }

    private Bitmap bitmap;
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LogUtils.i(TAG, "myHandler:" + index);
                    if (index == len)
                        index = 0;
                    //通过ImageView对象拿到背景显示的AnimationDrawable
                    try {
                        bitmap = MyUtil.readBitMap(getApplicationContext(), succeedAnim[index++]);
                        animImg.setImageBitmap(bitmap);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        LogUtils.i(TAG, "内存溢出:" + e.getMessage());
                    }
                    break;
                case 2:
                    if (index == len)
                        index = 0;
                    try {
                        bitmap=MyUtil.readBitMap(getApplicationContext(), failedAnim[index++]);
                        animImg.setImageBitmap(bitmap);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        LogUtils.i(TAG, "内存溢出:" + e.getMessage());
                    }
                    break;
            }
        }
    };
}
