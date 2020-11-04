package com.thdz.ywqx.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 检查升级<br/>
 * stopService(Intent name)
 */
public class UpdateDownloadService extends Service {

    private String TAG = "DownloadService";
    private Context context = this;

    private Binder mBinder = new ServiceBinder();


    @Override
    public void onCreate() {
        super.onCreate();

        mBinder = new ServiceBinder();
//        mDownloadQueue = new ArrayList<TaskInfo>();
//        mNotificationManager = (NotificationManager) getSystemService(
//                android.content.Context.NOTIFICATION_SERVICE);
//        mNotification = new Notification();
//        mRemoteView = new RemoteViews(this.getPackageName(), R.layout.remote_view_layout);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void checkUpdate() {
        UpdateManager update = new UpdateManager(context);
        update.CheckNewVersion();
    }

    public class ServiceBinder extends Binder {
        public UpdateDownloadService getService() {
            return UpdateDownloadService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        checkUpdate();
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        mBinder = null;
//        mDownloadQueue = null;
//        mNotificationManager = null;
//        mNotification = null;
//        mRemoteView = null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
