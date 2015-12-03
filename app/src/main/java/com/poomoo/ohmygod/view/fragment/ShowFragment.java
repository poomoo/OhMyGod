/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ReplyBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.ShowBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.ShowAdapter;
import com.poomoo.ohmygod.config.MyConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 晒
 * 作者: 李苜菲
 * 日期: 2015/11/20 09:50.
 */
public class ShowFragment extends BaseFragment {
    private RelativeLayout replyRlayout;
    private ListView list;
    private ShowAdapter showAdapter;
    private ShowBO showBO;
    private ReplyBO replyBO;
    private List<ShowBO> showBOList;

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
        replyRlayout = (RelativeLayout) getActivity().findViewById(R.id.rlayout_reply);
        list = (ListView) getActivity().findViewById(R.id.list_show);
        showAdapter = new ShowAdapter(getActivity());
//        list.setAdapter(showAdapter);

        showBO = new ShowBO();
//        showBO.setPics(MyConfig.testUrls);
//        replyBO = new ReplyBO();
//        replyBO.setContent("测试测试");
//        replyBO.setFloor_user_name("十年九梦你");
//        replyBO.setRevert_user_name("糊涂图");
//        showBO.setReplyBO(replyBO);

        showBOList = new ArrayList<>();
        showBOList.add(showBO);
        showAdapter.setItems(showBOList);

        replyRlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (replyRlayout.getVisibility() == View.VISIBLE) {
                    replyRlayout.setVisibility(View.GONE);
                }
//                if (comment_layout.getVisibility() == View.INVISIBLE)
//                    comment_layout.setVisibility(View.VISIBLE);
                //隐藏键盘
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
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
