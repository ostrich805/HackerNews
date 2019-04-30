package group1.project.graphics.screens;
import group1.project.Launcher;
import group1.project.data.Comment;
import group1.project.data.Story;
import group1.project.data.User;
import group1.project.graphics.elements.*;
import processing.core.PFont;
import processing.event.MouseEvent;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Yannick Gloster
 */
public class UserScreen extends Screen{
    private final int PREVIEW_STORIES = 0;
    private final int PREVIEW_COMMENTS = 1;
    private ArrayList<Integer> currentIndex = new ArrayList<>();
    private int chunkSize;
    private int numPoints, numComments, numPosts;
    private User user;
    private List<Widget> allWidgets = new ArrayList<>();
    private List<Widget> displayedPreviews = new ArrayList<>();
    private int currentPreview;
    private DropDown sortBy;
    private ArrowButton leftPage, rightPage;
    private Button stories, comments;
    private PFont whitney32;

    public UserScreen(Launcher p, int width, int height, int backgroundColor, User user){
        super(p, width, height, backgroundColor);
        currentIndex.add(0);
        chunkSize = 10;
        this.user = user;
        whitney32 = p.createFont("Whitney-MediumSC", 32);
        numComments = user.getTotalComments();
        numPosts = user.getTotalStories();
        numPoints = user.getTotalStoryScore();
        addStories(Arrays.asList(user.getStories()));
        currentPreview = PREVIEW_STORIES;
        Map<String, Consumer<Button>> dropDownElements = new LinkedHashMap<>();
        dropDownElements.put("New", button -> System.out.println("new"));
        dropDownElements.put("Top Score", button -> System.out.println("Top Score"));
        dropDownElements.put("Alphabetical", button -> System.out.println("Alphabetical"));
        sortBy = new DropDown(p, 500, 35, 100, 20, Colours.ORANGE, "Sort", whitney32, dropDownElements);
        leftPage = new ArrowButton(p,p.width - 70, p.height - 30, 25, 20, Colours.ORANGE, p.LEFT, button -> {
            try {
                if (currentIndex.get(currentIndex.size() - 3) > 0) {
                    currentIndex.remove(currentIndex.size() - 1);
                    currentIndex.remove(currentIndex.size() - 1);
                    if (currentPreview == PREVIEW_STORIES) {
                        addStories(Arrays.asList(user.getStories()));
                    }
                    if (currentPreview == PREVIEW_COMMENTS) {
                        List<Comment> comments = Arrays.asList(user.getComments());
                        comments.removeIf(Objects::isNull); //Remove null comments
                        addComments(comments);
                    }
                }
            }
            catch (Exception e) {
                //e.printStackTrace();
            }
        });
        rightPage = new ArrowButton(p,p.width - 35, p.height - 30, 25, 20, Colours.ORANGE, p.RIGHT, button -> {
            if(currentPreview == PREVIEW_STORIES && currentIndex.get(currentIndex.size()-1) < user.getStories().length) {
                addStories(Arrays.asList(user.getStories()));
            }
            if(currentPreview == PREVIEW_COMMENTS && currentIndex.get(currentIndex.size()-1) < user.getComments().length) {
                addComments(Arrays.asList(user.getComments()));
            }
        });
        stories = new Button(p, p.width - 160, 135, 80, 20, Colours.ORANGE, "Stories", whitney32, button -> {
            currentIndex = new ArrayList<>();
            currentIndex.add(0);
            addStories(Arrays.asList(user.getStories()));
            currentPreview = PREVIEW_STORIES;
            System.out.println("user - Stories");
        });
        comments = new Button(p, p.width - 250, 135, 80, 20, Colours.ORANGE, "Comments", whitney32, 25, button -> {
            currentIndex = new ArrayList<>();
            currentIndex.add(0);
            List<Comment> comments = Arrays.asList(user.getComments());
            List<Comment> nonNullComments = new ArrayList<>();
            comments.forEach(comment -> {
                if(comment != null) nonNullComments.add(comment);
            });
            System.out.println("NONULLCOMMENTS: " + nonNullComments.size());
            addComments(nonNullComments);
            currentPreview = PREVIEW_COMMENTS;
            System.out.println("user - Comments");
        });
        //allWidgets.add(sortBy);
        allWidgets.add(leftPage);
        allWidgets.add(rightPage);
        allWidgets.add(stories);
        allWidgets.add(comments);
    }

    public void draw() {
        super.draw();
        for(Widget preview : displayedPreviews) {
            preview.draw();
        }
        for(Widget widget : allWidgets) {
            widget.draw();
        }
        p.fill(255);
        p.fill(0);
        p.textFont(whitney32);
        p.textAlign(p.RIGHT);
        p.text(user.getUsername(), 640, 82);
        p.textSize(15);
        p.text("Comment Points: " + numPoints, 640, 95);
        p.text("Number of Comments: " + numComments, 640, 110);
        p.text("Number of Posts: " + numPosts, 640, 125);
        p.textAlign(p.LEFT);
        p.textSize(32);
        drawProfileLogo(640 - p.textWidth(user.getUsername()) - 15, 61, 10, 20);
        drawBanner();
        radarChart();
    }

