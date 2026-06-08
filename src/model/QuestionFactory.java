package model;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class QuestionFactory {

    private QuestionFactory() {
        // Prevents creating QuestionFactory objects.
    }

    public static Question createQuestion(final ResultSet theResultSet) throws SQLException {
        String questionText = theResultSet.getString("question_text");
        String optionA = theResultSet.getString("option_a");
        String optionB = theResultSet.getString("option_b");
        String optionC = theResultSet.getString("option_c");
        String optionD = theResultSet.getString("option_d");
        String correctAnswer = theResultSet.getString("correct_answer");
        String questionType = theResultSet.getString("question_type");

        if (questionType.equalsIgnoreCase("multiple choice")) {
            return new MultipleChoiceQuestion(
                    questionText,
                    optionA,
                    optionB,
                    optionC,
                    optionD,
                    correctAnswer
            );
        } else if (questionType.equalsIgnoreCase("true/false")) {
            return new TrueFalseQuestion(questionText, correctAnswer);
        } else if (questionType.equalsIgnoreCase("short answer")) {
            return new ShortAnswerQuestion(questionText, correctAnswer);
        } else {
            throw new IllegalArgumentException("Unknown question type: " + questionType);
        }
    }
}