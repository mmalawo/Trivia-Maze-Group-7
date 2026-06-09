package controller;

import view.GameMenuView;

import javax.swing.*;

/**
 * MenuController handles user interactions on the main game menu.
 * It delegates application navigation to AppController instead of using MainGUI globals.
 */
public class MenuController {
    private final AppController myApp;
    private final GameMenuView myMenu;

    /**
     * Constructs a MenuController and registers listeners
     * for the main menu view.
     *
     * @param theApp the main application controller
     * @param theMenu the game menu view
     */
    public MenuController(final AppController theApp, final GameMenuView theMenu) {
        myApp = theApp;
        myMenu = theMenu;
        addListeners();
    }

    /**
     * Registers all event listeners for the main menu.
     *
     * <p>Listeners handle starting a new game, resuming a saved
     * game, opening the settings screen, and exiting the application.</p>
     */
    private void addListeners() {
        myMenu.addPlayListener(e -> myApp.showPlayerSetup());

        myMenu.addResumeListener(e -> {
            if (!myApp.loadGame()) {
                JOptionPane.showMessageDialog(
                        myApp.getWindow(),
                        "No saved game found.",
                        "Resume",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        myMenu.addExitListener(e -> {
            System.out.println("Application Closed.");
            System.exit(0);
        });

        myMenu.addSettingsListener(e -> myApp.showSettings());
    }
}
