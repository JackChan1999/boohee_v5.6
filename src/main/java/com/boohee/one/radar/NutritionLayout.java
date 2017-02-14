package com.boohee.one.radar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.boohee.one.R;

public class NutritionLayout extends FrameLayout {
    private final int      CHART_COLUMN;
    private final int      COLUMN_WIDTH;
    private final int[]    ICON;
    private final String[] ICON_DES;
    @InjectView(2131429419)
    TextView emptyStructure;
    private BarChart mChart;
    private Context  mContext;
    private float[]  percent;

    public NutritionLayout(Context context) {
        super(context);
        this.ICON = new int[]{R.drawable.a_2, R.drawable.a_5, R.drawable.a_a, R.drawable.a_c};
        this.ICON_DES = new String[]{"谷薯", "肉蛋", "果蔬", "油脂"};
        this.CHART_COLUMN = 4;
        this.COLUMN_WIDTH = 25;
        this.percent = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        init(context);
    }

    public NutritionLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ICON = new int[]{R.drawable.a_2, R.drawable.a_5, R.drawable.a_a, R.drawable.a_c};
        this.ICON_DES = new String[]{"谷薯", "肉蛋", "果蔬", "油脂"};
        this.CHART_COLUMN = 4;
        this.COLUMN_WIDTH = 25;
        this.percent = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        init(context);
    }

    public NutritionLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ICON = new int[]{R.drawable.a_2, R.drawable.a_5, R.drawable.a_a, R.drawable.a_c};
        this.ICON_DES = new String[]{"谷薯", "肉蛋", "果蔬", "油脂"};
        this.CHART_COLUMN = 4;
        this.COLUMN_WIDTH = 25;
        this.percent = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.NutritionLayout, this, false);
        this.mChart = (BarChart) view.findViewById(R.id.chart);
        this.mChart.setColumn(4, 25);
        this.mChart.setPercent(this.percent);
        addView(view);
        ButterKnife.inject((View) this);
        if (!isInEditMode()) {
            postDelayed(new Runnable() {
                public void run() {
                    NutritionLayout.this.addTag();
                }
            }, 100);
        }
    }

    public void setPercent(float[] percent) {
        this.mChart.setPercent(percent);
        this.emptyStructure.setVisibility(8);
    }

    public void setTarget(float[] target) {
        this.mChart.setTarget(target);
        this.emptyStructure.setVisibility(8);
    }

    public void startAnim() {
        this.mChart.startAnim();
    }

    public void showEmpty() {
        this.percent = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        this.mChart.setPercent(this.percent);
        this.emptyStructure.setVisibility(0);
        this.emptyStructure.setText("暂无数据");
    }

    public void showLoading() {
        this.percent = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
        this.mChart.setPercent(this.percent);
        this.emptyStructure.setVisibility(0);
        this.emptyStructure.setText("加载中");
    }

    private void addTag() {
        if (this.mChart.getStartPoint() != null) {
            for (int i = 0; i < 4; i++) {
                View tagView = LayoutInflater.from(this.mContext).inflate(R.layout.q7, this, false);
                TextView tag = (TextView) tagView.findViewById(R.id.text);
                ((ImageView) tagView.findViewById(R.id.icon)).setImageResource(this.ICON[i]);
                tag.setText(this.ICON_DES[i]);
                LayoutParams params = (LayoutParams) tagView.getLayoutParams();
                params.leftMargin = (int) (((float) this.mChart.getLeft()) + this.mChart
                        .getStartPoint()[i]);
                params.topMargin = this.mChart.getBottom();
                addView(tagView);
            }
        }
    }
}
