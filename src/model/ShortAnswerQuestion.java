package model;

/**
 * Represents a short-answer trivia question.
 * This question type requires the player to enter a free-form
 * text response that is compared against the correct answer.
 */
public class ShortAnswerQuestion extends Question {

    /**
     * Constructs a short-answer question with the specified
     * question text and correct answer.
     *
     * @param theQuestionText the text of the question
     * @param theCorrectAnswer the correct answer for the question
     */
    public ShortAnswerQuestion(final String theQuestionText,
                               final String theCorrectAnswer) {
        super(theQuestionText, null, null, null, null,
                theCorrectAnswer, "short answer");
    }
}