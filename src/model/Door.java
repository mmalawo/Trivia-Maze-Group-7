package model;

import java.io.Serializable;

/**
 * Represents a door in the maze.
 * Each door is locked and requires the player to answer
 * a trivia question correctly to pass through.
 * Players have 2 attempts before the door is permanently locked.
 * Each wrong answer fetches a new question for the next attempt.
 * Questions are assigned lazily (on first access) to avoid wasting
 * questions on doors that get replaced during maze generation.
 */
public class Door implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean isLocked;
    private boolean isPermanentlyClosed;
    private int attemptsRemaining;
    private Question myQuestion;

    /**
     * Constructor - creates a locked door.
     * Question is NOT assigned here — it is assigned lazily
     * the first time the player tries to open this door.
     */
    public Door() {
        this.isLocked = true;
        this.isPermanentlyClosed = false;
        this.attemptsRemaining = 2;
        this.myQuestion = null;
    }

    /**
     * Returns whether the door is locked.
     *
     * @return true if the door is locked
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Returns whether the door is permanently closed.
     *
     * @return true if the door is permanently closed
     */
    public boolean isPermanentlyClosed() {
        return isPermanentlyClosed;
    }

    /**
     * Sets whether the door is permanently closed.
     * Used to visually hide perimeter non-exit doors after the player clicks them.
     *
     * @param theClosed true to permanently close the door
     */
    public void setPermanentlyClosed(boolean theClosed) {
        isPermanentlyClosed = theClosed;
    }

    /**
     * Returns the number of attempts remaining for this door.
     *
     * @return the number of attempts remaining
     */
    public int getAttemptsRemaining() {
        return attemptsRemaining;
    }

    /**
     * Sets the locked state of the door.
     *
     * @param locked true to lock the door
     */
    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    /**
     * Returns the Question object assigned to this door.
     * Lazily assigns a question on first access so questions are only
     * pulled from the pool when a player actually clicks the door.
     *
     * @return the question assigned to this door
     */
    public Question getQuestion() {
        if (myQuestion == null) {
            QuestionDAO dao = new QuestionDAO();
            myQuestion = dao.getRandomQuestion();
        }
        return myQuestion;
    }

    /**
     * Checks if the player's answer matches the correct answer.
     * Uses startsWith so full option text like "B) Christian Bale" matches correct answer "B".
     * Marks current question as used before fetching a new one on wrong answers.
     *
     * @param theAnswer the player's answer
     * @return true if the answer is correct
     */
    public boolean attemptAnswer(String theAnswer) {
        if (isPermanentlyClosed) return false;

        if (myQuestion != null && theAnswer.toUpperCase().startsWith(myQuestion.getCorrectAnswer().toUpperCase())) {
            isLocked = false;
            return true;
        }

        attemptsRemaining--;

        if (attemptsRemaining <= 0) {
            // Mark last question as used so it won't appear on other doors
            QuestionDAO.markAsCorrectlyAnswered(myQuestion);
            isPermanentlyClosed = true;
            isLocked = true;
        } else {
            // Mark current question as used before fetching a new one
            QuestionDAO.markAsCorrectlyAnswered(myQuestion);
            QuestionDAO dao = new QuestionDAO();
            myQuestion = dao.getRandomQuestion();
        }

        return false;
    }

    /**
     * Resets the door to its initial locked state.
     */
    public void reset() {
        this.isLocked = true;
        this.isPermanentlyClosed = false;
        this.attemptsRemaining = 2;
        this.myQuestion = null;
    }
}