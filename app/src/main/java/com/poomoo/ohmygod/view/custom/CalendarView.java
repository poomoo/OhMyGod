package com.poomoo.ohmygod.view.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.poomoo.ohmygod.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日历控件
 */
public class CalendarView extends View {
    private final static String TAG = "CalendarView";
    private Date selectedStartDate;
    private Date selectedEndDate;
    private Date curDate; // 当前日历显示的月
    private Date today; // 今天的日期文字显示红色

    private Calendar calendar;
    private Surface surface;
    private String[] date = new String[36]; // 日历显示数字
    private int column = 7; //日历列数
    private int row = 0; //日历行数(除去星期)
    private int dayInWeek;
    private int dayInMonth;
    private int curStartIndex, curEndIndex; // 当前显示的日历起始的索引
    private int curMonthDay;//当前月份天数
    private boolean isSigned = false;//是否签到
    private List<String> dateList;//已签到的日期集合
    private List<Integer> isSignedList = new ArrayList<>();//已签到的日期下标集合
    private SimpleDateFormat format;

    public CalendarView(Context context) {
        super(context);
        init();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        curDate = selectedStartDate = selectedEndDate = today = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        surface = new Surface();
        surface.density = getResources().getDisplayMetrics().density;
        setBackgroundColor(surface.bgColor);
    }

    private void intiDate() {
        date = new String[column * row + 1];
        for (int i = 0; i < date.length; i++)
            date[i] = "";
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        surface.width = getResources().getDisplayMetrics().widthPixels;
        surface.height = getResources().getDisplayMetrics().heightPixels * 2 / 5;
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
        Log.i(TAG, "[onLayout] changed:"
                + (changed ? "new size" : "not change") + " left:" + left
                + " top:" + top + " right:" + right + " bottom:" + bottom);
        if (changed) {
            calculateRow();
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "onDraw");
        // 画框
        canvas.drawPath(surface.boxPath, surface.borderPaint);

        // 星期
        float weekTextY = surface.weekHeight * 3 / 4f;

        for (int i = 0; i < surface.weekText.length; i++) {
            float weekTextX = i
                    * surface.cellWidth
                    + (surface.cellWidth - surface.weekPaint
                    .measureText(surface.weekText[i])) * 1 / 4f;
            if (i == 0)
                surface.weekPaint.setColor(surface.textColorRed);
            else
                surface.weekPaint.setColor(surface.textColor);
            drawWeekBg(canvas, i, surface.signedBgColor);
            canvas.drawText(surface.weekText[i], weekTextX, weekTextY,
                    surface.weekPaint);
        }
        // 计算日期
        calculateDate();
        calendar.setTime(curDate);
        String curYearAndMonth = calendar.get(Calendar.YEAR) + ""
                + calendar.get(Calendar.MONTH);
        calendar.setTime(today);
        String todayYearAndMonth = calendar.get(Calendar.YEAR) + ""
                + calendar.get(Calendar.MONTH);
        int todayIndex = -1;
        if (curYearAndMonth.equals(todayYearAndMonth)) {
            int todayNumber = calendar.get(Calendar.DAY_OF_MONTH);
            todayIndex = curStartIndex + todayNumber - 1;
        }

        for (int i = 0; i < column * row; i++) {
            //背景
            if (isSignedList.contains(i)) {
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
            if (todayIndex != -1 && i == todayIndex) {
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
        intiDate();
        curStartIndex = dayInWeek;
        date[dayInWeek] = 1 + "";
        Log.i(TAG, curMonthDay + "");
        for (int i = 1; i < curMonthDay; i++) {
            date[dayInWeek + i] = i + 1 + "";
        }
        //计算已签到的日期下标
        if (dateList != null) {
            int len = dateList.size();
            format = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < len; i++) {
                try {
                    Date date = format.parse(dateList.get(i));
                    calendar.setTime(date);
                    dayInMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    isSignedList.add(dayInMonth + curStartIndex - 1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        curEndIndex = dayInWeek + curMonthDay;
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
        float cellY = surface.weekHeight + (y - 1) * surface.cellHeight + surface.cellHeight * 2 / 5f;
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
        float textSize = surface.datePaint.measureText(text);
        float margin;
        float radius;
        float offSetX;
        float offSetY;

        if (text.length() == 1) {
            radius = textSize * 7 / 4;
            margin = radius / 7f;
            offSetX = textSize / 1.9f;
            offSetY = textSize / 1.7f;

        } else {
            radius = textSize;
            margin = radius / 5f;
            offSetX = textSize / 2f;
            offSetY = textSize / 3f;
        }
        radius += margin;

        float cellX = (surface.cellWidth * (x - 1)) + (surface.cellWidth - textSize) * 1 / 5f + offSetX;
        float cellY = surface.weekHeight + (y - 1) * surface.cellHeight + surface.cellHeight * 2 / 5f - offSetY;

        canvas.drawCircle(cellX, cellY, radius / 2, surface.curDateBgPaint);
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
    public void
    setCalendarData(Date date, List<String> dateList) {
        Log.i(TAG, "Date:" + date);
        if (dateList != null)
            this.dateList = dateList;
        calendar.setTime(date);
        curDate = calendar.getTime();
        calculateRow();
        invalidate();
    }

    private void calculateRow() {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        dayInWeek -= 1;

        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        curMonthDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (dayInWeek + curMonthDay > 35)
            row = 6;
        else
            row = 5;
        Log.i(TAG, "calculateRow:" + row + "");
        surface.init(row + 1);
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
        public float cellTextSize;//字体大小

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


        public void init(int row) {
            Log.i(TAG, "init:" + row);
            float temp = height / row;
            weekHeight = (float) ((temp) * 0.7);
            cellHeight = (height - weekHeight) / (row - 1);
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
            cellTextSize = cellHeight * 1 / 3f;
            datePaint.setTextSize(cellTextSize);
            datePaint.setTypeface(Typeface.DEFAULT_BOLD);

            boxPath = new Path();
            boxPath.rLineTo(width, 0);
            boxPath.moveTo(0, weekHeight);
            boxPath.rLineTo(width, 0);
            for (int i = 1; i < row; i++) {
                Log.i(TAG, "countBox:" + row + ":" + i);
                //横线
                boxPath.moveTo(0, weekHeight + i * cellHeight);
                boxPath.rLineTo(width, 0);
            }
            for (int i = 1; i < column; i++) {
                //竖线
                boxPath.moveTo(i * cellWidth, 0);
                boxPath.rLineTo(0, height);
            }


            curDateBgPaint = new Paint();
            curDateBgPaint.setColor(textColorRed);
            curDateBgPaint.setAntiAlias(true);
            curDateBgPaint.setStyle(Paint.Style.FILL);

            cellBgPaint = new Paint();
            cellBgPaint.setAntiAlias(true);
            cellBgPaint.setStyle(Paint.Style.FILL);
        }
    }
}
