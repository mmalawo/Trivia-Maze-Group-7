package model;

public class Room {
    private Door northDoor;
    private Door southDoor;
    private Door eastDoor;
    private Door westDoor;
    private boolean visited;
    private int doors;
    private boolean hasLeft;
    private boolean hasRight;
    private boolean hasCenter;

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

    public Door getNorthDoor() { return northDoor; }
    public Door getSouthDoor() { return southDoor; }
    public Door getEastDoor() { return eastDoor; }
    public Door getWestDoor() { return westDoor; }

    public boolean isVisited() { return visited; }
    public void setVisited(boolean visited) { this.visited = visited; }

    public int getDoors() { return doors; }
    public boolean canGoLeft() { return hasLeft; }
    public boolean canGoRight() { return hasRight; }
    public boolean canGoCenter() { return hasCenter; }
}