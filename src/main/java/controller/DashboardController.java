package controller;

import dao.ChapterDAO;
import dao.LessonDAO;
import dao.LevelDAO;
import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import model.Chapter;
import model.Lesson;
import model.Level;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class DashboardController {
    @FXML private Text chapterText;
    @FXML private ListView<String> chapterListView;
    @FXML private Button basicLevelButton;
    @FXML private Button intermediateLevelButton;
    @FXML private Button advancedLevelButton;

    private Connection conn;
    private int currentLevelId = 1; // Default to Basic Level
    private boolean isLessonsVisible = false;
    private String lastSelectedChapter = null; // To track which chapter was last clicked

    public void initialize() {
        // Establish database connection
        conn = DatabaseConnection.getConnection();

        // Load levels dynamically
        loadLevels();

        // Set up event handlers for level buttons
        basicLevelButton.setOnAction(e -> showBasicLevel());
        intermediateLevelButton.setOnAction(e -> showIntermediateLevel());
        advancedLevelButton.setOnAction(e -> showAdvancedLevel());

        // Set up chapter selection event
        chapterListView.setOnMouseClicked(e -> showLessons());
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

    // Event handlers for level selection
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

        chapterListView.getItems().clear(); // Clear previous chapters

        for (Chapter chapter : chapters) {
            chapterListView.getItems().add(chapter.getName()); // Add chapter name
        }

        chapterText.setText("Chapters for Level " + levelId);
        isLessonsVisible = false; // Reset lesson visibility
    }

    public void showLessons() {
        String selectedChapterName = chapterListView.getSelectionModel().getSelectedItem();

        if (selectedChapterName == null || selectedChapterName.startsWith("   ")) {
            return; // Ignore clicks on lessons (they start with spaces for indentation)
        }

        // Toggle lesson visibility if the same chapter is clicked
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
            listItems.add(chapter.getName()); // Add chapter name

            // If this chapter is clicked, show its lessons below it
            if (showLessons && chapter.getName().equals(expandedChapter)) {
                for (Lesson lesson : lessons) {
                    listItems.add("   ➤ " + lesson.getName()); // Indented lesson
                }
            }
        }

        chapterListView.getItems().setAll(listItems);
    }

    // Navigate to the lesson detail page when a lesson is clicked
    private void navigateToLessonDetail(Lesson lesson) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/lesson_detail.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            // Pass lesson data to the lesson detail controller
            LessonDetailController controller = loader.getController();
            controller.setLessonData(lesson);

            stage.setTitle(lesson.getName());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
