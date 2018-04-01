package ru.pakhotin.circleview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;


public class CircleView extends View {
    int arcColor = Color.RED;
    Paint mPaint;
    RectF arcRect;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, int color) {
        super(context);
        arcColor = color;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(arcColor);
        mPaint.setStrokeWidth(30);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        arcRect = new RectF(30,30,getWidth() - 30,getHeight() - 30);
        canvas.drawArc(arcRect, 10, 340, false, mPaint);
    }
}
