package me.leslie.demo;

import android.os.Handler;

import me.leslie.afv.now.BaseAfvFragment;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-02-07  11:21
 */

public class M1Fragment extends BaseAfvFragment {
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
