package controller;

import dao.ChapterDAO;
import dao.LessonDAO;
import dao.LevelDAO;
import database.DatabaseConnection;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.effect.Glow;
import javafx.scene.effect.GaussianBlur;
import javafx.util.Duration;
import model.Chapter;
import model.Lesson;
import model.Level;
import java.sql.Connection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DashboardASLAPPController {
    @FXML private Text chapterText;
    @FXML private ListView<String> chapterListView;
    @FXML private Button basicLevelButton;
    @FXML private Button intermediateLevelButton;
    @FXML private Button advancedLevelButton;

    private Connection conn;
    private int currentLevelId = 1; // Default to Basic Level
    private boolean isLessonsVisible = false;
    private String lastSelectedChapter = null; // Track last selected chapter

    public void initialize() {
        conn = DatabaseConnection.getConnection();
        loadLevels();

        // Add animations to buttons
        addButtonAnimation(basicLevelButton);
        addButtonAnimation(intermediateLevelButton);
        addButtonAnimation(advancedLevelButton);

        if (basicLevelButton != null) {
            basicLevelButton.setOnAction(e -> showBasicLevel());
        }
        if (intermediateLevelButton != null) {
            intermediateLevelButton.setOnAction(e -> showIntermediateLevel());
        }
        if (advancedLevelButton != null) {
            advancedLevelButton.setOnAction(e -> showAdvancedLevel());
        }
        if (chapterListView != null) {
            chapterListView.setOnMouseClicked(e -> showLessons());
        }

        // Add fade-in animation to the chapter text
        addFadeInAnimation(chapterText);
    }

    private void addButtonAnimation(Button button) {
        // Hover animation (scale and glow)
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);

        Glow glow = new Glow(0.5);
        button.setEffect(glow);

        button.setOnMouseEntered(event -> {
            scaleTransition.play();
            glow.setLevel(1.0);
            startPulseAnimation(button); // Start pulse animation on hover
        });

        button.setOnMouseExited(event -> {
            scaleTransition.stop();
            button.setScaleX(1.0);
            button.setScaleY(1.0);
            glow.setLevel(0.5);
            stopPulseAnimation(button); // Stop pulse animation when not hovered
        });

        // Click animation (blur and fade)
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(200), button);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.8);

        GaussianBlur blur = new GaussianBlur(0);
        button.setEffect(blur);

        button.setOnMousePressed(event -> {
            fadeTransition.play();
            blur.setRadius(5);
        });

        button.setOnMouseReleased(event -> {
            fadeTransition.stop();
            button.setOpacity(1.0);
            blur.setRadius(0);
        });
    }

    private void startPulseAnimation(Button button) {
        ScaleTransition pulseTransition = new ScaleTransition(Duration.millis(500), button);
        pulseTransition.setFromX(1.0);
        pulseTransition.setFromY(1.0);
        pulseTransition.setToX(1.05);
        pulseTransition.setToY(1.05);
        pulseTransition.setCycleCount(Animation.INDEFINITE);
        pulseTransition.setAutoReverse(true);
        pulseTransition.play();
    }

    private void stopPulseAnimation(Button button) {
        ScaleTransition pulseTransition = new ScaleTransition(Duration.millis(500), button);
        pulseTransition.stop();
        button.setScaleX(1.0);
        button.setScaleY(1.0);
    }

    private void addFadeInAnimation(Node node) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), node);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();

        // Additional translate animation
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(1000), node);
        translateTransition.setFromY(-50);
        translateTransition.setToY(0);
        translateTransition.play();
    }

    private void loadLevels() {
        List<Level> levels = LevelDAO.getAllLevels(conn);

        for (Level level : levels) {
            switch (level.getId()) {
                case 1:
                    basicLevelButton.setText(level.getName());
                    break;
                case 2:
                    intermediateLevelButton.setText(level.getName());
                    break;
                case 3:
                    advancedLevelButton.setText(level.getName());
                    break;
            }
        }
    }

    public void showBasicLevel() {
        currentLevelId = 1;
        loadChapters(currentLevelId);
    }

    public void showIntermediateLevel() {
        currentLevelId = 2;
        loadChapters(currentLevelId);
    }

    public void showAdvancedLevel() {
        currentLevelId = 3;
        loadChapters(currentLevelId);
    }

    private void loadChapters(int levelId) {
        List<Chapter> chapters = ChapterDAO.getChaptersByLevel(conn, levelId);
        chapterListView.getItems().clear();

        for (Chapter chapter : chapters) {
            chapterListView.getItems().add(chapter.getName());
        }

        chapterText.setText("Chapters for Level " + levelId);
        isLessonsVisible = false;
    }

    @FXML
    private void showLessons() {
        String selectedItem = chapterListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (selectedItem.startsWith("   ➤ ")) {  // It's a lesson
                String lessonName = selectedItem.substring(4);  // Remove the "   ➤ " prefix
                openLessonDetail(lessonName);  // Open the lesson details
            } else {  // It's a chapter
                showLessonsForChapter(selectedItem);  // Show lessons for the chapter
            }
        }
    }

    private void showLessonsForChapter(String selectedChapterName) {
        if (lastSelectedChapter != null && lastSelectedChapter.equals(selectedChapterName)) {
            isLessonsVisible = !isLessonsVisible;
        } else {
            isLessonsVisible = true;
        }

        lastSelectedChapter = selectedChapterName;

        int chapterId = ChapterDAO.getChapterIdByName(conn, selectedChapterName);
        List<Lesson> lessons = LessonDAO.getLessonsByChapter(conn, chapterId);

        loadChaptersWithLessons(currentLevelId, selectedChapterName, isLessonsVisible, lessons);
    }

    private void loadChaptersWithLessons(int levelId, String expandedChapter, boolean showLessons, List<Lesson> lessons) {
        List<Chapter> chapters = ChapterDAO.getChaptersByLevel(conn, levelId);
        List<String> listItems = new ArrayList<>();

        for (Chapter chapter : chapters) {
            listItems.add(chapter.getName());

            if (showLessons && chapter.getName().equals(expandedChapter)) {
                for (Lesson lesson : lessons) {
                    listItems.add("   ➤ " + lesson.getName());
                }
            }
        }

        chapterListView.getItems().setAll(listItems);
    }

    private void openLessonDetail(String lessonName) {
        try {
            System.out.println("Opening lesson: " + lessonName);

            // Get the Lesson ID by Name
            int lessonId = LessonDAO.getLessonIdByName(conn, lessonName);
            if (lessonId <= 0) {
                System.out.println("Error: Lesson ID not found for " + lessonName);
                return;
            }

            System.out.println("Lesson ID: " + lessonId);

            // Fetch Lesson by ID
            Lesson lesson = LessonDAO.getLessonById(conn, lessonId);
            if (lesson == null) {
                System.out.println("Error: No lesson found with ID " + lessonId);
                return;
            }

            // Fetch all lessons for the current chapter
            int chapterId = LessonDAO.getChapterIdByLessonId(conn, lessonId);
            List<Lesson> lessons = LessonDAO.getLessonsByChapter(conn, chapterId);

            // Find the index of the current lesson
            int currentLessonIndex = -1;
            for (int i = 0; i < lessons.size(); i++) {
                if (lessons.get(i).getId() == lessonId) {
                    currentLessonIndex = i;
                    break;
                }
            }

            if (currentLessonIndex == -1) {
                System.out.println("Error: Lesson not found in the list of lessons.");
                return;
            }

            // Load the lesson detail FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/asl_app/lesson_detail.fxml"));
            VBox lessonPage = loader.load();

            // Get the LessonDetailController
            LessonDetailController controller = loader.getController();
            if (controller == null) {
                System.out.println("Error: LessonDetailController not loaded.");
                return;
            }

            // Pass the lesson data, list of lessons, and current lesson index to the controller
            controller.setLessonData(lesson, lessons, currentLessonIndex);

            // Create and show the new stage
            Stage lessonStage = new Stage();
            controller.setStage(lessonStage); // Pass the stage reference to the controller

            lessonStage.setTitle("Lesson Details: " + lesson.getName());
            lessonStage.initModality(Modality.APPLICATION_MODAL);
            lessonStage.setScene(new Scene(lessonPage));
            lessonStage.showAndWait();

        } catch (IOException e) {
            System.out.println("Error loading lesson detail view: " + e.getMessage());
            e.printStackTrace();
        }
    }
}