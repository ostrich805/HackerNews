/**
 * @author Yannick Gloster
 * @author Kevin Troisi
 */

package group1.project.graphics.elements;

import group1.project.Launcher;

import java.util.ArrayList;
import java.util.List;

public class Banner extends Widget {

    private List<Widget> widgets = new ArrayList<>();
    private Button titleButton;

    public Banner(Launcher p, int x, int y, int height, int widgetColor) {
        super(p, x, y, p.width, height, widgetColor, "",
                p.createFont("Whitney-MediumSC", 48), widget -> {});

        int fontSize = 48;
        p.textSize(fontSize);

        titleButton = new Button(p, 10, 30, (int) (p.textWidth("News")), 30, Colours.BLUE, "News", widgetFont, fontSize,
                button -> Launcher.goBackAScreen());

        titleButton.setLabelColor(p.color(255));
        widgets.add(titleButton);
    }

    @Override
    public void fire(int targetx, int targety) {
        //Delegate mouse press to widgets
        widgets.forEach(widget -> {
            if(widget.isWithinBounds(targetx,targety))
                widget.fire(targetx, targety);
        });
    }

    @Override
    public void draw() {
        //super.draw();

        p.noStroke();
        p.fill(Colours.BLUE);
        p.rect(0, 0, width, height);
        p.fill(Colours.RED);
        p.rect(p.width - 80, 0, 80, 45);
        p.fill(Colours.ORANGE);
        p.rect(p.width - 60, 0, 60, 30);
        p.fill(Colours.YELLOW);
        p.rect(p.width - 40, 0, 40, 15);
        p.fill(255);

        widgets.forEach(Widget::draw);
        //p.textFont(titleFont);
        //p.textAlign(p.LEFT, p.CENTER);
        //p.text("News", 20, 24);
    }
}
