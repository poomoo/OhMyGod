package com.poomoo.ohmygod.view.custom;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * 跑马灯效果<向上方向>的TextView
 * Created by jayuchou on 15/7/9.
 */
public class UpMarqueeTextView extends TextView implements Animator.AnimatorListener {

    private static final String TAG = "UpMarqueeTextView";

    private static final int ANIMATION_DURATION = 800;
    private float height;
    private AnimatorSet mAnimatorStartSet;
    private AnimatorSet mAnimatorEndSet;
    private String mText;

    public UpMarqueeTextView(Context context) {
        super(context);
    }

    public UpMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        height = getHeight();// 确保view的高度

    }

    /**
     * --- 初始化向上脱离屏幕的动画效果 ---
     */
    private void initStartAnimation() {
//        Log.i(TAG, "--- initStartAnimation ---" + height);
        ObjectAnimator translate = ObjectAnimator.ofFloat(this, "translationY", 0, -(height * 2 / 3));
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
        mAnimatorStartSet = new AnimatorSet();
        mAnimatorStartSet.play(translate).with(alpha);
        mAnimatorStartSet.setDuration(ANIMATION_DURATION);
        mAnimatorStartSet.addListener(this);
    }

    /**
     * --- 初始化从屏幕下面向上的动画效果 ---
     */
    private void initEndAnimation() {
//        Log.i(TAG, "--- initEndAnimation ---" + height);
        ObjectAnimator translate = ObjectAnimator.ofFloat(this, "translationY", height, 0);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
        mAnimatorEndSet = new AnimatorSet();
        mAnimatorEndSet.play(translate).with(alpha);
        mAnimatorEndSet.setDuration(ANIMATION_DURATION);
    }

    /**
     * --- 设置内容 ---
     **/
    public void setText(String text) {
        Log.i(TAG, "--- setText ---" + text);
        if (TextUtils.isEmpty(text)) {
            Log.e(TAG, "--- 请确保text不为空 ---");
            return;
        }
        mText = text;

//        Log.i(TAG, "--- setText ---mAnimatorStartSet" + mAnimatorStartSet);
        if (null == mAnimatorStartSet) {
            initStartAnimation();
        }
        mAnimatorStartSet.start();
    }


    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        super.setText(mText);
        if (null == mAnimatorEndSet) {
            initEndAnimation();
        }
        mAnimatorEndSet.start();
    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }
}
