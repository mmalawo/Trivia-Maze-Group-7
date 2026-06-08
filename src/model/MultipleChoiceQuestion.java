package model;

public class MultipleChoiceQuestion extends Question {

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