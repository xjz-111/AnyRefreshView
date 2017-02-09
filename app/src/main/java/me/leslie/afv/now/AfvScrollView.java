package me.leslie.afv.now;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-02-07  10:33
 */

public class AfvScrollView extends ScrollView {
    private int downX;
    private int downY;
    private int touchSlop;
    private boolean isTop = true;
    private boolean isBottom;
    private OnAfvScrollListener onAfvScrollListener;

    public void setOnAfvScrollListener(OnAfvScrollListener l) {
        this.onAfvScrollListener = l;
    }

    public AfvScrollView(Context context) {
        super(context);
        init();
    }

    public AfvScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AfvScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AfvScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final int moveY = (int) ev.getRawY();
                if (Math.abs(downY - moveY) > touchSlop){
                    return true;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (0 == scrollY){
            isTop = true;
            isBottom = false;
        }else {
            isTop = false;
            isBottom = true;
        }
        onNotifyOnAfvScrollListener();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (Build.VERSION.SDK_INT < 9){
            if (0 == getScrollY()){
                isTop = true;
                isBottom = false;
            }else if (getScrollY() + getHeight() - getPaddingTop() - getPaddingBottom() == getChildAt(0).getHeight()){
                isBottom = true;
                isTop = false;
            }else {
                isTop = isBottom = false;
            }
            onNotifyOnAfvScrollListener();
        }
    }

    private void onNotifyOnAfvScrollListener(){
        if (null != onAfvScrollListener){
            if (isTop){
                onAfvScrollListener.onScroll2Top();
            }else if (isBottom){
                onAfvScrollListener.onScroll2Bottom();
            }
        }
    }

    public boolean isTop() {
        return isTop;
    }

    public boolean isBottom() {
        return isBottom;
    }
}
