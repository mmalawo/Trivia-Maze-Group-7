package model;

import java.io.Serializable;

/**
 * Represents the maze used in the game.
 *
 * <p>A maze consists of a two-dimensional grid of rooms,
 * an entrance room, an exit room, and an exit door that
 * allows the player to complete the maze.</p>
 */
public class Maze implements Serializable {
    private static final long serialVersionUID = 1L;

    private Room[][] rooms;
    private int rows;
    private int cols;
    private Room entrance;
    private Room exitRoom;
    private Door exitDoor;
    private String exitDoorDirection;

    /**
     * Constructs a maze with the specified dimensions.
     *
     * @param rows the number of rows in the maze
     * @param cols the number of columns in the maze
     */
    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.rooms = new Room[rows][cols];
    }



    // ===================================================================================================================
    //
    //   GETTERS AND SETTERS
    //
    // ===================================================================================================================


    // --------------------------
    //  GETTERS
    // --------------------------
    /**
     * Returns the two-dimensional array of rooms that make up the maze.
     * @return the room grid
     */
    public Room[][] getRooms() {
        return rooms;
    }

    /**
     * Returns the room at the specified row and column.
     *
     * @param r the row index
     * @param c the column index
     * @return the room at the specified location
     */
    public Room getRoom(int r, int c) {
        return rooms[r][c];
    }

    /**
     * Returns the entrance room of the maze.
     *
     * @return the entrance room
     */
    public Room getEntrance() {
        return entrance;
    }

    /**
     * Returns the exit room of the maze.
     *
     * @return the exit room
     */
    public Room getExit() {
        return exitRoom;
    }

    /**
     * Returns the exit door of the maze.
     *
     * @return the exit door
     */
    public Door getExitDoor() {
        return exitDoor;
    }

    /**
     * Returns the direction of the exit door.
     *
     * @return the exit door direction
     */
    public String getExitDoorDirection() {
        return exitDoorDirection;
    }

    /**
     * Returns the number of rows in the maze.
     *
     * @return the row count
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the number of columns in the maze.
     *
     * @return the column count
     */
    public int getCols() {
        return cols;
    }

    // --------------------------
    //  SETTERS
    // --------------------------

    /**
     * Stores a room at the specified location in the maze.
     *
     * @param r the row index
     * @param c the column index
     * @param room the room to store
     */
    public void setRooms(int r, int c, Room room) {
        rooms[r][c] = room;
    }

    /**
     * Sets the entrance room of the maze.
     *
     * @param entrance the entrance room
     */
    public void setEntrance(Room entrance) {
        this.entrance = entrance;
    }

    /**
     * Sets the exit room of the maze.
     *
     * @param exitRoom the exit room
     */
    public void setExitRoom(Room exitRoom) {
        this.exitRoom = exitRoom;
    }

    /**
     * Sets the exit door of the maze.
     *
     * @param exitDoor the exit door
     */
    public void setExitDoor(Door exitDoor) {
        this.exitDoor = exitDoor;
    }

    /**
     * Sets the direction of the exit door.
     *
     * @param exitDoorDirection the exit door direction
     */
    public void setExitDoorDirection(String exitDoorDirection) {
        this.exitDoorDirection = exitDoorDirection;
    }



// ===================================================================================================================





    /**
     * Finds the coordinates of a room within the maze.
     *
     * @param target the room to locate
     * @return an array containing the row and column of the room,
     *         or {@code null} if the room is not found
     */
    public int[] findRoom(Room target) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (rooms[r][c] == target) {
                    return new int[]{r, c};
                }
            }
        }
        return null;
    }
}