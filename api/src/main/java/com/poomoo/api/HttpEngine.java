/**
 * Copyright (C) 2015. Keegan小钢（http://keeganlee.me）
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.poomoo.api;

import android.util.Log;

import com.google.gson.Gson;
import com.poomoo.model.ResponseBO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http引擎处理类
 *
 * @author Keegan小钢
 * @version 1.0
 * @date 15/6/21
 */
public class HttpEngine {
    private final static String TAG = "HttpEngine";
    private final static String REQUEST_METHOD = "POST";
    private final static String ENCODE_TYPE = "UTF-8";
    // IP
    public static final String BaseLocalUrl = "http://192.168.0.122:8080/zgqg/app/";// 本地
    public static final String BaseRemoteUrl = "http://zgqg.91jiaoyou.cn/zgqg/app/";// 远程
    public static final String URL = BaseLocalUrl + "call.htm";

    // 时间
    public static final int TIMEOUT = 1 * 5 * 1000;// 网络通讯超时

    private static HttpEngine instance = null;

    private HttpEngine() {

    }

    public static HttpEngine getInstance() {
        if (instance == null) {
            instance = new HttpEngine();
        }
        return instance;
    }

    public ResponseBO postHandle(Map<String, String> paramsMap, Type typeOfT) throws IOException {
//        String data = joinParams(paramsMap);
        Gson gson = new Gson();
        String data = gson.toJson(paramsMap);
        HttpURLConnection connection = getConnection();
        // 打印出请求
        Log.i(TAG, "request: " + data + "url:" + connection.getURL());
        connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
        connection.connect();

        if (data != null && data.trim().length() > 0) {
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            byte[] content = data.toString().getBytes("utf-8");
            out.write(content, 0, content.length);
            out.flush();
            out.close();
        }

//        OutputStream os = connection.getOutputStream();
//        os.write(data.getBytes());
//        os.flush();
//        os.close();

        Log.i(TAG, "getResponseCode: " + connection.getResponseCode());
        if (connection.getResponseCode() == 200) {
            // 获取响应的输入流对象
            InputStream is = connection.getInputStream();
            // 创建字节输出流对象
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 定义读取的长度
            int len = 0;
            // 定义缓冲区
            byte buffer[] = new byte[1024];
            // 按照缓冲区的大小，循环读取
            while ((len = is.read(buffer)) != -1) {
                // 根据读取的长度写入到os对象中
                baos.write(buffer, 0, len);
            }
            // 释放资源
            is.close();
            baos.close();
            connection.disconnect();
            // 返回字符串
            final String result = new String(baos.toByteArray());
            // 打印出结果
            Log.i(TAG, "response: " + result);
            ResponseBO responseBO = gson.fromJson(result, ResponseBO.class);
            String jsonData = responseBO.getJsonData().toString();
            if (jsonData.contains("records")) {
                try {
                    responseBO.setObjList(new ArrayList());
                    JSONObject jsonObject;
                    jsonObject = new JSONObject(jsonData);
                    JSONArray pager = jsonObject.getJSONArray("records");
                    int length = pager.length();
                    Log.i(TAG, "typeOfT: " + typeOfT);
                    for (int i = 0; i < length; i++) {
                        responseBO.setObj(gson.fromJson(pager.getJSONObject(i).toString(), typeOfT));
                        responseBO.getObjList().add(responseBO.getObj());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                responseBO.setObj(gson.fromJson(jsonData, typeOfT));
            }
            return responseBO;
        } else {
            connection.disconnect();
            return null;
        }
    }

    // 获取connection
    private HttpURLConnection getConnection() {
        HttpURLConnection connection = null;
        // 初始化connection
        try {
            // 根据地址创建URL对象
            URL url = new URL(URL);
            // 根据URL对象打开链接
            connection = (HttpURLConnection) url.openConnection();
            // 设置请求的方式
            connection.setRequestMethod(REQUEST_METHOD);
            // 发送POST请求必须设置允许输入，默认为true
            connection.setDoInput(true);
            // 发送POST请求必须设置允许输出
            connection.setDoOutput(true);
            // 设置不使用缓存
            connection.setUseCaches(false);
            // 设置请求的超时时间
            connection.setReadTimeout(TIMEOUT);
            connection.setConnectTimeout(TIMEOUT);
            connection.setRequestProperty("Content-Type", "application/json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    // 拼接参数列表
    private String joinParams(Map<String, String> paramsMap) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String key : paramsMap.keySet()) {
            stringBuilder.append(key);
            stringBuilder.append("=");
            try {
                stringBuilder.append(URLEncoder.encode(paramsMap.get(key), ENCODE_TYPE));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            stringBuilder.append("&");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}
