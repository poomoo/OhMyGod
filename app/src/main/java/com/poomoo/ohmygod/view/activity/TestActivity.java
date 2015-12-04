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
    private int selectPosition;
    private int screenHeight;
    private int keyBoardHeight;

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
                    LogUtils.i(TAG, "键盘可见:" + "screenHeight:" + screenHeight + "r.bottom:" + r.bottom + "r.top:" + r.top + "keyBoardHeight" + keyBoardHeight);
                }
            }
        });


        showAdapter = new ShowAdapter(this, new ReplyListener() {
            @Override
            public void onResult(String name, int position) {
                MyUtil.showToast(getApplication(), "onResult 点击:" + name);
                selectPosition = position;
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                replyLlayout.setVisibility(View.VISIBLE);
                MyUtil.showToast(getApplication(), "replyRlayout状态:" + replyLlayout.getVisibility());
                replyEdt.setFocusable(true);
                replyEdt.setFocusableInTouchMode(true);
                replyEdt.requestFocus();
            }
        });
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

        LogUtils.i(TAG, "moveList:" + selectPosition + "count:" + showAdapter.getCount());
        if (showAdapter.getCount() == selectPosition + 1) {
            list.setSelection(list.getBottom());
        } else {
            LogUtils.i(TAG, "screenHeight:" + screenHeight + "keyBoardHeight:" + keyBoardHeight);
            int off = screenHeight - screenHeight;
            LogUtils.i(TAG, "off:" + off);
            list.setSelectionFromTop(selectPosition + 1, off);
        }
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
