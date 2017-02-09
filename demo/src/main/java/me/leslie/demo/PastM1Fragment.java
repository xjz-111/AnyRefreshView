package me.leslie.demo;

import android.os.Handler;

import me.leslie.afv.past.BasePastAfvFragment;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-02-07  18:11
 */

public class PastM1Fragment extends BasePastAfvFragment {
    private Handler handler = new Handler();

    @Override
    protected int getLayoutId() {
        return R.layout.m1_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onLoad() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoadFinish();
            }
        }, 2000);
    }
}
