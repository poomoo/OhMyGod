/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.poomoo.model.CommentBO;
import com.poomoo.model.ReplyBO;
import com.poomoo.model.ShowBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.ReplyListener;
import com.poomoo.ohmygod.adapter.ShowAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private ListView list;
    private ShowAdapter showAdapter;
    private ShowBO showBO;
    private CommentBO commentBO;
    private ReplyBO replyBO;
    private List<ShowBO> showBOList = new ArrayList<>();
    private List<ReplyBO> replyBOList = new ArrayList<>();
    private List<CommentBO> commentBOList = new ArrayList<>();
    private List<String> picList = new ArrayList<>();
    private int heightDifference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initView();
    }

//    @Override
//    protected void initView() {
//        startTxt = (TextView) findViewById(R.id.txt_start);
//        endTxt = (TextView) findViewById(R.id.txt_end);
//        bezierView = (BezierView) findViewById(R.id.BezierView);
//    }

    protected void initView() {
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

//        replyLlayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            /**
//             * the result is pixels
//             */
//            @Override
//            public void onGlobalLayout() {
//
//                Rect r = new Rect();
//                replyLlayout.getWindowVisibleDisplayFrame(r);
//
//                int screenHeight = replyLlayout.getRootView().getHeight();
//                heightDifference = screenHeight - (r.bottom - r.top);
//                Log.e("Keyboard Size", "Size:" + heightDifference);
//
//                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) replyLlayout.getLayoutParams();
//                params.setMargins(0, 647 - params.height, 0, 647);// 通过自定义坐标来放置你的控件
//                replyLlayout.setLayoutParams(params);
//            }
//        });


        showAdapter = new ShowAdapter(this, new ReplyListener() {
            @Override
            public void onResult(String name) {
                MyUtil.showToast(getApplication(), "onResult 点击:" + name);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                replyLlayout.setVisibility(View.VISIBLE);
                int location[] = new int[2];
//                replyLlayout.getLocationOnScreen(location);
//                LogUtils.i(TAG,"x:"+location[0] + "y:" + location[1]);
//                LogUtils.i(TAG, "height:" + replyLlayout.getLayoutParams().height + "  heightDifference:" + heightDifference);
                MyUtil.showToast(getApplication(), "replyRlayout状态:" + replyLlayout.getVisibility());
                replyEdt.setFocusable(true);
                replyEdt.setFocusableInTouchMode(true);
                replyEdt.requestFocus();
            }
        });
        list.setAdapter(showAdapter);

        showBO = new ShowBO();
        int len = MyConfig.testUrls.length;
        for (int i = 0; i < len; i++)
            picList.add(MyConfig.testUrls[i]);
        showBO.setPicList(picList);
        showBO.setNickName("十年九梦你");
        showBO.setDynamicDt((new Date()).toString());
        showBO.setContent("货已经收到了，东西不错");
        showBO.setTitle("(第123期)电脑疯抢玩命中...");

        commentBO = new CommentBO();
        commentBO.setNickName("十年九梦你");
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
        showBOList.add(showBO);
        showBOList.add(showBO);
//        showBOList.add(showBO);

        showAdapter.setItems(showBOList);

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
            }
        });
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
}
