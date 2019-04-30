/**
 * @author Neil Kilbane
 */

package group1.project.graphics.elements;

import group1.project.Launcher;

import processing.core.PConstants;
import processing.core.PShape;

import java.util.List;
import java.util.Map;

public class LineGraph extends Graph {
    private List<PShape> points;
    public LineGraph(Launcher p, float x, float y, float width, float height, Map<Object,Object> data){
        super(p, x,  y, width, height, data);
        for(Object o : data.values()) {
            int value = (int)o;
            float pointWidth = width/data.values().size();
            double percentage = (double)value/(double)data.values().size();
            points.add(p.createShape(PConstants.ELLIPSE, x + points.indexOf(o) * pointWidth, (float)(height * percentage), 4, 4));
        }
    }

    @Override
    public void draw() {
        for(int i = 0; i < points.size(); i++)
        {
            p.shape(points.get(i));
        }
        for(int i = 1; i < points.size(); i++)
        {
            p.line(points.get(i).getParam(1),  points.get(i).getParam(2),
                    points.get(i - 1).getParam(1), points.get(i - 1).getParam(2));
        }
    }
}
