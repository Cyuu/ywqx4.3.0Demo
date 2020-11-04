package com.thdz.ywqx.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.igexin.sdk.PushManager;
import com.igexin.sdk.PushService;
import com.thdz.ywqx.R;
import com.thdz.ywqx.app.MyApplication;
import com.thdz.ywqx.base.BaseActivity;
import com.thdz.ywqx.event.ClientIdEvent;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.Finals;
import com.thdz.ywqx.util.SpUtil;
import com.thdz.ywqx.util.StatusBarCompat;
import com.thdz.ywqx.util.VUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 登录页面<br/>
 * ip地址是否用自动输入完成框<br/>
 * 如果不是自动登录，则不填充控件<br/>
 * 这里默认自动登录，而只有退出登录，才取消自动登录<br/>
 * 登录--->请求app使用的状态码-->成功后goMain(),失败后，goMain()<br/>
 * 如果上次已经登录，尚未注销，那将不再登录，直接：--->请求app使用的状态码 <br/>
 * <br/>
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.ip_addr_tv)
    EditText ip_addr_tv;

    @BindView(R.id.username_tv)
    EditText username_tv;

    @BindView(R.id.password_tv)
    EditText password_tv;

    @BindView(R.id.login_btn)
    Button login_btn;

    private String username = "";
    private String password = "";
    private String ipStr = "";

    private String msgFail = "连接服务器失败";

    @Override
    public void cycleRequest() {
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        PushManager.getInstance().initialize(context.getApplicationContext(), PushService.class);
        login_btn.setOnClickListener(this);

        // 设置沉浸式状态栏
        int staBarColor = ContextCompat.getColor(this, R.color.color_login_top);
        int navBarColor = ContextCompat.getColor(this, R.color.color_login_top);
        StatusBarCompat.setStatusBarColor(this, staBarColor, navBarColor);

        // 获取到ClientId之前，将登陆按钮置灰，不可用
        if (TextUtils.isEmpty(SpUtil.getData(Finals.SP_CLIENTID))) {
            login_btn.setBackgroundResource(R.color.gray);
            login_btn.setEnabled(false);
        }
        // 测试打开TestActivity
//        if (BuildConfig.DEBUG) {
//            findViewById(R.id.login_logo_iv).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent it = new Intent(context, TestActivity.class);
//                    startActivity(it);
//                }
//            });
//        }

        // 局域网测试，没有外网，获取不到ClientId
//        if (BuildConfig.DEBUG) {
//            login_btn.setBackgroundResource(R.drawable.bg_green_selector);
//            login_btn.setEnabled(true);
//
//            findViewById(R.id.view_com).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ip_addr_tv.setText(Finals.HOST_4);
//                    username_tv.setText("zzx");
//                    password_tv.setText("123");
//                }
//            });
//        }

        showLoginEditView();

    }


    @Override
    public void onClick(View v) {
        if (VUtils.isFastDoubleClick()) {
            return;
        } else {
            switch (v.getId()) {
                case R.id.login_btn:
                    // doLogin();
                    doLogin4RSA();
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 响应 获取个推ClientId的 请求
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onGetClientIdEvent(ClientIdEvent event) {
        Log.i(TAG, "已获取到ClientId: " + event.getClientId());
        // toast("获取到ClientId："+ event.getClientId());
        // 如果本地未保存，则调整登陆按钮颜色，并保存（第一次登录使用），否则，不做任何处理(以后使用)
        if (!TextUtils.isEmpty(SpUtil.getData(Finals.SP_CLIENTID))) {
            // 将登陆框置为绿色，并可用
            login_btn.setBackgroundResource(R.drawable.bg_green_selector);
            login_btn.setEnabled(true);
            SpUtil.save(Finals.SP_CLIENTID, event.getClientId());
        }

    }

    /**
     * 获取用户控制权限
     */
    private void RequestUserInfo() {
//        MyApplication.controlFlag = false;
//        MyApplication.alarmHandleFlag = false;

        SpUtil.save(Finals.SP_CONTROL_FLAG, false);
        SpUtil.save(Finals.SP_ALARM_HANDLE_FLAG, false);

        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetUserInfo, "");
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                e.printStackTrace();
                hideProgressDialog();
                gotoMain();
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    String value = DataUtils.getRespString(response);
                    Log.i(TAG, "Return userinfo = " + value);
                    String dataStr = DataUtils.getReturnData(value);

                    JSONObject jsonObj = new JSONObject(dataStr);
                    String controlFlag = jsonObj.getString("ClientControl");
                    String alarmHandleFlag = jsonObj.getString("ClientAlarmHandle");

                    if ("true".equalsIgnoreCase(controlFlag)) {
//                        MyApplication.controlFlag = true;
                        SpUtil.save(Finals.SP_CONTROL_FLAG, true);
                    } else {
//                        MyApplication.controlFlag = false;
                        SpUtil.save(Finals.SP_CONTROL_FLAG, false);
                    }

                    if ("true".equalsIgnoreCase(alarmHandleFlag)) {
//                        MyApplication.alarmHandleFlag = true;
                        SpUtil.save(Finals.SP_ALARM_HANDLE_FLAG, true);
                    } else {
//                        MyApplication.alarmHandleFlag = false;
                        SpUtil.save(Finals.SP_ALARM_HANDLE_FLAG, false);
                    }
                    Log.i(TAG, "controlFlag = " + controlFlag + " alarmHandleFlag = " + alarmHandleFlag);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                hideProgressDialog();
                gotoMain();
            }
        });
    }

    // openssl加密登录，
    private void doLogin4RSA() {
        if (!doCheck()) {// 输入验证
            return;
        }
        showProgressDialog();
        String url = DataUtils.createReqUrl4Get4Login(ipStr, "", Finals.CMD_UserLogin, createParams(),username, password);
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                hideProgressDialog();
                toast(msgFail);
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                hideProgressDialog();
                String value = DataUtils.getRespString(response);
                Log.i(TAG, "解析出json，返回参数是：" + value);
                String dataStr = "";
                try {
                    dataStr = DataUtils.getReturnData(value);
                    if (DataUtils.isReturnOK(dataStr)) {
                        String uid = DataUtils.getReturnUserId(dataStr);
                        application.setUid(uid);
                        SpUtil.save(Finals.SP_UID, uid);
                        saveLoginInfo();

                        RequestMonitors(); // 获取监控点列表

                        RequestUserInfo();

                    } else {
                        toast(DataUtils.getReturnMsg(dataStr));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (!TextUtils.isEmpty(dataStr)) {
                        try {
                            org.json.JSONObject failObj = new org.json.JSONObject(dataStr);
                            String msgStr = failObj.getString("sMsg");
                            toast(msgStr);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            toast(msgFail);
                        }
                    } else {
                        toast(msgFail);
                    }
                }
            }
        });
    }

    /**
     * 登录请求
     */
    private void doLogin() {
        if (!doCheck()) {// 输入验证
            return;
        }
        showProgressDialog();
        String url = DataUtils.createReqUrl4Get(ipStr, "", Finals.CMD_UserLogin, createParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                hideProgressDialog();
                toast(msgFail);
                e.printStackTrace();

            }

            @Override
            public void onResponse(String response, int id) {
                hideProgressDialog();
                String value = DataUtils.getRespString(response);
                Log.i(TAG, "解析出json，返回参数是：" + value);
                String dataStr = "";
                try {
                    dataStr = DataUtils.getReturnData(value);
                    if (DataUtils.isReturnOK(dataStr)) {
                        String uid = DataUtils.getReturnUserId(dataStr);
                        application.setUid(uid);
                        SpUtil.save(Finals.SP_UID, uid);
                        saveLoginInfo();

                        RequestMonitors(); // 获取监控点列表

                        RequestUserInfo();

                    } else {
                        toast(DataUtils.getReturnMsg(dataStr));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (!TextUtils.isEmpty(dataStr)) {
                        try {
                            org.json.JSONObject failObj = new org.json.JSONObject(dataStr);
                            String msgStr = failObj.getString("sMsg");
                            toast(msgStr);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            toast(msgFail);
                        }
                    } else {
                        toast(msgFail);
                    }
                }
            }
        });
    }

    private void RequestMonitors() {
        MyApplication.isRequestingMonitors = true;
        String url = DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                Finals.CMD_GetStnList, createParams4Monitors());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                MyApplication.isRequestingMonitors = false;
                e.printStackTrace();
            }

            @Override
            public void onResponse(String response, int id) {
                MyApplication.isRequestingMonitors = false;
                String value = DataUtils.getRespString(response);
                MyApplication.saveMobitros2DB(value);
            }
        });
    }

    private String createUserCheckParams() {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("UserName", SpUtil.getData(Finals.SP_USERNAME));
            jsonObj.put("UserPwd", SpUtil.getData(Finals.SP_PWD));
            jsonObj.put("ClientId", SpUtil.getData(Finals.SP_CLIENTID));
            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String createParams4Monitors() {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("nUserId", application.getUid());
            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            toast(errorTip);
        }
        return "";
    }

    private String createParams() {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("UserName", username);
            jsonObj.put("UserPwd", password);
            jsonObj.put("ClientId", SpUtil.getData(Finals.SP_CLIENTID));
            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 设置检测规则，检查输入是否正确, true 输入正确 // false 输入错误
     */
    private boolean doCheck() {
        ipStr = ip_addr_tv.getText().toString().trim();
        username = username_tv.getText().toString().trim();
        password = password_tv.getText().toString().trim();

        if (TextUtils.isEmpty(ipStr)) {
            toast("请输入合法的ip地址");
            return false;
        }

        if (TextUtils.isEmpty(username)) {
            toast("请输入用户名");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            toast("请输入密码");
            return false;
        }
        return true;
    }


    /**
     * 跳转至首页
     */
    private void gotoMain() {
        MyApplication.isShowAlarmDialog = false;
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void doAutoLogin() {
        RequestMonitors(); // 如果已经登录， 获取监控点列表

        username_tv.setText(SpUtil.getData(Finals.SP_USERNAME));
        password_tv.setText(SpUtil.getData(Finals.SP_PWD));
        ip_addr_tv.setText(SpUtil.getData(Finals.SP_IP));

        ip_addr_tv.setSelection(ip_addr_tv.getText().toString().length());
        username_tv.setSelection(username_tv.getText().toString().length());
        password_tv.setSelection(password_tv.getText().toString().length());

        application.setIP(SpUtil.getData(Finals.SP_IP));
        application.setUid(SpUtil.getUid());

        RequestUserInfo();// 如果已经登录，则直接请求
    }

    private void requestLogout() {
        String url = DataUtils.createReqUrl4Get(
                SpUtil.getData(Finals.SP_IP),
                SpUtil.getUid(),
                Finals.CMD_AppLogout,
                createLogoutParams());

        SpUtil.save(Finals.SP_AUTOLOGIN, "0");
        SpUtil.save(Finals.SP_PWD, "");
        SpUtil.save(Finals.SP_UID, "");
        doRequestGet(url, null);
    }

    private String createLogoutParams() {
        try {
            org.json.JSONObject jsonObj = new org.json.JSONObject();
            jsonObj.put("UserName", "0");
            jsonObj.put("UserPwd", "0");
            jsonObj.put("ClientId", SpUtil.getData(Finals.SP_CLIENTID));
            return jsonObj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void checkAutoLogin() {
        String url = DataUtils.createReqUrl4Get(SpUtil.getData(Finals.SP_IP), "", Finals.CMD_UserCheck, createUserCheckParams());
        doRequestGet(url, new StringCallback() {
            @Override
            public void onError(Call call, final Exception e, int id) {
                doAutoLogin();
            }

            @Override
            public void onResponse(String response, int id) {
                String value = DataUtils.getRespString(response);
                Log.i(TAG, "解析出json，返回参数是：" + value);
                String dataStr = "";
                try {
                    dataStr = DataUtils.getReturnData(value);
                    if (DataUtils.isReturnOK(dataStr)) {
                        doAutoLogin();
                    } else {
                        requestLogout();
                        forceShowLoginView();
                    }
                } catch (Exception e) {
                    requestLogout();
                    forceShowLoginView();
                }
            }
        });
    }

    private void forceShowLoginView() {
        hideProgressDialog();
        ip_addr_tv.setText(SpUtil.getData(Finals.SP_IP));
        username_tv.setText(SpUtil.getData(Finals.SP_USERNAME));

        ip_addr_tv.setSelection(ip_addr_tv.getText().toString().length());
        username_tv.setSelection(username_tv.getText().toString().length());
    }


    /**
     * 判断是否是退出登录而来,这里默认自动登录，而只有退出登录，才取消自动登录<br/>
     * 根据sp里的值，填充到控件中，如果没有则，不动<br/>
     */
    private void showLoginEditView() {
        showProgressDialog();
        if (SpUtil.isAutoLogin()) { // 自动登录
            checkAutoLogin();
        } else { // 正常登录或者退出登录
            forceShowLoginView();
        }

    }


    /**
     * 登录成功后保存登录信息
     */
    private void saveLoginInfo() {
        SpUtil.save(Finals.SP_IP, ipStr);//登录ip
        SpUtil.save(Finals.SP_USERNAME, username);
        SpUtil.save(Finals.SP_PWD, password);
        SpUtil.save(Finals.SP_AUTOLOGIN, "1");//自动登录

        application.setIP(ipStr);
    }

}
