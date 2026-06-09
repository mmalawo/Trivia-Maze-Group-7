package model;

import java.io.Serializable;

/**
 * Represents a room within the maze.
 * Each room contains up to four doors, tracks whether it has
 * been visited, and stores information about available paths.
 */
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private Door northDoor;
    private Door southDoor;
    private Door eastDoor;
    private Door westDoor;
    private boolean visited;
    private int doors;
    private boolean hasLeft;
    private boolean hasRight;
    private boolean hasCenter;

    /**
     * Constructs a room with four doors initialized in their
     * default state and no visitation or path flags set.
     */
    public Room() {
        this.northDoor = new Door();
        this.southDoor = new Door();
        this.eastDoor = new Door();
        this.westDoor = new Door();
        this.visited = false;
        this.doors = 0;
        this.hasLeft = false;
        this.hasRight = false;
        this.hasCenter = false;
    }


    // =====================================================
    //
    //  GETTERS AND SETTERS FOR THE DOORS
    //
    // =====================================================

        // ---------------------
        //   GETTERS
        // ---------------------
    /** Returns the north door of this room.
     * @return the north door
     */
    public Door getNorthDoor() { return northDoor; }

    /** Returns the south door of this room.
     * @return the south door
     */
    public Door getSouthDoor() { return southDoor; }

    /** Returns the east door of this room.
     * @return the east door
     */
    public Door getEastDoor() { return eastDoor; }

    /** Returns the west door of this room.
     * @return the west door
     */
    public Door getWestDoor() { return westDoor; }


        // --------------------------------
        //   SETTERS
        // --------------------------------

    /** Sets the north door for this room.
     * @param door the new north door
     */
    public void setNorthDoor(Door door) { this.northDoor = door; }

    /** Sets the south door for this room.
     * @param door the new south door
     */
    public void setSouthDoor(Door door) { this.southDoor = door; }

    /** Sets the east door for this room.
     * @param door the new east door
     */
    public void setEastDoor(Door door)  { this.eastDoor = door; }

    /** Sets the west door for this room.
     * @param door the new west door
     */
    public void setWestDoor(Door door)  { this.westDoor = door; }

// ==================================================================


    /**
     * Determines whether this room has been visited.
     *
     * @return true if the room has been visited; false otherwise
     */
    public boolean isVisited() { return visited; }

    /**
     * Updates the visited status of this room.
     *
     * @param visited true if the room has been visited; false otherwise
     */
    public void setVisited(boolean visited) { this.visited = visited; }



    /**
     * Returns the number of available doors associated with this room.
     *
     * @return the number of doors
     */
    public int getDoors() { return doors; }

    /**
     * Determines whether the room has a left path available.
     *
     * @return true if a left path exists; false otherwise
     */
    public boolean canGoLeft() { return hasLeft; }

    /**
     * Determines whether the room has a right path available.
     *
     * @return true if a right path exists; false otherwise
     */
    public boolean canGoRight() { return hasRight; }

    /**
     * Determines whether the room has a center path available.
     *
     * @return true if a center path exists; false otherwise
     */
    public boolean canGoCenter() { return hasCenter; }



    /**
     * Returns a string representation of the room including
     * door lock states and visitation status.
     *
     * @return a string describing the room
     */
    @Override
    public String toString() {
        return "Room{ north=" + northDoor.isLocked() +
                ", east=" + eastDoor.isLocked() +
                ", south=" + southDoor.isLocked() +
                ", west=" + westDoor.isLocked() +
                ", visited=" + visited + " }";
    }
}