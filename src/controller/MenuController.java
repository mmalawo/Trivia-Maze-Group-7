package controller;

import java.util.*;
import java.io.*;
import javax.swing.*;   // for JFrame
import java.awt.*;
import java.awt.event.*;

import model.SoundManager;
import view.*;

public class MenuController {// Action Listener and events

    private GameMenuView menu;
    private SettingsView settingsView;

    public MenuController(GameMenuView theMenu, SettingsView theSettingsView) {
        this.menu = theMenu;
        this.settingsView = theSettingsView;

        addListeners();
    }

    private void addListeners() {
        menu.addPlayListener(e -> {
            MainGUI.switchView(MainGUI.setupView);

        });

        menu.addExitListener(e -> {
            System.out.println("Application Closed.");
            System.exit(0);
        });

        menu.addSettingsListener(e -> {
            MainGUI.switchView(MainGUI.settingsView);

            //MainGUI.window.getContentPane().removeAll();

            // SettingsView settingsView = new SettingsView();
            // JPanel settingsPanel = settingsView.create();
            // SettingsController settingsController = new SettingsController(settingsView, soundManager);

            //MainGUI.window.add(settingsPanel);

        });



    }
}

