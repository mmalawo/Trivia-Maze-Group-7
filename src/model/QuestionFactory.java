package model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Factory class responsible for creating the appropriate
 * {@link Question} subclass from database query results.
 *
 * <p>The factory examines the question type stored in the
 * result set and returns an instance of the corresponding
 * question implementation.</p>
 */
public final class QuestionFactory {

    /**
     * Prevents instantiation of this utility class.
     */
    private QuestionFactory() {
        // Prevents creating QuestionFactory objects.
    }

    /**
     * Creates a {@link Question} object from the current row
     * of the provided result set.
     *
     * <p>The question type determines which subclass is created:
     * multiple choice, true/false, or short answer.</p>
     *
     * @param theResultSet the result set containing question data
     * @return a Question instance corresponding to the stored question type
     * @throws SQLException if an error occurs while reading data from the result set
     * @throws IllegalArgumentException if the question type is not recognized
     */
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