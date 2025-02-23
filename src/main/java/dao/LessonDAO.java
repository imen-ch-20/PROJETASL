package dao;

import model.Lesson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LessonDAO {
    public static List<Lesson> getLessonsByChapter(Connection conn, int chapterId) {
        List<Lesson> lessons = new ArrayList<>();
        String query = "SELECT * FROM lessons WHERE chapter_id = ?";

        if (conn == null) {
            System.out.println("Connection is null!");
            return lessons;
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, chapterId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Lesson lesson = new Lesson(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("content_text"),  // Assuming the column name for text content is "content_text"
                        rs.getString("content_image")  // Assuming the column name for image content is "content_image"
                );
                lessons.add(lesson);

                // Debugging Output to check if data is being retrieved
                System.out.println("Lesson ID: " + lesson.getId());
                System.out.println("Lesson Name: " + lesson.getName());
                System.out.println("Lesson Text: " + lesson.getContentText());
                System.out.println("Lesson Image: " + lesson.getContentImage());
            }
        } catch (Exception e) {
            System.out.println("Error retrieving lessons: " + e.getMessage());
        }

        return lessons;
    }
}
