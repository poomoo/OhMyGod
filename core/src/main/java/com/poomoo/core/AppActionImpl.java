package com.poomoo.core;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.poomoo.api.Api;
import com.poomoo.api.ApiImpl;
import com.poomoo.model.AdBO;
import com.poomoo.model.GrabBO;
import com.poomoo.model.ResponseBO;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AppAction接口的实现类
 * 作者: 李苜菲
 * 日期: 2015/11/11 11:14.
 */
public class AppActionImpl implements AppAction {
    private Context context;
    private Api api;

    public AppActionImpl(Context context) {
        this.context = context;
        this.api = new ApiImpl();
    }

    @Override
    public void logIn(final String phoneNum, final String passWord, final ActionCallbackListener listener) {
        // 参数检查
        if (TextUtils.isEmpty(phoneNum)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "手机号为空");
            }
            return;
        }
        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(phoneNum);
        if (!matcher.matches()) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            }
            return;
        }

        if (TextUtils.isEmpty(passWord)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "密码为空");
            }
            return;
        }
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<Void>>() {
            @Override
            protected ResponseBO<Void> doInBackground(Void... voids) {
                return api.login(phoneNum, passWord);
            }

            @Override
            protected void onPostExecute(ResponseBO<Void> response) {
                if (listener != null && response != null) {
                    if (response.isSuccess()) {
                        listener.onSuccess(null);
                    } else {
                        listener.onFailure(response.getRsCode(), response.getMsg());
                    }
                }
            }
        }.execute();

    }

    @Override
    public void getCode(final String phoneNum, final ActionCallbackListener listener) {
        // 参数检查
        if (TextUtils.isEmpty(phoneNum)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "手机号为空");
            }
            return;
        }

        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(phoneNum);
        if (!matcher.matches()) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            }
            return;
        }
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<Void>>() {
            @Override
            protected ResponseBO<Void> doInBackground(Void... voids) {
                return api.getCode(phoneNum);
            }

            @Override
            protected void onPostExecute(ResponseBO<Void> response) {
                if (listener != null && response != null) {
                    if (response.isSuccess()) {
                        listener.onSuccess(response);
                    } else {
                        listener.onFailure(response.getRsCode(), response.getMsg());
                    }
                }
            }
        }.execute();
    }

    @Override
    public void register(final String phoneNum, final String passWord, final String code, final String age, final String sex, final String channelId, final ActionCallbackListener listener) {
        // 参数检查
        if (TextUtils.isEmpty(phoneNum)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "手机号为空");
            }
            return;
        }

        Pattern pattern = Pattern.compile("1\\d{10}");
        Matcher matcher = pattern.matcher(phoneNum);
        if (!matcher.matches()) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_ILLEGAL, "手机号不正确");
            }
            return;
        }

        if (TextUtils.isEmpty(passWord)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "密码为空");
            }
            return;
        }

        if (TextUtils.isEmpty(code)) {
            if (listener != null) {
                listener.onFailure(ErrorEvent.PARAM_NULL, "验证码为空");
            }
            return;
        }

        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<Void>>() {
            @Override
            protected ResponseBO<Void> doInBackground(Void... voids) {
                return api.register(phoneNum, passWord, code, age, sex, channelId);
            }

            @Override
            protected void onPostExecute(ResponseBO<Void> response) {
                if (listener != null && response != null) {
                    if (response.isSuccess()) {
                        listener.onSuccess(null);
                    } else {
                        listener.onFailure(response.getRsCode(), response.getMsg());
                    }
                }
            }
        }.execute();
    }

    @Override
    public void getAdvertisement(final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<AdBO>>() {
            @Override
            protected ResponseBO<AdBO> doInBackground(Void... params) {
                return api.getAdvertisement();
            }

            @Override
            protected void onPostExecute(ResponseBO<AdBO> response) {
                if (listener != null && response != null) {
                    if (response.isSuccess()) {
                        listener.onSuccess(response);
                    } else {
                        listener.onFailure(response.getRsCode(), response.getMsg());
                    }
                }
            }
        }.execute();
    }

    @Override
    public void getGrabList(final String cityName, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<GrabBO>>() {
            @Override
            protected ResponseBO<GrabBO> doInBackground(Void... params) {
                return api.getGrabList(cityName);
            }

            @Override
            protected void onPostExecute(ResponseBO<GrabBO> response) {
                if (listener != null && response != null) {
                    if (response.isSuccess()) {
                        listener.onSuccess(response);
                    } else {
                        listener.onFailure(response.getRsCode(), response.getMsg());
                    }
                }
            }
        }.execute();
    }

    @Override
    public void getCommodityInformation(String userId, String activeId, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<GrabBO>>() {
            @Override
            protected ResponseBO<GrabBO> doInBackground(Void... params) {
//                return api.getGrabList(cityName);
                return null;
            }

            @Override
            protected void onPostExecute(ResponseBO<GrabBO> response) {
                if (listener != null && response != null) {
                    if (response.isSuccess()) {
                        listener.onSuccess(response);
                    } else {
                        listener.onFailure(response.getRsCode(), response.getMsg());
                    }
                }
            }
        }.execute();
    }
}
