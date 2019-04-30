package group1.project.graphics.elements;

import group1.project.Launcher;
import group1.project.data.Comment;
import group1.project.data.User;
import group1.project.graphics.screens.UserScreen;
import processing.core.PConstants;
import processing.core.PFont;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kevin Troisi
 */
public class CommentContainer extends Button {

    private final Comment comment;
    private String info = " on %s | %d children";
    private String text;
    private final int textHeight, indent = 10, lineSpacing = 1, margin = 5;
    private float lineHeight;
    private final List<CommentContainer> children = new ArrayList<>();
    private final TextButton authorButton;

    public CommentContainer(Launcher p, int x, int y, int width, int widgetColor, PFont widgetFont, Comment comment) {
        super(p, x, y, width, 0, widgetColor, button -> {
            //todo collapse the comment's children
        });
        this.comment = comment;
        p.textSize(12);

        info = String.format(info,comment.formatDate(),comment.childrenCount());

        //compact the text
        text = comment.getText().replaceAll("<p>"," ")
                .replaceAll("<.{1,2}>","");

        StringBuilder b = new StringBuilder();

        float currentWidth = 0;
        int totalLines = 1;

        char[] arr = text.toCharArray();

        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];

            float nextCharWidth = p.textWidth(c);
            //if it fits on this line
            currentWidth += nextCharWidth;
            if(currentWidth < width - margin) {

                char next;

                //if the next char doesn't fit, meaning the current one is the last on this line:
                if((i + 1 < arr.length) && currentWidth + p.textWidth((next = arr[i + 1])) >= width - margin) {

                    if(c == ' '){ //if this char is space and last on this line
                        b.append("\n");
                        currentWidth = 0;
                        totalLines++;
                    } else if(next == ' '){
                        b.append(c).append("\n");
                        currentWidth = 0;
                        totalLines++;
                        i++; //skip the space on a new line
                    } else { //we're splitting a word
                        b.append('-').append("\n").append(c).append(next);
                        i++; //skip next, we've just added it
                        currentWidth = p.textWidth(next);
                        totalLines++;
                    }
                }
                else {
                    b.append(c);
                }
            }
        }

        text = b.toString();

        String authorName = comment.getUsername();
        authorButton = new TextButton(p, x + lineSpacing,
                y + lineSpacing,
                widgetColor,
                authorName,
                widgetFont,
                12,
                action ->{
                    User author = comment.getAuthor();
                    if(author != null)
                        Launcher.moveToScreen(new UserScreen(p, p.width, p.height, p.color(255), author));
                });

        lineHeight = p.textAscent() + p.textDescent();
        p.textLeading(lineSpacing + lineHeight);
        textHeight = (int) (totalLines * (lineSpacing + lineHeight));

        lineHeight = p.textAscent() + p.textDescent();
        height = authorButton.height + textHeight + 5 * lineSpacing;

        Comment[] kids = comment.getComments();
        int h = y + height;
        for (Comment child : kids) {
            if(child != null) {
                CommentContainer cc = new CommentContainer(p, x + indent, h,
                        width - indent, widgetColor, widgetFont, child);
                children.add(cc);
                h += cc.getTotalHeight();
            }
        }
    }

    @Override
    public void draw(){
        p.textSize(12);
        p.fill(widgetColor);
        p.stroke(0); //black border
        p.rect(x,y,width,height);
        p.noStroke();
        p.fill(0); //black
        p.textAlign(PConstants.LEFT, PConstants.TOP);
        p.textLeading(lineSpacing + lineHeight);
        p.text(text,x + lineSpacing ,y + lineSpacing + lineHeight); //comment's text
        authorButton.setY(y + lineSpacing);
        authorButton.draw();
        p.text(info, x + authorButton.width, y + lineSpacing); //info bar

        //draw itself with the given dimensions
        children.forEach(CommentContainer::draw);
    }

    @Override
    public void mouseMoved() {
        authorButton.mouseMoved();
        children.forEach(child -> child.mouseMoved());
    }

    @Override
    public void fire(int targetx, int targety) {
        if(authorButton.isWithinBounds(targetx,targety))
            authorButton.fire(targetx,targety);
        else {
            action.accept(this);
            clicked = !clicked;
        }

        children.forEach(child -> child.fire(targetx,targety));
    }

    public void changeY(float delta){
        y += delta;
        children.forEach(child -> child.changeY(delta));
    }

    /**
     * Gets height of this CommentContainer and all children, recursively
     * @return Total height
     */
    public int getTotalHeight(){
        int tot = height;
        for (CommentContainer child : children) {
            tot += child.getTotalHeight();
        }
        return tot;
    }
}
