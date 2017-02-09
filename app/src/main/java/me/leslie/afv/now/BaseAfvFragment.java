package me.leslie.afv.now;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import me.leslie.afv.base.BaseAnyRefreshViewFragment;
import me.leslie.afv.base.OnLoadListener;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-02-07  10:31
 */

public abstract class BaseAfvFragment extends BaseAnyRefreshViewFragment implements OnLoadListener, SwipeRefreshLayout.OnRefreshListener, OnAfvScrollListener{
    protected SwipeRefreshLayout refreshLayout;
    protected AfvScrollView scrollView;
    private State state = State.Top;


    @Override
    public boolean isRefreshEnable() {
        return true;
    }

    @Override
    protected View getLayout() {
        return null;
    }

    @Override
    public void onAutoRefresh(){
        if (isRefreshEnable() && null != refreshLayout && !refreshLayout.isRefreshing()){
            refreshLayout.post(truRunnable);
            onLoad();
        }
    }

    @Override
    public void onLoadFinish(){
        if (null != refreshLayout){
            if (State.Top == state){
                setEnable(true);
            }
            //在ViewPager中使用可能出现falseRunnable先与trueRunnable，特此处理
            refreshLayout.postDelayed(falseRunnable, 20);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        refreshLayout = new AfvSwipeRefreshLayout(getContext());
        scrollView = new AfvScrollView(getContext());
        refreshLayout.addView(scrollView, new SwipeRefreshLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (getLayoutId() > 0) {
            LayoutInflater.from(getContext()).inflate(getLayoutId(), scrollView, true);
        }else if (null != getLayout()){
            scrollView.addView(getLayout(), new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        if (isRefreshEnable()){
            refreshLayout.setOnRefreshListener(this);
        }
        scrollView.setOnAfvScrollListener(this);
        refreshLayout.setEnabled(isRefreshEnable());
        initView();
        onAutoRefresh();
        return refreshLayout;

    }

    @Override
    public void onScroll2Top() {
        state = State.Top;
        setEnable(true);
    }

    @Override
    public void onScroll2Bottom() {
        state = State.Bottom;
        setEnable(false);
    }

    @Override
    public void onScroll2Center() {
        state = State.None;
        setEnable(false);
    }

    @Override
    public void onRefresh() {
        onLoad();
    }

    private void setEnable(boolean enable){
        if (null != refreshLayout){
            refreshLayout.setEnabled(enable);
        }
    }

    private final Runnable truRunnable = new Runnable() {
        @Override
        public void run() {
            refreshLayout.setRefreshing(true);
        }
    };

    private final Runnable falseRunnable = new Runnable() {
        @Override
        public void run() {
            refreshLayout.setRefreshing(false);
        }
    };
}
