package com.thdz.ywqx.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.thdz.ywqx.R;
import com.thdz.ywqx.bean.UpdateBean;
import com.thdz.ywqx.util.DataUtils;
import com.thdz.ywqx.util.Finals;
import com.thdz.ywqx.util.TsUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 自动更新
 */
public class UpdateManager {

    private String TAG = "UPDATE";
    private Context context;

    /**
     * 下载中
     */
    private final int CODE_DOWNLOAD = 11;
    /**
     * 下载结束
     */
    private final int CODE_DOWN_FINISH = 22;

    /**
     * 下载保存路径
     */
    private String mSavePath = Finals.FilePath;
    /**
     * 下载apk包的名称
     */
    private String apkName;

    private UpdateBean updateBean;

    /**
     * 记录进度条数量
     */
    private int progress;

    private int old_ver = -1;

    public NotificationManager downloadNM;

    //BASE Notification ID
    private int Notification_ID_BASE = 110;

    public UpdateManager(Context context) {
        this.context = context;
        downloadNM = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
    }

    /**
     * 检测最新版本
     */
    public void CheckNewVersion() {
        updateBean = DataUtils.getUpdateBean();
        if (hasUpdate()) {
            //打开对话框下载
            new DownloadApkThread().start();
        }

    }

    /**
     * 比较版本号，判断是否有更新
     */
    public boolean hasUpdate() {
        try {
            int new_ver = Integer.parseInt(updateBean.getVersion());
            old_ver = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            if (new_ver > old_ver) {
                apkName = "ywqx_" + new_ver + ".apk";
                return true;

            } else {
                TsUtil.showToast(context, "已是最新版本");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 下载文件线程
     */
    private class DownloadApkThread extends Thread {

        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, apkName);

                    String tempUrl = updateBean.getUrl();
                    URL url = new URL(tempUrl);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    Log.i(TAG, "url = " + tempUrl);
                    Log.i(TAG, "length = " + length);
                    // 创建输入流
                    InputStream is = conn.getInputStream();
                    FileOutputStream fos = new FileOutputStream(apkFile);

                    byte[] buf = new byte[1024]; // 缓存
                    int charB = -1;
                    int count = 0; // 已下载文件长度
                    int tempP = -1; //用于不重复发送handler
                    while ((charB = is.read(buf)) != -1) {
                        fos.write(buf, 0, charB);// 写入到文件中
                        count += charB;

                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        Log.i(TAG, "progess = " + progress + "%");
                        Log.i(TAG, "count = " + count);

                        // TODO 定时任务
                        if (progress % 5 == 0 && progress != 100 && tempP != progress) {
                            tempP = progress;
                            mHandler.sendEmptyMessage(CODE_DOWNLOAD);
                            Log.i(TAG, "handler发送处理");
                        }

                    }

                    fos.close();
                    is.close();
                    Log.i(TAG, "下载结束");
                    Log.i(TAG, "length = " + length);
                    mHandler.sendEmptyMessage(CODE_DOWN_FINISH);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 安装APK文件
     */
    public void installApk(Context context) {
        File apkfile = new File(mSavePath, apkName);
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
        // ((Activity) context).finish();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case CODE_DOWNLOAD:
                    Log.i(TAG, "下载中");
                    showNotification(false);
                    break;
                case CODE_DOWN_FINISH:
                    Log.i(TAG, "下载完成");
                    showNotification(true);
                    doAfterFinish();
                    break;
                default:
                    break;
            }
        }

    };

    private NotificationCompat.Builder builder = null;

    public PendingIntent getDefalutIntent(int flags) {
        // TODO 有问题的代码
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, new Intent(), flags);
        return pendingIntent;
    }

    /**
     * 展示通知栏，是否点击后就关闭
     */
    private void showNotification(boolean isFinish) {
        if (builder == null) {
            Intent intent = new Intent(context, context.getClass());
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            // 填充视图
            // RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_update);

            builder = new NotificationCompat.Builder(context);
            //setSmallIcon   setContentTitle   setContentText是必须的 不定义或少定义都不显示notificaiton
            builder.setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getApplicationContext().getString(R.string.app_name) + ".apk")
                    .setContentText("正在下载")
                    .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
                    .setTicker("正在升级" + context.getApplicationContext().getString(R.string.app_name) + ".apk")
                    .setOngoing(true)
                    .setWhen(0)
                    .setAutoCancel(true);
        }

        if (isFinish) {// progress == 100
            Intent finishIntent = new Intent(Intent.ACTION_VIEW);
            finishIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            File apkfile = new File(mSavePath, apkName);
            finishIntent.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                    "application/vnd.android.package-archive");
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, finishIntent, PendingIntent.FLAG_ONE_SHOT);
            builder.setOngoing(false).setContentText("下载完成").setContentIntent(pendingIntent);
        }

        //这个方法是显示进度条，这个比较有用
        builder.setProgress(100, progress, false);
        downloadNM.notify(Notification_ID_BASE, builder.build());

    }

    private void doAfterFinish() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 安装文件
                installApk(context);
            }
        }, 100);

    }

}