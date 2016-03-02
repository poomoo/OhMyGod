package com.poomoo.ohmygod.view.custom;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

/**
 * 选择年月的对话框
 * 作者: 李苜菲
 * 日期: 2015/11/19 16:40.
 */
public class CustomerDatePickerDialog extends DatePickerDialog {

    public CustomerDatePickerDialog(Context context,
                                    OnDateSetListener callBack, int year, int monthOfYear,
                                    int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }


    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
        setTitle(year + "年" + (month + 1) + "月" + day + "号");
    }
}