package group1.project.graphics.elements;

import group1.project.Launcher;

import java.util.Map;

/**
 * @author Yannick Gloster
 */
public abstract class Graph {
    protected float x, y, width, height;
    protected int numberOfDataPoints;
    protected Map<Object,Object> data;
    protected Launcher p;
    protected float X, Y, chartWidth, chartHeight;
    protected int intervals;
    protected RadarChartAxes[] axis;

    public Graph(Launcher p, float x, float y, float width, float height, Map<Object,Object> data) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.data = data;
        this.p = p;
        numberOfDataPoints = data.size();
    }

    public Graph(Launcher p, float X, float Y, float chartWidth, float chartHeight, int intervals, RadarChartAxes[] axis){
        this.X = X;
        this.Y = Y;
        this.chartWidth = chartWidth;
        this.chartHeight = chartHeight;
        this.intervals = intervals;
        this.axis = axis;
        this.p = p;
    }

    public abstract void draw();
}
