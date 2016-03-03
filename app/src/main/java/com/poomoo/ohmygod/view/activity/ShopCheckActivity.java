/**
 * Copyright (c) 2016. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.CodeBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.CodeStatusListAdapter;
import com.poomoo.ohmygod.config.MyConfig;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.custom.CustomerDatePickerDialog;
import com.poomoo.ohmygod.view.custom.MyPullUpListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * 商家验证
 * 作者: 李苜菲
 * 日期: 2016/3/1 15:16.
 */
public class ShopCheckActivity extends BaseActivity implements MyPullUpListView.OnLoadListener {
    private TextView shopNameTxt;
    private EditText codeEdt;
    private TextView checkedNumTxt;
    private TextView totalNumTxt;
    private TextView dateTxt;
    private TextView activeNameTxt;
    //    private RefreshLayout refreshLayout;
    private MyPullUpListView listView;
    private RelativeLayout progressRlayout;

    private CodeStatusListAdapter adapter;
    private List<CodeBO> codeBOList;
    private CodeBO codeBO;
    private int activeId = 0; //活动编号
    private String playDt;   //"2016-02-22",--中奖时间（注意时间格式）
    private String isGot = "";    //默认是全部，则不传，0：未验证，1：已经验证
    private boolean isLoad = false;//true 加载 false刷新
    private int currPage = 1;
    private String winNumber;//中奖号码
    private String shopName;
    private int totalWinNum;
    private int gotWinNum;
    private String activeName;
    private ArrayList<HashMap<String, String>> list_activeType;
    private String currStatus = "";//当前的isGot
    private boolean isFirst = true;
    private Calendar cal = Calendar.getInstance();
    private int nYear;
    private int nMonth;
    private int nDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_check);
        initView();
    }

    @Override
    protected void initView() {
        initTitleBar();

//        refreshLayout = (RefreshLayout) findViewById(R.id.refresh_merchantInfo);
//        refreshLayout.setOnLoadListener(this);
//        refreshLayout.setRefreshing(false);
        listView = (MyPullUpListView) findViewById(R.id.list_code_status);
        listView.setOnLoadListener(this);
        adapter = new CodeStatusListAdapter(this);
        listView.setAdapter(adapter);

        shopNameTxt = (TextView) findViewById(R.id.txt_shopName);
        codeEdt = (EditText) findViewById(R.id.edt_code);
        checkedNumTxt = (TextView) findViewById(R.id.txt_checkedNum);
        totalNumTxt = (TextView) findViewById(R.id.txt_totalNum);
        dateTxt = (TextView) findViewById(R.id.txt_date);
        activeNameTxt = (TextView) findViewById(R.id.txt_activeName);
        progressRlayout = (RelativeLayout) findViewById(R.id.rlayout_progress);

        showProgressDialog(getString(R.string.dialog_message));
        isLoad = false;
        getData();
        dateTxt.setText(cal.get(Calendar.YEAR) + "年" + (cal.get(Calendar.MONTH) + 1) + "月" + cal.get(Calendar.DAY_OF_MONTH) + "日");
//        initTestData();
    }

    private void initTestData() {
        codeBOList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            codeBO = new CodeBO();
            codeBO.setActiveName("测试活动" + (i + 1));
            codeBO.setWinNumber("123 456 789");
            if (i % 2 == 0)
                codeBO.setIsGot(1);
            else
                codeBO.setIsGot(0);
            codeBO.setNickName("哇哈哈");
            codeBOList.add(codeBO);
        }
        adapter.setItems(codeBOList);
    }

    @Override
    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_shopCheck);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
    }

    public void toGetAll(View view) {
        isGot = "";
        if (!TextUtils.isEmpty(currStatus)) {
            codeBOList = new ArrayList<>();
            adapter.setItems(codeBOList);
            currPage = 1;
            isLoad = false;
            listView.smoothScrollToPosition(0);
            getData();
        }
        currStatus = isGot;
    }

    public void toGetUnChecked(View view) {
        isGot = "0";
        if (!currStatus.equals("0")) {
            codeBOList = new ArrayList<>();
            adapter.setItems(codeBOList);
            currPage = 1;
            isLoad = false;
            listView.smoothScrollToPosition(0);
            getData();
        }
        currStatus = isGot;
    }

    public void toGetChecked(View view) {
        isGot = "1";
        if (!currStatus.equals("1")) {
            codeBOList = new ArrayList<>();
            adapter.setItems(codeBOList);
            currPage = 1;
            isLoad = false;
            listView.smoothScrollToPosition(0);
            getData();
        }
        currStatus = isGot;
    }

    /**
     * 选择活动类型
     *
     * @param view
     */
    public void toSelectActiveType(View view) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_activeName));
        pBundle.putSerializable("activeList", list_activeType);
        openActivityForResult(SelectActivity.class, pBundle, MyConfig.ACTIVE);
    }

    /**
     * 选择活动日期
     *
     * @param view
     */
    public void toSelectDate(View view) {
        pickDate();
    }

    /**
     * 验证
     *
     * @param view
     */
    public void toCheck(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        winNumber = codeEdt.getText().toString().trim();
        showProgressDialog(getString(R.string.dialog_message));
        this.appAction.checkWinNum(winNumber, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                codeEdt.setText("");
                Dialog dialog = new AlertDialog.Builder(ShopCheckActivity.this).setMessage(data.getMsg()).
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                codeBOList = new ArrayList<>();
                                adapter.setItems(codeBOList);
                                currPage = 1;
                                isLoad = false;
                                listView.smoothScrollToPosition(0);
                                getData();
                            }
                        }).create();
                dialog.show();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    private void getData() {
        if (!isFirst && !isLoad) {
            listView.setVisibility(View.GONE);
            progressRlayout.setVisibility(View.VISIBLE);
        }

        this.appAction.getMerchantInfo(application.getUserId(), activeId, playDt, "", isGot, currPage, MyConfig.PAGESIZE, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                LogUtils.i(TAG, data.getOtherData());
                closeProgressDialog();
                if (!isFirst && !isLoad) {
                    progressRlayout.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    LogUtils.i(TAG, "显示listview,隐藏进度条");
                } else
                    isFirst = false;

                // 更新完后调用该方法结束刷新
                if (isLoad) {
                    listView.onLoadComplete();
                    int len = data.getObjList().size();
                    for (int i = 0; i < len; i++)
                        codeBOList.add((CodeBO) data.getObjList().get(i));
                    if (len > 0) {
                        currPage++;
                        adapter.addItems(codeBOList);
                    } else
                        MyUtil.showToast(getApplicationContext(), "没有更多");
                } else {
                    currPage = 1;
                    currPage++;
                    codeBOList = new ArrayList<>();
                    codeBOList = data.getObjList();
                    adapter.setItems(codeBOList);
                }
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(data.getOtherData());
                    shopName = jsonObject.getString("shopName");
                    shopNameTxt.setText(shopName);
                    totalWinNum = jsonObject.getInt("totalWinNum");
                    totalNumTxt.setText(totalWinNum + "");
                    gotWinNum = jsonObject.getInt("gotWinNum");
                    checkedNumTxt.setText(gotWinNum + "");
                    JSONArray activeList = jsonObject.getJSONArray("activeList");
                    int length = activeList.length();
                    list_activeType = new ArrayList<>();
                    HashMap<String, String> item;
                    for (int i = 0; i < length; i++) {
                        item = new HashMap<>();
                        item.put("activeId", activeList.getJSONObject(i).getInt("activeId") + "");
                        item.put("activeName", activeList.getJSONObject(i).getString("activeName"));
                        list_activeType.add(item);
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                if (!isFirst && !isLoad) {
                    progressRlayout.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                } else
                    isFirst = false;
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    @Override
    public void onLoad() {
        isLoad = true;
        getData();
    }

    /**
     * 选择年月
     */
    private void pickDate() {

        final DatePickerDialog mDialog = new CustomerDatePickerDialog(this, null, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        // 手动设置按钮
        mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
                DatePicker datePicker = mDialog.getDatePicker();
                nYear = datePicker.getYear();
                nMonth = datePicker.getMonth() + 1;
                nDay = datePicker.getDayOfMonth();

                playDt = nYear + "-" + nMonth + "-" + nDay;
                dateTxt.setText(nYear + "年" + nMonth + "月" + nDay + "日");
                currPage = 1;
                isLoad = false;
                codeBOList = new ArrayList<>();
                adapter.setItems(codeBOList);
                getData();

            }
        });
        // 取消按钮，如果不需要直接不设置即可
        mDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        mDialog.show();

        DatePicker dp = mDialog.getDatePicker();
//        ((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);  //隐藏掉日
        dp.setMaxDate(cal.getTime().getTime());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MyConfig.ACTIVE && resultCode == MyConfig.ACTIVE) {
            String temp[] = data.getStringExtra(getString(R.string.intent_activeName)).split("#");
            activeName = temp[0];
            activeNameTxt.setText(activeName);
            activeId = Integer.parseInt(temp[1]);
            currPage = 1;
            isLoad = false;
            getData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
