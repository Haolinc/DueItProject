package com.example.duelt;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Color;
import androidx.annotation.Nullable;


public class StatesView extends View {
    private int color = Color.GREEN;
    private float maxCount = 100, currentCount = 90;
    private Paint mPaint;
    private int mWidth,mHeight;
    private boolean isLiedown = true;

    public void setLiedown(boolean liedown) {
        isLiedown = liedown;
    }

    public StatesView(Context context) {
        super(context);
        init(null);
    }

    public StatesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public StatesView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs){
    }


    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        float ratio = currentCount/maxCount;

            int round = mHeight/2;
            mPaint.setColor(Color.BLACK);
            RectF rectInterBg = new RectF(0,0, mWidth,mHeight);
            canvas.drawRoundRect(rectInterBg,round,round,mPaint);
            mPaint.setColor(Color.WHITE);
            RectF rectOuterBg = new RectF(2,2, mWidth-2, mHeight-2);
            canvas.drawRoundRect(rectOuterBg,round,round,mPaint);
            RectF rectProgressBg;
            if(isLiedown)
                rectProgressBg = new RectF(3, 3, (mWidth-3)*ratio, mHeight-3);
            else
                rectProgressBg = new RectF(3, 3+mHeight*(1-ratio), mWidth-3, (mHeight-3));

        if(ratio <= 0.01f) mPaint.setColor(Color.TRANSPARENT);
            else mPaint.setColor(color);
            canvas.drawRoundRect(rectProgressBg, round, round, mPaint);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 0;
        }
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = 0;
        } else {
            mHeight = heightSpecSize;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }

    public void setCurrentCount(float currentCount) {
        this.currentCount = currentCount;
        invalidate();
    }
}
