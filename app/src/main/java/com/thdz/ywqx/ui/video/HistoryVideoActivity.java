package com.thdz.ywqx.ui.video;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

import com.thdz.ywqx.R;
import com.thdz.ywqx.util.TsUtil;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * 历史视频
 * mp4网络视频播放
 */
public class HistoryVideoActivity extends AppCompatActivity {

    private String TAG = "HistoryVideoActivity";

    // 视频控件
    JCVideoPlayerStandard jiecaoPlayer;

    // 视频控件
//    JzvdStd jz_video;

    private String path;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rt_video);

        try {
            path = getIntent().getStringExtra("url");

            initVideoView();
            findViewById(R.id.left_img).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            TsUtil.showToast(this, "视频地址有误");
            finish();
            return;
        }

    }

//    private void initJzVideo() {
//        String testImgUrl = "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640";
//        // 设置视频地址、缩略图地址、标题
//        jz_video.setUp(path, "告警历史视频"); // path
//        // jz_video.posterImageView.setImage(testImgUrl);
//        Glide.with(this).load(testImgUrl).into(jz_video.posterImageView);
//
//    }

    private void initVideoView() {
        jiecaoPlayer = (JCVideoPlayerStandard) findViewById(R.id.jiecao_player);
//        int w = MyApplication.getApplication().screenWidth;
//        int h = w * 16/9;
//        jiecaoPlayer.setLayoutParams(new RelativeLayout.LayoutParams(w,h));
        //设置视频上显示的文字
        jiecaoPlayer.setUp(path, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "视频正在播放~");
        jiecaoPlayer.startVideo();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();

    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

//    @Override
//    public void onBackPressed() {
//        if (Jzvd.backPress()) {
//            return;
//        }
//        super.onBackPressed();
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Jzvd.releaseAllVideos();
//    }

}
