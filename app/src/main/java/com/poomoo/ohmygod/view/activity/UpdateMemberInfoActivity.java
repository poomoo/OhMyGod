/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.UserBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.PubAdapter;
import com.poomoo.ohmygod.adapter.TimeAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2015/12/22 14:41.
 */
public class UpdateMemberInfoActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private EditText nameEdt;
    //    private TextView genderTxt;
    private EditText ageEdt;
    private TextView telTxt;
    private TextView addressTxt;
    private EditText idCardNumEdt;
    private RadioButton manRBtn;
    private RadioButton womanRBtn;
    private RadioButton yesRBtn;
    private RadioButton noRBtn;
    private LinearLayout educationLlayout;
    private TextView educationTxt;
    private LinearLayout careerLlayout;
    private TextView careerTxt;
    private LinearLayout incomeLlayout;
    private TextView incomeTxt;
    private LinearLayout layout2;
    private TextView txt2;
    private LinearLayout layout3;
    private TextView txt3;
    private LinearLayout layout4;
    private TextView txt4;
    private LinearLayout layout5;
    private TextView txt5;
    private EditText zoneEdt;
    private LinearLayout layout7;
    private TextView txt7;


    private String name;
    private String sex;
    private String age;
    private String address;
    private String idCardNum;
    private String education;
    private String carerr;
    private String income;
    private String item1;
    private String item2;
    private String item3;
    private String item4;
    private String item5;
    private String item6;
    private String item7;

    //    private GenderPopupWindow genderWindow;
    private PopupWindow pubPopupWindow;
    private View mMenuView;
    private ListView pubListView;
    private PubAdapter pubAdapter;
    private int flag = 0;//1-学历 2-职业 3-收入 4-如购置房屋选择付款方式 5-如购置房屋选择意愿 6-如购置房屋选择面积 7-如购置房屋选择价位 8-如购置房屋您最为看重的问题
    private ArrayList<String> pubList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_member_information);
        addActivityToArrayList(this);
        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();

        nameEdt = (EditText) findViewById(R.id.edt_memberInfo_name);
        ageEdt = (EditText) findViewById(R.id.edt_memberInfo_age);
        telTxt = (TextView) findViewById(R.id.txt_memberInfo_tel);
        addressTxt = (TextView) findViewById(R.id.txt_memberInfo_address);
        idCardNumEdt = (EditText) findViewById(R.id.edt_memberInfo_idCardNum);
        educationLlayout = (LinearLayout) findViewById(R.id.llayout_education);
        careerLlayout = (LinearLayout) findViewById(R.id.llayout_career);
        incomeLlayout = (LinearLayout) findViewById(R.id.llayout_income);
        layout2 = (LinearLayout) findViewById(R.id.llayout_2);
        layout3 = (LinearLayout) findViewById(R.id.llayout_3);
        layout4 = (LinearLayout) findViewById(R.id.llayout_4);
        layout5 = (LinearLayout) findViewById(R.id.llayout_5);
        zoneEdt = (EditText) findViewById(R.id.edt_memberInfo_zone);
        layout7 = (LinearLayout) findViewById(R.id.llayout_7);
        educationTxt = (TextView) findViewById(R.id.txt_education);
        careerTxt = (TextView) findViewById(R.id.txt_career);
        incomeTxt = (TextView) findViewById(R.id.txt_income);
        txt2 = (TextView) findViewById(R.id.txt_2);
        txt3 = (TextView) findViewById(R.id.txt_3);
        txt4 = (TextView) findViewById(R.id.txt_4);
        txt5 = (TextView) findViewById(R.id.txt_5);
        txt7 = (TextView) findViewById(R.id.txt_7);
        yesRBtn = (RadioButton) findViewById(R.id.radio_yes);
        noRBtn = (RadioButton) findViewById(R.id.radio_no);
        manRBtn = (RadioButton) findViewById(R.id.radio_gender_man);
        womanRBtn = (RadioButton) findViewById(R.id.radio_gender_woman);

        mMenuView = LayoutInflater.from(this).inflate(R.layout.popupwindow_pub, null);
        pubListView = (ListView) mMenuView.findViewById(R.id.list_pub);
        pubListView.setOnItemClickListener(this);

        educationLlayout.setOnClickListener(this);
        careerLlayout.setOnClickListener(this);
        incomeLlayout.setOnClickListener(this);
        layout2.setOnClickListener(this);
        layout3.setOnClickListener(this);
        layout4.setOnClickListener(this);
        layout5.setOnClickListener(this);
        layout7.setOnClickListener(this);


        pubAdapter = new PubAdapter(this);
        pubListView.setAdapter(pubAdapter);

        addressTxt.setText(application.getAddress());
        initPopWindow();
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
//        select_gender();
    }

