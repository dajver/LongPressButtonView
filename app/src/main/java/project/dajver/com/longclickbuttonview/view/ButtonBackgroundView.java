package project.dajver.com.longclickbuttonview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import project.dajver.com.longclickbuttonview.R;

public class ButtonBackgroundView extends View {

    private Paint mSolidPaint;
    private RectF mSolidRect;
    private float mSolidMultiplier;
    private float mClipMultiplier;

    private Paint mStrokePaint;
    private RectF mStrokeRect;
    private float mStrokeMultiplier;

    private Paint mWipePaint;

    public ButtonBackgroundView(Context context) {
        super(context);
        init();
    }

    public ButtonBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSolidPaint = new Paint();
        mSolidPaint.setColor(getResources().getColor(R.color.colorPrimary));
        mSolidPaint.setAntiAlias(true);

        mStrokePaint = new Paint();
        mStrokePaint.setColor(getResources().getColor(R.color.colorPrimary));
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(getResources().getDimension(R.dimen.ring_width));
        mStrokePaint.setAntiAlias(true);

        mWipePaint = new Paint();
        mWipePaint.setColor(Color.WHITE);
        mWipePaint.setAlpha(51);
        mWipePaint.setAntiAlias(true);

        mSolidRect = new RectF();
        mStrokeRect = new RectF();

        resetAnimatedValues();
    }

    private void resetAnimatedValues() {
        mSolidMultiplier = 1.0f;
        mStrokeMultiplier = 1.0f;
        mStrokePaint.setAlpha(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        float halfWidth = width / 2.0f;
        float adjustedHalfWidth = halfWidth * 0.78260869565217f;
        float solidHalfWidth = adjustedHalfWidth * mSolidMultiplier;
        float strokeHalfWidth = adjustedHalfWidth * mStrokeMultiplier;
        float strokeDifference = strokeHalfWidth - solidHalfWidth;

        int height = canvas.getHeight();
        float halfHeight = height / 2.0f;
        float solidHalfHeight = halfHeight * mSolidMultiplier * 0.46808510638298f;
        float strokeHeightDifference = solidHalfHeight + strokeDifference;

        canvas.drawARGB(0, 0, 0, 0);

        mStrokeRect.set(halfWidth - strokeHalfWidth, halfHeight - strokeHeightDifference, halfWidth + strokeHalfWidth, halfHeight + strokeHeightDifference);
        canvas.drawRoundRect(mStrokeRect, strokeHeightDifference, strokeHeightDifference, mStrokePaint);

        mSolidRect.set(halfWidth - solidHalfWidth, halfHeight - solidHalfHeight, halfWidth + solidHalfWidth, halfHeight + solidHalfHeight);
        canvas.drawRoundRect(mSolidRect, solidHalfHeight, solidHalfHeight, mSolidPaint);

        float clipStart = halfWidth - solidHalfWidth;
        float clipEnd = clipStart + (solidHalfWidth * 2.0f) * mClipMultiplier;
        canvas.clipRect(clipStart, 0, clipEnd, height);
        canvas.drawRoundRect(mSolidRect, solidHalfHeight, solidHalfHeight, mWipePaint);
    }

    public Animator getWipeAnimator(long animationTimeMs) {
        ValueAnimator wipeAnimator = ValueAnimator.ofFloat(0.0f, 1.0f).setDuration(animationTimeMs);
        wipeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mClipMultiplier = valueAnimator.getAnimatedFraction();
                invalidate();
            }
        });
        wipeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                mClipMultiplier = 0f;
                invalidate();
            }
        });
        return wipeAnimator;
    }
}