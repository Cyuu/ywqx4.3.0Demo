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
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;

import com.thdz.ywqx.R;
import com.thdz.ywqx.util.VUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * com.thdz.ywqx.view.RadarView
 * TouchEvent（scaler, offersetx,y）方式实现
 * 调用个步骤：
 * 1 初始化
 * 2 setData()
 * 3 postValidate()
 */
public class SimpleView extends View implements ViewTreeObserver.OnGlobalLayoutListener {

    private static final String TAG = "RadarView";
    private static final int DRAG = 1;  //拖拽标志
    private static final int ZOOM = 2;  //缩放标志

    private static final float MaxScale = 8f;
    private static final float MinScale = 1f;

    /**
     * 拖拽或缩放操作标准
     */
    private int mMode = DRAG;

    // 上下文
    private Context mContext;

    private boolean isNew = true;

    //  --------- 点集合 ----------

    // 1 雷达坐标点集合
    private float[] radarFloats;

    // 2.1 防区1数据点
    private float[] areaFloats1;
    // 2.2 防区2数据点
    private float[] areaFloats2;
    // 2.3 防区3数据点
    private float[] areaFloats3;

    // 3 目标数据点
    private float[] objFloats;

    // 1 坐标点集合Paint
    private Paint paint;

    //  --------- draw ----------

    // 绘制文字的尺寸
    private float textSize = 32f;
    // 手指触摸点坐标提示
    private float posSize = 26f;

    // 地图原点坐标 -- 中心点坐标
    private PointF centerF;

    // 当前缩放的中心点
    private PointF zoomCF;

    // 当前手指点击屏幕坐标点，手指抬起时，置空
    private PointF mDownPoint;

    // 拖拽偏移量
    private float mOffsetX;
    private float mOffsetY;

    // 缩放比率
    private float mScaler = 1;

    // 控件的宽和高
    private float mViewWidth;
    private float mViewHeight;

    /**
     * 白色圆圈的半径，这个是根据数据生成的，可以是事先设置
     */
    private float radius4Circle;

    // 缩放手势监听
    private ScaleGestureDetector mScaleGestureDetector = null;

    private float objWidth = 6f;       // 目标点的宽度
    private float pointWidth = 2.5f;   // 轨迹描点的宽度
    private float axisWidth = 0.8f;    // 横线竖线宽
    private float areaLineWidth = 2;   // 防区连线的宽度

    private int radarColor = getResources().getColor(R.color.blue);           // 雷达数据点
    private int objColor = getResources().getColor(R.color.red_deep_color);   // 告警目标
    private int area1Color = getResources().getColor(R.color.orange_color);   // 防区1
    private int area2Color = getResources().getColor(R.color.green_color);    // 防区2
    private int area3Color = getResources().getColor(R.color.red_deep_color); // 防区3
    private int axisColor = getResources().getColor(R.color.radar_line);      // xy轴线


    // new调用
    public SimpleView(Context context) {
        super(context);
        mContext = context;
    }

    // xml里调用， layout_width="match_parent"
    public SimpleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        centerF = new PointF();
        zoomCF = new PointF();
        mDownPoint = new PointF();

        //定义缩放手势监听
        mScaleGestureDetector = new ScaleGestureDetector(mContext, new SimpleView.ScaleGestureListener());
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