//    private void select_gender() {
//        // 实例化GenderPopupWindow
//        genderWindow = new GenderPopupWindow(this, itemsOnClick);
//        // 显示窗口
//        genderWindow.showAtLocation(this.findViewById(R.id.llayout_updateMember),
//                Gravity.BOTTOM, 0, 0); // 设置layout在genderWindow中显示的位置
//    }

    // 为弹出窗口实现监听类
//    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
//
//        @Override
//        public void onClick(View view) {
//            genderWindow.dismiss();
//            switch (view.getId()) {
//                case R.id.llayout_man:
//                    genderTxt.setText(getString(R.string.label_gender_man));
//                    break;
//                case R.id.llayout_woman:
//                    genderTxt.setText(getString(R.string.label_gender_woman));
//                    break;
//            }
//        }
//    };

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
//            if (1 == 1)
//                return;
            showProgressDialog(getString(R.string.dialog_message));
            this.appAction.putAdvanceInfo(application.getUserId(), name, sex, age, application.getTel(), address, idCardNum, education, carerr, income, item1, item2, item3, item4, item5, item6, item7, new ActionCallbackListener() {
                @Override
                public void onSuccess(ResponseBO data) {
                    closeProgressDialog();
                    MyUtil.showToast(getApplicationContext(), "报名成功");
                    UserBO userBO = (UserBO) data.getObj();
                    application.setIsAdvancedUser("1");
                    application.setRealName(userBO.getRealName());
                    application.setSex(sex);
                    application.setAge(age);
                    application.setAddress(userBO.getAddress());
                    application.setIdCardNum(idCardNum);

                    SPUtils.put(getApplicationContext(), getString(R.string.sp_isAdvancedUser), "1");
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_realName), userBO.getRealName());
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_sex), sex);
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_age), age);
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_address), userBO.getAddress());
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

        if (!manRBtn.isChecked() && !womanRBtn.isChecked()) {
            MyUtil.showToast(getApplicationContext(), "请选择性别");
            return false;
        }
        if (manRBtn.isChecked())
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

        education = educationTxt.getText().toString();
        if (education.contains("请选择"))
            education = "";

        carerr = careerTxt.getText().toString();
        if (carerr.contains("请选择"))
            carerr = "";

        income = incomeTxt.getText().toString();
        if (income.contains("请选择"))
            income = "";

        item3 = txt3.getText().toString();
        if (item3.contains("请选择"))
            item3 = "";
        item6 = zoneEdt.getText().toString().trim();

        if (!yesRBtn.isChecked() && !noRBtn.isChecked()) {
            MyUtil.showToast(getApplicationContext(), "近期是否有购置房屋意向");
            return false;
        }
        if (yesRBtn.isChecked())
            item1 = "是";
        else
            item1 = "否";

        item2 = txt2.getText().toString();
        if (TextUtils.isEmpty(item2) || item2.contains("请选择")) {
            MyUtil.showToast(getApplicationContext(), "如购置房屋选择付款方式");
            return false;
        }

        item4 = txt4.getText().toString();
        if (TextUtils.isEmpty(item4) || item4.contains("请选择")) {
            MyUtil.showToast(getApplicationContext(), "如购置房屋选择面积");
            return false;
        }

        item5 = txt5.getText().toString();
        if (TextUtils.isEmpty(item5) || item5.contains("请选择")) {
            MyUtil.showToast(getApplicationContext(), "如购置房屋选择价位");
            return false;
        }

        item7 = txt7.getText().toString();
        if (TextUtils.isEmpty(item7) || item7.contains("请选择")) {
            MyUtil.showToast(getApplicationContext(), "如购置房屋您最为看重的问题");
            return false;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if (TextUtils.isEmpty(nameEdt.getText().toString().trim()))
            nameEdt.setText(application.getRealName());
        if (application.getSex().equals("1"))
            manRBtn.setChecked(true);
        else if (application.getSex().equals("2"))
            womanRBtn.setChecked(true);
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

    private void show() {
        pubPopupWindow.showAtLocation(findViewById(R.id.llayout_updateMember), Gravity.CENTER, 0, 0);
    }

    private void initPopWindow() {
        pubPopupWindow = new PopupWindow(mMenuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pubPopupWindow.setFocusable(true);
        pubPopupWindow.setFocusable(true);

        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height_top = mMenuView.findViewById(R.id.popup_pub_layout).getTop();
                int height_bottom = mMenuView.findViewById(R.id.popup_pub_layout).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height_top || y > height_bottom) {
                        pubPopupWindow.dismiss();
                    }
                }
                return true;
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llayout_education:
                flag = 1;
                pubList = new ArrayList<>();
                int len = MyConfig.education.length;
                for (int i = 0; i < len; i++)
                    pubList.add(MyConfig.education[i]);
                pubAdapter.setItems(pubList);
                break;
            case R.id.llayout_career:
                flag = 2;
                pubList = new ArrayList<>();
                len = MyConfig.career.length;
                for (int i = 0; i < len; i++)
                    pubList.add(MyConfig.career[i]);
                pubAdapter.setItems(pubList);
                break;
            case R.id.llayout_income:
                flag = 3;
                pubList = new ArrayList<>();
                len = MyConfig.income.length;
                for (int i = 0; i < len; i++)
                    pubList.add(MyConfig.income[i]);
                pubAdapter.setItems(pubList);
                break;
            case R.id.llayout_2:
                flag = 4;
                pubList = new ArrayList<>();
                len = MyConfig.item2.length;
                for (int i = 0; i < len; i++)
                    pubList.add(MyConfig.item2[i]);
                pubAdapter.setItems(pubList);
                break;
            case R.id.llayout_3:
                flag = 5;
                pubList = new ArrayList<>();
                len = MyConfig.item3.length;
                for (int i = 0; i < len; i++)
                    pubList.add(MyConfig.item3[i]);
                pubAdapter.setItems(pubList);
                break;
            case R.id.llayout_4:
                flag = 6;
                pubList = new ArrayList<>();
                len = MyConfig.item4.length;
                for (int i = 0; i < len; i++)
                    pubList.add(MyConfig.item4[i]);
                pubAdapter.setItems(pubList);
                break;
            case R.id.llayout_5:
                flag = 7;
                pubList = new ArrayList<>();
                len = MyConfig.item5.length;
                for (int i = 0; i < len; i++)
                    pubList.add(MyConfig.item5[i]);
                pubAdapter.setItems(pubList);
                break;
            case R.id.llayout_7:
                flag = 8;
                pubList = new ArrayList<>();
                len = MyConfig.item7.length;
                for (int i = 0; i < len; i++)
                    pubList.add(MyConfig.item7[i]);
                pubAdapter.setItems(pubList);
                break;
        }
        show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (flag) {
            case 1:
                educationTxt.setText(pubList.get(position));
                break;
            case 2:
                careerTxt.setText(pubList.get(position));
                break;
            case 3:
                incomeTxt.setText(pubList.get(position));
                break;
            case 4:
                txt2.setText(pubList.get(position));
                break;
            case 5:
                txt3.setText(pubList.get(position));
                break;
            case 6:
                txt4.setText(pubList.get(position));
                break;
            case 7:
                txt5.setText(pubList.get(position));
                break;
            case 8:
                txt7.setText(pubList.get(position));
                break;
        }
        pubPopupWindow.dismiss();
    }
}
