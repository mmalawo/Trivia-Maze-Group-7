package model;

import java.io.*;

public class SaveManager {
    private static final String SAVE_FILE = "savegame.dat";

    public static void saveGame(Memento memento) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(memento);
            System.out.println("Game saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
}