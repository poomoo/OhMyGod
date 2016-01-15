/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.core.ErrorEvent;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.UserBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.other.CountDownListener;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;
import com.poomoo.ohmygod.utils.TimeCountDownUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 注册
 * 作者: 李苜菲
 * 日期: 2015/11/25 14:52.
 */
public class RegisterActivity extends BaseActivity {
    private EditText phoneNumEdt;
    private EditText codeEdt;
    private EditText passWordEdt;
    private CheckBox agreeChk;
    private Button codeBtn;
    private TextView ageTxt;
    private TextView genderTxt;

    private String phoneNum;
    private String passWord;
    private String code;
    private String age;
    private String sex;
    private String imei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    @Override
    protected void initView() {
        phoneNumEdt = (EditText) findViewById(R.id.edt_register_phoneNum);
        codeEdt = (EditText) findViewById(R.id.edt_register_code);
        passWordEdt = (EditText) findViewById(R.id.edt_register_passWord);
        codeBtn = (Button) findViewById(R.id.btn_register_code);
        ageTxt = (TextView) findViewById(R.id.txt_age);
        genderTxt = (TextView) findViewById(R.id.txt_gender);
        agreeChk = (CheckBox) findViewById(R.id.chk_agree);
    }

    /**
     * @param view
     */
    public void toCode(View view) {
        if (checkInput()) {
            TimeCountDownUtil timeCountDownUtil = new TimeCountDownUtil(MyConfig.SMSCOUNTDOWNTIME, MyConfig.COUNTDOWNTIBTERVAL, codeBtn, new CountDownListener() {
                @Override
                public void onFinish(int result) {
                    codeBtn.setText("重新获取");
                    codeBtn.setClickable(true);// 重新获得点击
                    codeBtn.setBackgroundResource(R.drawable.selector_get_code_button);// 还原背景色
                    codeBtn.setTextColor(Color.parseColor("#FFFFFF"));
                }
            });
            timeCountDownUtil.start();

            this.appAction.getCode(phoneNum, new ActionCallbackListener() {
                @Override
                public void onSuccess(ResponseBO data) {
                    MyUtil.showToast(getApplicationContext(), "验证码发送成功");
                }

                @Override
                public void onFailure(int errorCode, String message) {
                    MyUtil.showToast(getApplicationContext(), message);
                }
            });
        }
    }

    public boolean checkInput() {
        phoneNum = phoneNumEdt.getText().toString().trim();
        // 参数检查
        if (TextUtils.isEmpty(phoneNum)) {
            MyUtil.showToast(getApplicationContext(), "手机号为空");
            return false;
        }
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(phoneNum);
        if (!matcher.matches()) {
            MyUtil.showToast(getApplicationContext(), "手机号不正确");
            return false;
        }
        return true;
    }

