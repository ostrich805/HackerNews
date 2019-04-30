/**
 * @author Group 1
 */

package group1.project;

import group1.project.data.DatabaseManager;
import group1.project.data.FileLoader;
import group1.project.graphics.elements.Button;
import group1.project.graphics.elements.DropDown;
import group1.project.graphics.elements.SearchBar;
import group1.project.graphics.elements.Widget;
import group1.project.graphics.screens.StoryScreen;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class mainTest extends PApplet{

    private List<Widget> widgets = new ArrayList<>();
    private PFont myFont;
    private static mainTest INSTANCE;
    private StoryScreen testStoryScreen;

    public void settings(){
        size(720, 480);
    }

    public void setup() {
        INSTANCE = this;
        pixelDensity(displayDensity());
        FileLoader.initialise(this);
        DatabaseManager.initialise();
        //testStoryScreen = new OldStoryScreen(this, width, height, 255, new StoryQuery(StorySortType.TITLE, ElementOrder.DESCENDING, 10).nextChunk().get(1));

        myFont = createFont("Whitney-MediumSC", 32);

        widgets.add(new Button(this, 600, 200, 100, 40, this.color(255), "button", myFont,
                button -> System.out.println(button.toString())));

        Map<String, Consumer<Button>> dropDownElements = new HashMap<>();
        dropDownElements.put("Test1", button -> System.out.println("I'm a button"));
        dropDownElements.put("Test2", button -> System.out.println("same"));
        dropDownElements.put("Test3", button -> System.out.println("So am I"));

        widgets.add(new DropDown(this, 600, 300, 100, 40, this.color(255), "button", myFont, dropDownElements));
        SearchBar searchBar = new SearchBar(this, 600, 100, 100, 40, this.color(200), myFont, action -> System.out.println("This shouldn't print"));
        widgets.add(searchBar);

    }

    public void draw(){
        background(100);
        widgets.forEach(Widget::draw);
        color(255);
        testStoryScreen.draw();
    }

    public void mousePressed(){
        widgets.forEach(widget -> {
            if(widget.isWithinBounds(mouseX,mouseY))
                widget.fire(mouseX,mouseY);
        });
        testStoryScreen.mousePressed();
    }

    public static void main(String[] args){
        PApplet.main("group1.project.mainTest");
    }
}