package com.poomoo.core;

/**
 * Action的处理结果回调监听器
 * <p/>
 * 作者: 李苜菲
 * 日期: 2015/11/11 11:11.
 */
public interface ActionCallbackListener<T> {
    /**
     * 成功时调用
     *
     * @param data 返回的数据
     */
    public void onSuccess(T data);

    /**
     * 失败时调用
     *
     * @param errorCode 错误码
     * @param message    错误信息
     */
    public void onFailure(int errorCode, String message);
}
