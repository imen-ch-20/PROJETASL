package dao;

import model.Level;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LevelDAO {
    public static List<Level> getAllLevels(Connection conn) {
        List<Level> levels = new ArrayList<>();
        String query = "SELECT * FROM levels";

        if (conn == null) {
            System.out.println("Connection is null!");
            return levels;
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                levels.add(new Level(rs.getInt("id"), rs.getString("name")));
            }
        } catch (Exception e) {
            System.out.println("Error retrieving levels: " + e.getMessage());
        }

        return levels;
    }
}
