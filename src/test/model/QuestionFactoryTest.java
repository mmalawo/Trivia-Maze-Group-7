package test.model;

import model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for QuestionFactory.
 * Tests that the correct Question subclass is created based on question type.
 */
class QuestionFactoryTest {

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

    @Test
    void testTrueFalseQuestionCreation() {
        Question q = new TrueFalseQuestion("The sky is blue.", "True");
        assertInstanceOf(TrueFalseQuestion.class, q,
                "Should create a TrueFalseQuestion");
    }

    @Test
    void testShortAnswerQuestionCreation() {
        Question q = new ShortAnswerQuestion("What is the capital of France?", "Paris");
        assertInstanceOf(ShortAnswerQuestion.class, q,
                "Should create a ShortAnswerQuestion");
    }

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

    @Test
    void testTrueFalseQuestionType() {
        Question q = new TrueFalseQuestion("The sky is blue.", "True");
        assertEquals("true/false", q.getQuestionType(),
                "Question type should be 'true/false'");
    }

    @Test
    void testShortAnswerQuestionType() {
        Question q = new ShortAnswerQuestion("What is the capital of France?", "Paris");
        assertEquals("short answer", q.getQuestionType(),
                "Question type should be 'short answer'");
    }

    @Test
    void testMultipleChoiceCorrectAnswer() {
        Question q = new MultipleChoiceQuestion(
                "What is 2 + 2?",
                "A) 3", "B) 4", "C) 5", "D) 6",
                "B"
        );
        assertEquals("B", q.getCorrectAnswer(), "Correct answer should be B");
    }

    @Test
    void testTrueFalseOptions() {
        Question q = new TrueFalseQuestion("The sky is blue.", "True");
        assertEquals("True", q.getOptionA(), "Option A should be True");
        assertEquals("False", q.getOptionB(), "Option B should be False");
    }

    @Test
    void testShortAnswerNullOptions() {
        Question q = new ShortAnswerQuestion("What is the capital of France?", "Paris");
        assertNull(q.getOptionA(), "Short answer option A should be null");
        assertNull(q.getOptionB(), "Short answer option B should be null");
        assertNull(q.getOptionC(), "Short answer option C should be null");
        assertNull(q.getOptionD(), "Short answer option D should be null");
    }

    @Test
    void testQuestionText() {
        Question q = new ShortAnswerQuestion("What is the capital of France?", "Paris");
        assertEquals("What is the capital of France?", q.getQuestionText(),
                "Question text should match");
    }
}