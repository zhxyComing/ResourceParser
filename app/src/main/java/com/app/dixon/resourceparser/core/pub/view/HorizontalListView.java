package com.app.dixon.resourceparser.core.pub.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;

import java.util.List;

/**
 * Created by dixon.xu on 2018/5/28.
 * <p>
 * 等长横向ListView 后一个item微露出 且滑动只能滑向相邻item
 */

public class HorizontalListView extends LinearLayout {

    private Scroller mScroller;//用于松手后自动滑动
    private VelocityTracker mVelocityTracker;//速度监听 超多限定速度则滚动到相邻item
    private int mPointerId;

    private int offsetX; //默认左右边距为24dp 为了实现第二个item漏出一角 且第一个item居中的效果 初始化第一个item必须偏移一定的距离
    private int mMaxVelocity; //最大滑动速度
    private int mCurrentIndex; //当前item

    private float itemScale = 0.92f; //item被点击时缩小或放大的比例
    private int mMinVelocity = 1000; //最小切换item的速度判定 只要松手时速度>mMinVelocity 就切换到相邻item

    public HorizontalListView(Context context) {
        super(context);
        init();
    }

    public HorizontalListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void setMargin(int margin) {
        this.margin = margin;
        offsetX = -dpToPx(getContext(), margin);
    }

//    private int margin = 24;
    private int margin = 0;

    private void init() {
        this.setOrientation(LinearLayout.HORIZONTAL);
        offsetX = -dpToPx(getContext(), margin);
        mScroller = new Scroller(getContext());
        mMaxVelocity = ViewConfiguration.getMaximumFlingVelocity();//最大滑动速度
    }

    /**
     * 滚动到指定item
     */
    public void moveToIndex(int index) {
        if (!canMove(index)) {
            return;
        }
        mScroller.startScroll(
                getScrollX(), getScrollY(),
                index * getChildAt(0).getWidth() - getScrollX() + offsetX, getScrollY());
        invalidate();
        mCurrentIndex = index;
        if (onItemChangedListener != null) {
            onItemChangedListener.onChanged(mCurrentIndex);
        }
        if (mOnScrollListener != null) mOnScrollListener.onScroll(mCurrentIndex);
    }

    private boolean canMove(int index) {
        if (index < 0 || index >= getChildCount()) {
            return false;
        }
        return true;
    }

    public int getCount() {
        return getChildCount();
    }

    /*
    //该方法因没有实际用途未测试
    public void stopMove() {
        if (!mScroller.isFinished()) {
            int currentX = mScroller.getCurrX();
            int targetIndex = (currentX + getWidth() / 2) / getWidth();
            mScroller.abortAnimation();
            this.scrollTo(targetIndex * getWidth(), 0);
            mCurrentIndex = targetIndex;
        }
    }
    */
    @Override
    public void computeScroll() {
        boolean isNotFinished = mScroller.computeScrollOffset();
        if (isNotFinished) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    /**
     * 核心方法 添加子view
     * <p>
     * 为了方便控制宽度等信息 子View先添加到FrameLayout中 该frameLayout作为子View添加到HorListView里
     */
    public void addChild(View view) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.addView(view);
        addView(frameLayout);

        //设定子view为屏幕宽度
        ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();
        layoutParams.width = getDisplayWidth(getContext()) - dpToPx(getContext(), margin * 2);
        frameLayout.setLayoutParams(layoutParams);
    }

