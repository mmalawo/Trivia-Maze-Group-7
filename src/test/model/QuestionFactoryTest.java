package test.model;

import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for question creation behavior.
 *
 * <p>These tests verify that multiple-choice, true/false, and short-answer
 * questions are created with the correct subclass type, question type,
 * answer options, correct answer, and question text.</p>
 */
class QuestionFactoryTest {

    /**
     * Tests that a multiple-choice question is created as a
     * {@link MultipleChoiceQuestion}.
     */
    @Test
    void testMultipleChoiceQuestionCreation() {
        Question q = new MultipleChoiceQuestion(
                "What is 2 + 2?",
                "A) 3", "B) 4", "C) 5", "D) 6",
                "B"
        );
        assertInstanceOf(MultipleChoiceQuestion.class, q,
                "Should create a MultipleChoiceQuestion");
    }

    /**
     * Tests that a true/false question is created as a
     * {@link TrueFalseQuestion}.
     */
    @Test
    void testTrueFalseQuestionCreation() {
        Question q = new TrueFalseQuestion("The sky is blue.", "True");
        assertInstanceOf(TrueFalseQuestion.class, q,
                "Should create a TrueFalseQuestion");
    }

    /**
     * Tests that a short-answer question is created as a
     * {@link ShortAnswerQuestion}.
     */
    @Test
    void testShortAnswerQuestionCreation() {
        Question q = new ShortAnswerQuestion("What is the capital of France?", "Paris");
        assertInstanceOf(ShortAnswerQuestion.class, q,
                "Should create a ShortAnswerQuestion");
    }

    /**
     * Tests that a multiple-choice question stores the correct question type.
     */
    @Test
    void testMultipleChoiceQuestionType() {
        Question q = new MultipleChoiceQuestion(
                "What is 2 + 2?",
                "A) 3", "B) 4", "C) 5", "D) 6",
                "B"
        );
        assertEquals("multiple choice", q.getQuestionType(),
                "Question type should be 'multiple choice'");
    }

    /**
     * Tests that a true/false question stores the correct question type.
     */
    @Test
    void testTrueFalseQuestionType() {
        Question q = new TrueFalseQuestion("The sky is blue.", "True");
        assertEquals("true/false", q.getQuestionType(),
                "Question type should be 'true/false'");
    }

    /**
     * Tests that a short-answer question stores the correct question type.
     */
    @Test
    void testShortAnswerQuestionType() {
        Question q = new ShortAnswerQuestion("What is the capital of France?", "Paris");
        assertEquals("short answer", q.getQuestionType(),
                "Question type should be 'short answer'");
    }

    /**
     * Tests that a multiple-choice question stores the correct answer.
     */
    @Test
    void testMultipleChoiceCorrectAnswer() {
        Question q = new MultipleChoiceQuestion(
                "What is 2 + 2?",
                "A) 3", "B) 4", "C) 5", "D) 6",
                "B"
        );
        assertEquals("B", q.getCorrectAnswer(), "Correct answer should be B");
    }

    /**
     * Tests that a true/false question stores {@code "True"} and {@code "False"}
     * as its answer options.
     */
    @Test
    void testTrueFalseOptions() {
        Question q = new TrueFalseQuestion("The sky is blue.", "True");
        assertEquals("True", q.getOptionA(), "Option A should be True");
        assertEquals("False", q.getOptionB(), "Option B should be False");
    }

    /**
     * Tests that a short-answer question stores no answer options.
     */
    @Test
    void testShortAnswerNullOptions() {
        Question q = new ShortAnswerQuestion("What is the capital of France?", "Paris");
        assertNull(q.getOptionA(), "Short answer option A should be null");
        assertNull(q.getOptionB(), "Short answer option B should be null");
        assertNull(q.getOptionC(), "Short answer option C should be null");
        assertNull(q.getOptionD(), "Short answer option D should be null");
    }

    /**
     * Tests that a question stores the expected question text.
     */
    @Test
    void testQuestionText() {
        Question q = new ShortAnswerQuestion("What is the capital of France?", "Paris");
        assertEquals("What is the capital of France?", q.getQuestionText(),
                "Question text should match");
    }
}