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
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.FileBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;
import com.poomoo.ohmygod.utils.picUtils.Bimp;
import com.poomoo.ohmygod.utils.picUtils.FileUtils;
import com.poomoo.ohmygod.view.popupwindow.GenderPopupWindow;
import com.poomoo.ohmygod.view.popupwindow.SelectPicsPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 个人资料
 * 作者: 李苜菲
 * 日期: 2015/11/24 15:37.
 */
public class UserInfoActivity extends BaseActivity {
    private ImageView headImg;
    private TextView nickNameTxt;
    private TextView genderTxt;
    private TextView ageTxt;
    private TextView phoneNumTxt;
    private TextView idCardNumTxt;
    private TextView bankCardNumTxt;
    private GenderPopupWindow genderWindow;
    private SelectPicsPopupWindow popupWindow;

    private String key;
    private String value;
    private boolean isMan = true;//true男 false女
    public static UserInfoActivity instance;

    private static final int NONE = 0;
    private static final int PHOTOHRAPH = 1;// 拍照
    private static final int PHOTORESOULT = 2;// 结果

    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static String image_capture_path = Environment.getExternalStorageDirectory() + "/" + "OhMyGodHead.temp";

    private File file;
    private List<FileBO> fileBOList;
    private String url;
    private FileBO fileBO;
    private Bitmap bitmap;
    private String headPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        instance = this;
        initView();
    }

    protected void initView() {
        initTitleBar();

        headImg = (ImageView) findViewById(R.id.img_userInfo_head);
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
        LogUtils.i(TAG, "bitmap:" + bitmap + "url:" + application.getHeadPic());
        headPic = (String) SPUtils.get(getApplicationContext(), "headPic", "");
        if (!TextUtils.isEmpty(headPic))
            headImg.setImageDrawable(MyUtil.loadDrawable(getApplicationContext()));
        else if (!TextUtils.isEmpty(application.getHeadPic())) {
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                    .showImageForEmptyUri(R.drawable.ic_avatar) //
                    .showImageOnFail(R.drawable.ic_avatar) //
                    .cacheInMemory(true) //
                    .cacheOnDisk(false) //
                    .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                    .build();//
            ImageLoader.getInstance().loadImage(application.getHeadPic(), defaultOptions, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    headImg.setImageBitmap(loadedImage);
                    SPUtils.put(getApplicationContext(), "headPic", MyUtil.saveDrawable(loadedImage));
                }
            });
        }

        nickNameTxt.setText(this.application.getNickName());
        genderTxt.setText(this.application.getSex().equals("1") ? "男" : "女");
        ageTxt.setText(this.application.getAge());
        phoneNumTxt.setText(MyUtil.hiddenTel(this.application.getTel()));
        idCardNumTxt.setText(MyUtil.hiddenIdCardNum(this.application.getIdCardNum()));
        bankCardNumTxt.setText(MyUtil.hiddenBankCardNum(this.application.getBankCardNum()));
    }

    /**
     * 头像
     *
     * @param view
     */
    public void toAvatar(View view) {
        select_pics();
    }

    /**
     * 昵称
     *
     * @param view
     */
    public void toNickName(View view) {
        Bundle pBundle = new Bundle();
        pBundle.putString(getString(R.string.intent_parent), getString(R.string.intent_nickName));
        openActivity(NickNameActivity.class, pBundle);
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
        openActivity(NickNameActivity.class, pBundle);
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
            toSubmitGender();
        }
    };

    /**
     * 提交
     */
    private void toSubmitGender() {
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

    private void select_pics() {
        // 实例化SelectPicPopupWindow
        popupWindow = new SelectPicsPopupWindow(UserInfoActivity.this, itemsOnClick1);
        // 显示窗口
        popupWindow.showAtLocation(UserInfoActivity.this.findViewById(R.id.llayout_userInfo),
                Gravity.BOTTOM, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    // 为弹出窗口实现监听类
    private OnClickListener itemsOnClick1 = new OnClickListener() {

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
                    Cursor cursor = getContentResolver().query(mImageCaptureUri, new String[]{MediaStore.Images.Media.DATA}, null,
                            null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    String imagePath = cursor.getString(columnIndex); // 从内容提供者这里获取到图片的路径
                    cursor.close();
                    setImage(imagePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void upload() {
        showProgressDialog("提交中...");
        this.appAction.uploadPics(fileBOList.get(0), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                try {
                    LogUtils.i(TAG, "onSuccess");
                    JSONObject result = new JSONObject(data.getJsonData().toString());
                    url = result.getString("picUrl");
                    toSubmitHeadPic();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    /**
     * 提交
     */
    private void toSubmitHeadPic() {
        key = "headPic";
        value = url;

        this.appAction.changePersonalInfo(this.application.getUserId(), key, value, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                application.setHeadPic(url);
                MyUtil.saveDrawable(bitmap);
                headImg.setImageBitmap(bitmap);
                MyUtil.showToast(getApplicationContext(), "修改头像成功");
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }


    private void setImage(String path) {
        try {
            bitmap = Bimp.revitionImageSize(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        file = FileUtils.saveBitmapByPath(bitmap, image_capture_path);
        fileBOList = new ArrayList<>();
        fileBO = new FileBO();
        fileBO.setType("1");
        fileBO.setImgFile(file);
        fileBOList.add(fileBO);
        upload();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
