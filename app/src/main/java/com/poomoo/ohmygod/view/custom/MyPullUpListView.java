package com.poomoo.ohmygod.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import com.poomoo.ohmygod.R;

public class MyPullUpListView extends ListView implements AbsListView.OnScrollListener {

    private View footer;

    private boolean isLoading;// 判断是否正在加载  

    private OnLoadListener onLoadListener;
    private LayoutInflater inflater;
    /**
     * 按下时的y坐标
     */
    private int mYDown;
    /**
     * 抬起时的y坐标, 与mYDown一起用于滑动到底部时判断是上拉还是下拉
     */
    private int mLastY;

    /**
     * 滑动到最下面时的上拉操作
     */

    private int mTouchSlop;

    public MyPullUpListView(Context context) {
        super(context);
        initView(context);
    }

    public MyPullUpListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initView(context);
    }

    public MyPullUpListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    // 加载更多监听  
    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }

    // 初始化组件
    private void initView(Context context) {
        inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.listview_footer, null);

        this.setOnScrollListener(this);
    }

    public void onLoad() {
        if (onLoadListener != null) {
            onLoadListener.onLoad();
        }
    }

    // 用于加载更多结束后的回调  
    public void onLoadComplete() {
        setLoading(false);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        ifNeedLoad(view, scrollState);
//        if (canLoad()) {
//            onLoad();
//            isLoading = true;
//        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                mYDown = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                // 移动
                mLastY = (int) event.getRawY();
                break;

            case MotionEvent.ACTION_UP:
                // 抬起
                if (canLoad()) {
                    loadData();
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    /**
     * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
     */
    private void loadData() {
        if (onLoadListener != null) {
            // 设置状态
            setLoading(true);
            onLoadListener.onLoad();
        }
    }

    /**
     * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
     *
     * @return
     */
    private boolean canLoad() {
        return isBottom() && !isLoading && isPullUp();
    }

    /**
     * 判断是否到了最底部
     */
    private boolean isBottom() {

        if (this.getAdapter() != null) {
            return this.getLastVisiblePosition() == (this
                    .getAdapter().getCount() - 1);
        }
        return false;
    }

    /**
     * 是否是上拉操作
     *
     * @return
     */
    private boolean isPullUp() {
        return (mYDown - mLastY) >= mTouchSlop;
    }

    // 根据listview滑动的状态判断是否需要加载更多  
    private void ifNeedLoad(AbsListView view, int scrollState) {
        try {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    && !isLoading
                    && view.getLastVisiblePosition() == view.getPositionForView(footer) && isPullUp()) {
                onLoad();
                isLoading = true;
            }
        } catch (Exception e) {
        }
    }

    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if (loading) {
            this.addFooterView(footer);
        } else {
            this.removeFooterView(footer);
            mYDown = 0;
            mLastY = 0;
        }
    }

    /* 
     * 定义加载更多接口 
     */
    public interface OnLoadListener {
        void onLoad();
    }

} 