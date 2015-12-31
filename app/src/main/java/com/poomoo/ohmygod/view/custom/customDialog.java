/**
 * @Title: PostalService
 * @Package com.dvt.common
 * @author 李苜菲
 * @date 2014年4月28日 上午9:15:18
 * @version V1.0
 */
package com.poomoo.ohmygod.view.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.listeners.DialogResultListener;


/**
 * @author 李苜菲
 * @ClassName MessageBox_YESNO
 * @Description TODO(有确定和取消按钮的对话框)
 * @date 2014年8月28日 下午4:40:56
 */
public class customDialog extends Dialog {
    int dialogResult;
    private Activity context;
    private TextView okTxt;
    private ImageView closeImg;

    public customDialog(Activity context) {
        super(context);
        this.context = context;
        dialogResult = 0;
        setOwnerActivity(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        onCreate();
    }

    public void onCreate() {
        setContentView(R.layout.messagebox1);
        this.setCanceledOnTouchOutside(false);
        okTxt = (TextView) this.findViewById(R.id.txt_clickToCompleteInfo);
        closeImg = (ImageView) this.findViewById(R.id.img_close);
    }


    public int getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }

    public void endDialog(int result) {
        dismiss();
        setDialogResult(result);
    }

    public int showDialog(final DialogResultListener dialogResultListener) {
        super.show();

        findViewById(R.id.img_close).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View paramView) {
                        endDialog(0);
                        if (dialogResultListener != null)
                            dialogResultListener.onFinishDialogResult(0);
                    }
                });

        findViewById(R.id.txt_clickToCompleteInfo).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View paramView) {
                        endDialog(1);
                        if (dialogResultListener != null)
                            dialogResultListener.onFinishDialogResult(1);
                    }
                });

        this.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                // TODO 自动生成的方法存根
                if (keyCode == KeyEvent.KEYCODE_ENTER
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    endDialog(0);
                    if (dialogResultListener != null)
                        dialogResultListener.onFinishDialogResult(0);
                }
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    return true;
                }
                return false;
            }
        });
        return dialogResult;
    }
}
