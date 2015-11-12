package com.poomoo.ohmygod.view.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.view.fragment.GrabFrament;
import com.poomoo.ohmygod.view.popupwindow.InformPopupWindow;

/**
 * Created by Android_PM on 2015/11/10.
 */
public class MainFragmentActivity extends
        BaseActivity {
    private InformPopupWindow informPopupWindow;
    private GrabFrament grabFrament;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDefaultFragment();
    }

    private void setDefaultFragment() {
        // TODO 自动生成的方法存根
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        grabFrament = new GrabFrament();
        fragmentTransaction.add(R.id.activity_main_frameLayout, grabFrament);
        fragmentTransaction.commit();
    }


    private void show() {
        // 实例化SelectPicPopupWindow
        informPopupWindow = new InformPopupWindow(this);
        // 显示窗口
        informPopupWindow.showAtLocation(
                this.findViewById(R.id.activity_main_frameLayout), Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    public void showInform(View view) {
        show();
    }
}
