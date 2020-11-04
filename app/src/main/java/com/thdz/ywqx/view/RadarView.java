package com.thdz.ywqx.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;

import com.thdz.ywqx.R;
import com.thdz.ywqx.bean.RadarTargetBean;

import gyif.com.library.ScaleCanvasView;


/**
 * desc:    绘制雷达点
 * Matrix方式实现
 * author:  Administrator
 * date:    2018/8/13  9:00
 */
public class RadarView extends ScaleCanvasView implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = "RadarView";

    private Paint paint;

    // 控件的宽和高
    private float mViewWidth = 0f;
    private float mViewHeight = 0f;

    // 1 雷达坐标点集合
    private float[] radarFloats;

    // 2 目标数据点
    private RadarTargetBean[] targets;

    // 3 防区1数据点
    private PointF[][] areaPoints1; // 背景区
    private PointF[][] areaPoints2; // 预警区
    private PointF[][] areaPoints3; // 告警区

    /**
     * 矩形宽高，需要比圆形稍宽, 用于换算坐标
     */
    private int drawRadius = 4200;

    /**
     * 雷达扫描直径， 用于绘制白色圆形的半径
     */
    private int rectWidth = 4000;


    // 绘制文字的尺寸
    private float textSize = 32f;
    // 手指触摸点坐标提示
    private float posSize = 26f;

    private float CircleCenterWidth = 6f;  // 圆心的宽度
    private float objWidth = 5f;        // 目标点的宽度
    private float pointWidth = 1.5f;      // 轨迹点的宽度
    private float axisWidth = 0.8f;     // 横线竖线宽
    private float areaLineWidth = 2;    // 防区连线的宽度

    private int radarColor = getResources().getColor(R.color.blue);           // 雷达数据点
    private int objColor = getResources().getColor(R.color.red_deep_color);   // 告警目标
    private int area1Color = getResources().getColor(R.color.orange_color);   // 防区1
    private int area2Color = getResources().getColor(R.color.green_color);    // 防区2
    private int area3Color = getResources().getColor(R.color.red_deep_color); // 防区3
    private int axisColor = getResources().getColor(R.color.radar_line);      // xy轴线


    public RadarView(Context context) {

        super(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

    }

    public void setData(int r, float[] radars, RadarTargetBean[] targets, PointF[][] area1, PointF[][] area2, PointF[][] area3) {
        // 雷达扫描半径
        if (r != 0) {
            this.rectWidth = r * 2;
            this.drawRadius = rectWidth + rectWidth / 10;
        }
        // 雷达数据
        if (radars != null && radars.length > 0) {
            for (int i = 0; i < radars.length; i++) {
                if (radars[i] > rectWidth / 2) {
                    radars[i] = 0;
                } else {
                    radars[i] = radars[i] * mViewWidth / drawRadius;
                }
            }
            this.radarFloats = radars;
        }
        // 雷达告警目标
        if (targets != null && targets.length > 0) {
            for (int i = 0; i < targets.length; i++) {
                float x = targets[i].x * mViewWidth / drawRadius;
                float y = targets[i].y * mViewWidth / drawRadius;
                int type = targets[i].type;
                int objtype = targets[i].objType;
                int cprad = targets[i].cpRad;
                targets[i] = new RadarTargetBean(x, y, type, objtype, cprad);
            }
            this.targets = targets;
        }

        // 雷达防区
        if (area1 != null && area1.length > 0) {
            for (int index = 0; index < area1.length; index++) {
                for (int i = 0; i < area1[index].length; i++) {
                    area1[index][i] = new PointF(
                            mViewWidth / 2 + (area1[index][i].x * mViewWidth / drawRadius),
                            mViewHeight / 2 - (area1[index][i].y * mViewWidth / drawRadius));
                }
            }
            this.areaPoints1 = area1;
        }
        if (area2 != null && area2.length > 0) {
            for (int index = 0; index < area2.length; index++) {
                for (int i = 0; i < area2[index].length; i++) {
                    area2[index][i] = new PointF(
                            mViewWidth / 2 + (area2[index][i].x * mViewWidth / drawRadius),
                            mViewHeight / 2 - (area2[index][i].y * mViewWidth / drawRadius));
                }
            }
            this.areaPoints2 = area2;
        }
        if (area3 != null && area3.length > 0) {
            for (int index = 0; index < area3.length; index++) {
                for (int i = 0; i < area3[index].length; i++) {
                    area3[index][i] = new PointF(
                            mViewWidth / 2 + (area3[index][i].x * mViewWidth / drawRadius),
                            mViewHeight / 2 - (area3[index][i].y * mViewWidth / drawRadius));
                }
            }
            this.areaPoints3 = area3;
        }
    }

    //region 获取控件宽高

    /**
     * 在 onAttachedToWindow 注册 onGlobalLayout 事件监听
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        //注册onGlobalLayout事件监听
        this.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /**
     * 在 onDetachedFromWindow 解除 onGlobalLayout 事件监听
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        // 解除onGlobalLayout事件监听
        this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        this.destroyDrawingCache();
    }


    /**
     * 在onGlobalLayouts 事件中获取组件最终呈现的高度和宽度
     * 这就是 canvas 画布的尺寸
     */
    @Override
    public void onGlobalLayout() {
        // 组件呈现的宽度高度
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();
        Log.i(TAG, "获取到控件宽高：" + mViewWidth + ", " + mViewHeight);
    }
    //endregion


    /**
     * 将dp转换成px
     */
    public int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @Override
    protected void drawCustom(Canvas canvas) {

        // 清空画布
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        // 抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));

        // 绘制灰色背景
        canvas.drawColor(getResources().getColor(R.color.radar_gray));

        // 画白色圆 todo 动态半径
        paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawCircle(mViewWidth / 2, mViewHeight / 2, mViewWidth * rectWidth / drawRadius / 2, paint);

        // 画坐标轴
        drawAxisLine(canvas);

        // 画黑色圆心
        paint.setColor(Color.BLACK);
        canvas.drawCircle(mViewWidth / 2, mViewHeight / 2, dp2px(getContext(), CircleCenterWidth), paint);

        // 防区 - 背景区
        drawArea(canvas, areaPoints1, 1);
        // 防区 - 预警区
        drawArea(canvas, areaPoints2, 2);
        // 防区 - 告警区
        drawArea(canvas, areaPoints3, 3);

        // 绘制雷达点
        drawRadar(canvas);

        // 画雷达目标
        drawObj(canvas);

    }


    /**
     * 绘制xy轴线, 变化的横竖线间距，变化的横竖线数量
     */
    private void drawAxisLine(Canvas canvas) {
        try {
            paint = new Paint();
            paint.setColor(axisColor);
            paint.setStrokeWidth(axisWidth);

            float paddingX = mViewWidth / 8; // x，y轴padding相同

            float startX = 0;
            float starty = 0;

            float rad = mViewWidth / 2;
            float restW = mViewHeight / 2 - mViewWidth / 2;

            for (int j = 0; j <= 8; j++) {
                // 画竖线
                canvas.drawLine(
                        startX + j * paddingX,
                        starty + restW,
                        startX + j * paddingX,
                        starty + rad * 2 + restW,
                        paint);
                // 画横线
                canvas.drawLine(
                        startX,
                        starty + j * paddingX + restW,
                        startX + rad * 2,
                        starty + j * paddingX + restW,
                        paint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 绘制雷达数据点
     */
    public void drawRadar(Canvas canvas) {
        try {
            if (radarFloats != null && radarFloats.length > 0) {
                paint = new Paint();
                paint.setColor(radarColor);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);
                float pie = (float) (Math.PI / 180);

                // todo 新版雷达
                // 雷达点个数，360个 --> 720个
                // 整个角度：361°， 角度分辨率：0.5, 更换雷达后，变为：0.25
                double angleResolution = 1.0 / (radarFloats.length / 180);
                for (int i = 0; i < radarFloats.length; i++) {

                    float rangle = (float) ((i * angleResolution) % 361);

                    if (radarFloats[i] == 0) {
                        continue;
                    }

                    // 与雷达的相对距离 -- > 相对左上角(无缩放和平移)的坐标
                    float fx = mViewWidth / 2 + radarFloats[i] * (float) Math.cos(rangle * pie);
                    float fy = mViewHeight / 2 - radarFloats[i] * (float) Math.sin(rangle * pie);

                    canvas.drawCircle(fx, fy, pointWidth, paint);
                }

                // todo 旧版雷达
//                for (int i = 0; i < radarFloats.length; i++) {
//
//                    // 整个角度：361°， 角度分辨率：0.5
//                    float rangle = (float) ((i * 0.5) % 361);
//
//                    if (radarFloats[i] == 0) {
//                        continue;
//                    }
//
//                    // 与雷达的相对距离 -- > 相对左上角(无缩放和平移)的坐标
//                    float fx = mViewWidth / 2 + radarFloats[i] * (float) Math.cos(rangle * pie);
//                    float fy = mViewHeight / 2 - radarFloats[i] * (float) Math.sin(rangle * pie);
//
//                    canvas.drawCircle(fx, fy, pointWidth, paint);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 绘制雷达目标点
     */
    public void drawObj(Canvas canvas) {
        try {
            if (targets != null && targets.length > 0) {
                paint = new Paint();
                paint.setColor(objColor);
                paint.setAntiAlias(true);

                paint.setStyle(Paint.Style.FILL);

                for (int i = 0; i < targets.length; i++) {
                    RadarTargetBean bean = targets[i];
                    if (bean.x == 0 && bean.y == 0) {
                        continue;
                    }
                    float fx = mViewWidth / 2 + bean.x;
                    float fy = mViewHeight / 2 - bean.y;
                    canvas.drawCircle(fx, fy, objWidth, paint);

                    if (!TextUtils.isEmpty(bean.getTypeValue())) {
                        paint.setTextSize(textSize);
                        paint.setTypeface(Typeface.DEFAULT_BOLD);
                        paint.setStrokeWidth(0);
                        canvas.drawText(bean.getTypeValue(), fx + 10, fy + 12, paint);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 绘制防区1
     *
     * @param points 防区数据点
     * @param type   1 背景区 2 预警区 3 告警区
     */
    public void drawArea(Canvas canvas, PointF[][] points, int type) {
        try {
            if (points != null && points.length > 0) {
                Path path = new Path();
                for (int index = 0; index < points.length; index++) {
                    for (int i = 0; i < points[index].length; i++) {
                        PointF p = points[index][i];
                        if (i == 0) {
                            path.moveTo(p.x, p.y);
                        } else {
                            path.lineTo(p.x, p.y);
                        }
                    }

                    path.close();

                    paint = new Paint();
                    paint.setColor(area1Color);
                    paint.setStyle(Paint.Style.STROKE);
                    paint.setStrokeWidth(areaLineWidth);
                    if (type == 1) {
                        paint.setColor(area1Color);
                    } else if (type == 2) {
                        paint.setColor(area2Color);
                    } else if (type == 3) {
                        paint.setColor(area3Color);
                    }

                    canvas.drawPath(path, paint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
