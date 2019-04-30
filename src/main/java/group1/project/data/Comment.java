package group1.project.data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author Kevin Troisi
 * @author Alex Mahon
 */
public class Comment implements Data {

    private final Integer[] childrenIds;
    private final int parent;
    private final String text;
    private final int time;
    private final String username;
    private final int id;

    private Comment[] comments;

    public Comment(int id, int parent, String author, String text, Integer[] childrenIds, int time) {
        this.childrenIds = childrenIds;
        this.parent = parent;
        this.text = text;
        this.time = time;
        this.username = author;
        this.id = id;
    }

    @Override
    public void load(){
        comments = new Comment[childrenIds.length];

        for (int i = 0; i < childrenIds.length; i++) {
            Integer currentId = childrenIds[i];
            comments[i] = DatabaseManager.getComment(currentId);
        }
    }

    public Comment[] getComments() {
        if(comments == null) load();
        return comments;
    }

    public int getParent() {
        return parent;
    }

    public String getText() {
        return text;
    }

    public int getTime() {
        return time;
    }

    public User getAuthor() {
        return DatabaseManager.getUser(username);
    }

    public String getUsername(){
        return username;
    }

    public int getId(){
        return id;
    }

    public int childrenCount(){
        return childrenIds.length;
    }

    /**
     * @author Neil Kilbane
     */
    public String formatDate(){
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma, EEEE MMMM d, yyyy", Locale.ENGLISH);
        return dateTime.format(formatter);
    }
}
