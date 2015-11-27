/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.view.custom.BezierView;

import org.w3c.dom.Text;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/26 10:22.
 */
public class TestActivity extends BaseActivity {
    private TextView startTxt;
    private TextView endTxt;
    private BezierView bezierView;
    private Point start;
    private Point end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initView();
    }

    @Override
    protected void initView() {
        startTxt = (TextView) findViewById(R.id.txt_start);
        endTxt = (TextView) findViewById(R.id.txt_end);
        bezierView = (BezierView) findViewById(R.id.BezierView);
    }

    public void toTest(View view) {
        int[] location = new int[2];
        startTxt.getLocationOnScreen(location);

        start = new Point();
        start.x = location[0];
        start.y = location[1];

        location = new int[2];
        endTxt.getLocationOnScreen(location);

        end = new Point();
        end.x = location[0];
        end.y = location[1];

//        bezierView.reDraw(start, end);
        Log.i("lmf", "start:" + start + "end" + end);
    }
}
