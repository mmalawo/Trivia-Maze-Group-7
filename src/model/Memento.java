package model;

import java.io.Serializable;

public class Memento implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Player player;
    private final Maze maze;

    public Memento(Player player, Maze maze) {
        this.player = player;
        this.maze = maze;
    }

    public Player getPlayer() {
        return player;
    }

    public Maze getMaze() {
        return maze;
    }
}
