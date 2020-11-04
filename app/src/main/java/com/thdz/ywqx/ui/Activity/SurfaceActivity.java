package com.thdz.ywqx.ui.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.thdz.ywqx.R;
import com.thdz.ywqx.base.BaseActivity;

import java.io.IOException;

import butterknife.BindView;

/**
 * 播放rtsp视频
 */
public class SurfaceActivity extends BaseActivity implements
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {

    @BindView(R.id.surfaceview)
    SurfaceView surfaceview;

    private String urlQiujiH = "http://192.188.0.193:8080/stream"; // 球机 http

    private MediaPlayer mediaPlayer;
    private SurfaceHolder surfaceHolder;
    private int videoWidth;
    private int videoHeight;

    @Override
    public void cycleRequest(){
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_surface);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        surfaceHolder = surfaceview.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        Log.v("mplayer", ">>>create ok.");

//        playVideo();

    }

    private void playVideo() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(urlQiujiH);
            mediaPlayer.setDisplay(this.surfaceHolder);
            mediaPlayer.prepare();
            mediaPlayer.start();

            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            // mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Log.v("mplayer", ">>>play video");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null)
            mediaPlayer.release();
        super.onDestroy();
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    public void surfaceCreated(SurfaceHolder arg0) {
        playVideo();
        Log.v("mplayer", ">>>surface created");
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {

    }

    public void onPrepared(MediaPlayer arg0) {
        this.videoWidth = this.mediaPlayer.getVideoWidth();
        this.videoHeight = this.mediaPlayer.getVideoHeight();

        if (this.videoHeight != 0 && this.videoWidth != 0) {
            this.surfaceHolder.setFixedSize(this.videoWidth, this.videoHeight);
            this.mediaPlayer.start();
        }

    }

    public void onCompletion(MediaPlayer arg0) {

    }

    public void onBufferingUpdate(MediaPlayer arg0, int arg1) {

    }

}
