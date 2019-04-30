package group1.project.graphics.screens;

import group1.project.Launcher;
import group1.project.data.Data;
import group1.project.data.DatabaseManager;
import group1.project.data.Story;
import group1.project.data.User;
import group1.project.data.queries.*;
import group1.project.graphics.elements.*;
import processing.core.PFont;
import processing.event.MouseEvent;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Yannick Gloster
 * @author Kevin Troisi
 */
public class HomeScreen extends Screen {
    private final int SEARCH_USER = 0;
    private final int SEARCH_STORY = 1;
    private int chunkSize;
    private DropDown sortBy, searchBy;
    private int searchType;
    private Button graphButton;
    private SearchBar search;
    private ArrowButton leftPage, rightPage;
    private List<Widget> allWidgets = new ArrayList<>();
    private PFont whitney32;
    private int widgetStartY = 63;

    private static int chunk;
    private static Query currentQuery;
    private static List<Widget> displayedQuery = new ArrayList<>();

    public HomeScreen(Launcher p, int width, int height, int backgroundColor){
        super(p, width, height, backgroundColor);
        chunkSize = 10;

        whitney32 = p.createFont("Whitney-MediumSC", 32);

        searchType = SEARCH_STORY;

        //Run Story Query in the background
        if(displayedQuery.isEmpty()) {
            chunk = 0;
            Thread queryThread = new Thread(() -> {
                currentQuery = new StoryQuery(StorySortType.TIME, ElementOrder.DESCENDING, chunkSize);
                addStoryWidgets(currentQuery.nextChunk());
            });
            queryThread.setPriority(Thread.MAX_PRIORITY);
            queryThread.start();
        }

        Map<String, Consumer<Button>> dropDownElements = new LinkedHashMap<>();
        dropDownElements.put("Newest", button -> new Thread
                (() -> {
                    currentQuery = new StoryQuery(StorySortType.TIME, ElementOrder.DESCENDING, chunkSize);
                    addStoryWidgets(currentQuery.nextChunk());
                    chunk = 0;
                }).start());

        dropDownElements.put("Oldest", button -> new Thread
                (() -> {
                    currentQuery = new StoryQuery(StorySortType.TIME, ElementOrder.ASCENDING, chunkSize);
                    addStoryWidgets(currentQuery.nextChunk());
                    chunk = 0;
                }).start());

        dropDownElements.put("Top Score", button -> new Thread(() -> {
            currentQuery = new StoryQuery(StorySortType.SCORE, ElementOrder.DESCENDING, chunkSize);
            addStoryWidgets(currentQuery.nextChunk());
            chunk = 0;
        }).start());

        dropDownElements.put("Alphabetical", button -> new Thread(() -> {
            currentQuery = new StoryQuery(StorySortType.TITLE, chunkSize);
            addStoryWidgets(currentQuery.nextChunk());
            chunk = 0;
        }).start());

        dropDownElements.put("Comment Count", button -> new Thread(() -> {
            currentQuery = new StoryQuery(StorySortType.COMMENT_COUNT, ElementOrder.DESCENDING, chunkSize);
            addStoryWidgets(currentQuery.nextChunk());
            chunk = 0;
        }).start());

        sortBy = new DropDown(p, 500, 35, 100, 20, Colours.ORANGE, "Sort", whitney32, dropDownElements);

        Map<String, Consumer<Button>> searchByDropDown = new LinkedHashMap<>();
        searchByDropDown.put("Story", button ->  {
            searchType = SEARCH_STORY;
        });

        searchByDropDown.put("Author", button ->  {
            searchType = SEARCH_USER;
        });

        searchBy = new DropDown(p, 150, 35, 100, 20, Colours.ORANGE, "Search By", whitney32, searchByDropDown);

        sortBy.fixTextSize();
        search = new SearchBar(p, 275, 35, 200, 20, Colours.ORANGE, whitney32, button -> {});
        leftPage = new ArrowButton(p,p.width - 70, p.height - 30, 25, 20, Colours.ORANGE, p.LEFT, button -> {
            if(chunk > 0) {
                addStoryWidgets(currentQuery.previousChunk());
                chunk--;
            }
        });
        rightPage = new ArrowButton(p,p.width - 35, p.height - 30, 25, 20, Colours.ORANGE, p.RIGHT, button -> {
            addStoryWidgets(currentQuery.nextChunk());
            chunk++;
        });


        Button resetDatabaseButton = new Button(p, 500, 5, 100, 20, Colours.ORANGE, "Reset", whitney32, button -> {
            displayedQuery.clear();
            Thread thread = new Thread(() -> {
                DatabaseManager.loadDatabase();
                currentQuery = new StoryQuery(StorySortType.TIME, ElementOrder.DESCENDING, chunkSize);
                addStoryWidgets(currentQuery.nextChunk());
            });
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();
        });
        graphButton = new Button(p, 150, 5, 100, 20, Colours.ORANGE, "Graphs", whitney32, button -> new Thread(() -> {
            Launcher.moveToScreen(new GraphScreen(p, p.width, p.height, p.color(255)));
        }).start());
        //allWidgets.add(graphButton);
        allWidgets.add(sortBy);
        allWidgets.add(searchBy);
        allWidgets.add(search);
        allWidgets.add(leftPage);
        allWidgets.add(rightPage);
        allWidgets.add(resetDatabaseButton);
    }

