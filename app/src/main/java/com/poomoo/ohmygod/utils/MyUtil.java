/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/17 14:41.
 */
public class MyUtil {
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
