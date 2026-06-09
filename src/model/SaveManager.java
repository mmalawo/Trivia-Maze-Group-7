package model;

import java.io.*;

/**
 * Manages saving and loading game state using Java serialization.
 */
public class SaveManager {

    /** The file path for the save game file. */
    private static final String SAVE_FILE = "savegame.dat";

    /**
     * Saves the given Memento object to disk.
     *
     * @param theMemento the memento containing the game state to save
     */
    public static void saveGame(Memento theMemento) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(theMemento);
            System.out.println("Game saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a Memento object from disk.
     *
     * @return the loaded Memento, or null if no save file exists or loading fails
     */
    public static Memento loadGame() {
        File file = new File(SAVE_FILE);

        if (!file.exists()) {
            System.out.println("No save file found.");
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(SAVE_FILE))) {
            System.out.println("Game loaded!");
            return (Memento) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks whether a save file exists on disk.
     *
     * @return true if a save file exists, false otherwise
     */
    public static boolean saveFileExists() {
        return new File(SAVE_FILE).exists();
    }

    /**
     * Deletes the save file from disk.
     * Called when starting a new game so the Resume button is hidden.
     */
    public static void deleteSaveFile() {
        File file = new File(SAVE_FILE);
        if (file.exists()) {
            file.delete();
            System.out.println("Save file deleted.");
        }
    }
}