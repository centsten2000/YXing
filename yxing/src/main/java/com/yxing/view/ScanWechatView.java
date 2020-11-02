package com.yxing.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import androidx.annotation.Nullable;
import com.example.yxing.R;
import com.yxing.utils.SizeUtils;
import com.yxing.view.base.BaseScanView;

public class ScanWechatView extends BaseScanView {

    private int scanMaginWith;
    private int scanMaginheight;

    private Paint paint;
    private Bitmap scanLine;
    private Rect scanRect;
    private Rect lineRect;

    //扫描线位置
    private int scanLineTop;
    //透明度
    private int alpha = 100;

    public ScanWechatView(Context context) {
        super(context);
        init();
    }

    public ScanWechatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScanWechatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        scanLine = BitmapFactory.decodeResource(getResources(),
                R.drawable.scan_wechatline);

        scanRect = new Rect();
        lineRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        scanMaginWith = getWidth() / 10;
        scanMaginheight = getHeight() >> 2;
        scanRect.set(scanMaginWith, scanMaginheight, getWidth() - scanMaginWith, getHeight() - scanMaginheight);
        startAnim();
        paint.setAlpha(alpha);
        lineRect.set(scanMaginWith, scanLineTop, getWidth() - scanMaginWith, scanLineTop + SizeUtils.dp2px(getContext(), 20));
        canvas.drawBitmap(scanLine, null, lineRect, paint);
    }

    @Override
    public void startAnim() {
        if(valueAnimator == null) {
            valueAnimator = ValueAnimator.ofInt(scanRect.top, scanRect.bottom - SizeUtils.dp2px(getContext(), 20));
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator.setDuration(4000);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scanLineTop = (int) animation.getAnimatedValue();
                    int startHideHeight = (scanRect.bottom - scanRect.top) / 6;
                    alpha = scanRect.bottom - scanLineTop <= startHideHeight ? (int) (((double) (scanRect.bottom - scanLineTop) / startHideHeight) * 100) : 100;
                    postInvalidate();
                }
            });
            valueAnimator.start();
        }
    }

    @Override
    public void cancelAnim() {
        if(valueAnimator != null){
            valueAnimator.cancel();
        }
    }
}
