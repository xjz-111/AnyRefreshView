package me.leslie.afv.past;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MotionEventCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import me.leslie.afv.base.OnLoadListener;
import me.leslie.afv.R;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-02-07  17:09
 */

public class PastAfvScrollView extends ScrollView {
    private final String TAG = "PastAfvScrollView";
    private float xDistance, yDistance, xLast, yLast;
    private TextView headerTime;
    private Scroller scroller;
    private int headerHeight;
    private LinearLayout content;
    private float lastY = -1;
    private long refreshTime;
    private String timeTag = TAG;
    private boolean isRefreshing;
    private boolean isRefreshEnable;
    private PastAfvScrollView scrollView;
    private PastAfvHeaderView headerView;
    private View headerLayout;
    private final int SCROLL_DURATION = 400;
    private final float OFFSET_RADIO = 1.8F;
    private final int LAST_REFRESH_TIME = 1000;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private OnLoadListener onLoadListener;
    private OnPastScrollListener onScrollListener;

    public void setOnLoadListener(OnLoadListener l) {
        this.onLoadListener = l;
    }

    public void setOnScrollListener(OnPastScrollListener l) {
        this.onScrollListener = l;
    }

    public PastAfvScrollView(Context context) {
        super(context);
        init();
    }

    public PastAfvScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PastAfvScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PastAfvScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void onAutoPullDown() {
        String last = getLastRefreshTime();
        headerTime.setText(last);
        isRefreshing = true;
        headerView.setStatus(PastAfvHeaderView.STATUS_REFRESHING);
        if (onLoadListener != null) {
            refreshTime = System.currentTimeMillis();
            onLoadListener.onLoad();
        }
        scroller.startScroll(0, 0, 0, headerHeight, 1);
        invalidate();
    }

    public void stopRefresh(final boolean success) {
        if (isRefreshing) {
            isRefreshing = false;
            long times = System.currentTimeMillis() - refreshTime;
            if (times >= LAST_REFRESH_TIME) {
                resetHeaderHeight();
                if (success) {
                    onRefreshTime();
                }
            } else {
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resetHeaderHeight();
                        if (success) {
                            onRefreshTime();
                        }
                    }
                }, LAST_REFRESH_TIME - times);
            }
        }
    }


    private void init(){
        scroller = new Scroller(getContext(), new DecelerateInterpolator());
        headerView = new PastAfvHeaderView(getContext());
        headerLayout = headerView.findViewById(R.id.header_layout);
        headerTime = (TextView) headerView.findViewById(R.id.header_time);
        headerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                headerHeight = headerLayout.getHeight();
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        content = new LinearLayout(getContext());
        content.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        content.addView(headerView, layoutParams);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        content.setLayoutParams(params);
        setEnableRefresh(true);
        onRefreshTime();
        updateHeaderHeight(0);
    }

    public void setEnableRefresh(boolean enable) {
        this.isRefreshEnable = enable;
        if (null != headerLayout) {
            if (!enable) {
                headerLayout.setVisibility(View.GONE);
            } else {
                headerLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        content.addView(child, params);
        ViewGroup group = (ViewGroup) content.getParent();
        if (group != null) {
            group.removeView(content);
        }
        super.addView(content);
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            headerView.setVisibleHeight(scroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();
    }

    private void resetHeaderHeight() {
        int height = headerView.getVisibleHeight();
        int finalHeight = 0;
        if (isRefreshing && height > headerHeight) {
            finalHeight = headerHeight;
        }
        if (!scroller.isFinished()) {
            scroller.abortAnimation();
        }
        scroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);
        invalidate();
    }

    private void updateHeaderHeight(float delta) {
        headerView.setVisibleHeight((int) delta + headerView.getVisibleHeight());
        if (isRefreshEnable && !isRefreshing) {
            if (headerView.getVisibleHeight() >= headerHeight) {
                headerView.setStatus(PastAfvHeaderView.STATUS_READY);
            } else {
                headerView.setStatus(PastAfvHeaderView.STATUS_NORMAL);
            }
        }
    }

    private void onRefreshTime(){
        setTime();
        if (null != headerTime){
            headerTime.setText(getLastRefreshTime());
        }
    }

    private String getLastRefreshTime() {
        long lastRefreshTime = getTime();
        String time = simpleDateFormat.format(lastRefreshTime);
        if(TextUtils.isEmpty(time)){
            time = simpleDateFormat.format(System.currentTimeMillis());
        }
        return time;
    }

    private long getTime(){
        return getContext().getSharedPreferences(timeTag, Context.MODE_PRIVATE).getLong(timeTag, System.currentTimeMillis());
    }

    private void setTime(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(timeTag, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(timeTag, System.currentTimeMillis());
        editor.apply();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (lastY == -1) {
            lastY = ev.getRawY();
        }
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - lastY;
                lastY = ev.getRawY();
                if (getScrollY() ==0 && headerView != null && (headerView.getVisibleHeight() > 0 || deltaY > 0)) {
                    if (isRefreshEnable) {
                        updateHeaderHeight(deltaY / OFFSET_RADIO);
                    }
                    if (null != onScrollListener){
                        onScrollListener.onScroll(deltaY / OFFSET_RADIO);
                    }
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    ev.setLocation(-1,-1);
                }
                break;
            default:
                lastY = -1;
                if (getScrollY() < 5 && headerView != null) {
                    if (isRefreshEnable && headerView.getVisibleHeight() >= headerHeight) {
                        isRefreshing = true;
                        headerView.setStatus(PastAfvHeaderView.STATUS_REFRESHING);
                        if (null != onLoadListener) {
                            refreshTime = System.currentTimeMillis();
                            onLoadListener.onLoad();
                        }
                    }
                    resetHeaderHeight();
                    if (null != onScrollListener){
                        onScrollListener.dissScroll();
                        invalidate();
                    }
                }
                break;
        }
        try {
            return super.onTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                // 计算在X和Y方向的偏移量
                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                // 横向滑动大于纵向滑动时不截断事件
                if (xDistance > yDistance && yDistance <= 50) {
                    return false;
                }
                else if (yDistance > xDistance && yDistance > 50) {
                    return true;
                }
                break;
            default:
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }
}
