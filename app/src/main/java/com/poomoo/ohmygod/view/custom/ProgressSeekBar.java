package com.poomoo.ohmygod.view.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SeekBar;

import com.poomoo.ohmygod.R;

/**
 * :ProgressSeekBar
 * TODO(带有数字的水平拖动条)
 */
public class ProgressSeekBar extends SeekBar {

    private String mText;

    private Paint mPaint;

    private final int textsize = 30;

    private boolean ishide;

    public ProgressSeekBar(Context context) {
        super(context);
        init();
    }

    public ProgressSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    // 屏蔽滑动
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    // 修改setpadding 使其在外部调用的时候无效
    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
    }

    // 初始化
    private void init() {
        initDraw();
        setOnSeekBarChangeListener(new onSeekBarChangeListener());
    }

    private void initDraw() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextSize(textsize);
        mPaint.setColor(getResources().getColor(R.color.white));
    }


    private class onSeekBarChangeListener implements OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            setIshide(true);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub
            setIshide(false);
        }

    }

    protected synchronized void onDraw(Canvas canvas) {

        try {
            super.onDraw(canvas);
            if (ishide == true) {
                mText = (getProgress() * 100 / getMax()) + "%";
//                mTextWidth = mPaint.measureText(mText);
                Rect bounds = this.getProgressDrawable().getBounds();
                float xText;
                Log.i("lmf", "getProgress:" + getProgress());
                if (getProgress() >= 10 && getProgress() < 90)
                    xText = bounds.width() * getProgress() / getMax() - getTextHei();
                else if (getProgress() >= 90 && getProgress() < 98)
                    xText = bounds.width() * getProgress() / getMax() - getTextHei();
                else if (getProgress() >= 98 && getProgress() < 100)
                    xText = bounds.width() * getProgress() / getMax() - getTextHei() - 8;
                else if (getProgress() == 100)
                    xText = bounds.width() * getProgress() / getMax() - getTextHei() - 30;
                else
                    xText = bounds.width() * getProgress() / getMax();
                Log.i("lmf", "xText:" + xText + ":" + bounds.width());
//                Log.i("lmf", "y:" + getTextHei() + ":" + bounds.height());
                float yText = getTextHei() / 1.5f;
//                Log.i("lmf", "yText:" + yText);
                canvas.drawText(mText, xText, yText, mPaint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 替代setpadding
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setMyPadding(int left, int top, int right, int bottom) {
        setPadding(left, top, right, bottom);
    }

    private float getTextHei() {
        FontMetrics fm = mPaint.getFontMetrics();
        return (float) Math.ceil(fm.descent - fm.top) + 2;
    }


    public void setIshide(boolean ishide) {
        this.ishide = ishide;
    }


}
