package test.model;

import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link SaveManager} class.
 *
 * <p>These tests verify saving game state, loading saved game state,
 * checking for save file existence, and deleting save files.</p>
 */
class SaveManagerTest {

    /** The player used to create test save data. */
    private Player myPlayer;

    /** The maze used to create test save data. */
    private Maze myMaze;

    /**
     * Initializes test player and maze data before each test.
     *
     * <p>This also deletes any existing save file so each test starts
     * with a clean save state.</p>
     */
    @BeforeEach
    void setUp() {
        myPlayer = new Player();
        myPlayer.setName("TestPlayer");
        myPlayer.setCorrectScore(5);
        myPlayer.setIncorrectScore(2);
        myPlayer.setRecordTime(120.0);

        myMaze = new Maze(5, 5);
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                myMaze.setRooms(r, c, new Room());
            }
        }
        myMaze.setEntrance(myMaze.getRoom(2, 2));

        SaveManager.deleteSaveFile();
    }

    /**
     * Deletes the save file after each test.
     */
    @AfterEach
    void tearDown() {
        SaveManager.deleteSaveFile();
    }

    /**
     * Tests that no save file exists before a game is saved.
     */
    @Test
    void testSaveFileDoesNotExistInitially() {
        assertFalse(SaveManager.saveFileExists(), "Save file should not exist before saving");
    }

    /**
     * Tests that saving a game creates a save file.
     */
    @Test
    void testSaveGameCreatesFile() {
        Memento memento = new Memento(myPlayer, myMaze);
        SaveManager.saveGame(memento);
        assertTrue(SaveManager.saveFileExists(), "Save file should exist after saving");
    }

    /**
     * Tests that loading returns {@code null} when no save file exists.
     */
    @Test
    void testLoadGameReturnsNullWhenNoFile() {
        assertNull(SaveManager.loadGame(), "Load should return null when no save file exists");
    }

    /**
     * Tests that saving and loading restores the player's name.
     */
    @Test
    void testSaveAndLoadRestoresPlayerName() {
        Memento memento = new Memento(myPlayer, myMaze);
        SaveManager.saveGame(memento);
        Memento loaded = SaveManager.loadGame();
        assertNotNull(loaded, "Loaded memento should not be null");
        assertEquals("TestPlayer", loaded.getPlayer().getName(), "Player name should be restored");
    }

    /**
     * Tests that saving and loading restores the player's correct score.
     */
    @Test
    void testSaveAndLoadRestoresCorrectScore() {
        Memento memento = new Memento(myPlayer, myMaze);
        SaveManager.saveGame(memento);
        Memento loaded = SaveManager.loadGame();
        assertEquals(5, loaded.getPlayer().getCorrectScore(), "Correct score should be restored");
    }

    /**
     * Tests that saving and loading restores the player's incorrect score.
     */
    @Test
    void testSaveAndLoadRestoresIncorrectScore() {
        Memento memento = new Memento(myPlayer, myMaze);
        SaveManager.saveGame(memento);
        Memento loaded = SaveManager.loadGame();
        assertEquals(2, loaded.getPlayer().getIncorrectScore(), "Incorrect score should be restored");
    }

    /**
     * Tests that saving and loading restores the maze dimensions.
     */
    @Test
    void testSaveAndLoadRestoresMazeDimensions() {
        Memento memento = new Memento(myPlayer, myMaze);
        SaveManager.saveGame(memento);
        Memento loaded = SaveManager.loadGame();
        assertEquals(5, loaded.getMaze().getRows(), "Maze rows should be restored");
        assertEquals(5, loaded.getMaze().getCols(), "Maze cols should be restored");
    }

    /**
     * Tests that deleting a save file removes it from disk.
     */
    @Test
    void testDeleteSaveFile() {
        Memento memento = new Memento(myPlayer, myMaze);
        SaveManager.saveGame(memento);
        assertTrue(SaveManager.saveFileExists(), "Save file should exist before deletion");
        SaveManager.deleteSaveFile();
        assertFalse(SaveManager.saveFileExists(), "Save file should not exist after deletion");
    }

    /**
     * Tests that deleting a save file does not throw an exception
     * when no save file exists.
     */
    @Test
    void testDeleteSaveFileWhenNoFileExists() {
        assertDoesNotThrow(() -> SaveManager.deleteSaveFile(),
                "Deleting non-existent save file should not throw");
    }
}