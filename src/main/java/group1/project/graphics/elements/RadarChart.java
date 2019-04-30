/**
 * @author Alex Mahon
 */

package group1.project.graphics.elements;

import group1.project.Launcher;
import processing.core.PFont;

public class RadarChart extends Graph{

    private float angle;
    private float centerX;
    private float centerY;
    private float axisLength;
    private float intervals;
    private float intervalLength;
    private int dimensions;
    private RadarChartAxes[] axis;

    public RadarChart(Launcher p, float X, float Y, float chartWidth, float chartHeight, int intervals, RadarChartAxes[] axis){
        super (p, X, Y, chartWidth, chartHeight, intervals, axis);
        dimensions = 4;
        angle = 360 / dimensions;
        centerX = X + (chartWidth/2);
        centerY = Y + (chartHeight/2);
        axisLength = (chartHeight/2);
        this.intervals = intervals;
        intervalLength = axisLength/intervals;
        this.axis = axis;
    }

    public void draw(){}

    public void drawAxis() {
        p.stroke(100);
        p.strokeWeight(1);
        p.point(centerX,centerY);

        float[] axisPointsX = new float[dimensions];
        float[] axisPointsY = new float[dimensions];
        float xValue;
        float yValue;
        float ang = 0;

        for(int i = 0; i < dimensions; i++, ang += angle){

            float len = intervalLength;
            axisPointsX[i] = getX(ang, axisLength);
            axisPointsY[i] = getY(ang, axisLength);

            p.stroke(200);
            p.strokeWeight(1);
            p.line(centerX,centerY,axisPointsX[i],axisPointsY[i]);

            p.stroke(0);

            for(int index = 0; index < intervals; index++, len += intervalLength){
                xValue = getX(ang, len);
                yValue = getY(ang, len);

                p.strokeWeight(4);
                p.stroke(200);
                p.point(xValue,yValue);
                p.strokeWeight(1);
                p.stroke(0);
            }
        }
    }

    public void drawDataPoints(float R, float G, float B, float fillAlpha, RadarChartDataPoint[] dataPoints){
        float[] xValue = new float[4];
        float[] yValue = new float[4];
        float ang = 0;


        for(int i = 0; i < dimensions; i++, ang += angle){
            float len = (dataPoints[i].plottedValue/dataPoints[i].maxValue)*axisLength;
            xValue[i]=getX(ang,len);
            yValue[i]=getY(ang,len);
            displayLabels(ang, axis[i], dataPoints);

        }
        p.fill(R, G, B,fillAlpha);
        p.stroke(R, G, B);
        p.strokeWeight(2);
        p.quad(xValue[0],yValue[0],xValue[1],yValue[1],xValue[2],yValue[2],xValue[3],yValue[3]);
    }

    private float getX(float ang, float len){
        return (centerX + (len * p.cos(p.radians(ang))));
    }
    private float getY(float ang, float len){
        return (centerY + (len * p.sin(p.radians(ang))));
    }

    private void displayLabels(float ang, RadarChartAxes axis, RadarChartDataPoint[] dataPoints){
        float xValue = getX(ang, axisLength);
        float yValue = getY(ang, axisLength);
        float axisScale;
        int point = 2;
        float xVal;
        float yVal;

        PFont myFont = p.createFont("SansSerif", 10);
        PFont mySmallFont = p.createFont("SansSerif", 8);
        p.textFont(myFont);
        p.fill(0);
        p.textAlign(p.CENTER,p.TOP);

        if(ang==0){
            p.textAlign(p.CENTER, p.BOTTOM);
            p.text(axis.name,xValue,yValue-5);
            axisScale = dataPoints[0].maxValue/intervals;
            while(point < intervals){
                int axisScaleInt = Math.round(axisScale*point);
                xVal=getX(ang,intervalLength*point);
                yVal=getY(ang,intervalLength*point);
                p.textFont(mySmallFont);
                p.textAlign(p.CENTER, p.TOP);
                p.text(axisScaleInt, xVal, yVal+3);
                point += 2;
            }
        }
        if(ang==90){
            p.textAlign(p.CENTER, p.TOP);
            p.text(axis.name,xValue,yValue+2);
            axisScale = dataPoints[1].maxValue/intervals;
            while(point < intervals){
                int axisScaleInt = Math.round(axisScale*point);
                xVal=getX(ang,intervalLength*point);
                yVal=getY(ang,intervalLength*point);
                p.textFont(mySmallFont);
                p.textAlign(p.LEFT, p.CENTER);
                p.text(axisScaleInt, xVal+5, yVal-2);
                point += 2;
            }
        }
        if(ang==180){
            p.textAlign(p.CENTER, p.BOTTOM);
            p.text(axis.name,xValue,yValue-5);
            axisScale = dataPoints[2].maxValue/intervals;
            while(point < intervals){
                int axisScaleInt = Math.round(axisScale*point);
                xVal=getX(ang,intervalLength*point);
                yVal=getY(ang,intervalLength*point);
                p.textFont(mySmallFont);
                p.textAlign(p.CENTER, p.TOP);
                p.text(axisScaleInt, xVal, yVal+3);
                point += 2;
            }
        }
        if(ang==270){
            p.textAlign(p.CENTER, p.BOTTOM);
            p.text(axis.name,xValue,yValue);
            axisScale = dataPoints[3].maxValue/intervals;
            while(point < intervals){
                int axisScaleInt = Math.round(axisScale*point);
                xVal=getX(ang,intervalLength*point);
                yVal=getY(ang,intervalLength*point);
                p.textFont(mySmallFont);
                p.textAlign(p.LEFT, p.CENTER);
                p.text(axisScaleInt, xVal+5, yVal-2);
                point += 2;
            }
        }
    }
}
