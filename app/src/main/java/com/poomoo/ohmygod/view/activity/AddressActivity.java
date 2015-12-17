/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.database.CityInfo;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 修改地址
 * 作者: 李苜菲
 * 日期: 2015/12/17 10:57.
 */
public class AddressActivity extends BaseActivity {
    private TextView cityTxt;
    private Spinner areaSpinner;
    private EditText addressEdt;

    private ArrayList<CityInfo> cityInfoArrayList;
    private List<String> areaInfoArrayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private String city_id;
    private String area;
    private String address;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();

        cityTxt = (TextView) findViewById(R.id.txt_locateCity);
        areaSpinner = (Spinner) findViewById(R.id.spinner_area);
        addressEdt = (EditText) findViewById(R.id.edt_address1);

        cityInfoArrayList = MyUtil.getCityList();
        LogUtils.i(TAG, "cityInfoArrayList:" + cityInfoArrayList);
        city_id = MyUtil.getCityId(cityInfoArrayList, application.getLocateCity());
        LogUtils.i(TAG, "city_id:" + city_id);
        areaInfoArrayList = MyUtil.getAreaList(city_id);
        LogUtils.i(TAG, "areaInfoArrayList:" + areaInfoArrayList);

        adapter = new ArrayAdapter<>(this, R.layout.item_spinner_textview, areaInfoArrayList);
        areaSpinner.setAdapter(adapter);

        //添加事件Spinner事件监听
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                area = areaInfoArrayList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_address);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 提交
     *
     * @param view
     */
    public void toSubmitAddress(View view) {
        address = addressEdt.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            MyUtil.showToast(getApplicationContext(), "请输入详细地址");
            return;
        }
        address = application.getLocateCity() + area + address;
        showProgressDialog("提交中...");
        key = "address";
        this.appAction.changePersonalInfo(this.application.getUserId(), key, address, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                application.setAddress(address);
                SPUtils.put(getApplicationContext(), getString(R.string.sp_address), address);
                MyUtil.showToast(getApplicationContext(), "修改成功");
                finish();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }


}
