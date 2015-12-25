/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.application.MyApplication;
import com.poomoo.ohmygod.database.AreaInfo;
import com.poomoo.ohmygod.database.CityInfo;
import com.poomoo.ohmygod.view.activity.LogInActivity;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/17 14:41.
 */
public class MyUtil {
    private static String TAG = "MyUtil";

    public static String[] CURSOR_COLS = new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ARTIST_ID, MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TRACK};

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    //把字符串转为日期
    public static Date ConvertToDate(String strDate) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        return df.parse(strDate);
    }

    /**
     * 格式化输入银行卡号
     *
     * @param et
     */
    public static void fortmatCardNum(final EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;

            int location = 0;// 记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (isChanged) {
                    location = et.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if (index % 5 == 4) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }

                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }

                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }

                    et.setText(str);
                    Editable etable = et.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
            }
        });
    }

    /**
     * 隐藏手机号
     *
     * @param tel
     * @return
     */
    public static String hiddenTel(String tel) {
        if (TextUtils.isEmpty(tel))
            return "";
        String temp;
        temp = tel.substring(0, 3) + tel.substring(3, 7).replaceAll("[0123456789]", "*")
                + tel.substring(7, tel.length());
        return temp;
    }

    /**
     * 隐藏身份证号
     *
     * @param num
     * @return
     */
    public static String hiddenIdCardNum(String num) {
        if (TextUtils.isEmpty(num))
            return "";
        String temp;
        temp = num.substring(0, 3) + num.substring(3, 14).replaceAll("[0123456789]", "*")
                + num.substring(14, num.length());
        return temp;
    }

    /**
     * 隐藏银行卡号
     *
     * @param num
     * @return
     */
    public static String hiddenBankCardNum(String num) {
        if (TextUtils.isEmpty(num))
            return "";
        String temp;
        temp = num.substring(0, 4) + num.substring(4, 16).replaceAll("[0123456789]", "*")
                + num.substring(16, num.length());
        return temp;
    }

    /**
     * 去掉所有空格
     *
     * @param string
     * @return
     */
    public static String trimAll(String string) {
        return string.replaceAll(" ", "");
    }

    /**
     * 把bitmap保存到sharedPreference
     *
     * @param bitmap
     * @return
     */
    public static String saveDrawable(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        String imageBase64 = new String(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));
        return imageBase64;
    }

    /**
     * 把保存到sharedPreference的bitmap取出来
     *
     * @param context
     * @return
     */
    public static Drawable loadDrawable(Context context) {
        String temp = (String) SPUtils.get(context, context.getString(R.string.sp_headPicBitmap), "");
        LogUtils.i("loadDrawable", "temp:" + temp);
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        return Drawable.createFromStream(bais, "");
    }

    /**
     * @return ArrayList<AreaInfo>
     * @throws @date 2015-8-17上午10:40:43
     * @Title: getAreaList
     * @Description: TODO 获取区域列表
     * @author 李苜菲
     */
    public static List<String> getAreaList(String city_id) {
        LogUtils.i(TAG, "city_id:" + city_id);
        List<AreaInfo> areaList = DataSupport.where("cityinfo_id = ?", city_id).find(AreaInfo.class);
        List<String> list = new ArrayList<>();
        int len = areaList.size();
        for (int i = 0; i < len; i++)
            list.add(areaList.get(i).getArea_name());

        return list;
    }

    /**
     * @return int
     * @throws @date 2015年8月17日下午9:57:59
     * @Title: getCityPosition
     * @Description: TODO 查找某城市的ID
     * @author 李苜菲
     */
    public static String getCityId(ArrayList<CityInfo> cityList, String city) {
        int i = 0;
        for (CityInfo cityInfo : cityList) {
            i++;
            if (cityInfo.getCity_name().equals(city))
                return cityInfo.getCity_id();
        }
        return "";
    }

    /**
     * @return ArrayList<CityInfo>
     * @throws @date 2015-8-17上午10:39:44
     * @Title: getCityList
     * @Description: TODO 获取城市列表
     * @author 李苜菲
     */
    public static ArrayList<CityInfo> getCityList() {
        List<CityInfo> cityList = DataSupport.findAll(CityInfo.class);
        return (ArrayList<CityInfo>) cityList;
    }

    /**
     * double相加
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * double相减
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 是否需要完善资料
     *
     * @return
     */
    public static boolean isNeedCompleteInfo(MyApplication application) {
        if (application.getIsAdvancedUser().equals("1")) { //升级会员
            if (TextUtils.isEmpty(application.getRealName()) || TextUtils.isEmpty(application.getBankName())
                    || TextUtils.isEmpty(application.getBankCardNum()) || TextUtils.isEmpty(application.getIdFrontPic())
                    || TextUtils.isEmpty(application.getIdOpsitePic()))
                return true;
        } else {
            if (TextUtils.isEmpty(application.getRealName()) || TextUtils.isEmpty(application.getIdCardNum())
                    || TextUtils.isEmpty(application.getAddress()))
                return true;
        }
        return false;
    }

    public static boolean isSongExist(Context context, String name) {
        boolean isSongExist = false;
        Cursor c = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, CURSOR_COLS, null,
                null, null);
        if (c != null) {
            while (c.moveToNext()) {
                String songname = c.getString(c.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                String dispalyname = songname.substring(0, songname.lastIndexOf(".")).toLowerCase();

                if (name.equals(dispalyname)) {
                    isSongExist = true;
                    if (c != null)
                        c.close();
                    return isSongExist;

                } else {
                    isSongExist = false;
                }
            }
        }
        if (c != null)
            c.close();
        return isSongExist;
    }

    /**
     * 是否登录
     *
     * @param activity
     * @return
     */
    public static boolean isLogin(Activity activity) {
        if (!(boolean) SPUtils.get(activity, activity.getString(R.string.sp_isLogin), false)) {
            activity.finish();
            Intent intent = new Intent(activity, LogInActivity.class);
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.activity_in_from_right,
                    R.anim.activity_center);
            showToast(activity, "请登录");
            return false;
        } else
            return true;
    }
}
