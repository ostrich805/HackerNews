package group1.project.graphics.screens;

import group1.project.Launcher;
import group1.project.data.Comment;
import group1.project.data.Story;
import group1.project.graphics.elements.Colours;
import group1.project.graphics.elements.CommentContainer;
import group1.project.graphics.elements.TextButton;
import group1.project.graphics.elements.Widget;
import processing.core.PFont;
import processing.core.PImage;
import processing.event.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Desktop.getDesktop;
import static java.net.URI.create;

/**
 * Displays story information and comments on screen
 * @author Kevin Troisi
 */
public class StoryScreen extends Screen {

    private final Story story;
    private PImage thumbnail;
    private TextButton titleButton, authorButton;
    private String subtitle1 = "%d points by ";
    private String subtitle2 = " at %s | %d comments";
    private List<CommentContainer> commentContainers = new ArrayList<>();

    private final int margin = 5;
    private final int bannerHeight = getBanner().getHeight();
    private final int subtitle1Width, subtitleHeight;
    private float subtitleY;

    public StoryScreen(Launcher p, int width, int height, int backgroundColor, Story story) {
        super(p, width, height, backgroundColor);
        this.story = story;
        PFont font = p.createFont("Whitney-MediumSC", 28);
        p.textFont(font);

        getThumbnail();

        formatSubtitle();
        p.textSize(12);
        subtitle1Width = (int) p.textWidth(subtitle1);

        String title = story.getTitle();
        //int titleWidth = (int) p.textWidth(title);
        subtitleHeight = (int) (p.textAscent() + p.textDescent());

        titleButton = new TextButton(p,thumbnail.width + margin,
                bannerHeight + margin,
                255,
                title,
                font,
                14,
                action -> {
                    try {
                        getDesktop().browse(create(story.getUrl()));
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                });

        widgets.add(titleButton);

        subtitleY = bannerHeight + thumbnail.height + margin;

        String authorName = story.getUsername();
        authorButton = new TextButton(p, subtitle1Width + margin,
                (int) subtitleY,
                255,
                authorName,
                font,
                12,
                action -> Launcher.moveToScreen(new UserScreen(p, p.width, p.height, p.color(255), story.getAuthor())));

        widgets.add(authorButton);

        Comment[] comments = story.getComments();
        float h = subtitleY + subtitleHeight + margin;

        for (Comment child : comments) {
            if(child != null) {
                CommentContainer cc = new CommentContainer(p, margin, (int) h,
                        width - 2 * margin, Colours.YELLOW, font, child);

                commentContainers.add(cc);
                h += cc.getTotalHeight();
            }
        }

    }

    private void formatSubtitle(){
        subtitle1 = String.format(subtitle1,story.getScore());
        subtitle2 = String.format(subtitle2,story.getDate(),story.getCommentCount());
    }

    @Override
    public void draw() {
        authorButton.setY(subtitleY);
        super.draw(); //draw background

        p.image(thumbnail,0,titleButton.getY());
        p.fill(0); // black
        p.textSize(12);
        p.text(subtitle1, 0, subtitleY);
        p.text(subtitle2, subtitle1Width + 2 * margin + authorButton.getWidth(), subtitleY);

        for (CommentContainer commentContainer : commentContainers) {
            if(commentContainer.getY() < p.height)
                commentContainer.draw();
        }

        drawBanner();
    }

    /**
     * Loads up thumbnail from URL, if available
     */
    private void getThumbnail(){
        thumbnail = p.loadImage("src/main/resources/icons/missing.png");
        PImage a = null;
        try {
            a = p.loadImage("https://www.google.com/s2/favicons?domain=" + story.getUrl(), "png");
        } catch (Exception e){
        }
        if(a != null) thumbnail = a;
        thumbnail.resize(30,30);
    }

    @Override
    public void mouseMoved() {
        //Delegate to widgets
        widgets.forEach(Widget::mouseMoved);
        commentContainers.forEach(CommentContainer::mouseMoved);
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        float d = event.getCount() * -3;
        subtitleY += d;
        titleButton.setY(titleButton.getY() + d);
        commentContainers.forEach(commentContainer -> commentContainer.changeY(d));
    }

    @Override
    public void mousePressed() {
        widgets.forEach(widget -> {
            if(widget.isWithinBounds(p.mouseX,p.mouseY))
                widget.fire(p.mouseX,p.mouseY);
        });
        commentContainers.forEach(commentContainer -> commentContainer.fire(p.mouseX,p.mouseY));
    }
}
