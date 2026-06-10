package view;

import controller.AppController;

import javax.swing.*;

/**
 * Provides the main entry point and application window shell.
 *
 * <p>This class creates the main Swing window and initializes the
 * application controller. It does not directly manage model, view,
 * or controller state beyond launching the application.</p>
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
     * Creates and displays the main application window.
     *
     * <p>This method configures the main frame, initializes the application
     * controller, centers the window, and makes it visible.</p>
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
