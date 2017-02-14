package com.boohee.one.radar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.baidu.location.aj;
import com.boohee.one.R;
import com.boohee.one.tinker.reporter.SampleTinkerReport;
import com.boohee.utility.DensityUtil;

public class SportLayout extends FrameLayout {
    private final int      COLUMN;
    private final int      COLUMN_WIDTH;
    private       int      MAX;
    private       String[] TAG;
    @InjectView(2131429420)
    TextView emptySport;
    private BarChart mChart;
    private Context  mContext;
    private float[]  percent;

    public SportLayout(Context context) {
        super(context);
        this.COLUMN = 7;
        this.COLUMN_WIDTH = 15;
        this.MAX = SampleTinkerReport.KEY_LOADED_PACKAGE_CHECK_SIGNATURE;
        this.percent = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        this.TAG = new String[]{"日", "一", "二", "三", "四", "五", "六"};
        init(context);
    }

    public SportLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.COLUMN = 7;
        this.COLUMN_WIDTH = 15;
        this.MAX = SampleTinkerReport.KEY_LOADED_PACKAGE_CHECK_SIGNATURE;
        this.percent = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        this.TAG = new String[]{"日", "一", "二", "三", "四", "五", "六"};
        init(context);
    }

    public SportLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.COLUMN = 7;
        this.COLUMN_WIDTH = 15;
        this.MAX = SampleTinkerReport.KEY_LOADED_PACKAGE_CHECK_SIGNATURE;
        this.percent = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        this.TAG = new String[]{"日", "一", "二", "三", "四", "五", "六"};
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.SportLayout, this, false);
        this.mChart = (BarChart) view.findViewById(R.id.chart);
        this.mChart.setColumn(7, 15);
        this.mChart.setPercent(this.percent);
        this.mChart.setLine(6);
        addView(view);
        ButterKnife.inject((View) this);
        if (!isInEditMode()) {
            postDelayed(new Runnable() {
                public void run() {
                    SportLayout.this.addTag();
                }
            }, 100);
        }
    }

    public void setTarget(int[] data) {
        if (data != null) {
            int i;
            this.emptySport.setVisibility(8);
            for (int i2 : data) {
                if (i2 > this.MAX) {
                    this.MAX = i2;
                }
            }
            if (this.MAX % 50 != 0) {
                this.MAX = ((this.MAX / 50) + 1) * 50;
            }
            this.percent = new float[7];
            for (i2 = 0; i2 < data.length; i2++) {
                this.percent[i2] = (((float) data[i2]) * 1.0f) / ((float) this.MAX);
            }
            this.mChart.setTarget(this.percent);
        }
    }

    public void startAnim() {
        this.mChart.startAnim();
    }

    public void showEmpty() {
        this.percent = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        this.mChart.setPercent(this.percent);
        this.emptySport.setVisibility(0);
        this.emptySport.setText("暂无数据");
    }

    public void showLoading() {
        this.percent = new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        this.mChart.setPercent(this.percent);
        this.emptySport.setVisibility(0);
        this.emptySport.setText("加载中");
    }

    private void addTag() {
        if (this.mChart.getStartPoint() != null) {
            int i;
            TextView tv;
            LayoutParams params;
            for (i = 0; i < 7; i++) {
                tv = (TextView) LayoutInflater.from(this.mContext).inflate(R.layout.qz, this,
                        false);
                tv.setText(this.TAG[i]);
                params = (LayoutParams) tv.getLayoutParams();
                params.leftMargin = (int) (((float) this.mChart.getLeft()) + this.mChart
                        .getStartPoint()[i]);
                params.topMargin = this.mChart.getBottom();
                tv.setLayoutParams(params);
                addView(tv);
            }
            int lengthInterval = (int) (((float) this.mChart.getHeight()) / ((float) 7));
            float interval = ((float) this.MAX) / 7.0f;
            for (i = 0; i < 8; i++) {
                tv = new TextView(this.mContext);
                int textSize = DensityUtil.dip2px(this.mContext, 9.0f);
                tv.setTextSize(9.0f);
                tv.setTextColor(-2143183879);
                params = new LayoutParams(-2, -2);
                params.gravity = 85;
                params.rightMargin = (getWidth() - this.mChart.getLeft()) + DensityUtil.dip2px
                        (this.mContext, aj.hA);
                params.bottomMargin = (int) (((float) ((getHeight() - this.mChart.getBottom()) +
                        (lengthInterval * i))) - (((float) textSize) / 2.0f));
                tv.setLayoutParams(params);
                tv.setText(String.valueOf(Math.round(((float) i) * interval)));
                addView(tv);
            }
        }
    }
}
