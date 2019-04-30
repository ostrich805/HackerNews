package group1.project.data;

import processing.core.PApplet;
import processing.data.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * @author Kevin Troisi
 */
public class FileLoader {

    private static final String FILE_PATH = "hackerNews1M.json";
    private static final String FILE_PATH_DEFAULT = "news.json";
    private static PApplet p;

    public FileLoader(PApplet p){
        initialise(p);
    }

    public static void initialise(PApplet p){
        FileLoader.p = p;
    }

    public static void loadEntireFile() throws IOException, SQLException {
        System.out.println("Loading up entire file...");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(FILE_PATH_DEFAULT);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while((line = br.readLine()) != null)
            loadLine(line);

        DatabaseManager.buildUsersTable();

        /**
         * @author Yannick Gloster
         */
        /*try(InputStream is = classloader.getResourceAsStream(FILE_PATH)){
            br = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = br.readLine()) != null)
                loadLine(line);

            DatabaseManager.buildUsersTable();
        } catch (Exception e1){
            try(InputStream is = classloader.getResourceAsStream(FILE_PATH_DEFAULT)){
                br = new BufferedReader(new InputStreamReader(is));
                String line;
                while((line = br.readLine()) != null)
                    loadLine(line);

                DatabaseManager.buildUsersTable();
            } catch(Exception e2){
                e2.printStackTrace();
            }
        }*/

        /*
        System.out.println(tests);
        BufferedReader br;*/

    }

    private static void loadLine(String line) throws SQLException {
        //Load up this line as a JSON object
        JSONObject object = p.parseJSONObject(line);

        // Ignore wrongly-formatted items
        String type = object.getString("type");
        if (type == null) return;

        // Ignore deleted items
        if (object.hasKey("deleted")) return;

        if (type.equals("story")) {
            if(!object.hasKey("id") && !object.hasKey("title") && !object.hasKey("by")
                    && !object.hasKey("score") && !object.hasKey("time")) return;

            String title = object.getString("title");
            if(title == null) return;
            title = title.trim().replaceAll("\n", "");

            int id = object.getInt("id");

            String url = object.getString("url");

            String author = object.getString("by");

            int score = object.getInt("score");
            int time = object.getInt("time");

            Integer[] commentIds = object.hasKey("kids") ?
                    Arrays.stream(object.getJSONArray("kids").getIntArray()).boxed().toArray(Integer[]::new) :
                    new Integer[0];

            DatabaseManager.insertStory(id,title,author,url,time,score,commentIds);

        } else if (type.equals("comment")){
            if(!object.hasKey("id") && !object.hasKey("parent") && !object.hasKey("text")
                    && !object.hasKey("time") && !object.hasKey("by")) return;

            int id = object.getInt("id");
            String text = object.getString("text");
            int time = object.getInt("time");
            int parent = object.getInt("parent");
            String author = object.getString("by");

            Integer[] commentIds = object.hasKey("kids") ?
                    Arrays.stream(object.getJSONArray("kids").getIntArray()).boxed().toArray(Integer[]::new) :
                    new Integer[0];

            DatabaseManager.insertComment(id,parent,author,text,commentIds,time);
        }
    }
}
