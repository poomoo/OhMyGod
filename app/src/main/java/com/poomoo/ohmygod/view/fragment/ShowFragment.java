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

import com.poomoo.model.ReplyBO;
import com.poomoo.model.ShowBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.ShowAdapter;

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
    private final String[] urls = {"http://pic1a.nipic.com/2008-12-04/2008124215522671_2.jpg", "http://pic.nipic.com/2007-11-09/2007119122519868_2.jpg", "http://pic14.nipic.com/20110522/7411759_164157418126_2.jpg", "http://img.taopic.com/uploads/allimg/130501/240451-13050106450911.jpg", "http://pic25.nipic.com/20121209/9252150_194258033000_2.jpg", "http://pic.nipic.com/2007-11-09/200711912230489_2.jpg"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        replyRlayout = (RelativeLayout) getActivity().findViewById(R.id.rlayout_reply);
        list = (ListView) getActivity().findViewById(R.id.list_show);
        showAdapter = new ShowAdapter(getActivity());
        list.setAdapter(showAdapter);

        showBO = new ShowBO();
        showBO.setPics(urls);
        replyBO = new ReplyBO();
        replyBO.setContent("测试测试");
        replyBO.setFloor_user_name("十年九梦你");
        replyBO.setRevert_user_name("糊涂图");
        showBO.setReplyBO(replyBO);

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
}
