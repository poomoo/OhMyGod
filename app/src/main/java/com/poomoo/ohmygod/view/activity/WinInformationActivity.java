package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.poomoo.model.WinInformationBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.WinInformationAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_information);

        initView();
    }

    protected void initView() {
        initTitleBar();

        refreshableView = (RefreshableView) findViewById(R.id.activity_win_information_refreshable);
        listView = (ListView) findViewById(R.id.activity_win_information_listView);

        winInformationAdapter = new WinInformationAdapter(this);
        listView.setAdapter(winInformationAdapter);
        list = new ArrayList<>();
        WinInformationBO winInformationBO = new WinInformationBO();
        winInformationBO.setTitle("测试测试");
        list.add(winInformationBO);
        winInformationAdapter.setItems(list);

        //下拉刷新
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {

            }
        }, 0);
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
}
