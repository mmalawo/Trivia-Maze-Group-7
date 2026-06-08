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
        //testMaze();
    }
    public static JFrame window;

    public static JPanel currentView;
    public static JPanel previousView;
    public static Stack<JPanel> panelHistory = new Stack<>();

    // Views
    public static GameMenuView menuView;
    public static SoundManager soundManager;
    public static SettingsView settingsView;
    public static MazeView mazeView;
    public static PlayerSetupView setupView;
    public static Maze maze;

    // Models
    public static Player player;

    // Controls
    public static GameController gameController;


    public static JMenuBar menuBar;

    public static InstructionsView instructionsView;

    public static void startApplication() {

        // Initialize window
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        // window.setUndecorated(true); // For Fullscreen mode

        window.setUndecorated(false);
        window.setExtendedState(JFrame.NORMAL);
        window.setSize(1280, 720);
        window.setTitle("Trivia Maze - Main Menu");

        soundManager = new SoundManager();
        String[] songs = {
                //"src/sounds/Boing.wav",
                "src/sounds/HELL IN HEAVEN.wav",
                //"src/sounds/Knock.wav",
                "src/sounds/we makin it outta unova with this one.wav"
        };

        soundManager.loadPlaylist(songs);

        // Show menuView
        menuView = new GameMenuView();

        settingsView = new SettingsView();
        setupView = new PlayerSetupView();
        //menuBar = menuView.createMenuBar();
        menuBar = createMenuBar();
        window.setJMenuBar(menuBar);
        instructionsView = new InstructionsView();

        startNewGame();

        currentView = menuView;
        previousView = menuView;

        gameController = new GameController(menuView, player);
        new MenuController(menuView, settingsView);
        new PlayerController(setupView);
        new SettingsController(settingsView, soundManager);

        switchView(menuView);

        window.pack(); // Causes this window to be sized to fit preferred size in gamepanel
        window.setLocationRelativeTo(null); // Puts it at the center
        window.setVisible(true);


    }

    public static void switchView(JPanel panel) {
        if(currentView != null) {
            panelHistory.push(currentView);
        }
        currentView = panel;

        window.setContentPane(panel);
//        window.getContentPane().removeAll();
//        window.getContentPane().add(panel);

        window.revalidate();
        window.repaint();
    }
    public static void goBack() {
        if(!panelHistory.isEmpty()) {
            currentView = panelHistory.pop();
            window.setContentPane(currentView);
            window.revalidate();
            window.repaint();
        }
    }



    // ----------------------------------------------------------------------
    // THIS CODE IS FOR A GAME BAR THAT GOES ON TOP OF THE SCREEN
    // ----------------------------------------------------------------------



    public static JMenuBar createMenuBar() {

        // The actual bar at the top
        JMenuBar menuBar = new JMenuBar();

        // "Game" drop down menu
        JMenu gameMenu  = new JMenu("Game");

        // "Help" drop down menu
        JMenu help = new JMenu("Help");

        // _____________________________________________________
        // Options you can click in the dropdown menu of Game.
        // _____________________________________________________
        JMenuItem itemRestartGame = new JMenuItem("Main Menu");
        // Shortcut to restart game with keyboard
        itemRestartGame.setAccelerator(KeyStroke.getKeyStroke("control R"));

        JMenuItem itemSaveGame = new JMenuItem("Save Game");
        itemSaveGame.setAccelerator(KeyStroke.getKeyStroke("control S"));

        JMenuItem itemLoadGame = new JMenuItem("Load Game");
        itemLoadGame.setAccelerator(KeyStroke.getKeyStroke("control L"));

        JMenuItem itemExitGame = new JMenuItem("Exit");
        itemExitGame.setAccelerator(KeyStroke.getKeyStroke("control E"));

        JMenuItem itemSettings = new JMenuItem("Settings");
        // CAN ADD A SHORTCUT TO SETTINGS HERE IF WE WANT

        JMenuItem itemLeaderboard = new JMenuItem("Leaderboard");

        JMenuItem itemLeaderboardTest = new JMenuItem("Leaderboard Test");
        JMenuItem itemAddFakeScore = new JMenuItem("Add Fake Score");

        gameMenu.add(itemRestartGame);
        gameMenu.addSeparator();
        gameMenu.add(itemSaveGame);
        gameMenu.addSeparator();
        gameMenu.add(itemLoadGame);
        gameMenu.addSeparator();
        gameMenu.add(itemExitGame);
        gameMenu.addSeparator();
        gameMenu.add(itemSettings);
        gameMenu.addSeparator();
        gameMenu.add(itemLeaderboard);
        gameMenu.addSeparator();
        gameMenu.add(itemLeaderboardTest);
        gameMenu.addSeparator();
        gameMenu.add(itemAddFakeScore);

        menuBar.add(gameMenu);

        // CHANGE THIS TO BE THE FRAME OR PANEL OF THE GAME VIEW
        //window.setJMenuBar(menuBar);


        JMenuItem itemAbout = new JMenuItem("About");

        JMenuItem itemInstructions = new JMenuItem("How To Play");

        help.add(itemAbout);
        help.add(itemInstructions);

        menuBar.add(help);

        itemExitGame.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to exit?", "Exit",
                    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });


        itemSettings.addActionListener(e -> {
            MainGUI.switchView(MainGUI.settingsView);


        });

        itemRestartGame.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to go back? Progress will not save.",
                    "Main Menu",
                    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION) {
                MainGUI.startNewGame();
                MainGUI.switchView(MainGUI.menuView);
            }

        });

        itemSaveGame.addActionListener(e -> {
            MainGUI.saveGame();
            JOptionPane.showMessageDialog(window, "Game saved!");
        });

        itemLoadGame.addActionListener(e -> {
            boolean loaded = MainGUI.loadGame();

            if (loaded) {
                JOptionPane.showMessageDialog(window, "Game loaded!");
            } else {
                JOptionPane.showMessageDialog(window, "No saved game found.");
            }
        });

        itemLeaderboard.addActionListener(e -> {
            LeaderboardView.showLeaderboard();
        });

        itemLeaderboardTest.addActionListener(e -> {
            LeaderboardView.showLeaderboard();
        });

        itemAddFakeScore.addActionListener(e -> {
            Player fakePlayer = new Player();

            fakePlayer.setName("Test Player");
            fakePlayer.setRecordTime(99.9);
            fakePlayer.setCorrectScore(5);
            fakePlayer.setIncorrectScore(2);

            LeaderboardDAO dao = new LeaderboardDAO();
            dao.saveScore(fakePlayer);

            JOptionPane.showMessageDialog(
                    window,
                    "Fake leaderboard score added!",
                    "Test",
                    JOptionPane.INFORMATION_MESSAGE
            );

            LeaderboardView.showLeaderboard();
        });


        // HELP ---> DROP DOWN ACTION LISTENERS
        itemAbout.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    window,
                    "About the game:\n\n" +
                            "Created by: Angelina Christianson, Makani Malawo, and Tifanie Ngo\n" +
                            "Course: TCSS 360\n" +
                            "Version: 2.7\n\n" +
                            "This game is a garden style trivia maze game where you go through\ndoors by answering trivia questions correctly.\n",
                    "About",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        itemInstructions.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    window,
                    "How to Play:\n\n" +
                            "Use the buttons on the right side of the screen to traverse through the maze.\n" +
                            "You have 3 tries per door to get it right. If you fail all 3 tries, the door locks permanently.\n" +
                            "Use the hint button if you can't find the exit!\n\n",
                    "Instructions",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });

        return menuBar;
    }

    public static void testMaze() {
        Maze thisMaze = GenerateMaze.generateMaze();


        System.out.println("Current Room: " + player.getCurrentRoom());
        System.out.println("Maze Columns: " + thisMaze.getCols()); // 5
        System.out.println("Maze Rows: " + thisMaze.getRows()); // 5

       // System.out.println(thisMaze.getEntrance());
       // System.out.println(thisMaze.getExit());

        for(int r = 0; r < thisMaze.getRows(); r++) {
            for(int c = 0; c < thisMaze.getCols(); c++) {
                System.out.println(thisMaze.getRoom(r,c));
            }
        }
        System.out.println();
    }

    public static void saveGame() {
        Memento save = new Memento(player, maze);
        SaveManager.saveGame(save);
    }

    public static boolean loadGame() {
        Memento loaded = SaveManager.loadGame();

        if (loaded != null) {
            player = loaded.getPlayer();
            maze = loaded.getMaze();

            mazeView = new MazeView(maze);
            panelHistory.clear();
            switchView(mazeView);

            System.out.println("Loaded player: " + player.getName());
            return true;
        }
        
        return false;
    }

    public static void startNewGame() {
        maze = GenerateMaze.generateMaze();

        player = new Player();
        player.setCurrentRoom(maze.getEntrance());

        mazeView = new MazeView(maze);

        panelHistory.clear();

        System.out.println("Started a new game.");
    }
}