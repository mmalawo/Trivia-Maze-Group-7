package model;

import java.io.Serializable;

public class Maze implements Serializable {
    private static final long serialVersionUID = 1L;

    private Room[][] rooms;
    private int rows;
    private int cols;
    private Room entrance;
    private Room exitRoom;
    private Door exitDoor;
    private String exitDoorDirection;

    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.rooms = new Room[rows][cols];
    }

    public Room[][] getRooms() {
        return rooms;
    }

    public void setRooms(int r, int c, Room room) {
        rooms[r][c] = room;
    }

    public Room getRoom(int r, int c) {
        return rooms[r][c];
    }

    public Room getEntrance() {
        return entrance;
    }

    public void setEntrance(Room entrance) {
        this.entrance = entrance;
    }

    public Room getExit() {
        return exitRoom;
    }

    public void setExitRoom(Room exitRoom) {
        this.exitRoom = exitRoom;
    }

    public Door getExitDoor() {
        return exitDoor;
    }

    public void setExitDoor(Door exitDoor) {
        this.exitDoor = exitDoor;
    }

    public String getExitDoorDirection() {
        return exitDoorDirection;
    }

    public void setExitDoorDirection(String exitDoorDirection) {
        this.exitDoorDirection = exitDoorDirection;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

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