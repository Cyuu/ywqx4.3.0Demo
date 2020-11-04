package com.thdz.ywqx.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.thdz.ywqx.app.MyApplication;

import java.util.Map;
import java.util.Set;

public class SpUtil {

    /**
     * 取sp数据
     */
    public static String getData(String key) {
        SharedPreferences sp = MyApplication.getApplication()
                .getSharedPreferences(Finals.SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    /**
     * 取sp数据
     */
    public static boolean getBoolean(String key) {
        SharedPreferences sp = MyApplication.getApplication()
                .getSharedPreferences(Finals.SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    /**
     * 是否是自动登录, 1和默认是：自动登录，0 是不自动登录
     */
    public static boolean isAutoLogin() {
        SharedPreferences sp = MyApplication.getApplication()
                .getSharedPreferences(Finals.SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(Finals.SP_AUTOLOGIN, "0").equals("1");
    }

//    /**
//     * 是否是自动登录,1和默认是：内网，0 是外网
//     */
//    public static boolean isInternalNet(Context context) {
//        SharedPreferences sp = context
//                .getSharedPreferences(Finals.SP_NAME, Context.MODE_PRIVATE);
//        return sp.getString(Finals.SP_NET_PLACE,"1").equals("1");
//    }

    /**
     * 取uid数据
     */
    public static String getUid() {
        SharedPreferences sp = MyApplication.getApplication()
                .getSharedPreferences(Finals.SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(Finals.SP_UID, "");
    }

    /**
     * 取ip数据
     */
    public static String getIP() {
        SharedPreferences sp = MyApplication.getApplication()
                .getSharedPreferences(Finals.SP_NAME, Context.MODE_PRIVATE);
        return sp.getString(Finals.SP_IP, "");
    }


    /**
     * 保存sp数据 String
     */
    public static void save(String key, String value) {
        new SpSaveThread(key, value).run();
    }

    /**
     * 保存sp数据 boolean
     */
    public static void save(String key, boolean value) {
        new SpSaveBoolThread(key, value).run();
    }

    /**
     * 保存sp数据
     */
    public static void save(Map<String, String> spMap) {
        Set<String> keySet = spMap.keySet();
        for (String key : keySet) {
            new SpSaveThread(key,
                    spMap.get(key)).run();
        }
    }

    /**
     * 保存字符串
     */
    public static class SpSaveThread extends Thread {
        public String key;
        public String data;

        public SpSaveThread(String key, String data) {
            super();
            this.key = key;
            this.data = data;
        }

        @Override
        public void run() {
            super.run();
            SharedPreferences sp = MyApplication.getApplication().
                    getSharedPreferences(Finals.SP_NAME, Context.MODE_PRIVATE);
            sp.edit().putString(key, data).commit();
        }
    }

    /**
     * 保存字符串
     */
    public static class SpSaveBoolThread extends Thread {
        public String key;
        public boolean value;

        public SpSaveBoolThread(String key, boolean value) {
            super();
            this.key = key;
            this.value = value;
        }

        @Override
        public void run() {
            super.run();
            SharedPreferences sp = MyApplication.getApplication().
                    getSharedPreferences(Finals.SP_NAME, Context.MODE_PRIVATE);
            sp.edit().putBoolean(key, value).commit();
        }
    }

    /**
     * 告警处理权限
     */
    public static void saveAlarmHandleFlag(boolean flag) {
        save(Finals.SP_ALARM_HANDLE_FLAG, flag);
    }

    /**
     * 告警处理权限
     */
    public boolean getAlarmHandleFlag() {
        return getBoolean(Finals.SP_ALARM_HANDLE_FLAG);
    }

    /**
     * 详情页控制权限
     */
    public static void saveControlFlag(boolean flag) {
        save(Finals.SP_CONTROL_FLAG, flag);
    }

    /**
     * 详情页控制权限
     */
    public boolean getControlFlag() {
        return getBoolean(Finals.SP_CONTROL_FLAG);
    }
}
