package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ActiveWinInfoBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.WinInformationBO;
import com.poomoo.model.WinningRecordsBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.ActiveWinnerInfoListAdapter;
import com.poomoo.ohmygod.adapter.WinInformationAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.custom.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 中奖信息
 * 作者: 李苜菲
 * 日期: 2015/11/13 10:51.
 */
public class WinInformationActivity extends BaseActivity implements RefreshLayout.OnLoadListener, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {
    private RefreshLayout refreshLayout;
    private ListView listView;

    private ActiveWinnerInfoListAdapter adapter;
    private List<ActiveWinInfoBO> list = new ArrayList<>();

    private int currPage = 1;
    private boolean isLoad = false;//true 加载 false刷新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_information);
        addActivityToArrayList(this);
        initView();
        showProgressDialog(getString(R.string.dialog_message));
        getData();
    }

    protected void initView() {
        initTitleBar();

        refreshLayout = (RefreshLayout) findViewById(R.id.refresh_winInfo);
        listView = (ListView) findViewById(R.id.activity_win_information_listView);

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);

        adapter = new ActiveWinnerInfoListAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_win_information);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
    }

    private void getData() {
        this.appAction.getWinningInfo(application.getCurrCity(), currPage, MyConfig.PAGESIZE, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                // 更新完后调用该方法结束刷新
                if (isLoad) {
                    refreshLayout.setLoading(false);
                    int len = data.getObjList().size();
                    for (int i = 0; i < len; i++) {
                        list.add((ActiveWinInfoBO) data.getObjList().get(i));
                    }
                    if (len > 0) {
                        currPage++;
                        adapter.addItems(data.getObjList());
                    }
                } else {
                    refreshLayout.setRefreshing(false);
                    currPage++;
                    list = new ArrayList<>();
                    list = data.getObjList();
                    adapter.setItems(list);
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
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Bundle bundle = new Bundle();
//        bundle.putInt(getString(R.string.intent_activeId), list.get(position).getActiveId());
//        bundle.putString(getString(R.string.intent_activityName), list.get(position).getTitle());
//        openActivity(CommodityInformation2Activity.class, bundle);
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
}
