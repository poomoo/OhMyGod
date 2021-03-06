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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.FileBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.UserBO;
import com.poomoo.model.WinningRecordsBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.database.CityInfo;
import com.poomoo.ohmygod.service.Get_UserInfo_Service;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;
import com.poomoo.ohmygod.utils.StatusBarUtil;
import com.poomoo.ohmygod.utils.picUtils.Bimp;
import com.poomoo.ohmygod.utils.picUtils.FileUtils;
import com.poomoo.ohmygod.view.popupwindow.SelectPicsPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 完善升级会员资料
 * 作者: 李苜菲
 * 日期: 2015/11/17 14:18.
 */
public class CompleteMemberInformationActivity extends BaseActivity {
    private EditText accountNameEdt;
    private EditText bankCardNumEdt;
    private EditText openBankEdt;
    private ImageView frontIdCardImg;
    private ImageView backIdCardImg;
    private SelectPicsPopupWindow popupWindow;
    private int flag;//1-正面 2-反面
    private Bitmap bitmap;
    private String path1;
    private String path2;
    private File file1;
    private File file2;
    private FileBO fileBO;
    private List<FileBO> fileBOList;
    private int index = 0;
    private List<String> urlList = new ArrayList<>();
    private String accountName;
    private String bankCardNum;
    private String bankName;

    private static final int NONE = 0;
    private static final int PHOTOHRAPH = 1;// 拍照
    private static final int PHOTORESOULT = 2;// 结果

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private final static String image_capture_path = Environment.getExternalStorageDirectory() + "/" + "OhMyGod.temp";
    private WinningRecordsBO winningRecordsBO;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_member_information);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.themeRed), 0);
        addActivityToArrayList(this);
        initView();
    }

    protected void initView() {
        initTitleBar();

        accountNameEdt = (EditText) findViewById(R.id.edt_memberInfo_accountName);
        bankCardNumEdt = (EditText) findViewById(R.id.edt_memberInfo_bankAccountNum);
        openBankEdt = (EditText) findViewById(R.id.edt_memberInfo_openBank);
        frontIdCardImg = (ImageView) findViewById(R.id.img_front_idCard);
        backIdCardImg = (ImageView) findViewById(R.id.img_back_idCard);

        MyUtil.fortmatCardNum(bankCardNumEdt);

        initData();
    }


    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_completeInformation);
        headerViewHolder.backImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
            }
        });
    }

    private void initData() {
        options = new DisplayImageOptions.Builder() //
                .cacheInMemory(true) //
                .cacheOnDisk(true) //
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                .build();//
        if (!TextUtils.isEmpty(application.getIdFrontPic()))
            ImageLoader.getInstance().loadImage(application.getIdFrontPic(), options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    path1 = Environment.getExternalStorageDirectory() + "/" + "OhMyGod1.jpg";
                    file1 = FileUtils.saveBitmapByPath(loadedImage, path1);
                    frontIdCardImg.setImageBitmap(loadedImage);
                }
            });
//            ImageLoader.getInstance().displayImage(application.getIdFrontPic(), frontIdCardImg, options);

        if (!TextUtils.isEmpty(application.getIdOpsitePic()))
            ImageLoader.getInstance().loadImage(application.getIdOpsitePic(), options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    path2 = Environment.getExternalStorageDirectory() + "/" + "OhMyGod1.jpg";
                    file2 = FileUtils.saveBitmapByPath(loadedImage, path2);
                    backIdCardImg.setImageBitmap(loadedImage);
                }
            });
//            ImageLoader.getInstance().displayImage(application.getIdOpsitePic(), backIdCardImg, options);


        openBankEdt.setText(application.getBankName());
        accountNameEdt.setText(application.getRealName());
        bankCardNumEdt.setText(application.getBankCardNum());
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
    public void toCompleteMemberInfo(View view) {
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

            showProgressDialog(getString(R.string.dialog_message));
            upload();
        }
    }

    private boolean checkInput() {

        if (file1 == null) {
            MyUtil.showToast(getApplicationContext(), "请选择手持身份证正面照");
            return false;
        }

        if (file2 == null) {
            MyUtil.showToast(getApplicationContext(), "请选择手持身份证反面照");
            return false;
        }
        bankName = openBankEdt.getText().toString().trim();
        if (TextUtils.isEmpty(bankName)) {
            MyUtil.showToast(getApplicationContext(), "请填写开户银行");
            return false;
        }

        accountName = accountNameEdt.getText().toString().trim();
        if (TextUtils.isEmpty(accountName)) {
            MyUtil.showToast(getApplicationContext(), "请填写账户名");
            return false;
        }


        bankCardNum = MyUtil.trimAll(bankCardNumEdt.getText().toString().trim());
        if (TextUtils.isEmpty(bankCardNum)) {
            MyUtil.showToast(getApplicationContext(), "请填写银行账号");
            return false;
        }
        if (bankCardNum.length() != 19) {
            bankCardNumEdt.setFocusable(true);
            bankCardNumEdt.requestFocus();
            MyUtil.showToast(getApplicationContext(), "请输入19位有效账号");
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
        this.appAction.putMemberInfo(this.application.getUserId(), bankName, accountName, bankCardNum, urlList.get(0), urlList.get(1), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), "上传成功");
                UserBO userBO = (UserBO) data.getObj();
                application.setBankName(userBO.getBankName());
                application.setRealName(userBO.getRealName());
                application.setBankCardNum(bankCardNum);
                application.setIdFrontPic(urlList.get(0));
                application.setIdOpsitePic(urlList.get(1));

                SPUtils.put(getApplicationContext(), getString(R.string.sp_bankName), userBO.getBankName());
                SPUtils.put(getApplicationContext(), getString(R.string.sp_realName), userBO.getRealName());
                SPUtils.put(getApplicationContext(), getString(R.string.sp_bankCardNum), bankCardNum);
                SPUtils.put(getApplicationContext(), getString(R.string.sp_idFrontPic), urlList.get(0));
                SPUtils.put(getApplicationContext(), getString(R.string.sp_idOpsitePic), urlList.get(1));
                startService(new Intent(CompleteMemberInformationActivity.this, Get_UserInfo_Service.class));
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

    private void select_pics() {
        // 实例化SelectPicPopupWindow
        popupWindow = new SelectPicsPopupWindow(CompleteMemberInformationActivity.this, itemsOnClick);
        // 显示窗口
        popupWindow.showAtLocation(CompleteMemberInformationActivity.this.findViewById(R.id.llayout_completeMemberInfo),
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
                    intent.setDataAndType(Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
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
            Dialog dialog = new AlertDialog.Builder(CompleteMemberInformationActivity.this).setMessage("资料没有完善,确定退出?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