        resetParam();
    }
    //endregion


    public float getRadiusOfWhiteCircle() {
        radius4Circle = getRadiusOfCircle() - VUtils.dp2px(mContext, 20) * mScaler;
        return radius4Circle;
    }

    public void setRadius4Circle(float r) {
        radius4Circle = r;
    }

    /**
     * 重置参数
     */
    private void resetParam() {
        if (isNew) {
            centerF = new PointF(mViewWidth / 2, mViewHeight / 2);
            zoomCF = new PointF(mViewWidth / 2, mViewHeight / 2);
            isNew = false;
        }
    }

    /**
     * 获取绘制轴线的虚拟半径
     */
    private float getRadiusOfCircle() {
        return Math.min(mViewWidth, mViewHeight) * mScaler / 2;
    }

    /**
     * 设置需要绘制的数据
     */
    public void setData(float[] radarFloats, float[] objFloats, float[] areaFloats1, float[] areaFloats2, float[] areaFloats3) {
        // 雷达数据
        if (radarFloats != null && radarFloats.length > 0) {
            for (int i = 0; i < radarFloats.length; i++) {
                radarFloats[i] = radarFloats[i] * mViewWidth / 5000;
            }
            this.radarFloats = radarFloats;
        }

        if (objFloats != null && objFloats.length > 0) {
            for (int i = 0; i < objFloats.length; i++) {
                objFloats[i] = objFloats[i] * mViewWidth / 5000;
            }
            this.objFloats = objFloats;
        }

        if (areaFloats1 != null && areaFloats1.length > 0) {
            for (int i = 0; i < areaFloats1.length; i++) {
                areaFloats1[i] = areaFloats1[i] * mViewWidth / 5000;
            }
            this.areaFloats1 = areaFloats1;
        }

        if (areaFloats2 != null && areaFloats2.length > 0) {
            for (int i = 0; i < areaFloats2.length; i++) {
                areaFloats2[i] = areaFloats2[i] * mViewWidth / 5000;
            }
            this.areaFloats2 = areaFloats2;
        }

        if (areaFloats3 != null && areaFloats3.length > 0) {
            for (int i = 0; i < areaFloats3.length; i++) {
                areaFloats3[i] = areaFloats3[i] * mViewWidth / 5000;
            }
            this.areaFloats3 = areaFloats3;
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initDraw(canvas);

//        drawGrayRect(canvas);
        drawWhiteCircle(canvas);
        drawCenterPoint(canvas);
        drawAxisLine(canvas);
        drawRadar(canvas);
        drawPostion(canvas);
        drawObj(canvas);
        drawArea1(canvas);
        drawArea2(canvas);
        drawArea3(canvas);

    }

    private void initDraw(Canvas canvas) {
        // 清空画布
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawColor(getResources().getColor(R.color.radar_gray));
        // 抗锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
    }

//    // 绘制灰框
//    private void drawGrayRect(Canvas canvas) {
//        Paint paint = new Paint();
//        paint.setColor(getResources().getColor(R.color.radar_gray));
//        paint.setStyle(Paint.Style.FILL);
//
//        float halfRadius = Math.min(mMapWidth, mMapHeight) * mScaler / 2;
//
//        canvas.drawRect(
//                mOffsetX + (centerF.x - halfRadius) * mScaler / orgScale,
//                mOffsetY + (centerF.y - halfRadius) * mScaler / orgScale,
//                mOffsetX + (centerF.x + halfRadius) * mScaler / orgScale,
//                mOffsetY + (centerF.y + halfRadius) * mScaler / orgScale,
//                paint);
//    }


    // 绘制白圆
    private void drawWhiteCircle(Canvas canvas) {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.white));
        paint.setStyle(Paint.Style.FILL);

        // todo 这个圆的半径，需要根据数据确定，而不是这样处理的。暂且如此
        float rad = getRadiusOfWhiteCircle();
        canvas.drawCircle(
                mOffsetX + centerF.x,
                mOffsetY + centerF.y,
                rad,
                paint);
    }

    // 绘制原点
    private void drawCenterPoint(Canvas canvas) {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.black_deep));
        paint.setStyle(Paint.Style.FILL);

        float centerRadius = 10f; // 中心点半径
        canvas.drawCircle(
                mOffsetX + centerF.x,
                mOffsetY + centerF.y,
                centerRadius,
                paint);
    }


    /**
     * 绘制xy轴线
     */
    private void drawAxisLine(Canvas canvas) {
        paint = new Paint();
        paint.setColor(axisColor);
        paint.setStrokeWidth(axisWidth);

        int count = 10;// 横，竖线的数量
        float paddingX = mViewWidth / count * mScaler; // x，y轴padding相同

        float rad = getRadiusOfCircle();

        for (int j = 0; j <= count; j++) {
            // 画竖线
            canvas.drawLine(
                    mOffsetX + centerF.x - rad + j * paddingX,
                    mOffsetY + centerF.y - rad,
                    mOffsetX + centerF.x - rad + j * paddingX,
                    mOffsetY + centerF.y + rad,
                    paint);
            // 画横线
            canvas.drawLine(
                    mOffsetX + centerF.x - rad,
                    mOffsetY + centerF.y - rad + j * paddingX,
                    mOffsetX + centerF.x + rad,
                    mOffsetY + centerF.y - rad + j * paddingX,
                    paint);
        }
    }


    /**
     * 绘制雷达数据点
     */
    public void drawRadar(Canvas canvas) {
        if (radarFloats != null && radarFloats.length > 0) {
            paint = new Paint();
            paint.setColor(radarColor);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            float pie = (float) (Math.PI / 180);

            for (int i = 0; i < radarFloats.length; i++) {

                // 整个角度：361°， 角度分辨率：0.5
                float rangle = (float) ((i * 0.5) % 361);

                //region 4个象限计算符号
                // 4个象限计算符号
//                int addMinus1 = 1;
//                int addMinus2 = 1;
//                if (rangle <= 90) {
//                    addMinus1 = 1;
//                    addMinus2 = -1;
//                } else if (rangle < 180) {
//                    addMinus1 = -1;
//                    addMinus2 = -1;
//                } else if (rangle < 270) {
//                    addMinus1 = -1;
//                    addMinus2 = 1;
//                } else {
//                    addMinus1 = 1;
//                    addMinus2 = 1;
//                }
                //endregion

                float fx = /*(zoomCF.x - centerF.x)*/ +centerF.x + radarFloats[i] * (float) Math.cos(rangle * pie) * mScaler + mOffsetX;
                float fy = /*(zoomCF.y - centerF.y)*/ +centerF.y + radarFloats[i] * (float) Math.sin(rangle * pie) * mScaler + mOffsetY;
                canvas.drawCircle(fx, fy, pointWidth, paint);
            }
        }
    }

    /**
     * 绘制手指触点位置
     */
    private void drawPostion(Canvas canvas) {
        if (mDownPoint.x == 0 && mDownPoint.y == 0) {
            return;
        }
        paint = new Paint();
        paint.setColor(objColor);
        paint.setAntiAlias(true);
        paint.setTextSize(posSize);
        paint.setStrokeWidth(0);
        paint.setAntiAlias(true);

        float x = centerF.x - (mDownPoint.x);
        float y = centerF.y - (mDownPoint.y);

        int intx = (int) (x / 0.144 / mScaler);
        int inty = (int) (y / 0.144 / mScaler);

        canvas.drawText("触点坐标：" + intx + ", " + inty, 30, 40, paint);
    }

    /**
     * 绘制雷达目标点
     */
    public void drawObj(Canvas canvas) {
        if (objFloats != null && objFloats.length > 0) {
            paint = new Paint();
            paint.setColor(objColor);
            paint.setAntiAlias(true);

            paint.setTextSize(textSize);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setStrokeWidth(0);

            paint.setStyle(Paint.Style.FILL);

            for (int i = 0; i < objFloats.length / 2; i++) {
                if (objFloats[i * 2] == 0 && objFloats[i * 2 + 1] == 0) {
                    continue;
                }
                float fx = centerF.x - objFloats[i] * mScaler + mOffsetX;
                float fy = centerF.y - objFloats[i * 2 + 1] * mScaler + mOffsetY;
                canvas.drawCircle(fx, fy, objWidth, paint);
                canvas.drawText("雷达目标", fx + 10, fy + 10, paint);
            }
        }
    }


    /**
     * 绘制防区1
     */
    public void drawArea1(Canvas canvas) {
        if (areaFloats1 != null && areaFloats1.length > 0) {
            Path path = new Path();
            for (int i = 0; i < areaFloats1.length / 2; i++) {

                if (i == 0) {
                    path.moveTo(areaFloats1[i * 2], areaFloats1[i * 2 + 1]);
                } else {
                    path.lineTo(areaFloats1[i * 2], areaFloats1[i * 2 + 1]);
                }
            }
            path.close();

            paint = new Paint();
            paint.setColor(area1Color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(areaLineWidth);

            canvas.drawPath(path, paint);
        }
    }

    /**
     * 绘制防区2
     */
    public void drawArea2(Canvas canvas) {
        if (areaFloats2 != null && areaFloats2.length > 0) {
            Path path = new Path();
            for (int i = 0; i < areaFloats2.length / 2; i++) {
                if (i == 0) {
                    path.moveTo(areaFloats2[i * 2], areaFloats2[i * 2 + 1]);
                } else {
                    path.lineTo(areaFloats2[i * 2], areaFloats2[i * 2 + 1]);
                }
            }
            path.close();

            paint = new Paint();
            paint.setColor(area2Color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(areaLineWidth);

            canvas.drawPath(path, paint);
        }
    }

    /**
     * 绘制防区3
     */
    public void drawArea3(Canvas canvas) {
        if (areaFloats3 != null && areaFloats3.length > 0) {
            Path path = new Path();
            for (int i = 0; i < areaFloats3.length / 2; i++) {
                if (i == 0) {
                    path.moveTo(areaFloats3[i * 2], areaFloats3[i * 2 + 1]);
                } else {
                    path.lineTo(areaFloats3[i * 2], areaFloats3[i * 2 + 1]);
                }
            }
            path.close();

            paint = new Paint();
            paint.setColor(area3Color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(areaLineWidth);
            canvas.drawPath(path, paint);
        }
    }


    /**
     * 缩放手势监听
     */
    public class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // 缩放比例
            float scaleFactor = detector.getScaleFactor();

            if (mScaler * scaleFactor > MaxScale) {
                mScaler = MaxScale;
                scaleFactor = MaxScale / mScaler;
            } else if (mScaler * scaleFactor < MinScale) {
                mScaler = MinScale;
                scaleFactor = MinScale / mScaler;
            } else {
                mScaler = mScaler * scaleFactor;
            }

            // todo
            zoomCF.x = detector.getFocusX(); // 获得两手指点之间的 中点X值
            zoomCF.y = detector.getFocusY(); // 获得两手指点之间的 中点Y值

            Log.i(TAG, "两指中点坐标：" + zoomCF.x + "," + zoomCF.y);
            Log.i(TAG, "缩放比例：mScaler= " + mScaler + ", scaleFactor= " + scaleFactor);
            EventBus.getDefault().post("Scale = " + mScaler);

            invalidate();

            return true; // true 表示已处理，false表示未处理，继续计算
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }
    }


    /**
     * 触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // 放大缩小手势检测
        if (mScaleGestureDetector != null)
            mScaleGestureDetector.onTouchEvent(event);

        switch (event.getAction() & event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:    //单指

                mMode = DRAG;

                // 获取多点触控的数量求点击屏幕坐标均值
                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {
                    mDownPoint.x += event.getX(i);
                    mDownPoint.y += event.getY(i);
                }
                mDownPoint.x /= pointerCount;
                mDownPoint.y /= pointerCount;


//                mDownPoint.x += event.getX();
//                mDownPoint.y += event.getY();
                Log.i(TAG, "手指按下：" + mDownPoint.x + ", " + mDownPoint.y);

                break;
            case MotionEvent.ACTION_POINTER_DOWN: // 多指
                // 多指则认为是缩放操作
                mMode = ZOOM;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMode == DRAG) {
                    mOffsetX = event.getX() - mDownPoint.x;
                    mOffsetY = event.getY() - mDownPoint.y;

                    invalidateView();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mMode == DRAG) {
                    mOffsetX = event.getX() - mDownPoint.x;
                    mOffsetY = event.getY() - mDownPoint.y;
                    Log.i(TAG, "mOffset：" + mOffsetX + ", " + mOffsetY);

                    centerF.x += mOffsetX;
                    centerF.y += mOffsetY;

//                    zoomCF.x += mOffsetX;
//                    zoomCF.y += mOffsetY;

                    mOffsetX = 0;
                    mOffsetY = 0;

                    invalidateView();
                }

                mDownPoint.x = 0;
                mDownPoint.y = 0;
                break;
        }

        return true;
    }

    /**
     * 重绘View
     */
    public void invalidateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

}
