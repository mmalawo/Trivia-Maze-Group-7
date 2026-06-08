package model;

public class TrueFalseQuestion extends Question {

    public TrueFalseQuestion(final String theQuestionText,
                             final String theCorrectAnswer) {
        super(theQuestionText, "True", "False", null, null,
                theCorrectAnswer, "true/false");
    }
}