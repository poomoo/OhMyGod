package com.poomoo.ohmygod.utils;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.provider.CalendarContract;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.other.CountDownListener;

import org.w3c.dom.Text;

import java.util.List;

/**
 * 倒计时器
 * 作者: 李苜菲
 * 日期: 2015/11/16 15:49.
 */
public class TimeCountDownUtil extends CountDownTimer {
    private String TAG = this.getClass().getSimpleName();
    private View view;
    private List<TextView> textViewList;
    private long millisUntilFinished;
    private CountDownListener countDownListener;
    private boolean isList;//是否传入list
    private String flag = "";//1-活动倒计时 2-其他倒计时

    // 在这个构造方法里需要传入三个参数，一个是Activity，一个是总的时间millisInFuture，一个是countDownInterval，然后就是你在哪个按钮上做这个事，就把这个按钮传过来就可以了
    public TimeCountDownUtil(long millisInFuture, long countDownInterval, List<TextView> textViewList, final CountDownListener countDownListener) {
        super(millisInFuture, countDownInterval);
        this.textViewList = textViewList;
        this.countDownListener = countDownListener;
        this.isList = true;
    }

    public TimeCountDownUtil(long millisInFuture, long countDownInterval, View view, String flag) {
        super(millisInFuture, countDownInterval);
        this.view = view;
        this.isList = false;
        this.flag = flag;
    }

    public void setTextViewList(List<TextView> textViewList, final CountDownListener countDownListener) {
        this.textViewList = textViewList;
        textViewList.add((TextView) this.view);
        this.isList = true;
        this.countDownListener = countDownListener;
    }

    public TimeCountDownUtil(long millisInFuture, long countDownInterval, View view, final CountDownListener countDownListener) {
        super(millisInFuture, countDownInterval);
        this.view = view;
        this.countDownListener = countDownListener;
        this.isList = false;
    }


    @Override
    public void onTick(long millisUntilFinished) {
        this.millisUntilFinished = millisUntilFinished;
        Spanned spanned = dealTime(millisUntilFinished / 1000);
//        SpannableString ss;
//        ss = new SpannableString("倒计时：" + spanned);
//        //设置字体颜色
//        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 0,
//                4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#FCE023")), 4,
//                4 + spanned.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (isList)
            for (TextView textView : textViewList)
                textView.setText(spanned);
        else {
            if (view instanceof Button) {
                view.setClickable(false);// 设置不能点击
                ((Button) view).setText(millisUntilFinished / 1000 + "s");// 设置倒计时时间
                ((Button) view).setTextColor(Color.parseColor("#E81540"));
                // 设置按钮为灰色，这时是不能点击的
                view.setBackgroundResource(R.drawable.bg_get_code_pressed);
            } else if (view instanceof TextView) {
//                if (view.getTag().equals("TextView")) {
//                    view.setClickable(false);// 设置不能点击
//                    ((TextView) view).setText(millisUntilFinished / 1000 + "s");
//                } else {
//                    ((TextView) view).setText(spanned);
//                }
                if (flag.equals("1")) {
                    ((TextView) view).setText(spanned);
                } else {
                    view.setClickable(false);// 设置不能点击
                    ((TextView) view).setText(millisUntilFinished / 1000 + "s");
                }

            }
        }

    }

    @Override
    public void onFinish() {
        this.millisUntilFinished = 0;
        if (this.countDownListener != null)
            countDownListener.onFinish(1);
        if (isList) {
            for (TextView textView : textViewList)
                textView.setText("活动已开始");
        } else {
            if (this.countDownListener != null) {
                countDownListener.onFinish(1);
                return;
            }

            if (view instanceof TextView) {
                if (view.getTag().equals("TextView")) {
                    view.setClickable(true);// 设置点击
                    ((TextView) view).setText("重新获取");
                } else {
                    ((TextView) view).setText("活动已开始");
                    ((TextView) view).setTextColor(Color.parseColor("#E81540"));
                }

            }

//            if (view instanceof Button) {
//                ((Button) view).setText("重新获取");
//                view.setClickable(true);// 重新获得点击
//                view.setBackgroundResource(R.drawable.selector_get_code_button);// 还原背景色
//                ((Button) view).setTextColor(Color.parseColor("#FFFFFF"));
//            }
        }

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
//        Log.i(TAG, "time:" + time + "day:" + day + "hours:" + hours + "minutes:" + minutes + "second:" + second);
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

    public long getMillisUntilFinished() {
        return millisUntilFinished;
    }

}