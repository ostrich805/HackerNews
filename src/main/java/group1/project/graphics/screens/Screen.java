package group1.project.graphics.screens;

import group1.project.Launcher;
import group1.project.graphics.elements.Banner;
import group1.project.graphics.elements.Widget;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yannick Gloster
 * @author Kevin Troisi
 */
public abstract class Screen {
    private int width;
    private int height;
    private int x;
    private int y;
    protected int backgroundColor;

    protected Launcher p;
    protected List<Widget> widgets = new ArrayList<>();
    private Banner banner;

    public Screen(Launcher p, int width, int height, int backgroundColor) {
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.p = p;
        banner = new Banner(p,0,0, 60, backgroundColor);
        widgets.add(banner);
    }
    public Screen(Launcher p, int width, int height, int backgroundColor, int x, int y) {
        this(p, width, height, backgroundColor);
        this.x = x;
        this.y = y;
    }
    public void draw() {
        if(p.width == this.width && p.height == this.height) {
            p.background(backgroundColor);
        }
        else {
            p.background(0);
            p.fill(backgroundColor);
            p.rect(x, y, width, height);
        }
        widgets.forEach(Widget::draw);
    }

    public void mousePressed() {
        widgets.forEach(widget -> {
            if(widget.isWithinBounds(p.mouseX,p.mouseY))
                widget.fire(p.mouseX,p.mouseY);
        });
    }

    public void drawBanner() {
        banner.draw();
    }
    public void keyPressed() {
    }

    public void mouseWheel(MouseEvent event) {
    }

    public void mouseDragged() {
    }

    public void mouseMoved() {
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Banner getBanner() {
        return banner;
    }
}
