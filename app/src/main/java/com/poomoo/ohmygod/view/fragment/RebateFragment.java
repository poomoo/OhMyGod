/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.SignedBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.activity.SettingActivity;
import com.poomoo.ohmygod.view.activity.WebViewActivity;
import com.poomoo.ohmygod.view.activity.WithdrawDepositActivity;
import com.poomoo.ohmygod.view.custom.CalendarView;
import com.poomoo.ohmygod.view.custom.CustomerDatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 返
 * 作者: 李苜菲
 * 日期: 2015/11/17 14:51.
 */
public class RebateFragment extends BaseFragment implements View.OnClickListener {
    private TextView withDrawDepositTxt;
    private TextView yearTxt;
    private TextView monthTxt;
    private TextView totalAmountTxt;//总金额池
    private TextView useAmountTxt;//未返金额
    private TextView usedAmountTxt;//已返金额
    private TextView signTxt;//签到按钮文字
    private ImageView yearLeftImg;
    private ImageView yearDownImg;
    private ImageView monthDownImg;
    private CalendarView calendar;
    private LinearLayout signLlayout;//签到
    private LinearLayout signedLlayout;
    private LinearLayout rebateLlayout;

    private SimpleDateFormat format;
    private Calendar cal = Calendar.getInstance();
    private Date curDate;
    private String strYear;
    private String strMonth;
    private int nYear;
    private int nMonth;

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
        //获取日历控件对象
        Log.i(TAG, "initView");
        calendar = (CalendarView) getActivity().findViewById(R.id.calendar);

        withDrawDepositTxt = (TextView) getActivity().findViewById(R.id.txt_withDrawDeposit);
        yearTxt = (TextView) getActivity().findViewById(R.id.txt_year);
        monthTxt = (TextView) getActivity().findViewById(R.id.txt_month);
        totalAmountTxt = (TextView) getActivity().findViewById(R.id.txt_totalAmount);
        useAmountTxt = (TextView) getActivity().findViewById(R.id.txt_useAmount);
        usedAmountTxt = (TextView) getActivity().findViewById(R.id.txt_usedAmount);
        signTxt = (TextView) getActivity().findViewById(R.id.txt_sign);
        yearLeftImg = (ImageView) getActivity().findViewById(R.id.img_year_left);
        yearDownImg = (ImageView) getActivity().findViewById(R.id.img_year_down);
        monthDownImg = (ImageView) getActivity().findViewById(R.id.img_month_down);
        signLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_signed);
        signedLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_signed_explain);
        rebateLlayout = (LinearLayout) getActivity().findViewById(R.id.llayout_rebate_explain);

        withDrawDepositTxt.setOnClickListener(this);
        yearTxt.setOnClickListener(this);
        monthTxt.setOnClickListener(this);
        yearDownImg.setOnClickListener(this);
        monthDownImg.setOnClickListener(this);
        signLlayout.setOnClickListener(this);
        signedLlayout.setOnClickListener(this);
        rebateLlayout.setOnClickListener(this);

        nYear = cal.get(Calendar.YEAR);
        nMonth = cal.get(Calendar.MONTH) + 1;
        strYear = nYear + "";
        strMonth = nMonth + "";
        curDate = new Date();
        getData(strYear, strMonth);
    }

    /**
     * 选择年月
     */
    private void pickDate() {
        cal = Calendar.getInstance();
        final DatePickerDialog mDialog = new CustomerDatePickerDialog(getActivity(), null,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        // 手动设置按钮
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
                DatePicker datePicker = mDialog.getDatePicker();
                nYear = datePicker.getYear();
                nMonth = datePicker.getMonth() + 1;

                String dateStr = nYear + "-" + nMonth;

                try {
                    curDate = MyUtil.ConvertToDate(dateStr);
                    cal.setTime(curDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "curDate:" + curDate);
                strYear = nYear + "";
                strMonth = nMonth + "";
                getData(strYear, strMonth);
            }
        });
        // 取消按钮，如果不需要直接不设置即可
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        mDialog.show();
        DatePicker dp = mDialog.getDatePicker();
        ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
        dp.setMaxDate(cal.getTime().getTime());
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
                toSign();
                break;
            case R.id.txt_withDrawDeposit:
                openActivity(WithdrawDepositActivity.class);
                break;
            default:
                pickDate();
                break;
        }
    }

    /**
     * 签到
     */
    private void toSign() {
        showProgressDialog("签到中...");
        this.appAction.toSign(this.application.getUserId(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                setButtonEnable(getString(R.string.label_signed));
                cal = Calendar.getInstance();
                nYear = cal.get(Calendar.YEAR);
                nMonth = cal.get(Calendar.MONTH) + 1;
                strYear = nYear + "";
                strMonth = nMonth + "";
                getData(strYear, strMonth);
                MyUtil.showToast(getActivity().getApplicationContext(), data.getMsg());
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getActivity().getApplicationContext(), message);
            }
        });
    }

    /**
     * 已签到置灰
     */
    public void setButtonEnable(String btn) {
        signLlayout.setBackgroundResource(R.drawable.bg_btn_signed);
        signLlayout.setClickable(false);
        signTxt.setText(btn);
    }

    /**
     * 签到列表
     */
    private void getData(String year, String month) {
        showProgressDialog("查询中...");
        this.appAction.getSignedList(this.application.getUserId(), year, month, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                yearTxt.setText(nYear + "年");
                monthTxt.setText(nMonth + "月");

                SignedBO signedBO = (SignedBO) data.getObj();
                totalAmountTxt.setText(signedBO.getTotalCashFee());
                useAmountTxt.setText(signedBO.getCountUse());
                usedAmountTxt.setText(signedBO.getCountUsed());
                if (signedBO.getCountUse().equals("0"))
                    setButtonEnable(getString(R.string.label_unSign));
                LogUtils.i(TAG, "curDate:" + curDate);
                calendar.setCalendarData(curDate, signedBO.getMySignRecords());
                if (signedBO.getMySignRecords() != null) {
                    format = new SimpleDateFormat("yyyy-MM-dd");
                    String curDate = format.format(new Date());
                    LogUtils.i(TAG, "有签到记录:" + curDate);
                    //当日已签到
                    if (signedBO.getMySignRecords().contains(curDate)) {
                        LogUtils.i(TAG, "当日已签到");
                        setButtonEnable(getString(R.string.label_unSign));
                    }

                }

            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getActivity().getApplicationContext(), message);
            }
        });
    }
}
