package me.leslie.afv.base;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-02-09  10:14
 */

public abstract class BaseAnyRefreshViewFragment extends Fragment{
    /**
     * 获取要嵌套的内部布局
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化自己的View
     */
    protected abstract void initView();

    /**
     * 是否可刷新，默认实现
     * @return
     */
    public abstract boolean isRefreshEnable();

    /**
     * 获取View
     * @return
     */
    protected abstract View getLayout();

    /**
     * 自动刷新
     */
    public abstract void onAutoRefresh();

    /**
     * 页面所有请求完成后调用
     */
    public abstract void onLoadFinish();

}