    public void draw() {
        super.draw();
        displayedQuery.forEach(Widget::draw);
        drawBanner();
        allWidgets.forEach(Widget::draw);
    }

    public void mousePressed(){
        super.mousePressed();
        Optional<Widget> found;

        Predicate<Widget> widgetAction = widget -> widget.isWithinBounds(p.mouseX,p.mouseY);

        //Check screen widgets first, last added first
        found = allWidgets.stream().filter(widgetAction).min(Collections.reverseOrder());

        if(!found.isPresent())
        found = displayedQuery.stream().filter(widgetAction).min(Collections.reverseOrder());

        found.ifPresent(widget -> widget.fire(p.mouseX, p.mouseY));

        if (!search.isWithinBounds(p.mouseX, p.mouseY)) {
            search.setFocus(false);
        }
    }

    private void addStoryWidgets(List<? extends Data> stories){
        int height = 40;
        int padding = 3;
        displayedQuery = new ArrayList<>();

        for(int i = 0; i <= chunkSize && i < stories.size(); i++)
            displayedQuery.add(new StoryPreviewContainer(p, 20, widgetStartY + (i * (height + padding)),
                    p.width - 100, height, Colours.YELLOW, whitney32, (Story)stories.get(i)));
    }

    private void addUser(List<? extends Data> users){
        int height = 40;
        int padding = 3;
        displayedQuery = new ArrayList<>();

        for(int i = 0; i <= chunkSize && i < users.size(); i++) {
            User tempUser = (User)users.get(i);
            displayedQuery.add(new Button(p, 20, widgetStartY + (i * (height + padding)), p.width - 100, height, Colours.YELLOW, tempUser.getUsername(), whitney32, button -> new Thread(() -> {
                Launcher.moveToScreen(new UserScreen(p, p.width, p.height, p.color(255), tempUser));
            }).start()));
        }
    }

    public void keyPressed() {
        if(search.getFocus()) {
                if(p.key != p.CODED && p.key != p.ENTER && p.key != p.RETURN) {
                    sortBy.setTopLabel("Search");
                    search.append(p.key);
                    if(searchType == SEARCH_STORY) {
                        currentQuery = new StoryQuery(search.getLabel(), chunkSize, true, SearchType.NEW);
                        this.addStoryWidgets(currentQuery.nextChunk());
                    }
                    else {
                        currentQuery = new UserQuery(search.getLabel(), chunkSize, ElementOrder.ASCENDING, UserSortType.USERNAME);
                        this.addUser(currentQuery.nextChunk());
                    }
                }
                if(p.key == p.ENTER || p.key == p.RETURN) {
                    if(searchType == SEARCH_STORY) {
                        currentQuery = new StoryQuery(search.getLabel(), chunkSize, false, SearchType.NEW);
                        this.addStoryWidgets(currentQuery.nextChunk());
                    }
                    else {
                        currentQuery = new UserQuery(search.getLabel(), chunkSize, ElementOrder.ASCENDING, UserSortType.USERNAME);
                        this.addUser(currentQuery.nextChunk());
                    }
                    chunk = 0;
                }
            }
    }

    public void mouseDragged() {
    }

    public void mouseWheel(MouseEvent event) {
        float e = event.getCount();
        if(displayedQuery.get(0).getY() - e > widgetStartY) {
            float difference = (displayedQuery.get(0).getY() - e) - widgetStartY;
            displayedQuery.forEach(story -> story.setY(story.getY() - difference));
        }
        else if (displayedQuery.get(displayedQuery.size()-1).getY() + 40 - e < getHeight() - 3) {
            float difference = (displayedQuery.get(displayedQuery.size()-1).getY() + 40 - e) - (getHeight() - 3);
            displayedQuery.forEach(story -> story.setY(story.getY() - difference));
        }
        else {
            displayedQuery.forEach(story -> story.setY(story.getY() - e));
        }
    }

    public void mouseMoved() {
        displayedQuery.forEach(Widget::mouseMoved);
        allWidgets.forEach(Widget::mouseMoved);
    }
}