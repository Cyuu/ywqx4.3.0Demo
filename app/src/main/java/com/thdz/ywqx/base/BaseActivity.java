package com.thdz.ywqx.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thdz.ywqx.R;
import com.thdz.ywqx.app.MyApplication;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.Finals;
import com.thdz.ywqx.util.SpUtil;
import com.thdz.ywqx.util.StatusBarCompat;
import com.thdz.ywqx.util.TsUtil;
import com.thdz.ywqx.view.ProgressWheel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

/**
 * Activity框架<br/>
 * (请求接口步骤)<br/>
 * 1 initQueue() <br/>
 * 2 拼接参数 先调用buildCommonHttpParams()，然后构建自己的私有参数buildParams4XX()，返回map <br/>
 * 3 发送请求 volleyPostRequest() <br/>
 * ------------------------- <br/>
 * 公共的loading 需要在每个页面的initView方法里，加上common_null_layout.setOnClickListener(this);
 * 然后再pressEvent()里，添加该控件的点击事件
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener {

    public final String failTip = "连接服务器失败";
    public final String errorTip = "无相关数据";

    public MyApplication application;

    /**
     * 需要输入密码校验的对话框，里面的密码输入框
     */
    public EditText pEdit;

    protected Context context;
    public ProgressDialog progressDialog = null;
    /**
     * 通过 android:theme 属性应用这个样式到你的 ProgressBar 。
     * <style name="CircularProgress" parent="Theme.AppCompat.Light">
     * <item name="colorAccent">@color/indigo</item>
     * </style>
     * android:theme="@style/CircularProgress"
     */
    public String TAG = this.getClass().getSimpleName();

    public RelativeLayout common_loading; // 整个加载View，RelativeLayout
    public ProgressWheel common_bar; // progressbar
    public TextView common_null_tv; // 图文提示 common_null_tv

    public InputMethodManager imm = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        application = (MyApplication) getApplication();// 获取应用程序全局的实例引用
