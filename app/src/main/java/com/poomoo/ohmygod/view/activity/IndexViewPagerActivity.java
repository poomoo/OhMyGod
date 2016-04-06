package com.poomoo.ohmygod.view.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.PicBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.ViewPagerAdapter;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.picUtils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class IndexViewPagerActivity extends BaseActivity implements
        OnClickListener, OnPageChangeListener, View.OnTouchListener {

    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private MyPagerAdapter myPagerAdapter;
    private List<View> views;

    // 底部小店图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex = 0;

    private TextView clickInTxt;
    private String PARENT;
    private float x1 = 0;
    private float x2 = 0;
    private List<PicBO> picBOList;
    private LinearLayout.LayoutParams mParams;
    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList = new ArrayList<>();
    private DisplayImageOptions defaultOptions;
    private String bootPicPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ohmygod/" + "index";
    private int index = 0;
    private boolean isExist = false;//引导页是否存在本地 默认不存在
    private int totalNum = 0;//图片的总张数

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);

        PARENT = getIntent().getStringExtra(getString(R.string.intent_parent));
        clickInTxt = (TextView) findViewById(R.id.txt_clickIn);

        views = new ArrayList<>();
        defaultOptions = new DisplayImageOptions.Builder() //
                .showImageOnLoading(null)//
                .showImageForEmptyUri(null)//
                .showImageOnFail(null)//
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//
        mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        if (PARENT.equals("index")) {
            //先清除原有的引导页的缓存图片
            while (1 == 1) {
                File f = new File(bootPicPath + index++ + ".jpg");
                if (f.exists()) {
                    f.delete();
                } else
                    break;
            }
            getData();
        } else {
            // 初始化引导图片列表
            while (1 == 1) {
                File f = new File(bootPicPath + index + ".jpg");
                if (f.exists()) {
                    ImageView iv = new ImageView(this);
                    iv.setLayoutParams(mParams);
                    iv.setAdjustViewBounds(true);
                    // 防止图片不能填满屏幕
                    iv.setScaleType(ScaleType.CENTER_CROP);
                    iv.setImageBitmap(FileUtils.readBitmapByPath(bootPicPath + index++ + ".jpg"));
                    views.add(iv);
                    isExist = true;
                } else
                    break;
            }
            LogUtils.i(TAG, "isExist:" + isExist);
            if (!isExist)
                getData();
            else {
                vp = (ViewPager) findViewById(R.id.viewpager_viewpager);
                // 初始化Adapter
                vpAdapter = new ViewPagerAdapter(views);
                vp.setAdapter(vpAdapter);
                views.get(index - 1).setOnClickListener(this);
                if (PARENT.equals("index"))
                    views.get(index - 1).setOnTouchListener(this);
            }

            // 初始化底部小点
//                initDots();
        }
    }

//    private void initDots() {
//        LinearLayout ll = (LinearLayout) findViewById(R.id.viewpager_ll);
//        dots = new ImageView[lenth];
//
//        // 循环取得小点图片
//        for (int i = 0; i < lenth; i++) {
//            dots[i] = (ImageView) ll.getChildAt(i);
//            dots[i].setEnabled(false);// 都设为灰色
//            dots[i].setOnClickListener(this);
//            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
//        }
//
//        currentIndex = 0;
//        dots[currentIndex].setEnabled(true);// 设置为白色，即选中状态
//    }

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= totalNum) {
            return;
        }
        vp.setCurrentItem(position);
    }

    /**
     * 当前引导小点的选中
     */
//    private void setCurDot(int positon) {
//        if (positon < 0 || positon > lenth - 1 || currentIndex == positon) {
//            return;
//        }
//        dots[positon].setEnabled(true);
//        dots[currentIndex].setEnabled(false);
//        currentIndex = positon;
//    }

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
        currentIndex = arg0;
        LogUtils.i(TAG, "onPageSelected:" + currentIndex);
    }

    @Override
    public void onClick(View v) {
        if (PARENT.equals("index")) {
            LogUtils.i(TAG, "onClick:" + currentIndex + ":" + totalNum);
            if (currentIndex < totalNum - 1)
                setCurView(currentIndex + 1);
            else {
                openActivity(MainFragmentActivity.class);
                finish();
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
        LogUtils.i(TAG, "onTouch:" + event.getAction());
        //继承了Activity的onTouchEvent方法，直接监听点击事件
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            LogUtils.i(TAG, "ACTION_DOWN:" + x1);
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //当手指离开的时候
            x2 = event.getX();
            LogUtils.i(TAG, "ACTION_MOVE:" + "x1:" + x1 + " x2:" + x2);
            if ((x1 - x2 > 5)) {//向左滑
                if (PARENT.equals("index")) {
                    openActivity(MainFragmentActivity.class);
                    finish();
                }
                return true;
            }
        }
        return false;
    }

    private void getData() {
        this.appAction.getBootPics(2, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                picBOList = data.getObjList();
                totalNum = picBOList.size();
                // 初始化引导图片列表
                for (int i = 0; i < totalNum; i++) {
                    ImageView iv = new ImageView(IndexViewPagerActivity.this);
                    iv.setTag(picBOList.get(i).getPicture());
                    iv.setLayoutParams(mParams);
                    iv.setAdjustViewBounds(true);
                    // 防止图片不能填满屏幕
                    iv.setScaleType(ScaleType.CENTER_CROP);
//                    iv.setImageResource(R.mipmap.ic_launcher);

                    imageViewsList.add(iv);
                    views.add(iv);
                    iv.setOnClickListener(IndexViewPagerActivity.this);
                }
                vp = (ViewPager) findViewById(R.id.viewpager_viewpager);
                // 初始化Adapter
                myPagerAdapter = new MyPagerAdapter();
                vp.setAdapter(myPagerAdapter);
                vp.setOnPageChangeListener(IndexViewPagerActivity.this);
//                views.get(picBOList.size() - 1).setOnClickListener(IndexViewPagerActivity.this);
                views.get(picBOList.size() - 1).setOnTouchListener(IndexViewPagerActivity.this);
            }

            @Override
            public void onFailure(int errorCode, String message) {

            }
        });
    }

    /**
     * 填充ViewPager的页面适配器
     */
    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final ImageView imageView = imageViewsList.get(position);
            ImageLoader.getInstance().loadImage(imageView.getTag() + "", defaultOptions, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    imageView.setImageBitmap(loadedImage);
                    FileUtils.saveBitmapByPath(loadedImage, bootPicPath + position + ".jpg");
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                }
            });
//            ImageLoader.getInstance().displayImage(imageView.getTag() + "", imageView, defaultOptions);
            container.addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
//            LogUtils.i(TAG, "imageViewsList.size():" + imageViewsList.size());
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LogUtils.i(TAG, "onKeyDown" + PARENT);
            if (PARENT.equals("index"))
                return true;
        }
        LogUtils.i(TAG, "onKeyDown11" + PARENT);
        return super.onKeyDown(keyCode, event);
    }
}
