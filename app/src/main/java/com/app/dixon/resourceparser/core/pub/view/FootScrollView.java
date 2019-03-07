package com.app.dixon.resourceparser.core.pub.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;

import com.app.dixon.resourceparser.core.util.ScreenUtils;

/**
 * Created by dixon.xu on 2019/3/6.
 * <p>
 * 底部隐藏View
 */

public class FootScrollView extends CardView {

    private Statue mStatue;

    public FootScrollView(@NonNull Context context) {
        super(context);
        init();
    }

    public FootScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FootScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        post(new Runnable() {
            @Override
            public void run() {
                quickHide();
            }
        });
    }

    public void move() {
        mStatue.move();
    }

    public void alwaysHide() {
        if (mStatue == showStatue) {
            mStatue.move();
        }
    }

    private interface Statue {
        void move();
    }

    private Statue hideStatue = new Statue() {
        @Override
        public void move() {
            realShow();
        }
    };

    private Statue showStatue = new Statue() {
        @Override
        public void move() {
            realHide();
        }
    };

    private Statue runningStatue = new Statue() {
        @Override
        public void move() {

        }
    };

    private void realShow() {
        mStatue = runningStatue;
//        ObjectAnimator tranY = ObjectAnimator.ofFloat(this, "translationY", ScreenUtils.dpToPx(getContext(), 30), 0);
        ObjectAnimator tranY = ObjectAnimator.ofFloat(this, "translationY", getMeasuredHeight(), 0);
        tranY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mStatue = showStatue;
            }
        });
//        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 0.5F, 1);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.setInterpolator(new DecelerateInterpolator());
        set.playTogether(tranY);
        set.start();
    }

    private void realHide() {
        mStatue = runningStatue;
        ObjectAnimator tranY = ObjectAnimator.ofFloat(this, "translationY", 0, getMeasuredHeight());
        tranY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mStatue = hideStatue;
            }
        });
//        ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 1, 0);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(300);
        set.setInterpolator(new DecelerateInterpolator());
        set.playTogether(tranY);
        set.start();
    }

    private void quickHide() {
        ObjectAnimator.ofFloat(this, "translationY", 0, getMeasuredHeight()).setDuration(0).start();
        mStatue = hideStatue;
    }
}
