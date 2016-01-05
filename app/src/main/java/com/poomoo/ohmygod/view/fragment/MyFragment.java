/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.FileBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.PersonalCenterAdapter;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;
import com.poomoo.ohmygod.utils.picUtils.Bimp;
import com.poomoo.ohmygod.utils.picUtils.FileUtils;
import com.poomoo.ohmygod.view.activity.InStationMessagesActivity;
import com.poomoo.ohmygod.view.activity.MainFragmentActivity;
import com.poomoo.ohmygod.view.activity.MyShowActivity;
import com.poomoo.ohmygod.view.activity.MyWithdrawDepositActivity;
import com.poomoo.ohmygod.view.activity.SettingActivity;
import com.poomoo.ohmygod.view.activity.SnatchRecordActivity;
import com.poomoo.ohmygod.view.activity.WebViewActivity;
import com.poomoo.ohmygod.view.activity.WinningRecordActivity;
import com.poomoo.ohmygod.view.activity.WithdrawDepositActivity;
import com.poomoo.ohmygod.view.activity.WithdrawDepositDetailsActivity;
import com.poomoo.ohmygod.view.popupwindow.SelectPicsPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 我
 * 作者: 李苜菲
 * 日期: 2015/11/20 15:25.
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {
    private ImageView dialImg;
    private ImageView settingImg;
    private ImageView avatarImg;
    private TextView nickNameTxt;
    private TextView balanceTxt;
    private TextView infoCountTxt;
    private ImageView genderImg;
    private RelativeLayout snatchRlayout;
    private RelativeLayout winRlayout;
    private RelativeLayout withDrawRecordRlayout;
    private RelativeLayout withDrawRlayout;
    private RelativeLayout innerRlayout;
    private RelativeLayout showRlayout;
    private RelativeLayout contactRlayout;

    private String headPic;
    private SelectPicsPopupWindow popupWindow;

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
    private String key;
    private String value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {

        dialImg = (ImageView) getActivity().findViewById(R.id.img_dial);
        settingImg = (ImageView) getActivity().findViewById(R.id.img_personal_setting);
        avatarImg = (ImageView) getActivity().findViewById(R.id.img_personal_avatar);
        genderImg = (ImageView) getActivity().findViewById(R.id.img_personal_gender);
        nickNameTxt = (TextView) getActivity().findViewById(R.id.txt_personal_nickName);
        balanceTxt = (TextView) getActivity().findViewById(R.id.txt_personal_walletBalance);
        infoCountTxt = (TextView) getActivity().findViewById(R.id.txt_personal_centerinform_count);

        snatchRlayout = (RelativeLayout) getActivity().findViewById(R.id.rlayout_snatch);
        winRlayout = (RelativeLayout) getActivity().findViewById(R.id.rlayout_winning);
        withDrawRecordRlayout = (RelativeLayout) getActivity().findViewById(R.id.rlayout_withDrawDepositRecord);
        withDrawRlayout = (RelativeLayout) getActivity().findViewById(R.id.rlayout_withDrawDeposit);
        innerRlayout = (RelativeLayout) getActivity().findViewById(R.id.rlayout_innerInfo);
        showRlayout = (RelativeLayout) getActivity().findViewById(R.id.rlayout_show);
        contactRlayout = (RelativeLayout) getActivity().findViewById(R.id.rlayout_contactUs);

        catInfoCount();

        snatchRlayout.setOnClickListener(this);
        winRlayout.setOnClickListener(this);
        withDrawRecordRlayout.setOnClickListener(this);
        withDrawRlayout.setOnClickListener(this);
        innerRlayout.setOnClickListener(this);
        showRlayout.setOnClickListener(this);
        contactRlayout.setOnClickListener(this);

        dialImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent_dial = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getString(R.string.dial)));
