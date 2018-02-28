package com.xyc.okutils.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xyc.okutils.R;

/**
 * Created by lifesensexu on 16/8/5.
 */
public class GuProgressBarView extends RelativeLayout {
    private TextView tvCurrentTopTip;
    private ProgressBar progressBar;
    private double percent;
    private Context mContext;
    private TextView tvCurrentCenterTip;
    private boolean isShowCenter = false;
    private boolean isShowTip = true;
    private String textTip;

    public GuProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GuProgressBarView(Context context) {
        super(context);
        mContext = context;
    }

    public GuProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.progressbar_view, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initView();
    }

    private void initView() {
        tvCurrentTopTip = (TextView) findViewById((R.id.tvCurrentTopTip));
        tvCurrentCenterTip = (TextView) findViewById((R.id.tvCurrentCenterTip));
        progressBar = ((ProgressBar) findViewById(R.id.progressBar_detail));
    }

    /**
     * true 设置数值百分数显示在进度条中, false或者不设置则显示在
     * 进度条上方
     */
    public void setCurrentTextCenter(boolean isShowCenter) {
        this.isShowCenter = isShowCenter;
        if (textTip != null) {
            setCurrentText(textTip);
        }
        if (isShowCenter) {
            tvCurrentCenterTip.setVisibility(VISIBLE);
            tvCurrentTopTip.setVisibility(GONE);
        } else {
            tvCurrentTopTip.setVisibility(VISIBLE);
            tvCurrentCenterTip.setVisibility(GONE);
        }
    }

    /**
     * 设置是否显示进度条数字 true显示
     *
     * @param isShowTip
     */
    public void setShowTextTip(boolean isShowTip) {
        this.isShowTip = isShowTip;
        if (!isShowTip) {
            tvCurrentCenterTip.setVisibility(GONE);
            tvCurrentTopTip.setVisibility(GONE);
            return;
        } else {
            if (isShowCenter) {
                tvCurrentCenterTip.setVisibility(VISIBLE);
                tvCurrentTopTip.setVisibility(GONE);
            } else {
                tvCurrentCenterTip.setVisibility(GONE);
                tvCurrentTopTip.setVisibility(VISIBLE);
            }
        }
    }

    public void setCurrentText(CharSequence text) {
        textTip = text.toString();
        if (isShowCenter) {
            tvCurrentCenterTip.setText(text);
            tvCurrentCenterTip.setVisibility(VISIBLE);
            tvCurrentTopTip.setVisibility(GONE);
        } else {
            tvCurrentTopTip.setText(text);
            tvCurrentTopTip.setVisibility(VISIBLE);
            tvCurrentCenterTip.setVisibility(GONE);
        }
    }

    /**
     * 设置进度条样式
     *
     * @param drawableId
     */
    public void setCurrentProDrawable(int drawableId) {
        if (drawableId == 0) {
            return;
        }
        progressBar.setProgressDrawable(mContext.getResources().getDrawable(drawableId));
    }

    /**
     * 设置进度条百分比  例如 0.7,0.8。。。
     *
     * @param percent
     */
    public void setPercent(double percent) {
        this.percent = percent;
        int progress = (int) (percent * 100);
        if (progress >= 100) {
            progress = 100;
        }
        progressBar.setProgress(progress);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("xyc", "onMeasure: isShowCenter=" + isShowCenter);
        int textWidth;
        if (isShowCenter) {
            textWidth = tvCurrentCenterTip.getMeasuredWidth();
        } else {
            textWidth = tvCurrentTopTip.getMeasuredWidth();
        }
        int fullWidth = getMeasuredWidth();
        int progressWidthMeasured = MeasureSpec.makeMeasureSpec(fullWidth - textWidth, MeasureSpec.EXACTLY);
        int progressHeightMeasured = MeasureSpec.makeMeasureSpec(progressBar.getMeasuredHeight(), MeasureSpec.EXACTLY);
        progressBar.measure(progressWidthMeasured, progressHeightMeasured);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int textWidth;
        final int oldTop;
        final int oldBottom;
        if (isShowCenter) {
            textWidth = tvCurrentCenterTip.getMeasuredWidth();
            oldTop = tvCurrentCenterTip.getTop();
            oldBottom = tvCurrentCenterTip.getBottom();
        } else {
            textWidth = tvCurrentTopTip.getMeasuredWidth();
            oldTop = tvCurrentTopTip.getTop();
            oldBottom = tvCurrentTopTip.getBottom();
        }
        int progressBarLeft = textWidth / 2;
        int progressBarRight = progressBarLeft + progressBar.getMeasuredWidth();
        progressBar.layout(progressBarLeft, progressBar.getTop(), progressBarRight, progressBar.getBottom());

        final int width = progressBar.getMeasuredWidth();
        int percentWidth = ((int) (width * percent));
        int textLeft = percentWidth;

        if (isShowCenter) {
            int textRight = textLeft + tvCurrentCenterTip.getMeasuredWidth();
            tvCurrentCenterTip.layout(textLeft, oldTop, textRight, oldBottom);
        } else {
            int textRight = textLeft + tvCurrentTopTip.getMeasuredWidth();
            tvCurrentTopTip.layout(textLeft, oldTop, textRight, oldBottom);
        }
    }
}
