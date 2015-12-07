/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.service.Get_UserInfo_Service;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.popupwindow.GenderPopupWindow;

/**
 * 个人资料
 * 作者: 李苜菲
 * 日期: 2015/11/24 15:37.
 */
public class UserInfoActivity extends BaseActivity {
    private TextView nickNameTxt;
    private TextView genderTxt;
    private TextView ageTxt;
    private TextView phoneNumTxt;
    private TextView idCardNumTxt;
    private TextView bankCardNumTxt;
    private GenderPopupWindow genderWindow;

    private String key;
    private String value;
    private boolean isMan = true;//true男 false女

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();
    }

    protected void initView() {
        initTitleBar();

        nickNameTxt = (TextView) findViewById(R.id.txt_userInfo_nickName);
        genderTxt = (TextView) findViewById(R.id.txt_userInfo_gender);
        ageTxt = (TextView) findViewById(R.id.txt_userInfo_age);
        phoneNumTxt = (TextView) findViewById(R.id.txt_userInfo_phoneNum);
        idCardNumTxt = (TextView) findViewById(R.id.txt_userInfo_idCardNum);
        bankCardNumTxt = (TextView) findViewById(R.id.txt_userInfo_bankCardNum);

        initData();
    }


    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_userInfo);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        nickNameTxt.setText(this.application.getNickName());
        genderTxt.setText(this.application.getSex().equals("1") ? "男" : "女");
        ageTxt.setText(this.application.getAge());
        phoneNumTxt.setText(this.application.getTel());
        idCardNumTxt.setText(this.application.getIdCardNum());
        bankCardNumTxt.setText(this.application.getBankCardNum());
    }

    /**
     * 头像
     *
     * @param view
     */
    public void toAvatar(View view) {
        MyUtil.showToast(getApplicationContext(), "头像");
    }

    /**
     * 昵称
     *
     * @param view
     */
    public void toNickName(View view) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_nickName));
        openActivityForResult(NickNameActivity.class, pBundle, 1);
    }

    /**
     * 性别
     *
     * @param view
     */
    public void toGender(View view) {
        select_gender();
    }

    /**
     * 年龄
     *
     * @param view
     */
    public void toAge(View view) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_age));
        openActivityForResult(NickNameActivity.class, pBundle, 2);
    }

    /**
     * 手机号码
     *
     * @param view
     */
    public void toCelPhone(View view) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_phone));
        openActivity(VerifyPhoneNum2Activity.class, pBundle);
    }

    /**
     * 身份证号
     *
     * @param view
     */
    public void toIdCardNum(View view) {
        openActivity(EditPersonalInformationActivity.class);
    }

    /**
     * 银行卡
     *
     * @param view
     */
    public void toBankCard(View view) {
        openActivity(EditPersonalInformationActivity.class);
    }

    /**
     * 修改密码
     *
     * @param view
     */
    public void toChangePassWord(View view) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_passWord));
        openActivity(VerifyPhoneNum2Activity.class, pBundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1)
            nickNameTxt.setText(data.getStringExtra("value"));

        if (requestCode == 2 && resultCode == 1)
            ageTxt.setText(data.getStringExtra("value"));
    }

    private void select_gender() {
        // 实例化GenderPopupWindow
        genderWindow = new GenderPopupWindow(this, itemsOnClick);
        // 显示窗口
        genderWindow.showAtLocation(this.findViewById(R.id.llayout_userInfo),
                Gravity.BOTTOM, 0, 0); // 设置layout在genderWindow中显示的位置
    }

    // 为弹出窗口实现监听类
    private OnClickListener itemsOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            genderWindow.dismiss();
            switch (view.getId()) {
                case R.id.llayout_man:
                    isMan = true;
                    value = getString(R.string.label_gender_man);
                    break;
                case R.id.llayout_woman:
                    isMan = false;
                    value = getString(R.string.label_gender_woman);
                    break;
            }
            toSubmit();
        }
    };

    /**
     * 提交
     */
    private void toSubmit() {
        key = "sex";
        if (value.equals(getString(R.string.label_gender_man)))
            value = "1";
        else
            value = "2";

        showProgressDialog("提交中...");
        this.appAction.changePersonalInfo(this.application.getUserId(), key, value, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), "修改成功");
                if (isMan)
                    genderTxt.setText(getString(R.string.label_gender_man));
                else
                    genderTxt.setText(getString(R.string.label_gender_woman));
                application.setSex(value);
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }
}
