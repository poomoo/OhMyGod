/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
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
    private int screenHeight;
    private int keyBoardHeight;
    private int selectPosition;
    private boolean isKeyBoardShow = false;
    private View view;
    private int viewTop;
    private int itemTop;
    private int y1;//键盘弹出后剩余的界面底部y坐标
    private int y2;//点击的view距离当前item顶端的距离
    private String content;
    private int commentPosition;
    private boolean isComment = false;//评论
    private boolean isReply = false;//回复
    private boolean isReplyComment = false;//true-回复评论 false-回复回复列表里面的评论
    private String toNickName;
    private String toUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        getData();
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
                content = replyEdt.getText().toString().trim();
                if (TextUtils.isEmpty(content))
                    MyUtil.showToast(application.getApplicationContext(), "请输入内容");
                else {
                    hiddenReply(v);
                    if (isComment)
                        putComment();
                    if (isReply)
                        putReply();
                }

            }
        });
        fragmentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                fragmentView.getWindowVisibleDisplayFrame(r);
                screenHeight = fragmentView.getRootView().getHeight();
                keyBoardHeight = screenHeight - (r.bottom - r.top);
                boolean visible = keyBoardHeight > screenHeight / 3;
                LogUtils.i(TAG, "键盘不可见:" + "screenHeight:" + screenHeight + "r.bottom:" + r.bottom + "r.top:" + r.top + "keyBoardHeight" + keyBoardHeight);
                if (visible)
                    moveList(selectPosition);
                LogUtils.i(TAG, "键盘可见:" + "screenHeight:" + screenHeight + "r.bottom:" + r.bottom + "r.top:" + r.top + "keyBoardHeight" + keyBoardHeight);

//                    Rect fragmentWindowRect = new Rect();
//                    Rect fragmentLocalRect = new Rect();
//                    Rect inputViewRect = new Rect();
//                    //显示Fragment的可见区域
//                    //若键盘在隐藏状态，会返回全屏幕的尺寸
//                    //若键盘在显示状态，会返回除键盘以外的尺寸，这是我想要的
//                    fragmentView.getWindowVisibleDisplayFrame(fragmentWindowRect);
//                    //获取Fragment在activity上显示区域
//                    //无论键盘是否显示，值都一样
//                    fragmentView.getLocalVisibleRect(fragmentLocalRect);
//                    //获取输入界面的位置和大小
//                    editLlayout.getLocalVisibleRect(inputViewRect);
//
//                    int inputViewHeight = inputViewRect.bottom - inputViewRect.top;
//                    LogUtils.i(TAG, "inputViewHeight:" + inputViewHeight);
////                    int targetBottom = Math.min(fragmentLocalRect.bottom, fragmentWindowRect.bottom);
//                    int targetBottom = fragmentWindowRect.bottom;
//                    LogUtils.i(TAG, "fragmentLocalRect.bottom:" + fragmentLocalRect.bottom + "fragmentWindowRect.bottom:" + fragmentWindowRect.bottom);
//                    int top = targetBottom - inputViewHeight;
//                    LogUtils.i(TAG, "top:" + top + "right:" + inputViewRect.right + "bottom:" + targetBottom);
//                    editLlayout.layout(0, top, inputViewRect.right, targetBottom);
//                }
            }
        });

        showAdapter = new ShowAdapter(getActivity(), new ReplyListener() {
            @Override
            public void onResult(String name, int position, View v, ShowBO show, int commentPos) {
                toNickName = name;
                selectPosition = position;
                view = v;
                commentPosition = commentPos;
                viewTop = view.getTop();
                showBO = show;
                LogUtils.i(TAG, "showBO:" + showBO);

                MyUtil.showToast(getActivity().getApplication(), "onResult 点击:" + name);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                MainFragmentActivity.instance.invisible();
                replyRlayout.setVisibility(View.VISIBLE);
                LogUtils.i(TAG, "replyRlayout可见");
                replyEdt.setFocusable(true);
                replyEdt.setFocusableInTouchMode(true);
                replyEdt.requestFocus();
                isKeyBoardShow = true;
                //名字为空则是评论
                if (TextUtils.isEmpty(toNickName)) {
                    replyEdt.setHint("");
                    isComment = true;
                    isReply = false;
                }
                //否则为回复
                else {
                    replyEdt.setHint("回复" + name);
                    isComment = false;
                    isReply = true;
                }
            }
        });
        list.setAdapter(showAdapter);

        replyRlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.i(TAG, "点击屏幕");
                hiddenReply(view);
            }
        });
