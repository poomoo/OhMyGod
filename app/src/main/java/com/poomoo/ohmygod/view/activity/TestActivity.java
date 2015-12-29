/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.model.CommentBO;
import com.poomoo.model.ReplyBO;
import com.poomoo.model.ShowBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.PersonalCenterAdapter;
import com.poomoo.ohmygod.listeners.ReplyListener;
import com.poomoo.ohmygod.adapter.ShowAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/26 10:22.
 */
public class TestActivity extends BaseActivity {
//    private TextView startTxt;
//    private TextView endTxt;
//    private BezierView bezierView;
//    private Point start;
//    private Point end;

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
    private static final String content = "<p style=\\\"text-align: center; \\\">&nbsp; &nbsp; &nbsp; 风机房：456方法发 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 方法：发的说法 地方 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;放假： 1268方法 &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 简介：15发的说法 &nbsp; &nbsp; 发的说法：455 &nbsp;发发斯蒂芬：59发的 &nbsp; &nbsp;</p>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        webView = (WebView) findViewById(R.id.web_test);
        //html自适应
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webView.loadUrl("http://zgqg.91jiaoyou.cn/zgqg/upload/umEditor/1451098807631.jpg");
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
    }


//    @Override
//    protected void initView() {
//        startTxt = (TextView) findViewById(R.id.txt_start);
//        endTxt = (TextView) findViewById(R.id.txt_end);
//        bezierView = (BezierView) findViewById(R.id.BezierView);
//    }

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
                replyLlayout.getWindowVisibleDisplayFrame(r);
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
        if (firstFlag) {
            decrease();
            firstFlag = false;
        }
        percentTxt.setText(percent + "%");
        if (percent < 100) {
            percent++;
            if (percent % (100 / MyConfig.house.length) == 0) {
                imageView.setImageResource(MyConfig.house[index++]);
            }
        } else if (percent == 100) {
            MyUtil.showToast(getApplicationContext(), "抢购完成");
        }
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
