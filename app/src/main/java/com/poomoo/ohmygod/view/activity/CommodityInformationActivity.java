package com.poomoo.ohmygod.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.poomoo.ohmygod.R;
import com.poomoo.ohmygod.utils.TimeCountDownUtil;
import com.poomoo.ohmygod.view.custom.ProgressSeekBar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者: 李苜菲
 * 日期: 2015/11/13 16:15.
 */
public class CommodityInformationActivity extends BaseActivity {
    private TextView headTimeCountdownTxt;//头部倒计时控件
    private TextView footTimeCountdownTxt;//底部倒计时控件

    private ProgressSeekBar seekBar;
    private TimeCountDownUtil headTimeCountDownUtil;
    private TimeCountDownUtil footTimeCountDownUtil;

    private static final long TIME = 2 * 24 * 60 * 60 * 1000 + 5 * 60 * 60 * 1000 + 57 * 60 * 1000 + 23 * 1000;//System.currentTimeMillis()+30*1000*24*24 2 * 5 * 57 * 21 * 1000


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_information);

        initView();
        decrease();
    }

    private void initView() {
        HeaderViewHolder headerViewHolder = getHeaderView();
        headerViewHolder.title.setText(R.string.title_commodity_information);
        headerViewHolder.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        seekBar = (ProgressSeekBar) findViewById(R.id.activity_commodity_information_seekBar);
        seekBar.setMyPadding(0, 30, 0, 0);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar sb, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                seekBar.setIshide(true);
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
        headTimeCountdownTxt = (TextView) findViewById(R.id.txt_head_timeCountDown);
        footTimeCountdownTxt = (TextView) findViewById(R.id.txt_foot_timeCountDown);
        headTimeCountDownUtil = new TimeCountDownUtil(
                this, TIME, 1000, headTimeCountdownTxt);
        headTimeCountDownUtil.start();

        footTimeCountDownUtil = new TimeCountDownUtil(
                this, TIME, 1000, footTimeCountdownTxt);
        footTimeCountDownUtil.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    seekBar.setProgress(seekBar.getProgress() - 1);
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

    public void toGrab(View view) {
        seekBar.setProgress(seekBar.getProgress() + 5);
    }

}
