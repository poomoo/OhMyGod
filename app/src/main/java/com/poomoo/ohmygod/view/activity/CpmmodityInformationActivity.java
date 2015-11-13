package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;

import com.poomoo.ohmygod.R;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/13 16:15.
 */
public class CpmmodityInformationActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_information);

        initView();
    }

    private void initView() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.title.setText(R.string.title_commodity_information);
        headerViewHolder.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
