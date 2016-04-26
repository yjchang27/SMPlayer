package com.yjchang.cs442.smplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by yjchang on 4/26/16.
 */
public class MusicPlayService extends Service
        implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    public static final String ACTION_PLAY = "com.yjchang.cs442.smplayer.action.PLAY";
    private MediaPlayer mMediaPlayer = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_PLAY)) {
            if (mMediaPlayer == null)
                initMediaPlayer();
            else
                mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(getApplicationContext(), intent.getData());
            } catch (IOException e) {
                Toast.makeText(this, "Unsupported file", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                stopSelf();
                return 0;
            }
            // Non-blocking media player preparation
            mMediaPlayer.prepareAsync();
        }

        return 0;
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }

    @Override
    public void onPrepared(MediaPlayer mp) { mp.start(); }

    @Override
    public void onCompletion(MediaPlayer mp) { stopSelf(); }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "MUSIC Service Starting", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "MUSIC Service Dying", Toast.LENGTH_SHORT).show();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        super.onDestroy();
    }
}
