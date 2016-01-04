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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.FileBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.WinningRecordsBO;
import com.poomoo.ohmygod.R;
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
public class CompleteUserInformationActivity extends BaseActivity {
    private EditText realNameEdt;
    private EditText idCardNumEdt;
    private EditText addressEdt;
    private EditText bankCardNumEdt;
    private ImageView frontIdCardImg;
    private ImageView backIdCardImg;
    private TextView cityTxt;
    private TextView telTxt;
    private Spinner areaSpinner;
    private SelectPicsPopupWindow popupWindow;
    private int flag;//1-正面 2-反面
    private String path = "";
    private Bitmap bitmap;
    private String path1;
    private String path2;
    private File file1;
    private File file2;
    private FileBO fileBO;
    private List<FileBO> fileBOList;
    private int index = 0;
    private List<String> urlList = new ArrayList<>();
    private String realName;
    private String idCardNum;
    private String bankCardNum;
    private String address;

    private ArrayList<CityInfo> cityInfoArrayList;
    private List<String> areaInfoArrayList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private String city_id;
    private String area;

    private static final int NONE = 0;
    private static final int PHOTOHRAPH = 1;// 拍照
    private static final int PHOTORESOULT = 2;// 结果

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private final static String image_capture_path = Environment.getExternalStorageDirectory() + "/" + "OhMyGod.temp";
    private WinningRecordsBO winningRecordsBO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_user_information);
        initView();
    }

    protected void initView() {
        initTitleBar();

        realNameEdt = (EditText) findViewById(R.id.edt_realName);
        idCardNumEdt = (EditText) findViewById(R.id.edt_idCardNum);
        addressEdt = (EditText) findViewById(R.id.edt_address1);
        cityTxt = (TextView) findViewById(R.id.txt_locateCity);
        telTxt = (TextView) findViewById(R.id.txt_celPhoneNum);
        areaSpinner = (Spinner) findViewById(R.id.spinner_area);

        realNameEdt.setText(application.getRealName());
        idCardNumEdt.setText(application.getIdCardNum());
        telTxt.setText(application.getTel());

        cityTxt.setText(application.getLocateCity());
        cityInfoArrayList = MyUtil.getCityList();
        city_id = MyUtil.getCityId(cityInfoArrayList, application.getLocateCity());
        areaInfoArrayList = MyUtil.getAreaList(city_id);

        area = areaInfoArrayList.get(0);
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
     * 身份证正面
     *
     * @param view
     */
    public void toSelectFront(View view) {
        flag = 1;
        select_pics();
    }

    /**
     * 身份证反面
     *
     * @param view
     */
    public void toSelectBack(View view) {
        flag = 2;
        select_pics();
    }

    /**
     * 保存
     *
     * @param view
     */
    public void toDo(View view) {
//        if (checkInput()) {
//            fileBOList = new ArrayList<>();
//            fileBO = new FileBO();
//            fileBO.setType("2");
//            fileBO.setImgFile(file1);
//            fileBOList.add(fileBO);
//
//            fileBO = new FileBO();
//            fileBO.setType("3");
//            fileBO.setImgFile(file2);
//            fileBOList.add(fileBO);
//
//            showProgressDialog("上传中...");
//            upload();
//        }
        if (checkInput())
            submit();
    }

    private boolean checkInput() {
        realName = realNameEdt.getText().toString().trim();
        if (TextUtils.isEmpty(realName)) {
            MyUtil.showToast(getApplicationContext(), "请填写真实姓名");
            return false;
        }

        idCardNum = idCardNumEdt.getText().toString().trim();
        if (TextUtils.isEmpty(idCardNum)) {
            MyUtil.showToast(getApplicationContext(), "请填写身份证号");
            return false;
        }

        if (idCardNum.length() != 18) {
            idCardNumEdt.setFocusable(true);
            idCardNumEdt.requestFocus();
            MyUtil.showToast(getApplicationContext(), "请输入18位有效号码");
            return false;
        }
        address = addressEdt.getText().toString().trim();
        if (!TextUtils.isEmpty(address))
            address = application.getLocateCity() + area + address;

//
//        if (file1 == null) {
//            MyUtil.showToast(getApplicationContext(), "请选择手持身份证正面照");
//            return false;
//        }
//
//        if (file2 == null) {
//            MyUtil.showToast(getApplicationContext(), "请选择手持身份证反面照");
//            return false;
//        }
//
//        bankCardNum = MyUtil.trimAll(bankCardNumEdt.getText().toString().trim());
//        if (TextUtils.isEmpty(bankCardNum)) {
//            MyUtil.showToast(getApplicationContext(), "请填写银行卡号");
//            return false;
//        }
//        if (bankCardNum.length() != 19) {
//            idCardNumEdt.setFocusable(true);
//            idCardNumEdt.requestFocus();
//            MyUtil.showToast(getApplicationContext(), "请输入19位有效卡号");
//            return false;
//        }

        return true;
    }

    public void upload() {
        this.appAction.uploadPics(fileBOList.get(index++), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                try {
                    LogUtils.i(TAG, "onSuccess");
                    JSONObject result = new JSONObject(data.getJsonData().toString());
                    String url = result.getString("picUrl");
                    urlList.add(url);
                    LogUtils.i(TAG, "url:" + url);
                    Message message = new Message();
                    message.what = 1;
                    myHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == 1) {
                if (index > 1) {
                    submit();
                } else {
                    upload();
                }
            }
            super.handleMessage(msg);
        }
    };

    public void submit() {
        showProgressDialog("请稍后...");
        this.appAction.putPersonalInfo(this.application.getUserId(), realName, idCardNum, address, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), "上传成功");
                application.setRealName(realName);
                application.setIdCardNum(idCardNum);
//                application.setIdFrontPic(urlList.get(0));
//                application.setIdOpsitePic(urlList.get(1));
//                application.setBankCardNum(bankCardNum);
                application.setAddress(address);
                SPUtils.put(getApplicationContext(), getString(R.string.sp_realName), realName);
                SPUtils.put(getApplicationContext(), getString(R.string.sp_idCardNum), idCardNum);
                SPUtils.put(getApplicationContext(), getString(R.string.sp_address), address);
                startService(new Intent(CompleteUserInformationActivity.this, Get_UserInfo_Service.class));
                winningRecordsBO = (WinningRecordsBO) getIntent().getSerializableExtra(getString(R.string.intent_value));
                Bundle pBundle = new Bundle();
                pBundle.putSerializable(getString(R.string.intent_value), winningRecordsBO);
                openActivity(WinningRecord2Activity.class, pBundle);
                finish();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    private void select_pics() {
        // 实例化SelectPicPopupWindow
        popupWindow = new SelectPicsPopupWindow(CompleteUserInformationActivity.this, itemsOnClick);
        // 显示窗口
        popupWindow.showAtLocation(CompleteUserInformationActivity.this.findViewById(R.id.llayout_editPersonalInformation),
                Gravity.BOTTOM, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    // 为弹出窗口实现监听类
    private OnClickListener itemsOnClick = new OnClickListener() {

        @Override
        public void onClick(View view) {
            popupWindow.dismiss();
            switch (view.getId()) {
                case R.id.btn_camera:
                    camera();
                    break;
                case R.id.btn_photo:
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, PHOTORESOULT);
                    break;
            }
        }
    };

    private void camera() {
        Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
        intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(image_capture_path)));
        startActivityForResult(intent1, PHOTOHRAPH);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;
        // 拍照
        if (requestCode == PHOTOHRAPH) {
            System.out.println("拍照返回");
            setImage(image_capture_path);
        }
        if (data == null) {
            System.out.println("返回为空");
            return;
        }
        // 处理结果
        if (requestCode == PHOTORESOULT) {
            // 取得返回的Uri,基本上选择照片的时候返回的是以Uri形式，但是在拍照中有得机子呢Uri是空的，所以要特别注意
            Uri mImageCaptureUri = data.getData();
            System.out.println("mImageCaptureUri:" + mImageCaptureUri);
            // 返回的Uri不为空时，那么图片信息数据都会在Uri中获得。如果为空，那么我们就进行下面的方式获取
            if (mImageCaptureUri != null) {
                try {
                    String imagePath;
                    Cursor cursor = getContentResolver().query(mImageCaptureUri, new String[]{Media.DATA}, null,
                            null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(Media.DATA);
                    imagePath = cursor.getString(columnIndex); // 从内容提供者这里获取到图片的路径
                    cursor.close();
                    setImage(imagePath);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void setImage(String path) {
        try {
            bitmap = Bimp.revitionImageSize(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (flag) {
            case 1:
                frontIdCardImg.setImageBitmap(bitmap);
                path1 = Environment.getExternalStorageDirectory() + "/" + "OhMyGod1.jpg";
                file1 = FileUtils.saveBitmapByPath(bitmap, path1);
                break;
            case 2:
                backIdCardImg.setImageBitmap(bitmap);
                path2 = Environment.getExternalStorageDirectory() + "/" + "OhMyGod2.jpg";
                file2 = FileUtils.saveBitmapByPath(bitmap, path2);
                break;
        }
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
}
