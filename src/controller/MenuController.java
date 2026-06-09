package controller;

import javax.swing.*;
import java.awt.event.*;

import model.SoundManager;
import view.*;

/**
 * MenuController handles user interactions on the main game menu.
 * It listens for button clicks on the GameMenuView and delegates
 * actions such as starting a new game, resuming, opening settings, or exiting.
 */
public class MenuController {

    /** The main menu view this controller manages. */
    private GameMenuView menu;

    /** The settings view to switch to when settings is clicked. */
    private SettingsView settingsView;

    /**
     * Constructs a MenuController and registers listeners on the given views.
     *
     * @param theMenu         the main game menu view
     * @param theSettingsView the settings view
     */
    public MenuController(GameMenuView theMenu, SettingsView theSettingsView) {
        this.menu = theMenu;
        this.settingsView = theSettingsView;
        addListeners();
    }

    /**
     * Registers action listeners for the play, resume, exit, and settings buttons.
     */
    private void addListeners() {

        menu.addPlayListener(e -> {
            MainGUI.startNewGame();
            MainGUI.switchView(MainGUI.setupView);
        });

        menu.addResumeListener(e -> {
            boolean loaded = MainGUI.loadGame();
            if (loaded) {
                // Resume the timer display loop from where it left off
                new javax.swing.Timer(1000, evt -> {
                    double time = MainGUI.player.elapsedTime();
                    MainGUI.mazeView.updateTimer(time);
                }).start();
            } else {
                JOptionPane.showMessageDialog(
                        MainGUI.getWindow(),
                        "No saved game found.",
                        "Resume",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        });

        menu.addExitListener(e -> {
            System.out.println("Application Closed.");
            System.exit(0);
        });

        menu.addSettingsListener(e -> {
            MainGUI.switchView(MainGUI.settingsView);
        });
    }
}