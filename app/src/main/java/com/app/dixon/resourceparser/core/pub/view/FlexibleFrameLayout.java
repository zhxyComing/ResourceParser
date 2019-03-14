package com.app.dixon.resourceparser.core.pub.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.app.dixon.resourceparser.core.util.AnimationUtils;

/**
 * Created by dixon.xu on 2019/3/14.
 */

public class FlexibleFrameLayout extends FrameLayout {

    private Statue mStatue;
    private int mOriginHeight;
    private OnMoveListener mListener;

    public FlexibleFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        post(new Runnable() {
            @Override
            public void run() {
                mOriginHeight = getMeasuredHeight();
                alwaysHide();
            }
        });
        mStatue = hideStatue;
    }

    public FlexibleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlexibleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void move() {
        if (getVisibility() == GONE) {
            setVisibility(VISIBLE);
        }
        mStatue.move();
    }

    public boolean isShow() {
        return mStatue == showStatue;
    }

    private interface Statue {
        void move();
    }

    public void alwaysHide() {
        mStatue = hideStatue;
        AnimationUtils.height(FlexibleFrameLayout.this, mOriginHeight, 0, 0, null, null).start();
    }

    private Statue showStatue = new Statue() {
        @Override
        public void move() {
            mStatue = runningStatue;
            if (mListener != null) {
                mListener.onHide();
            }
            AnimationUtils.height(FlexibleFrameLayout.this, mOriginHeight, 0, 300, new DecelerateInterpolator(), new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mStatue = hideStatue;
                }
            }).start();
        }
    };

    private Statue hideStatue = new Statue() {
        @Override
        public void move() {
            mStatue = runningStatue;
            if (mListener != null) {
                mListener.onShow();
            }
            AnimationUtils.height(FlexibleFrameLayout.this, 0, mOriginHeight, 300, new DecelerateInterpolator(), new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mStatue = showStatue;
                }
            }).start();
        }
    };

    private Statue runningStatue = new Statue() {
        @Override
        public void move() {

        }
    };

    public interface OnMoveListener {
        void onShow();

        void onHide();
    }

    public void setOnMoveListener(OnMoveListener listener) {
        this.mListener = listener;
    }
}
