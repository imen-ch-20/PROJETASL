module com.example.asl_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens controller to javafx.fxml;
    opens com.example.asl_app to javafx.fxml;
    exports com.example.asl_app;
    exports model;
    opens model to javafx.fxml;
}