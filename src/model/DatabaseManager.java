package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class manages the SQLite database for the Trivia Maze game.
 * It uses the Singleton pattern to ensure only ONE database connection
 * is ever created. Think of it like a single filing cabinet that everyone
 * in the game shares - you don't want multiple cabinets with different info!
 *
 * This class handles:
 * 1. Creating the database file (trivia.db)
 * 2. Creating the questions table
 * 3. Pre-populating the table with trivia questions
 *
 * HOW TO ADD MORE QUESTIONS:
 * - Find the questions array in populateQuestions() below
 * - Add a new INSERT statement following the same format
 * - Delete the trivia.db file from the project folder
 * - Rerun the game and the database will recreate with your new questions
 */
public class DatabaseManager {

    // The single instance of this class (Singleton pattern from lecture)
    // "volatile" makes sure this works correctly even with multiple threads
    private static volatile DatabaseManager myInstance;

    // The database connection object - this is what lets us talk to SQLite
    private Connection myConnection;

    // The path/name of our database file.
    // This will create a file called "trivia.db" in the project folder.
    private static final String DB_URL = "jdbc:sqlite:trivia.db";

    /**
     * Private constructor - this is the Singleton pattern!
     * By making the constructor private, no other class can do
     * "new DatabaseManager()" - they MUST use getInstance() instead.
     * This guarantees only one DatabaseManager ever exists.
     */
    private DatabaseManager() {
        // When the DatabaseManager is first created, set up everything
        connect();
        createTable();
        populateQuestions();
    }

    /**
     * This is how other classes get access to the DatabaseManager.
     * If it doesn't exist yet, create it. If it does, just return it.
     * This is the Singleton getInstance() method from the lecture slides!
     *
     * @return the single instance of DatabaseManager
     */
    public static DatabaseManager getInstance() {
        // Only create a new instance if one doesn't exist yet
        if (myInstance == null) {
            // "synchronized" makes this thread-safe (from lecture)
            synchronized (DatabaseManager.class) {
                if (myInstance == null) {
                    myInstance = new DatabaseManager();
                }
            }
        }
        return myInstance;
    }

