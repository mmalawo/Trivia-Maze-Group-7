package model;

/**
 * Represents a true/false trivia question.
 * This question type provides "True" and "False" as the only
 * answer options and stores the correct answer.
 */
public class TrueFalseQuestion extends Question {

    /**
     * Constructs a true/false question with the specified question text
     * and correct answer.
     *
     * @param theQuestionText the text of the question
     * @param theCorrectAnswer the correct answer, typically "True" or "False"
     */
    public TrueFalseQuestion(final String theQuestionText,
                             final String theCorrectAnswer) {
        super(theQuestionText, "True", "False", null, null,
                theCorrectAnswer, "true/false");
    }
}