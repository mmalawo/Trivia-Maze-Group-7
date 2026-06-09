package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides database access for leaderboard operations.
 *
 * <p>This class is responsible for storing player scores,
 * retrieving the top leaderboard entries, and managing
 * leaderboard data in the database.</p>
 */
public class LeaderboardDAO {
    private final DatabaseManager myDB;

    /**
     * Constructs a LeaderboardDAO and initializes access
     * to the database manager.
     */
    public LeaderboardDAO() {
        myDB = DatabaseManager.getInstance();
    }

    /**
     * Saves a player's game results to the leaderboard.
     *
     * <p>If the player does not have a name, the name
     * "Anonymous" is used instead.</p>
     *
     * @param player the player whose score should be saved
     */
    public void saveScore(Player player) {
        String sql = "INSERT INTO leaderboard " +
                "(player_name, time_seconds, correct_score, incorrect_score) " +
                "VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stmt = myDB.getConnection().prepareStatement(sql);

            String name = player.getName();
            if (name == null || name.isBlank()) {
                name = "Anonymous";
            }

            stmt.setString(1, name);
            stmt.setDouble(2, player.getRecordTime());
            stmt.setInt(3, player.getCorrectScore());
            stmt.setInt(4, player.getIncorrectScore());

            stmt.executeUpdate();

            System.out.println("Leaderboard score saved.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the top leaderboard scores ordered by
     * completion time in ascending order.
     *
     * <p>The result is limited to the ten fastest entries.</p>
     *
     * @return a list containing the top leaderboard entries
     */
    public List<LeaderboardEntry> getTopScores() {
        List<LeaderboardEntry> scores = new ArrayList<>();

        String sql = "SELECT player_name, time_seconds, correct_score, incorrect_score " +
                "FROM leaderboard " +
                "ORDER BY time_seconds ASC " +
                "LIMIT 10";

        try {
            Statement stmt = myDB.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                scores.add(new LeaderboardEntry(
                        rs.getString("player_name"),
                        rs.getDouble("time_seconds"),
                        rs.getInt("correct_score"),
                        rs.getInt("incorrect_score")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return scores;
    }

    /**
     * Removes all entries from the leaderboard table.
     */
    public void clearLeaderboard() {
        String sql = "DELETE FROM leaderboard";

        try {
            Statement stmt = myDB.getConnection().createStatement();
            stmt.executeUpdate(sql);

            System.out.println("Leaderboard cleared.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}