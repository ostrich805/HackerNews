/**
 * @author Yannick Gloster
 */

package group1.project.graphics.elements;

import group1.project.Launcher;
import group1.project.data.Story;
import group1.project.graphics.screens.StoryScreen;
import processing.core.PConstants;
import processing.core.PFont;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class StoryPreviewContainer extends Button {
    private Story story;
    private PFont widgetFont;

    public StoryPreviewContainer(Launcher p, int x, int y, int width, int height, int widgetColor, PFont widgetFont, Story story, Consumer<? extends Button> action) {
        super(p, x, y, width, height, widgetColor, action);
        this.widgetFont = widgetFont;
        this.story = story;
    }

    public StoryPreviewContainer(Launcher p, int x, int y, int width, int height, int widgetColor, PFont widgetFont, Story story) {
        super(p, x, y, width, height, widgetColor,
                button -> new Thread (() ->{
                    Launcher.moveToScreen(new StoryScreen(p, p.width, p.height, p.color(255), story));
                }).start());
        this.widgetFont = widgetFont;
        this.story = story;
    }

    public void draw() {
        super.draw();
        p.color(255);
        p.textFont(widgetFont);
        p.textAlign(PConstants.RIGHT);
        p.textSize(((float)height/(float)40)*30);
        p.text(story.getScore(), x+width-5, y+((float)height/(float)40)*28);
        String score = story.getScore() + "";
        float scoreWidth = p.textWidth(score);
        p.textAlign(PConstants.LEFT);
        p.textSize(((float)height/(float)40)*14);
        String title = story.getTitle();
        if(p.textWidth(story.getTitle()) > width-scoreWidth-5) {
            float sizeDifference = p.textWidth(story.getTitle()) - (width-scoreWidth-5);
            float textSize = ((float)height/(float)40)*14 - sizeDifference/(float)25;
            if(textSize > 0) {
                p.textSize(textSize);
            }
            else {
                List<Character> remove = new ArrayList<>();
                String testChar = "";
                char[] titleChar = title.toCharArray();
                int i = titleChar.length - 1;
                while(p.textWidth(testChar) - 20 <= sizeDifference) {
                    remove.add(titleChar[i]);
                    char[] tempChar = new char[remove.size()];
                    int index = 0;
                    for(Character c : remove) {
                        tempChar[index] = c;
                        index++;
                    }
                    testChar = new String(tempChar);
                    i--;
                }
                Collections.reverse(remove);
                char[] tempChar = new char[remove.size()];
                int index = 0;
                for(Character c : remove) {
                    tempChar[index] = c;
                    index++;
                }
                String actualRemove = new String(tempChar);
                title = title.substring(0, title.indexOf(actualRemove));
                title += "...";
            }
        }
        p.text(title, x+5, y+((float)height/(float)40)*15);
        p.textSize(((float)height/(float)40)*10);
        p.text("submitted by " + story.getUsername() + " at " + story.getDate(), x+5, y+((float)height/(float)40)*25);
        p.textSize(((float)height/(float)40)*12);
        p.text((story.getComments().length+1) + " comments", x+5, y+((float)height/(float)40)*37);
    }

}
