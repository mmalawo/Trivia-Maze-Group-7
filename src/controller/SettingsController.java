package controller;

import model.SoundManager;
import view.SettingsView;

import javax.swing.*;
import java.awt.*;

/**
 * SettingsController handles user interactions on the SettingsView.
 */
public class SettingsController {
    private final AppController myApp;
    private final SettingsView mySettingsMenu;
    private final SoundManager mySoundManager;
    private final double myScreenWidth;
    private final double myScreenHeight;

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

        mySettingsMenu.addDarkModeListener(e ->
                mySettingsMenu.setDarkMode(mySettingsMenu.isDarkModeSelected()));

        mySettingsMenu.addVolumeListener(e -> {
            int volume = mySettingsMenu.getVolumeValue();
            mySoundManager.setVolume(volume);
            System.out.println("Volume: " + volume);
        });

        mySettingsMenu.addBackListener(e -> myApp.goBack());
    }
}