    /**
     * Opens a connection to the SQLite database file.
     * If the file doesn't exist yet, SQLite will create it automatically.
     * This is the JDBC connection from Task 2.
     */
    private void connect() {
        try {
            // DriverManager.getConnection() is the JDBC way of opening a database
            myConnection = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to SQLite database successfully!");
        } catch (SQLException e) {
            // If something goes wrong, print the error so we can debug it
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    /**
     * Creates the questions table in the database if it doesn't exist yet.
     * "IF NOT EXISTS" means it won't crash if the table is already there.
     * Each row in the table represents one trivia question.
     *
     * Table columns:
     * - id: a unique number for each question (auto-increments)
     * - question_text: the actual question being asked
     * - option_a through option_d: the answer choices (null for true/false or short answer)
     * - correct_answer: the correct answer
     * - question_type: "multiple choice" or "true/false" or "short answer"
     */
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS questions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "question_text TEXT NOT NULL, " +
                "option_a TEXT, " +
                "option_b TEXT, " +
                "option_c TEXT, " +
                "option_d TEXT, " +
                "correct_answer TEXT NOT NULL, " +
                "question_type TEXT NOT NULL)";
        try {
            Statement stmt = myConnection.createStatement();
            stmt.execute(sql);
            System.out.println("Questions table created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    /**
     * Inserts trivia questions into the database.
     * Currently supports (as of the Third Iteration):
     * - 8 multiple choice questions
     * - 8 true/false questions
     * - 5 Short answer questions
     *
     * HOW TO ADD MORE QUESTIONS:
     * Copy one of the existing INSERT statements below and modify it.
     * For multiple choice: fill in all 4 options and the correct answer.
     * For true/false: option_c and option_d should be NULL.
     * For short answer: all should be NULL.
     * After adding, delete trivia.db and rerun the game to reset the database.
     */
    private void populateQuestions() {
        // Check if questions already exist so we don't add duplicates
        try {
            Statement checkStmt = myConnection.createStatement();
            var rs = checkStmt.executeQuery("SELECT COUNT(*) FROM questions");
            if (rs.getInt(1) > 0) {
                System.out.println("Questions already exist in database.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error checking questions: " + e.getMessage());
        }

        String[] questions = {

                // ------------------------------------------------------------------------
                // MULTIPLE CHOICE QUESTIONS (8 total)
                // Format: question, A, B, C, D, correct letter, type
                // To add more: copy a line below and change the values
                // ------------------------------------------------------------------------

                "INSERT INTO questions VALUES (NULL, " +
                        "'Which actor played both Patrick Bateman in American Psycho and Bruce Wayne in Batman Begins?', " +
                        "'A) Matt Damon', 'B) Christian Bale', 'C) Joaquin Phoenix', 'D) Tom Hardy', " +
                        "'B', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'The Bear is set inside what type of restaurant in Chicago?', " +
                        "'A) Fine dining', 'B) Pizza', 'C) Italian beef sandwich', 'D) Sushi', " +
                        "'C', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Which 2013 Spike Jonze film stars Joaquin Phoenix falling in love with an AI operating system?', " +
                        "'A) Ex Machina', 'B) Eternal Sunshine', 'C) Her', 'D) Bicentennial Man', " +
                        "'C', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What was the original network that aired Its Always Sunny in Philadelphia before it moved to FX?', " +
                        "'A) Comedy Central', 'B) FXX', 'C) Fox', 'D) FX', " +
                        "'D', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Which prestige drama features the fictional media dynasty the Roy family?', " +
                        "'A) Billions', 'B) Succession', 'C) Industry', 'D) The Affair', " +
                        "'B', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What 2018 film directed by Alfonso Cuaron won Best Picture Best Director and Best Foreign Language Film at the Oscars?', " +
                        "'A) A Fantastic Woman', 'B) Roma', 'C) Cold War', 'D) Shoplifters', " +
                        "'B', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Which actress played Amy Dunne in Gone Girl?', " +
                        "'A) Rooney Mara', 'B) Natalie Portman', 'C) Emily Blunt', 'D) Rosamund Pike', " +
                        "'D', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Barry Keoghan plays which character in the psychological thriller Saltburn?', " +
                        "'A) Felix', 'B) Farleigh', 'C) Oliver', 'D) Duncan', " +
                        "'C', 'multiple choice')",

                // ------------------------------------------------------------------------
                // TRUE/FALSE QUESTIONS (8 total)
                // Format: question, True, False, NULL, NULL, correct answer, type
                // To add more: copy a line below and change the values
                // ------------------------------------------------------------------------

                "INSERT INTO questions VALUES (NULL, " +
                        "'Aaron Paul and Bryan Cranston appeared together in a Super Bowl commercial reprising their Breaking Bad roles.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'True', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'The Truman Show starring Jim Carrey was directed by Peter Weir not Steven Spielberg.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'True', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Quentin Tarantino has stated he will retire after directing his 10th film.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'True', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'The Wire creator David Simon was formerly a crime reporter for the Baltimore Sun.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'True', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Robert Eggers directed both The Witch and The Lighthouse.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'True', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Martin Scorseses Killers of the Flower Moon is based on a true story about the Osage Nation murders in Oklahoma.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'True', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'TWICE member Jihyo trained at JYP Entertainment for 10 years before making her debut.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'True', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'There are two Japanese members in TWICE', " +
                        "'True', 'False', NULL, NULL, " +
                        "'False', 'true/false')",

                // ------------------------------------------------------------------------
                // SHORT ANSWER QUESTIONS (5 total)
                // Format: question, NULL, NULL, NULL, NULL, correct answer, type
                // To add more: copy a line below and change the values
                // ------------------------------------------------------------------------

                "INSERT INTO questions VALUES (NULL, " +
                        "'Who played Ken in the 2023 live-action film Barbie?', " +
                        "NULL, NULL, NULL, NULL, " +
                        "'Ryan Gosling', 'short answer')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Who was the creator of Star Wars?', " +
                        "NULL, NULL, NULL, NULL, " +
                        "'George Lucas', 'short answer')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What was the name of the survival show that the K-Pop girl group TWICE formed from?', " +
                        "NULL, NULL, NULL, NULL, " +
                        "'SIXTEEN', 'short answer')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What is the name of the song behind the infamous Rickroll meme?', " +
                        "NULL, NULL, NULL, NULL, " +
                        "'Never Gonna Give You Up', 'short answer')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What venue in Seattle will the FIFA World Cup 2026 be held in?', " +
                        "NULL, NULL, NULL, NULL, " +
                        "'Lumen Field', 'short answer')"
        };

        // Loop through each question and insert it into the database
        try {
            Statement stmt = myConnection.createStatement();
            int count = 0;
            for (String question : questions) {
                count++;
                stmt.execute(question);
            }
            System.out.println(count + " trivia questions added to database successfully!");
        } catch (SQLException e) {
            System.out.println("Error inserting questions: " + e.getMessage());
        }
    }

    /**
     * Returns the database connection so other classes can use it
     * to retrieve questions. QuestionDAO will use this to query the database.
     *
     * @return the active database connection
     */
    public Connection getConnection() {
        return myConnection;
    }

    /**
     * Closes the database connection when the game is done.
     * Always important to close connections to avoid memory leaks!
     */
    public void closeConnection() {
        try {
            if (myConnection != null && !myConnection.isClosed()) {
                myConnection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    /**
     * Simple test method to verify the database is working.
     * Run this directly to test without opening the full game window.
     * DELETE or COMMENT OUT this method when done testing!
     */
    public static void main(String[] args) {
        System.out.println("Testing database...");

        // This will trigger the Singleton to create the database
        DatabaseManager db = DatabaseManager.getInstance();

        // Test that we can retrieve questions
        try {
            var stmt = db.getConnection().createStatement();
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM questions");
            System.out.println("Total questions in database: " + rs.getInt(1));

            // Print all questions to verify they loaded correctly
            var rs2 = stmt.executeQuery("SELECT id, question_type, question_text FROM questions");
            while (rs2.next()) {
                System.out.println("ID: " + rs2.getInt("id") +
                        " | Type: " + rs2.getString("question_type") +
                        " | Question: " + rs2.getString("question_text"));
            }

            db.closeConnection();
            System.out.println("Database test complete!");

        } catch (Exception e) {
            System.out.println("Error testing database: " + e.getMessage());
        }
    }
}