package com.app.dixon.resourceparser.core.pub.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.util.ScreenUtils;


/**
 * Created by dixon.xu on 2019/2/12.
 * <p>
 * 临时替代组件 宽度强数值 没有扩展性
 */

public class MenuView extends LinearLayout {

    private TextView mMenu;
    private LinearLayout mContent;
    private ItemStatue mStatue;

    public MenuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuView(@NonNull Context context) {
        super(context);
        init();
    }

    public MenuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.content_menu_view, this);

        findView();
        initView();
    }

    private void findView() {
        mMenu = findViewById(R.id.tvMenu);
        mContent = findViewById(R.id.llContent);
    }

    private void initView() {
        ObjectAnimator.ofFloat(mContent, "translationX", 0, ScreenUtils.dpToPx(getContext(), 80)).setDuration(0).start();
        mStatue = closeStatue;
        mMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatue.changeStatue();
            }
        });
    }

    private interface ItemStatue {
        void changeStatue();
    }

    private ItemStatue closeStatue = new ItemStatue() {
        //关闭状态只能打开
        @Override
        public void changeStatue() {
            mStatue = changStatue;
            ObjectAnimator animator = ObjectAnimator.ofFloat(mContent, "translationX", ScreenUtils.dpToPx(getContext(), 80), 0);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mStatue = openStatue;
                }
            });
            animator.setDuration(300).start();
        }
    };

    private ItemStatue openStatue = new ItemStatue() {
        //打开状态只能关闭
        @Override
        public void changeStatue() {
            mStatue = changStatue;
            ObjectAnimator animator = ObjectAnimator.ofFloat(mContent, "translationX", 0, ScreenUtils.dpToPx(getContext(), 80));
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mStatue = closeStatue;
                }
            });
            animator.setDuration(300).start();
        }
    };

    private ItemStatue changStatue = new ItemStatue() {
        //变化状态操作无效
        @Override
        public void changeStatue() {

        }
    };

    public static class Item extends FrameLayout {

        private TextView mTip;

        public Item(@NonNull Context context) {
            super(context);
            init();
        }

        public Item(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public Item(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            LayoutInflater.from(getContext()).inflate(R.layout.item_menu_view, this);
            findView();
        }

        private void findView() {
            mTip = findViewById(R.id.tvTip);
        }

        public void setText(String text) {
            mTip.setText(text);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            mTip.setOnClickListener(listener);
        }
    }

    public void addItem(Item item) {
        mContent.addView(item);
    }

}
