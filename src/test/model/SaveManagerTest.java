package test.model;

import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SaveManager.
 * Tests save, load, file existence check, and deletion.
 */
class SaveManagerTest {

    private Player myPlayer;
    private Maze myMaze;

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

    @AfterEach
    void tearDown() {
        SaveManager.deleteSaveFile();
    }

    @Test
    void testSaveFileDoesNotExistInitially() {
        assertFalse(SaveManager.saveFileExists(), "Save file should not exist before saving");
    }

    @Test
    void testSaveGameCreatesFile() {
        Memento memento = new Memento(myPlayer, myMaze);
        SaveManager.saveGame(memento);
        assertTrue(SaveManager.saveFileExists(), "Save file should exist after saving");
    }

    @Test
    void testLoadGameReturnsNullWhenNoFile() {
        assertNull(SaveManager.loadGame(), "Load should return null when no save file exists");
    }

    @Test
    void testSaveAndLoadRestoresPlayerName() {
        Memento memento = new Memento(myPlayer, myMaze);
        SaveManager.saveGame(memento);
        Memento loaded = SaveManager.loadGame();
        assertNotNull(loaded, "Loaded memento should not be null");
        assertEquals("TestPlayer", loaded.getPlayer().getName(), "Player name should be restored");
    }

    @Test
    void testSaveAndLoadRestoresCorrectScore() {
        Memento memento = new Memento(myPlayer, myMaze);
        SaveManager.saveGame(memento);
        Memento loaded = SaveManager.loadGame();
        assertEquals(5, loaded.getPlayer().getCorrectScore(), "Correct score should be restored");
    }

    @Test
    void testSaveAndLoadRestoresIncorrectScore() {
        Memento memento = new Memento(myPlayer, myMaze);
        SaveManager.saveGame(memento);
        Memento loaded = SaveManager.loadGame();
        assertEquals(2, loaded.getPlayer().getIncorrectScore(), "Incorrect score should be restored");
    }

    @Test
    void testSaveAndLoadRestoresMazeDimensions() {
        Memento memento = new Memento(myPlayer, myMaze);
        SaveManager.saveGame(memento);
        Memento loaded = SaveManager.loadGame();
        assertEquals(5, loaded.getMaze().getRows(), "Maze rows should be restored");
        assertEquals(5, loaded.getMaze().getCols(), "Maze cols should be restored");
    }

    @Test
    void testDeleteSaveFile() {
        Memento memento = new Memento(myPlayer, myMaze);
        SaveManager.saveGame(memento);
        assertTrue(SaveManager.saveFileExists(), "Save file should exist before deletion");
        SaveManager.deleteSaveFile();
        assertFalse(SaveManager.saveFileExists(), "Save file should not exist after deletion");
    }

    @Test
    void testDeleteSaveFileWhenNoFileExists() {
        assertDoesNotThrow(() -> SaveManager.deleteSaveFile(),
                "Deleting non-existent save file should not throw");
    }
}