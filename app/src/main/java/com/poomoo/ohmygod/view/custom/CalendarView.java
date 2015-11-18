package com.poomoo.ohmygod.view.custom;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.poomoo.ohmygod.R;

/**
 * 日历控件 功能：获得点选的日期区间
 */
public class CalendarView extends View {
    private final static String TAG = "anCalendar";
    private Date selectedStartDate;
    private Date selectedEndDate;
    private Date curDate; // 当前日历显示的月
    private Date today; // 今天的日期文字显示红色
    private Date showFirstDate, showLastDate; // 日历显示的第一个日期和最后一个日期

    private Calendar calendar;
    private Surface surface;
    private String[] date = new String[35]; // 日历显示数字
    private int curStartIndex, curEndIndex; // 当前显示的日历起始的索引
    private int curMonthDay;//当前月份天数
    private boolean isSelectMore = false;
    private boolean isSigned = false;//是否签到

    public CalendarView(Context context) {
        super(context);
        init();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        intiDate();
        curDate = selectedStartDate = selectedEndDate = today = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        surface = new Surface();
        surface.density = getResources().getDisplayMetrics().density;
        setBackgroundColor(surface.bgColor);
    }

    private void intiDate() {
        for (int i = 0; i < date.length; i++)
            date[i] = "";
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        surface.width = getResources().getDisplayMetrics().widthPixels;
        surface.height = (int) (getResources().getDisplayMetrics().heightPixels * 2 / 5);
//		if (View.MeasureSpec.getMode(widthMeasureSpec) == View.MeasureSpec.EXACTLY) {
//			surface.width = View.MeasureSpec.getSize(widthMeasureSpec);
//		}
//		if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.EXACTLY) {
//			surface.height = View.MeasureSpec.getSize(heightMeasureSpec);
//		}
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(surface.width,
                MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(surface.height,
                MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        Log.d(TAG, "[onLayout] changed:"
                + (changed ? "new size" : "not change") + " left:" + left
                + " top:" + top + " right:" + right + " bottom:" + bottom);
        if (changed) {
            surface.init();
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw");
        // 画框
        canvas.drawPath(surface.boxPath, surface.borderPaint);
        // 年月
        //String monthText = getYearAndmonth();
        //float textWidth = surface.monthPaint.measureText(monthText);
        //canvas.drawText(monthText, (surface.width - textWidth) / 2f,
        //		surface.monthHeight * 3 / 4f, surface.monthPaint);
        // 上一月/下一月
        //canvas.drawPath(surface.preMonthBtnPath, surface.monthChangeBtnPaint);
        //canvas.drawPath(surface.nextMonthBtnPath, surface.monthChangeBtnPaint);
        // 星期
        float weekTextY = surface.weekHeight * 3 / 4f;
        // 星期背景
//		surface.cellBgPaint.setColor(surface.textColor);
//		canvas.drawRect(surface.weekHeight, surface.width, surface.weekHeight, surface.width, surface.cellBgPaint);
        for (int i = 0; i < surface.weekText.length; i++) {
            float weekTextX = i
                    * surface.cellWidth
                    + (surface.cellWidth - surface.weekPaint
                    .measureText(surface.weekText[i])) * 1 / 4f;
            if (i == 0)
                surface.weekPaint.setColor(surface.textColorRed);
            else
                surface.weekPaint.setColor(surface.textColor);
            drawWeekBg(canvas, i, getResources().getColor(R.color.bg_calender_signed));
            canvas.drawText(surface.weekText[i], weekTextX, weekTextY,
                    surface.weekPaint);
        }

        // 计算日期
        calculateDate();
        int todayIndex = -1;
        calendar.setTime(curDate);
        String curYearAndMonth = calendar.get(Calendar.YEAR) + ""
                + calendar.get(Calendar.MONTH);


        calendar.setTime(today);
        String todayYearAndMonth = calendar.get(Calendar.YEAR) + ""
                + calendar.get(Calendar.MONTH);
        if (curYearAndMonth.equals(todayYearAndMonth)) {
            int todayNumber = calendar.get(Calendar.DAY_OF_MONTH);
            todayIndex = curStartIndex + todayNumber - 1;
        }

        for (int i = 0; i < 35; i++) {
            //背景
            if (i == 8 || i == 9 || i == 10) {
                isSigned = true;
                drawDateBg(canvas, i, surface.signedBgColor);
            } else {
                isSigned = false;
                drawDateBg(canvas, i, surface.unSignedBgColor);
            }

            //文字
            int color = surface.textColor;
            if (i % 7 == 0)
                color = surface.textColorRed;
//            if (todayIndex != -1 && i == todayIndex) {
            if (i == 1) {
                color = surface.todayNumberColor;
                drawCurDateBg(canvas, i, date[i]);
            }
            drawCellText(canvas, i, date[i], color);

            //图标
            Bitmap bmp;
            if (!TextUtils.isEmpty(date[i])) {
                if (isSigned)
                    bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_signed);
                else
                    bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_unsigned);
                drawCellIcon(canvas, i, bmp);
            }
        }
        super.onDraw(canvas);
    }

    private void calculateDate() {
        calendar.setTime(curDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int monthStart = dayInWeek;

        monthStart -= 1;  //以日为开头-1，以星期一为开头-2
        curStartIndex = monthStart;
        date[monthStart] = 1 + "";
        showFirstDate = calendar.getTime();
        // this month
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);

        curMonthDay = calendar.get(Calendar.DAY_OF_MONTH);
        for (int i = 1; i < curMonthDay; i++) {
            date[monthStart + i] = i + 1 + "";
        }
        curEndIndex = monthStart + curMonthDay;
        showLastDate = calendar.getTime();
    }

    /**
     * @param canvas
     * @param index
     * @param text
     */
    private void drawCellText(Canvas canvas, int index, String text, int color) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);

