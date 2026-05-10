package controller;

import java.util.*;
import java.io.*;
import javax.swing.*;   // for JFrame
import java.awt.*;
import java.awt.event.*;
import view.*;

public class MenuController {// Action Listener and events

    private GameMenuView menu;
    private SettingsView settingsView;
    private JPanel settingsPanel;

    public MenuController(GameMenuView theMenu, SettingsView theSettingsView, JPanel theSettingsPanel) {
        this.menu = theMenu;
        this.settingsView = theSettingsView;
        this.settingsPanel = theSettingsPanel;

        addListeners();
    }

    private void addListeners() {
        menu.addPlayListener(e -> {
            MainGUI.window.getContentPane().removeAll();

            PlayerSetupView setupView = new PlayerSetupView();
            JPanel playerPanel = setupView.getPlayerPanel();
            //PlayerController playerController = new PlayerController(setupView);

            MainGUI.window.add(playerPanel);
            MainGUI.window.revalidate();
            MainGUI.window.repaint();
        });

        menu.addExitListener(e -> {
            System.out.println("Application Closed.");
            System.exit(0);
        });

        menu.addSettingsListener(e -> {
            MainGUI.window.getContentPane().removeAll();

//            SettingsView settingsView = new SettingsView();
//            JPanel settingsPanel = settingsView.create();
            //SettingsController settingsController = new SettingsController(settingsView);

            MainGUI.window.add(MainGUI.settingsPanel);
            MainGUI.window.revalidate();
            MainGUI.window.repaint();
        });

    }
}

