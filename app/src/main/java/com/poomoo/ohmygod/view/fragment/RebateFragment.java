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
import android.widget.TextView;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.custom.CalendarView;
import com.poomoo.ohmygod.view.custom.CustomerDatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 返
 * 作者: 李苜菲
 * 日期: 2015/11/17 14:51.
 */
public class RebateFragment extends BaseFragment implements View.OnClickListener {
    private String TAG = this.getClass().getSimpleName();

    private TextView yearTxt;
    private TextView monthTxt;
    private ImageView yearLeftImg;
    private ImageView yearDownImg;
    private ImageView monthDownImg;
    private CalendarView calendar;

    private SimpleDateFormat format;
    private Calendar cal = Calendar.getInstance();
    private Date curDate;

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

        yearTxt = (TextView) getActivity().findViewById(R.id.txt_year);
        monthTxt = (TextView) getActivity().findViewById(R.id.txt_month);
        yearLeftImg = (ImageView) getActivity().findViewById(R.id.img_year_left);
        yearDownImg = (ImageView) getActivity().findViewById(R.id.img_year_down);
        monthDownImg = (ImageView) getActivity().findViewById(R.id.img_month_down);

        yearTxt.setOnClickListener(this);
        monthTxt.setOnClickListener(this);
        yearDownImg.setOnClickListener(this);
        monthDownImg.setOnClickListener(this);

        format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //设置日历日期
            Date date = format.parse("2015-11-17");
            calendar.setCalendarData(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void pickDate() {
        final DatePickerDialog mDialog = new CustomerDatePickerDialog(getActivity(), null,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));


        // 手动设置按钮
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
                DatePicker datePicker = mDialog.getDatePicker();
                int year = datePicker.getYear();
                int month = datePicker.getMonth() + 1;

                yearTxt.setText(year + "年");
                monthTxt.setText(month + "月");

                String dateStr = year + "-" + month;

                try {
                    curDate = MyUtil.ConverToDate(dateStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i(TAG, "curDate:" + curDate);
                calendar.setCalendarData(curDate);
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
        pickDate();
    }
}
