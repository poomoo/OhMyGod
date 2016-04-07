/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.google.gson.JsonObject;
import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.MessageInfoBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.StatementBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * webView
 * 作者: 李苜菲
 * 日期: 2015/11/19 16:59.
 */
public class WebViewActivity extends BaseActivity {
    private String PARENT;
    private String title;
    private WebView webView;
    private String type;//--1：注册声明，2：游戏规则声明，3返现声明，4提现帮助，5公共消息,6签到声明,7关于,8站内消息,9用户帮助 10联系我们
    private int statementId;//--声明编号
    private int advId;//广告Id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub_webview);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.themeRed), 0);
        addActivityToArrayList(this);
        initView();
    }

    protected void initView() {
        initTitleBar();

        webView = (WebView) findViewById(R.id.web);
    }

    protected void initTitleBar() {
        PARENT = getIntent().getStringExtra(getString(R.string.intent_parent));

        if (PARENT.equals(getString(R.string.intent_protocol))) {
            type = "1";
            title = getString(R.string.title_protocol);
            getData();
        }

        if (PARENT.equals(getString(R.string.intent_rebate))) {
            type = "3";
            title = getString(R.string.title_rebate_explain);
            getData();
        }

        if (PARENT.equals(getString(R.string.intent_withDrawDeposit))) {
            type = "4";
            title = getString(R.string.title_withdraw_deposit_help);
            getData();
        }

        if (PARENT.equals(getString(R.string.intent_signed))) {
            type = "6";
            title = getString(R.string.title_signed_explain);
            getData();
        }

        if (PARENT.equals(getString(R.string.intent_about))) {
            type = "7";
            title = getString(R.string.title_about);
            getData();
        }

        if (PARENT.equals(getString(R.string.intent_userHelp))) {
            type = "9";
            title = getString(R.string.title_userHelp);
            getData();
        }

        if (PARENT.equals(getString(R.string.intent_contactUs))) {
            type = "10";
            title = getString(R.string.title_contactUs);
            getData();
        }

        if (PARENT.equals(getString(R.string.intent_message))) {
            statementId = getIntent().getIntExtra(getString(R.string.intent_value), 0);
            title = getIntent().getStringExtra(getString(R.string.intent_title));
            getInfo();
        }

        if (PARENT.equals(getString(R.string.intent_ad))) {
            advId = getIntent().getIntExtra(getString(R.string.intent_value), 0);
            title = getIntent().getStringExtra(getString(R.string.intent_title));
            getAd();
        }

        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(title);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
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
        showProgressDialog(getString(R.string.dialog_message));
        this.appAction.getStatement(type, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                StatementBO statementBO = (StatementBO) data.getObj();
                webView.getSettings().setDefaultTextEncodingName("UTF-8");
                webView.loadData(statementBO.getContent(), "text/html; charset=UTF-8", null);// 这种写法可以正确解码
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
            }
        });
    }

    private void getInfo() {
        showProgressDialog(getString(R.string.dialog_message));
        this.appAction.getMessageInfo(statementId + "", new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                MessageInfoBO messageInfoBO = (MessageInfoBO) data.getObj();
                webView.getSettings().setDefaultTextEncodingName("UTF-8");
                webView.loadData(messageInfoBO.getContent(), "text/html; charset=UTF-8", null);// 这种写法可以正确解码
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    private void getAd() {
        showProgressDialog(getString(R.string.dialog_message));
        this.appAction.getAd(advId, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                try {
                    JSONObject jsonObject = new JSONObject(data.getJsonData());
                    webView.getSettings().setDefaultTextEncodingName("UTF-8");
                    webView.loadData(jsonObject.getString("content"), "text/html; charset=UTF-8", null);// 这种写法可以正确解码
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }
}
