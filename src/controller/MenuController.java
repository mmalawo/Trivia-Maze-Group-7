package controller;

import java.util.*;
import java.io.*;
import javax.swing.*;   // for JFrame
import java.awt.*;
import java.awt.event.*;
import view.GameMenuView;
import view.MainGUI;
import view.SettingsView;
import view.*;

public class MenuController {// Action Listener and events

    private GameMenuView menu;

    public MenuController(GameMenuView theMenu) {
        this.menu = theMenu;
        addListeners();
    }

    private void addListeners() {
        menu.addExitListener(e -> {
            System.out.println("Application Closed.");
            System.exit(0);
        });
        menu.addSettingsListener(e -> {
            MainGUI.window.getContentPane().removeAll();
            MainGUI.window.add(SettingsView.create());
            MainGUI.window.revalidate();
            MainGUI.window.repaint();
        });



        menu.addSettingsListener(e -> {
            MainGUI.window.getContentPane().removeAll();
            SettingsView settingsView = new SettingsView();
            MainGUI.window.add(settingsView.create());
            MainGUI.window.revalidate();
            MainGUI.window.repaint();


        });

    }
}

