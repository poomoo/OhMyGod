/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;

/**
 * 修改身份证信息
 * 作者: 李苜菲
 * 日期: 2015/11/25 09:42.
 */
public class ChangeIdCardInfoActivity extends BaseActivity {
    private EditText idCardNumEdt;

    private String idCardNum;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_idcard_info);

        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();

        idCardNumEdt = (EditText) findViewById(R.id.edt_idCardNum);
        idCardNumEdt.setText(application.getIdCardNum());
    }

    @Override
    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_changeIdCardInfo);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
    }

    /**
     * 提交
     *
     * @param view
     */
    public void toSubmitIdCardNum(View view) {
        idCardNum = idCardNumEdt.getText().toString().trim();
        if (TextUtils.isEmpty(idCardNum)) {
            MyUtil.showToast(getApplicationContext(), "请输入身份证号");
            return;
        }
        showProgressDialog("提交中...");
        key = "idCardNum";
        this.appAction.changePersonalInfo(this.application.getUserId(), key, idCardNum, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                application.setIdCardNum(idCardNum);
                SPUtils.put(getApplicationContext(), getString(R.string.sp_idCardNum), idCardNum);
                MyUtil.showToast(getApplicationContext(), "修改成功");
                finish();
                getActivityOutToRight();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }
}
