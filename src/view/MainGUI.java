package view;

import controller.AppController;

import javax.swing.*;

/**
 * MainGUI is now only the application entry point / window shell.
 * It does not store global model, view, or controller state.
 */
public class MainGUI {

    /**
     * Launches the application on the Swing Event Dispatch Thread (EDT).
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::startApplication);
    }

    /**
     * Creates and configures the main application window, initializes
     * the application controller, and makes the window visible.
     */
    private static void startApplication() {
        JFrame window = new JFrame("Garden Trivia Maze - Version 2.7");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setUndecorated(false);
        window.setExtendedState(JFrame.NORMAL);
        window.setSize(1280, 720);

        new AppController(window);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
}
