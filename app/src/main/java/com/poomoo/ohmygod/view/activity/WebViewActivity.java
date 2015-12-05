/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.StatementBO;
import com.poomoo.ohmygod.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * webview
 * 作者: 李苜菲
 * 日期: 2015/11/19 16:59.
 */
public class WebViewActivity extends BaseActivity {
    private String PARENT;
    private String title;
    private WebView webView;
    private String type;//--1：注册声明，2：游戏规则声明，3返现声明，4提现帮助，5公共消息,6签到声明,7关于,8站内消息,9用户帮助

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_webview);

        initView();
        getData();
    }

    protected void initView() {
        initTitleBar();

        webView = (WebView) findViewById(R.id.web);
//        try {
//            webView.loadData(new
//                            String(InputStreamToByte(getAssets().open("test.html"))),
//                    "text/html; charset=UTF-8", null);
//        } catch (IOException e) {
//            // TODO 自动生成的 catch 块
//            e.printStackTrace();
//        }
    }

    protected void initTitleBar() {
        PARENT = getIntent().getStringExtra(getString(R.string.intent_parent));
        if (PARENT.equals(getString(R.string.intent_signed))) {
            type = "6";
            title = getString(R.string.title_signed_explain);
        }

        if (PARENT.equals(getString(R.string.intent_rebate))) {
            type = "3";
            title = getString(R.string.title_rebate_explain);
        }

        if (PARENT.equals(getString(R.string.intent_withDrawDeposit))) {
            type = "4";
            title = getString(R.string.title_withdraw_deposit_help);
        }

        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(title);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

//    private byte[] InputStreamToByte(InputStream is) throws IOException {
//        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
//        int ch;
//        while ((ch = is.read()) != -1) {
//            bytestream.write(ch);
//        }
//        byte imgdata[] = bytestream.toByteArray();
//        bytestream.close();
//        return imgdata;
//    }

    private void getData() {
        this.appAction.getStatement(type, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                StatementBO statementBO = (StatementBO) data.getObj();
                webView.getSettings().setDefaultTextEncodingName("UTF-8");
                webView.loadData(statementBO.getContent(), "text/html; charset=UTF-8", null);// 这种写法可以正确解码
            }

            @Override
            public void onFailure(int errorCode, String message) {

            }
        });
    }
}
