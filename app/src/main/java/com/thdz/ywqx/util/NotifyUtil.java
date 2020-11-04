package com.thdz.ywqx.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.thdz.ywqx.R;
import com.thdz.ywqx.ui.Activity.alarm.AlarmDetailActivity;

/**
 * 通知栏工具类
 */
public class NotifyUtil {

    /**
     * 通知
     *
     * @param context
     * @param title
     * @param content
     * @param index
     * @param intent
     */
    public static void CreateNotify(Context context, String title, String content, int index, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext());

        PendingIntent pi = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, 0);
        // 震动通知
        // long[] vibratePattern = new long[] {400,800,1200,1600};
        // 创建一个 Notification
        Notification notification = builder
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pi)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setColor(context.getApplicationContext().getResources().getColor(R.color.white))
                .setSmallIcon(R.mipmap.ic_launcher)
                // .setVibrate(vibratePattern)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL; // 设置点击后自动消失
        // 使用 NotificationManagerCompat 的 notify 方法展示你设置了 id 的那个 Notification
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context.getApplicationContext());
        notificationManager.notify(index, notification);
    }

    /**
     * 打开详情页的通知
     *
     * @param context
     * @param title
     * @param content
     * @param index
     */
    public static void CreateNotification(Context context, String title, String content, int index) {
        Intent intent = new Intent(context.getApplicationContext(), AlarmDetailActivity.class);
        CreateNotify(context, title, content, index, intent);
    }

    public static void showNotification(Context context, String title, String content, Intent intent, int notyId) {
        // 获取一个 NotificationCompat.Builder 实例。
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext());
        PendingIntent pi = PendingIntent.getActivity(context.getApplicationContext(), 0, intent, 0);
        // 震动通知
        long[] vibratePattern = new long[]{400, 800, 1200, 1600};
        // 创建一个 Notification
        Notification notification = builder
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .setColor(context.getApplicationContext().getResources().getColor(R.color.white))
                .setSmallIcon(R.drawable.push)
                .setSound(Uri.parse("android.resource://" +  context.getPackageName() + "/" + R.raw.call))
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .build();

//        notification.defaults |= Notification.DEFAULT_VIBRATE;
        if (title.contains("告警取消")){
            notification.defaults |= Notification.DEFAULT_SOUND;
        }

        notification.flags |= Notification.FLAG_AUTO_CANCEL; // 设置点击后自动消失
        // 使用 NotificationManagerCompat 的 notify 方法展示你设置了 id 的那个 Notification
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context.getApplicationContext());
        notificationManager.notify(notyId, notification);
    }
}
