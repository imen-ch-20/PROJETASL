package controller;

import dao.LessonDAO;
import model.Lesson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class LessonController {

    @FXML
    private ListView<String> lessonListView;  // ListView for displaying lesson names

    private Connection connection;  // Database connection
    private List<Lesson> lessons;  // List of lessons for the selected chapter

    /**
     * Initializes the controller.
     * Loads lessons for Chapter 1 by default.
     */
    @FXML
    public void initialize() {
        loadLessons(1); // Load lessons for Chapter 1 by default
    }

    /**
     * Sets the database connection.
     *
     * @param connection The database connection to be used.
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * Loads lessons for a specific chapter and populates the ListView.
     *
     * @param chapterId The ID of the chapter for which lessons are to be loaded.
     */
    public void loadLessons(int chapterId) {
        if (connection == null) {
            System.err.println("Database connection is not set.");
            return;
        }

        // Fetch lessons from the database
        lessons = LessonDAO.getLessonsByChapter(connection, chapterId);

        // Clear the ListView before populating it
        lessonListView.getItems().clear();

        // Debugging: Print the number of lessons loaded
        System.out.println("Loaded " + lessons.size() + " lessons for Chapter " + chapterId);

        // Populate the ListView with lesson names
        for (Lesson lesson : lessons) {
            System.out.println("Adding lesson to ListView: " + lesson.getName());  // Debug line
            lessonListView.getItems().add(lesson.getName());
        }

        // Debugging: Check if the list is empty
        if (lessons.isEmpty()) {
            System.out.println("No lessons found for Chapter " + chapterId);
        }
    }

    /**
     * Handles the event when a lesson is selected from the ListView.
     * Opens a new window to display the selected lesson's content.
     */
    @FXML
    public void showLessonContent() {
        int selectedIndex = lessonListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            // Get the selected lesson from the list
            Lesson selectedLesson = lessons.get(selectedIndex);

            // Debugging: Print the selected lesson's name
            System.out.println("Selected lesson: " + selectedLesson.getName());

            // Open the lesson details in a new window
            openLessonWindow(selectedLesson, lessons, selectedIndex);
        } else {
            System.out.println("No lesson selected.");  // Debug line if no lesson is selected
        }
    }

    /**
     * Opens a new window to display the content of the selected lesson.
     *
     * @param selectedLesson The lesson whose content is to be displayed.
     * @param lessons The list of lessons for the current chapter.
     * @param currentLessonIndex The index of the currently displayed lesson.
     */
    private void openLessonWindow(Lesson selectedLesson, List<Lesson> lessons, int currentLessonIndex) {
        try {
            // Load the lesson detail FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/lesson_detail.fxml"));
            Stage stage = new Stage();  // Create a new stage (window)
            Scene scene = new Scene(loader.load());  // Load the FXML into the scene

            // Get the controller for the lesson detail view
            LessonDetailController controller = loader.getController();

            // Pass the selected lesson, list of lessons, and current lesson index to the controller
            controller.setLessonData(selectedLesson, lessons, currentLessonIndex);

            // Set the window title to the lesson name
            stage.setTitle(selectedLesson.getName());

            // Set the scene for the stage
            stage.setScene(scene);

            // Show the new window
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to open lesson detail window.");
            e.printStackTrace();  // Print the stack trace if an error occurs
        }
    }
}