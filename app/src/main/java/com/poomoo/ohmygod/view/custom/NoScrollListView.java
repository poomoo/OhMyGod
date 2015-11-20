package com.poomoo.ohmygod.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Android_PM on 2015/11/3.
 * 自定义的listview——用于嵌套在scrollview中
 * 解决的问题：ListView显示不全，只显示了一行，重写ListView来解决
 */
public class NoScrollListView extends ListView {
    public NoScrollListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public NoScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
