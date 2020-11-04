package com.thdz.ywqx.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.thdz.ywqx.R;
import com.thdz.ywqx.app.MyApplication;

public class TsUtil {
    /**
     * toast提示消息
     */
//    public static void showTip(Context context, String msg) {
//        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
//    }

//	public static void WarnNoNet(Context context) {
//		Toast.makeText(context.getApplicationContext(), "无网络连接", Toast.LENGTH_SHORT).show();
//	}


    /**
     * 自定义Toast展示
     */
    public static void showToast(Context context, String info) {
//        LayoutInflater inflater = (LayoutInflater)
//                context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View toastView = inflater.inflate(R.layout.layout_toast, null, false);
//
//        Toast toast = new Toast(context.getApplicationContext());
//        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 0);
//        toast.setDuration(Toast.LENGTH_SHORT);
//
//        TextView txt = (TextView) toastView.findViewById(R.id.txt_tips);
//        txt.setText(info);
//
//        toast.setView(toastView);
//        toast.show();

        try {
            if (info.length() > 20) {
                Toast.makeText(context, info, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void toast(String info) {
        // showToast(MyApplication.getApplication(), info);
        try {
            if (info.length() > 20) {
                Toast.makeText(MyApplication.getApplication(), info, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MyApplication.getApplication(), info, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
