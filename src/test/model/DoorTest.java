package test.model;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Door} class.
 *
 * <p>These tests verify the door's initial locked state, permanent closure
 * behavior, attempt tracking, locked-state updates, and reset behavior.</p>
 */
class DoorTest {

    private Door myDoor;
    private Question myQuestion;

    /**
     * Initializes a new door before each test.
     */
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

    /**
     * Tests that a new door starts in the locked state.
     */
    @Test
    void testDoorStartsLocked() {
        assertTrue(myDoor.isLocked(), "Door should start locked");
    }

    /**
     * Tests that a new door is not permanently closed.
     */
    @Test
    void testDoorStartsNotPermanentlyClosed() {
        assertFalse(myDoor.isPermanentlyClosed(), "Door should not start permanently closed");
    }

    /**
     * Tests that a new door starts with two remaining attempts.
     */
    @Test
    void testDoorStartsWithTwoAttempts() {
        assertEquals(2, myDoor.getAttemptsRemaining(), "Door should start with 2 attempts");
    }

    /**
     * Tests that a door can be marked as permanently closed.
     */
    @Test
    void testSetPermanentlyClosed() {
        myDoor.setPermanentlyClosed(true);
        assertTrue(myDoor.isPermanentlyClosed(), "Door should be permanently closed after setting");
    }

    /**
     * Tests that a door's permanent closure state can be cleared.
     */
    @Test
    void testSetPermanentlyClosedFalse() {
        myDoor.setPermanentlyClosed(true);
        myDoor.setPermanentlyClosed(false);
        assertFalse(myDoor.isPermanentlyClosed(), "Door should not be permanently closed after resetting");
    }

    /**
     * Tests that a permanently closed door rejects answer attempts.
     */
    @Test
    void testAttemptAnswerOnPermanentlyClosedDoor() {
        myDoor.setPermanentlyClosed(true);
        assertFalse(myDoor.attemptAnswer("B"), "Permanently closed door should reject all answers");
    }

    /**
     * Tests that a door can be manually unlocked.
     */
    @Test
    void testSetLocked() {
        myDoor.setLocked(false);
        assertFalse(myDoor.isLocked(), "Door should be unlocked after setLocked(false)");
    }

    /**
     * Tests that resetting a door restores its initial locked state,
     * clears permanent closure, and restores the attempt count.
     */
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