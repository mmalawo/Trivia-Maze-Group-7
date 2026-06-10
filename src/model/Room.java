package model;

import java.io.Serializable;

/**
 * Represents a room within the maze.
 *
 * <p>Each room contains doors in the four cardinal directions, tracks
 * whether it has been visited, and stores path-related information used
 * by the maze.</p>
 */
public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private Door myNorthDoor;
    private Door mySouthDoor;
    private Door myEastDoor;
    private Door myWestDoor;
    private boolean myVisited;

    /**
     * Constructs a room with four doors initialized in their
     * default state and no visitation or path flags set.
     */
    public Room() {
        this.myNorthDoor = new Door();
        this.mySouthDoor = new Door();
        this.myEastDoor = new Door();
        this.myWestDoor = new Door();
        this.myVisited = false;
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
    public Door getNorthDoor() { return myNorthDoor; }

    /** Returns the south door of this room.
     * @return the south door
     */
    public Door getSouthDoor() { return mySouthDoor; }

    /** Returns the east door of this room.
     * @return the east door
     */
    public Door getEastDoor() { return myEastDoor; }

    /** Returns the west door of this room.
     * @return the west door
     */
    public Door getWestDoor() { return myWestDoor; }


        // --------------------------------
        //   SETTERS
        // --------------------------------

    /** Sets the north door for this room.
     * @param theDoor the new north door
     */
    public void setNorthDoor(final Door theDoor) { this.myNorthDoor = theDoor; }

    /** Sets the south door for this room.
     * @param theDoor the new south door
     */
    public void setSouthDoor(final Door theDoor) { this.mySouthDoor = theDoor; }

    /** Sets the east door for this room.
     * @param theDoor the new east door
     */
    public void setEastDoor(final Door theDoor)  { this.myEastDoor = theDoor; }

    /** Sets the west door for this room.
     * @param theDoor the new west door
     */
    public void setWestDoor(final Door theDoor)  { this.myWestDoor = theDoor; }

// ==================================================================


    /**
     * Returns whether this room has been visited.
     *
     * @return {@code true} if the room has been visited;
     *         {@code false} otherwise
     */
    public boolean isVisited() { return myVisited; }

    /**
     * Sets whether this room has been visited.
     *
     * @param theVisited {@code true} if the room has been visited;
     *                   {@code false} otherwise
     */
    public void setVisited(final boolean theVisited) { this.myVisited = theVisited; }

    /**
     * Returns a string representation of the room including
     * door lock states and visitation status.
     *
     * @return a string describing the room
     */
    @Override
    public String toString() {
        return "Room{ north=" + myNorthDoor.isLocked() +
                ", east=" + myEastDoor.isLocked() +
                ", south=" + mySouthDoor.isLocked() +
                ", west=" + myWestDoor.isLocked() +
                ", visited=" + myVisited + " }";
    }
}