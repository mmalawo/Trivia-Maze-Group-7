package model;

import java.io.Serializable;

/**
 * Represents a door in the maze.
 * Each door is locked and requires the player to answer
 * a trivia question correctly to pass through.
 * Players have 3 attempts before the door is permanently locked.
 * Each wrong answer fetches a new question for the next attempt.
 */
public class Door implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean isLocked;
    private boolean isPermanentlyClosed;
    private int attemptsRemaining;
    private Question myQuestion;

    /**
     * Constructor - creates a locked door and assigns
     * a random trivia question from the database using QuestionDAO.
     */
    public Door() {
        this.isLocked = true;
        this.isPermanentlyClosed = false;
        this.attemptsRemaining = 3;
        QuestionDAO dao = new QuestionDAO();
        this.myQuestion = dao.getRandomQuestion();
    }

    /**
     * Returns whether the door is currently locked.
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Returns whether the door is permanently closed (failed 3 times).
     */
    public boolean isPermanentlyClosed() {
        return isPermanentlyClosed;
    }

    /**
     * Returns how many attempts the player has remaining.
     */
    public int getAttemptsRemaining() {
        return attemptsRemaining;
    }

    /**
     * Sets the locked state of the door.
     */
    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    /**
     * Returns the Question object assigned to this door.
     */
    public Question getQuestion() {
        return myQuestion;
    }

    /**
     * Checks if the player's answer matches the correct answer.
     * Uses startsWith so full option text like "B) Christian Bale" matches correct answer "B".
     * If correct, unlocks the door.
     * If wrong, decrements attempts, fetches a new question, and permanently closes after 3 failures.
     *
     * @param theAnswer the answer the player provided
     * @return true if correct, false if wrong
     */
    public boolean attemptAnswer(String theAnswer) {
        if (isPermanentlyClosed) return false;

        if (myQuestion != null && theAnswer.toUpperCase().startsWith(myQuestion.getCorrectAnswer().toUpperCase())) {
            isLocked = false;
            return true;
        }

        attemptsRemaining--;

        if (attemptsRemaining <= 0) {
            isPermanentlyClosed = true;
            isLocked = true;
        } else {
            // Fetch a new question for the next attempt
            QuestionDAO dao = new QuestionDAO();
            myQuestion = dao.getRandomQuestion();
        }

        return false;
    }

    public void reset() {
        this.isLocked = true;
        this.isPermanentlyClosed = false;
        this.attemptsRemaining = 3;
    }

}