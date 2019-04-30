/**
 * @author Alex Mahon
 */

package group1.project.data.queries;

import group1.project.data.DatabaseManager;
import group1.project.data.User;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Kevin Troisi
 * @author Alex Mahon
 */
public class UserQuery extends Query {

    public UserQuery(UserSortType queryType, int chunkSize) {
        this(queryType, ElementOrder.ASCENDING, chunkSize);
    }

    public UserQuery(UserSortType queryType, ElementOrder order, int chunkSize){
        CHUNK_SIZE = chunkSize;

        try {
            switch(queryType) {
                case USERNAME:
                    result = DatabaseManager.runQuery("SELECT username FROM users ORDER BY username " + order);
                    break;
                case POST_COUNT:
                    result = DatabaseManager.runQuery("SELECT username, story_count FROM users ORDER BY story_count" + order);
                    break;
                case COMMENT_COUNT:
                    result = DatabaseManager.runQuery("SELECT username, comment_count FROM users ORDER BY comment_count" + order);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UserQuery(String userSearch, int chunkSize, ElementOrder order, UserSortType queryType){
        CHUNK_SIZE = chunkSize;
        try {
            switch(queryType) {
                case USERNAME:
                    result = DatabaseManager.runQuery("SELECT * FROM USERS WHERE USERNAME LIKE '" + userSearch.trim() + "%'  ORDER BY  USERNAME " + order);
                    break;
                case POST_COUNT:
                    result = DatabaseManager.runQuery("SELECT * FROM USERS WHERE USERNAME LIKE '" + userSearch.trim() + "%'  ORDER BY STORY_COUNT " + order);
                    break;
                case COMMENT_COUNT:
                    result = DatabaseManager.runQuery("SELECT * FROM USERS WHERE USERNAME LIKE '" + userSearch.trim() + "%'  ORDER BY COMMENT_COUNT " + order);
                    break;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> nextChunk() {
        return (List<User>) nextChunk(DatabaseManager::parseUser);
    }

    @Override
    public List<User> previousChunk() {
        return (List<User>) previousChunk(DatabaseManager::parseUser);
    }
}
