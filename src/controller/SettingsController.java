package controller;

import model.SoundManager;
import view.SettingsView;

import javax.swing.*;
import java.awt.*;

/**
 * Controls user interactions on the settings screen.
 *
 * <p>This controller connects the settings view to application-level actions,
 * including fullscreen mode, dark mode, audio volume control, and returning
 * to the previous screen.</p>
 */
public class SettingsController {
    private final AppController myApp;
    private final SettingsView mySettingsMenu;
    private final SoundManager mySoundManager;
    private final double myScreenWidth;
    private final double myScreenHeight;

    /**
     * Constructs a settings controller and registers listeners for the settings view.
     *
     * <p>This also stores the current screen dimensions so the application window
     * can be restored when fullscreen mode is disabled.</p>
     *
     * @param theApp the application controller used for navigation and window access
     * @param theSettingsMenu the settings view controlled by this controller
     * @param theSoundManager the sound manager used for volume adjustments
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
        });

        mySettingsMenu.addBackListener(e -> myApp.goBack());
    }
}