    private void drawProfileLogo(float x, float y, float width, float height) {
        p.stroke(0);
        p.strokeWeight(1);
        p.line(x, y+height, x+width, y+height);
        p.line(x, y+height, x+width/2, y+3*height/4);
        p.line(x+width, y+height, x+width/2, y+3*height/4);
        p.noFill();
        p.ellipseMode(p.CENTER);
        p.circle(x+width/2, y+height/2, height/2);
    }

    private void addComments(List<Comment> comments) {
        int startY = 63;
        int height = 40;
        int padding = 3;
        displayedPreviews = new ArrayList<>();
        try {
            int index = currentIndex.get(currentIndex.size()-1);
            int i = 0;
            do {
                if(comments.get(index) != null) {
                    displayedPreviews.add(new CommentPreviewContainer(p, 20, startY + (i * (height + padding)),
                            p.width - 300, height, Colours.YELLOW, whitney32, comments.get(index)));
                    System.out.println("comment");
                }
                index++;
                i++;
            }
            while (displayedPreviews.size() <= chunkSize && index < comments.size());
            currentIndex.add(index);
        }
        catch(Exception e) {
            System.err.println(e);
        }
        currentPreview = PREVIEW_COMMENTS;
    }
    
    private void addStories(List<Story> stories) {
        int startY = 63;
        int height = 40;
        int padding = 3;
        displayedPreviews = new ArrayList<>();
        try {
            int index = currentIndex.get(currentIndex.size()-1);
            int i = 0;
            do {
                if (stories.get(index) != null) {
                    displayedPreviews.add(new StoryPreviewContainer(p, 20, startY + (i * (height + padding)),
                            p.width - 300, height, Colours.YELLOW, whitney32, stories.get(index)));
                }
                index++;
                i++;
            } while (displayedPreviews.size() <= chunkSize && index < stories.size());
            currentIndex.add(index);
        }
        catch(Exception e) {
            System.err.println(e);
        }
        currentPreview = PREVIEW_STORIES;
    }

    public void mouseWheel(MouseEvent event) {
        float e = event.getCount();
        if(displayedPreviews.get(0).getY() - e > 63) {
            float difference = (displayedPreviews.get(0).getY() - e) - 63;
            displayedPreviews.forEach(story -> story.setY(story.getY() - difference));
        }
        else if (displayedPreviews.get(displayedPreviews.size()-1).getY() + 40 - e < getHeight() - 3) {
            float difference = (displayedPreviews.get(displayedPreviews.size()-1).getY() + 40 - e) - (getHeight() - 3);
            displayedPreviews.forEach(story -> story.setY(story.getY() - difference));
        }
        else {
            displayedPreviews.forEach(story -> story.setY(story.getY() - e));
        }
    }

    public void mouseMoved() {
        displayedPreviews.forEach(Widget::mouseMoved);
        allWidgets.forEach(Widget::mouseMoved);
    }

    private void radarChart(){
        int xScale = 0;
        int yScale = 0;
        RadarChartAxes[] axis;
        RadarChartDataPoint[] dataPoints;

        axis = new RadarChartAxes[4];
        axis[0] = new RadarChartAxes("Comments");
        axis[1] = new RadarChartAxes("Story Score");
        axis[2] = new RadarChartAxes("Stories");
        axis[3] = new RadarChartAxes("Story Comment Count");

        do {
            xScale += 200;
        } while(xScale < Math.max(user.getTotalComments(), user.getTotalStories()));

        do {
            yScale+= 200;
        } while(yScale < Math.max(user.getTotalStoryScore(), user.getTotalStoryCommentCount()));

        dataPoints = new RadarChartDataPoint[4];
        dataPoints[0] = new RadarChartDataPoint(user.getTotalComments(), xScale);
        dataPoints[1] = new RadarChartDataPoint(user.getTotalStoryScore(),yScale);
        dataPoints[2] = new RadarChartDataPoint(user.getTotalStories(), xScale);
        dataPoints[3] = new RadarChartDataPoint(user.getTotalStoryCommentCount(),yScale);

        RadarChart rc = new RadarChart(p,470,200,200,200,10, axis);

        rc.drawAxis(); //draw the axis
        rc.drawDataPoints(82, 89, 233, 150, dataPoints); //draw the data and labels
    }

    public void mousePressed() {
        super.mousePressed();
        Optional<Widget> found;

        Predicate<Widget> widgetAction = widget -> widget.isWithinBounds(p.mouseX,p.mouseY);

        //Check screen widgets first, last added first
        found = allWidgets.stream().filter(widgetAction).min(Collections.reverseOrder());

        if(!found.isPresent())
            found = displayedPreviews.stream().filter(widgetAction).min(Collections.reverseOrder());

        found.ifPresent(widget -> widget.fire(p.mouseX, p.mouseY));
    }

}