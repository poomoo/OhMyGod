package com.poomoo.ohmygod.utils;

import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

import com.poomoo.ohmygod.R;


public class SoundUtil {
    private SoundPool soundpool = null;
    private int housesoundid;
    private int carsoundid;
    private int drawsoundid;
    private int othersoundid;
    public final static int HOUSE = 1;
    public final static int CAR = 2;
    public final static int DRAW = 3;
    public final static int OTHER = 4;
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
            housesoundid = soundpool.load(context, R.raw.house, 1);
            carsoundid = soundpool.load(context, R.raw.click, 1);
            drawsoundid = soundpool.load(context, R.raw.click, 1);
            othersoundid = soundpool.load(context, R.raw.click, 1);
        }
    }

    public void playSound(int soundType) {
        float streamVolume = 0.8f;
        switch (soundType) {
            case HOUSE:
                soundpool.play(housesoundid, streamVolume, streamVolume, 1, 0, 1f);
                break;
            case CAR:
                soundpool.play(carsoundid, streamVolume, streamVolume, 1, 0, 1f);
                break;
            case DRAW:
                soundpool.play(drawsoundid, streamVolume, streamVolume, 1, 0, 1f);
                break;
            case OTHER:
                soundpool.play(othersoundid, streamVolume, streamVolume, 1, 0, 1f);
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
