/**
 * @author Neil Kilbane
 */

package group1.project.graphics.elements;

import group1.project.Launcher;

import processing.core.PConstants;
import processing.core.PShape;

import java.util.List;
import java.util.Map;

public class BarChart extends Graph {
    private List<PShape> bars;
    public BarChart(Launcher p, float x, float y, float width, float height, Map<Object,Object> data){
        super(p, x,  y, width, height, data);
        for(Object o : data.values()) {
            int value = (int)o;
            float barWidth = width/data.values().size();
            double percentage = (double)value/(double)data.values().size();
            bars.add(p.createShape(PConstants.RECT, x + bars.indexOf(o) * barWidth, y, barWidth, (float)(height * percentage)));
        }
    }

    @Override
    public void draw() {
        for(int i = 0; i < bars.size(); i++)
        {
            p.shape(bars.get(i));
        }
    }
}
