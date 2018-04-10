package project.dajver.com.longclickbuttonview.view;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import project.dajver.com.longclickbuttonview.R;

public class ButtonView extends FrameLayout {

    public static final long HOLD_TIME_MS = 1800L;

    @BindView(R.id.background)
    ButtonBackgroundView mBackground;

    private Animator mLongPressAnimator;
    private Rect mBoundsRectangle;
    private Handler mHandler;
    private OnButtonLongPressReachedEndListener listener;

    public ButtonView(Context context) {
        super(context);
        init();
    }

    public ButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_long_press_button, this);
        ButterKnife.bind(this);

        mBoundsRectangle = new Rect();
        mHandler = new Handler();
    }

    private Runnable mOnLongPressed = new Runnable() {
        public void run() {
            if (mLongPressAnimator != null) {
                mLongPressAnimator.cancel();
                listener.onButtonLongPressReachedEnd();
            }
        }
    };

    @OnTouch(R.id.hold_to_end_button)
    boolean onTouchPulsingButton(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mBoundsRectangle.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());

                mLongPressAnimator = mBackground.getWipeAnimator(HOLD_TIME_MS);
                mLongPressAnimator.start();
                mHandler.postDelayed(mOnLongPressed, HOLD_TIME_MS);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mBoundsRectangle.contains(view.getLeft() + (int) event.getX(), view.getTop() + (int) event.getY())) {
                    break;
                }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                mHandler.removeCallbacks(mOnLongPressed);
                mLongPressAnimator.cancel();
                break;
        }
        return true;
    }

    public void setOnButtonLongPressReachedEndListener(OnButtonLongPressReachedEndListener listener) {
        this.listener = listener;
    }

    public interface OnButtonLongPressReachedEndListener {
        void onButtonLongPressReachedEnd();
    }
}