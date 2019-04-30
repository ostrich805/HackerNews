/**
 * @author Neil Kilbane
 * @author Yannick Gloster
 */

package group1.project.graphics.elements;

import group1.project.data.Comment;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;

public class CommentPreviewContainer extends Button{
    private Comment comment;
    private PFont widgetFont;
    private boolean expanded;

    public CommentPreviewContainer(PApplet p, int x, int y, int width, int height, int widgetColor, PFont widgetFont, Comment comment) {
        super(p, x, y, width, height, widgetColor, button ->
        {
            if(button.getExpandable())
            {
                button.expand();
            }
            else if(button.getExpanded())
            {
                button.contract();
            }
        });
        this.widgetFont = widgetFont;
        this.comment = comment;
        expandable = false;
        expanded = false;
    }

    public void draw() {
        super.draw();

        //If comment is not in current dataset don't try to draw it
        if(comment == null) return;

        p.color(255);
        p.textFont(widgetFont);
        p.textAlign(PConstants.LEFT);
        p.textSize(16);
        p.text(comment.getUsername(), x + 5 , y + 14);
        p.textSize(14);
        String commentText = comment.getText();
        String[] commentTextArray = commentText.split(" ");
        String commentLine = "";
        int lineNumber = 2;
        for(int index = 0; index < commentTextArray.length; index++) {
                if (p.textWidth(commentLine + commentTextArray[index]) < width - 5) {
                    commentLine += commentTextArray[index] + " ";
                }
                else if((lineNumber + 2) * 14 < height) {
                    p.text(commentLine, x + 5, y + lineNumber * 14 + 5);
                    index--;
                    commentLine = "";
                    lineNumber++;
                    expandable = false;
                }
                else{
                    commentLine += "...";
                    expandable = true;
                    break;
                }
        }
        while(height > (lineNumber + 2) * 14)
        {
            height--;
        }
        p.text(commentLine, x + 5, y + lineNumber * 14 + 5);
        p.textAlign(PConstants.RIGHT);
        p.textSize(16);
        p.text(comment.formatDate(), x + width - 5, y + 14);

    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public int getHeight() {
        return  height;
    }
}
