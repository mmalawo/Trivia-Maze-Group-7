package controller;

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import view.*;
import model.*;

/**
 * SettingsController handles user interactions on the SettingsView.
 * It manages fullscreen toggling, dark mode, volume adjustments, and
 * navigation back to the previous view.
 */
public class SettingsController {

    /** The settings view this controller manages. */
    private SettingsView settingsMenu;

    /** The sound manager used to adjust game volume. */
    private SoundManager soundManager;

    /** The width of the user's screen. */
    private final double screenWidth;

    /** The height of the user's screen. */
    private final double screenHeight;

    /**
     * Constructs a SettingsController and registers listeners on the given view.
     *
     * @param theSettingsMenu the settings view
     * @param theSoundManager the sound manager for volume control
     */
    public SettingsController(SettingsView theSettingsMenu, SoundManager theSoundManager) {
        this.settingsMenu = theSettingsMenu;
        this.soundManager = theSoundManager;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.screenWidth = screenSize.getWidth();
        this.screenHeight = screenSize.getHeight();

        addListeners();
    }

    /**
     * Registers action listeners for fullscreen, dark mode, volume, and back buttons.
     */
    private void addListeners() {

        /**
         * Toggles fullscreen mode on or off based on the checkbox state.
         */
        settingsMenu.addFullscreenListener(e -> {
            boolean fullscreen = settingsMenu.isFullscreenSelected();

            MainGUI.getWindow().dispose();

            if (fullscreen) {
                MainGUI.getWindow().setUndecorated(true);
                MainGUI.getWindow().setExtendedState(JFrame.MAXIMIZED_BOTH);
            } else {
                MainGUI.getWindow().setPreferredSize(new Dimension((int) screenWidth, (int) screenHeight));
                MainGUI.getWindow().setUndecorated(false);
                MainGUI.getWindow().setExtendedState(JFrame.NORMAL);
                MainGUI.getWindow().pack();
                MainGUI.getWindow().setLocationRelativeTo(null);
            }

            MainGUI.getWindow().setVisible(true);
        });

        /**
         * Toggles dark mode on or off based on the checkbox state.
         */
        settingsMenu.addDarkModeListener(e -> {
            settingsMenu.setDarkMode(settingsMenu.isDarkModeSelected());
        });

        /**
         * Updates the game volume based on the slider value.
         */
        settingsMenu.addVolumeListener(e -> {
            int volume = settingsMenu.getVolumeValue();
            soundManager.setVolume(volume);
            System.out.println("Volume: " + volume);
        });

        /**
         * Navigates back to the previous view.
         */
        settingsMenu.addBackListener(e -> {
            MainGUI.goBack();
        });
    }
}