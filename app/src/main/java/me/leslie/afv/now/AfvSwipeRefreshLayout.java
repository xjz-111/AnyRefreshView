package me.leslie.afv.now;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-02-07  10:38
 */

public class AfvSwipeRefreshLayout extends SwipeRefreshLayout {
    private float downX;
    private float downY;
    private int touchSlop;

    public AfvSwipeRefreshLayout(Context context) {
        super(context);
        init();
    }

    public AfvSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float moveX = ev.getX();
                final float moveY = ev.getY();
                final float diffX = Math.abs(moveX - downX);
                final float diffY = Math.abs(moveY - downY);
                if (diffX > touchSlop && diffY > touchSlop && diffX > diffY){
                    return false;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void bringToFront() {
//        super.bringToFront();
    }
}
