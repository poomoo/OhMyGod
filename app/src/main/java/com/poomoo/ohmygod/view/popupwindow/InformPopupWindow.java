package com.poomoo.ohmygod.view.popupwindow;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.picUtils.BitmapCache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/12 15:33.
 */
public class InformPopupWindow extends PopupWindow {
    private View mMenuView;
    private TextView titleTxt;
    private ImageView moreImg;
    private ImageView closeImg;
    private WebView webView;


    public InformPopupWindow(Activity context, String title, String content, OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popupwindow_inform, null);
//        titleTxt = (TextView) mMenuView.findViewById(R.id.txt_informTitle);
        moreImg = (ImageView) mMenuView.findViewById(R.id.img_more);
        closeImg = (ImageView) mMenuView.findViewById(R.id.img_close);
        webView = (WebView) mMenuView.findViewById(R.id.popup_inform_webView);

        titleTxt.setText(title);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.loadData(content, "text/html; charset=UTF-8", null);// 这种写法可以正确解码

        moreImg.setOnClickListener(itemsOnClick);
        closeImg.setOnClickListener(itemsOnClick);

        this.setContentView(mMenuView);
        mMenuView.setPadding(30, 20, 30, 30);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        this.setAnimationStyle(R.style.PopupAnimation);


//        ObjectAnimator mAnimator;
//        mAnimator=ObjectAnimator.ofFloat(mMenuView,View.X,View.Y,path);
//        mAnimator.start();


        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height_top = mMenuView.findViewById(R.id.popup_inform_layout).getTop();
                int height_bottom = mMenuView.findViewById(R.id.popup_inform_layout).getBottom();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height_top || y > height_bottom) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    private byte[] InputStreamToByte(InputStream is) throws IOException {
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int ch;
        while ((ch = is.read()) != -1) {
            bytestream.write(ch);
        }
        byte imgdata[] = bytestream.toByteArray();
        bytestream.close();
        return imgdata;
    }


}
