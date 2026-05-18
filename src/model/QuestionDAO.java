package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * QuestionDAO (Data Access Object) is responsible for retrieving
 * trivia questions from the SQLite database.
 * It acts as the bridge between the database and the rest of the game.
 */
public class QuestionDAO {

    // The database manager that handles our connection to the SQLite database
    private final DatabaseManager myDB;

    /**
     * Constructor - gets the single instance of DatabaseManager
     * (Singleton pattern, same one used everywhere in the game)
     */
    public QuestionDAO() {
        myDB = DatabaseManager.getInstance();
    }

    /**
     * Retrieves a random trivia question from the database.
     * Uses SQL's ORDER BY RANDOM() to shuffle the results,
     * then LIMIT 1 to grab just one question.
     *
     * @return a Question object with all its data, or null if something goes wrong
     */
    public Question getRandomQuestion() {
        // SQL query that selects one random question from the questions table
        String sql = "SELECT * FROM questions ORDER BY RANDOM() LIMIT 1";

        try {
            // Get the active database connection
            Connection conn = myDB.getConnection();

            // Prepare and execute the SQL query
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            // If a row was returned, build a Question object from the data
            if (rs.next()) {
                return new Question(
                        rs.getString("question_text"),  // The question being asked
                        rs.getString("option_a"),        // First answer choice
                        rs.getString("option_b"),        // Second answer choice
                        rs.getString("option_c"),        // Third answer choice (null if not multiple choice)
                        rs.getString("option_d"),        // Fourth answer choice (null if not multiple choice)
                        rs.getString("correct_answer"),  // The correct answer
                        rs.getString("question_type")    // "multiple choice", "true/false", or "short answer"
                );
            }
        } catch (SQLException e) {
            // Print any database errors to help with debugging
            e.printStackTrace();
        }

        // Return null if no question was found or an error occurred
        return null;
    }
}