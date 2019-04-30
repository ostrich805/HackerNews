/**
 * @author Kevin Troisi
 * @author Yannick Gloster
 */

package group1.project;

import group1.project.data.DatabaseManager;
import group1.project.data.FileLoader;
import group1.project.graphics.screens.HomeScreen;
import group1.project.graphics.screens.Screen;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;

import java.util.Stack;

public class Launcher extends PApplet{

    private static Screen currentScreen;
    private static Launcher INSTANCE;
    private static Stack<Screen> screenHistory = new Stack<>();

    public void settings(){
        size(720, 480);
    }

    public void setup() {
        INSTANCE = this;
        pixelDensity(displayDensity());
        PImage icon = loadImage("src/main/java/group1/project/graphics/icon.png");
        surface.setIcon(icon);
        surface.setTitle("  News");
        FileLoader.initialise(this);
        DatabaseManager.initialise();
        currentScreen = new HomeScreen(this, width, height, color(255));
    }

    public static Launcher getINSTANCE() {
        return INSTANCE;
    }

    public void draw(){
        currentScreen.draw();
    }

    public void mousePressed(){
        currentScreen.mousePressed();
    }

    public void keyPressed() {
        currentScreen.keyPressed();
    }

    public void mouseDragged() {
        currentScreen.mousePressed();
    }

    public void mouseWheel(MouseEvent event) {
        currentScreen.mouseWheel(event);
    }

    public void mouseMoved() {
        currentScreen.mouseMoved();
    }

    public static void moveToScreen(Screen screen) {
        screenHistory.push(currentScreen);
        setCurrentScreen(screen);
    }

    private static void setCurrentScreen(Screen screen){
        currentScreen = screen;
    }

    public static void goBackAScreen(){
        if(!screenHistory.empty())
            setCurrentScreen(screenHistory.pop());
    }

    public static void main(String[] args){
        PApplet.main("group1.project.Launcher");
    }
}