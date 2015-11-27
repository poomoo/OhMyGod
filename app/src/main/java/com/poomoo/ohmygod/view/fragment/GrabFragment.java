package com.poomoo.ohmygod.view.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.core.AppAction;
import com.poomoo.model.AdBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.application.MyApplication;
import com.poomoo.ohmygod.view.custom.SlideShowView;
import com.poomoo.ohmygod.view.popupwindow.InformPopupWindow;

import java.util.List;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/11 16:26.
 */
public class GrabFragment extends BaseFragment {
    private SlideShowView slideShowView;
    private String[] urls;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grab, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        slideShowView = (SlideShowView) getActivity().findViewById(R.id.flipper_ad);
        getAd();
    }

    private void getAd() {
        this.appAction.getAdvertisement(new ActionCallbackListener<ResponseBO<List<AdBO>>>() {
            @Override
            public void onSuccess(ResponseBO<List<AdBO>> data) {
                urls = new String[data.getObjList().size()];
                slideShowView = new SlideShowView(getActivity(), urls);
            }

            @Override
            public void onFailure(int errorCode, String message) {

            }
        });
    }

}
