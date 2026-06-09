package test.model;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Maze class.
 * Tests maze dimensions, room access, entrance/exit setup, and room finding.
 */
class MazeTest {

    private Maze myMaze;

    @BeforeEach
    void setUp() {
        myMaze = new Maze(5, 5);
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                myMaze.setRooms(r, c, new Room());
            }
        }
    }

    @Test
    void testMazeHasFiveRows() {
        assertEquals(5, myMaze.getRows(), "Maze should have 5 rows");
    }

    @Test
    void testMazeHasFiveCols() {
        assertEquals(5, myMaze.getCols(), "Maze should have 5 columns");
    }

    @Test
    void testMazeIsAtLeastFourByFour() {
        assertTrue(myMaze.getRows() >= 4, "Maze should have at least 4 rows");
        assertTrue(myMaze.getCols() >= 4, "Maze should have at least 4 columns");
    }

    @Test
    void testGetRoomReturnsCorrectRoom() {
        Room room = myMaze.getRoom(2, 3);
        assertNotNull(room, "Room at [2][3] should not be null");
    }

    @Test
    void testSetEntrance() {
        Room entrance = myMaze.getRoom(2, 2);
        myMaze.setEntrance(entrance);
        assertEquals(entrance, myMaze.getEntrance(), "Entrance should match the set room");
    }

    @Test
    void testSetExitRoom() {
        Room exitRoom = myMaze.getRoom(0, 0);
        myMaze.setExitRoom(exitRoom);
        assertEquals(exitRoom, myMaze.getExit(), "Exit room should match the set room");
    }

    @Test
    void testSetExitDoor() {
        Door exitDoor = new Door();
        myMaze.setExitDoor(exitDoor);
        assertEquals(exitDoor, myMaze.getExitDoor(), "Exit door should match the set door");
    }

    @Test
    void testSetExitDoorDirection() {
        myMaze.setExitDoorDirection("north");
        assertEquals("north", myMaze.getExitDoorDirection(), "Exit door direction should be north");
    }

    @Test
    void testFindRoom() {
        Room room = myMaze.getRoom(1, 3);
        int[] pos = myMaze.findRoom(room);
        assertNotNull(pos, "findRoom should return a position");
        assertEquals(1, pos[0], "Row should be 1");
        assertEquals(3, pos[1], "Col should be 3");
    }

    @Test
    void testFindRoomReturnsNullForUnknownRoom() {
        Room unknownRoom = new Room();
        int[] pos = myMaze.findRoom(unknownRoom);
        assertNull(pos, "findRoom should return null for a room not in the maze");
    }

    @Test
    void testGeneratedMazeHasEntrance() {
        Maze generated = GenerateMaze.generateMaze();
        assertNotNull(generated.getEntrance(), "Generated maze should have an entrance");
    }

    @Test
    void testGeneratedMazeHasExitDoor() {
        Maze generated = GenerateMaze.generateMaze();
        assertNotNull(generated.getExitDoor(), "Generated maze should have an exit door");
    }

    @Test
    void testGeneratedMazeHasExitRoom() {
        Maze generated = GenerateMaze.generateMaze();
        assertNotNull(generated.getExit(), "Generated maze should have an exit room");
    }

    @Test
    void testAllRoomsNotNull() {
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                assertNotNull(myMaze.getRoom(r, c), "Room at [" + r + "][" + c + "] should not be null");
            }
        }
    }
}