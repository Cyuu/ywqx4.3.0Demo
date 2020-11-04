package com.thdz.ywqx.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.thdz.ywqx.R;

/**
 * 用于抠图的imageview
 * com.thdz.ywqx.view.CutImageView
 */

public class CutImageView extends ImageView {

    private final String TAG = "CutImageView";
    private float mOldX;
    private float mOldY;
    private float mCurrentX;
    private float mCurrentY;
    private Canvas mCanvas;

    private boolean left2Right = true;

    private boolean top2Bottom = true;

    public CutImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.mCanvas = canvas;
        super.onDraw(canvas);
        drawFrame(canvas);
    }

    /**
     * 绘制选择范围的边框
     */
    private void drawFrame(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.trans_black));
        paint.setStyle(Paint.Style.FILL); // STROKE
        paint.setStrokeWidth(2.0f); // 线宽--画线必须要设置线宽

        // 根据两个坐标，计算l t r b
        if (left2Right && top2Bottom) {
            canvas.drawRect(mOldX, mOldY, mCurrentX, mCurrentY, paint);
        } else if (left2Right && !top2Bottom) {
            canvas.drawRect(mOldX, mCurrentY, mCurrentX, mOldY, paint);
        } else if (!left2Right && top2Bottom) {
            canvas.drawRect(mCurrentX, mOldY, mOldX, mCurrentY, paint);
        } else {
            canvas.drawRect(mCurrentX, mCurrentY, mOldX, mOldY, paint);
        }

    }


    /**
     * 重写触摸事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            // 按下
            case MotionEvent.ACTION_DOWN:
                mOldX = event.getX();
                mOldY = event.getY();
                break;
            // 移动,根据坐标判断移动方向（偏移量是需要定义dimen，这里为了方便）
            case MotionEvent.ACTION_MOVE:
                mCurrentX = event.getX();
                mCurrentY = event.getY();
                // 从右往左画
                if (mCurrentX < mOldX ) {
                    left2Right = false;
                } else {
                    left2Right = true;
                }

                // 从下往上画
                if (mCurrentY < mOldY) {
                    top2Bottom = false;
                } else {
                    top2Bottom = true;
                }

                this.invalidate();
                break;
            // 释放
            case MotionEvent.ACTION_UP:
//                mMoveListener.createImg();
                Log.e(TAG, "mOldX, mOldY, mCurrentX, mCurrentY = " + mOldX + ", " + mOldY + ", " + mCurrentX + ", " + mCurrentY);
                if (mCutterListener != null) {
                    if (left2Right && top2Bottom) {
                        mCutterListener.getSelectLocation(mOldX, mOldY, mCurrentX, mCurrentY);
                    } else if (left2Right && !top2Bottom) {
                        mCutterListener.getSelectLocation(mOldX, mCurrentY, mCurrentX, mOldY);
                    } else if (!left2Right && top2Bottom) {
                        mCutterListener.getSelectLocation(mCurrentX, mOldY, mOldX, mCurrentY);
                    } else {
                        mCutterListener.getSelectLocation(mCurrentX, mCurrentY, mOldX, mOldY);
                    }
                }

                break;
        }
        return true;

    }


    private OnCutterPicListener mCutterListener;

    public void setOnCutterPicture(OnCutterPicListener listener) {
        mCutterListener = listener;
    }

    /*
    *左右滑动点击的回调接口
    */
    public interface OnCutterPicListener {
        void getSelectLocation(float mOldX, float mOldY, float mCurrentX, float mCurrentY);
    }

}
