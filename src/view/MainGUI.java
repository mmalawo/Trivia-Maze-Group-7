package view;

import controller.*;
import model.*;
import view.*;

import java.util.*;
import java.io.*;
import javax.swing.*;   // for JFrame
import java.awt.*;
import java.awt.event.*;  // Action Listener and events


public class MainGUI {

    public static void main(String args[]) {
        System.out.println("Starting application...");
        startApplication();
    }
    public static JFrame window;

    public static GameMenuView menuView;
    public static SoundManager soundManager;
    public static SettingsView settingsView;

    public static JPanel settingsPanel;
    public static Player player;

    public static PlayerSetupView setupView;


    public static JMenuBar menuBar;

    public static void startApplication() {

        // Initialize window
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setUndecorated(true); // For Fullscreen mode

        window.setTitle("Trivia Maze - Main Menu");

        soundManager = new SoundManager();
        String[] songs = {
                //"src/sounds/Boing.wav",
                //"src/sounds/HELL IN HEAVEN.wav",
                //"src/sounds/Knock.wav",
                "src/sounds/we makin it outta unova with this one.wav"
        };

        soundManager.loadPlaylist(songs);

        // Show menuView
        menuView = new GameMenuView();
        player = new Player();
        settingsView = new SettingsView();
        settingsPanel = settingsView.create();
        setupView = new PlayerSetupView();
        menuBar = menuView.createMenuBar();
        window.setJMenuBar(menuBar);

        new GameController(menuView, player);
        new MenuController(menuView, settingsView, settingsPanel);
        new PlayerController(setupView);
        new SettingsController(settingsView, soundManager);



        window.add(menuView);

        window.pack(); // Causes this window to be sized to fit preferred size in gamepanel
        window.setLocationRelativeTo(null); // Puts it at the center
        window.setVisible(true);


    }
}
