package lecho.lib.hellocharts.model;

import com.baidu.location.aj;
import java.util.ArrayList;
import java.util.List;
import uk.co.senab.photoview.IPhotoView;

public class LineChartData extends AbstractChartData {
    public static final float DEFAULT_BASE_VALUE = 0.0f;
    private float baseValue = 0.0f;
    protected float imgRadius = 15.0f;
    private List<Line> lines = new ArrayList();

    public LineChartData(List<Line> lines) {
        setLines(lines);
    }

    public LineChartData(LineChartData data) {
        super(data);
        this.baseValue = data.baseValue;
        for (Line line : data.lines) {
            this.lines.add(new Line(line));
        }
    }

    public static LineChartData generateDummyData() {
        LineChartData data = new LineChartData();
        List values = new ArrayList(4);
        values.add(new PointValue(0.0f, 2.0f));
        values.add(new PointValue(1.0f, aj.hA));
        values.add(new PointValue(2.0f, IPhotoView.DEFAULT_MAX_SCALE));
        values.add(new PointValue(IPhotoView.DEFAULT_MAX_SCALE, aj.hA));
        Line line = new Line(values);
        List<Line> lines = new ArrayList(1);
        lines.add(line);
        data.setLines(lines);
        return data;
    }

    public void update(float scale) {
        for (Line line : this.lines) {
            line.update(scale);
        }
    }

    public void finish() {
        for (Line line : this.lines) {
            line.finish();
        }
    }

    public List<Line> getLines() {
        return this.lines;
    }

    public LineChartData setLines(List<Line> lines) {
        if (lines == null) {
            this.lines = new ArrayList();
        } else {
            this.lines = lines;
        }
        return this;
    }

    public float getBaseValue() {
        return this.baseValue;
    }

    public LineChartData setBaseValue(float baseValue) {
        this.baseValue = baseValue;
        return this;
    }

    public float getImgRadius() {
        return this.imgRadius;
    }

    public void setImgRadius(float imgRadius) {
        this.imgRadius = imgRadius;
    }
}
