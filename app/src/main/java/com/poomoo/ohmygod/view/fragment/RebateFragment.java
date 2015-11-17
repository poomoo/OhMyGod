/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.custom.CalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 返
 * 作者: 李苜菲
 * 日期: 2015/11/17 14:51.
 */
public class RebateFragment extends BaseFragment {
    private String TAG = this.getClass().getSimpleName();
    private CalendarView calendar;
    private SimpleDateFormat format;

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
        calendar.setSelectMore(false); //单选

        format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //设置日历日期
            Date date = format.parse("2015-11-17");
            calendar.setCalendarData(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //设置控件监听，可以监听到点击的每一天（大家也可以在控件中根据需求设定）
        calendar.setOnItemClickListener(new CalendarView.OnItemClickListener() {

            @Override
            public void OnItemClick(Date selectedStartDate,
                                    Date selectedEndDate, Date downDate) {
                if (calendar.isSelectMore()) {
                    MyUtil.showToast(getActivity(), format.format(selectedStartDate) + "到" + format.format(selectedEndDate));
                } else {
                    MyUtil.showToast(getActivity(), format.format(downDate));
                }
            }
        });
    }
}
