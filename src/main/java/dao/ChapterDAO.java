package dao;

import model.Chapter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChapterDAO {
    public static List<Chapter> getChaptersByLevel(Connection conn, int levelId) {
        List<Chapter> chapters = new ArrayList<>();
        String query = "SELECT * FROM chapters WHERE level_id = ?";

        if (conn == null) {
            System.out.println("Connection is null!");
            return chapters;
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, levelId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                chapters.add(new Chapter(rs.getInt("id"), rs.getString("name"), rs.getInt("level_id")));
            }
        } catch (Exception e) {
            System.out.println("Error retrieving chapters: " + e.getMessage());
        }

        return chapters;

    }


        public static int getChapterIdByName(Connection conn, String chapterName) {
            String query = "SELECT id FROM chapters WHERE name = ?";
            int chapterId = -1; // Default to -1 if not found

            if (conn == null) {
                System.out.println("Connection is null!");
                return chapterId;
            }

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, chapterName);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    chapterId = rs.getInt("id");
                }
            } catch (Exception e) {
                System.out.println("Error retrieving chapter ID: " + e.getMessage());
            }

            return chapterId;
        }


}
