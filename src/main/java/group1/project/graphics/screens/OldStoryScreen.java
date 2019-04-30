package group1.project.graphics.screens;

import group1.project.Launcher;
import group1.project.data.Comment;
import group1.project.data.Story;
import group1.project.graphics.elements.Button;
import group1.project.graphics.elements.CommentPreviewContainer;
import group1.project.graphics.elements.Widget;
import processing.core.PFont;
import processing.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Neil Kilbane
 * @author Yannick Gloster
 */
public class OldStoryScreen extends Screen {
    private Story story;
    private List<Widget> allWidgets = new ArrayList<>();
    private List<Widget> allComments = new ArrayList<>();
    private PFont myFont;

    public OldStoryScreen(Launcher p, int width, int height, int backgroundColor, Story story){
        super(p, width, height, backgroundColor);
        this.story = story;
        myFont = p.createFont("Whitney-MediumSC", 24);
        p.textSize(24);

        /*URLButton urlButton = new URLButton(p, 20, 150, (int)p.textWidth(story.getUrl()),
                30,  p.color(255), story.getUrl(), myFont,
                24);*/

        Button authorButton = new Button(p, (20 + (int) p.textWidth("Posted by ")),
                120, (int)p.textWidth(story.getUsername()),
                30, p.color(255), story.getUsername(),
                myFont, 24,
                button -> Launcher.moveToScreen(new UserScreen(p, p.width, p.height, p.color(255), story.getAuthor())));

        authorButton.setLabelColor(p.color(0,0,255));
        addCommentsWidgets(story.getComments());
        //allWidgets.add(urlButton);
        allWidgets.add(authorButton);
    }

    @Override
    public void draw() {
        super.draw();

        for (int i = allComments.size() - 1; i >= 0; i--) allComments.get(i).draw();

        p.fill(0);

        String title = story.getTitle();

        int size = 32;
        boolean didElide = false;
        while(p.textWidth(title) > 720) {
            title = title.substring(0, title.length() - 1);
            didElide = true;
        }

        if(didElide) title += "...";

        p.textSize(size);
        p.text(title, 20, 80);

        p.textSize(24);
        p.text("Posted by ", 20, 120);

        p.text(" at " + story.getDate(), 20 + p.textWidth("Posted by " + story.getUsername()), 120);
        p.fill(0);
        p.text("Score: " + story.getScore(), 20, 200);
        p.text((story.getComments().length+1) + " comments", 20, 240);

        allWidgets.forEach(Widget::draw);
    }

    private void addCommentsWidgets(Comment[] comments){
        int startY = 280;
        int height = 40;
        int padding = 3;
        allComments = new ArrayList<>();
        System.out.println(comments.length);

        for (int i = 0; i < comments.length; i++) {
            Comment comment = comments[i];
            if(comment == null) continue;
            System.out.println(comment.getText());
            allComments.add(new CommentPreviewContainer(p, 20, startY + (i * (height + padding)),
                    p.width - 100, height, 15, myFont, comment));
        }

    }

    public void mousePressed(){
        super.mousePressed();
        allWidgets.forEach(widget -> {
            if(widget.isWithinBounds(p.mouseX,p.mouseY))
                widget.fire(p.mouseX,p.mouseY);
        });
        allComments.forEach(widget -> {
            if(widget.isWithinBounds(p.mouseX,p.mouseY))
                widget.fire(p.mouseX,p.mouseY);
        });
    }

    public void mouseMoved() {
        allWidgets.forEach(Widget::mouseMoved);
        allComments.forEach(Widget::mouseMoved);
    }

    public void mouseWheel(MouseEvent event) {
        float e = event.getCount();
        allComments.forEach(comment -> comment.setY(comment.getY() - e));
    }
}