package model;

/**
 * Represents a single entry in the leaderboard.
 * Each entry stores a player's name, completion time,
 * and trivia performance statistics.
 */
public class LeaderboardEntry {
    private final String playerName;
    private final double timeSeconds;
    private final int correctScore;
    private final int incorrectScore;

    /**
     * Constructs a leaderboard entry with the specified player data.
     *
     * @param playerName the name of the player
     * @param timeSeconds the completion time in seconds
     * @param correctScore the number of correctly answered questions
     * @param incorrectScore the number of incorrectly answered questions
     */
    public LeaderboardEntry(String playerName, double timeSeconds, int correctScore, int incorrectScore) {
        this.playerName = playerName;
        this.timeSeconds = timeSeconds;
        this.correctScore = correctScore;
        this.incorrectScore = incorrectScore;
    }

    /**
     * Returns the player's name.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the player's completion time in seconds.
     *
     * @return the completion time
     */
    public double getTimeSeconds() {
        return timeSeconds;
    }

    /**
     * Returns the number of correctly answered questions.
     *
     * @return the correct answer count
     */
    public int getCorrectScore() {
        return correctScore;
    }

    /**
     * Returns the number of incorrectly answered questions.
     *
     * @return the incorrect answer count
     */
    public int getIncorrectScore() {
        return incorrectScore;
    }
}