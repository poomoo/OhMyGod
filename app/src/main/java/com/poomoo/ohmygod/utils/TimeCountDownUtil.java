package com.poomoo.ohmygod.utils;

import android.app.Activity;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.widget.TextView;

/**
 * 倒计时器
 * 作者: 李苜菲
 * 日期: 2015/11/16 15:49.
 */
public class TimeCountDownUtil extends CountDownTimer {
    private String TAG = this.getClass().getSimpleName();
    private Activity mActivity;
    private TextView textView;

    // 在这个构造方法里需要传入三个参数，一个是Activity，一个是总的时间millisInFuture，一个是countDownInterval，然后就是你在哪个按钮上做这个事，就把这个按钮传过来就可以了
    public TimeCountDownUtil(Activity mActivity, long millisInFuture,
                             long countDownInterval, TextView textView) {
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
        this.textView = textView;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        Spanned spanned = dealTime(millisUntilFinished / 1000);
        this.textView.setText(spanned);
    }

    @Override
    public void onFinish() {

    }

    /**
     * 倒计时时间转换
     *
     * @param time
     * @return
     */
    public Spanned dealTime(long time) {
        Spanned str;
        StringBuffer returnString = new StringBuffer();
        //2 * 5 * 57 * 21 * 1000
        long day = time / (24 * 60 * 60);
        long hours = (time % (24 * 60 * 60)) / (60 * 60);
        long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
        long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;
        Log.i(TAG, "time:" + time + "day:" + day + "hours:" + hours + "minutes:" + minutes + "second:" + second);
        String dayStr = String.valueOf(day);
        String hoursStr = timeStrFormat(String.valueOf(hours));
        String minutesStr = timeStrFormat(String.valueOf(minutes));
        String secondStr = timeStrFormat(String.valueOf(second));
        returnString.append(dayStr).append("天").append(hoursStr).append("小时")
                .append(minutesStr).append("分钟").append(secondStr).append("秒");
        str = Html.fromHtml(returnString.toString());

//        if (day >= 10) {
//            ((Spannable) str).setSpan(new AbsoluteSizeSpan(16), 2, 3,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ((Spannable) str).setSpan(new AbsoluteSizeSpan(16), 5, 7,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ((Spannable) str).setSpan(new AbsoluteSizeSpan(16), 9, 11,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ((Spannable) str).setSpan(new AbsoluteSizeSpan(16), 13, 14,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        } else {
//            ((Spannable) str).setSpan(new AbsoluteSizeSpan(16), 1, 2,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ((Spannable) str).setSpan(new AbsoluteSizeSpan(16), 4, 6,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ((Spannable) str).setSpan(new AbsoluteSizeSpan(16), 8, 10,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            ((Spannable) str).setSpan(new AbsoluteSizeSpan(16), 12, 13,
//                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
        return str;
    }

    /**
     * 补0操作
     *
     * @param timeStr
     * @return
     */
    private static String timeStrFormat(String timeStr) {
        switch (timeStr.length()) {
            case 1:
                timeStr = "0" + timeStr;
                break;
        }
        return timeStr;
    }
}
