/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poomoo.model.CityBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.application.MyApplication;
import com.poomoo.ohmygod.database.ActivityInfo;
import com.poomoo.ohmygod.database.AreaInfo;
import com.poomoo.ohmygod.database.CityInfo;
import com.poomoo.ohmygod.database.HistoryCityInfo;
import com.poomoo.ohmygod.database.MessageInfo;
import com.poomoo.ohmygod.view.activity.LogInActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
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
        if (num.length() != 18)
            return num;
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
            LogUtils.i(TAG, "bankName:" + application.getBankName());
            LogUtils.i(TAG, "getIdFrontPic:" + application.getIdFrontPic());
            LogUtils.i(TAG, "getIdOpsitePic:" + application.getIdOpsitePic());
//            if (TextUtils.isEmpty(application.getRealName()) || TextUtils.isEmpty(application.getBankName())
//                    || TextUtils.isEmpty(application.getBankCardNum()) || TextUtils.isEmpty(application.getIdFrontPic())
//                    || TextUtils.isEmpty(application.getIdOpsitePic()))
            return false;
        } else {
            if (TextUtils.isEmpty(application.getRealName()) || TextUtils.isEmpty(application.getAddress()))
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

    /**
     * 把消息插入id本地数据库
     *
     * @param infoList
     */
    public static void insertMessageInfo(List<MessageInfo> infoList, int flag) {
//        DataSupport.saveAll(infoList);
        for (MessageInfo messageInfo : infoList) {
            Cursor cursor = DataSupport.findBySQL("select * from messageinfo where statementId = ?", messageInfo.getStatementId() + "");
            if (cursor.getCount() == 0) {
                messageInfo.setFlag(flag);
                messageInfo.save();
            }

            cursor.close();
        }
    }

    /**
     * 更新消息为已读
     *
     * @param statementId
     */
    public static void updateMessageInfo(int statementId) {
        ContentValues values = new ContentValues();
        values.put("status", true);
        DataSupport.updateAll(MessageInfo.class, values, "statementId = ?", statementId + "");
    }

    /**
     * 查询未读的消息数
     *
     * @return
     */
    public static int getUnReadInfoCount(int flag) {
        List<MessageInfo> infoList = DataSupport.where("status = ? and flag=?", "0", flag + "").find(MessageInfo.class);
//        Cursor cursor = DataSupport.findBySQL("select * from messageinfo where isRead = ?", "0");
//        int len = cursor.getCount();
        int len = infoList.size();
        LogUtils.i("getUnReadInfoCount", "len:" + len);
        return len;
    }

    /**
     * 消息状态(已读 未读)
     *
     * @return
     */
    public static boolean isRead(int statementId) {
        LogUtils.i(TAG, "statementId:" + statementId);
        List<MessageInfo> infoList = DataSupport.where("statementId = ?", statementId + "").find(MessageInfo.class);
        LogUtils.i(TAG, "isRead:" + infoList.get(0).isStatus());
        if (infoList.size() == 1) {
            if (infoList.get(0).isStatus())
                return true;
        }
        return false;
    }

    /**
     * 插入活动列表
     *
     * @param infoList
     */
    public static void insertActivityInfo(List<ActivityInfo> infoList) {
        for (ActivityInfo activityInfo : infoList) {
            Cursor cursor = DataSupport.findBySQL("select * from activityinfo where activeid = ?", activityInfo.getActiveId() + "");
            if (cursor.getCount() == 0)
                activityInfo.save();
            cursor.close();
        }
    }

    /**
     * 活动提醒设置状态(已设置 未设置)
     *
     * @return
     */
    public static boolean isRemind(int activeId) {
        LogUtils.i(TAG, "activeId:" + activeId);
        List<ActivityInfo> infoList = DataSupport.where("activeId = ?", activeId + "").find(ActivityInfo.class);
        LogUtils.i(TAG, "isRead:" + infoList.get(0).isFlag());
        if (infoList.size() == 1) {
            if (infoList.get(0).isFlag())
                return true;
        }
        return false;
    }

    /**
     * 更新活动提醒状态
     *
     * @param activeId
     */
    public static void updateActivityInfo(int activeId, boolean flag, String eventId) {
        ContentValues values = new ContentValues();
        values.put("flag", flag);
        values.put("eventId", eventId);
        DataSupport.updateAll(ActivityInfo.class, values, "activeId = ?", activeId + "");
    }

    /**
     * 查询活动提醒的ID
     *
     * @return
     */
    public static String getEventId(int activeId) {
        List<ActivityInfo> infoList = DataSupport.where("activeId = ?", activeId + "").find(ActivityInfo.class);
        if (infoList.size() == 1)
            return infoList.get(0).getEventId();
        return "";
    }

    /**
     * 判断app是否正在运行
     *
     * @param ctx
     * @param packageName
     * @return
     */
    public static boolean appIsRunning(Context ctx, String packageName) {
        ActivityManager am = (ActivityManager) ctx.getSystemService(ctx.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        if (runningAppProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.processName.startsWith(packageName)) {
                    LogUtils.i("CallAlarm", "processName:" + runningAppProcessInfo.processName + " packageName:" + packageName);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * app 是否在后台运行
     *
     * @param ctx
     * @param packageName
     * @return
     */
    public static boolean appIsBackgroundRunning(Context ctx, String packageName) {
        ActivityManager am = (ActivityManager) ctx.getSystemService(ctx.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        if (runningAppProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.processName.startsWith(packageName)) {
                    return runningAppProcessInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && runningAppProcessInfo.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE; //排除无界面的app
                }
            }
        }
        return false;
    }

    public static void saveBitmap(Context context, Bitmap bitmap) {
        String path = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg";
        File my2code = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(my2code);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            showToast(context, "图片已保存到:" + path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存历史城市
     *
     * @param cityInfo
     */
    public static void saveHistoryCity(HistoryCityInfo cityInfo) {
        LogUtils.i(TAG, "cityname:" + cityInfo.getCityName());
        Cursor cursor = DataSupport.findBySQL("select * from historycityinfo where cityname = ?", cityInfo.getCityName());
        if (cursor.getCount() == 0)
            cityInfo.save();
        cursor.close();
    }

    /**
     * 查询历史城市信息
     *
     * @return
     */
    public static List<String> getHistoryCitys() {
        List<String> cityBOList = new ArrayList<>();
        List<HistoryCityInfo> historyCityInfoList = DataSupport.findAll(HistoryCityInfo.class);
        int len = historyCityInfoList.size();
        for (int i = 0; i < len; i++)
            cityBOList.add(historyCityInfoList.get(i).getCityName());
        return cityBOList;
    }

    //把字符串转为日期
    public static Date ConverToDate(String strDate) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return df.parse(strDate);
    }


    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }
}
