package com.thdz.ywqx.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.thdz.ywqx.util.TsUtil;
import com.thdz.ywqx.view.ProgressWheel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

/**
 * Fragment框架<br/>
 * EventBus.getDefault().register(this);
 * EventBus.getDefault().unregister(this);
 */
public abstract class BaseFragment extends Fragment implements OnClickListener {

//    public final int CODE_RELOAD = 0; // 重新加载
//    public final int CODE_LOAD_NEW = 1; // 加载新内容
//    public final int CODE_LOAD_MORE = 2;// 加载更多

    public final String failTip = "连接服务器失败";
    public final String errorTip = "未获取到信息";

    public String TAG = this.getClass().getSimpleName();

    public MyApplication application;

    public Context context;
    public ProgressDialog progressDialog = null;

    public RelativeLayout common_loading; // 整个加载View，RelativeLayout
    public ProgressWheel common_bar; // progressbar
    public TextView common_null_tv; // 图文提示 common_null_tv

    /**
     * 需要输入密码校验的对话框，里面的密码输入框
     */
    public EditText pEdit;

    /**
     * fragment的父view
     */
    public View contentView;

    public InputMethodManager imm;
//    /**
//     * cancelAll使用tag，需要设置的话， 要在
//     */
//    public String request_tag;

//    public Unbinder unbinder;

//    public final int TIME_OUT_MS = 10000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = this.getActivity();
        application = (MyApplication) getActivity().getApplication();
        if (null == contentView) {
            contentView = inflateView(inflater, container, savedInstanceState);
        }
//        unbinder = ButterKnife.bind(this, contentView);
        ButterKnife.bind(this, contentView);
//        MyApplication.getRefWatcher(context).watch(this);
        initLoadingView(contentView);
        // imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (common_null_tv != null) {
            common_null_tv.setOnClickListener(this);
        }
        initView(savedInstanceState, contentView, inflater);
        if (timerEnable) {
            openTimer();
        }
        return contentView;
    }

    /**
     * 获取root view的layout id
     */
    public abstract View inflateView(LayoutInflater inflater, ViewGroup arg1, Bundle arg2);

    /**
     * 初始化Fragment
     */
    public abstract void initView(Bundle savedInstanceState, View view,
                                  LayoutInflater inflater);

    /**
     * 点击事件响应
     */
    @Override
    public abstract void onClick(View v);

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
        RequestControlCMD(stnNo, unitNo, code, true);
    }


    /**
     * 发送控制命令的请求，没有Toast<br/>
     */
    public void RequestControlCMD(String stnNo, String unitNo, int code, boolean needToast) {
        String dataStr = DataUtils.CreateCommandParams(context, stnNo, unitNo, code);
        String url = DataUtils.createReqUrl4Get(
                application.getIP(), application.getUid(), Finals.CMD_SystemControlCmd, dataStr);
        doRequestGet(url, null);
        if (needToast) {
//            toast("命令已发送"); // 已由EventBus分发到各页面处理
        }
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_right_in,
                R.anim.push_left_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.push_right_in,
                R.anim.push_left_out);
    }

//    /**
//     * 获取imei
//     */
//    public void getIMEI() {
//        if (TextUtils.isEmpty(Finals.imei)) {
//            Finals.imei = ((TelephonyManager) context
//                    .getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//        }
//    }

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
    private void initLoadingView(View view) {
        common_loading = (RelativeLayout) view.findViewById(R.id.common_loading);
        common_bar = (ProgressWheel) view.findViewById(R.id.common_bar);
        common_null_tv = (TextView) view.findViewById(R.id.common_null_tv);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        hideInputMethod();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        stopTask();

    }

    /**
     * 隐藏输入框
     */
    public void hideInputMethod() {
        if (imm != null) {
            if (contentView != null) {
                IBinder mBinder = contentView.getWindowToken();
                if (mBinder != null) {
                    imm.hideSoftInputFromWindow(mBinder,
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

//	/**
//	 * 短显示Snackbar，自定义颜色
//	 */
//	public void ShowSnack(View view, String message, int messageColor, int backgroundColor) {
//		Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
//		View v = snackbar.getView();
//		if (v != null) {
//			v.setBackgroundColor(backgroundColor);
//			((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(messageColor);
//		}
//		snackbar.show();
//	}
//
//	// 默认颜色的Snack
//	public void ShowSnack(View view, String message) {
//		int messageColor = getResources().getColor(R.color.white);
//		int bgColor = getResources().getColor(R.color.blue_color);
//		ShowSnack(view, message, messageColor, bgColor);
//	}

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

        progressDialog.show();
    }

    public void showProgressDialog(String txt, DialogInterface.OnClickListener sureListener) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(true);
            progressDialog.setProgressDrawable(getResources().getDrawable(R.drawable.ic_checked));
            progressDialog.setMessage(txt);

            progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", sureListener);
            progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    hideProgressDialog();
                }
            });
        }

        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null) {
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
        // mBuilder.setTitle("请输入密码进行校验");
        mBuilder.setPositiveButton("确认", sureListener);

        mBuilder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
//                            if (imm != null) {
//                                 imm.hideSoftInputFromWindow(pEdit.getWindowToken(), 0);// 关闭输入法
//                            }
                            hideInputMethod();// 关闭输入法
                            toast("已取消");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
        AlertDialog pwdDialog = mBuilder.create();
        pwdDialog.show();

    }

    /**
     * 设置title for Fragment
     */
    public void setTitle(View view, String title) {
        TextView titletv = (TextView) view.findViewById(R.id.title_tv);
        titletv.setText(title);
    }


    /**
     * 隐藏返回箭头title for Fragment
     */
    public void setBackGone(View view) {
        ImageView back = (ImageView) view.findViewById(R.id.left_img);
        back.setVisibility(View.GONE);
    }


    /**
     * 隐藏返回箭头title for Fragment
     */
    public void setRightTopGone(View view) {
        ImageView back = (ImageView) view.findViewById(R.id.right_img);
        back.setVisibility(View.INVISIBLE);
    }

    public void toast(String info) {
        TsUtil.showToast(context, info);
    }

    /**
     * 根据code，判断textView展示的文本
     *
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
