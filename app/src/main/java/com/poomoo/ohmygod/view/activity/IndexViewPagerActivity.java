package com.poomoo.ohmygod.view.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.ViewPagerAdapter;


public class IndexViewPagerActivity extends BaseActivity implements
        OnClickListener, OnPageChangeListener {

    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private Button button;

    // 引导图片资源
    private static final int[] pics = {R.drawable.index1, R.drawable.index2, R.drawable.index3};
    private static final int lenth = pics.length;

    // 底部小店图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        views = new ArrayList<>();

        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        // 初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            // 防止图片不能填满屏幕
            iv.setScaleType(ScaleType.FIT_XY);

            iv.setImageResource(pics[i]);
            views.add(iv);
        }
        vp = (ViewPager) findViewById(R.id.viewpager_viewpager);
        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);

        // 初始化底部小点
//        initDots();

//        button = (Button) findViewById(R.id.index_button);

//        button.setOnClickListener(this);
//        button.setTag("button");

    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.viewpager_ll);
        dots = new ImageView[lenth];

        // 循环取得小点图片
        for (int i = 0; i < lenth; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(false);// 都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(true);// 设置为白色，即选中状态
    }

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= lenth) {
            return;
        }
        vp.setCurrentItem(position);
    }

    /**
     * 当前引导小点的选中
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > lenth - 1 || currentIndex == positon) {
            return;
        }

        dots[positon].setEnabled(true);
        dots[currentIndex].setEnabled(false);

        currentIndex = positon;
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
//        setCurDot(arg0);
        if (arg0 == 3)
            finish();
//            button.setVisibility(View.VISIBLE);
//        else
//            button.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals("button")) {
            Intent i = new Intent(IndexViewPagerActivity.this, MainFragmentActivity.class);
            startActivity(i);
            finish();
        } else {
            int position = (Integer) v.getTag();
            setCurView(position);
            setCurDot(position);
        }
    }
}
