package com.poomoo.ohmygod.application;

import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.poomoo.core.AppAction;
import com.poomoo.core.AppActionImpl;
import com.poomoo.ohmygod.R;

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

        initImageLoader();
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                .showImageForEmptyUri(R.mipmap.ic_launcher) //
                .showImageOnFail(R.mipmap.ic_launcher) //
                .cacheInMemory(true) //
                .cacheOnDisk(false) //
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//
        ImageLoaderConfiguration config = new ImageLoaderConfiguration//
                .Builder(getApplicationContext())//
                .defaultDisplayImageOptions(defaultOptions)//
                .writeDebugLogs()//
                .build();//
        ImageLoader.getInstance().init(config);
    }

    public AppAction getAppAction() {
        return appAction;
    }


}
