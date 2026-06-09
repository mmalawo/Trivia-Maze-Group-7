package controller;

import model.SoundManager;
import view.SettingsView;

import javax.swing.*;
import java.awt.*;

/**
 * Controls interactions between the settings view and the application.
 *
 * <p>This controller manages fullscreen mode, dark mode,
 * audio volume adjustments, and navigation back to the
 * previous screen.</p>
 */
public class SettingsController {
    private final AppController myApp;
    private final SettingsView mySettingsMenu;
    private final SoundManager mySoundManager;
    private final double myScreenWidth;
    private final double myScreenHeight;

    /**
     * Constructs a controller for the settings screen and
     * registers all required event listeners.
     *
     * @param theApp the main application controller
     * @param theSettingsMenu the settings view managed by this controller
     * @param theSoundManager the sound manager used for volume control
     */
    public SettingsController(final AppController theApp,
                              final SettingsView theSettingsMenu,
                              final SoundManager theSoundManager) {
        myApp = theApp;
        mySettingsMenu = theSettingsMenu;
        mySoundManager = theSoundManager;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        myScreenWidth = screenSize.getWidth();
        myScreenHeight = screenSize.getHeight();

        addListeners();
    }

    /**
     * Registers all event listeners for the settings view.
     *
     * <p>Listeners handle fullscreen mode, dark mode,
     * volume changes, and navigation back to the
     * previous screen.</p>
     */
    private void addListeners() {
        mySettingsMenu.addFullscreenListener(e -> {
            boolean fullscreen = mySettingsMenu.isFullscreenSelected();
            JFrame window = myApp.getWindow();

            window.dispose();

            if (fullscreen) {
                window.setUndecorated(true);
                window.setExtendedState(JFrame.MAXIMIZED_BOTH);
            } else {
                window.setPreferredSize(new Dimension((int) myScreenWidth, (int) myScreenHeight));
                window.setUndecorated(false);
                window.setExtendedState(JFrame.NORMAL);
                window.pack();
                window.setLocationRelativeTo(null);
            }

            window.setVisible(true);
        });

        mySettingsMenu.addDarkModeListener(e -> {
            boolean darkModeSelected = mySettingsMenu.isDarkModeSelected();
            mySettingsMenu.setDarkMode(darkModeSelected);
            myApp.applyDarkMode(darkModeSelected);
        });

        mySettingsMenu.addVolumeListener(e -> {
            int volume = mySettingsMenu.getVolumeValue();
            mySoundManager.setVolume(volume);
            System.out.println("Volume: " + volume);
        });

        mySettingsMenu.addBackListener(e -> myApp.goBack());
    }
}
