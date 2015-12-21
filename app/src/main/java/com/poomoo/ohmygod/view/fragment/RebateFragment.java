/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.view.activity.WebViewActivity;
import com.poomoo.ohmygod.view.activity.WithdrawDepositActivity;

/**
 * 返
 * 作者: 李苜菲
 * 日期: 2015/11/17 14:51.
 */
public class RebateFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rebate, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_signed_explain:
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.intent_parent), getString(R.string.intent_signed));
                openActivity(WebViewActivity.class, bundle);
                break;
            case R.id.llayout_rebate_explain:
                bundle = new Bundle();
                bundle.putString(getString(R.string.intent_parent), getString(R.string.intent_rebate));
                openActivity(WebViewActivity.class, bundle);
                break;
            case R.id.llayout_signed:
                break;
            case R.id.txt_withDrawDeposit:
                openActivity(WithdrawDepositActivity.class);
                break;
            default:
                break;
        }
    }
}
