package model;

import java.io.Serializable;

/**
 * Represents a snapshot of the game's state for use with the
 * Memento design pattern.
 *
 * <p>A memento stores the current player and maze objects so that
 * the game can be saved and later restored to the same state.</p>
 */
public class Memento implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Player player;
    private final Maze maze;

    /**
     * Constructs a memento containing the specified player and maze state.
     *
     * @param player the player state to save
     * @param maze the maze state to save
     */
    public Memento(Player player, Maze maze) {
        this.player = player;
        this.maze = maze;
    }

    /**
     * Returns the saved player state.
     *
     * @return the saved player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the saved maze state.
     *
     * @return the saved maze
     */
    public Maze getMaze() {
        return maze;
    }
}
