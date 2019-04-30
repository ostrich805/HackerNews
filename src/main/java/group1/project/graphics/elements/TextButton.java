package group1.project.graphics.elements;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

import java.util.function.Consumer;

/**
 * @author Kevin Troisi
 */
public class TextButton extends Button {

    public TextButton(PApplet p, int x, int y, int widgetColor, String label, PFont widgetFont, int fontSize, Consumer<? extends Button> action) {
        super(p, x, y, (int) p.textWidth(label), fontSize, widgetColor, label, widgetFont, fontSize, action);
        p.textSize(fontSize);
        width = (int) p.textWidth(label);
        height = (int) (p.textAscent() + p.textDescent());
    }

    @Override
    public void drawText() {
        p.noFill();
        p.textAlign(PConstants.LEFT, PConstants.TOP);
        p.textFont(widgetFont);
        p.textSize(fontSize);
        p.text(label, x, y);
    }

    @Override
    public void hover() {
        p.fill(Colours.PURE_BLUE);
        int thickness = 2;
        p.rect(x, y + height, width, thickness);
    }
}
