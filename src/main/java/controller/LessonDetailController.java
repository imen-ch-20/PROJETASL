package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Lesson;

public class LessonDetailController {
    @FXML
    private Label lessonTitle;
    @FXML private TextArea lessonContentText;
    @FXML private ImageView lessonContentImage;

    private Lesson lesson;

    // This method sets the selected lesson
    public void setLessonData(Lesson lesson) {
        this.lesson = lesson;
        loadLessonData();  // Load the data for the selected lesson
    }

    // Load the lesson data (text and image if applicable)
    private void loadLessonData() {
        lessonTitle.setText(lesson.getName());
        lessonContentText.setText(lesson.getContentText());

        // If the lesson has an image, load it
        if (lesson.getContentImage() != null && !lesson.getContentImage().isEmpty()) {
            Image image = new Image("file:" + lesson.getContentImage());  // Adjust path as needed
            lessonContentImage.setImage(image);
            lessonContentImage.setVisible(true);
        } else {
            lessonContentImage.setVisible(false);  // Hide image view if no image exists
        }
    }
}
