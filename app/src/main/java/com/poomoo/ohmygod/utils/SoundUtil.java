package com.poomoo.ohmygod.utils;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

import com.poomoo.ohmygod.R;


public class SoundUtil {
    private SoundPool soundpool = null;
    private int successsoundid;
    public final static int SOUND_TYPE_SUCCESS = 0;
    private static SoundUtil mySound = null;
    private Context context;

    public static SoundUtil getMySound(Context context) {
        if (mySound == null) {
            mySound = new SoundUtil(context);
        }
        return mySound;
    }

    public SoundUtil(Context context) {
        this.context = context;
        if (soundpool == null) {
            soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100);
            successsoundid = soundpool.load(context, R.raw.click, 1);
        }
    }

    public void playSound(int soundType) {
        float streamVolume = 0.8f;
        int soundResId = successsoundid;
        switch (soundType) {
            case SOUND_TYPE_SUCCESS:
                soundpool.play(soundResId, streamVolume, streamVolume, 1, 0, 1f);
                LogUtils.i("SoundUtil", "播放声音");
                break;
            default:
                break;
        }
    }


    public void Vibrate(long milliseconds) {
        try {
            Vibrator vib = (Vibrator) context
                    .getSystemService(Service.VIBRATOR_SERVICE);
            vib.vibrate(milliseconds);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
