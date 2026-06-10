package test.model;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Player} class.
 *
 * <p>These tests verify score tracking, player name storage, timer behavior,
 * room tracking, reset behavior, and remaining-attempt logic.</p>
 */
class PlayerTest {

    /** The player used for each test case. */
    private Player myPlayer;

    /**
     * Initializes a new player before each test.
     */
    @BeforeEach
    void setUp() {
        myPlayer = new Player();
    }

    /**
     * Tests that a new player starts with a correct score of zero.
     */
    @Test
    void testInitialCorrectScoreIsZero() {
        assertEquals(0, myPlayer.getCorrectScore(), "Correct score should start at 0");
    }

    /**
     * Tests that a new player starts with an incorrect score of zero.
     */
    @Test
    void testInitialIncorrectScoreIsZero() {
        assertEquals(0, myPlayer.getIncorrectScore(), "Incorrect score should start at 0");
    }

    /**
     * Tests that incrementing the correct score increases it by one.
     */
    @Test
    void testIncrementCorrectScore() {
        myPlayer.incrementCorrectScore();
        assertEquals(1, myPlayer.getCorrectScore(), "Correct score should be 1 after one increment");
    }

    /**
     * Tests that incrementing the incorrect score increases it by one.
     */
    @Test
    void testIncrementIncorrectScore() {
        myPlayer.incrementIncorrectScore();
        assertEquals(1, myPlayer.getIncorrectScore(), "Incorrect score should be 1 after one increment");
    }

    /**
     * Tests that the correct score can be incremented multiple times.
     */
    @Test
    void testIncrementCorrectScoreMultipleTimes() {
        myPlayer.incrementCorrectScore();
        myPlayer.incrementCorrectScore();
        myPlayer.incrementCorrectScore();
        assertEquals(3, myPlayer.getCorrectScore(), "Correct score should be 3 after three increments");
    }

    /**
     * Tests that the player's name can be set and retrieved.
     */
    @Test
    void testSetName() {
        myPlayer.setName("Makani");
        assertEquals("Makani", myPlayer.getName(), "Player name should be Makani");
    }

    /**
     * Tests that the player's current room can be set and retrieved.
     */
    @Test
    void testSetCurrentRoom() {
        Room room = new Room();
        myPlayer.setCurrentRoom(room);
        assertEquals(room, myPlayer.getCurrentRoom(), "Current room should match the set room");
    }

    /**
     * Tests that the player's correct score can be set directly.
     */
    @Test
    void testSetCorrectScore() {
        myPlayer.setCorrectScore(10);
        assertEquals(10, myPlayer.getCorrectScore(), "Correct score should be 10");
    }

    /**
     * Tests that the player's incorrect score can be set directly.
     */
    @Test
    void testSetIncorrectScore() {
        myPlayer.setIncorrectScore(5);
        assertEquals(5, myPlayer.getIncorrectScore(), "Incorrect score should be 5");
    }

    /**
     * Tests that the player's recorded completion time can be set and retrieved.
     */
    @Test
    void testSetRecordTime() {
        myPlayer.setRecordTime(99.9);
        assertEquals(99.9, myPlayer.getRecordTime(), 0.001, "Record time should be 99.9");
    }

    /**
     * Tests that starting and stopping the timer records elapsed time.
     *
     * @throws InterruptedException if the sleep operation is interrupted
     */
    @Test
    void testTimerStartAndStop() throws InterruptedException {
        myPlayer.startTimer();
        Thread.sleep(100);
        myPlayer.stopTimer();
        assertTrue(myPlayer.getRecordTime() >= 0.1, "Record time should be at least 0.1 seconds");
    }

    /**
     * Tests that the timer can save elapsed time and resume from that value.
     *
     * @throws InterruptedException if the sleep operation is interrupted
     */
    @Test
    void testSaveAndResumeTimer() throws InterruptedException {
        myPlayer.startTimer();
        Thread.sleep(200);
        myPlayer.saveElapsedTime();
        double savedTime = myPlayer.getSavedElapsedTime();
        myPlayer.resumeTimer();
        assertTrue(myPlayer.elapsedTime() >= savedTime, "Elapsed time after resume should be >= saved time");
    }

    /**
     * Tests that resetting the player clears name, scores, current room,
     * and recorded completion time.
     */
    @Test
    void testReset() {
        myPlayer.setName("Makani");
        myPlayer.incrementCorrectScore();
        myPlayer.incrementIncorrectScore();
        myPlayer.setCurrentRoom(new Room());
        myPlayer.reset();

        assertNull(myPlayer.getName(), "Name should be null after reset");
        assertEquals(0, myPlayer.getCorrectScore(), "Correct score should be 0 after reset");
        assertEquals(0, myPlayer.getIncorrectScore(), "Incorrect score should be 0 after reset");
        assertNull(myPlayer.getCurrentRoom(), "Current room should be null after reset");
        assertEquals(0.0, myPlayer.getRecordTime(), 0.001, "Record time should be 0 after reset");
    }

    /**
     * Tests that the game is considered over when no attempts remain.
     */
    @Test
    void testIsGameOverWhenNoAttemptsRemain() {
        myPlayer.setRemainingAttempts(0);
        assertTrue(myPlayer.isGameOver(), "Game should be over when attempts reach 0");
    }

    /**
     * Tests that the game is not considered over when attempts remain.
     */
    @Test
    void testIsGameOverWhenAttemptsRemain() {
        myPlayer.setRemainingAttempts(2);
        assertFalse(myPlayer.isGameOver(), "Game should not be over when attempts remain");
    }

    /**
     * Tests that decrementing attempts reduces the remaining attempt count by one.
     */
    @Test
    void testDecrementAttempts() {
        myPlayer.setRemainingAttempts(2);
        myPlayer.decrementAttempts();
        assertEquals(1, myPlayer.getRemainingAttempts(), "Remaining attempts should be 1 after decrement");
    }
}