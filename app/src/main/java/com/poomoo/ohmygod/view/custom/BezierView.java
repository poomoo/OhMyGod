/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/26 10:04.
 */
public class BezierView extends View {
    private Point start;
    private Point end;
//    private Paint paint;

    private int mViewWidth;
    private int mViewHeight;
    private float mLevelLine;
    private float mWaveHeight = 80;
    private float mWaveWidth = 200;
    private float mLeftSide;

    private List<Point> mPointsList;
    private Paint mPaint;
    //    private Paint mTextPaint;
    private Path mWavePath;
    private boolean isMeasured = false;

    private float mMoveLen;
    public static final float SPEED = 1.7f;

    private Timer timer;
    private MyTimerTask mTask;

    public BezierView(Context context) {
        super(context);
        Log.i("lmf", "BezierView1");
    }

    public BezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i("lmf", "BezierView2");
        init();

//        start.x =;
//        start.y = 0;


//        end.x = -10;
//        end.y = getResources().getDisplayMetrics().heightPixels;
    }

    private void init() {
        start = new Point(getResources().getDisplayMetrics().widthPixels - 100, 0);
        end = new Point(0, getResources().getDisplayMetrics().heightPixels);

        mPointsList = new ArrayList<>();
        timer = new Timer();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLACK);

//        paint = new Paint() {
//            {
//                setStyle(Paint.Style.STROKE);
//                setStrokeCap(Paint.Cap.ROUND);
//                setStrokeWidth(3.0f);
//                setAntiAlias(true);
//            }
//        };

//        mTextPaint = new Paint();
//        mTextPaint.setColor(Color.WHITE);
//        mTextPaint.setTextAlign(Paint.Align.CENTER);
//        mTextPaint.setTextSize(30);

        mWavePath = new Path();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        // 开始波动
        start();
    }

    private void start() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTask = new MyTimerTask(updateHandler);
        timer.schedule(mTask, 0, 1000);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isMeasured) {
            isMeasured = true;
//            mViewHeight = getMeasuredHeight();
//            mViewWidth = getMeasuredWidth();
            mViewWidth = getResources().getDisplayMetrics().widthPixels;
            mViewHeight = getResources().getDisplayMetrics().heightPixels;
            float itemX = mViewWidth / 4;
            for (int i = 1; i < 5; i++)
                mPointsList.add(new Point(itemX * i, 0));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.i("lmf", "onDraw");
//        final Path path = new Path();
        mWavePath.reset();
        mWavePath.moveTo(start.x, start.y);
        final float x2 = start.x - (end.x + start.x) / 5;
        final float y2 = (end.y + start.y) / 2;
        mWavePath.quadTo(x2, y2, end.x, end.y);
//            Log.i("lmf", "start.x:" + start.x + "start.y:" + start.y + "x2:" + x2 + "y2:" + y2);

        mWavePath.close();
//        Log.i("lmf", "x1:" + start.x + "y1:" + start.y + "x2:" + x2 + "y2:" + y2 + "x3:" + end.x + "y3:" + end.y);
//        canvas.drawPath(path, mPaint);
//        mWavePath.reset();
//        int i = 0;
//        mWavePath.moveTo(mPointsList.get(0).getX(), mPointsList.get(0).getY());
//        for (; i < mPointsList.size() - 2; i = i + 2) {
//            mWavePath.quadTo(mPointsList.get(i + 1).getX(),
//                    mPointsList.get(i + 1).getY(), mPointsList.get(i + 2)
//                            .getX(), mPointsList.get(i + 2).getY());
//        }
//        mWavePath.lineTo(mPointsList.get(i).getX(), mViewHeight);
//        mWavePath.lineTo(mLeftSide, mViewHeight);
//        mWavePath.close();
//
//        // mPaint的Style是FILL，会填充整个Path区域
        canvas.drawPath(mWavePath, mPaint);
        super.onDraw(canvas);
    }

    public void reDraw(Point start, Point end) {
        this.start = start;
        this.end = end;
        invalidate();
    }

    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            for (int i = 3; i >= 0; i--) {
                start = mPointsList.get(i);
                invalidate();
            }

        }

    };

    private void resetPoints() {
        mLeftSide = -mWaveWidth;
        for (int i = 0; i < mPointsList.size(); i++) {
            mPointsList.get(i).setX(i * mWaveWidth / 4 - mWaveWidth);
        }
    }

    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendMessage(handler.obtainMessage());
        }

    }

    class Point {
        private float x;
        private float y;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

    }
}
