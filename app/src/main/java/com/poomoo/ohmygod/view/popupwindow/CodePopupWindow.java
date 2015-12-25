package com.poomoo.ohmygod.view.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.Code;
import com.poomoo.ohmygod.utils.LogUtils;
import com.poomoo.ohmygod.utils.MyUtil;
import com.poomoo.ohmygod.view.activity.CommodityInformationActivity;

/**
 * 验证码
 * 作者: 李苜菲
 * 日期: 2015/11/12 15:33.
 */
public class CodePopupWindow extends PopupWindow {
    private View mMenuView;
    private LinearLayout layout;
    private TextView changeTxt;
    private ImageView codeImg;
    private EditText codeEdt;

    //产生的验证码
    private String realCode;
    private Activity activity;

    public CodePopupWindow(final Activity context) {
        super(context);
        activity = context;
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = LayoutInflater.from(activity).inflate(R.layout.popupwindow_code, null);
        layout = (LinearLayout) mMenuView.findViewById(R.id.popup_code_layout_all);
        changeTxt = (TextView) mMenuView.findViewById(R.id.txt_change);
        codeEdt = (EditText) mMenuView.findViewById(R.id.et_phoneCodes);
        codeImg = (ImageView) mMenuView.findViewById(R.id.img_showCode);

        //将验证码用图片的形式显示出来
        codeImg.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode();

        changeTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                codeImg.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode();
            }
        });

        codeEdt.addTextChangedListener(new TextWatcher() {
            int len;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                len = s.length();
                LogUtils.i("code", "len:" + len + " s:" + s.toString());
                if (len == 4) {
                    LogUtils.i("code", "realCode:" + realCode + " s:" + s.toString());
                    if (s.toString().equals(realCode)) {
                        dismiss();
//                        CommodityInformationActivity.isCode = true;
                    } else
                        MyUtil.showToast(context, "验证码不对");
                }
            }
        });

        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        this.setBackgroundDrawable(dw);

//        mMenuView.setFocusable(true);
        mMenuView.setFocusableInTouchMode(true);
        mMenuView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                LogUtils.i("code", "的房间开蜡防静电");
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    LogUtils.i("code", "是否显示验证码:");
                    dismiss();
                    activity.finish();
                }
                return true;
            }
        });
    }

}
