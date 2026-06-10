package model;

import java.io.Serializable;

/**
 * Represents a door in the maze.
 *
 * <p>Each door begins locked and requires the player to answer a trivia
 * question correctly before passing through. Questions are assigned lazily
 * when the player first attempts the door so unused doors do not consume
 * questions from the question pool.</p>
 *
 * <p>If the player answers incorrectly, the door loses one attempt and
 * receives a new question if attempts remain. After all attempts are used,
 * the door becomes permanently closed.</p>
 */
public class Door implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean isLocked;
    private boolean isPermanentlyClosed;
    private int myAttemptsRemaining;
    private Question myQuestion;

    /**
     * Constructs a locked door with no assigned question.
     *
     * <p>The question is assigned lazily when the player first attempts to
     * open the door.</p>
     */
    public Door() {
        this.isLocked = true;
        this.isPermanentlyClosed = false;
        this.myAttemptsRemaining = 2;
        this.myQuestion = null;
    }

    /**
     * Returns whether this door is currently locked.
     *
     * @return {@code true} if the door is locked;
     *         {@code false} otherwise
     */
    public boolean isLocked() {
        return isLocked;
    }

    /**
     * Returns whether this door is permanently closed.
     *
     * @return {@code true} if the door is permanently closed;
     *         {@code false} otherwise
     */
    public boolean isPermanentlyClosed() {
        return isPermanentlyClosed;
    }

    /**
     * Sets whether this door is permanently closed.
     *
     * <p>This is used when a door can no longer be attempted, including
     * perimeter doors that do not lead to valid rooms.</p>
     *
     * @param theClosed {@code true} to permanently close the door;
     *                  {@code false} to reopen it
     */
    public void setPermanentlyClosed(final boolean theClosed) {
        isPermanentlyClosed = theClosed;
    }

    /**
     * Returns the number of attempts remaining for this door.
     *
     * @return the number of attempts remaining
     */
    public int getAttemptsRemaining() {
        return myAttemptsRemaining;
    }

    /**
     * Sets whether this door is locked.
     *
     * @param theLocked {@code true} to lock the door;
     *                  {@code false} to unlock it
     */
    public void setLocked(final boolean theLocked) {
        isLocked = theLocked;
    }

    /**
     * Returns the question assigned to this door.
     *
     * <p>If no question has been assigned yet, a random question is retrieved
     * from the question database and stored for this door.</p>
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
     * Attempts to unlock this door using the player's answer.
     *
     * <p>If the answer is correct, the door is unlocked. If the answer is
     * incorrect, the remaining attempt count decreases. When attempts remain,
     * the current question is marked as used and replaced with a new random
     * question. If no attempts remain, the door becomes permanently closed.</p>
     *
     * @param theAnswer the player's submitted answer
     * @return {@code true} if the answer is correct and the door is unlocked;
     *         {@code false} otherwise
     */
    public boolean attemptAnswer(final String theAnswer) {
        if (isPermanentlyClosed) return false;

        if (myQuestion != null && isCorrectAnswer(theAnswer)) {
            isLocked = false;
            return true;
        }

        myAttemptsRemaining--;

        if (myAttemptsRemaining <= 0) {
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
     * Determines whether the submitted answer matches the door's correct answer.
     *
     * <p>Short-answer questions require an exact case-insensitive match after
     * trimming whitespace. Other question types allow answers such as
     * {@code "B) Christian Bale"} to match a correct answer of {@code "B"}.</p>
     *
     * @param theAnswer the player's submitted answer
     * @return {@code true} if the submitted answer is correct;
     *         {@code false} otherwise
     */
    private boolean isCorrectAnswer(final String theAnswer) {
        if (theAnswer == null || myQuestion == null || myQuestion.getCorrectAnswer() == null) {
            return false;
        }

        String playerAnswer = theAnswer.trim().toUpperCase();
        String correctAnswer = myQuestion.getCorrectAnswer().trim().toUpperCase();

        if (myQuestion instanceof ShortAnswerQuestion) {
            return playerAnswer.equals(correctAnswer);
        }

        return playerAnswer.startsWith(correctAnswer);
    }

    /**
     * Resets this door to its initial locked state.
     *
     * <p>This clears the assigned question, restores the attempt count, unlocks
     * any permanent closure, and locks the door again.</p>
     */
    public void reset() {
        this.isLocked = true;
        this.isPermanentlyClosed = false;
        this.myAttemptsRemaining = 2;
        this.myQuestion = null;
    }
}