package controller;

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import model.SoundManager;
import view.*;

/**
 * MenuController handles user interactions on the main game menu.
 * It listens for button clicks on the GameMenuView and delegates
 * actions such as starting a new game, opening settings, or exiting.
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
     * Registers action listeners for the play, exit, and settings buttons
     * on the main menu view.
     */
    private void addListeners() {
        menu.addPlayListener(e -> {
            MainGUI.startNewGame();
            MainGUI.switchView(MainGUI.setupView);
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