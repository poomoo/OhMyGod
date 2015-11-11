package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.poomoo.ohmygod.R;

/**
 * Created by Android_PM on 2015/11/10.
 */
public class MainFragmentActivity extends
        FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉默认标题栏
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }


}
