package model;

import java.io.Serializable;

/**
 * Represents a trivia question in the Trivia Maze game.
 *
 * <p>This abstract class stores the shared data used by all question types,
 * including the question text, answer options, correct answer, and question
 * type. Specific question types, such as multiple choice, true/false, and
 * short answer, extend this class.</p>
 */
public abstract class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    /** The text of the trivia question. */
    private final String myQuestionText;

    /** The first answer option, if applicable. */
    private final String myOptionA;

    /** The second answer option, if applicable. */
    private final String myOptionB;

    /** The third answer option, if applicable. */
    private final String myOptionC;

    /** The fourth answer option, if applicable. */
    private final String myOptionD;

    /** The correct answer for the question. */
    private final String myCorrectAnswer;

    /** The type of trivia question. */
    private final String myQuestionType;

    /**
     * Constructs a question with the specified text, answer options,
     * correct answer, and question type.
     *
     * @param theQuestionText the text of the question
     * @param theOptionA the first answer option, or {@code null} if not used
     * @param theOptionB the second answer option, or {@code null} if not used
     * @param theOptionC the third answer option, or {@code null} if not used
     * @param theOptionD the fourth answer option, or {@code null} if not used
     * @param theCorrectAnswer the correct answer for the question
     * @param theQuestionType the type of question
     */
    public Question(final String theQuestionText,
                    final String theOptionA,
                    final String theOptionB,
                    final String theOptionC,
                    final String theOptionD,
                    final String theCorrectAnswer,
                    final String theQuestionType) {

        // Store each parameter into its corresponding field
        this.myQuestionText = theQuestionText;
        this.myOptionA = theOptionA;
        this.myOptionB = theOptionB;
        this.myOptionC = theOptionC;
        this.myOptionD = theOptionD;
        this.myCorrectAnswer = theCorrectAnswer;
        this.myQuestionType = theQuestionType;
    }

    // -----------------------------------------------------------------------
    // GETTERS - these allow other classes to READ the question's information.
    // We don't have setters because a question shouldn't change once created.
    // -----------------------------------------------------------------------

    /**
     * Returns the text of the question.
     *
     * @return the question text
     */
    public String getQuestionText() { return myQuestionText; }

    /**
     * Returns the first answer option.
     *
     * @return the first answer option, or {@code null} if not used
     */
    public String getOptionA() { return myOptionA; }

    /**
     * Returns the second answer option.
     *
     * @return the second answer option, or {@code null} if not used
     */
    public String getOptionB() { return myOptionB; }

    /**
     * Returns the third answer option.
     *
     * @return the third answer option, or {@code null} if not used
     */
    public String getOptionC() { return myOptionC; }

    /**
     * Returns the fourth answer option.
     *
     * @return the fourth answer option, or {@code null} if not used
     */
    public String getOptionD() { return myOptionD; }

    /**
     * Returns the correct answer for this question.
     *
     * @return the correct answer
     */
    public String getCorrectAnswer() { return myCorrectAnswer; }

    /**
     * Returns the type of this question.
     *
     * @return the question type
     */
    public String getQuestionType() { return myQuestionType; }

    /**
     * Returns a string representation of this question.
     *
     * <p>The returned string includes the question type, question text,
     * and correct answer for debugging purposes.</p>
     *
     * @return a string representation of this question
     */
    @Override
    public String toString() {
        return "Question{" +
                "type='" + myQuestionType + '\'' +
                ", question='" + myQuestionText + '\'' +
                ", correctAnswer='" + myCorrectAnswer + '\'' +
                '}';
    }
}