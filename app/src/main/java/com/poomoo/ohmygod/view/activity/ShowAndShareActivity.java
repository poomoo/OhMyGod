/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.AddPicsAdapter;
import com.poomoo.ohmygod.utils.picUtils.Bimp;
import com.poomoo.ohmygod.view.activity.pics.PhotoActivity;
import com.poomoo.ohmygod.view.activity.pics.PhotosActivity;

import java.io.File;

/**
 * 晒单分享
 * 作者: 李苜菲
 * 日期: 2015/11/23 15:27.
 */
public class ShowAndShareActivity extends BaseActivity {
    private GridView gridView;
    private AddPicsAdapter addPicsAdapter;

    private static final int TAKE_PICTURE = 0x000000;
    private String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_and_share);

        initView();
    }

    private void initView() {
        initTitleBar();

        gridView = (GridView) findViewById(R.id.grid_add_pics);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        addPicsAdapter = new AddPicsAdapter(this);
        addPicsAdapter.update();
        gridView.setAdapter(addPicsAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == Bimp.bmp.size()) {
                    new PopupWindows(ShowAndShareActivity.this, gridView);
                } else {
                    Intent intent = new Intent(ShowAndShareActivity.this, PhotoActivity.class);
                    intent.putExtra("ID", arg2);
                    startActivity(intent);
                }
            }
        });
    }

    private void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_show_and_share);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class PopupWindows extends PopupWindow {
        private View view;

        public PopupWindows(Context mContext, View parent) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.popup_show_and_share, null);
            LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.popup_supply_layout);

            setContentView(view);
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            setBackgroundDrawable(dw);
            setFocusable(true);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);

            Button bt1 = (Button) view.findViewById(R.id.popup_supply_camera);
            Button bt2 = (Button) view.findViewById(R.id.popup_supply_photo);
            Button bt3 = (Button) view.findViewById(R.id.popup_supply_cancel);
            bt1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    photo();
                    dismiss();
                }
            });
            bt2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(ShowAndShareActivity.this, PhotosActivity.class);
                    startActivity(intent);
                    dismiss();
                }
            });
            bt3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    int height_top = view.findViewById(R.id.popup_supply_layout).getTop();
                    int height_bottom = view.findViewById(R.id.popup_supply_layout).getBottom();
                    int y = (int) motionEvent.getY();
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height_top || y > height_bottom) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        addPicsAdapter.update();
    }

    public void photo() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory() + "/myimage/",
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.drr.size() < 9 && resultCode == -1) {
                    Bimp.drr.add(path);
                }
                break;
        }
    }
}
