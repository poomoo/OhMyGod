/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;
import com.poomoo.ohmygod.view.popupwindow.GenderPopupWindow;

/**
 * 作者: 李苜菲
 * 日期: 2015/12/22 14:41.
 */
public class UpdateMemberInfoActivity extends BaseActivity {
    private EditText nameEdt;
    private TextView genderTxt;
    private EditText ageEdt;
    private TextView telTxt;
    private TextView addressTxt;
    private EditText idCardNumEdt;

    private String name;
    private String sex;
    private String age;
    private String address;
    private String idCardNum;
    private GenderPopupWindow genderWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_member_information);

//        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();

        nameEdt = (EditText) findViewById(R.id.edt_memberInfo_name);
        genderTxt = (TextView) findViewById(R.id.txt_memberInfo_gender);
        ageEdt = (EditText) findViewById(R.id.edt_memberInfo_age);
        telTxt = (TextView) findViewById(R.id.txt_memberInfo_tel);
        addressTxt = (TextView) findViewById(R.id.txt_memberInfo_address);
        idCardNumEdt = (EditText) findViewById(R.id.edt_memberInfo_idCardNum);

        addressTxt.setText(application.getAddress());
        initData();
    }

    @Override
    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_updateMember);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
    }

    /**
     * 性别
     *
     * @param view
     */
    public void toSelectGender(View view) {
        select_gender();
    }

    private void select_gender() {
        // 实例化GenderPopupWindow
        genderWindow = new GenderPopupWindow(this, itemsOnClick);
        // 显示窗口
        genderWindow.showAtLocation(this.findViewById(R.id.llayout_updateMember),
                Gravity.BOTTOM, 0, 0); // 设置layout在genderWindow中显示的位置
    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            genderWindow.dismiss();
            switch (view.getId()) {
                case R.id.llayout_man:
                    genderTxt.setText(getString(R.string.label_gender_man));
                    break;
                case R.id.llayout_woman:
                    genderTxt.setText(getString(R.string.label_gender_woman));
                    break;
            }
        }
    };

    /**
     * 地址
     *
     * @param view
     */
    public void toAddress(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.intent_parent), getString(R.string.intent_addressAlter));
        openActivityForResult(AddressActivity.class, bundle, 1);
    }

    /**
     * 提交
     *
     * @param view
     */
    public void toUpdateMemberInfo(View view) {
        if (checkInput()) {
            showProgressDialog(getString(R.string.dialog_message));
            this.appAction.putAdvanceInfo(application.getUserId(), name, sex, age, application.getTel(), address, idCardNum, new ActionCallbackListener() {
                @Override
                public void onSuccess(ResponseBO data) {
                    closeProgressDialog();
                    MyUtil.showToast(getApplicationContext(), "报名成功");
                    application.setIsAdvancedUser("1");
                    application.setRealName(name);
                    application.setSex(sex);
                    application.setAge(age);
                    application.setAddress(address);
                    application.setIdCardNum(idCardNum);

                    SPUtils.put(getApplicationContext(), getString(R.string.sp_isAdvancedUser), "1");
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_realName), name);
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_sex), sex);
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_age), age);
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_address), address);
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_idCardNum), idCardNum);

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

    private boolean checkInput() {
        name = nameEdt.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            MyUtil.showToast(getApplicationContext(), "请填写姓名");
            return false;
        }

        sex = genderTxt.getText().toString().trim();
        if (TextUtils.isEmpty(sex)) {
            MyUtil.showToast(getApplicationContext(), "请选择性别");
            return false;
        }
        if (sex.equals(getString(R.string.label_gender_man)))
            sex = "1";
        else
            sex = "2";

        age = ageEdt.getText().toString().trim();
        if (TextUtils.isEmpty(age)) {
            MyUtil.showToast(getApplicationContext(), "请填写年龄");
            return false;
        }

        address = addressTxt.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            MyUtil.showToast(getApplicationContext(), "请填写地址");
            return false;
        }
        idCardNum = idCardNumEdt.getText().toString().trim();
        if (TextUtils.isEmpty(idCardNum)) {
            MyUtil.showToast(getApplicationContext(), "请填写身份证号");
            return false;
        }

        if (idCardNum.length() != 18) {
            MyUtil.showToast(getApplicationContext(), "身份证号位数不对,请填写18位有效号码");
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initData();
    }

    private void initData() {
        nameEdt.setText(application.getRealName());
        genderTxt.setText(application.getSex().equals("1") ? "男" : "女");
        ageEdt.setText(application.getAge());
        telTxt.setText(application.getTel());
        idCardNumEdt.setText(application.getIdCardNum());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1) {
            address = data.getStringExtra(getString(R.string.intent_value));
            addressTxt.setText(address);
        }
    }
}
