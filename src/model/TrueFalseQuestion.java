package model;

/**
 * Represents a true/false trivia question.
 *
 * <p>This question type provides {@code "True"} and {@code "False"} as
 * the answer options and stores one of them as the correct answer.</p>
 */
public class TrueFalseQuestion extends Question {

    /**
     * Constructs a true/false question with the specified question text
     * and correct answer.
     *
     * @param theQuestionText the text of the question
     * @param theCorrectAnswer the correct answer for the question,
     *                         typically {@code "True"} or {@code "False"}
     */
    public TrueFalseQuestion(final String theQuestionText,
                             final String theCorrectAnswer) {
        super(theQuestionText, "True", "False", null, null,
                theCorrectAnswer, "true/false");
    }
}