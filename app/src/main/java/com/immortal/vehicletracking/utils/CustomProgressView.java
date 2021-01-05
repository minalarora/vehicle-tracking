package com.immortal.vehicletracking.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import com.immortal.vehicletracking.R;


/**
 * Created by Amit on 10/23/2017.
 */

public class CustomProgressView extends View {

    private Context context = null;

    public CustomProgressView(Context context) {
        super(context);

        inIt(context);
    }

    public CustomProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        inIt(context);
    }

    public CustomProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inIt(context);
    }

    private Bitmap loadingCircle = null;
    private Matrix matrix = null;
    private Handler handler = null;
    private int angle = 0;
    private int refreshTime = 50;
    private Runnable runnable = null;

    private void inIt(Context context) {

        this.context = context;

        loadingCircle = ImageFactory.getBitmapFromFactory(context, R.drawable.progressbar);
        matrix = new Matrix();
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {

                invalidate();
                angle += 12;
                handler.postDelayed(runnable, refreshTime);
            }
        };
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        matrix.setRotate(angle, getWidth() / 2f, getHeight() / 2f);

        if (loadingCircle.isRecycled()) {
            loadingCircle = ImageFactory.getBitmapFromFactory(context, R.drawable.progressbar);
        }

        canvas.drawBitmap(loadingCircle, matrix, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (loadingCircle != null) {
            setMeasuredDimension(loadingCircle.getWidth(), loadingCircle.getHeight());
        }
    }

    public void startLoading() {
        handler.postDelayed(runnable, refreshTime);
    }

    public void stopLoading() {
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