//        MyApplication application = (MyApplication) MyApplication.getInstance();
//        application.activityList.add(this); // 把当前Activity放入集合中
        application.addActivity(this);

        if (application.screenWidth == 0) {
            application.screenWidth = getScreenWidth();
        }

        if (application.screenWidth == 0) {
            application.screenheigth = getScreenHeight();
        }

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView();
        ButterKnife.bind(this);
        initLoadingView();

        if (common_null_tv != null) {
            common_null_tv.setOnClickListener(this);
        }

        // 设置沉浸式状态栏
        int staBarColor = ContextCompat.getColor(this, R.color.colorPrimary);
        int navBarColor = ContextCompat.getColor(this, R.color.colorPrimary);
        StatusBarCompat.setStatusBarColor(this, staBarColor, navBarColor);

        initView(savedInstanceState);

        if (timerEnable) {
            openTimer();
        }

    }


    /**
     * 获得屏幕宽度
     */
    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获得屏幕高度
     */
    public int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * setContentView
     */
    public abstract void setContentView();


    /**
     * get方式
     *
     * @param url      url
     * @param callback 回调方法
     */
    public void doRequestGet(String url, StringCallback callback) {
        Log.i(TAG, "请求地址：" + url);
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(callback);
    }

    /**
     * post方式
     *
     * @param url      url
     * @param params   post参数
     * @param callback 回调方法
     */
    public void doRequestPost(String url, String params, StringCallback callback) {
        Log.i(TAG, "请求地址：" + url);
        OkHttpUtils
                .get()
                .url(url)
                .addParams("sCmd", params)
                .build()
                .execute(callback);
    }


    /**
     * 发送控制命令的请求<br/>
     * 1 code， no的确认<br/>
     * 2 没有StringCallback
     */
    public void RequestControlCMD(String stnNo, String unitNo, int code) {
        String dataStr = DataUtils.CreateCommandParams(context, stnNo, unitNo, code);
        String url = DataUtils.createReqUrl4Get(
                application.getIP(), application.getUid(), Finals.CMD_SystemControlCmd, dataStr);
        doRequestGet(url, null);
//        toast("命令已发送"); // 已由EventBus分发到各页面处理
    }


    public void showProgressDialog() {
        showProgressDialog("正在加载...");
    }

    public void showProgressDialog(String txt) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(true);
            progressDialog.setMessage(txt);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }

    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**
     * 打开确认对话框，
     */
    public void showSureDialog(String tip, DialogInterface.OnClickListener sureListener) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View mView = layoutInflater.inflate(R.layout.dialog_sure, null);
        TextView dialog_sure_tv = (TextView) mView.findViewById(R.id.dialog_sure_tv);
        dialog_sure_tv.setText(tip);
        mBuilder.setView(mView);
        mBuilder.setCancelable(false);
        mBuilder.setPositiveButton("确认", sureListener);

        mBuilder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog sureDialog = mBuilder.create();
        sureDialog.show();
    }

    /**
     * init view after setcontentView()
     */
    public abstract void initView(Bundle savedInstanceState);

    // @Subscribe
    @Override
    public abstract void onClick(View v);

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
    }

    @Override
    public void finish() {
        // 之前是在这里做的清理Activity的操作，现转到ondestroy里
        super.finish();
        overridePendingTransition(0, R.anim.push_right_out);
    }

    /**
     * 初始化加载时
     */
    public void loadInit() {
        common_loading.setVisibility(View.VISIBLE);
        common_bar.setVisibility(View.VISIBLE);
        common_null_tv.setVisibility(View.GONE);
    }

    /**
     * 加载成功
     */
    public void loadOK() {
        common_loading.setVisibility(View.GONE);
    }

    /**
     * 加载失败
     */
    public void loadFail() {
        common_loading.setVisibility(View.VISIBLE);
        common_bar.setVisibility(View.GONE);
        common_null_tv.setVisibility(View.VISIBLE);
    }

    /**
     * 实例化布局
     */
    private void initLoadingView() {
        common_loading = (RelativeLayout) findViewById(R.id.common_loading);
        common_bar = (ProgressWheel) findViewById(R.id.common_bar);
        common_null_tv = (TextView) findViewById(R.id.common_null_tv);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideInputMethod();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        stopTask();

//        application.activityList.remove(this); // 把当前Activity从集合中移除
        application.removeActivity(this);

    }


    /**
     * 隐藏输入框
     */
    public void hideInputMethod() {
        if (imm != null) {
            View view = ((Activity) context).getCurrentFocus();
            if (view != null) {
                IBinder mBinder = view.getWindowToken();
                if (mBinder != null) {
                    imm.hideSoftInputFromWindow(mBinder, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (progressDialog != null && progressDialog.isShowing()) {
            hideProgressDialog();
            return;
        }
        super.onBackPressed();
    }


    /**
     * 打开输入密码验证对话框
     */
    public void showPwdDialog(DialogInterface.OnClickListener sureListener) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View pwdView = layoutInflater.inflate(R.layout.dialog_pwd, null);
        TextView title_tv = (TextView) pwdView.findViewById(R.id.dialog_title_tv); // title
        title_tv.setText("请输入密码进行校验");
        pEdit = (EditText) pwdView.findViewById(R.id.pwd_check_ed);
        if (Finals.IS_TEST) {
            pEdit.setText(SpUtil.getData(Finals.SP_PWD));
            pEdit.setSelection(pEdit.getText().toString().length());
        }
        mBuilder.setView(pwdView);
        mBuilder.setCancelable(false);

        mBuilder.setPositiveButton("确认", sureListener);
        mBuilder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (imm != null) {
                                imm.hideSoftInputFromWindow(pEdit.getWindowToken(), 0);// 关闭输入法
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
        AlertDialog pwdDialog = mBuilder.create();
        pwdDialog.show();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // 打开输入法：
                if (pEdit != null) {
                    //设置可获得焦点
                    pEdit.setFocusable(true);
                    pEdit.setFocusableInTouchMode(true);
                    //请求获得焦点
                    pEdit.requestFocus();
                    //调用系统输入法
                    InputMethodManager inputManager = (InputMethodManager) pEdit
                            .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(pEdit, 0);
                }
            }
        }, 200);

    }


    /**
     * 设置title for Activity
     */
    public void setTitle(String title) {
        TextView titletv = (TextView) findViewById(R.id.title_tv);
        titletv.setText(title);
    }

    /**
     * 隐藏返回箭头 for Activity
     */
    public void setBackGone() {
        ImageView back = (ImageView) findViewById(R.id.left_img);
        back.setVisibility(View.GONE);
    }


    /**
     * 设置返回箭头动作
     */
    public void setBackActive() {
        ImageView back = (ImageView) findViewById(R.id.left_img);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 隐藏title栏右侧按钮 for Activity
     */
    public void setRightTopGone() {
        ImageView back = (ImageView) findViewById(R.id.right_img);
        back.setVisibility(View.INVISIBLE);
    }

    public void goActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(context, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void goActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(context, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void toast(String info) {
        TsUtil.showToast(context, info);
    }


    /**
     * 根据code，判断textView展示的文本
     * @param tv        textView
     * @param codeValue 目标code值
     * @param codeOK    正确code
     * @param codeFalse 错误code
     */
    public void showTvValue(TextView tv, String codeValue, int codeOK, int codeFalse) {
        if (tv != null) {
            String value = "";
            if (codeValue.equals(codeOK + "")) {
                value = "正常";
            } else if (codeValue.equals(codeFalse + "")) {
                value = "异常";
            }
            tv.setText(value);
        }
    }


    /**
     * 根据code，判断textview的字体颜色
     *
     * @param tv        textview
     * @param codeValue 目标code
     * @param codeOK    正常的code
     */
    public void showTvColor(TextView tv, String codeValue, int codeOK) {
        String value = tv.getText().toString().trim();
        if (TextUtils.isEmpty(value)) {
            return;
        }

        if (codeValue.equals(codeOK + "")) {
            tv.setTextColor(getResources().getColor(R.color.green_deep_color));
        } else {
            tv.setTextColor(getResources().getColor(R.color.red_color));
        }
    }


    /////////////////////////////////////

    public Timer timer = null;  // 定时器
    public TimerTask reqTask = null;
//    public boolean isRuning = true;  // 主动发送信息轮询检测标识， true,继续轮询； false, 停止轮询


    public void openTimer() {
        if (timer == null) {
            timer = new Timer();
        }

        if (reqTask == null) {
            reqTask = new TimerTask() {
                @Override
                public void run() {
//                    if (isRuning)
                    cycleRequest();
                }
            };
        }
        if (timer != null && reqTask != null) {
            timer.schedule(reqTask, 100, Finals.CONST_INTERVAL);
        }
    }

    public abstract void cycleRequest();

    public void stopTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (reqTask != null) {
            reqTask.cancel();
            reqTask = null;
        }
    }


    /**
     * 是否交由心跳发送, true 默认不发送
     */
    private boolean timerEnable = false;

    /**
     * 请求数据刷新页面， FIXME 请在initView()里调用
     *
     * @param flag 是否交由心跳发送, true 默认不发送
     */
    public void setTimerEnable(boolean flag) {
        this.timerEnable = flag;
    }

}
