package group1.project.graphics.elements;

import processing.core.PApplet;
import processing.core.PFont;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Yannick Gloster
 * @author Kevin Troisi
 */
public class DropDown extends Button {

    private List<Button> dropDownWidgets = new ArrayList<>();

    public DropDown(PApplet p, int x, int y, int width, int height, int widgetColor,
                    String label, PFont widgetFont, Map<String,Consumer<Button>> dropDownElements) {

        super(p, x, y, width, height, widgetColor, label, widgetFont, button -> {});

        int widgetDropDownY = y;

        for (String dropDownLabel : dropDownElements.keySet()) {
            widgetDropDownY += height;
            dropDownWidgets.add(new Button(p, x, widgetDropDownY, width, height, widgetColor, dropDownLabel, widgetFont,
                    dropDownElements.get(dropDownLabel)));
        }
    }

    public void draw() {
        super.draw();
        if(clicked) {
            if(hover) {
                dropDownWidgets.forEach(Button::hover);
            }
            dropDownWidgets.forEach(Button::draw);
        }
    }

    public void setTopLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean isWithinBounds(int targetx, int targety){
        if(!clicked) return super.isWithinBounds(targetx,targety);

        return targetx >= x && targetx <= x + width &&
                targety >= y &&
                targety <= y + height + dropDownWidgets.size() * height;

    }

    @Override
    public void fire(int targetx, int targety) {
        clicked = !clicked;
        if(super.isWithinBounds(targetx,targety)) return;

        //Call click consumer of the correct dropdown button
        int widgetNum = (targety - (int)y) / height - 1;
        dropDownWidgets.get(widgetNum).fire(targetx,targety);
        this.label = dropDownWidgets.get(widgetNum).label;
        super.fixTextSize();
    }

    @Override
    public void fixTextSize() {
        super.fixTextSize();
        for(Button dropDownItem : dropDownWidgets) {
            dropDownItem.fixTextSize();
        }
    }
}
