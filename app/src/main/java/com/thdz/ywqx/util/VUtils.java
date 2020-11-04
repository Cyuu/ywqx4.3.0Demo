package com.thdz.ywqx.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;

import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.thdz.ywqx.ui.Activity.ImageActivity;
import com.thdz.ywqx.ui.Activity.VideoActivity;
import com.thdz.ywqx.ui.video.HistoryVideoActivity;
import com.thdz.ywqx.ui.video.PreviewActivity;

import static android.R.attr.path;

public class VUtils {

    /**
     * 上次点击时间
     */
    private static long lastClickTime;
    private static long interval_time = 450;

    /**
     * 防止重复点击，需要加：click select itemClick
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < interval_time) {
            // toast("按太快了");
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static void setSwipeColor(SwipeRefreshLayout sLayout) {
        sLayout.setColorSchemeResources(
                android.R.color.holo_blue_dark,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
    }

    /**
     * 设置SwipyLayout的颜色
     */
    public static void setSwipyColor(SwipyRefreshLayout sLayout) {
        sLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
    }


    /**
     * TODO 查看已下载的视频
     */
    public static void gotoVideo(Context context, String url, boolean isLocal) {
        TsUtil.showToast(context, "rtsp视频");
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("url", "file://" + path); // TODO url
        intent.putExtra("isLocal", isLocal);
        context.startActivity(intent);

    }

    /**
     * TODO 查看已下载的大图
     */
    public static void gotoImage(Context context, String image_name) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra("path", image_name);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

    }

    /**
     * 将dp转换成px
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将像素转换成dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


//    /**
//     * 提供ViewPager的ViewList，注意些Adapter的时候必须用view_pager_item
//     */
//    public static List<View> getViewPagerViewList(Context context) {
//        List<View> views = new ArrayList<View>();
//        for (int i = 0; i < 4; i++) {
//            View view = LayoutInflater.from(context.getApplicationContext()).inflate(
//                    R.layout.view_pager_item, null);
//            views.add(view);
//        }
//        return views;
//    }


//
//    private static int screenWidth = 0;
//    private static int screenHeight = 0;
//
//    /**
//     * 获取屏幕宽度
//     */
//    public static int getScreenWidth(Context context) {
//        if (screenWidth == 0) {
//            DisplayMetrics metrics = new DisplayMetrics();
//            ((Activity) context).getWindowManager().getDefaultDisplay()
//                    .getMetrics(metrics);
//            screenWidth = metrics.widthPixels;
//        }
//
//        return screenWidth; // 屏幕的宽
//    }
//
//    /**
//     * 获取屏幕高度
//     */
//    public static int getScreenHeight(Context context) {
//        if (screenHeight == 0) {
//            DisplayMetrics metrics = new DisplayMetrics();
//            ((Activity) context).getWindowManager().getDefaultDisplay()
//                    .getMetrics(metrics);
//            screenHeight = metrics.heightPixels;
//        }
//        return screenHeight;// 屏幕的高
//    }

    /**
     * 获得屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获得屏幕高度
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static void gotoVideoPreview(Context context, String url, String title) {
        Intent intent = new Intent(context, PreviewActivity.class); // PreviewActivity  PreEasyActivity
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    public static void gotoVideoHistory(Context context, String url) {
        Intent intent = new Intent(context, HistoryVideoActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

}
