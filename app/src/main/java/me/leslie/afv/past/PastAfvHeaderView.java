package me.leslie.afv.past;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import me.leslie.afv.R;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2014-10-11  17:19
 */

public class PastAfvHeaderView extends LinearLayout {
    private LinearLayout layout;                           //布局
    private ProgressBar progressBar;                       //滚动条
    private TextView hintText;                             //显示状态
    public final static int STATUS_NORMAL = 0;             //正常状态
    public final static int STATUS_READY = 1;              //装备状态
    public final static int STATUS_REFRESHING = 2;         //刷新状态
    public final static int STATUS_INIT = 3;
    private int status;                                    //当前状态

    public PastAfvHeaderView(Context context) {
        super(context);
        init();
    }

    public PastAfvHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PastAfvHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PastAfvHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        status = STATUS_NORMAL;
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.past_afv_header_view, this);
        layout.setLayoutParams(lp);
        setGravity(Gravity.BOTTOM);
        hintText = (TextView) findViewById(R.id.header_hint_textview);
        progressBar = (ProgressBar) findViewById(R.id.app_progressbar);
    }

    /**
     * 设置状态
     *
     * @param mStatus
     */
    public void setStatus(int mStatus) {
        if (this.status == mStatus) {
            return;
        }
        if (mStatus == STATUS_REFRESHING) {          //显示滚动条
            progressBar.setVisibility(View.VISIBLE);
        } else {                                       //显示箭头
            progressBar.setVisibility(View.INVISIBLE);
        }
        switch (mStatus) {
            case STATUS_NORMAL:
                setVisibility(View.VISIBLE);
                if (STATUS_READY == status) {
                    hintText.setText("下拉刷新");
                } else if (STATUS_REFRESHING == status) {
                    setRight("下拉刷新", true);
                }
                break;
            case STATUS_READY:
                setVisibility(View.VISIBLE);
                if (STATUS_READY != status) {
                    setRight("松手后刷新", true);
                }
                break;
            case STATUS_REFRESHING:
                setVisibility(View.VISIBLE);
                setRight("正在刷新", false);
                break;
            case STATUS_INIT:
                setVisibility(View.GONE);
                setVisibleHeight(0);
            default:
                break;
        }
        this.status = mStatus;
    }


    private void setRight(String s, boolean visible) {
        if (null != hintText && !TextUtils.isEmpty(s) && null != progressBar) {
            hintText.setText(s);
            if (visible) {
                hintText.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                hintText.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setVisibleHeight(int height) {
        if (height < 0) {
            height = 0;
        }
        LayoutParams lp = (LayoutParams) layout.getLayoutParams();
        lp.height = height;
        layout.setLayoutParams(lp);
    }

    public int getVisibleHeight() {
        return null != layout ? layout.getHeight() : 0;
    }
}
