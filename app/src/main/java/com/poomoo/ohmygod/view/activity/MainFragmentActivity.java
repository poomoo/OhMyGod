package com.poomoo.ohmygod.view.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.view.fragment.GrabFrament;

/**
 * Created by Android_PM on 2015/11/10.
 */
public class MainFragmentActivity extends
        BaseActivity {
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
}
