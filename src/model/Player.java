package model;

import java.io.Serializable;

/**
 * Represents a player in the Trivia Maze game.
 * Tracks the player's name, score, timer, and current room.
 */
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The player's name. */
    private String myPlayerName;

    /** The player's recorded completion time in seconds. */
    private double myRecordTime;

    /** The number of correctly answered trivia questions. */
    private int myCorrectScore;

    /** The number of incorrectly answered trivia questions. */
    private int myIncorrectScore;

    /** The system time in milliseconds when the timer was started. */
    private long myStartTime;

    /** The elapsed time in seconds saved at the moment of saving the game. */
    private double mySavedElapsedTime;

    /** The player's current room in the maze. */
    private Room myCurrentRoom;

    /** The number of remaining attempts for the current door. */
    private int myRemainingAttempts;

    /**
     * Returns the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return myPlayerName;
    }

    /**
     * Sets the player's name.
     *
     * @param theName the name to set
     */
    public void setName(String theName) {
        this.myPlayerName = theName;
    }

    /**
     * Returns the number of correctly answered questions.
     *
     * @return the correct score
     */
    public int getCorrectScore() {
        return myCorrectScore;
    }

    /**
     * Sets the number of correctly answered questions.
     *
     * @param theCorrectScore the correct score to set
     */
    public void setCorrectScore(int theCorrectScore) {
        this.myCorrectScore = theCorrectScore;
    }

    /**
     * Increments the correct score by one.
     */
    public void incrementCorrectScore() {
        myCorrectScore++;
    }

    /**
     * Returns the number of incorrectly answered questions.
     *
     * @return the incorrect score
     */
    public int getIncorrectScore() {
        return myIncorrectScore;
    }

    /**
     * Sets the number of incorrectly answered questions.
     *
     * @param theIncorrectScore the incorrect score to set
     */
    public void setIncorrectScore(int theIncorrectScore) {
        this.myIncorrectScore = theIncorrectScore;
    }

    /**
     * Increments the incorrect score by one.
     */
    public void incrementIncorrectScore() {
        myIncorrectScore++;
    }

    /**
     * Starts the game timer by recording the current system time.
     */
    public void startTimer() {
        myStartTime = System.currentTimeMillis();
    }

    /**
     * Stops the game timer and records the elapsed time.
     */
    public void stopTimer() {
        myRecordTime = elapsedTime();
    }

    /**
     * Returns the elapsed time in seconds since the timer was started.
     *
     * @return elapsed time in seconds
     */
    public double elapsedTime() {
        return (System.currentTimeMillis() - myStartTime) / 1000.0;
    }

    /**
     * Returns the current elapsed time in seconds.
     *
     * @return current elapsed time in seconds
     */
    public double getCurrentTime() {
        return (System.currentTimeMillis() - myStartTime) / 1000.0;
    }

    /**
     * Returns the recorded completion time in seconds.
     *
     * @return the record time in seconds
     */
    public double getRecordTime() {
        return myRecordTime;
    }

    /**
     * Sets the recorded completion time.
     *
     * @param theRecordTime the record time to set in seconds
     */
    public void setRecordTime(double theRecordTime) {
        this.myRecordTime = theRecordTime;
    }

    /**
     * Saves the current elapsed time so it can be restored after loading a saved game.
     * Should be called immediately before serializing the game state.
     */
    public void saveElapsedTime() {
        mySavedElapsedTime = elapsedTime();
    }

    /**
     * Resumes the timer from the saved elapsed time.
     * Should be called immediately after loading a saved game so the timer
     * continues from where it left off.
     */
    public void resumeTimer() {
        myStartTime = (long)(System.currentTimeMillis() - (mySavedElapsedTime * 1000));
    }

    /**
     * Returns the elapsed time that was saved at the moment of saving the game.
     *
     * @return the saved elapsed time in seconds
     */
    public double getSavedElapsedTime() {
        return mySavedElapsedTime;
    }

    /**
     * Resets all player data for a new game.
     */
    public void reset() {
        myPlayerName = null;
        myRecordTime = 0;
        myCorrectScore = 0;
        myIncorrectScore = 0;
        myStartTime = 0;
        mySavedElapsedTime = 0;
        myCurrentRoom = null;
    }

    /**
     * Returns the player's current room.
     *
     * @return the current room
     */
    public Room getCurrentRoom() {
        return myCurrentRoom;
    }

    /**
     * Sets the player's current room.
     *
     * @param theRoom the room to set as current
     */
    public void setCurrentRoom(Room theRoom) {
        myCurrentRoom = theRoom;
    }

    /**
     * Returns the number of remaining door attempts.
     *
     * @return the remaining attempts
     */
    public int getRemainingAttempts() {
        return myRemainingAttempts;
    }

    /**
     * Sets the number of remaining door attempts.
     *
     * @param theAttempts the number of attempts to set
     */
    public void setRemainingAttempts(int theAttempts) {
        myRemainingAttempts = theAttempts;
    }

    /**
     * Decrements the remaining attempts by one.
     */
    public void decrementAttempts() {
        myRemainingAttempts--;
    }

    /**
     * Returns whether the game is over based on remaining attempts.
     *
     * @return true if no attempts remain, false otherwise
     */
    public boolean isGameOver() {
        return myRemainingAttempts <= 0;
    }
}