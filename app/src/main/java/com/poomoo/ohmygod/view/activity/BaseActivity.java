package com.poomoo.ohmygod.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
    //日志标签
    public String TAG = getClass().getSimpleName();
    //进度对话框
    public ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        application = (MyApplication) this.getApplication();
        appAction = application.getAppAction();
        // 去掉默认标题栏
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
    }

    protected void initView() {
    }

    protected void initTitleBar() {
    }

    /**
     * 统一头部条
     *
     * @return lHeaderViewHolder 头部条对象
     */
    protected HeaderViewHolder getHeaderView() {
        HeaderViewHolder headerViewHolder = new HeaderViewHolder();
        headerViewHolder.titleTxt = (TextView) findViewById(R.id.txt_titleBar_name);
        headerViewHolder.backImg = (ImageView) findViewById(R.id.img_titleBar_back);
        headerViewHolder.rightImg = (ImageView) findViewById(R.id.img_titleBar_right);
        headerViewHolder.rightTxt = (TextView) findViewById(R.id.txt_titleBar_right);
        return headerViewHolder;
    }

    protected class HeaderViewHolder {
        public TextView titleTxt;//标题
        public TextView rightTxt;//右边标题
        public ImageView backImg;//返回键
        public ImageView rightImg;//右边图标
    }

    /**
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        Intent intent = new Intent(this, pClass);
        this.startActivity(intent);
    }

    /**
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        this.startActivity(intent);
    }

    /**
     * @param pClass
     * @param requestCode
     */
    protected void openActivityForResult(Class<?> pClass, int requestCode) {
        Intent intent = new Intent(this, pClass);
        startActivityForResult(intent, requestCode);
    }

    /**
     * @param pClass
     * @param requestCode
     */
    protected void openActivityForResult(Class<?> pClass, Bundle pBundle, int requestCode) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 显示进度对话框
     *
     * @param msg
     */
    protected void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(msg);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
//        progressDialog.setOnKeyListener(new OnKeyListener() {
//
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                // TODO Auto-generated method stub
//                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//                    progressDialog.dismiss();
//                    progressDialog = null;
//                    Thread thread = HttpUtil.thread;
//                    HttpUtil.thread = null;
//                    thread.interrupt();
//                    finish();
//                }
//                return false;
//            }
//        });
    }

    /**
     * 关闭对话框
     */
    protected void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }

    }
}
