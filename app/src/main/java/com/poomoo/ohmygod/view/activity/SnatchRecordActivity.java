/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.WinningRecordsBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.SnatchAdapter;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.custom.RefreshLayout;
import com.poomoo.ohmygod.view.custom.RefreshLayout.OnLoadListener;

import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 夺宝记录
 * 作者: 李苜菲
 * 日期: 2015/11/23 09:23.
 */
public class SnatchRecordActivity extends BaseActivity implements OnLoadListener, OnRefreshListener {
    //    private Fragment curFragment;
//    private AllFragment allFragment;
//    private AlreadyFragment alreadyFragment;
    private RefreshLayout refreshLayout;
    private ListView listView;
    private SnatchAdapter adapter;
    private int currPage = 1;
    private final static int PAGESIZE = 10;
    private List<WinningRecordsBO> recordsBOList = new ArrayList<>();
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snatch_record);

        initView();
//        refreshLayout.post(new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                refreshLayout.setRefreshing(true);
//            }
//        }));
        getData();
    }

    protected void initView() {
        initTitleBar();

        refreshLayout = (RefreshLayout) findViewById(R.id.refresh_snatch);
        listView = (ListView) findViewById(R.id.list_snatch);
        adapter = new SnatchAdapter(this);
        listView.setAdapter(adapter);

        refreshLayout.setOnLoadListener(this);
        refreshLayout.setRefreshing(false);

    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_snatch_record);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData() {
        showProgressDialog("加载中...");
        this.appAction.getWinningList(this.application.getUserId(), "1", currPage++, PAGESIZE, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                recordsBOList = data.getObjList();
                if (isFirst)
                    adapter.setItems(recordsBOList);
                else
                    adapter.addItems(recordsBOList);
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    @Override
    public void onLoad() {
        LogUtils.i(TAG, "onLoad");
        refreshLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新完后调用该方法结束刷新
                refreshLayout.setLoading(false);
                // 更新数据
                getData();
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {

    }

    //    private void setDefaultFragment() {
//        // TODO 自动生成的方法存根
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        allFragment = new AllFragment();
//        curFragment = allFragment;
//        fragmentTransaction.add(R.id.flayout_snatch_record, allFragment);
//        fragmentTransaction.commit();
//    }

//    public void switchToAll(View view) {
//        if (allFragment == null)
//            allFragment = new AllFragment();
//        switchFragment(allFragment);
//        curFragment = allFragment;
//    }
//
//    public void switchToAlready(View view) {
//        if (alreadyFragment == null)
//            alreadyFragment = new AlreadyFragment();
//        switchFragment(alreadyFragment);
//        curFragment = alreadyFragment;
//    }

    /**
     * 切换fragment
     *
     * @param to
     */
//    public void switchFragment(Fragment to) {
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        if (!to.isAdded()) { // 先判断是否被add过
//            fragmentTransaction.hide(curFragment).add(R.id.flayout_snatch_record, to); // 隐藏当前的fragment，add下一个到Activity中
//        } else {
//            fragmentTransaction.hide(curFragment).show(to); // 隐藏当前的fragment，显示下一个
//        }
//        fragmentTransaction.commit();
//    }
}
