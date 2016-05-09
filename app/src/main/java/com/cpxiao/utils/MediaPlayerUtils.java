package com.cpxiao.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Created by cpxiao on 5/9/16.
 * 可播放文件格式：wav、mp3
 * 不能播放：mov
 */
public class MediaPlayerUtils {

    /**
     * 双重校验锁单例模式，注意volatile关键字
     */
    private static volatile MediaPlayerUtils mediaPlayerUtils = null;

    private MediaPlayerUtils() {

    }

    public static MediaPlayerUtils getInstance() {
        if (mediaPlayerUtils == null) {
            synchronized (SoundPoolUtils.class) {
                if (mediaPlayerUtils == null) {
                    mediaPlayerUtils = new MediaPlayerUtils();
                }
            }
        }
        return mediaPlayerUtils;
    }

    /**
     * 音乐状态常量
     */
    private static final int MEDIAPLAYER_START = 0;
    private static final int MEDIAPLAYER_PAUSE = 1;
    private static final int MEDIAPLAYER_STOP = 2;

    /**
     * 当前状态
     */
    private int mediaStat = -1;

    private MediaPlayer mediaPlayer;

    private static final int setTime = 5000;
    private int currentTime;

    private int musicMaxTime;

    private int currentVol;

    private AudioManager audioManager;

    public void init(Context context, int id) {
        mediaPlayer = MediaPlayer.create(context, id);

        if (mediaPlayer != null) {
            mediaPlayer.setLooping(true);

            musicMaxTime = mediaPlayer.getDuration();

            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            if (context instanceof Activity) {
                Log.d("CPXIAO", "aaa");
                ((Activity) context).setVolumeControlStream(AudioManager.STREAM_MUSIC);
            }
        }
    }

    public void start() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
        }
    }

    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    public void soundUp() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol + 1, AudioManager.FLAG_PLAY_SOUND);

    }

    public void soundDown() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVol - 1, AudioManager.FLAG_PLAY_SOUND);
    }

    public void fastForward() {
        int deltaTime = currentTime + setTime;
        if (deltaTime > musicMaxTime) {
            mediaPlayer.seekTo(musicMaxTime);
        } else {
            mediaPlayer.seekTo(deltaTime);
        }
    }

    public void rewind() {
        int deltaTime = currentTime - setTime;
        if (deltaTime < 0) {
            mediaPlayer.seekTo(0);
        } else {
            mediaPlayer.seekTo(deltaTime);
        }
    }


}
