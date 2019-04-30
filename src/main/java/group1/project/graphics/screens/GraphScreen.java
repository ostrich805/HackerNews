/**
 * @author Yannick Gloster
 */

package group1.project.graphics.screens;

import group1.project.Launcher;
import group1.project.data.User;
import group1.project.data.queries.ElementOrder;
import group1.project.data.queries.UserQuery;
import group1.project.data.queries.UserSortType;
import group1.project.graphics.elements.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GraphScreen extends Screen {
    private PieChart topUsersScores;
    private List<Widget> allWidgets = new ArrayList<>();

    public GraphScreen(Launcher p, int width, int height, int backgroundColor) {
        super(p, width, height, backgroundColor);
        List<User> top10Users = new UserQuery("", 10, ElementOrder.ASCENDING, UserSortType.POST_COUNT).nextChunk();
        Map<Object, Object> users = new LinkedHashMap<>();
        for(User user : top10Users) {
            users.put(user.getUsername(), user.getTotalStories());
        }
        topUsersScores = new PieChart(p, 20, 63, 400, 400, users);
    }

    public void draw() {
        super.draw();
        topUsersScores.draw();
        drawBanner();
    }
}
