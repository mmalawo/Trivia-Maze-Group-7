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

    // Shuffled pool of all questions for the current game
    private static List<Question> questionPool = new ArrayList<>();
    private static int poolIndex = 0;

    // Tracks questions that were correctly answered so they don't repeat
    private static final Set<String> correctlyAnswered = new HashSet<>();

    /**
     * Constructs a QuestionDAO and initializes access to the database.
     */
    public QuestionDAO() {
        myDB = DatabaseManager.getInstance();
    }

    /**
     * Loads all questions from the database, shuffles them randomly,
     * and resets the index. Call this at the start of each new game.
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
     * Marks a question as correctly answered so it won't be given out again.
     *
     * @param question the question that was correctly answered
     */
    public static void markAsCorrectlyAnswered(Question question) {
        if (question != null) {
            correctlyAnswered.add(question.getQuestionText());
        }
    }

    /**
     * Returns the next question from the pre-shuffled pool that hasn't
     * been correctly answered yet.
     * If the pool runs out, reshuffles and starts over.
     *
     * @return a Question object, or null if pool is empty
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