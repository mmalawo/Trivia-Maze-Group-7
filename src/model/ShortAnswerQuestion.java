package model;

public class ShortAnswerQuestion extends Question {

    public ShortAnswerQuestion(final String theQuestionText,
                               final String theCorrectAnswer) {
        super(theQuestionText, null, null, null, null,
                theCorrectAnswer, "short answer");
    }
}