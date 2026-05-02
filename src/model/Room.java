
public class Room {
    private int doors;
    private boolean hasLeft;
    private boolean hasRight;
    private boolean hasCenter;

    public Room() {
        this.doors = 0;
        this.hasLeft = false;
        this.hasRight = false;
        this.hasCenter = false;
    }

    public int getDoors() {
        return doors;
    }

    public boolean canGoLeft() {
        return hasLeft;
    }

    public boolean canGoRight() {
        return hasRight;
    }

    public boolean canGoCenter() {
        return hasCenter;
    }
}