        surface.datePaint.setColor(color);
        float cellX = (surface.cellWidth * (x - 1)) + (surface.cellWidth - surface.datePaint.measureText(text)) * 1 / 5f;
        float cellY = surface.weekHeight + (y - 1) * surface.cellHeight + surface.cellHeight * 2 / 5f;
        canvas.drawText(text, cellX, cellY, surface.datePaint);
    }

    /**
     * @param canvas
     * @param index
     * @param bitmap
     */
    private void drawCellIcon(Canvas canvas, int index, Bitmap bitmap) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);

        float cellX = (surface.cellWidth * (x - 1)) + (surface.cellWidth) * 1 / 2f;
        float cellY = surface.weekHeight + (y - 1) * surface.cellHeight + surface.cellHeight * 1 / 2f;
        canvas.drawBitmap(bitmap, cellX, cellY, surface.datePaint);
    }

    /**
     * @param canvas
     * @param i
     * @param color
     */
    private void drawWeekBg(Canvas canvas, int i, int color) {
        surface.cellBgPaint.setColor(color);
        float left = surface.cellWidth * i + surface.borderWidth;
        float top = surface.borderWidth;
        float right = surface.cellWidth * (i + 1) - surface.borderWidth;
        float bottom = surface.weekHeight - surface.borderWidth;
        canvas.drawRect(left, top, right, bottom, surface.cellBgPaint);
    }

    /**
     * @param canvas
     * @param index
     * @param color
     */
    private void drawDateBg(Canvas canvas, int index, int color) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        surface.cellBgPaint.setColor(color);
        float left = surface.cellWidth * (x - 1) + surface.borderWidth;
        float top = surface.weekHeight + (y - 1) * surface.cellHeight + surface.borderWidth;
        float right = left + surface.cellWidth - 2 * surface.borderWidth;
        float bottom = top + surface.cellHeight - 2 * surface.borderWidth;
        canvas.drawRect(left, top, right, bottom, surface.cellBgPaint);
    }

    /**
     * @param canvas
     * @param index
     */
    private void drawCurDateBg(Canvas canvas, int index, String text) {
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        float radius = surface.cellWidth / 3f;
        float left = surface.cellWidth * (x - 1) + surface.cellWidth / 10f;
        float right = left + radius;
        float bottom = surface.weekHeight + (y - 1) * surface.cellHeight + surface.cellHeight * 2 / 5f;
        float top = bottom - radius;
        float rx = radius / 2f;
        float ry = rx;
        canvas.drawRoundRect(new RectF(left, top, right, bottom), rx, ry, surface.curDateBgPaint);
    }


    private int getXByIndex(int i) {
        return i % 7 + 1; // 1 2 3 4 5 6 7
    }

    private int getYByIndex(int i) {
        return i / 7 + 1; // 1 2 3 4 5 6
    }

    // 获得当前应该显示的年月
    public String getYearAndmonth() {
        calendar.setTime(curDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        return year + "-" + month;
    }

    //上一月
    public String clickLeftMonth() {
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, -1);
        curDate = calendar.getTime();
        invalidate();
        return getYearAndmonth();
    }

    //下一月
    public String clickRightMonth() {
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, 1);
        curDate = calendar.getTime();
        invalidate();
        return getYearAndmonth();
    }

    //设置日历时间
    public void setCalendarData(Date date) {
        calendar.setTime(date);
        invalidate();
    }

    //获取日历时间
    public void getCalendatData() {
        calendar.getTime();
    }

    public void setSelectMore(boolean isSelectMore) {
        this.isSelectMore = isSelectMore;
    }


    /**
     * 1. 布局尺寸 2. 文字颜色，大小 3. 当前日期的颜色，选择的日期颜色
     */
    private class Surface {
        public float density;
        public int width; // 整个控件的宽度
        public int height; // 整个控件的高度
        public float weekHeight; // 显示星期的高度
        public float cellWidth; // 日期方框宽度
        public float cellHeight; // 日期方框高度
        public float borderWidth;

        public int bgColor = Color.parseColor("#FFFFFF");
        private int textColor = Color.BLACK;
        private int textColorRed = getResources().getColor(R.color.themeRed);
        private int borderColor = getResources().getColor(R.color.themeGray);
        public int todayNumberColor = getResources().getColor(R.color.white);
        public int signedBgColor = getResources().getColor(R.color.bg_calender_signed);
        public int unSignedBgColor = getResources().getColor(R.color.bg_calender_unsigned);

        public Paint borderPaint;
        public Paint weekPaint;
        public Paint datePaint;
        public Paint curDateBgPaint;
        public Paint cellBgPaint;
        public Path boxPath; // 边框路径

        public final String[] weekText = {getResources().getString(R.string.sun), getResources().getString(R.string.mon), getResources().getString(R.string.tus), getResources().getString(R.string.wen), getResources().getString(R.string.thu), getResources().getString(R.string.fri), getResources().getString(R.string.sat)};


        public void init() {
            float temp = height / 6f;
            weekHeight = (float) ((temp) * 0.7);
            cellHeight = (height - weekHeight) / 5f;
            cellWidth = width / 7f;

            borderPaint = new Paint();
            borderPaint.setColor(borderColor);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderWidth = (float) (0.5 * density);
            borderWidth = borderWidth < 1 ? 1 : borderWidth;
            borderPaint.setStrokeWidth(borderWidth);

            weekPaint = new Paint();
            weekPaint.setColor(textColor);
            weekPaint.setAntiAlias(true);
            float weekTextSize = weekHeight * 0.5f;
            weekPaint.setTextSize(weekTextSize);
            weekPaint.setTypeface(Typeface.DEFAULT_BOLD);

            datePaint = new Paint();
            datePaint.setColor(textColor);
            datePaint.setAntiAlias(true);
            float cellTextSize = cellHeight * 1 / 3f;
            datePaint.setTextSize(cellTextSize);
            datePaint.setTypeface(Typeface.DEFAULT_BOLD);

            curDateBgPaint = new Paint();
            curDateBgPaint.setColor(textColorRed);
            curDateBgPaint.setAntiAlias(true);
            curDateBgPaint.setStyle(Paint.Style.FILL);

            boxPath = new Path();
            boxPath.rLineTo(width, 0);
            boxPath.moveTo(0, weekHeight);
            boxPath.rLineTo(width, 0);
            for (int i = 1; i < 6; i++) {
                boxPath.moveTo(0, weekHeight + i * cellHeight);
                boxPath.rLineTo(width, 0);
                boxPath.moveTo(i * cellWidth, 0);
                boxPath.rLineTo(0, height);
            }
            boxPath.moveTo(6 * cellWidth, 0);
            boxPath.rLineTo(0, height);

            cellBgPaint = new Paint();
            cellBgPaint.setAntiAlias(true);
            cellBgPaint.setStyle(Paint.Style.FILL);
        }
    }
}