package model;

import java.io.Serializable;

/**
 * Represents a door in the maze.
 * Each door is locked and requires the player to answer
 * a trivia question correctly to pass through.
 * Players have 3 attempts before the door is permanently locked.
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
        this.attemptsRemaining = 3;
        this.myQuestion = null;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public boolean isPermanentlyClosed() {
        return isPermanentlyClosed;
    }

    public int getAttemptsRemaining() {
        return attemptsRemaining;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    /**
     * Returns the Question object assigned to this door.
     * Lazily assigns a question on first access so questions are only
     * pulled from the pool when a player actually clicks the door.
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

    public void reset() {
        this.isLocked = true;
        this.isPermanentlyClosed = false;
        this.attemptsRemaining = 3;
        this.myQuestion = null;
    }
}