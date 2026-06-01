package model;

public class LeaderboardEntry {
    private final String playerName;
    private final double timeSeconds;
    private final int correctScore;
    private final int incorrectScore;

    public LeaderboardEntry(String playerName, double timeSeconds, int correctScore, int incorrectScore) {
        this.playerName = playerName;
        this.timeSeconds = timeSeconds;
        this.correctScore = correctScore;
        this.incorrectScore = incorrectScore;
    }

    public String getPlayerName() {
        return playerName;
    }

    public double getTimeSeconds() {
        return timeSeconds;
    }

    public int getCorrectScore() {
        return correctScore;
    }

    public int getIncorrectScore() {
        return incorrectScore;
    }
}