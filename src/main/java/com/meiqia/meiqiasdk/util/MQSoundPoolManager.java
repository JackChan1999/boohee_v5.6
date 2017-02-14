package com.meiqia.meiqiasdk.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.Builder;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Build.VERSION;
import android.support.annotation.RawRes;

import java.util.HashMap;
import java.util.Map;

public class MQSoundPoolManager {
    private static final int SOUND_INTERNAL_TIME = 500;
    private static final int STREAMS_COUNT       = 1;
    private AudioManager mAudioManager;
    private Context      mContext;
    private long mPrePlayTime = 0;
    private SoundPool             mSoundPool;
    private Map<Integer, Integer> mSoundSourceMap;

    public static MQSoundPoolManager getInstance(Context context) {
        return new MQSoundPoolManager(context.getApplicationContext());
    }

    private MQSoundPoolManager(Context context) {
        this.mContext = context;
        if (VERSION.SDK_INT >= 21) {
            this.mSoundPool = new Builder().setMaxStreams(1).build();
        } else {
            this.mSoundPool = new SoundPool(1, 3, 0);
        }
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        this.mSoundSourceMap = new HashMap();
    }

    public void playSound(@RawRes final int resId) {
        if (this.mSoundSourceMap != null) {
            if (this.mSoundSourceMap.containsKey(Integer.valueOf(resId))) {
                play(((Integer) this.mSoundSourceMap.get(Integer.valueOf(resId))).intValue());
                return;
            }
            this.mSoundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    if (status == 0) {
                        MQSoundPoolManager.this.mSoundSourceMap.put(Integer.valueOf(resId),
                                Integer.valueOf(sampleId));
                        MQSoundPoolManager.this.play(sampleId);
                    }
                }
            });
            this.mSoundPool.load(this.mContext.getApplicationContext(), resId, 1);
        }
    }

    private void play(int soundId) {
        if (!isPlaying() && this.mAudioManager.getRingerMode() != 0) {
            this.mSoundPool.stop(soundId);
            this.mSoundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    public void release() {
        this.mSoundPool.release();
        this.mSoundPool = null;
        this.mAudioManager = null;
        this.mContext = null;
        this.mSoundSourceMap = null;
    }

    private boolean isPlaying() {
        if (System.currentTimeMillis() - this.mPrePlayTime <= 500) {
            return true;
        }
        this.mPrePlayTime = System.currentTimeMillis();
        return false;
    }
}
