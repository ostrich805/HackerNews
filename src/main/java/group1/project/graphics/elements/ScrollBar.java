/**
 * @author Yannick Gloster
 */

package group1.project.graphics.elements;

import processing.core.PApplet;

import java.util.List;
import java.util.function.Consumer;

public class ScrollBar extends Button {
    private float startingY;
    private float mousePressedY;
    private int screenHeight;
    private int totalHeight;
    private float ratio;
    private List<Widget> allWidgets;

    public ScrollBar(PApplet p, int x, int y, int screenHeight, int totalHeight, int widgetColor, List<Widget> allWidgets, Consumer<? extends Button> action) {
        super(p, x, y, 10, screenHeight, widgetColor, action);
        this.ratio = ((float)screenHeight/(float)totalHeight);
        System.out.println("\n" + ratio + "\n");
        this.setHeight((int)((float)screenHeight*ratio));
        this.allWidgets = allWidgets;
        this.totalHeight = totalHeight;
        startingY = y;
        this.screenHeight = screenHeight;
    }

    public void draw() {
        if(ratio < 1) {
            super.draw();
        }
    }

    public void mouseDragged() {
        if(this.isWithinBounds(p.mouseX, p.mouseY)) {
            if(this.y >= startingY && this.y < startingY + (totalHeight - screenHeight)) {
                this.y = p.mouseY - mousePressedY;
                for (Widget widget : allWidgets) {
                    if(this.y > mousePressedY) {
                        widget.setY(widget.getY() - ratio*(this.y - mousePressedY));
                    }
                    if(this.y < mousePressedY) {
                        widget.setY(widget.getY() + ratio*(this.y - mousePressedY));
                    }
                }

            }
        }
    }

    public void mousePressed() {
        mousePressedY = y;
        System.out.println(mousePressedY);
    }


    public void keyPressed() {
        if(p.key == p.CODED) {
            if(p.keyCode == p.DOWN) {
                if(this.y >= startingY && this.y < startingY + (totalHeight - screenHeight)) {
                    for (Widget widget : allWidgets) {
                        widget.setY(widget.getY() - 1);
                    }
                    this.y += ratio;
                }
            }
            if(p.keyCode == p.UP) {
                if(this.y > startingY && this.y <= startingY + (totalHeight - screenHeight) + 1) {
                    for(Widget widget : allWidgets) {
                        widget.setY(widget.getY() + 1);
                    }
                    this.y -= ratio;
                }
            }
        }
    }
}
