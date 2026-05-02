public class Maze {
    private Room[][] rooms;
    private int rows;
    private int cols;
    private Room entrance;
    private Room exit;

    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.rooms = new Room[rows][cols];
    }

    public Room[][] getRooms() {
        return rooms;
    }

    public Room getEntrance() {
        return entrance;
    }

    public void setEntrance(Room entrance) {
        this.entrance = entrance;
    }

    public Room getExit() {
        return exit;
    }

    public void setExit(Room exit) {
        this.exit = exit;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}