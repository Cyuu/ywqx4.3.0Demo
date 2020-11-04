package com.thdz.ywqx.ui.video;

import android.content.res.Configuration;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hikvision.open.hikvideoplayer.CustomRect;
import com.hikvision.open.hikvideoplayer.HikVideoPlayer;
import com.hikvision.open.hikvideoplayer.HikVideoPlayerCallback;
import com.hikvision.open.hikvideoplayer.HikVideoPlayerFactory;
import com.thdz.ywqx.R;
import com.thdz.ywqx.ui.video.widget.AutoHideView;
import com.thdz.ywqx.ui.video.widget.PlayWindowContainer;

import java.text.DecimalFormat;
import java.text.MessageFormat;

/**
 * 错误码开头：17是mgc或媒体取流SDK的错误，18是vod，19是dac
 */
public class PreviewActivity extends AppCompatActivity implements View.OnClickListener, HikVideoPlayerCallback, TextureView.SurfaceTextureListener {

    private static final String TAG = "PreviewActivity";

    // private String previewUri = "rtsp://10.66.165.243:655/EUrl/8ccdhg4";

    /**
     * 播放区域
     */
    protected PlayWindowContainer frameLayout;
    protected TextureView textureView;
    protected ProgressBar progressBar;
    protected TextView playHintText;
    protected TextView digitalScaleText;
    protected AutoHideView autoHideView;
    /**
     * 控制按钮
     */
//    protected View start;
//    protected View stop;

