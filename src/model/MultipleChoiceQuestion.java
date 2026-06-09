package model;

/**
 * Represents a multiple-choice trivia question.
 * This question type provides four answer options, one of which
 * is designated as the correct answer.
 */
public class MultipleChoiceQuestion extends Question {

    /**
     * Constructs a multiple-choice question with four answer options
     * and a correct answer.
     *
     * @param theQuestionText the text of the question
     * @param theOptionA the first answer option
     * @param theOptionB the second answer option
     * @param theOptionC the third answer option
     * @param theOptionD the fourth answer option
     * @param theCorrectAnswer the correct answer for the question
     */
    public MultipleChoiceQuestion(final String theQuestionText,
                                  final String theOptionA,
                                  final String theOptionB,
                                  final String theOptionC,
                                  final String theOptionD,
                                  final String theCorrectAnswer) {
        super(theQuestionText, theOptionA, theOptionB, theOptionC, theOptionD,
                theCorrectAnswer, "multiple choice");
    }
}