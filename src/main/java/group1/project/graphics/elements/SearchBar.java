/**
 * @author Yannick Gloster
 */

package group1.project.graphics.elements;

import processing.core.PApplet;
import processing.core.PFont;

import java.util.function.Consumer;

public class SearchBar extends Button {
    private boolean focus;
    private int line;

    public SearchBar(PApplet p, int x, int y, int width, int height, int widgetColor, PFont widgetFont, Consumer<? extends Button> action) {
        super(p, x, y, width, height, widgetColor, "", widgetFont, action);
        this.focus = false;
        this.action = Button -> focus = true;
        labelColor = p.color(0);
        this.line = 0;
    }

    public void draw() {
        super.draw();
        if(label.length() == 0) {
            if(focus) {
                if(line < 30) {
                    this.drawCustomText("|", p.color(100));

                }
                else if(line < 60){
                    this.drawCustomText("", p.color(100));
                }
                else {
                    line = 0;
                }
                line++;
            }
            else {
                this.drawCustomText("Search", p.color(100));
            }
        }
        else {
            this.drawText();
        }
        p.stroke(labelColor);
        p.noFill();
        int circleD = height - 12;
        int circleCenterX = (int)x + width - 8;
        int circleCenterY = (int)y + height - 12;
        p.strokeWeight(1);
        p.circle(circleCenterX,  circleCenterY, circleD);
        p.fill(labelColor);
        p.line(circleCenterX - circleD/2 + 1, circleCenterY + circleD/2 - 1, circleCenterX - circleD/2 - height/5, circleCenterY + circleD/2 + height/5);
    }

    public boolean getFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public String getLabel() {
        return label;
    }

    public void append(char s){
        if(s==p.BACKSPACE){
            if(!label.equals(""))
                label=label.substring(0 , label.length()-1);
        }
        else {
            label = label + s;
            if (p.textWidth(label) > (width - 10) - 20) {
                label=label.substring(0 , label.length()-1);
            }
        }
    }
}