    /**
     * 注册
     *
     * @param view
     */
    public void toRegister(View view) {
        if (!agreeChk.isChecked()) {
            MyUtil.showToast(getApplicationContext(), "请同意注册协议");
            return;
        }
        phoneNum = phoneNumEdt.getText().toString().trim();
        code = codeEdt.getText().toString().trim();
        age = ageTxt.getText().toString().trim();
        sex = genderTxt.getText().toString().trim();
        passWord = passWordEdt.getText().toString().trim();
        if (sex.equals("男"))
            sex = "1";
        else
            sex = "2";
        imei = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId();

        showProgressDialog(getString(R.string.dialog_message));
        this.appAction.register(phoneNum, passWord, code, age, sex, imei, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), "注册成功");
                login();
//                finish();
//                getActivityOutToRight();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    /**
     * 选择年龄
     *
     * @param view
     */
    public void toAge(View view) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_age));
        openActivityForResult(SelectActivity.class, pBundle, MyConfig.AGE);
    }

    /**
     * 选择性别
     *
     * @param view
     */
    public void toGender(View view) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_gender));
        openActivityForResult(SelectActivity.class, pBundle, MyConfig.GENDER);
    }

    /**
     * 协议
     *
     * @param view
     */
    public void toProtocol(View view) {
        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.intent_parent), getString(R.string.intent_protocol));
        openActivity(WebViewActivity.class, bundle);
    }

    /**
     * 登陆
     */
    private void login() {
        showProgressDialog("登录中...");
        this.appAction.logIn(phoneNum, passWord, application.getChannelId(), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                UserBO userBO = (UserBO) data.getObj();
                SPUtils.put(getApplicationContext(), getString(R.string.sp_isLogin), true);
                Log.i(TAG, "data:" + userBO);
                setAppInfo(userBO);
                openActivity(MainFragmentActivity.class);
                if (LogInActivity.instance != null)
                    LogInActivity.instance.finish();
                finish();
            }

            @Override
            public void onFailure(int errorEvent, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    private void setAppInfo(UserBO userBO) {
        SPUtils.put(getApplicationContext(), getString(R.string.sp_rememberPassWord), false);
        SPUtils.put(getApplicationContext(), getString(R.string.sp_passWord), "");

        this.application.setUserId(userBO.getUserId());
        this.application.setTel(userBO.getTel());
        this.application.setNickName(userBO.getNickName());
        this.application.setRealName(userBO.getRealName());
        this.application.setHeadPic(userBO.getHeadPic());
        this.application.setCurrentFee(userBO.getCurrentFee());
        this.application.setRealNameAuth(userBO.getRealNameAuth());
        this.application.setIdCardNum(userBO.getIdCardNum());
        this.application.setSex(userBO.getSex());
        this.application.setAge(userBO.getAge());
        this.application.setIdFrontPic(userBO.getIdFrontPic());
        this.application.setIdOpsitePic(userBO.getIdOpsitePic());
        this.application.setBankCardNum(userBO.getBankCardNum());
        this.application.setBankName(userBO.getBankName());
        this.application.setAddress(userBO.getAddress());
        this.application.setIsAdvancedUser(userBO.getIsAdvancedUser());

//        LogUtils.i(TAG, "userBO.getTel():" + userBO.getTel() + " phoneNum:" + SPUtils.get(getApplicationContext(), getString(R.string.sp_phoneNum), ""));
        if (!userBO.getTel().equals(SPUtils.get(getApplicationContext(), getString(R.string.sp_phoneNum), ""))) {
            SPUtils.put(getApplicationContext(), getString(R.string.sp_headPicBitmap), "");
            LogUtils.i(TAG, "电话不一样，清除头像缓存:" + SPUtils.get(getApplicationContext(), getString(R.string.sp_headPicBitmap), "123"));
        }
        SPUtils.put(getApplicationContext(), getString(R.string.sp_userId), userBO.getUserId());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_phoneNum), userBO.getTel());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_nickName), userBO.getNickName());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_realName), userBO.getRealName());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_headPic), userBO.getHeadPic());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_currentFee), userBO.getCurrentFee());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_realNameAuth), userBO.getRealNameAuth());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_idCardNum), userBO.getIdCardNum());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_sex), userBO.getSex());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_age), userBO.getAge());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_idFrontPic), userBO.getIdFrontPic());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_idOpsitePic), userBO.getIdOpsitePic());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_bankCardNum), userBO.getBankCardNum());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_bankName), userBO.getBankName());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_address), userBO.getAddress());
        SPUtils.put(getApplicationContext(), getString(R.string.sp_isAdvancedUser), userBO.getIsAdvancedUser());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyConfig.AGE && resultCode == MyConfig.AGE) {
            ageTxt.setText(data.getStringExtra(getString(R.string.intent_age)));
        }
        if (requestCode == MyConfig.GENDER && resultCode == MyConfig.GENDER) {
            genderTxt.setText(data.getStringExtra(getString(R.string.intent_gender)));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
