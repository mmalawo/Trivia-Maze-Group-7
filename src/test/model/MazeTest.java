package test.model;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Maze} class.
 *
 * <p>These tests verify maze dimensions, room access, entrance and exit
 * setup, room lookup behavior, generated maze setup, and room initialization.</p>
 */
class MazeTest {

    /** The maze used for each test case. */
    private Maze myMaze;

    /**
     * Initializes a five-by-five maze with non-null rooms before each test.
     */
    @BeforeEach
    void setUp() {
        myMaze = new Maze(5, 5);
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                myMaze.setRooms(r, c, new Room());
            }
        }
    }

    /**
     * Tests that the maze stores the expected number of rows.
     */
    @Test
    void testMazeHasFiveRows() {
        assertEquals(5, myMaze.getRows(), "Maze should have 5 rows");
    }

    /**
     * Tests that the maze stores the expected number of columns.
     */
    @Test
    void testMazeHasFiveCols() {
        assertEquals(5, myMaze.getCols(), "Maze should have 5 columns");
    }

    /**
     * Tests that the maze meets the minimum size requirement.
     */
    @Test
    void testMazeIsAtLeastFourByFour() {
        assertTrue(myMaze.getRows() >= 4, "Maze should have at least 4 rows");
        assertTrue(myMaze.getCols() >= 4, "Maze should have at least 4 columns");
    }

    /**
     * Tests that retrieving a room from a valid location returns a non-null room.
     */
    @Test
    void testGetRoomReturnsCorrectRoom() {
        Room room = myMaze.getRoom(2, 3);
        assertNotNull(room, "Room at [2][3] should not be null");
    }

    /**
     * Tests that the maze entrance can be set and retrieved.
     */
    @Test
    void testSetEntrance() {
        Room entrance = myMaze.getRoom(2, 2);
        myMaze.setEntrance(entrance);
        assertEquals(entrance, myMaze.getEntrance(), "Entrance should match the set room");
    }

    /**
     * Tests that the maze exit room can be set and retrieved.
     */
    @Test
    void testSetExitRoom() {
        Room exitRoom = myMaze.getRoom(0, 0);
        myMaze.setExitRoom(exitRoom);
        assertEquals(exitRoom, myMaze.getExit(), "Exit room should match the set room");
    }

    /**
     * Tests that the maze exit door can be set and retrieved.
     */
    @Test
    void testSetExitDoor() {
        Door exitDoor = new Door();
        myMaze.setExitDoor(exitDoor);
        assertEquals(exitDoor, myMaze.getExitDoor(), "Exit door should match the set door");
    }

    /**
     * Tests that the maze exit door direction can be set and retrieved.
     */
    @Test
    void testSetExitDoorDirection() {
        myMaze.setExitDoorDirection("north");
        assertEquals("north", myMaze.getExitDoorDirection(), "Exit door direction should be north");
    }

    /**
     * Tests that the maze can find the coordinates of a room it contains.
     */
    @Test
    void testFindRoom() {
        Room room = myMaze.getRoom(1, 3);
        int[] pos = myMaze.findRoom(room);
        assertNotNull(pos, "findRoom should return a position");
        assertEquals(1, pos[0], "Row should be 1");
        assertEquals(3, pos[1], "Col should be 3");
    }

    /**
     * Tests that searching for a room not contained in the maze returns {@code null}.
     */
    @Test
    void testFindRoomReturnsNullForUnknownRoom() {
        Room unknownRoom = new Room();
        int[] pos = myMaze.findRoom(unknownRoom);
        assertNull(pos, "findRoom should return null for a room not in the maze");
    }

    /**
     * Tests that a generated maze has an entrance room.
     */
    @Test
    void testGeneratedMazeHasEntrance() {
        Maze generated = GenerateMaze.generateMaze();
        assertNotNull(generated.getEntrance(), "Generated maze should have an entrance");
    }

    /**
     * Tests that a generated maze has an exit door.
     */
    @Test
    void testGeneratedMazeHasExitDoor() {
        Maze generated = GenerateMaze.generateMaze();
        assertNotNull(generated.getExitDoor(), "Generated maze should have an exit door");
    }

    /**
     * Tests that a generated maze has an exit room.
     */
    @Test
    void testGeneratedMazeHasExitRoom() {
        Maze generated = GenerateMaze.generateMaze();
        assertNotNull(generated.getExit(), "Generated maze should have an exit room");
    }

    /**
     * Tests that every room in the initialized maze is non-null.
     */
    @Test
    void testAllRoomsNotNull() {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                assertNotNull(myMaze.getRoom(r, c), "Room at [" + r + "][" + c + "] should not be null");
            }
        }
    }
}