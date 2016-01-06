/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.SignedBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.activity.RebateRecordsActivity;
import com.poomoo.ohmygod.view.activity.WebViewActivity;
import com.poomoo.ohmygod.view.activity.WithdrawDepositActivity;
import com.poomoo.ohmygod.view.custom.pullDownScrollView.PullDownElasticImp;
import com.poomoo.ohmygod.view.custom.pullDownScrollView.PullDownScrollView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 返
 * 作者: 李苜菲
 * 日期: 2015/11/17 14:51.
 */
public class RebateFragment extends BaseFragment implements View.OnClickListener, PullDownScrollView.RefreshListener {
    private TextView totalBalanceTxt;//总金额
    private TextView unRebatedAmountTxt;//未返金额
    private TextView rebatedAmountTxt;//已返金额
    private LinearLayout recordsLlayout;//返现记录
    private LinearLayout statementLlayout;//返现声明
    private LinearLayout withDrawDepositLlayout;//我要提现

    private PullDownScrollView refreshableView;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");//下拉时间格式

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
        refreshableView = (PullDownScrollView) getActivity().findViewById(R.id.refresh_rebate);
        totalBalanceTxt = (TextView) getActivity().findViewById(R.id.txt_rebateTotalBalance);
        unRebatedAmountTxt = (TextView) getActivity().findViewById(R.id.txt_unRebatedAmount);
        rebatedAmountTxt = (TextView) getActivity().findViewById(R.id.txt_rebatedAmount);
        recordsLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_rebateRecords);
        statementLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_rebateStatement);
        withDrawDepositLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_rebateWithdrawDeposit);

        recordsLlayout.setOnClickListener(this);
        statementLlayout.setOnClickListener(this);
        withDrawDepositLlayout.setOnClickListener(this);

        //初始化下拉刷新
        refreshableView.setRefreshListener(this);
        refreshableView.setPullDownElastic(new PullDownElasticImp(getActivity()));

        showProgressDialog(getString(R.string.dialog_message));
        getData(false);
    }

    private void getData(final boolean isRefreshable) {

        this.appAction.getRebate(application.getUserId(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                if (isRefreshable)
                    refreshableView.finishRefresh(format.format(new Date(System.currentTimeMillis())));

                SignedBO signedBO = (SignedBO) data.getObj();
                //中奖
                if (signedBO.getIsWin() == 1) {
                    totalBalanceTxt.setText(signedBO.getTotalCashFee());
                    totalBalanceTxt.setTextSize(getResources().getDimension(R.dimen.sp_26));
                }
                unRebatedAmountTxt.setText(signedBO.getCountUse());
                rebatedAmountTxt.setText(signedBO.getCountUsed());
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                if (isRefreshable)
                    refreshableView.finishRefresh(format.format(new Date(System.currentTimeMillis())));
                MyUtil.showToast(getActivity().getApplicationContext(), message);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_rebateRecords:
                openActivity(RebateRecordsActivity.class);
                break;
            case R.id.llayout_rebateStatement:
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.intent_parent), getString(R.string.intent_rebate));
                openActivity(WebViewActivity.class, bundle);
                break;
            case R.id.llayout_rebateWithdrawDeposit:
                openActivity(WithdrawDepositActivity.class);
                break;
        }
    }

    @Override
    public void onRefresh(PullDownScrollView view) {
        getData(true);
    }
}
