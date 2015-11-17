package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.other.CountDownListener;
import com.poomoo.ohmygod.utils.TimeCountDownUtil;
import com.poomoo.ohmygod.view.custom.ProgressSeekBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 商品详情
 * <p/>
 * 作者: 李苜菲
 * 日期: 2015/11/13 16:15.
 */
public class CommodityInformationActivity extends BaseActivity {
    private String TAG = this.getClass().getSimpleName();
    private TextView headTimeCountdownTxt;//头部倒计时控件
    private TextView middleTimeCountdownTxt;//中部部倒计时控件
    private TextView footTimeCountdownTxt;//底部倒计时控件
    private TextView openActivityTxt;//确认开启活动按钮
    private Button grabBtn;
    private LinearLayout llayout;//底部倒计时显示layout

    private ProgressSeekBar seek;
    private TimeCountDownUtil headTimeCountDownUtil;
    private TimeCountDownUtil footTimeCountDownUtil;
    private List<TextView> textViewList;

    //    private static final long TIME = 2 * 24 * 60 * 60 * 1000 + 5 * 60 * 60 * 1000 + 57 * 60 * 1000 + 23 * 1000;//System.currentTimeMillis()+30*1000*24*24 2 * 5 * 57 * 21 * 1000
    private static final long TIME = 10 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_information);

        initView();
        decrease();
    }

    private void initView() {
        initTitleBar();

        headTimeCountdownTxt = (TextView) findViewById(R.id.txt_head_timeCountDown);
        middleTimeCountdownTxt = (TextView) findViewById(R.id.txt_middle_timeCountDown);
        footTimeCountdownTxt = (TextView) findViewById(R.id.txt_foot_timeCountDown);
        openActivityTxt = (TextView) findViewById(R.id.txt_openActivity);
        seek = (ProgressSeekBar) findViewById(R.id.seek_grab);
        grabBtn = (Button) findViewById(R.id.btn_grab);
        llayout = (LinearLayout) findViewById(R.id.llayout_foot_timeCountDown);

        seek.setMyPadding(0, 30, 0, 0);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar sb, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                seek.setIshide(true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        textViewList = new ArrayList<>();
        textViewList.add(headTimeCountdownTxt);
        textViewList.add(middleTimeCountdownTxt);
        textViewList.add(footTimeCountdownTxt);
        headTimeCountDownUtil = new TimeCountDownUtil(TIME, 1000, textViewList, new CountDownListener() {
            @Override
            public void onFinish(int result) {
                begin();
            }
        });
        headTimeCountDownUtil.start();

//        footTimeCountDownUtil = new TimeCountDownUtil(TIME, 1000, footTimeCountdownTxt);
//        footTimeCountDownUtil.start();
    }

    private void initTitleBar() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.titleTxt.setText(R.string.title_commodity_information);
        headerViewHolder.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**
     * 确认开启活动
     *
     * @param view
     */
    public void toOpen(View view) {
        openActivityTxt.setVisibility(View.GONE);
        middleTimeCountdownTxt.setVisibility(View.VISIBLE);
        llayout.setVisibility(View.GONE);
    }

    public void begin() {
        grabBtn.setBackgroundResource(R.drawable.selector_grab_button_grab);
        grabBtn.setClickable(true);

        seek.setThumb(getResources().getDrawable(R.drawable.ic_progressbar_selected));
        seek.setThumbOffset(0);
    }

    /**
     * 点击增加进度条进度
     *
     * @param view
     */
    public void toGrab(View view) {
        seek.setProgress(seek.getProgress() + 2);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (seek.getProgress() < 100)
                        seek.setProgress(seek.getProgress() - 1);
                    break;
            }
        }
    };

    private void decrease() {
        TimerTask t = new TimerTask() {
            public void run() {
                handler.sendEmptyMessage(1);
            }
        };
        Timer timer = new Timer();
        timer.schedule(t, 1000, 1000);
    }
}
