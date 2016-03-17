package com.poomoo.ohmygod.view.custom.pullable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.poomoo.ohmygod.utils.LogUtils;

public class PullableScrollView extends ScrollView implements Pullable {

    public PullableScrollView(Context context) {
        super(context);
    }

    public PullableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown() {
        if (getScrollY() == 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean canPullUp() {
        View view = getChildAt(getChildCount() - 1);
        int d = view.getBottom();
        LogUtils.i("PullableScrollView", "d:" + d);
        d -= (getHeight() + getScrollY());
        LogUtils.i("PullableScrollView", "getScrollY:" + getScrollY() + "getHeight():" + getHeight() + "d:" + d + "getMeasuredHeight:" + getMeasuredHeight());
        if (getScrollY() == 0) {
            if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
                return true;
        } else if (d == 0)
            return true;
        return false;
    }

}