//                startActivity(intent_dial);

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getString(R.string.dial)));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        avatarImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_pics();
            }
        });

        settingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SettingActivity.class);
            }
        });
    }

    private void catInfoCount() {
        int count = MyUtil.getUnReadInfoCount();
        if (count > 0) {
            infoCountTxt.setVisibility(View.VISIBLE);
            infoCountTxt.setText(count + "");
        }
    }

    private void select_pics() {
        // 实例化SelectPicPopupWindow
        popupWindow = new SelectPicsPopupWindow(getActivity(), itemsOnClick1);
        // 显示窗口
        popupWindow.showAtLocation(getActivity().findViewById(R.id.llayout_main),
                Gravity.BOTTOM, 0, 0); // 设置layout在PopupWindow中显示的位置
    }

    // 为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick1 = new View.OnClickListener() {

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    Cursor cursor = getActivity().getContentResolver().query(mImageCaptureUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
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

    public void upload() {
        showProgressDialog(getString(R.string.dialog_message));
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
                MyUtil.showToast(getActivity().getApplicationContext(), message);
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
                SPUtils.put(getActivity().getApplicationContext(), getString(R.string.sp_headPic), url);
                SPUtils.put(getActivity().getApplicationContext(), getString(R.string.sp_headPicBitmap), MyUtil.saveDrawable(bitmap));
                avatarImg.setImageBitmap(bitmap);
                MyUtil.showToast(getActivity().getApplicationContext(), "修改头像成功");
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getActivity().getApplicationContext(), message);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        headPic = (String) SPUtils.get(getActivity().getApplicationContext(), getString(R.string.sp_headPicBitmap), "");
        LogUtils.i(TAG, "headPic:" + headPic);
        if (!TextUtils.isEmpty(headPic))
            avatarImg.setImageDrawable(MyUtil.loadDrawable(getActivity().getApplicationContext()));
        else if (!TextUtils.isEmpty(application.getHeadPic())) {
            DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder() //
                    .showImageForEmptyUri(R.drawable.ic_avatar) //
                    .showImageOnFail(R.drawable.ic_avatar) //
                    .cacheInMemory(true) //
                    .cacheOnDisk(true) //
                    .bitmapConfig(Bitmap.Config.RGB_565)// 设置最低配置
                    .build();//
            ImageLoader.getInstance().loadImage(application.getHeadPic(), defaultOptions, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    avatarImg.setImageBitmap(loadedImage);
                    SPUtils.put(getActivity().getApplicationContext(), getString(R.string.sp_headPic), MyUtil.saveDrawable(loadedImage));
                }
            });
        }
        nickNameTxt.setText(application.getNickName());
        if (TextUtils.isEmpty(application.getSex()))
            genderImg.setVisibility(View.GONE);
        else if (application.getSex().equals("1"))
            genderImg.setImageResource(R.drawable.ic_gender_man);
        else
            genderImg.setImageResource(R.drawable.ic_gender_woman);
        balanceTxt.setText(TextUtils.isEmpty(application.getCurrentFee()) ? "0" : application.getCurrentFee());
        catInfoCount();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlayout_snatch:
                openActivity(SnatchRecordActivity.class);
                break;
            case R.id.rlayout_winning:
                openActivity(WinningRecordActivity.class);
                break;
            case R.id.rlayout_withDrawDepositRecord:
                openActivity(MyWithdrawDepositActivity.class);
                break;
            case R.id.rlayout_withDrawDeposit:
                openActivity(WithdrawDepositActivity.class);
                break;
            case R.id.rlayout_innerInfo:
                Bundle bundle = new Bundle();
                bundle.putSerializable(getString(R.string.intent_value), (Serializable) MainFragmentActivity.messageBOList);
                bundle.putString(getString(R.string.intent_parent), getString(R.string.intent_pubMessage));
                openActivity(InStationMessagesActivity.class, bundle);
                break;
            case R.id.rlayout_show:
                openActivity(MyShowActivity.class);
                break;
            case R.id.rlayout_contactUs:
                bundle = new Bundle();
                bundle.putSerializable(getString(R.string.intent_value), (Serializable) MainFragmentActivity.messageBOList);
                bundle.putString(getString(R.string.intent_parent), getString(R.string.intent_pubMessage));
//                openActivity(WebViewActivity.class, bundle);
                break;
        }
    }
}
