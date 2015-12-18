/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.CommentBO;
import com.poomoo.model.ReplyBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.ShowBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.listeners.LongClickListener;
import com.poomoo.ohmygod.listeners.ReplyListener;
import com.poomoo.ohmygod.adapter.ShowAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.listeners.ShareListener;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.custom.RefreshLayout;
import com.poomoo.ohmygod.view.custom.RefreshLayout.OnLoadListener;
import com.poomoo.ohmygod.view.popupwindow.CopyPopupWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的晒单
 * 作者: 李苜菲
 * 日期: 2015/11/24 11:38.
 */
public class MyShowActivity extends BaseActivity implements OnRefreshListener, OnLoadListener, ReplyListener, ShareListener, LongClickListener {
    private RefreshLayout refreshLayout;
    private EditText replyEdt;
    private Button replyBtn;
    private LinearLayout replyRlayout;
    private LinearLayout editLlayout;
    private ListView list;
    private ShowAdapter adapter;
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
    private String content;
    private int commentPosition;
    private boolean isComment = false;//评论
    private boolean isReply = false;//回复
    private boolean isReplyComment = false;//true-回复评论 false-回复回复列表里面的评论
    private String toNickName;
    private String toUserId;
    private boolean isLoad = false;//true 加载 false刷新
    private int currPage = 1;
    private CopyPopupWindow copyPopupWindow;
    private String copyContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_show);

        initView();
        showProgressDialog("查询中...");
        getData();
    }

    protected void initView() {
        initTitleBar();

        refreshLayout = (RefreshLayout) findViewById(R.id.refresh_show);
        replyRlayout = (LinearLayout) findViewById(R.id.rlayout_reply);
        editLlayout = (LinearLayout) findViewById(R.id.layout_edittext);
        replyEdt = (EditText) findViewById(R.id.edt_reply);
        replyBtn = (Button) findViewById(R.id.btn_reply);
        list = (ListView) findViewById(R.id.list_show);

        refreshLayout.setOnLoadListener(this);
        refreshLayout.setOnRefreshListener(this);

        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = replyEdt.getText().toString().trim();
                if (TextUtils.isEmpty(content))
                    MyUtil.showToast(application.getApplicationContext(), "请输入内容");
                else {
                    replyEdt.setText("");
                    hiddenReply(v);
                    if (isComment)
                        putComment();
                    if (isReply)
                        putReply();
                }

            }
        });
        replyRlayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                replyRlayout.getWindowVisibleDisplayFrame(r);
                screenHeight = replyRlayout.getRootView().getHeight();
                keyBoardHeight = screenHeight - (r.bottom - r.top);
                boolean visible = keyBoardHeight > screenHeight / 3;
//                LogUtils.i(TAG, "键盘不可见:" + "screenHeight:" + screenHeight + "r.bottom:" + r.bottom + "r.top:" + r.top + "keyBoardHeight" + keyBoardHeight);
                if (visible)
                    moveList(selectPosition);
//                LogUtils.i(TAG, "键盘可见:" + "screenHeight:" + screenHeight + "r.bottom:" + r.bottom + "r.top:" + r.top + "keyBoardHeight" + keyBoardHeight);

            }
        });

        adapter = new ShowAdapter(this, this, this, this);
        list.setAdapter(adapter);

        replyRlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.i(TAG, "点击屏幕");
                hiddenReply(view);
            }
        });
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_my_show);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headerViewHolder.rightImg.setVisibility(View.VISIBLE);
        headerViewHolder.rightImg.setImageResource(R.drawable.ic_winning_small);
        headerViewHolder.rightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(WinningRecordActivity.class);
            }
        });
    }

    private void getData() {
        //1表示晒单分享列表，2表示我的晒单列表
        this.appAction.getShowList(2, application.getUserId(), currPage, MyConfig.PAGESIZE, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                LogUtils.i(TAG, data.getObjList().toString());
                closeProgressDialog();
                showBOList = new ArrayList<>();
                // 更新完后调用该方法结束刷新
                if (isLoad) {
                    LogUtils.i(TAG, "加载");
                    refreshLayout.setLoading(false);
                    int len = data.getObjList().size();
                    for (int i = 0; i < len; i++)
                        showBOList.add((ShowBO) data.getObjList().get(i));
                    if (len > 0) {
                        currPage++;
                        adapter.addItems(showBOList);
                    }

                } else {
                    LogUtils.i(TAG, "刷新");
                    currPage++;
                    refreshLayout.setRefreshing(false);
                    showBOList = data.getObjList();
                    if (showBOList.size() > 0)
                        adapter.setItems(showBOList);
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                if (isLoad)
                    refreshLayout.setLoading(false);
                else
                    refreshLayout.setRefreshing(false);
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    public void hiddenReply(View view) {
        MainFragmentActivity.instance.visible();
        if (replyRlayout.getVisibility() == View.VISIBLE) {
            replyRlayout.setVisibility(View.GONE);
        }
        //隐藏键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        isKeyBoardShow = false;
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
                adapter.notifyDataSetChanged();
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
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(application.getApplicationContext(), message);
            }
        });
    }

    private void moveList(int selectPosition) {
        if (adapter == null)
            return;

        LogUtils.i(TAG, "selectPosition:" + selectPosition);
        if (isKeyBoardShow) {
            list.setSelectionFromTop(selectPosition, 0);
        }
        isKeyBoardShow = false;
    }

    @Override
    public void onLoad() {
        isLoad = true;
        refreshLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据
                getData();
            }
        }, 0);
    }

    @Override
    public void onRefresh() {
        currPage = 1;
        isLoad = false;
        refreshLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据
                getData();
            }
        }, 0);
    }

    @Override
    public void onResult(String name, int position, View v, ShowBO show, int commentPos) {
        toNickName = name;
        selectPosition = position;
        view = v;
        commentPosition = commentPos;
        showBO = show;
        LogUtils.i(TAG, "showBO:" + showBO);

        MyUtil.showToast(getApplication(), "onResult 点击:" + name);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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

    @Override
    public void onResult(String title, String content, String picUrl) {

    }

    @Override
    public void onResult(String content) {
        copyContent = content;
        copy();
    }

    private void copy() {
        // 实例化
        copyPopupWindow = new CopyPopupWindow(this, itemsOnClick);
        // 显示窗口
        copyPopupWindow.showAtLocation(this.findViewById(R.id.llayout_show),
                Gravity.CENTER, 0, 0); // 设置layout在genderWindow中显示的位置
    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            copyPopupWindow.dismiss();
            switch (view.getId()) {
                case R.id.llayout_copy:
                    ClipboardManager cmb = (ClipboardManager) MyShowActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                    cmb.setText(copyContent.trim()); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
//                    cmb.getText();//获取粘贴信息
                    MyUtil.showToast(application.getApplicationContext(), "复制 " + copyContent + " 成功");
                    break;
            }
        }
    };
}
