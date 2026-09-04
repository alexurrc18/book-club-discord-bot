package com.alexandruc.bookclub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:bookclub.db";

    public static void initDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS user_profiles (
                discord_id INTEGER PRIMARY KEY,
                goodreads_id TEXT NOT NULL
            );
        """;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Eroare la inițializarea bazei de date: " + e.getMessage());
        }
    }

    public static boolean isGoodreadsIdLinked(String goodreadsId) {
        String sql = "SELECT 1 FROM user_profiles WHERE goodreads_id = ? LIMIT 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, goodreadsId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Eroare la verificarea Goodreads ID: " + e.getMessage());
            return false;
        }
    }

    public static boolean saveUser(long discordId, String goodreadsId) {
            if(!isGoodreadsIdLinked(goodreadsId)){
            String sql = "INSERT OR REPLACE INTO user_profiles (discord_id, goodreads_id) VALUES (?, ?)";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setLong(1, discordId);
                pstmt.setString(2, goodreadsId);
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                System.err.println("Eroare la salvarea utilizatorului: " + e.getMessage());
                return false;
            }
        }
        else System.err.println("Nu s-a putut salva profilul " + discordId + " + " + goodreadsId + " deoarece acesta este deja legat de un cont!");
        return false;
    }

    public static boolean deleteByGoodreadsId(String goodreadsId) {
        String sql = "DELETE FROM user_profiles WHERE goodreads_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, goodreadsId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Eroare la ștergerea după Goodreads ID: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteUser(long discordId) {
        String sql = "DELETE FROM user_profiles WHERE discord_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, discordId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Eroare la ștergerea utilizatorului: " + e.getMessage());
            return false;
        }
    }


    public static Optional<String> getGoodreadsId(long discordId) {
        String sql = "SELECT goodreads_id FROM user_profiles WHERE discord_id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, discordId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getString("goodreads_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Eroare la citirea utilizatorului: " + e.getMessage());
        }

        return Optional.empty();
    }
}