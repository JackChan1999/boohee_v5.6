package lecho.lib.hellocharts.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import lecho.lib.hellocharts.computator.PreviewChartComputator;
import lecho.lib.hellocharts.gesture.PreviewChartTouchHandler;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.renderer.PreviewLineChartRenderer;

public class PreviewLineChartView extends LineChartView {
    private static final String TAG = "PreviewLineChartView";
    protected PreviewLineChartRenderer previewChartRenderer;

    public PreviewLineChartView(Context context) {
        this(context, null, 0);
    }

    public PreviewLineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreviewLineChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.chartComputator = new PreviewChartComputator();
        this.previewChartRenderer = new PreviewLineChartRenderer(context, this, this);
        this.touchHandler = new PreviewChartTouchHandler(context, this);
        setChartRenderer(this.previewChartRenderer);
        setLineChartData(LineChartData.generateDummyData());
    }

    public int getPreviewColor() {
        return this.previewChartRenderer.getPreviewColor();
    }

    public void setPreviewColor(int color) {
        this.previewChartRenderer.setPreviewColor(color);
        ViewCompat.postInvalidateOnAnimation(this);
    }
}
