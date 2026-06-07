package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static volatile DatabaseManager myInstance;
    private Connection myConnection;
    private static final String DB_URL = "jdbc:sqlite:trivia.db";

    private DatabaseManager() {
        connect();
        createTable();
        populateQuestions();
        createLeaderboardTable();
    }

    public static DatabaseManager getInstance() {
        if (myInstance == null) {
            synchronized (DatabaseManager.class) {
                if (myInstance == null) {
                    myInstance = new DatabaseManager();
                }
            }
        }
        return myInstance;
    }

    private void connect() {
        try {
            myConnection = DriverManager.getConnection(DB_URL);
            System.out.println("Connected to SQLite database successfully!");
        } catch (SQLException e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

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
     * Currently supports (as of the Sixth Iteration):
     * - 23 multiple choice questions
     * - 10 true/false questions
     * - 6 short answer questions
     * Total: 39 questions
     */
    private void populateQuestions() {
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
                // MULTIPLE CHOICE QUESTIONS (28 total)
                // ------------------------------------------------------------------------

                "INSERT INTO questions VALUES (NULL, " +
                        "'Which actor played both Patrick Bateman in American Psycho and Bruce Wayne in Batman Begins?', " +
                        "'A) Matt Damon', 'B) Christian Bale', 'C) Pedro Pascal', 'D) Tom Hardy', " +
                        "'B', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Which film stars Joaquin Phoenix falling in love with an AI operating system?', " +
                        "'A) Ex Machina', 'B) Eternal Sunshine', 'C) Her', 'D) Bicentennial Man', " +
                        "'C', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What was the original network that aired Its Always Sunny in Philadelphia?', " +
                        "'A) Comedy Central', 'B) FXX', 'C) Fox', 'D) FX', " +
                        "'D', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Which drama features the Roy family?', " +
                        "'A) Billions', 'B) Succession', 'C) Industry', 'D) The Affair', " +
                        "'B', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Which actress played Amy Dunne in Gone Girl?', " +
                        "'A) Rooney Mara', 'B) Natalie Portman', 'C) Emily Blunt', 'D) Rosamund Pike', " +
                        "'D', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What draft position was Michael Jordan selected in the 1984 NBA draft?', " +
                        "'A) First', 'B) Second', 'C) Third', 'D) Fifth', " +
                        "'C', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Which Steven Spielberg film features a friendly alien stranded on Earth?', " +
                        "'A) Close Encounters', 'B) War of the Worlds', 'C) Signs', 'D) ET', " +
                        "'D', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'In the movie Superbad, what year of high school are Seth and Evan in?', " +
                        "'A) Junior', 'B) Freshman', 'C) Sophomore', 'D) Senior', " +
                        "'D', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Which ancient Greek city state was known for having the most powerful army?', " +
                        "'A) Athens', 'B) Corinth', 'C) Sparta', 'D) Thebes', " +
                        "'C', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'In Ratatouille, what is the name of the villainous food critic?', " +
                        "'A) Gusteau', 'B) Skinner', 'C) Anton Ego', 'D) Colette', " +
                        "'C', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'This Pixar film was rejected by every major studio before Disney picked it up and its director John Lasseter was fired from Disney earlier for promoting the same technology used to make it.', " +
                        "'A) Toy Story', 'B) A Bugs Life', 'C) Monsters Inc', 'D) Wall-E', " +
                        "'A', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Which planet is known as the Red Planet?', " +
                        "'A) Venus', 'B) Jupiter', 'C) Mars', 'D) Saturn', " +
                        "'C', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'How many continents are there on Earth?', " +
                        "'A) 5', 'B) 6', 'C) 7', 'D) 8', " +
                        "'C', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What is the fastest land animal in the world?', " +
                        "'A) Lion', 'B) Cheetah', 'C) Horse', 'D) Leopard', " +
                        "'B', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'In which ocean is the Bermuda Triangle located?', " +
                        "'A) Pacific', 'B) Indian', 'C) Arctic', 'D) Atlantic', " +
                        "'D', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'How many rings are on the Olympic flag?', " +
                        "'A) 4', 'B) 6', 'C) 5', 'D) 3', " +
                        "'C', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What is the name of the longest running animated TV show in history?', " +
                        "'A) Family Guy', 'B) South Park', 'C) Futurama', 'D) The Simpsons', " +
                        "'D', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What is the most spoken language in the world?', " +
                        "'A) English', 'B) Spanish', 'C) Hindi', 'D) Mandarin', " +
                        "'D', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Which country has won the most FIFA World Cup titles?', " +
                        "'A) Germany', 'B) Argentina', 'C) Brazil', 'D) France', " +
                        "'C', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Who was historically considered the greatest traitor to George Washington?', " +
                        "'A) Benedict Arnold', 'B) Alexander Hamilton', 'C) Henry Knox', 'D) Robert Townsend', " +
                        "'A', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What did Thailand offer the USA during the Civil War?', " +
                        "'A) Money', 'B) Elephants', 'C) Silk Armor', 'D) Mongkut''s Firstborn', " +
                        "'B', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Who is a son of Ragnar Lothbrok, one of the most famous Vikings?', " +
                        "'A) Ivar the Boneless', 'B) Rollo', 'C) Erik the Red', 'D) Floki', " +
                        "'A', 'multiple choice')",
                
                "INSERT INTO questions VALUES (NULL, " +
                        "'How many bones are in the adult human body?', " +
                        "'A) 196', 'B) 206', 'C) 216', 'D) 226', " +
                        "'B', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Which element has the chemical symbol Au?', " +
                        "'A) Silver', 'B) Copper', 'C) Gold', 'D) Aluminum', " +
                        "'C', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Which planet has the most moons?', " +
                        "'A) Jupiter', 'B) Saturn', 'C) Uranus', 'D) Neptune', " +
                        "'B', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What is the speed of light?', " +
                        "'A) 200,000 km/s', 'B) 250,000 km/s', 'C) 300,000 km/s', 'D) 350,000 km/s', " +
                        "'C', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Which artist holds the record for the most streamed song on Spotify of all time?', " +
                        "'A) Drake', 'B) Ed Sheeran', 'C) The Weeknd', 'D) Bad Bunny', " +
                        "'D', 'multiple choice')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What year was Google founded?', " +
                        "'A) 1996', 'B) 1998', 'C) 2000', 'D) 2002', " +
                        "'B', 'multiple choice')",

                // ------------------------------------------------------------------------
                // TRUE/FALSE QUESTIONS (13 total)
                // ------------------------------------------------------------------------

                "INSERT INTO questions VALUES (NULL, " +
                        "'Jihyo from TWICE trained at JYP Entertainment for 10 years before making her debut.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'True', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'There are two Japanese members in TWICE.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'False', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'All opposite sides of a standard die add up to seven.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'True', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'The Amazon River is the longest river in the world.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'False', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'The Great Wall of China is visible from space with the naked eye.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'False', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Australia is both a country and a continent.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'True', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'The sun rises in the east.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'True', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Octopuses have three hearts.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'True', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Elvis Presley ran for president in 1977.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'False', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'France allied with the Continental army (Americans) in the Revolutionary War.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'True', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'The video game Minecraft has sold more copies than any other game in history.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'True', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'American soldiers were known as Red-Coats.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'False', 'true/false')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'World War II ended in 1953.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'False', 'true/false')",
                        
                "INSERT INTO questions VALUES (NULL, " +
                        "'The iPhone was first released in 2007.', " +
                        "'True', 'False', NULL, NULL, " +
                        "'True', 'true/false')",

                        

                // ------------------------------------------------------------------------
                // SHORT ANSWER QUESTIONS (7 total)
                // ------------------------------------------------------------------------

                "INSERT INTO questions VALUES (NULL, " +
                        "'Who played Ken in the 2023 live-action film Barbie?', " +
                        "NULL, NULL, NULL, NULL, " +
                        "'Ryan Gosling', 'short answer')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What was the name of the survival show that the K-Pop girl group TWICE formed from?', " +
                        "NULL, NULL, NULL, NULL, " +
                        "'SIXTEEN', 'short answer')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What is the name of the restaurant SpongeBob works at?', " +
                        "NULL, NULL, NULL, NULL, " +
                        "'Krusty Krab', 'short answer')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What streaming service is home to shows like Stranger Things?', " +
                        "NULL, NULL, NULL, NULL, " +
                        "'Netflix', 'short answer')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What year did America gain Independence?', " +
                        "NULL, NULL, NULL, NULL, " +
                        "'1776', 'short answer')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'Who is considered the King of Pop?', " +
                        "NULL, NULL, NULL, NULL, " +
                        "'Michael Jackson', 'short answer')",

                "INSERT INTO questions VALUES (NULL, " +
                        "'What famous ancient city was destroyed by a volcanic eruption in 79 AD?', " +
                        "NULL, NULL, NULL, NULL, " +
                        "'Pompeii', 'short answer')"
        };

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

    private void createLeaderboardTable() {
        String sql = "CREATE TABLE IF NOT EXISTS leaderboard (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "player_name TEXT NOT NULL, " +
                "time_seconds REAL NOT NULL, " +
                "correct_score INTEGER, " +
                "incorrect_score INTEGER)";

        try {
            Statement stmt = myConnection.createStatement();
            stmt.execute(sql);
            System.out.println("Leaderboard table created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating leaderboard table: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return myConnection;
    }

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

    public static void main(String[] args) {
        System.out.println("Testing database...");
        DatabaseManager db = DatabaseManager.getInstance();
        try {
            var stmt = db.getConnection().createStatement();
            var rs = stmt.executeQuery("SELECT COUNT(*) FROM questions");
            System.out.println("Total questions in database: " + rs.getInt(1));
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