package com.poomoo.ohmygod.application;

import android.app.Application;

import com.poomoo.core.AppAction;
import com.poomoo.core.AppActionImpl;

/**
 * 自定义Application
 * 作者: 李苜菲
 * 日期: 2015/11/11 11:26.
 */
public class MyApplication extends Application {
    private AppAction appAction;

    @Override
    public void onCreate() {
        super.onCreate();
        appAction = new AppActionImpl(this);
    }

    public AppAction getAppAction() {
        return appAction;
    }
}