    private String mUri;
    private HikVideoPlayer mPlayer;
    private boolean mDigitalZooming = false;
    private PlayerStatus mPlayerStatus = PlayerStatus.IDLE;//默认闲置
    /**
     * 电子放大倍数格式化,显示小数点后一位
     */
    private DecimalFormat decimalFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);//防止键盘弹出
        super.setContentView(R.layout.activity_preview);

        mUri = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title"); // 标题，

        initView();
        initPlayWindowContainer();
        mPlayer = HikVideoPlayerFactory.provideHikVideoPlayer();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doPlay();
                ToastUtils.showShort("正在播放实时视频~");
            }
        }, 1000l);
    }

    private void initView() {
        textureView = findViewById(R.id.texture_view);
        progressBar = findViewById(R.id.progress_bar);
        playHintText = findViewById(R.id.result_hint_text);
        digitalScaleText = findViewById(R.id.digital_scale_text);
        autoHideView = findViewById(R.id.auto_hide_view);

//        start = findViewById(R.id.start);
//        stop = findViewById(R.id.stop);
//        start.setOnClickListener(this);
//        stop.setOnClickListener(this);
//        start.setVisibility(View.GONE);
//        stop.setVisibility(View.VISIBLE);

        textureView.setSurfaceTextureListener(this);
    }

    private void initPlayWindowContainer() {
        frameLayout = findViewById(R.id.frame_layout);
        frameLayout.setOnClickListener(new PlayWindowContainer.OnClickListener() {
            @Override
            public void onSingleClick() {
                if (autoHideView.isVisible()) {
                    autoHideView.hide();
                } else {
                    autoHideView.show();
                }
            }
        });
        frameLayout.setOnDigitalListener(new PlayWindowContainer.OnDigitalZoomListener() {
            @Override
            public void onDigitalZoomOpen() {
                executeDigitalZoom();
            }
        });
    }

    private void doPlay() {
        if (mPlayerStatus != PlayerStatus.SUCCESS) {
            startRealPlay(textureView.getSurfaceTexture());
//            start.setVisibility(View.GONE);
//            stop.setVisibility(View.VISIBLE);
        }
    }

    private void doStop() {
        if (mPlayerStatus == PlayerStatus.SUCCESS) {
            mPlayerStatus = PlayerStatus.IDLE;//释放这个窗口
            progressBar.setVisibility(View.GONE);
            playHintText.setVisibility(View.VISIBLE);
            playHintText.setText("");
            resetExecuteState();
            mPlayer.stopPlay();

//            start.setVisibility(View.VISIBLE);
//            stop.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
//        if (view.getId() == R.id.start) {
//            doPlay();
//        } else if (view.getId() == R.id.stop) {
//            doStop();
//        }
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        layoutViews();
    }

    /**
     * 屏幕方向变化后重新布局View
     */
    private void layoutViews() {
        ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
        if (ScreenUtils.isPortrait()) {
            //先显示系统状态栏
            showSystemUI();
            //再显示控制按钮区域
            layoutParams.height = SizeUtils.dp2px(250f);
            showOrHideControlArea(true);
        } else if (ScreenUtils.isLandscape()) {
            //隐藏系统UI
            hideSystemUI();
            showOrHideControlArea(false);
            layoutParams.height = ScreenUtils.getScreenHeight();
        }
    }

    /**
     * 显示或隐藏控制区域
     *
     * @param isShow true-显示，false-隐藏
     */
    private void showOrHideControlArea(boolean isShow) {
        int visibility = isShow ? View.VISIBLE : View.GONE;
//        start.setVisibility(visibility);
//        stop.setVisibility(visibility);
    }

    /**
     * 隐藏系统ui
     */
    private void hideSystemUI() {
        //隐藏ActionBar 如果使用了NoActionBar的Theme则不需要隐藏actionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //TODO：View.setSystemUiVisibility(int visibility)中，visibility是Mode与Layout任意取值的组合，可传入的实参为：

        // Mode属性
        //View.SYSTEM_UI_FLAG_LOW_PROFILE：状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏。
        //View.SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示，且状态栏被隐藏覆盖掉。等同于（WindowManager.LayoutParams.FLAG_FULLSCREEN）
        //View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
        //View.SYSTEM_UI_FLAG_IMMERSIVE：这个flag只有当设置了SYSTEM_UI_FLAG_HIDE_NAVIGATION才起作用。
        // 如果没有设置这个flag，任意的View相互动作都退出SYSTEM_UI_FLAG_HIDE_NAVIGATION模式。如果设置就不会退出。
        //View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY：这个flag只有当设置了SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_HIDE_NAVIGATION 时才起作用。
        // 如果没有设置这个flag，任意的View相互动作都坏退出SYSTEM_UI_FLAG_FULLSCREEN | SYSTEM_UI_FLAG_HIDE_NAVIGATION模式。如果设置就不受影响。

        // Layout属性
        //View.SYSTEM_UI_FLAG_LAYOUT_STABLE： 保持View Layout不变，隐藏状态栏或者导航栏后，View不会拉伸。
        //View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：让View全屏显示，Layout会被拉伸到StatusBar下面，不包含NavigationBar。
        //View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：让View全屏显示，Layout会被拉伸到StatusBar和NavigationBar下面。
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
        //解决在华为手机上横屏时，状态栏不消失的问题
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏

    }

    /**
     * 显示系统UI
     */
    private void showSystemUI() {
        //显示ActionBar 如果使用了NoActionBar的Theme则不需要显示actionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // This snippet shows the system bars. It does this by removing all the flags
            // except for the ones that make the content appear under the system bars.
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
        //解决在华为手机上横屏时，状态栏不消失的问题
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
    }

    /**
     * 执行电子放大操作
     */
    private void executeDigitalZoom() {
        if (mPlayerStatus != PlayerStatus.SUCCESS) {
            ToastUtils.showShort("没有视频在播放");
        }
        if (decimalFormat == null) {
            decimalFormat = new DecimalFormat("0.0");
        }
        if (!mDigitalZooming) {
            frameLayout.setOnScaleChangeListener(new PlayWindowContainer.OnDigitalScaleChangeListener() {
                @Override
                public void onDigitalScaleChange(float scale) {
                    Log.i(TAG, "onDigitalScaleChange scale = " + scale);
                    if (scale < 1.0f && mDigitalZooming) {
                        //如果已经开启了电子放大且倍率小于1就关闭电子放大
                        executeDigitalZoom();
                    }
                    if (scale >= 1.0f) {
                        digitalScaleText.setText(MessageFormat.format("{0}X", decimalFormat.format(scale)));
                    }
                }

                @Override
                public void onDigitalRectChange(CustomRect oRect, CustomRect curRect) {
                    mPlayer.openDigitalZoom(oRect, curRect);
                }
            });
            ToastUtils.showShort("电子放大开启");
            mDigitalZooming = true;
            digitalScaleText.setVisibility(View.VISIBLE);
            digitalScaleText.setText(MessageFormat.format("{0}X", decimalFormat.format(1.0f)));
        } else {
            ToastUtils.showShort("电子放大关闭");
            mDigitalZooming = false;
            digitalScaleText.setVisibility(View.GONE);
            frameLayout.setOnScaleChangeListener(null);
            mPlayer.closeDigitalZoom();
        }
    }

    /**
     * 重置所有的操作状态
     */
    private void resetExecuteState() {
        if (mDigitalZooming) {
            executeDigitalZoom();
        }
        frameLayout.setAllowOpenDigitalZoom(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //TODO 注意:APP前后台切换时 SurfaceTextureListener可能在有某些 华为手机 上不会回调，例如：华为P20，所以我们在这里手动调用
        if (textureView.isAvailable()) {
            Log.e(TAG, "onResume: onSurfaceTextureAvailable");
            onSurfaceTextureAvailable(textureView.getSurfaceTexture(), textureView.getWidth(), textureView.getHeight());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //TODO 注意:APP前后台切换时 SurfaceTextureListener可能在有某些 华为手机 上不会回调，例如：华为P20，所以我们在这里手动调用
        if (textureView.isAvailable()) {
            Log.e(TAG, "onPause: onSurfaceTextureDestroyed");
            onSurfaceTextureDestroyed(textureView.getSurfaceTexture());
        }
    }

    @Override
    protected void onDestroy() {
        doStop();
        super.onDestroy();
    }

    /**
     * 开始播放
     *
     * @param surface 渲染画面
     */
    private void startRealPlay(SurfaceTexture surface) {
        mPlayerStatus = PlayerStatus.LOADING;
        progressBar.setVisibility(View.VISIBLE);
        playHintText.setVisibility(View.GONE);
        mPlayer.setSurfaceTexture(surface);
        //TODO 注意: startRealPlay() 方法会阻塞当前线程，需要在子线程中执行,建议使用RxJava
        new Thread(() -> {
            //TODO 注意: 不要通过判断 startRealPlay() 方法返回 true 来确定播放成功，播放成功会通过HikVideoPlayerCallback回调，startRealPlay() 方法返回 false 即代表 播放失败;
            if (!mPlayer.startRealPlay(mUri, PreviewActivity.this)) {
                onPlayerStatus(Status.FAILED, mPlayer.getLastError());
            }
        }).start();
    }


    /**
     * 播放结果回调
     *
     * @param status    共四种状态：SUCCESS（播放成功）、FAILED（播放失败）、EXCEPTION（取流异常）、FINISH（回放结束）
     * @param errorCode 错误码，只有 FAILED 和 EXCEPTION 才有值
     */
    @Override
    @WorkerThread
    public void onPlayerStatus(@NonNull Status status, int errorCode) {
        //TODO 注意: 由于 HikVideoPlayerCallback 是在子线程中进行回调的，所以一定要切换到主线程处理UI
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                //只有播放成功时，才允许开启电子放大
                frameLayout.setAllowOpenDigitalZoom(status == Status.SUCCESS);
                switch (status) {
                    case SUCCESS:
                        //播放成功
                        mPlayerStatus = PlayerStatus.SUCCESS;
                        playHintText.setVisibility(View.GONE);
                        textureView.setKeepScreenOn(true);//保持亮屏
                        break;
                    case FAILED:
                        //播放失败
                        mPlayerStatus = PlayerStatus.FAILED;
                        playHintText.setVisibility(View.VISIBLE);
                        playHintText.setText(MessageFormat.format("预览失败，错误码：{0}", Integer.toHexString(errorCode)));
                        break;
                    case EXCEPTION:
                        //取流异常
                        mPlayerStatus = PlayerStatus.EXCEPTION;
                        mPlayer.stopPlay();//TODO 注意:异常时关闭取流
                        playHintText.setVisibility(View.VISIBLE);
                        playHintText.setText(MessageFormat.format("取流发生异常，错误码：{0}", Integer.toHexString(errorCode)));
                        break;
                }
            }
        });
    }

    /*************************TextureView.SurfaceTextureListener 接口的回调方法********************/
    //TODO 注意:APP前后台切换时 SurfaceTextureListener可能在有某些华为手机上不会回调，例如：华为P20，因此我们需要在Activity生命周期中手动调用回调方法
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        if (mPlayerStatus == PlayerStatus.STOPPING) {
            //恢复处于暂停播放状态的窗口
            startRealPlay(textureView.getSurfaceTexture());
            Log.d(TAG, "onSurfaceTextureAvailable: startRealPlay");
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mPlayerStatus == PlayerStatus.SUCCESS) {
            mPlayerStatus = PlayerStatus.STOPPING;//暂停播放，再次进入时恢复播放
            mPlayer.stopPlay();
            Log.d(TAG, "onSurfaceTextureDestroyed: stopPlay");
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

}
