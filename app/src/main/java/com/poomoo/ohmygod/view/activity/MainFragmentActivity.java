package com.poomoo.ohmygod.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.MessageBO;
import com.poomoo.model.MessageInfoBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.fragment.GrabFragment;
import com.poomoo.ohmygod.view.fragment.MyFragment;
import com.poomoo.ohmygod.view.fragment.RebateFragment;
import com.poomoo.ohmygod.view.fragment.ShowFragment;
import com.poomoo.ohmygod.view.popupwindow.InformPopupWindow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android_PM on 2015/11/10.
 */
public class MainFragmentActivity extends
        BaseActivity {
    private String TAG = this.getClass().getSimpleName();
    private RadioGroup group;
    private InformPopupWindow informPopupWindow;
    private Fragment curFragment;
    private GrabFragment grabFrament;
    private RebateFragment rebateFragment;
    private ShowFragment showFragment;
    private MyFragment myFragment;
    private long exitTime = 0;
    public static MainFragmentActivity instance;
    public static List<MessageBO> messageBOList = new ArrayList<>();
    private int statementId = 0;
    private String title;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        group = (RadioGroup) findViewById(R.id.group_main);
        setDefaultFragment();
        instance = this;
    }

    private void setDefaultFragment() {
        // TODO 自动生成的方法存根
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        grabFrament = new GrabFragment();
        curFragment = grabFrament;
        fragmentTransaction.add(R.id.activity_main_frameLayout, grabFrament);
        fragmentTransaction.commit();
    }


    private void show() {
        // 实例化SelectPicPopupWindow
        informPopupWindow = new InformPopupWindow(this, title, content, itemsOnClick);
        // 显示窗口
        informPopupWindow.showAtLocation(
                this.findViewById(R.id.activity_main_frameLayout), Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            informPopupWindow.dismiss();
            switch (view.getId()) {
                case R.id.img_more:
                    Bundle bundle = new Bundle();
                    bundle.putString(getString(R.string.intent_parent), getString(R.string.intent_pubMessage));
                    bundle.putSerializable(getString(R.string.intent_value), (Serializable) messageBOList);
                    openActivity(InStationMessagesActivity.class,bundle);
                    break;
            }
        }
    };

    /**
     * 切换到抢
     *
     * @param view
     */
    public void switchToGrab(View view) {
        if (grabFrament == null)
            grabFrament = new GrabFragment();
        switchFragment(grabFrament);
        curFragment = grabFrament;
    }

    /**
     * 切换到返
     *
     * @param view
     */
    public void switchToRebate(View view) {
        if (rebateFragment == null)
            rebateFragment = new RebateFragment();
        switchFragment(rebateFragment);
        curFragment = rebateFragment;
    }

    /**
     * 切换到晒
     *
     * @param view
     */
    public void switchToShow(View view) {
        if (showFragment == null)
            showFragment = new ShowFragment();
        switchFragment(showFragment);
        curFragment = showFragment;
    }

    /**
     * 切换到我
     *
     * @param view
     */
    public void switchToMy(View view) {
        if (myFragment == null)
            myFragment = new MyFragment();
        switchFragment(myFragment);
        curFragment = myFragment;
    }

    /**
     * 切换fragment
     *
     * @param to
     */
    public void switchFragment(Fragment to) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (!to.isAdded()) { // 先判断是否被add过
            fragmentTransaction.hide(curFragment).add(R.id.activity_main_frameLayout, to); // 隐藏当前的fragment，add下一个到Activity中
        } else {
            fragmentTransaction.hide(curFragment).show(to); // 隐藏当前的fragment，显示下一个
        }
        fragmentTransaction.commit();
    }

    public void showInform(View view) {
        getInfo();
    }

    private void getInfo() {
        statementId = messageBOList.get(0).getStatementId();
        title = messageBOList.get(0).getTitle();
        showProgressDialog("请稍后...");
        this.appAction.getMessageInfo(statementId + "", new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                MessageInfoBO messageInfoBO = (MessageInfoBO) data.getObj();
                content = messageInfoBO.getContent();
                show();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApp();
        }
        return true;
    }

    private void exitApp() {
        // 判断2次点击事件时间
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            MyUtil.showToast(getApplicationContext(), "再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    public void invisible() {
        group.setVisibility(View.GONE);
    }

    public void visible() {
        group.setVisibility(View.VISIBLE);
    }
}
