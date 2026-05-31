package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionDAO {

    private final DatabaseManager myDB;

    // Shuffled pool of all questions for the current game
    private static List<Question> questionPool = new ArrayList<>();
    private static int poolIndex = 0;

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

        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM questions");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                questionPool.add(new Question(
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_answer"),
                        rs.getString("question_type")
                ));
            }

            // Shuffle so every game gets a different order
            Collections.shuffle(questionPool);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the next question from the pre-shuffled pool.
     * If the pool runs out, reshuffles and starts over.
     *
     * @return a Question object, or null if pool is empty
     */
    public Question getRandomQuestion() {
        // If pool is empty or not initialized, load it
        if (questionPool.isEmpty()) {
            resetUsedQuestions();
        }

        // If still empty something went wrong with DB
        if (questionPool.isEmpty()) return null;

        // If we've used all questions, reshuffle and start over
        if (poolIndex >= questionPool.size()) {
            Collections.shuffle(questionPool);
            poolIndex = 0;
        }

        return questionPool.get(poolIndex++);
    }
}