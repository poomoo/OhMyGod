package com.poomoo.ohmygod.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
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
public class BaseActivity extends AppCompatActivity {
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        getActivityInFromRight();
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
        getActivityInFromRight();
    }

    /**
     * @param pClass
     * @param requestCode
     */
    protected void openActivityForResult(Class<?> pClass, int requestCode) {
        Intent intent = new Intent(this, pClass);
        startActivityForResult(intent, requestCode);
        getActivityInFromRight();
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
        getActivityInFromRight();
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
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                    progressDialog.dismiss();
                    progressDialog = null;
                    finish();
                }
                return false;
            }
        });
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

    /**
     * activity切换-> 向左进(覆盖)
     */
    protected void getActivityInFromRight() {
        overridePendingTransition(R.anim.activity_in_from_right,
                R.anim.activity_center);
    }


    /**
     * activity切换-> 向右出(抽出)
     */
    protected void getActivityOutToRight() {
        overridePendingTransition(R.anim.activity_center,
                R.anim.activity_out_to_right);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            getActivityOutToRight();
        }
        return true;
    }

    /**
     * 增加Activity
     *
     * @param activity 需要加入的activity
     */
    public void addActivityToArrayList(Activity activity) {
        application.getActivityList().add(activity);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(getString(R.string.sp_userId), application.getUserId());
        outState.putString(getString(R.string.sp_sex), application.getSex());
        outState.putString(getString(R.string.sp_nickName), application.getNickName());
        outState.putString(getString(R.string.sp_realName), application.getRealName());
        outState.putString(getString(R.string.sp_age), application.getAge());
        outState.putString(getString(R.string.sp_phoneNum), application.getTel());
        outState.putString(getString(R.string.sp_address), application.getAddress());
        outState.putString(getString(R.string.sp_isAdvancedUser), application.getIsAdvancedUser());
        outState.putString(getString(R.string.sp_idCardNum), application.getIdCardNum());
        outState.putString(getString(R.string.sp_idFrontPic), application.getIdFrontPic());
        outState.putString(getString(R.string.sp_idOpsitePic), application.getIdOpsitePic());
        outState.putString(getString(R.string.sp_bankCardNum), application.getBankCardNum());
        outState.putString(getString(R.string.sp_bankName), application.getBankName());
        outState.putString(getString(R.string.sp_channelId), application.getChannelId());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            application.setUserId(savedInstanceState.getString(getString(R.string.sp_userId)));
            application.setSex(savedInstanceState.getString(getString(R.string.sp_userId)));
            application.setRealName(savedInstanceState.getString(getString(R.string.sp_userId)));
            application.setAge(savedInstanceState.getString(getString(R.string.sp_userId)));
            application.setTel(savedInstanceState.getString(getString(R.string.sp_userId)));
            application.setAddress(savedInstanceState.getString(getString(R.string.sp_userId)));
            application.setIsAdvancedUser(savedInstanceState.getString(getString(R.string.sp_userId)));
            application.setIdCardNum(savedInstanceState.getString(getString(R.string.sp_userId)));
            application.setIdFrontPic(savedInstanceState.getString(getString(R.string.sp_userId)));
            application.setIdOpsitePic(savedInstanceState.getString(getString(R.string.sp_userId)));
            application.setBankCardNum(savedInstanceState.getString(getString(R.string.sp_userId)));
            application.setBankName(savedInstanceState.getString(getString(R.string.sp_userId)));
            application.setChannelId(savedInstanceState.getString(getString(R.string.sp_userId)));
        }
    }
}
