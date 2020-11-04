package com.thdz.ywqx.ui.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.thdz.ywqx.R;
import com.thdz.ywqx.base.BaseActivity;

import java.io.IOException;

import butterknife.BindView;

/**
 * 播放rtsp视频
 */
public class VideoActivity extends BaseActivity {

    @BindView(R.id.videoview)
    VideoView videoview;

    @BindView(R.id.btnplay)
    Button btnplay;

    @BindView(R.id.btnmp4play)
    Button btnmp4play;

    @BindView(R.id.btnoggplay)
    Button btnoggplay;

    @BindView(R.id.btnoggplay2)
    Button btnoggplay2;

    private String urlNet = "http://img1.peiyinxiu.com/2014121211339c64b7fb09742e2c.mp4"; // 动画片
    private String urlHaiguan = "rtsp://218.204.223.237:554/live/1/66251FC11353191F/e7ooqwcfbqjoo80j.sdp"; // 海关
    private String urlQiujiR = "rtsp://admin:GALAXY123@192.168.2.51/mpeg4/sub/av_stream"; // 球机 rtsp main  sub
    private String urlH264 = "rtsp://admin:GALAXY123@192.168.2.51/h264/ch1/sub/av_stream"; // 球机 rtsp main  sub
    private String urlQiujiH = "http://192.188.0.193:8080/stream"; // 球机 http

    private MediaPlayer mp = new MediaPlayer();

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_video);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        btnplay.setOnClickListener(this);
        btnmp4play.setOnClickListener(this);
        btnoggplay.setOnClickListener(this);
        btnoggplay2.setOnClickListener(this);

//        Intent intent = getIntent();
//        boolean isLocal = intent.getBooleanExtra("isLocal", false);
//        String url = intent.getStringExtra("url");
//        if (isLocal) {
//            currentUrl = urlNet;
//        } else {
//            currentUrl = urlHaiguan;
//        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnplay:
                playRtsp(urlQiujiR); //
                break;
            case R.id.btnmp4play:
                playRtsp(urlH264); //
                break;
            case R.id.btnoggplay:

                Intent it = new Intent();
                it.setAction(Intent.ACTION_VIEW);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Uri uri = Uri.parse(urlQiujiH);//此url就是流媒体文件的下载地址
                it.setDataAndType(uri, "video/*");//type的值是 "video/*"或者 "audio/*"
                startActivity(it);


//                Intent intent = new Intent(context, SurfaceActivity.class);
//                startActivity(intent);

//                Intent it = new Intent();
//                it.setAction(Intent.ACTION_VIEW);
//                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                Uri uri = Uri.parse(url);//此url就是流媒体文件的下载地址
//                it.setDataAndType(uri, type);//type的值是 "video/*"或者 "audio/*"
//                startActivity(it);

                break;

            case R.id.btnoggplay2:
                try {
                    mp.setDataSource(urlQiujiH);
                    mp.prepare();
                    mp.start();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });

        }
    }

    @Override
    public void cycleRequest(){
    }

    private void playRtsp(String url) {
        videoview.setMediaController(new MediaController(this));
        videoview.setVideoURI(Uri.parse(url)); // urlNet  urlHaiguan
        videoview.requestFocus();
        videoview.start();
    }

    @Override
    protected void onDestroy() {
        if (mp != null)
            mp.release();
        super.onDestroy();
    }

}
