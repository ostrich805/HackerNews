package group1.project.data.queries;

import group1.project.data.DatabaseManager;
import group1.project.data.Story;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Kevin Troisi
 * @author Alex Mahon
 */
public class StoryQuery extends Query {

    public StoryQuery(StorySortType queryType, int chunkSize) {
        this(queryType, ElementOrder.ASCENDING, chunkSize);
    }

    public StoryQuery(StorySortType queryType, ElementOrder order, int chunkSize){
        CHUNK_SIZE = chunkSize;
        try {
            result = DatabaseManager.runQuery("SELECT * FROM STORIES ORDER BY " + queryType.toString() + " " + order);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public StoryQuery(String userSearch, int chunkSize, boolean autoComplete, SearchType searchType){
        if(autoComplete) {
            CHUNK_SIZE = chunkSize;
            String query = "SELECT TOP 10 * FROM STORIES WHERE " +
                    "TITLE LIKE '" + userSearch.trim() + "%' OR " +
                    "TITLE LIKE '% " + userSearch.trim() + "%'";
            try {
                result = DatabaseManager.runQuery(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            CHUNK_SIZE = chunkSize;
            String query = "SELECT * FROM STORIES WHERE TITLE LIKE '%" + userSearch.trim() + "%' " + searchType.getQuerySyntax();
            try {
                result = DatabaseManager.runQuery(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public List<Story> nextChunk(){
        return (List<Story>) nextChunk(DatabaseManager::parseStory);
    }

    @Override
    public List<Story> previousChunk(){
        return (List<Story>) previousChunk(DatabaseManager::parseStory);
    }}
