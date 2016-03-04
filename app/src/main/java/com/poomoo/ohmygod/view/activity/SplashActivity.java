package com.poomoo.ohmygod.view.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.PicBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.service.Get_UserInfo_Service;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;
import com.poomoo.ohmygod.utils.picUtils.FileUtils;

import junit.framework.Test;

import org.litepal.tablemanager.Connector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity {
    private static String DB_PATH = "/data/data/com.poomoo.ohmygod/databases/";
    private static String DB_NAME = "area.db";

    private final int SPLASH_DISPLAY_LENGHT = 3000;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private boolean isIndex = false;//是否需要引导
    private int SDK_PERMISSION_REQUEST = 123;
    private String permissionInfo = "";
    private String url = "http://f.hiphotos.baidu.com/image/pic/item/80cb39dbb6fd5266fd25a12eac18972bd40736f9.jpg";
    private ImageView bgImg;
    private List<PicBO> picBOList = new ArrayList<>();
    private String bootPicPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ohmygod/" + "boot.jpg";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        bgImg = (ImageView) findViewById(R.id.img_bg);
        File f = new File(bootPicPath);
        if (f.exists()) {
            LogUtils.i(TAG, "图片不为空");
            bgImg.setImageBitmap(FileUtils.readBitmapByPath(bootPicPath));
        }

        importDB();        // 导入数据库文件
        SQLiteDatabase db = Connector.getDatabase();//新建表
        getPersimmions();
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        mLocationClient.start();
        isIndex = (boolean) SPUtils.get(getApplicationContext(), getString(R.string.sp_isIndex), true);
        LogUtils.i(TAG, "isIndex" + isIndex);
        this.appAction.getBootPics(1, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                picBOList = data.getObjList();
                url = picBOList.get(0).getPicture();
                LogUtils.i(TAG, "URL:" + url);
                if (!TextUtils.isEmpty(url) && !url.equals(SPUtils.get(getApplicationContext(), getString(R.string.sp_bootUrl), ""))) {
                    LogUtils.i(TAG, "有新图片");
                    ImageLoader.getInstance().loadImage(url, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            bgImg.setAdjustViewBounds(true);
                            bgImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            bgImg.setImageBitmap(loadedImage);
                            FileUtils.saveBitmapByPath(loadedImage, bootPicPath);
                            SPUtils.put(getApplicationContext(), getString(R.string.sp_bootUrl), url);
                            start();
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            start();
                        }
                    });
                } else
                    start();
            }

            @Override
            public void onFailure(int errorCode, String message) {

            }
        });

    }

    public void start() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (isIndex) {
                    SPUtils.put(getApplicationContext(), getString(R.string.sp_isIndex), false);
                    Bundle bundle = new Bundle();
                    bundle.putString(getString(R.string.intent_parent), "index");
                    openActivity(IndexViewPagerActivity.class, bundle);
                    finish();
                } else {
                    application.setUserId((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_userId), ""));
                    application.setSex((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_sex), "1"));
                    application.setNickName((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_nickName), ""));
                    application.setRealName((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_realName), ""));
                    application.setAge((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_age), ""));
                    application.setTel((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_phoneNum), ""));
                    application.setAddress((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_address), ""));
                    application.setIsAdvancedUser((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_isAdvancedUser), ""));
                    application.setIdCardNum((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_idCardNum), ""));
                    application.setIdFrontPic((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_idFrontPic), ""));
                    application.setIdOpsitePic((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_idOpsitePic), ""));
                    application.setBankCardNum((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_bankCardNum), ""));
                    application.setBankName((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_bankName), ""));
                    application.setChannelId((String) SPUtils.get(getApplicationContext(), getString(R.string.sp_channelId), ""));
                    application.setUserType((Integer) SPUtils.get(getApplicationContext(), getString(R.string.sp_userType), -1));
                    startService(new Intent(SplashActivity.this, Get_UserInfo_Service.class));
                    openActivity(MainFragmentActivity.class);
                    LogUtils.i(TAG, "openActivity(MainFragmentActivity.class);");
                    finish();
                }

            }
        }, SPLASH_DISPLAY_LENGHT);
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系，
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认false,设置是否使用gps
        option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        // option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        // option.setIsNeedLocationDescribe(true);//
        // 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        // option.setIsNeedLocationPoiList(true);//
        // 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            LogUtils.i(TAG, "location.getLongitude():" + location.getLongitude() + "location.getLatitude():"
                    + location.getLatitude());
            if (!TextUtils.isEmpty(location.getCity())) {
                MyUtil.showToast(getApplicationContext(), "定位城市:" + location.getCity());
                application.setCurrCity(location.getCity());
                application.setLocateCity(location.getCity());
            } else
                MyUtil.showToast(getApplicationContext(), "定位失败");
            mLocationClient.unRegisterLocationListener(myListener);
        }
    }

    private void importDB() {
        // TODO 自动生成的方法存根
        try {
            // 获得.db文件的绝对路径
            String databaseFilename = DB_PATH + DB_NAME;
            File dir = new File(DB_PATH);
            // 如果目录不存在，创建这个目录
            if (!dir.exists())
                dir.mkdir();
            boolean isExists = (new File(databaseFilename)).exists();
            // 如果在目录中不存在 .db文件，则从res\assets目录中复制这个文件到该目录
            if (!isExists) {
                LogUtils.i(TAG, "文件不存在");
                // 获得封装.db文件的InputStream对象
                InputStream is = getAssets().open(DB_NAME);
                FileOutputStream fos = new FileOutputStream(databaseFilename);
                byte[] buffer = new byte[7168];
                int count = 0;
                // 开始复制.db文件
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
                LogUtils.i(TAG, "导入数据库文件结束");
            }

        } catch (Exception e) {
        }
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
                        /*
                         * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
                         */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }
}
