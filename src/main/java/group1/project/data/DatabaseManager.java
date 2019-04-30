package group1.project.data;

import java.sql.*;
import java.util.Arrays;

/**
 * Handles database connection, tables, indices etc.
 * @author Kevin Troisi
 */
public class DatabaseManager {

    private static Connection con;
    private static PreparedStatement insertStory, insertUser, insertComment;

    public static void initialise(){
        try {
            con = DriverManager.getConnection("jdbc:h2:~/group1/jsondata", "sa", "");
            createTables();
            createIndices();
            prepareStatements();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createIndices() throws SQLException {
        executeStatement("CREATE INDEX IF NOT EXISTS index_stories ON stories (id,author,title,time,score,comment_count)");
        executeStatement("CREATE INDEX IF NOT EXISTS index_stories_score ON stories (score DESC,time DESC)");
        executeStatement("CREATE INDEX IF NOT EXISTS index_stories_time ON stories (time DESC,score)");
        executeStatement("CREATE INDEX IF NOT EXISTS index_stories_title ON stories (title,time,score)");
        executeStatement("CREATE INDEX IF NOT EXISTS index_comments ON comments (id,author,text,time)");
        executeStatement("CREATE INDEX IF NOT EXISTS index_stories_authors ON stories (author,id,score,comment_count)");
        executeStatement("CREATE INDEX IF NOT EXISTS index_comments_authors ON comments (author,id)");
        executeStatement("CREATE INDEX IF NOT EXISTS index_users ON users (username,story_count,story_score,story_comment_count,comment_count)");
    }

    private static void createTables() throws SQLException {
        createStoryTable();
        createCommentTable();
        createUsersTable();
    }

    /**
     * Deletes database and re-imports it from the JSON file
     */
    public static void loadDatabase(){
        try {
            executeStatement("DROP ALL OBJECTS ");
            createTables();
            createIndices();
            long startTime = System.currentTimeMillis();
            FileLoader.loadEntireFile();
            System.out.printf("Load time: %dms\n", System.currentTimeMillis() - startTime);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void prepareStatements() throws SQLException {
        insertStory = con.prepareStatement("merge into STORIES KEY (ID) values(?,?,?,?,?,?,?,?)");
        insertUser = con.prepareStatement("merge into USERS KEY (USERNAME) values(?,?,?,?,?,?,?)");
        insertComment = con.prepareStatement("merge into COMMENTS KEY (ID) values(?,?,?,?,?,?)");
    }

    private static void executeStatement(String statement) throws SQLException {
        Statement stmt = con.createStatement();
        stmt.executeUpdate(statement);
        stmt.close();
    }

    private static void createStoryTable() throws SQLException {
        executeStatement("CREATE TABLE IF NOT EXISTS " +
                "STORIES " +
                "(ID integer NOT NULL, " +
                "TITLE varchar NOT NULL, " +
                "AUTHOR varchar NOT NULL, " +
                "URL varchar, " +
                "TIME integer NOT NULL, " +
                "SCORE integer NOT NULL, " +
                "COMMENT_COUNT integer NOT NULL, " +
                "COMMENTS array," +
                "PRIMARY KEY (ID))");
    }

    private static void createUsersTable() throws SQLException {
        executeStatement("CREATE TABLE IF NOT EXISTS " +
                "USERS " +
                "(USERNAME varchar NOT NULL, " +
                "STORY_COUNT integer NOT NULL, " +
                "STORY_IDS array NOT NULL, " +
                "STORY_SCORE integer NOT NULL, " +
                "STORY_COMMENT_COUNT integer NOT NULL, " +
                "COMMENT_COUNT integer, " +
                "COMMENT_IDS array, " +
                "PRIMARY KEY (USERNAME))");
    }

    private static void createCommentTable() throws SQLException {
        executeStatement("CREATE TABLE IF NOT EXISTS " +
                "COMMENTS " +
                "(ID integer NOT NULL, " +
                "PARENT integer NOT NULL, " +
                "AUTHOR varchar NOT NULL, " +
                "TEXT varchar NOT NULL, " +
                "COMMENTS array NOT NULL, " +
                "TIME integer NOT NULL, " +
                "PRIMARY KEY (ID))");
    }

    public static void insertStory(int id, String title, String author, String url, int time, int score, Integer[] kids) throws SQLException {
        insertStory.setInt(1,id);
        insertStory.setString(2,title);
        insertStory.setString(3,author);
        insertStory.setString(4,url);
        insertStory.setInt(5,time);
        insertStory.setInt(6,score);
        insertStory.setInt(7,kids.length);
        insertStory.setArray(8,con.createArrayOf("integer",kids));
        insertStory.executeUpdate();
    }

    public static void insertComment(int id, int parent, String author, String text, Integer[] commentIds, int time) throws SQLException {
        insertComment.setInt(1,id);
        insertComment.setInt(2,parent);
        insertComment.setString(3,author);
        insertComment.setString(4,text);
        insertComment.setArray(5,con.createArrayOf("integer",commentIds));
        insertComment.setInt(6,time);
        insertComment.executeUpdate();
    }

    public static void insertUser(String userName, Integer[] storyIds, int storyScore,
                                  int storyCommentCount, Integer[] commentsIds) throws SQLException {
        insertUser.setString(1,userName);
        insertUser.setInt(2, storyIds.length);
        insertUser.setArray(3,con.createArrayOf("integer", storyIds));
        insertUser.setInt(4, storyScore);
        insertUser.setInt(5, storyCommentCount);
        insertUser.setInt(6, commentsIds.length);
        insertUser.setArray(7,con.createArrayOf("integer", commentsIds));
        insertUser.executeUpdate();
        System.out.println("Inserted user " + userName);
    }

    public static void buildUsersTable() throws SQLException {
        System.out.println("Building Users table...");
        long startTime = System.currentTimeMillis();

        ResultSet sq = con.createStatement().executeQuery("SELECT author, GROUP_CONCAT(id) AS story_ids, " +
                "SUM (score) AS story_score, SUM (comment_count) AS story_comment_count" +
                " FROM stories GROUP BY author");

        System.out.printf("Ran stories query in: %dms\n",System.currentTimeMillis()-startTime);

        PreparedStatement stmt = con.prepareStatement("MERGE INTO users(username,story_count,story_ids,story_score,story_comment_count)" +
                " KEY(username) VALUES(?,?,?,?,?)");

        while(sq.next()){
            //Merge user with everything but commentIds
            stmt.setString(1, sq.getString(1)); //username
            String storyIdsString = sq.getString(2);
            Integer[] storyIds = Arrays.stream(storyIdsString.split(",")).mapToInt(Integer::valueOf).boxed().toArray(Integer[]::new);
            stmt.setInt(2,storyIds.length); //story_count
            stmt.setArray(3, con.createArrayOf("integer", storyIds));

            stmt.setInt(4, sq.getInt(3)); //story_score
            stmt.setInt(5, sq.getInt(4)); //story_comment_count

            stmt.executeUpdate();
        }

        ResultSet cq = con.createStatement().executeQuery("SELECT GROUP_CONCAT(id) as comment_ids" +
                " FROM comments GROUP BY author");

        System.out.printf("Ran comments query in: %dms\n",System.currentTimeMillis()-startTime);

        PreparedStatement cstmt = con.prepareStatement("MERGE INTO users(comment_count,comment_ids) KEY(username) VALUES(?,?)");

        while(cq.next()){
            String commentIdsString = cq.getString(1);
            Integer[] commentIds = Arrays.stream(commentIdsString.split(",")).mapToInt(Integer::valueOf).boxed().toArray(Integer[]::new);
            cstmt.setInt(1,commentIds.length);
            cstmt.setArray(2, con.createArrayOf("integer", commentIds));
        }

        System.out.printf("Time to build user table: %dms\n",System.currentTimeMillis()-startTime);
    }

    /**
     * Runs a query against the database with scroll insensitivity
     * @param query The SQL query string
     * @return ResultSet of the query
     * @throws SQLException If something goes wrong executing the query
     */
    public static ResultSet runQuery(String query) throws SQLException {
        ResultSet rs = con.createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY)
                .executeQuery(query);
        return rs;
    }

    public static Story parseStory(ResultSet rs) {
        try {
            String title = "" + rs.getString("title");
            String author = rs.getString("author");
            String url = rs.getString("url");
            int time = rs.getInt("time");
            int score = rs.getInt("score");
            int id = rs.getInt("id");

            Array childrenArray = rs.getArray("comments");
            Integer[] childrenIds = parseArray(childrenArray);
            childrenArray.free();

            return new Story(childrenIds, url, title, author, score, time, id);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    private static Integer[] parseArray(Array array) throws SQLException {
        Object[] objarr = (Object[]) array.getArray();
        return Arrays.stream(objarr).mapToInt(a -> (Integer) a).boxed().toArray(Integer[]::new);
    }

    public static Comment parseComment(ResultSet rs) {
        try {
            int id = rs.getInt("id");
            int parent = rs.getInt("parent");
            String author = rs.getString("author");
            String text = rs.getString("text");

            Array childrenArray = rs.getArray("comments");
            Integer[] childrenIds = parseArray(childrenArray);
            childrenArray.free();

            int time = rs.getInt("time");
            return new Comment(id, parent, author, text, childrenIds, time);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static User parseUser(ResultSet rs) {
        try {
            String username = rs.getString(1);

            Array storyIdsArray = rs.getArray(3);
            Integer[] storyIds = parseArray(storyIdsArray);
            storyIdsArray.free();

            int totalStoryScore = rs.getInt(4);
            int totalStoryCommentCount = rs.getInt(5);

            Array commentIdsArray = rs.getArray(3);
            Integer[] commentIds = parseArray(commentIdsArray);
            commentIdsArray.free();

            return new User(username,storyIds,commentIds,totalStoryScore,totalStoryCommentCount);

        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static User getUser(String username) {
        try {
            ResultSet rs = runQuery("SELECT * FROM users WHERE username='" + username + "'");
            if(!rs.next()) throw new SQLException();
            return parseUser(rs);
        } catch (SQLException e) {
            System.err.println("User " + username + " not found!");
            return null;
        }
    }

    public static Story getStory(int id) throws SQLException {
        ResultSet rs = runQuery("SELECT * FROM stories WHERE id=" + id);
        rs.next();
        return parseStory(rs);
    }

    public static Comment getComment(Integer id) {
        try {
            ResultSet rs = runQuery("SELECT * FROM COMMENTS WHERE id=" + id.toString());
            if(!rs.next()) throw new SQLException();
            return parseComment(rs);
        } catch (SQLException e){
            System.err.println("Could not find comment!");
            return null;
        }
    }
}
