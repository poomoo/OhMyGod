package com.poomoo.core;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.poomoo.api.Api;
import com.poomoo.api.ApiImpl;
import com.poomoo.model.AdBO;
import com.poomoo.model.FileBO;
import com.poomoo.model.GrabBO;
import com.poomoo.model.GrabResultBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.model.StatementBO;
import com.poomoo.model.UserBO;
import com.poomoo.model.WinnerBO;
import com.poomoo.model.WinningRecordsBO;
import com.poomoo.model.WithdrawDepositBO;


import java.io.File;
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
        new AsyncTask<Void, Void, ResponseBO<UserBO>>() {
            @Override
            protected ResponseBO<UserBO> doInBackground(Void... params) {
                return api.login(phoneNum, passWord);
            }

            @Override
            protected void onPostExecute(ResponseBO<UserBO> response) {
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
    public void getCommodityInformation(final String userId, final String activeId, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<GrabBO>>() {
            @Override
            protected ResponseBO<GrabBO> doInBackground(Void... params) {
                return api.getCommodityInformation(userId, activeId);
            }

            @Override
            protected void onPostExecute(ResponseBO<GrabBO> response) {
                if (listener != null && response != null) {
                    if (response.isSuccess()) {
                        listener.onSuccess(response);
                    } else {
                        listener.onFailure(response.getRsCode(), response.getMsg());
                    }
                } else {
                    listener.onFailure(400, "连接错误");
                }
            }
        }.execute();
    }

    @Override
    public void putGrab(final String activeId, final String userId, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<GrabResultBO>>() {
            @Override
            protected ResponseBO<GrabResultBO> doInBackground(Void... params) {
                return api.putGrabInfo(activeId, userId);
            }

            @Override
            protected void onPostExecute(ResponseBO<GrabResultBO> response) {
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
    public void getWinnerList(final String cityName, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<WinnerBO>>() {
            @Override
            protected ResponseBO<WinnerBO> doInBackground(Void... params) {
                return api.getWinnerList(cityName);
            }

            @Override
            protected void onPostExecute(ResponseBO<WinnerBO> response) {
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
    public void uploadPics(final FileBO fileBO, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<Void>>() {
            @Override
            protected ResponseBO<Void> doInBackground(Void... params) {
                return api.uploadPics(fileBO);
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
    public void putPersonalInfo(final String userId, final String realName, final String idCardNum, final String bankCardNum, final String idFrontPic, final String idOpsitePic, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<Void>>() {
            @Override
            protected ResponseBO<Void> doInBackground(Void... params) {
                return api.putPersonalInfo(userId, realName, idCardNum, bankCardNum, idFrontPic, idOpsitePic);
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
    public void changePersonalInfo(final String userId, final String key, final String value, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<Void>>() {
            @Override
            protected ResponseBO<Void> doInBackground(Void... params) {
                return api.changePersonalInfo(userId, key, value);
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
    public void toSign(final String userId, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<Void>>() {
            @Override
            protected ResponseBO<Void> doInBackground(Void... params) {
                return api.toSigned(userId);
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
    public void getSignedList(final String userId, final String year, final String month, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<Void>>() {
            @Override
            protected ResponseBO<Void> doInBackground(Void... params) {
                return api.getSignedList(userId, year, month);
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
    public void getStatement(final String type, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<StatementBO>>() {
            @Override
            protected ResponseBO<StatementBO> doInBackground(Void... params) {
                return api.getStatement(type);
            }

            @Override
            protected void onPostExecute(ResponseBO<StatementBO> response) {
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
    public void getWinningList(final String userId, final String flag, final int currPage, final int pageSize, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<WinningRecordsBO>>() {
            @Override
            protected ResponseBO<WinningRecordsBO> doInBackground(Void... params) {
                return api.getWinningList(userId, flag, currPage, pageSize);
            }

            @Override
            protected void onPostExecute(ResponseBO<WinningRecordsBO> response) {
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
    public void getMyWithdrawDepositList(final String userId, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<WithdrawDepositBO>>() {
            @Override
            protected ResponseBO<WithdrawDepositBO> doInBackground(Void... params) {
                return api.getMyWithdrawDepositList(userId);
            }

            @Override
            protected void onPostExecute(ResponseBO<WithdrawDepositBO> response) {
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
    public void putShow(final String userId, final String activeId, final String content, final String pictures, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<Void>>() {
            @Override
            protected ResponseBO<Void> doInBackground(Void... params) {
                return api.putShow(userId, activeId, content, pictures);
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
    public void getShowList(final int flag, final String userId, final int currPage, final int pageSize, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<Void>>() {
            @Override
            protected ResponseBO<Void> doInBackground(Void... params) {
                return api.getShowList(flag, userId, currPage, pageSize);
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
    public void putComment(final String userId, final String content, final String dynamicId, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<Void>>() {
            @Override
            protected ResponseBO<Void> doInBackground(Void... params) {
                return api.putComment(userId, content, dynamicId);
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
    public void putReply(final String fromUserId, final String toUserId, final String content, final String commentId, final ActionCallbackListener listener) {
        // 请求Api
        new AsyncTask<Void, Void, ResponseBO<Void>>() {
            @Override
            protected ResponseBO<Void> doInBackground(Void... params) {
                return api.putReply(fromUserId, toUserId, content, commentId);
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
}
