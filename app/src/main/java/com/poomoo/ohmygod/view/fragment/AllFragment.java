/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.poomoo.model.SnatchBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.AllAdapter;
import com.poomoo.ohmygod.config.MyConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * 全部
 * 作者: 李苜菲
 * 日期: 2015/11/23 09:41.
 */
public class AllFragment extends BaseFragment {
    private ListView listView;
    private AllAdapter allAdapter;
    private List<SnatchBO> snatchBOList;
    private SnatchBO snatchBO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }

    private void initView() {
        listView = (ListView) getActivity().findViewById(R.id.list_all);
        allAdapter = new AllAdapter(getActivity());
        listView.setAdapter(allAdapter);

        snatchBOList = new ArrayList<>();

        int len = 5;
        for (int i = 0; i < len; i++) {
            snatchBO = new SnatchBO();
            snatchBO.setUrl("");
            snatchBOList.add(snatchBO);
        }

        allAdapter.setItems(snatchBOList);
    }
}
