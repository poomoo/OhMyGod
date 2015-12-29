package com.poomoo.ohmygod.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
    private ImageView animImg;//动画
    private TextView percentTxt;//百分比
    private ScrollView scrollView;//

    private TimeCountDownUtil headTimeCountDownUtil;
    private List<TextView> textViewList;
    private CommodityBO commodityBO;

    private boolean firstFlag = true;
    private long countDownTime;//倒计时时间
    private boolean isOpen = false;//是否开启活动
    private boolean isBegin = false;//活动是否开始
    private int activeId;//--活动编号
    private int typeId;//活动分类 1-房子 2-车子 3-装修 4-其他
    private Timer timer;
    private boolean isGrab = false;//是否已经参与了该活动
    private int succeedAnim;//成功动画
    private int failedAnim;//失败动画

    private double percent = 0;
    private int index = 0;
    private int[] anim;//动画
    private double animLen;//动画数组大小
    private boolean isScroll = false;
    private double step;//切换图片的步长
    private DecimalFormat df = new DecimalFormat("0.00");

    private PopupWindow codePopupWindow;
    private boolean isCode = false;//是否通过验证码

    private TextView changeTxt;
    private ImageView codeImg;
    private EditText codeEdt;
    //产生的验证码
    private String realCode;

    private int position;

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
        signInTxt = (TextView) findViewById(R.id.txt_signIn);
        openActivityTxt = (TextView) findViewById(R.id.txt_openActivity);
        commodityWeb = (WebView) findViewById(R.id.web_commodity);
        grabBtn = (Button) findViewById(R.id.btn_grab);
        llayout_head_timeCountDown = (LinearLayout) findViewById(R.id.llayout_head_timeCountDown);
        llayout_openActivity = (LinearLayout) findViewById(R.id.llayout_openActivity);
        llayout_bottom = (LinearLayout) findViewById(R.id.llayout_grab_bottom);
        llayout_anim = (LinearLayout) findViewById(R.id.llayout_anim);
        animImg = (ImageView) findViewById(R.id.img_anim);
        percentTxt = (TextView) findViewById(R.id.txt_percent);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        initCountDown();

        mMenuView = LayoutInflater.from(this).inflate(R.layout.popupwindow_code, null);
        changeTxt = (TextView) mMenuView.findViewById(R.id.txt_change);
        codeEdt = (EditText) mMenuView.findViewById(R.id.et_phoneCodes);
        codeImg = (ImageView) mMenuView.findViewById(R.id.img_showCode);

        //html自适应
        WebSettings webSettings = commodityWeb.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("UTF-8");

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

//        llayout_openActivity.setVisibility(View.GONE);
//        llayout_bottom.setVisibility(View.GONE);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return isScroll;
            }
        });

        typeId = getIntent().getIntExtra(getString(R.string.intent_typeId), -1);

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
        activeId = getIntent().getIntExtra(getString(R.string.intent_activeId), 0);

        showProgressDialog(getString(R.string.dialog_message));
        LogUtils.i(TAG, "activeId:" + activeId);
        this.appAction.getCommodityInformation(application.getUserId(), activeId + "", new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                commodityBO = (CommodityBO) data.getObj();
                //已参加
                if (commodityBO.getPlayFlag().equals("1"))
                    isGrab = false;
                else
                    isGrab = true;
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
        if (isGrab) {
            isMember();
            //房子
            if (typeId == 1) {
                anim = MyConfig.house;
                succeedAnim = R.drawable.housesuccess;
                failedAnim = R.drawable.housefailed;
            }

            //车子
            if (typeId == 2) {
                anim = MyConfig.car;
                succeedAnim = R.drawable.carsuccess;
                failedAnim = R.drawable.carfailed;
            }

            //装修
            if (typeId == 3) {
                anim = MyConfig.decorate;
                succeedAnim = R.drawable.decoratesuccess;
                failedAnim = R.drawable.decoratefailed;
            }

            //其他
            if (typeId == 4) {
                anim = MyConfig.box;
                succeedAnim = R.drawable.boxsuccess;
                failedAnim = R.drawable.boxfailed;
            }

        } else {
            llayout_openActivity.setVisibility(View.GONE);
            llayout_bottom.setVisibility(View.GONE);
            MyUtil.showToast(getApplicationContext(), "您已经参与过该活动,不能再次参与");
        }

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
        commodityWeb.loadData(commodityBO.getContent(), "text/html; charset=UTF-8", null);// 这种写法可以正确解码
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
        position = getIntent().getIntExtra(getString(R.string.intent_position), 0);
//        headTimeCountDownUtil = new TimeCountDownUtil(GrabFragment.adapter.getCountDownUtils().get(position).getMillisUntilFinished(), 1000, textViewList, new CountDownListener() {
//            @Override
//            public void onFinish(int result) {
//                begin();
//            }
//        });
//        headTimeCountDownUtil.start();
        headTimeCountDownUtil = GrabFragment.adapter.getCountDownUtils().get(position);
        headTimeCountDownUtil.setTextViewList(textViewList, new CountDownListener() {
            @Override
            public void onFinish(int result) {
                LogUtils.i(TAG, "倒计时结束");
                begin();
            }
        });
        if (headTimeCountDownUtil.getMillisUntilFinished() == 0) {
            begin();
            for (TextView textView : textViewList)
                textView.setText("活动已开始");
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
        if (!isCode) {
            code();
            return;
        }
        playSound();
        if (firstFlag) {
            decrease();
            llayout_anim.setVisibility(View.VISIBLE);
            isScroll = true;//禁止scrollview滚动
            animLen = anim.length;
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

        animImg.setImageResource(succeedAnim);
        submit();
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
                            animImg.setImageResource(succeedAnim);
                            String title = "恭喜中奖";
                            message = "请完善个人资料后领取奖品";

                            Dialog dialog = new AlertDialog.Builder(CommodityInformationActivity.this).setTitle(title).setMessage(message).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (MyUtil.isNeedCompleteInfo(application))
                                                if (application.getIsAdvancedUser().equals("1"))
                                                    openActivity(CompleteUserInformationActivity.class);
                                                else
                                                    openActivity(CompleteMemberInformationActivity.class);
                                            finish();
                                        }
                                    }
                            ).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).create();
                            Window window = dialog.getWindow();
                            window.setGravity(Gravity.BOTTOM);
                            dialog.show();
                        } else if (grabResultBO.getIsWin().equals("false")) {
                            LogUtils.i(TAG, "failedAnim:" + failedAnim);
                            animImg.setImageResource(failedAnim);
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

//        mMenuView.setFocusable(true);
//        mMenuView.setFocusableInTouchMode(true);
//        mMenuView.requestFocus();
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
                return true;
            }
        });

    }

    private void initPopWindow() {
        codePopupWindow = new PopupWindow(mMenuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        codePopupWindow.setFocusable(true);
        codePopupWindow.setFocusable(true);
    }

    private void playSound() {
        SoundUtil.getMySound(this).playSound(SoundUtil.SOUND_TYPE_SUCCESS);
    }
}
