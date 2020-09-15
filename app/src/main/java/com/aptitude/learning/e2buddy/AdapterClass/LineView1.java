package com.aptitude.learning.e2buddy.AdapterClass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.aptitude.learning.e2buddy.Util.Utils;


public class LineView1 extends View {

    public static final float LINE_WIDTH = 80.0f;
    public Paint paint = new Paint();
    protected Context context;

    public float startingX, startingY, endingX, endingY;

    public LineView1(Context context) {
        super(context);
        this.context = context;

        paint.setColor(Color.parseColor("#B93030"));
            paint.setStrokeWidth(LINE_WIDTH);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setDither(true);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(230);

    }

    public void setPoints(float startX, float startY, float endX, float endY) {

        startingX = startX;
        startingY = startY;
        endingX = endX;
        endingY = endY;
       // answerStatus = status;
        invalidate();
    }

    @Override
    public void onDraw(final Canvas canvas) {
        Log.e("LINEVIEW", "startingX" + startingX + "  startingY:" + startingY);
        Log.e("LINEVIEW", "endingX" + endingX + "  endingY:" + endingY);

        canvas.drawLine(startingX, startingY, endingX, endingY, paint);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                paint = new Paint();
                paint.setColor(0x00000000);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));

                invalidate();
            }
        }, 400);

    }
}
