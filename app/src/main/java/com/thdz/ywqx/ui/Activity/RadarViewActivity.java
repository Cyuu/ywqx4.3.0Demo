package com.thdz.ywqx.ui.Activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.thdz.ywqx.R;
import com.thdz.ywqx.base.BaseActivity;
import com.thdz.ywqx.bean.RadarTargetBean;
import com.thdz.ywqx.event.RadarBundle;
import com.thdz.ywqx.view.RadarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;


/**
 * @time : 2019/11/19
 * @author : cyun
 * @func :  雷达点绘制页面
 */
public class RadarViewActivity extends BaseActivity {

    // 自定义雷达图
    @BindView(R.id.radarview)
    RadarView radarview;

    @BindView(R.id.tv_position)
    TextView tv_position;

    @BindView(R.id.tv_data)
    TextView tv_data;

    int radius; // 雷达扫描半径
    float[] radars; // 雷达数据点
    RadarTargetBean[] targets; // 雷达告警目标点
    PointF[][] area1; // 背景区
    PointF[][] area2; // 预警区
    PointF[][] area3; // 告警区
    String label = "";
    boolean isAlarm;

    @Override
    public void cycleRequest(){
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            getRadarDataShow();
        }
    };

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_radar);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        hideInputMethod();

//        try {
//            Bundle bundle = getIntent().getExtras();
//            radius = bundle.getInt("radius", 0);
//            radars = bundle.getFloatArray("radar");
//            targets = (RadarTargetBean[]) bundle.getSerializable("target");
//            area1 = (PointF[][]) bundle.getSerializable("area1");
//            area2 = (PointF[][]) bundle.getSerializable("area2");
//            area3 = (PointF[][]) bundle.getSerializable("area3");
//            label = bundle.getString("label");
//            isAlarm = bundle.getBoolean("isAlarm");
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.w(TAG, "获取雷达数据出错");
//        }
//        showRadarView();

    }

    /**
     * 设置数据后重绘radarView
     */
    private void getRadarDataShow() {
        radarview.setData(radius, radars, targets, area1, area2, area3);
        radarview.postInvalidate();
    }

    private void showRadarView() {
        Message msg = mHandler.obtainMessage();
        mHandler.sendMessageDelayed(msg, 300);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
        }
    }


//    /**
//     * 显示ouTouch坐标
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void showScale(String str) {
//        tv_position.setText(str);
//    }


    /**
     * 显示ouTouch坐标
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void showRadarview(RadarBundle event) {
        if (event != null) {
            try {
                Bundle bundle = event.bundle;
                radius = bundle.getInt("radius", 2000);
                radars = bundle.getFloatArray("radar");
                targets = (RadarTargetBean[]) bundle.getSerializable("target");
                area1 = (PointF[][]) bundle.getSerializable("area1");
                area2 = (PointF[][]) bundle.getSerializable("area2");
                area3 = (PointF[][]) bundle.getSerializable("area3");
                label = bundle.getString("label");
                isAlarm = bundle.getBoolean("isAlarm");

                // 展示标题
                tv_data.setText(label);
                if (isAlarm) {
                    tv_data.setTextColor(Color.RED);
                } else {
                    tv_data.setTextColor(getResources().getColor(R.color.black_light));
                }

                // 绘制雷达数据
                showRadarView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
