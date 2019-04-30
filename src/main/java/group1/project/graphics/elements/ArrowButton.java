/**
 * @author Yannick Gloster
 */

package group1.project.graphics.elements;

import processing.core.PApplet;

import java.util.function.Consumer;

public class ArrowButton extends Button {
    private int direction;
    private final int LEFT = 37;
    private final int UP = 38;
    private final int RIGHT = 39;
    private final int DOWN = 40;

    public ArrowButton(PApplet p, int x, int y, int width, int height, int widgetColor, int direction, Consumer<? extends Button> action) {
        super(p, x, y, width, height, widgetColor, action);
        this.direction = direction;
    }

    public void draw() {
        super.draw();
        drawArrow();
    }

    public void drawArrow() {
        float halfY = y+(float)height/2;
        float padding = 4;
        p.stroke(0);
        p.strokeWeight((float)1.5);
        p.line(x+padding, halfY, x+width-padding, halfY);
        switch(direction) {
            case(LEFT):
                p.line(x+padding, halfY, x+4*padding, y+padding);
                p.line(x+padding, halfY, x+4*padding, y+height-padding);
                break;
            case(RIGHT):
                p.line(x+width-padding, halfY, x+width-4*padding, y+padding);
                p.line(x+width-padding, halfY, x+width-4*padding, y+height-padding);
                break;
        }
    }
}
