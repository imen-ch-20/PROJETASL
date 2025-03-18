package com.example.asl_app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainASLAPP extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/asl_app/dashboardASLAPP.fxml"));
        Parent root = fxmlLoader.load();

        // Set the scene
        Scene scene = new Scene(root);

        // Set the stage title
        primaryStage.setTitle("ASL App");

        // Set the scene to the stage
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}