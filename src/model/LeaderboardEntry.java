package model;

/**
 * Represents a single leaderboard entry.
 *
 * <p>Each entry stores a player's name, completion time, number of correct
 * answers, and number of incorrect answers.</p>
 */
public class LeaderboardEntry {
    private final String myPlayerName;
    private final double myTimeSeconds;
    private final int myCorrectScore;
    private final int myIncorrectScore;

    /**
     * Constructs a leaderboard entry with the specified player data.
     *
     * @param thePlayerName the name of the player
     * @param theTimeSeconds the completion time in seconds
     * @param theCorrectScore the number of correctly answered questions
     * @param theIncorrectScore the number of incorrectly answered questions
     */
    public LeaderboardEntry(final String thePlayerName, final double theTimeSeconds,
                            final int theCorrectScore, final int theIncorrectScore) {
        this.myPlayerName = thePlayerName;
        this.myTimeSeconds = theTimeSeconds;
        this.myCorrectScore = theCorrectScore;
        this.myIncorrectScore = theIncorrectScore;
    }

    /**
     * Returns the player's name.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return myPlayerName;
    }

    /**
     * Returns the player's completion time in seconds.
     *
     * @return the completion time in seconds
     */
    public double getTimeSeconds() {
        return myTimeSeconds;
    }

    /**
     * Returns the number of correctly answered questions.
     *
     * @return the correct answer count
     */
    public int getCorrectScore() {
        return myCorrectScore;
    }

    /**
     * Returns the number of incorrectly answered questions.
     *
     * @return the incorrect answer count
     */
    public int getIncorrectScore() {
        return myIncorrectScore;
    }
}