package com.thdz.ywqx.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtil {


    /**
     * 原生对话框， 只做展示，不做数据点选等交互
     */
    public static void showOnly(Context context, /*String title, */String msg) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                // .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

}
