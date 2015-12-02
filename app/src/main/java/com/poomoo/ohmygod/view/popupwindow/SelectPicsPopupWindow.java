package com.poomoo.ohmygod.view.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.poomoo.ohmygod.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 作者: 李苜菲
 * 日期: 2015/12/2 15:33.
 */
public class SelectPicsPopupWindow extends PopupWindow {
    private View mMenuView;
    private Button cameraBtn;
    private Button photosBtn;
    private Button cancleBtn;

    public SelectPicsPopupWindow(Activity context, OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_select_pics, null);
        cameraBtn = (Button) mMenuView.findViewById(R.id.btn_camera);
        photosBtn = (Button) mMenuView.findViewById(R.id.btn_photo);
        cancleBtn = (Button) mMenuView.findViewById(R.id.btn_cancel);

        cameraBtn.setOnClickListener(itemsOnClick);
        photosBtn.setOnClickListener(itemsOnClick);
        cancleBtn.setOnClickListener(itemsOnClick);
        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);

        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height_top = mMenuView.findViewById(R.id.popup_select_pics_layout).getTop();
                int height_bottom = mMenuView.findViewById(R.id.popup_select_pics_layout).getBottom();
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
