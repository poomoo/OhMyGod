/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Base64;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.poomoo.model.ResponseBO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/17 14:41.
 */
public class MyUtil {
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
        String temp;
        temp = tel.substring(0, 3) + tel.substring(3, 8).replaceAll("[0123456789]", "*")
                + tel.substring(8, tel.length());
        return temp;
    }

    /**
     * 隐藏身份证号
     *
     * @param num
     * @return
     */
    public static String hiddenIdCardNum(String num) {
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
        String temp = (String) SPUtils.get(context, "headPic", "");
        LogUtils.i("loadDrawable", "temp:" + temp);
        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(temp.getBytes(), Base64.DEFAULT));
        return Drawable.createFromStream(bais, "");
    }
}
