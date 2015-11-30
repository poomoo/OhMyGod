package com.poomoo.ohmygod.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.core.AppAction;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.application.MyApplication;

/**
 * Fragment 基类
 * 作者: 李苜菲
 * 日期: 2015/11/11 16:23.
 */
public class BaseFragment extends Fragment {
    // 应用全局的实例
    public MyApplication application;
    // 核心层的Action实例
    public AppAction appAction;
    public String TAG = getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        application = (MyApplication) getActivity().getApplication();
        appAction = application.getAppAction();
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        Intent intent = new Intent(getActivity(), pClass);
        getActivity().startActivity(intent);
    }

    /**
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(getActivity(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        getActivity().startActivity(intent);
    }

    /**
     * 统一头部条
     *
     * @return lHeaderViewHolder 头部条对象
     */
    protected HeaderViewHolder getHeaderView() {
        HeaderViewHolder headerViewHolder = new HeaderViewHolder();
        headerViewHolder.titleTxt = (TextView) getActivity().findViewById(R.id.txt_titleBar_name);
        headerViewHolder.backImg = (ImageView) getActivity().findViewById(R.id.img_titleBar_back);
        headerViewHolder.rightImg = (ImageView) getActivity().findViewById(R.id.img_titleBar_right);
        headerViewHolder.rightTxt = (TextView) getActivity().findViewById(R.id.txt_titleBar_right);
        return headerViewHolder;
    }

    protected class HeaderViewHolder {
        public TextView titleTxt;//标题
        public TextView rightTxt;//右边标题
        public ImageView backImg;//返回键
        public ImageView rightImg;//右边图标
    }
}
