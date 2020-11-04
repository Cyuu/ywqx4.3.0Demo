package com.thdz.ywqx.ui.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thdz.ywqx.R;
import com.thdz.ywqx.base.BaseActivity;
import com.thdz.ywqx.base.BaseFragment;
import com.thdz.ywqx.ui.fragment.AlarmFragment;
import com.thdz.ywqx.ui.fragment.MineFragment;
import com.thdz.ywqx.ui.fragment.MonitorFragment;
import com.thdz.ywqx.util.VUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页<br/>
 * addOnBackStackChangeListener() popBackStack() addToBackStack(),
 */
public class MainActivity extends BaseActivity {

    private long exitTime = 0;

    @BindView(R.id.main_group)
    LinearLayout main_group;

    @BindView(R.id.container)
    FrameLayout container;

    // 点击布局
    @BindView(R.id.alarm_layout)
    LinearLayout alarm_layout;
    @BindView(R.id.monitor_layout)
    LinearLayout monitor_layout;
    //    @BindView(R.id.story_layout)
//    LinearLayout story_layout;
    @BindView(R.id.mine_layout)
    LinearLayout mine_layout;

    @BindView(R.id.alarm_img)
    ImageView alarm_img;
    @BindView(R.id.monitor_img)
    ImageView monitor_img;
    @BindView(R.id.story_img)
    ImageView story_img;
    @BindView(R.id.mine_img)
    ImageView mine_img;

    @BindView(R.id.alarm_tv)
    TextView alarm_tv;
    @BindView(R.id.monitor_tv)
    TextView monitor_tv;
    @BindView(R.id.story_tv)
    TextView story_tv;
    @BindView(R.id.mine_tv)
    TextView mine_tv;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private AlarmFragment alarmFragment;
    private MonitorFragment monitorFragment;
    //    private HistoryFragment storyFragment;
    private MineFragment mineFragment;

    private BaseFragment currentFragment;

    private int currentIndex = -1;
    private int pushIndex = -1; // 根据这个指令，判断推送来的index，打开哪个tab

    private int color_blue;
    private int color_black;

    @Override
    public void cycleRequest(){
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);

        alarm_layout.setOnClickListener(this);
        monitor_layout.setOnClickListener(this);
//        story_layout.setOnClickListener(this);
        mine_layout.setOnClickListener(this);

        color_blue = getResources().getColor(R.color.blue_color);
        color_black = getResources().getColor(R.color.black_light_color);

        fragmentManager = getSupportFragmentManager();
//        int temp = pushIndex;
        pushIndex = getIntent().getIntExtra("index", 0);// 推送
        setTabSelection(pushIndex);

//        // todo TEST 打开通知--获取参数：不是singleTop不需要获取额外的参数，会重建的。
//        String param = getIntent().getStringExtra("param");
//        if (!TextUtils.isEmpty(param)) {
//            toast("param = "+  param);
//
//            if (param.equals("newalarm")) {
//
//                pushIndex = 0;
//                setTabSelection(pushIndex);
//                if (alarmFragment != null && temp != -1) {
//                    temp = pushIndex;
//                    alarmFragment.refreshAlarm(); // 满足预期
//                }
//            }
//        }

    }

    /**
     * 清除掉所有的选中状态。
     */
    private void resetBottomBtn() {
        alarm_img.setImageResource(R.drawable.ic_alarm_off);
        monitor_img.setImageResource(R.drawable.ic_monitor_off);
//        story_img.setImageResource(R.drawable.ic_radar);
        mine_img.setImageResource(R.drawable.ic_mine_off);

        alarm_tv.setTextColor(color_black);
        monitor_tv.setTextColor(color_black);
//        story_tv.setTextColor(color_black);
        mine_tv.setTextColor(color_black);

    }


    /**
     * 将所有的Fragment都置为隐藏状态。
     */
    private void hideFragments() {
        if (transaction == null) {
            transaction = fragmentManager.beginTransaction();
        }
        if (alarmFragment != null) {
            transaction.hide(alarmFragment);
        }
        if (monitorFragment != null) {
            transaction.hide(monitorFragment);
        }
//        if (storyFragment != null) {
//            transaction.hide(storyFragment);
//        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }

    }

    public void setTabSelection(int index) {
        boolean fromRight2Left = true;
        if (index < currentIndex) {
            fromRight2Left = false;
        }
        if (currentIndex == index) {
            return;
        }
        // 重置按钮
        resetBottomBtn();
        transaction = fragmentManager.beginTransaction();// 开启一个Fragment事务
        if (fromRight2Left) {
            transaction.setCustomAnimations(R.anim.push_right_in, R.anim.push_left_out);
        } else {
            transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out);
        }
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments();

        switch (index) {
            case 0:
                alarm_img.setImageResource(R.drawable.ic_alarm_on);
                int color_red = getResources().getColor(R.color.red_color);
                alarm_tv.setTextColor(color_red);
                if (alarmFragment == null) {
                    alarmFragment = new AlarmFragment();
                    transaction.add(R.id.container, alarmFragment);
                } else {
                    transaction.show(alarmFragment);
                }
                currentFragment = alarmFragment;
                currentIndex = 0;
                break;
            case 1:
                monitor_img.setImageResource(R.drawable.ic_monitor_on);
                monitor_tv.setTextColor(color_blue);
                if (monitorFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    monitorFragment = new MonitorFragment();
                    transaction.add(R.id.container, monitorFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(monitorFragment);
                }
                currentFragment = monitorFragment;
                currentIndex = 1;
                break;
            case 2:
//                story_img.setImageResource(R.drawable.ic_radar_on);
//                story_tv.setTextColor(color_blue);
//                if (storyFragment == null) {
//                    storyFragment = new HistoryFragment();
//                    transaction.add(R.id.container, storyFragment);
//                } else {
//                    transaction.show(storyFragment);
//                }
//                currentFragment = storyFragment;
//                currentIndex = 2;
                break;
            case 3:
                mine_img.setImageResource(R.drawable.ic_mine_on);
                mine_tv.setTextColor(color_blue);
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.container, mineFragment);
                } else {
                    transaction.show(mineFragment);
                }
                currentFragment = mineFragment;
                currentIndex = 3;
                break;
        }
        transaction.commit();
    }

    /**
     * 获取当前的Fragment
     */
    public BaseFragment getCurrentFragment() {
        return currentFragment;
    }

    @Subscribe
    @OnClick({R.id.alarm_layout, R.id.monitor_layout, R.id.story_layout, R.id.mine_layout})
    @Override
    public void onClick(View v) {
        if (VUtils.isFastDoubleClick()) {
            return;
        } else {
            switch (v.getId()) {
                case R.id.alarm_layout:
                    setTabSelection(0);
                    break;
                case R.id.monitor_layout:
                    setTabSelection(1);
                    break;
//                case R.id.story_layout:
//                    setTabSelection(2);
//                    break;
                case R.id.mine_layout:
                    setTabSelection(3);
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 再按一次退出
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (currentIndex == 1 && monitorFragment != null && monitorFragment.isStationLevelShow()) {
                monitorFragment.doPrevious();
            } else {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    toast("再按一次退出程序");
                    exitTime = System.currentTimeMillis();
                } else {
                    application.exit();
                    finish();
                }
            }
            return true;
        }

        // // 最小化程序
        // if (keyCode == KeyEvent.KEYCODE_BACK) {
        // Intent intent = new Intent(Intent.ACTION_MAIN);
        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // intent.addCategory(Intent.CATEGORY_HOME);
        // startActivity(intent);
        // return true;
        // }

        return super.onKeyDown(keyCode, event);
    }

}
