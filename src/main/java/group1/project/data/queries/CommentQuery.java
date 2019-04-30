package group1.project.data.queries;

import group1.project.data.Comment;
import group1.project.data.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Mahon
 * @author Kevin Troisi
 */
public class CommentQuery extends Query {

    public CommentQuery(CommentType queryType, int chunkSize) {
        this(queryType, ElementOrder.ASCENDING, chunkSize);
    }

    public CommentQuery(CommentType queryType, ElementOrder order, int chunkSize){
        CHUNK_SIZE = chunkSize;
        try {
            result = DatabaseManager.runQuery("SELECT * FROM COMMENTS ORDER BY " + queryType.toString() + " " + order);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Comment> searchCommentsText(String userSearch){
        String query = "SELECT * FROM COMMENTS WHERE TEXT LIKE '%" + userSearch + "%'";
        List<Comment> results = queryToCommentArray(query);
        return results;
    }

    private static List<Comment> queryToCommentArray(String query){
        List<Comment> results = new ArrayList<>();
        try {
            ResultSet rs = DatabaseManager.runQuery(query);
            while (rs.next()){
                Comment newComment = DatabaseManager.parseComment(rs);
                results.add(newComment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    public List<Comment> nextChunk(){
        return (List<Comment>) nextChunk(DatabaseManager::parseComment);
    }

    @Override
    public List<Comment> previousChunk(){
        return (List<Comment>) previousChunk(DatabaseManager::parseComment);
    }
}
