package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Provides access to trivia questions stored in the database.
 *
 * <p>This class manages a shuffled pool of questions for the current
 * game session, tracks correctly answered questions to prevent
 * repetition, and retrieves questions as needed.</p>
 */
public class QuestionDAO {

    private final DatabaseManager myDB;

    /** The shuffled pool of questions for the current game session. */
    private static List<Question> questionPool = new ArrayList<>();

    /** The current index in the shuffled question pool. */
    private static int poolIndex = 0;

    /** Tracks questions that were correctly answered so they don't repeat */
    private static final Set<String> correctlyAnswered = new HashSet<>();

    /**
     * Constructs a question data access object.
     *
     * <p>This initializes access to the shared database manager.</p>
     */
    public QuestionDAO() {
        myDB = DatabaseManager.getInstance();
    }

    /**
     * Resets the question pool for a new game session.
     *
     * <p>This clears the current question pool, resets the pool index, clears
     * the tracked used questions, loads all questions from the database, and
     * shuffles them randomly.</p>
     */
    public static void resetUsedQuestions() {
        questionPool = new ArrayList<>();
        poolIndex = 0;
        correctlyAnswered.clear();

        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM questions");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                questionPool.add(QuestionFactory.createQuestion(rs));
            }

            Collections.shuffle(questionPool);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Marks a question as used so it will not be selected again during the
     * current question cycle.
     *
     * @param theQuestion the question to mark as used
     */
    public static void markAsCorrectlyAnswered(final Question theQuestion) {
        if (theQuestion != null) {
            correctlyAnswered.add(theQuestion.getQuestionText());
        }
    }

    /**
     * Returns the next available question from the shuffled question pool.
     *
     * <p>If the question pool is empty, it is reloaded from the database. This
     * method skips questions that have already been marked as used. If every
     * question has been used, the used-question tracking is cleared, the pool
     * is reshuffled, and selection begins again.</p>
     *
     * @return the next available question;
     *         {@code null} if no questions are available
     */
    public Question getRandomQuestion() {
        if (questionPool.isEmpty()) {
            resetUsedQuestions();
        }

        if (questionPool.isEmpty()) return null;

        // Try to find a question that hasn't been correctly answered yet
        int attempts = 0;
        while (attempts < questionPool.size()) {
            if (poolIndex >= questionPool.size()) {
                Collections.shuffle(questionPool);
                poolIndex = 0;
            }

            Question q = questionPool.get(poolIndex++);

            if (!correctlyAnswered.contains(q.getQuestionText())) {
                return q;
            }

            attempts++;
        }

        // If all questions have been correctly answered, reset and return any question
        correctlyAnswered.clear();
        Collections.shuffle(questionPool);
        poolIndex = 0;
        return questionPool.get(poolIndex++);
    }
}