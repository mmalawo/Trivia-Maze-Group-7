package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides database access for leaderboard operations.
 *
 * <p>This data access object handles saving player scores, retrieving
 * the top leaderboard entries, and clearing leaderboard data from the
 * database.</p>
 */
public class LeaderboardDAO {
    private final DatabaseManager myDB;

    /**
     * Constructs a leaderboard data access object.
     *
     * <p>This initializes access to the shared database manager.</p>
     */
    public LeaderboardDAO() {
        myDB = DatabaseManager.getInstance();
    }

    /**
     * Saves a player's game results to the leaderboard table.
     *
     * <p>If the player does not have a name, {@code "Anonymous"} is used
     * as the stored player name.</p>
     *
     * @param thePlayer the player whose score should be saved
     */
    public void saveScore(final Player thePlayer) {
        String sql = "INSERT INTO leaderboard " +
                "(player_name, time_seconds, correct_score, incorrect_score) " +
                "VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stmt = myDB.getConnection().prepareStatement(sql);

            String name = thePlayer.getName();
            if (name == null || name.isBlank()) {
                name = "Anonymous";
            }

            stmt.setString(1, name);
            stmt.setDouble(2, thePlayer.getRecordTime());
            stmt.setInt(3, thePlayer.getCorrectScore());
            stmt.setInt(4, thePlayer.getIncorrectScore());

            stmt.executeUpdate();

            System.out.println("Leaderboard score saved.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the top leaderboard scores ordered by fastest completion time.
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
}