//        testData();
    }

    public void hiddenReply(View view) {
        MainFragmentActivity.instance.visible();
        if (replyRlayout.getVisibility() == View.VISIBLE) {
            replyRlayout.setVisibility(View.GONE);
        }
        //隐藏键盘
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        isKeyBoardShow = false;
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

    private void getData() {
        showProgressDialog("查询中...");
        this.appAction.getShowList(1, application.getUserId(), 1, 10, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                LogUtils.i(TAG, data.getObjList().toString());
                showBOList = data.getObjList();
                showAdapter.setItems(showBOList);
                closeProgressDialog();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getActivity().getApplicationContext(), message);
            }
        });
    }

    /**
     * 评论
     */
    private void putComment() {
        showProgressDialog("提交中...");
        this.appAction.putComment(application.getUserId(), content, showBO.getDynamicId(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                commentBO = new CommentBO();
                commentBO.setNickName(application.getNickName());
                commentBO.setContent(content);
                commentBO.setDynamicId(showBO.getDynamicId());
                replyBOList = new ArrayList<>();
                commentBO.setReplies(replyBOList);
                showBOList.get(selectPosition).getComments().add(commentBO);
                showAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(application.getApplicationContext(), message);
            }
        });
    }

    /**
     * 回复
     */
    private void putReply() {
        showProgressDialog("提交中...");
        final String fromUserId = application.getUserId();
        //回复评论
        if (!TextUtils.isEmpty(showBO.getComments().get(0).getUserId())) {
            isReplyComment = true;
            toUserId = showBO.getComments().get(0).getUserId();
        }
        LogUtils.i(TAG, "replyList:" + showBO.getComments().get(0).getReplies());
        //回复回复
        if (showBO.getComments().get(0).getReplies() != null && showBO.getComments().get(0).getReplies().size() > 0) {
            isReplyComment = false;
            toUserId = showBO.getComments().get(0).getReplies().get(0).getToUserId();
        }


        final String commentId = showBO.getComments().get(0).getCommentId();
        this.appAction.putReply(fromUserId, toUserId, content, commentId, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                replyBO = new ReplyBO();
                replyBO.setFromUserId(fromUserId);
                replyBO.setToUserId(toUserId);
                replyBO.setContent(content);
                replyBO.setCommentId(commentId);
                replyBO.setToNickName(toNickName);
                replyBO.setFromNickName(application.getNickName());
                if (isReplyComment) {
                    replyBOList = new ArrayList<>();
                    replyBOList.add(replyBO);
                    showBOList.get(selectPosition).getComments().get(commentPosition).setReplies(replyBOList);
                } else {
                    showBOList.get(selectPosition).getComments().get(commentPosition).getReplies().add(replyBO);
                }
                showAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(application.getApplicationContext(), message);
            }
        });
    }

    private void moveList(int selectPosition) {
        if (showAdapter == null)
            return;

        LogUtils.i(TAG, "selectPosition:" + selectPosition);
        if (isKeyBoardShow) {
//            LogUtils.i(TAG, "moveList:" + selectPosition + "count:" + showAdapter.getCount());
//            LogUtils.i(TAG, "screenHeight:" + screenHeight + "keyBoardHeight:" + keyBoardHeight);
//
//            y1 = screenHeight - keyBoardHeight;
//            itemTop = list.getChildAt(selectPosition).getTop();
//            LogUtils.i(TAG, "viewTop:" + viewTop + "list.getChildAt(selectPosition).getTop():" + itemTop);
//            y2 = viewTop - list.getChildAt(selectPosition).getTop();
//            int off = y1 - y2;
//            LogUtils.i(TAG, "off:" + off + "y1:" + y1 + "y2:" + y2);
            list.setSelectionFromTop(selectPosition, 0);
        }
        isKeyBoardShow = false;
    }

}
