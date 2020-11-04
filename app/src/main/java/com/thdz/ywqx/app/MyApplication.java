package com.thdz.ywqx.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.hikvision.open.hikvideoplayer.HikVideoPlayerFactory;
import com.thdz.ywqx.bean.MonitorBean;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.Finals;
import com.thdz.ywqx.util.NotifyUtil;
import com.thdz.ywqx.util.SpUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class MyApplication extends Application {

    /**
     * 通知的index
     */
    public static int notyId = 1;
    private String TAG = "MyApplication";
    private static MyApplication application;
    //    public static boolean controlFlag = false;
//    public static boolean alarmHandleFlag = false;
    //定时器，定时检测服务器是否通讯正常
    private final Timer timer = new Timer();
    public static boolean isShowAlarmDialog = false;
    private static String time = "";
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            // 检查服务器连接是否正常
            if (msg.what == 1 && !application.getIP().isEmpty() && !application.getUid().isEmpty()) {
                OkHttpUtils
                        .get()
                        .url(DataUtils.createReqUrl4Get(application.getIP(), application.getUid(),
                                Finals.CMD_GetUnitStatusByIds, ""))
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, final Exception e, int id) {
                                if (isShowAlarmDialog || getCurrentActivity() == null) {
                                    return;
                                }
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                time = sdf.format(new Date());
                                //显示连接异常提示
                                ShowAlarmDialog();
                                isShowAlarmDialog = true;
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                //服务器连接正常，不作处理
                            }
                        });
            }
            super.handleMessage(msg);
        }
    };

    public static void ShowAlarmDialog() {
        Activity activityNow = getCurrentActivity();
        if (activityNow != null) {
            //消息提示
            Intent intent = new Intent(activityNow, activityNow.getClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            NotifyUtil.showNotification(activityNow, "服务器连接异常！",
                    "服务器连接异常，请检查！", intent, 0);
        }

        //实例化建造者
        AlertDialog.Builder builder = new AlertDialog.Builder(getCurrentActivity());
        //设置警告对话框的标题
        builder.setTitle("系统提示");
        //设置警告显示的图片
        //builder.setIcon(android.R.drawable.ic_dialog_alert);
        //设置警告对话框的提示信息
        builder.setMessage(time + "\n服务器连接异常，请检查！");
        //设置”正面”按钮，及点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isShowAlarmDialog = false;
            }
        });
        builder.setCancelable(false);
        //显示对话框
        builder.show();
    }

    public static MyApplication getApplication() {
        if (null == application) {
            application = new MyApplication();
        }
        return application;
    }

    public int screenWidth = 0;
    public int screenheigth = 0;

    public static boolean isRequestingMonitors = false;
    public static List<MonitorBean> monitorList;

    private String IP_sever;
    private String uid;

    public String getIP() {
        if (TextUtils.isEmpty(IP_sever)) {
            IP_sever = SpUtil.getIP();
        }
        return IP_sever;
    }

    public void setIP(String ip) {
        this.IP_sever = ip;
    }


    public String getUid() {
        if (TextUtils.isEmpty(uid)) {
            uid = SpUtil.getUid();
        }

        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * 全部activity集合
     */
    public static List<Activity> activityList; // 全部activity集合

    public Context getInstance() {
//		if (application == null) {
//			application = new MyApplication();
//		}
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;// TODO 初始化一个？
//		refWatcher = LeakCanary.install(this);

        activityList = new LinkedList<>();

//        boolean sdCardExist = Environment.getExternalStorageState().equals(
//                android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
//        if (sdCardExist) {
//            File file = new File(Finals.imageCachePath);
//            if (!file.exists()) {
//                boolean isCreated = file.mkdir();
//                if (!isCreated) {
//                    Log.i(TAG, "缓存目录创建失败");
//                    // TsUtil.showTip(getApplicationContext(), "缓存目录创建失败");
//                }
//            }
//        } else {
//            // TsUtil.showTip(getApplicationContext(), "未获取到sd卡");
//        }

        // 初始化OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder() //
//        OkHttpClient okHttpClient = getUnsafeOkHttpClient().newBuilder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(20000L, TimeUnit.MILLISECONDS)
                .readTimeout(20000L, TimeUnit.MILLISECONDS)
                .writeTimeout(20000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
        //初始化定时器
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        timer.schedule(task, 60000, 300000);

        // 初始化海康
        //TODO: enableLog：在debug模式下打开日志，release关闭日志
        //TODO: 现阶段 appKey 不需要，直接传 null
        HikVideoPlayerFactory.initLib(null, true);

        // 初始化工具类
        Utils.init(this);
    }


//    private OkHttpClient getUnsafeOkHttpClient() {
//        try {
//            // Create a trust manager that does not validate certificate chains
//            final TrustManager[] trustAllCerts = new TrustManager[]{
//                    new X509TrustManager() {
//                        @Override
//                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                            return new java.security.cert.X509Certificate[]{};
//                        }
//                    }
//            };
//
//            // Install the all-trusting trust manager
//            final SSLContext sslContext = SSLContext.getInstance("SSL");
//            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//            // Create an ssl socket factory with our all-trusting manager
//            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            builder.sslSocketFactory(sslSocketFactory);
//            builder.hostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });
//
//            OkHttpClient okHttpClient = builder.build();
//            return okHttpClient;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }


    /**
     * 添加Activity到容器中
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 移除Activity到容器中
     */
    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public static Activity getCurrentActivity() {
        if (activityList.size() == 0) return null;
        Activity mAct = activityList.get(activityList.size() - 1);
        return mAct;
    }

    /**
     * 遍历所有Activity并finish
     */
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        timer.cancel();
        System.exit(0);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        exit();
        activityList.clear();
        activityList = null;
    }

    // 解析并保存数据
    public static void saveMobitros2DB(final String value) {
        Log.i("YWQX", "监控点列表json = " + value);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyApplication.isRequestingMonitors = false;
                try {
                    DataUtils.dealMntList(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
