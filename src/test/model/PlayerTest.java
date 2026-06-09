package test.model;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Player class.
 * Tests score tracking, timer behavior, room tracking, and reset functionality.
 */
class PlayerTest {

    private Player myPlayer;

    @BeforeEach
    void setUp() {
        myPlayer = new Player();
    }

    @Test
    void testInitialCorrectScoreIsZero() {
        assertEquals(0, myPlayer.getCorrectScore(), "Correct score should start at 0");
    }

    @Test
    void testInitialIncorrectScoreIsZero() {
        assertEquals(0, myPlayer.getIncorrectScore(), "Incorrect score should start at 0");
    }

    @Test
    void testIncrementCorrectScore() {
        myPlayer.incrementCorrectScore();
        assertEquals(1, myPlayer.getCorrectScore(), "Correct score should be 1 after one increment");
    }

    @Test
    void testIncrementIncorrectScore() {
        myPlayer.incrementIncorrectScore();
        assertEquals(1, myPlayer.getIncorrectScore(), "Incorrect score should be 1 after one increment");
    }

    @Test
    void testIncrementCorrectScoreMultipleTimes() {
        myPlayer.incrementCorrectScore();
        myPlayer.incrementCorrectScore();
        myPlayer.incrementCorrectScore();
        assertEquals(3, myPlayer.getCorrectScore(), "Correct score should be 3 after three increments");
    }

    @Test
    void testSetName() {
        myPlayer.setName("Makani");
        assertEquals("Makani", myPlayer.getName(), "Player name should be Makani");
    }

    @Test
    void testSetCurrentRoom() {
        Room room = new Room();
        myPlayer.setCurrentRoom(room);
        assertEquals(room, myPlayer.getCurrentRoom(), "Current room should match the set room");
    }

    @Test
    void testSetCorrectScore() {
        myPlayer.setCorrectScore(10);
        assertEquals(10, myPlayer.getCorrectScore(), "Correct score should be 10");
    }

    @Test
    void testSetIncorrectScore() {
        myPlayer.setIncorrectScore(5);
        assertEquals(5, myPlayer.getIncorrectScore(), "Incorrect score should be 5");
    }

    @Test
    void testSetRecordTime() {
        myPlayer.setRecordTime(99.9);
        assertEquals(99.9, myPlayer.getRecordTime(), 0.001, "Record time should be 99.9");
    }

    @Test
    void testTimerStartAndStop() throws InterruptedException {
        myPlayer.startTimer();
        Thread.sleep(100);
        myPlayer.stopTimer();
        assertTrue(myPlayer.getRecordTime() >= 0.1, "Record time should be at least 0.1 seconds");
    }

    @Test
    void testSaveAndResumeTimer() throws InterruptedException {
        myPlayer.startTimer();
        Thread.sleep(200);
        myPlayer.saveElapsedTime();
        double savedTime = myPlayer.getSavedElapsedTime();
        myPlayer.resumeTimer();
        assertTrue(myPlayer.elapsedTime() >= savedTime, "Elapsed time after resume should be >= saved time");
    }

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

    @Test
    void testIsGameOverWhenNoAttemptsRemain() {
        myPlayer.setRemainingAttempts(0);
        assertTrue(myPlayer.isGameOver(), "Game should be over when attempts reach 0");
    }

    @Test
    void testIsGameOverWhenAttemptsRemain() {
        myPlayer.setRemainingAttempts(2);
        assertFalse(myPlayer.isGameOver(), "Game should not be over when attempts remain");
    }

    @Test
    void testDecrementAttempts() {
        myPlayer.setRemainingAttempts(2);
        myPlayer.decrementAttempts();
        assertEquals(1, myPlayer.getRemainingAttempts(), "Remaining attempts should be 1 after decrement");
    }
}