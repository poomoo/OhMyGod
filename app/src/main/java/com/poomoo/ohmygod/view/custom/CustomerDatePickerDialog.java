package com.poomoo.ohmygod.view.custom;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.poomoo.ohmygod.R;

import java.lang.reflect.Field;

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
        // removes the original topbar:
        this.setTitle("");

        // Divider changing:
        DatePicker dpView = this.getDatePicker();
        LinearLayout llFirst = (LinearLayout) dpView.getChildAt(0);
        LinearLayout llSecond = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0; i < llSecond.getChildCount(); i++) {
            NumberPicker picker = (NumberPicker) llSecond.getChildAt(i); // Numberpickers in llSecond
            // reflection - picker.setDividerDrawable(divider); << didn't seem to work.
            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(context.getResources().getColor(R.color.themeRed)));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        // New top:
        int titleHeight = 90;
        // Container:
        LinearLayout llTitleBar = new LinearLayout(context);
        llTitleBar.setOrientation(LinearLayout.VERTICAL);
        llTitleBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, titleHeight));

        // TextView Title:
        TextView tvTitle = new TextView(context);
        tvTitle.setText("请选择日期");
        tvTitle.setGravity(Gravity.CENTER);
        tvTitle.setPadding(10, 10, 10, 10);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        tvTitle.setTextSize((int) (12f * dm.density));
        tvTitle.setTextColor(context.getResources().getColor(R.color.themeRed));
        tvTitle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, titleHeight - 2));
        llTitleBar.addView(tvTitle);

        // View line:
        View vTitleDivider = new View(context);
        vTitleDivider.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 2));
        vTitleDivider.setBackgroundColor(context.getResources().getColor(R.color.themeRed));
        llTitleBar.addView(vTitleDivider);

        dpView.addView(llTitleBar);
        FrameLayout.LayoutParams lp = (android.widget.FrameLayout.LayoutParams) llFirst.getLayoutParams();
        lp.setMargins(0, titleHeight, 0, 0);
    }


    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
        setTitle(year + "年" + (month + 1) + "月" + day + "号");
    }
}