/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.provider.MediaStore.Images.Media;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.FileBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.service.Get_UserInfo_Service;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.picUtils.Bimp;
import com.poomoo.ohmygod.utils.picUtils.FileUtils;
import com.poomoo.ohmygod.view.popupwindow.SelectPicsPopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 编辑个人信息
 * 作者: 李苜菲
 * 日期: 2015/11/17 14:18.
 */
public class EditPersonalInformationActivity extends BaseActivity {
    private EditText realNameEdt;
    private EditText idCardNumEdt;
    private EditText bankCardNumEdt;
    private ImageView frontIdCardImg;
    private ImageView backIdCardImg;
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


    private static final int NONE = 0;
    private static final int PHOTOHRAPH = 1;// 拍照
    private static final int PHOTORESOULT = 2;// 结果

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private final static String image_capture_path = Environment.getExternalStorageDirectory() + "/" + "OhMyGod.temp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_information);
        initView();
    }

    protected void initView() {
        initTitleBar();

        realNameEdt = (EditText) findViewById(R.id.edt_realName);
        idCardNumEdt = (EditText) findViewById(R.id.edt_idCardNum);
        bankCardNumEdt = (EditText) findViewById(R.id.edt_bankCardNum);
        frontIdCardImg = (ImageView) findViewById(R.id.img_front_idCard);
        backIdCardImg = (ImageView) findViewById(R.id.img_back_idCard);

        realNameEdt.setText(application.getRealName());
        idCardNumEdt.setText(application.getIdCardNum());
        bankCardNumEdt.setText(application.getBankCardNum());

        if (!TextUtils.isEmpty(application.getIdFrontPic()) && !TextUtils.isEmpty(application.getIdOpsitePic())) {
            ImageLoader.getInstance().displayImage(application.getIdFrontPic(), frontIdCardImg);
            ImageLoader.getInstance().displayImage(application.getIdOpsitePic(), backIdCardImg);
        }


        MyUtil.fortmatCardNum(bankCardNumEdt);
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_edit_personal_information);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        if (checkInput()) {
            fileBOList = new ArrayList<>();
            fileBO = new FileBO();
            fileBO.setType("2");
            fileBO.setImgFile(file1);
            fileBOList.add(fileBO);

            fileBO = new FileBO();
            fileBO.setType("3");
            fileBO.setImgFile(file2);
            fileBOList.add(fileBO);

            showProgressDialog("上传中...");
            upload();
        }

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

        if (file1 == null) {
            MyUtil.showToast(getApplicationContext(), "请选择手持身份证正面照");
            return false;
        }

        if (file2 == null) {
            MyUtil.showToast(getApplicationContext(), "请选择手持身份证反面照");
            return false;
        }

        bankCardNum = MyUtil.trimAll(bankCardNumEdt.getText().toString().trim());
        if (TextUtils.isEmpty(bankCardNum)) {
            MyUtil.showToast(getApplicationContext(), "请填写银行卡号");
            return false;
        }
        if (bankCardNum.length() != 19) {
            idCardNumEdt.setFocusable(true);
            idCardNumEdt.requestFocus();
            MyUtil.showToast(getApplicationContext(), "请输入19位有效卡号");
            return false;
        }

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
        this.appAction.putPersonalInfo(this.application.getUserId(), realName, idCardNum, bankCardNum, urlList.get(0), urlList.get(1), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                MyUtil.showToast(getApplicationContext(), "上传成功");
                application.setRealName(realName);
                application.setIdCardNum(idCardNum);
                application.setIdFrontPic(urlList.get(0));
                application.setIdOpsitePic(urlList.get(1));
                application.setBankCardNum(bankCardNum);
                startService(new Intent(EditPersonalInformationActivity.this, Get_UserInfo_Service.class));
                finish();
            }

            @Override
            public void onFailure(int errorCode, String message) {
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    private void select_pics() {
        // 实例化SelectPicPopupWindow
        popupWindow = new SelectPicsPopupWindow(EditPersonalInformationActivity.this, itemsOnClick);
        // 显示窗口
        popupWindow.showAtLocation(EditPersonalInformationActivity.this.findViewById(R.id.llayout_editPersonalInformation),
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

}
