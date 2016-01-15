/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.FileBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.UserBO;
import com.poomoo.model.WinningRecordsBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.PubAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.database.CityInfo;
import com.poomoo.ohmygod.service.Get_UserInfo_Service;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;
import com.poomoo.ohmygod.utils.picUtils.Bimp;
import com.poomoo.ohmygod.utils.picUtils.FileUtils;
import com.poomoo.ohmygod.view.popupwindow.SelectPicsPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

/**
 * 完善普通用户资料
 * 作者: 李苜菲
 * 日期: 2015/11/17 14:18.
 */
public class CompleteUserInformationActivity extends BaseActivity implements OnClickListener, AdapterView.OnItemClickListener {
    private EditText realNameEdt;
    private EditText addressEdt;
    private TextView cityTxt;
    private TextView telTxt;
    private TextView zoneTxt;
    private LinearLayout zoneLlayout;
    private String realName;
    private String address;

    private ArrayList<CityInfo> cityInfoArrayList;
    private List<String> areaInfoArrayList = new ArrayList<>();

    private String city_id;
    private String area;

    private WinningRecordsBO winningRecordsBO;
    private PopupWindow pubPopupWindow;
    private View mMenuView;
    private ListView pubListView;
    private PubAdapter pubAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_user_information);
        addActivityToArrayList(this);
        initView();
    }

    protected void initView() {
        initTitleBar();

        realNameEdt = (EditText) findViewById(R.id.edt_realName);
        addressEdt = (EditText) findViewById(R.id.edt_address1);
        cityTxt = (TextView) findViewById(R.id.txt_locateCity);
        telTxt = (TextView) findViewById(R.id.txt_celPhoneNum);
        zoneTxt = (TextView) findViewById(R.id.txt_zone);
        zoneLlayout = (LinearLayout) findViewById(R.id.llayout_zone);

        mMenuView = LayoutInflater.from(this).inflate(R.layout.popupwindow_pub, null);
        pubListView = (ListView) mMenuView.findViewById(R.id.list_pub);
        pubListView.setOnItemClickListener(this);

        pubAdapter = new PubAdapter(this);
        pubListView.setAdapter(pubAdapter);

        zoneLlayout.setOnClickListener(this);

        realNameEdt.setText(application.getRealName());
        telTxt.setText(application.getTel());

        cityTxt.setText(application.getLocateCity());
        cityInfoArrayList = MyUtil.getCityList();
        city_id = MyUtil.getCityId(cityInfoArrayList, application.getLocateCity());
        areaInfoArrayList = MyUtil.getAreaList(city_id);

//        area = areaInfoArrayList.get(0);
        pubAdapter.setItems(areaInfoArrayList);
        initPopWindow();
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_edit_personal_information);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
        headerViewHolder.rightTxt.setText(R.string.btn_save);
        headerViewHolder.rightTxt.setTextColor(getResources().getColor(R.color.themeRed));
        headerViewHolder.rightTxt.setVisibility(View.VISIBLE);
    }


    /**
     * 保存
     *
     * @param view
     */
    public void toDo(View view) {
        if (checkInput())
            submit();
    }

    private boolean checkInput() {
        realName = realNameEdt.getText().toString().trim();
        if (TextUtils.isEmpty(realName)) {
            MyUtil.showToast(getApplicationContext(), "请填写真实姓名");
            return false;
        }


        if (TextUtils.isEmpty(area)) {
            MyUtil.showToast(getApplicationContext(), "请选择区域");
            return false;
        }
        address = addressEdt.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            MyUtil.showToast(getApplicationContext(), "请填写详细地址");
            return false;
        }
        address = application.getLocateCity() + area + address;

        return true;
    }

    public void submit() {
        showProgressDialog(getString(R.string.dialog_message));
        this.appAction.putPersonalInfo(this.application.getUserId(), realName, "", address, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), "上传成功");
                UserBO userBO = (UserBO) data.getObj();
                application.setRealName(userBO.getRealName());
                application.setAddress(userBO.getAddress());
                SPUtils.put(getApplicationContext(), getString(R.string.sp_realName), userBO.getRealName());
                SPUtils.put(getApplicationContext(), getString(R.string.sp_address), userBO.getAddress());
                startService(new Intent(CompleteUserInformationActivity.this, Get_UserInfo_Service.class));
                winningRecordsBO = (WinningRecordsBO) getIntent().getSerializableExtra(getString(R.string.intent_value));
                if (winningRecordsBO != null) {
                    Bundle pBundle = new Bundle();
                    pBundle.putSerializable(getString(R.string.intent_value), winningRecordsBO);
                    openActivity(WinningRecord2Activity.class, pBundle);
                }
                finish();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Dialog dialog = new AlertDialog.Builder(CompleteUserInformationActivity.this).setMessage("资料没有完善,确定退出?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }
            ).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).create();
            dialog.show();
        }
        return true;
    }

    private void show() {
        pubPopupWindow.showAtLocation(findViewById(R.id.llayout_editPersonalInformation), Gravity.CENTER, 0, 0);
    }

    private void initPopWindow() {
        pubPopupWindow = new PopupWindow(mMenuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        pubPopupWindow.setFocusable(true);
        pubPopupWindow.setFocusable(true);

        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height_left = mMenuView.findViewById(R.id.popup_pub_layout).getLeft();
                int height_top = mMenuView.findViewById(R.id.popup_pub_layout).getTop();
                int height_right = mMenuView.findViewById(R.id.popup_pub_layout).getLeft();
                int height_bottom = mMenuView.findViewById(R.id.popup_pub_layout).getBottom();

                int x = (int) event.getX();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (x < height_left || x > height_right) {
                        pubPopupWindow.dismiss();
                    }
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
        show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        area = areaInfoArrayList.get(position);
        zoneTxt.setText(area);
        pubPopupWindow.dismiss();
    }
}
