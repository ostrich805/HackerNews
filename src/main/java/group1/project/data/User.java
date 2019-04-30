package group1.project.data;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Kevin Troisi
 * @author Alex Mahon
 */
public class User implements Data {

    private final String username;
    private final Integer[] storyIds, commentIds;
    private final int totalStoryScore, totalStoryCommentCount;

    private Story[] stories;
    private Comment[] comments;

    public User(String username, Integer[] storyIds, Integer[] commentIds, int totalStoryScore, int totalStoryCommentCount) {
        this.username = username;
        this.storyIds = storyIds;
        this.commentIds = commentIds;
        this.totalStoryScore = totalStoryScore;
        this.totalStoryCommentCount = totalStoryCommentCount;
    }

    public String getUsername() {
        return username;
    }

    public Story[] getStories() {
        if(stories == null) load();
        return stories;
    }

    public Comment[] getComments() {
        if(comments == null) load();
        System.out.println("Comment count: " + comments.length);
        return comments;
    }

    public int getTotalComments(){
        return commentIds.length;
    }

    public int getTotalStories(){
        return storyIds.length;
    }

    public int getTotalStoryScore() {
        return totalStoryScore;
    }

    public int getTotalStoryCommentCount() {
        return totalStoryCommentCount;
    }

    @Override
    public void load() {
        stories = new Story[storyIds.length];
        Arrays.sort(storyIds, Collections.reverseOrder()); //In descending order

        try {
            for (int i = 0; i < stories.length; i++) {
                Story story = DatabaseManager.getStory(storyIds[i]);
                stories[i] = story;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }

        /*comments = new Comment[commentIds.length];
        Arrays.sort(commentIds, Collections.reverseOrder()); //In descending order

        System.out.print("commentIds: ");
        for (Integer commentId : commentIds) {
            System.out.print(commentId + ",");
        }
        System.out.println();

        for (int i = 0; i < comments.length; i++) {
            Comment comment = DatabaseManager.getComment(commentIds[i]);
            comments[i] = comment;
        }*/

        comments = new Comment[commentIds.length];

        for (int i = 0; i < commentIds.length; i++) {
            Integer currentId = commentIds[i];
            comments[i] = DatabaseManager.getComment(currentId);
        }

    }

    @Override
    public String toString() {
        return this.getUsername();
    }
}
