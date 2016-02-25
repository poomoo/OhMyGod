package com.poomoo.ohmygod.view.custom;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SeekBar;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.LogUtils;

import org.litepal.util.LogUtil;

/**
 * :ProgressSeekBar
 * TODO(带有数字的水平拖动条)
 */
public class ProgressSeekBar extends SeekBar {

    private String mText;

    private Paint mPaint;

    private final int textsize = 24;

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
            mText = "剩" + (getMax() - getProgress()) + "个";
            Rect bounds = this.getProgressDrawable().getBounds();
            Rect thumbBounds = this.getThumb().getBounds();
            float xText;
            float textLen = mPaint.measureText(mText);
            float spaceLen = thumbBounds.width() - textLen;//左右端距离
//            LogUtils.i("ProgressSeekBar", "onDraw:" + mText + ":" + textLen + ":" + spaceLen);
//            LogUtils.i("ProgressSeekBar", "thumbBounds:" + thumbBounds.left + ":" + thumbBounds.right);
//            xText = bounds.width() * getProgress() / getMax();
//            LogUtils.i("poo", bounds.width() * getProgress() / getMax() + ":" + thumbBounds.width() / 2);
//            if (bounds.width() * getProgress() / getMax() < (thumbBounds.width() / 2))//刚开始
//                this.setThumbOffset(0);
//            else if ((bounds.width() - bounds.width() * getProgress() / getMax()) < (thumbBounds.width() / 2)) {//最后
//                this.setThumbOffset(thumbBounds.width() * 3 / 4);
//
//            } else {//中间
//                this.setThumbOffset(0);
////                if (spaceLen > 0)
////                    xText = bounds.width() * getProgress() / getMax() - thumbBounds.width() / 2 + spaceLen / 2;
////                else
////                    xText = bounds.width() * getProgress() / getMax() - thumbBounds.width() / 2;
//            }

            //改进版
            this.setThumbOffset(0);
            xText = thumbBounds.left + spaceLen / 2;
            if (this.getProgress() == this.getMax()){
                this.setThumbOffset(thumbBounds.width() * 3 / 4);
                xText = bounds.width() - thumbBounds.width() * 3 / 4;
            }
            float yText = bounds.height() * 2 + bounds.height() / 3;
            canvas.drawText(mText, xText, yText, mPaint);

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
