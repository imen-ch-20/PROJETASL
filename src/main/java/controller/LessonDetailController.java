package controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Lesson;

import java.util.List;

public class LessonDetailController {

    @FXML private Label lessonTitle;
    @FXML private TextArea lessonContentText;
    @FXML private ImageView lessonContentImage;
    @FXML private VBox contentContainer;

    private Stage stage; // Reference to the stage
    private List<Lesson> lessons;
    private int currentLessonIndex;

    /**
     * Sets the stage reference for this controller.
     *
     * @param stage The stage to be closed.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Sets the lesson data and updates the UI.
     *
     * @param lesson The lesson to display.
     * @param lessons The list of lessons for the current chapter.
     * @param currentLessonIndex The index of the currently displayed lesson.
     */
    public void setLessonData(Lesson lesson, List<Lesson> lessons, int currentLessonIndex) {
        this.lessons = lessons;
        this.currentLessonIndex = currentLessonIndex;
        updateUI(lesson);
    }

    /**
     * Updates the UI with the given lesson data.
     *
     * @param lesson The lesson to display.
     */
    private void updateUI(Lesson lesson) {
        lessonTitle.setText(lesson.getName());

        boolean hasText = lesson.getContentText() != null && !lesson.getContentText().isEmpty();
        boolean hasImage = lesson.getContentImage() != null && !lesson.getContentImage().isEmpty();

        // Clear the container before adding new content
        contentContainer.getChildren().clear();

        if (hasText && hasImage) {
            // Both text and image
            lessonContentText.setText(lesson.getContentText());
            lessonContentText.setVisible(true);

            try {
                Image image = new Image("file:" + lesson.getContentImage());
                lessonContentImage.setImage(image);
                lessonContentImage.setVisible(true);

                // Dynamically resize the image to fit within the screen
                lessonContentImage.setFitWidth(600); // Set a reasonable width
                lessonContentImage.setFitHeight(400); // Set a reasonable height
                lessonContentImage.setPreserveRatio(true); // Maintain aspect ratio
            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());
                lessonContentImage.setVisible(false);
            }

            // Create a horizontal split container for text and image
            HBox splitContainer = new HBox();
            splitContainer.setSpacing(20);
            splitContainer.setAlignment(Pos.CENTER);

            // Add text area to the left half
            VBox textContainer = new VBox(lessonContentText);
            textContainer.setAlignment(Pos.CENTER);
            textContainer.setPrefWidth(600); // Set a preferred width for the text area
            textContainer.setMaxWidth(600);  // Prevent the text area from expanding too much

            // Add image view to the right half
            VBox imageContainer = new VBox(lessonContentImage);
            imageContainer.setAlignment(Pos.CENTER);
            imageContainer.setPrefWidth(600); // Set a preferred width for the image
            imageContainer.setMaxWidth(600);  // Prevent the image from expanding too much

            // Add both containers to the split container
            splitContainer.getChildren().addAll(textContainer, imageContainer);

            // Add the split container to a ScrollPane
            ScrollPane scrollPane = new ScrollPane(splitContainer);
            scrollPane.setFitToWidth(true); // Ensure the content fits within the width of the ScrollPane
            scrollPane.setFitToHeight(true); // Ensure the content fits within the height of the ScrollPane

            // Add the ScrollPane to the main content container
            contentContainer.getChildren().add(scrollPane);

            // Set preferred size for the content container
            contentContainer.setPrefWidth(1200); // Set a reasonable width
            contentContainer.setPrefHeight(800); // Set a reasonable height
        } else if (hasText) {
            // Only text
            lessonContentText.setText(lesson.getContentText());
            lessonContentText.setVisible(true);
            lessonContentImage.setVisible(false);

            // Center the text area
            contentContainer.getChildren().clear();
            contentContainer.getChildren().add(lessonContentText);

            // Make the text area resize dynamically
            lessonContentText.setPrefWidth(800); // Set a preferred width for the text area
            lessonContentText.setPrefHeight(600); // Set a preferred height for the text area
            lessonContentText.setWrapText(true); // Ensure text wraps to the next line
        } else if (hasImage) {
            // Only image
            lessonContentText.setVisible(false);
            try {
                Image image = new Image("file:" + lesson.getContentImage());
                lessonContentImage.setImage(image);
                lessonContentImage.setVisible(true);

                // Dynamically resize the image to fit within the screen
                lessonContentImage.setFitWidth(800); // Set a reasonable width
                lessonContentImage.setFitHeight(600); // Set a reasonable height
                lessonContentImage.setPreserveRatio(true); // Maintain aspect ratio
            } catch (Exception e) {
                System.out.println("Error loading image: " + e.getMessage());
                lessonContentImage.setVisible(false);
            }

            // Center the image view
            contentContainer.getChildren().clear();
            contentContainer.getChildren().add(lessonContentImage);

            // Set preferred width and height for the image
            lessonContentImage.setFitWidth(800); // Set a reasonable width
            lessonContentImage.setFitHeight(600); // Set a reasonable height
        } else {
            // Neither text nor image
            lessonContentText.setVisible(false);
            lessonContentImage.setVisible(false);
        }
    }

    /**
     * Handles the "Previous" button click.
     * Navigates to the previous lesson in the list.
     */
    @FXML
    private void handlePreviousButton() {
        if (currentLessonIndex > 0) {
            currentLessonIndex--;
            updateUI(lessons.get(currentLessonIndex));
        }
    }

    /**
     * Handles the "Next" button click.
     * Navigates to the next lesson in the list.
     */
    @FXML
    private void handleNextButton() {
        if (currentLessonIndex < lessons.size() - 1) {
            currentLessonIndex++;
            updateUI(lessons.get(currentLessonIndex));
        }
    }

    /**
     * Handles the "Close" button click.
     */
    @FXML
    private void closeWindow() {
        if (stage != null) {
            stage.close(); // Close the stage
        } else {
            System.out.println("Stage is null. Cannot close the window.");
        }
    }
}