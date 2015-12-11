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

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.poomoo.model.FileBO;
import com.poomoo.model.ResponseBO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    public static final String URL = BaseRemoteUrl + "call.htm";
    public static final String PICSURL = BaseRemoteUrl + "/common/uploadPic.ajax";

    // 时间
    public static final int TIMEOUT = 1 * 30 * 1000;// 网络通讯超时

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
        Gson gson = new Gson();
        String data = gson.toJson(paramsMap);
        HttpURLConnection connection = getConnection();
        // 打印出请求
        Log.i(TAG, "request: " + data + "url:" + connection.getURL() + "  connection:" + connection);
//        connection.setRequestProperty("Content-Length", String.valueOf(data.getBytes().length));
        connection.connect();

        if (data != null && data.trim().length() > 0) {
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            Log.i(TAG, "getOutputStream: " + connection.getOutputStream());
            byte[] content = data.toString().getBytes(ENCODE_TYPE);
            Log.i(TAG, "content: " + content + "   length:" + content.length);
            out.write(content, 0, content.length);
            out.flush();
            out.close();
        }

        Log.i(TAG, "getResponseCode: " + connection.getResponseCode());
        if (connection.getResponseCode() == 200) {
            // 获取响应的输入流对象
            InputStream is = connection.getInputStream();
            // 创建字节输出流对象
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 定义读取的长度
            int len;
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

            if (typeOfT != null) {
                String jsonData = responseBO.getJsonData().toString();
                if (!TextUtils.isEmpty(jsonData)) {
                    if (jsonData.contains("records")) {
                        try {
                            responseBO.setObjList(new ArrayList());
                            JSONObject jsonObject;
                            Log.i(TAG, "jsonData:" + jsonData);
                            jsonObject = new JSONObject(jsonData);
                            JSONArray pager = jsonObject.getJSONArray("records");
                            int length = pager.length();
                            Log.i(TAG, "typeOfT: " + typeOfT);
                            for (int i = 0; i < length; i++) {
                                responseBO.setObj(gson.fromJson(pager.getJSONObject(i).toString(), typeOfT));
                                responseBO.getObjList().add(responseBO.getObj());
                            }
                            responseBO.setObj(gson.fromJson(jsonData, typeOfT));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(TAG, "异常:" + e.getMessage());
                        }
                    } else {
                        responseBO.setObj(gson.fromJson(jsonData, typeOfT));
                    }
                }
            }
            return responseBO;
        } else {
            connection.disconnect();
            return null;
        }
    }

    public ResponseBO uploadFile(FileBO fileBO) throws IOException {
        Log.i(TAG, "uploadFile");
        Gson gson = new Gson();
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型

        URL url = new URL(PICSURL);
        HttpURLConnection conn = (HttpURLConnection) url
                .openConnection();
        conn.setReadTimeout(5 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX);
        sb.append(BOUNDARY);
        sb.append(LINE_END);
        sb.append("Content-Disposition: form-data; name=\"type\"" + LINE_END);
        sb.append("Content-Type: text/plain; charset=" + ENCODE_TYPE + LINE_END);
        sb.append("Content-Transfer-Encoding: 8bit" + LINE_END);
        sb.append(LINE_END);
        sb.append(fileBO.getType());
        sb.append(LINE_END);
        Log.i(TAG, "sb:" + sb.toString());


        DataOutputStream outStream = new DataOutputStream(
                conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        // 发送文件数据
        Log.i(TAG, "fileBO:" + fileBO.getImgFile().getAbsolutePath());
        StringBuilder sb1 = new StringBuilder();
        sb1.append(PREFIX);
        sb1.append(BOUNDARY);
        sb1.append(LINE_END);
        sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
                + fileBO.getImgFile().getAbsolutePath() + "\"" + LINE_END);
        sb1.append("Content-Type: application/octet-stream; charset="
                + ENCODE_TYPE + LINE_END);
        sb1.append(LINE_END);
        Log.i(TAG, "sb1:" + sb1.toString());
        outStream.write(sb1.toString().getBytes());

        InputStream is = new FileInputStream(
                fileBO.getImgFile());
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        is.close();
        outStream.write(LINE_END.getBytes());

        // 请求结束标志
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                .getBytes();
        outStream.write(end_data);
        outStream.flush();

        int res = conn.getResponseCode();
        Log.i(TAG, "response code:" + res);
        // 获取响应的输入流对象
        InputStream is1 = conn.getInputStream();
        // 创建字节输出流对象
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        // 定义读取的长度
        int len1;
        // 定义缓冲区
        byte buffer1[] = new byte[1024];
        // 按照缓冲区的大小，循环读取
        while ((len1 = is1.read(buffer1)) != -1) {
            // 根据读取的长度写入到os对象中
            outputStream.write(buffer1, 0, len1);
        }
        // 释放资源
        is1.close();
        outputStream.close();
        conn.disconnect();
        // 返回字符串
        final String result = new String(outputStream.toByteArray());
        // 打印出结果
        Log.i(TAG, "response: " + result);
        ResponseBO responseBO = gson.fromJson(result, ResponseBO.class);
        return responseBO;
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
            Log.i(TAG, "异常:" + e.getMessage());
        }
        return connection;
    }
}
