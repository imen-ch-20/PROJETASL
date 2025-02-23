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

    private Connection connection;
    private List<Lesson> lessons;

    // Initialize the controller, loading lessons for Chapter 1
    public void initialize() {
        loadLessons(1); // Load lessons for Chapter 1 by default
    }

    // Setter method for the connection (to be used by DashboardController)
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    // Load lessons based on chapterId
    public void loadLessons(int chapterId) {
        lessons = LessonDAO.getLessonsByChapter(connection, chapterId);  // Get lessons from the database
        lessonListView.getItems().clear();  // Clear previous list items

        // Debugging Output to check if lessons are loaded
        System.out.println("Loaded " + lessons.size() + " lessons for Chapter " + chapterId);

        // Populate the ListView with lesson names
        for (Lesson lesson : lessons) {
            System.out.println("Adding lesson to ListView: " + lesson.getName());  // Debug line
            lessonListView.getItems().add(lesson.getName());
        }

        // Check if the list is populated
        if (lessons.isEmpty()) {
            System.out.println("No lessons found for Chapter " + chapterId);
        }
    }


    // Method triggered when a user selects a lesson
    @FXML

    public void showLessonContent() {
        int selectedIndex = lessonListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Lesson selectedLesson = lessons.get(selectedIndex);  // Get the selected lesson from the list
            System.out.println("Selected lesson: " + selectedLesson.getName());  // Debug line
            openLessonWindow(selectedLesson);  // Open the lesson details in a new window
        } else {
            System.out.println("No lesson selected.");  // Debug line if no lesson is selected
        }
    }


    // Open a new window to display the lesson content
    private void openLessonWindow(Lesson selectedLesson) {
        try {
            // Load the lesson detail FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/lesson_detail.fxml"));
            Stage stage = new Stage();  // Create a new stage (window)
            Scene scene = new Scene(loader.load());  // Load the FXML into the scene

            // Pass the selected lesson to the LessonDetailController
            LessonDetailController controller = loader.getController();
            controller.setLessonData(selectedLesson);  // Pass the selected lesson to the controller

            stage.setTitle(selectedLesson.getName());  // Set the window title as the lesson name
            stage.setScene(scene);  // Set the scene for the stage
            stage.show();  // Show the new window
        } catch (IOException e) {
            e.printStackTrace();  // Print the stack trace if an error occurs
        }
    }
}
