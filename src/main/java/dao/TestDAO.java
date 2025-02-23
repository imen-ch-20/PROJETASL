package dao;

import database.DatabaseConnection;
import model.Level;
import model.Chapter;
import model.Lesson;
import java.sql.Connection;

public class TestDAO {
    public static void main(String[] args) {
        // Get connection once here
        Connection conn = DatabaseConnection.getConnection();

        if (conn != null) {
            // Check connection status before starting queries
            checkConnectionStatus(conn);

            // Test getting all levels
            System.out.println("Levels:");
            for (Level level : LevelDAO.getAllLevels(conn)) {
                System.out.println(level.getId() + ": " + level.getName());
            }
            checkConnectionStatus(conn);

            // Test getting chapters from Level 1 (Basic Level)
            System.out.println("\nChapters in Basic Level:");
            for (Chapter chapter : ChapterDAO.getChaptersByLevel(conn, 1)) {
                System.out.println(chapter.getId() + ": " + chapter.getName());
            }
            checkConnectionStatus(conn);

            // Test getting lessons from Chapter 1 (Introduction to ASL)
            System.out.println("\nLessons in 'Introduction to ASL':");
            for (Lesson lesson : LessonDAO.getLessonsByChapter(conn, 1)) {
                System.out.println(lesson.getId() + ": " + lesson.getName());
            }
            checkConnectionStatus(conn);

            // Close connection after all queries are complete
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    System.out.println("Database connection closed.");
                }
            } catch (Exception e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // Method to check the connection status after each query
    private static void checkConnectionStatus(Connection conn) {
        try {
            if (conn.isClosed()) {
                System.out.println("Connection was closed unexpectedly!");
            } else {
                System.out.println("Connection is still open.");
            }
        } catch (Exception e) {
            System.out.println("Error checking connection status: " + e.getMessage());
        }
    }
}
