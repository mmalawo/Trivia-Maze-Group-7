package controller;

import java.util.*;
import java.io.*;
import javax.swing.*;   // for JFrame
import java.awt.*;
import java.awt.event.*;

import com.sun.tools.javac.Main;
import view.*;
import model.*;

public class SettingsController {

    private SettingsView settingsMenu;
    private SoundManager soundManager;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    double screenWidth = screenSize.getWidth();
    double screenHeight = screenSize.getHeight();

    public SettingsController(SettingsView theSettingsMenu, SoundManager theSoundManager) {
        this.settingsMenu = theSettingsMenu;
        this.soundManager = theSoundManager;
        addListeners();
    }

    private void addListeners() {
        settingsMenu.addFullscreenListener(e -> {
            boolean fullscreen = settingsMenu.isFullscreenSelected();

            MainGUI.getWindow().dispose();

            if (fullscreen) {
                MainGUI.getWindow().setUndecorated(true);
                MainGUI.getWindow().setExtendedState(JFrame.MAXIMIZED_BOTH);
            } else {
                MainGUI.getWindow().setPreferredSize(new Dimension((int)screenWidth, (int)screenHeight));
                MainGUI.getWindow().setUndecorated(false);
                MainGUI.getWindow().setExtendedState(JFrame.NORMAL);
                MainGUI.getWindow().pack();
                MainGUI.getWindow().setLocationRelativeTo(null);
            }
            MainGUI.getWindow().setVisible(true);
        });

        settingsMenu.addDarkModeListener(e -> {
            settingsMenu.setDarkMode(settingsMenu.isDarkModeSelected());
        });

        settingsMenu.addVolumeListener(e -> {
            int volume = settingsMenu.getVolumeValue();
            soundManager.setVolume(volume);
            System.out.println("Volume: " + volume);
        });

        settingsMenu.addBackListener(e -> {
            MainGUI.goBack();
            //MainGUI.switchView(MainGUI.previousView);
//            JFrame frame = MainGUI.window;
//            frame.getContentPane().removeAll();
//
//            frame.getContentPane().add(MainGUI.menuView);
//
//            frame.revalidate();
//            frame.repaint();
        });


    }
}

