/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.CommentBO;
import com.poomoo.model.ReplyBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.ShowBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.ReplyListener;
import com.poomoo.ohmygod.adapter.ShowAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.activity.MainFragmentActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 晒
 * 作者: 李苜菲
 * 日期: 2015/11/20 09:50.
 */
public class ShowFragment extends BaseFragment {
    private EditText replyEdt;
    private Button replyBtn;
    private LinearLayout fragmentView;
    private LinearLayout replyRlayout;
    private LinearLayout editLlayout;
    private ListView list;
    private ShowAdapter showAdapter;
    private ShowBO showBO;
    private CommentBO commentBO;
    private ReplyBO replyBO;
    private List<ShowBO> showBOList = new ArrayList<>();
    private List<ReplyBO> replyBOList = new ArrayList<>();
    private List<CommentBO> commentBOList = new ArrayList<>();
    private List<String> picList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
//        getData();
    }

    private void initView() {
        fragmentView = (LinearLayout) getActivity().findViewById(R.id.fragment_view);
        replyRlayout = (LinearLayout) getActivity().findViewById(R.id.rlayout_reply);
        editLlayout = (LinearLayout) getActivity().findViewById(R.id.layout_edittext);
        replyEdt = (EditText) getActivity().findViewById(R.id.edt_reply);
        replyBtn = (Button) getActivity().findViewById(R.id.btn_reply);
        list = (ListView) getActivity().findViewById(R.id.list_show);
        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUtil.showToast(getActivity().getApplicationContext(), "点击回复按钮");
            }
        });
        fragmentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                fragmentView.getWindowVisibleDisplayFrame(r);

                int screenHeight = fragmentView.getRootView().getHeight();
                int heightDifference = screenHeight - (r.bottom - r.top);
                boolean visible = heightDifference > screenHeight / 3;
                if (visible) {
                    LogUtils.i(TAG, "键盘可见");
                    Rect fragmentWindowRect = new Rect();
                    Rect fragmentLocalRect = new Rect();
                    Rect inputViewRect = new Rect();
                    //显示Fragment的可见区域
                    //若键盘在隐藏状态，会返回全屏幕的尺寸
                    //若键盘在显示状态，会返回除键盘以外的尺寸，这是我想要的
                    fragmentView.getWindowVisibleDisplayFrame(fragmentWindowRect);
                    //获取Fragment在activity上显示区域
                    //无论键盘是否显示，值都一样
                    fragmentView.getLocalVisibleRect(fragmentLocalRect);
                    //获取输入界面的位置和大小
                    editLlayout.getLocalVisibleRect(inputViewRect);

                    int inputViewHeight = inputViewRect.bottom - inputViewRect.top;
                    LogUtils.i(TAG, "inputViewHeight:" + inputViewHeight);
                    int targetBottom = Math.min(fragmentLocalRect.bottom, fragmentWindowRect.bottom);
                    targetBottom=860;
                    LogUtils.i(TAG, "fragmentLocalRect.bottom:" + fragmentLocalRect.bottom + "fragmentWindowRect.bottom:" + fragmentWindowRect.bottom);
                    int top = targetBottom - inputViewHeight;
                    LogUtils.i(TAG, "top:" + top + "right:" + inputViewRect.right + "bottom:" + targetBottom);
                    editLlayout.layout(0, top, inputViewRect.right, targetBottom);
                }
            }
        });

        showAdapter = new ShowAdapter(getActivity(), new ReplyListener() {
            @Override
            public void onResult(String name) {
                MyUtil.showToast(getActivity().getApplication(), "onResult 点击:" + name);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                MainFragmentActivity.instance.invisible();
                replyRlayout.setVisibility(View.VISIBLE);
                LogUtils.i(TAG, "replyRlayout可见");
                replyEdt.setFocusable(true);
                replyEdt.setFocusableInTouchMode(true);
                replyEdt.requestFocus();
            }
        });
        list.setAdapter(showAdapter);

        replyRlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.i(TAG, "点击屏幕");
                MainFragmentActivity.instance.visible();
                if (replyRlayout.getVisibility() == View.VISIBLE) {
                    replyRlayout.setVisibility(View.GONE);
                }
                //隐藏键盘
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
        testData();
    }

    private void testData() {
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

        showAdapter.setItems(showBOList);
    }

    private void getData() {
        showProgressDialog("查询中...");
        this.appAction.getShowList(1, application.getUserId(), 1, 10, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
            }
        });
    }
}
