/**
 * Copyright (c) 2015. 李苜菲 Inc. All rights reserved.
 */
package com.poomoo.ohmygod.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.poomoo.core.ActionCallbackListener;
import com.poomoo.model.FileBO;
import com.poomoo.model.ResponseBO;
import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.adapter.AddPicsAdapter;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.utils.picUtils.Bimp;
import com.poomoo.ohmygod.utils.picUtils.FileUtils;
import com.poomoo.ohmygod.view.activity.pics.PhotoActivity;
import com.poomoo.ohmygod.view.activity.pics.PhotosActivity;
import com.poomoo.ohmygod.view.fragment.ShowFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 晒单分享
 * 作者: 李苜菲
 * 日期: 2015/11/23 15:27.
 */
public class ShowAndShareActivity extends BaseActivity implements OnItemClickListener {
    private TextView titleTxt;
    private EditText contentEdt;
    private GridView gridView;
    private RadioButton showRBtn;
    private AddPicsAdapter addPicsAdapter;

    private static final int TAKE_PICTURE = 1;
    private String title;
    private int activeId;
    private String content;
    private String pictures = "";
    private FileBO fileBO;
    private List<FileBO> fileBOList;
    private int index = 0;
    private File file;
    private final String image_capture_path = Environment.getExternalStorageDirectory() + "/" + "ohMyGod.temp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_and_share);
        addActivityToArrayList(this);
        initView();
    }

    protected void initView() {
        initTitleBar();

        titleTxt = (TextView) findViewById(R.id.txt_show_title);
        contentEdt = (EditText) findViewById(R.id.edt_show_content);
        gridView = (GridView) findViewById(R.id.grid_add_pics);

        showRBtn = MainFragmentActivity.instance.showRBtn;

        title = getIntent().getStringExtra(getString(R.string.intent_title));
        activeId = getIntent().getIntExtra(getString(R.string.intent_activeId), 0);
        titleTxt.setText(title);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        addPicsAdapter = new AddPicsAdapter(this);
        addPicsAdapter.update();
        gridView.setAdapter(addPicsAdapter);
        gridView.setOnItemClickListener(this);
    }

    protected void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_show_and_share);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                getActivityOutToRight();
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
        LogUtils.i(TAG, "image_capture_path:" + image_capture_path);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(image_capture_path)));
        startActivityForResult(intent, TAKE_PICTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.i(TAG, "onActivityResult--" + "requestCode:" + requestCode + "resultCode:" + resultCode);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (Bimp.drr.size() < 9 && resultCode == -1)
                    Bimp.drr.add(image_capture_path);

                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == Bimp.bmp.size()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            new PopupWindows(ShowAndShareActivity.this, gridView);
        } else {
            Intent intent = new Intent(ShowAndShareActivity.this, PhotoActivity.class);
            intent.putExtra("ID", position);
            startActivity(intent);
        }
    }

    private boolean checkInput() {
        content = contentEdt.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            MyUtil.showToast(getApplicationContext(), "请填写评论");
            return false;
        }
        return true;
    }

    /**
     * 晒单
     *
     * @param view
     */
    public void toShow(View view) {
        if (checkInput()) {
            int len = Bimp.drr.size();
            fileBOList = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                fileBO = new FileBO();
                fileBO.setType("4");
                fileBO.setImgFile(Bimp.files.get(i));
                LogUtils.i(TAG, "i:" + i + "file:" + Bimp.files.get(i));
                fileBOList.add(fileBO);
            }
            showProgressDialog("上传中...");
            if (len > 0)
                uploadPics();
            else
                putShow();
        }
    }

    public void putShow() {
        this.appAction.putShow(application.getUserId(), activeId + "", content, pictures, new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), data.getMsg());
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(int errorCode, String message) {
                closeProgressDialog();
                MyUtil.showToast(getApplicationContext(), message);
                finish();
                getActivityOutToRight();
            }
        });
    }

    public void uploadPics() {
        this.appAction.uploadPics(fileBOList.get(index++), new ActionCallbackListener() {
            @Override
            public void onSuccess(ResponseBO data) {
                JSONObject result;
                try {
                    result = new JSONObject(data.getJsonData().toString());
                    String url = result.getString("picUrl");
                    if (TextUtils.isEmpty(pictures))
                        pictures = url + ";";
                    else
                        pictures += url + ";";
                    Message message = new Message();
                    message.what = 1;
                    myHandler.sendMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int errorCode, String message) {
                MyUtil.showToast(getApplicationContext(), message);
            }
        });
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == 1) {
                if (index == Bimp.drr.size()) {
                    putShow();
                } else {
                    uploadPics();
                }
            }
            super.handleMessage(msg);
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == 1) {
                if (WinningRecordActivity.instance != null)
                    WinningRecordActivity.instance.finish();
                if (MainFragmentActivity.showFragment == null)
                    MainFragmentActivity.showFragment = new ShowFragment();
                MainFragmentActivity.instance.switchFragment(MainFragmentActivity.showFragment);
                MainFragmentActivity.curFragment = MainFragmentActivity.showFragment;
                showRBtn.setChecked(true);
                finish();
                getActivityOutToRight();
            }
            super.handleMessage(msg);
        }
    };
}
