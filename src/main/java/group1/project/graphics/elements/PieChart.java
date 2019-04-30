/**
 * @author Yannick Gloster
 */

package group1.project.graphics.elements;

import group1.project.Launcher;

import java.util.Map;

public class PieChart extends Graph {
    private float[] pieAngles;
    public PieChart(Launcher p, float x, float y, float width, float height, Map<Object,Object> data) {
        super(p, x,  y, width, height, data);
        pieAngles = new float[numberOfDataPoints];
        int index = 0;
        for(Object o : data.values()) {
            int value = (int)o;
            float degree = ((float)value/numberOfDataPoints)*360;
            pieAngles[index] = degree;
            index++;
        }

    }

    public void draw() {
        float lastAngle = 0;
        for (int i = 0; i < pieAngles.length; i++) {
            float gray = p.map(i, 0, pieAngles.length, 0, 255);
            p.fill(gray);
            p.arc(x+width/2, y+height/2, width/2, width/2, lastAngle, lastAngle+p.radians((float)pieAngles[i]));
            lastAngle += p.radians((float)pieAngles[i]);
        }
    }
}
