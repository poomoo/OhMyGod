package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.WinInformationBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.WinInformationAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.custom.RefreshableView;

import java.util.ArrayList;
import java.util.List;

/**
 * 中奖信息
 * 作者: 李苜菲
 * 日期: 2015/11/13 10:51.
 */
public class WinInformationActivity extends BaseActivity {
    private RefreshableView refreshableView;
    private ListView listView;

    private WinInformationAdapter winInformationAdapter;
    private List<WinInformationBO> list;

    private int currPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_information);

        initView();
        getData();
    }

    protected void initView() {
        initTitleBar();

        refreshableView = (RefreshableView) findViewById(R.id.refresh_winInfo);
        listView = (ListView) findViewById(R.id.activity_win_information_listView);

        winInformationAdapter = new WinInformationAdapter(this);
        listView.setAdapter(winInformationAdapter);
        list = new ArrayList<>();
//        WinInformationBO winInformationBO = new WinInformationBO();
//        winInformationBO.setTitle("测试测试");
//        list.add(winInformationBO);
//        winInformationAdapter.setItems(list);

        //下拉刷新
//        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getData();
//            }
//        }, 0);
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_win_information);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getData() {
        showProgressDialog("请稍后...");
        this.appAction.getWinningInfo(application.getCurrCity(), currPage, MyConfig.PAGESIZE, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
//                refreshableView.finishRefreshing();
                closeProgressDialog();
                list = data.getObjList();
                winInformationAdapter.setItems(list);
//                currPage = 1;
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
//                refreshableView.finishRefreshing();
                MyUtil.showToast(getApplicationContext(), message);
                finish();
            }
        });
    }
}
