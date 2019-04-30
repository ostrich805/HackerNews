/**
 * @author Yannick Gloster
 * @author Kevin Troisi
 */

package group1.project.graphics.elements;

import processing.core.PApplet;
import processing.core.PFont;

import java.util.function.Consumer;

public class Button extends Widget {

    protected boolean clicked = false;
    protected boolean expandable = false;
    protected boolean expanded = false;

    public Button(PApplet p, int x, int y, int width, int height, int widgetColor, String label, PFont widgetFont, Consumer<? extends Button> action) {
        super(p, x, y, width, height, widgetColor, label, widgetFont, action);
    }

    public Button(PApplet p, int x, int y, int width, int height, int widgetColor, String label, PFont widgetFont, int fontSize, Consumer<? extends Button> action) {
        super(p, x, y, width, height, widgetColor, label, widgetFont, fontSize, action);
    }

    public Button(PApplet p, int x, int y, int width, int height, int widgetColor, Consumer<? extends Button> action) {
        super(p, x, y, width, height, widgetColor, action);
    }

    public boolean getExpandable()
    {
        return expandable;
    }

    public boolean getExpanded()
    {
        return expanded;
    }

    public void expand()
    {
        if(expandable)
        {
            height *= 2;
            expanded = true;
        }
    }

    public void contract()
    {
        if(expanded)
        {
            height = 40;
            expandable = true;
            expanded = false;
        }
    }

    @Override
    public void fire(int targetx, int targety) {
        action.accept(this);
        clicked = !clicked;
    }

}
