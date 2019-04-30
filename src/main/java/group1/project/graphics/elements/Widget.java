package group1.project.graphics.elements;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

import java.util.function.Consumer;

/**
 * @author Yannick Gloster
 * @author Kevin Troisi
 */
public abstract class Widget {

    protected PApplet p;
    protected float x, y;
    protected int width, height, cornerRadius;
    protected String label;
    protected int fontSize;
    protected int widgetColor, labelColor, strokeColor;
    protected PFont widgetFont;
    private boolean noStroke;
    protected Consumer<Widget> action;
    protected boolean hover;

    public Widget(PApplet p, int x, int y, int width, int height, int widgetColor, String label, PFont widgetFont, Consumer<? extends Widget> action) {
        this(p, x, y, width, height, widgetColor, action);
        this.label = label;
        this.widgetFont = widgetFont;
        this.fontSize = 32;
    }
    public Widget(PApplet p, int x, int y, int width, int height, int widgetColor, int strokeColor, String label, PFont widgetFont, Consumer<? extends Widget> action) {
        this(p, x, y, width, height, widgetColor, strokeColor, action);
        this.label = label;
        this.widgetFont = widgetFont;
        this.fontSize = 32;
    }
    public Widget(PApplet p, int x, int y, int width, int height, int widgetColor, String label, PFont widgetFont, int fontSize, Consumer<? extends Widget> action) {
        this(p, x, y, width, height, widgetColor, label, widgetFont, action);
        this.fontSize = fontSize;
    }
    public Widget(PApplet p, int x, int y, int width, int height, int widgetColor, int strokeColor, String label, PFont widgetFont, int fontSize, Consumer<? extends Widget> action) {
        this(p, x, y, width, height, widgetColor, strokeColor, label, widgetFont, action);
        this.fontSize = fontSize;
    }
    public Widget(PApplet p, int x, int y, int width, int height, int widgetColor, Consumer<? extends Widget> action) {
        this.p = p;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.cornerRadius = 0;
        this.widgetColor = widgetColor;
        labelColor = p.color(0);
        noStroke = true;
        this.action = (Consumer<Widget>) action;
    }
    public Widget(PApplet p, int x, int y, int width, int height, int widgetColor, int strokeColor, Consumer<? extends Widget> action) {
        this.p = p;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.cornerRadius = 0;
        this.widgetColor = widgetColor;
        labelColor = p.color(0);
        this.strokeColor = strokeColor;
        noStroke = false;
        this.action = (Consumer<Widget>) action;
    }

    public void draw() {
        if(hover) hover();

        if(noStroke) p.noStroke();
        else p.stroke(strokeColor);

        p.fill(widgetColor);
        p.rect(x, y, width, height, cornerRadius);
        p.fill(labelColor);
        if(widgetFont != null)
            drawText();
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public void drawText() {
        p.fill(labelColor);
        p.textAlign(PConstants.LEFT, PConstants.BOTTOM);
        p.textFont(widgetFont);
        p.textSize((height/(float)40)*fontSize);
        p.text(label, x+10, y+height-1);
    }

    public void drawCustomText(String customLabel, int customFill) {
        p.fill(customFill);
        p.textAlign(PConstants.LEFT, PConstants.TOP);
        p.textFont(widgetFont);
        p.textSize((height/(float)40)*fontSize);
        p.text(customLabel, x+10, y-1);
    }

    public void hover() {
        p.fill(0);
        p.rect(x+2, y+2, width, height, cornerRadius);
    }
    public void setStrokeColor(int c) {
        this.strokeColor = c;
    }

    public void setLabelColor(int c) {
        this.labelColor = c;
    }

    public void setCornerRadius(int r) {
        this.cornerRadius = r;
    }

    public void setHeight(int h) {
        this.height = h;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isWithinBounds(int targetx, int targety){
        return targetx >= x && targetx <= x + width &&
                targety >= y && targety <= y + height;
    }

    public void fixTextSize() {
        p.textSize(fontSize);
        if(p.textWidth(label) > width) {
            while (p.textWidth(label) > width + 80) {
                fontSize--;
                p.textSize(fontSize);
            }
        }
    }

    public void mouseMoved() {
        hover = isWithinBounds(p.mouseX, p.mouseY);
    }

    abstract public void fire(int targetx,int targety);
}
