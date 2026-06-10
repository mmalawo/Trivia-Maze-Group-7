package model;

import java.io.Serializable;

/**
 * Represents the maze used in the game.
 *
 * <p>A maze contains a two-dimensional grid of rooms, an entrance room,
 * an exit room, an exit door, and the direction of the exit door.</p>
 */
public class Maze implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Room[][] myRooms;
    private final int myRows;
    private final int myCols;
    private Room myEntrance;
    private Room myExitRoom;
    private Door myExitDoor;
    private String myExitDoorDirection;

    /**
     * Constructs a maze with the specified number of rows and columns.
     *
     * @param theRows the number of rows in the maze
     * @param theColumns the number of columns in the maze
     */
    public Maze(final int theRows, final int theColumns) {
        this.myRows = theRows;
        this.myCols = theColumns;
        this.myRooms = new Room[theRows][theColumns];
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
     * Returns the room at the specified row and column.
     *
     * @param theRow the row index
     * @param theColumn the column index
     * @return the room at the specified location
     */
    public Room getRoom(final int theRow, final int theColumn) {
        return myRooms[theRow][theColumn];
    }

    /**
     * Returns the entrance room of the maze.
     *
     * @return the entrance room
     */
    public Room getEntrance() {
        return myEntrance;
    }

    /**
     * Returns the exit room of the maze.
     *
     * @return the exit room
     */
    public Room getExit() {
        return myExitRoom;
    }

    /**
     * Returns the exit door of the maze.
     *
     * @return the exit door
     */
    public Door getExitDoor() {
        return myExitDoor;
    }

    /**
     * Returns the direction of the exit door.
     *
     * @return the exit door direction
     */
    public String getExitDoorDirection() {
        return myExitDoorDirection;
    }

    /**
     * Returns the number of rows in the maze.
     *
     * @return the row count
     */
    public int getRows() {
        return myRows;
    }

    /**
     * Returns the number of columns in the maze.
     *
     * @return the column count
     */
    public int getCols() {
        return myCols;
    }

    // --------------------------
    //  SETTERS
    // --------------------------

    /**
     * Stores a room at the specified row and column in the maze.
     *
     * @param theRow the row index
     * @param theColumn the column index
     * @param theRoom the room to store
     */
    public void setRooms(final int theRow, final int theColumn, final Room theRoom) {
        myRooms[theRow][theColumn] = theRoom;
    }

    /**
     * Sets the entrance room of the maze.
     *
     * @param theEntrance the entrance room
     */
    public void setEntrance(final Room theEntrance) {
        this.myEntrance = theEntrance;
    }

    /**
     * Sets the exit room of the maze.
     *
     * @param theExitRoom the exit room
     */
    public void setExitRoom(final Room theExitRoom) {
        this.myExitRoom = theExitRoom;
    }

    /**
     * Sets the exit door of the maze.
     *
     * @param theExitDoor the exit door
     */
    public void setExitDoor(final Door theExitDoor) {
        this.myExitDoor = theExitDoor;
    }

    /**
     * Sets the direction of the exit door.
     *
     * @param theExitDoorDirection the exit door direction
     */
    public void setExitDoorDirection(final String theExitDoorDirection) {
        this.myExitDoorDirection = theExitDoorDirection;
    }



// ===================================================================================================================

    /**
     * Finds the coordinates of the specified room within the maze.
     *
     * <p>The returned array contains two values: the row index at position
     * {@code 0} and the column index at position {@code 1}.</p>
     *
     * @param theTarget the room to locate
     * @return an array containing the row and column of the room;
     *         {@code null} if the room is not found
     */
    public int[] findRoom(final Room theTarget) {
        for (int r = 0; r < myRows; r++) {
            for (int c = 0; c < myCols; c++) {
                if (myRooms[r][c] == theTarget) {
                    return new int[]{r, c};
                }
            }
        }
        return null;
    }
}