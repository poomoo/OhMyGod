package com.poomoo.ohmygod.view.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.service.Get_UserInfo_Service;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.SPUtils;

import junit.framework.Test;

import org.litepal.tablemanager.Connector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class SplashActivity extends BaseActivity {
    private static String DB_PATH = "/data/data/com.poomoo.ohmygod/databases/";
    private static String DB_NAME = "area.db";

    private final int SPLASH_DISPLAY_LENGHT = 3000;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private boolean isIndex = false;//是否需要引导


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        SQLiteDatabase db = Connector.getDatabase();
        importDB();        // 导入数据库文件
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        initLocation();
        mLocationClient.start();
        isIndex = (boolean) SPUtils.get(getApplicationContext(), getString(R.string.sp_isIndex), true);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
//                if (!(boolean) SPUtils.get(getApplicationContext(), getString(R.string.sp_isLogin), false))
//                    openActivity(LogInActivity.class);
//                else {
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
                    startService(new Intent(SplashActivity.this, Get_UserInfo_Service.class));
                    openActivity(MainFragmentActivity.class);
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
}
