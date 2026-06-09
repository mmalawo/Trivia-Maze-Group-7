package test.model;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Door class.
 * Tests locking, unlocking, attempt tracking, and permanent closure behavior.
 */
class DoorTest {

    private Door myDoor;
    private Question myQuestion;

    @BeforeEach
    void setUp() {
        myDoor = new Door();
        // Create a simple multiple choice question for testing
        myQuestion = new MultipleChoiceQuestion(
                "What is 2 + 2?",
                "A) 3",
                "B) 4",
                "C) 5",
                "D) 6",
                "B"
        );
        // Inject question directly via setLocked workaround
        // We manually set the question by calling getQuestion and using reflection
        // Instead, we'll use attemptAnswer directly since getQuestion fetches lazily
    }

    @Test
    void testDoorStartsLocked() {
        assertTrue(myDoor.isLocked(), "Door should start locked");
    }

    @Test
    void testDoorStartsNotPermanentlyClosed() {
        assertFalse(myDoor.isPermanentlyClosed(), "Door should not start permanently closed");
    }

    @Test
    void testDoorStartsWithTwoAttempts() {
        assertEquals(2, myDoor.getAttemptsRemaining(), "Door should start with 2 attempts");
    }

    @Test
    void testSetPermanentlyClosed() {
        myDoor.setPermanentlyClosed(true);
        assertTrue(myDoor.isPermanentlyClosed(), "Door should be permanently closed after setting");
    }

    @Test
    void testSetPermanentlyClosedFalse() {
        myDoor.setPermanentlyClosed(true);
        myDoor.setPermanentlyClosed(false);
        assertFalse(myDoor.isPermanentlyClosed(), "Door should not be permanently closed after resetting");
    }

    @Test
    void testAttemptAnswerOnPermanentlyClosedDoor() {
        myDoor.setPermanentlyClosed(true);
        assertFalse(myDoor.attemptAnswer("B"), "Permanently closed door should reject all answers");
    }

    @Test
    void testSetLocked() {
        myDoor.setLocked(false);
        assertFalse(myDoor.isLocked(), "Door should be unlocked after setLocked(false)");
    }

    @Test
    void testReset() {
        myDoor.setLocked(false);
        myDoor.setPermanentlyClosed(true);
        myDoor.reset();

        assertTrue(myDoor.isLocked(), "Door should be locked after reset");
        assertFalse(myDoor.isPermanentlyClosed(), "Door should not be permanently closed after reset");
        assertEquals(2, myDoor.getAttemptsRemaining(), "Door should have 2 attempts after reset");
    }
}