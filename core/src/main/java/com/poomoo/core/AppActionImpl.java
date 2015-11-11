package com.poomoo.core;

import android.content.Context;

import com.poomoo.api.Api;
import com.poomoo.api.ApiImpl;

/**
 * AppAction接口的实现类
 * 作者: 李苜菲
 * 日期: 2015/11/11 11:14.
 */
public class AppActionImpl implements AppAction {
    private Context context;
    private Api api;

    public AppActionImpl(Context context) {
        this.context = context;
        this.api = new ApiImpl();
    }
}
