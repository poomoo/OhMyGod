package com.poomoo.ohmygod.view.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poomoo.core.AppAction;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.application.MyApplication;

/**
 * Activity基类
 * 作者: 李苜菲
 * 日期: 2015/11/11 11:30.
 */
public class BaseActivity extends Activity {
    // 上下文实例
    public Context context;
    // 应用全局的实例
    public MyApplication application;
    // 核心层的Action实例
    public AppAction appAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        application = (MyApplication) this.getApplication();
        appAction = application.getAppAction();
        // 去掉默认标题栏
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 统一头部条
     *
     * @return lHeaderViewHolder 头部条对象
     */
    protected HeaderViewHolder getHeaderView() {
        HeaderViewHolder headerViewHolder = new HeaderViewHolder();
        headerViewHolder.title = (TextView) findViewById(R.id.titlebar_textView_name);
        headerViewHolder.back = (ImageView) findViewById(R.id.titlebar_imageView_back);
        headerViewHolder.right = (ImageView) findViewById(R.id.titlebar_imageView_right);
        return headerViewHolder;
    }

    protected class HeaderViewHolder {
        public TextView title;//标题
        public ImageView back;//返回键
        public ImageView right;//右边图标
    }
}
