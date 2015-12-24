/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.WinningRecordsBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.SnatchAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.custom.RefreshLayout;
import com.poomoo.ohmygod.view.custom.RefreshLayout.OnLoadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 夺宝记录
 * 作者: 李苜菲
 * 日期: 2015/11/23 09:23.
 */
public class SnatchRecordActivity extends BaseActivity implements OnLoadListener, OnRefreshListener, OnItemClickListener {
    private RefreshLayout refreshLayout;
    private ListView listView;
    private SnatchAdapter adapter;
    private int currPage = 1;
    private boolean isLoad = false;//true 加载 false刷新
    private List<WinningRecordsBO> recordsBOList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snatch_record);

        initView();
        showProgressDialog(getString(R.string.dialog_message));
        getData();
    }

    protected void initView() {
        initTitleBar();

        refreshLayout = (RefreshLayout) findViewById(R.id.refresh_snatch);
        listView = (ListView) findViewById(R.id.list_snatch);
        adapter = new SnatchAdapter(this);
        listView.setAdapter(adapter);

        refreshLayout.setOnLoadListener(this);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setRefreshing(false);
        listView.setOnItemClickListener(this);

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
        this.appAction.getWinningList(this.application.getUserId(), "1", currPage, MyConfig.PAGESIZE, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                // 更新完后调用该方法结束刷新
                recordsBOList = new ArrayList<>();
                if (isLoad) {
                    refreshLayout.setLoading(false);
                    int len = data.getObjList().size();
                    for (int i = 0; i < len; i++) {
                        recordsBOList.add((WinningRecordsBO) data.getObjList().get(i));
                    }
                    if (len > 0) {
                        currPage++;
                        adapter.addItems(recordsBOList);
                    }
                } else {
                    refreshLayout.setRefreshing(false);
                    currPage++;
                    recordsBOList = data.getObjList();
                    adapter.setItems(recordsBOList);
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
        isLoad = false;
        currPage = 1;
        refreshLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                // 更新数据
                getData();
            }
        }, 0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogUtils.i(TAG, "recordsBOList大小:" + adapter.getItemList() + "position:" + position);
        Bundle bundle = new Bundle();
        bundle.putInt(getString(R.string.intent_activeId), adapter.getItemList().get(position).getActiveId());
        bundle.putString(getString(R.string.intent_activityName), adapter.getItemList().get(position).getTitle());
        openActivity(CommodityInformation2Activity.class, bundle);
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