    public void addChild(List<View> childList) {

        for (View view : childList) {
            addChild(view);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(final OnItemClickListener listener) {

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            final int finalI = i;
            childView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startClickAnim(v);
                    listener.onClick(finalI);
                }
            });
        }
    }

    //item被点击的动画
    private void startClickAnim(final View childView) {
        ValueAnimator animator = ValueAnimator.ofFloat(1, itemScale, 1);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                childView.setScaleX(value);
                childView.setScaleY(value);
            }
        });
        animator.setInterpolator(new OvershootInterpolator());
        animator.setDuration(300);
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int childCount = getChildCount();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view.getVisibility() == View.GONE) {
                continue;
            }

            measureChild(view, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams p = (MarginLayoutParams) view.getLayoutParams();
            width = Math.max(width, view.getMeasuredWidth() + p.leftMargin + p.rightMargin);
            height += view.getMeasuredHeight() + p.topMargin + p.bottomMargin;
        }

        // 如果mode是EXACTLY， 则设置为父布局传过来的值
        // 如果是AT_MOST， 则设置为自己测量的结果
        setMeasuredDimension(widthMode == MeasureSpec.EXACTLY ? widthSize
                : width, heightMode == MeasureSpec.EXACTLY ? heightSize
                : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        scrollTo(offsetX, 0); //布局结束后滑动偏移量
    }

    private float lastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                lastX = event.getRawX();

                mPointerId = event.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE:

                if (lastX == 0) {

                    lastX = event.getRawX();

                    mPointerId = event.getPointerId(0);
                }

                float nowX = event.getRawX();
                float disX = nowX - lastX;

                //左边界滑动 disX为正数  右边界滑动 dixX为负数
                if (getScrollX() - disX <= offsetX) {
                    scrollTo(offsetX, 0);
                } else if (getScrollX() - disX >= getMaxScrollWidth()) {
                    scrollTo(getMaxScrollWidth(), 0);
                } else {
                    scrollBy(-(int) disX, 0);
                }

                lastX = nowX;

                break;
            case MotionEvent.ACTION_UP:
                actionUpEvent();
                break;

            case MotionEvent.ACTION_CANCEL:
                actionUpEvent();
                break;
        }
        return true;
    }

    private void actionUpEvent() {

        mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
        final float velocityX = mVelocityTracker.getXVelocity(mPointerId);

        releaseVelocityTracker();

        //判定第几个item：滑动的距离/单个item宽度
        int i = getRelScrollX() / getItemWidth();
        if (velocityX > mMinVelocity) {
            moveToIndex(i);
        } else if (velocityX < -mMinVelocity) {
            moveToIndex(i + 1);
        } else if (getRelScrollX() % getItemWidth() > getItemWidth() / 2) {
            moveToIndex(i + 1);
        } else {
            moveToIndex(i);
        }

        lastX = 0;
    }

    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    //获取item的宽度 如第一个item的宽度应该为父组件的宽度-（左偏移 + 右item露出）
    public int getItemWidth() {
        return getMeasuredWidth() - Math.abs(offsetX * 2);
    }

    //获取实际的ScrollX的距离总长 因为初始化时偏移了-24dp 即后续滑动100dp时 得到的ScrollX为76dp ScrollX是X方向的坐标 并非滑动的距离！
    public int getRelScrollX() {
        return getScrollX() - offsetX;
    }

    //最大滑动宽度应该为子组件的宽度之和-1 （假如子组件等宽）
    private int getMaxScrollWidth() {
        int width = 0;
        for (int i = 0; i < getChildCount() - 1; i++) {
            View child = getChildAt(i);
            width += getChildAt(i).getMeasuredWidth();
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            width = width + layoutParams.leftMargin + layoutParams.rightMargin;
        }
        return width + offsetX;
    }


    //用于父组件滑动时抢夺事件
    private float interLastX;
    private float interLastY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:

                interLastX = ev.getRawX();
                interLastY = ev.getRawY();

                break;
            case MotionEvent.ACTION_MOVE:

                float nowX = ev.getRawX();
                float disX = nowX - interLastX;

                float nowY = ev.getRawY();
                float disY = nowY - interLastY;

                if (disX < -5 || disX > 5 || disY < -5 || disY > 5) {
                    interLastX = nowX;
                    interLastY = nowY;
                    return true;
                }

                interLastX = nowX;
                interLastY = nowY;
                break;
            case MotionEvent.ACTION_UP:

                interLastX = 0;
                interLastY = 0;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private static int dpToPx(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private static int pxToDp(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp / scale + 0.5f);
    }

    private static int getDisplayWidth(Context context) {
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        return display.widthPixels;
    }


    private OnScrollListener mOnScrollListener;

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    public interface OnScrollListener {
        void onScroll(int pos);
    }

    private OnItemChangedListener onItemChangedListener;

    public void setOnItemChangedListener(OnItemChangedListener onItemChangedListener) {
        this.onItemChangedListener = onItemChangedListener;
    }

    public interface OnItemChangedListener {
        void onChanged(int pos);
    }
}
