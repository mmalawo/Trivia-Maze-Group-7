package model;

/**
 * Represents a door in the maze.
 * Each door is locked and requires the player to answer
 * a trivia question correctly to pass through.
 */
public class Door {
    private boolean isLocked;
    private Question myQuestion;

    /**
     * Constructor - creates a locked door and assigns
     * a random trivia question from the database using QuestionDAO.
     */
    public Door() {
        this.isLocked = true;
        // Use QuestionDAO to pull a random question from the DB
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
     * Sets the locked state of the door.
     * Call with false when the player answers correctly.
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
     * If correct, unlocks the door.
     *
     * @param theAnswer the answer the player provided
     * @return true if correct, false if wrong
     */
    public boolean attemptAnswer(String theAnswer) {
        if (myQuestion != null && theAnswer.equalsIgnoreCase(myQuestion.getCorrectAnswer())) {
            isLocked = false;
            return true;
        }
        return false;
    }
}