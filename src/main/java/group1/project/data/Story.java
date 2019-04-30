package group1.project.data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * @author Kevin Troisi
 */
public class Story implements Data {

    private Comment[] comments;
    private final Integer[] commentIds;
    private final String url;
    private final String title;
    private final String author;
    private final int score;
    private final int time;
    private final int id;

    public Story(Integer[] commentIds, String url, String title, String author, int score, int time, int id) {
        this.commentIds = commentIds;
        this.url = url;
        this.title = title;
        this.author = author;
        this.score = score;
        this.time = time;
        this.id = id;
    }

    @Override
    public void load(){
        comments = new Comment[commentIds.length];

        for (int i = 0; i < commentIds.length; i++) {
            Integer currentId = commentIds[i];
            comments[i] = DatabaseManager.getComment(currentId);
        }
    }

    public Comment[] getComments() {
        if(comments == null) load();
        return comments;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public User getAuthor() {
        return DatabaseManager.getUser(author);
    }

    public String getUsername(){
        return author;
    }

    public int getScore() {
        return score;
    }

    public int getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public int getCommentCount(){
        return commentIds.length;
    }

    /**
     * @author Neil Kilbane
     */
    public String getDate(){
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma, EEEE MMMM d, yyyy", Locale.ENGLISH);
        return dateTime.format(formatter);
    }
}
