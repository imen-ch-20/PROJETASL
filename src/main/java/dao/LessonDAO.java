package dao;

import model.Lesson;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LessonDAO {

    // Get all lessons by chapter ID
    public static List<Lesson> getLessonsByChapter(Connection conn, int chapterId) {
        List<Lesson> lessons = new ArrayList<>();
        String query = "SELECT id, name, content_text, content_image FROM lessons WHERE chapter_id = ?";

        if (conn == null) {
            System.out.println("⚠️ Error: Database connection is null!");
            return lessons;
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, chapterId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Lesson lesson = new Lesson(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("content_text"),
                        rs.getString("content_image")
                );
                lessons.add(lesson);
            }
        } catch (Exception e) {
            System.out.println("❌ Error retrieving lessons: " + e.getMessage());
        }

        return lessons;
    }

    // Get lesson ID by name (case-insensitive, trimming spaces)
    public static int getLessonIdByName(Connection conn, String lessonName) {
        String query = "SELECT id FROM lessons WHERE LOWER(name) = LOWER(?)";
        if (conn == null) {
            System.out.println("⚠️ Error: Database connection is null!");
            return -1;
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            lessonName = lessonName.trim();  // Trim the lesson name to remove extra spaces
            System.out.println("Querying lesson: " + lessonName);  // Log the lesson name being queried
            stmt.setString(1, lessonName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            System.out.println("❌ Error retrieving lesson ID: " + e.getMessage());
        }
        return -1;
    }

    // Get lesson by ID
    public static Lesson getLessonById(Connection conn, int lessonId) {
        String query = "SELECT id, name, content_text, content_image FROM lessons WHERE id = ?";
        if (conn == null) {
            System.out.println("⚠️ Error: Database connection is null!");
            return null;
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, lessonId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Lesson(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("content_text"),
                        rs.getString("content_image")
                );
            }
        } catch (Exception e) {
            System.out.println("❌ Error retrieving lesson: " + e.getMessage());
        }

        return null;
    }

    public static int getChapterIdByLessonId(Connection conn, int lessonId) {
        String query = "SELECT chapter_id FROM lessons WHERE id = ?";
        int chapterId = -1;

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, lessonId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                chapterId = rs.getInt("chapter_id");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving chapter ID: " + e.getMessage());
        }

        return chapterId;
    }
}