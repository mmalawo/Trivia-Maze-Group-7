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

    public static JFrame window;

    public static void startApplication() {

        // Initialize window
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setUndecorated(true); // For Fullscreen mode
        //window.setUndecorated(false); // For Fullscreen mode

        window.setTitle("Trivia Maze - Main Menu");

        // Show menuView
        GameMenuView menuView = new GameMenuView();
        Player player = new Player();
        GameController controller = new GameController(menuView, player);
        MenuController menuController = new MenuController(menuView);

        window.add(menuView);

        window.pack(); // Causes this window to be sized to fit preferred size in gamepanel
        window.setLocationRelativeTo(null); // Puts it at the center
        window.setVisible(true);


    }
}
