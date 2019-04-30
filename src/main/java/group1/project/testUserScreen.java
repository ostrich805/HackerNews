/**
 * @author Yannick Gloster
 */

package group1.project;

import group1.project.data.DatabaseManager;
import group1.project.data.FileLoader;
import group1.project.data.User;
import group1.project.data.queries.ElementOrder;
import group1.project.data.queries.UserQuery;
import group1.project.data.queries.UserSortType;
import group1.project.graphics.elements.Widget;
import group1.project.graphics.screens.UserScreen;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;

public class testUserScreen extends PApplet {

    private static testUserScreen INSTANCE;
    private List<Widget> widgets = new ArrayList<>();
    private PFont whitney32;
    private UserScreen userScreen;


    public void settings(){
        size(720, 480);
    }

    public void setup() {
        INSTANCE = this;
        pixelDensity(displayDensity());
        //data = new JSONLoader(this);
        whitney32 = createFont("Whitney-MediumSC", 32);
        PImage icon = loadImage("src/main/java/group1/project/graphics/icon.png");
        surface.setIcon(icon);
        surface.setTitle("USER SCREEN TEST");
        FileLoader.initialise(this);
        DatabaseManager.initialise();

        UserQuery query = new UserQuery(UserSortType.POST_COUNT, ElementOrder.DESCENDING,10);
        List<User> chunk = query.nextChunk();

        //userScreen = new UserScreen(this, width, height, color(255), chunk.get(5));
    }

    public void draw(){
        userScreen.draw();
        widgets.forEach(Widget::draw);
        color(255);
    }

    public void mousePressed(){
        widgets.forEach(widget -> {
            if(widget.isWithinBounds(mouseX,mouseY))
                widget.fire(mouseX,mouseY);
        });
        userScreen.mousePressed();
    }

    public void mouseWheel(MouseEvent event) {
        userScreen.mouseWheel(event);
    }

    public static void main(String[] args){
        PApplet.main("group1.project.testUserScreen");
    }
}