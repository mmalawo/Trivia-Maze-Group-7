package model;

import java.io.Serializable;

/**
 * This class represents a single trivia question in the Trivia Maze game.
 * It can hold three types of questions: multiple choice, true/false, and short answer.
 * Think of this like a flashcard object that stores everything about one question.
 */
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    // The actual question being asked, e.g. "What is the capital of France?"
    private String myQuestionText;

    // These are the answer choices for MULTIPLE CHOICE questions only.
    // For true/false: optionA = "True", optionB = "False", C and D will be null.
    // For short answer: all options will be null since the player types their answer.
    private String myOptionA;
    private String myOptionB;
    private String myOptionC;
    private String myOptionD;

    // The correct answer to the question.
    // For multiple choice: this would be "A", "B", "C", or "D"
    // For true/false: this would be "True" or "False"
    // For short answer: this would be the expected answer string e.g. "Paris"
    private String myCorrectAnswer;

    // Tells us what kind of question this is.
    // Can only be one of three values: "multiple choice", "true/false", or "short answer"
    // This is important because the GUI will use this to decide how to display the question
    // (4 buttons for multiple choice, 2 buttons for true/false, text box for short answer)
    private String myQuestionType;

    /**
     * This is the constructor - it creates a new Question object with all its information.
     * When we pull a question from the SQLite database, we'll use this constructor
     * to turn that raw data into a Question object the rest of the code can use.
     *
     * @param theQuestionText the text of the question being asked
     * @param theOptionA      first answer choice (or "True" for true/false, null for short answer)
     * @param theOptionB      second answer choice (or "False" for true/false, null for short answer)
     * @param theOptionC      third answer choice (null if not multiple choice)
     * @param theOptionD      fourth answer choice (null if not multiple choice)
     * @param theCorrectAnswer the correct answer to the question
     * @param theQuestionType  the type of question: "multiple choice", "true/false", or "short answer"
     */
    public Question(final String theQuestionText,
                    final String theOptionA,
                    final String theOptionB,
                    final String theOptionC,
                    final String theOptionD,
                    final String theCorrectAnswer,
                    final String theQuestionType) {

        // Store each parameter into its corresponding field
        this.myQuestionText = theQuestionText;
        this.myOptionA = theOptionA;
        this.myOptionB = theOptionB;
        this.myOptionC = theOptionC;
        this.myOptionD = theOptionD;
        this.myCorrectAnswer = theCorrectAnswer;
        this.myQuestionType = theQuestionType;
    }

    // -----------------------------------------------------------------------
    // GETTERS - these allow other classes to READ the question's information.
    // We don't have setters because a question shouldn't change once created.
    // -----------------------------------------------------------------------

    /** @return the full text of the question */
    public String getQuestionText() { return myQuestionText; }

    /** @return option A (or "True" for true/false, null for short answer) */
    public String getOptionA() { return myOptionA; }

    /** @return option B (or "False" for true/false, null for short answer) */
    public String getOptionB() { return myOptionB; }

    /** @return option C (null if not a multiple choice question) */
    public String getOptionC() { return myOptionC; }

    /** @return option D (null if not a multiple choice question) */
    public String getOptionD() { return myOptionD; }

    /** @return the correct answer to this question */
    public String getCorrectAnswer() { return myCorrectAnswer; }

    /** @return the question type: "multiple choice", "true/false", or "short answer" */
    public String getQuestionType() { return myQuestionType; }

    /**
     * toString() gives us a readable summary of the question.
     * This is useful for debugging - if we print a Question object,
     * we'll see the type, question text, and correct answer instead of
     * a memory address like "model.Question@1a2b3c".
     *
     * @return a formatted string showing key question info
     */
    @Override
    public String toString() {
        return "Question{" +
                "type='" + myQuestionType + '\'' +
                ", question='" + myQuestionText + '\'' +
                ", correctAnswer='" + myCorrectAnswer + '\'' +
                '}';
    }
}