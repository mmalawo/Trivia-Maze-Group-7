package controller;

import model.*;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

/**
 * AppController owns application-level navigation and the currently active model objects.
 * This replaces the old MainGUI public static globals so the program follows MVC more cleanly.
 */
public class AppController {
    private final JFrame myWindow;
    private final Stack<JPanel> myPanelHistory;

    private GameMenuView myMenuView;
    private SettingsView mySettingsView;
    private PlayerSetupView mySetupView;
    private InstructionsView myInstructionsView;
    private MazeView myMazeView;

    private SoundManager mySoundManager;
    private Player myPlayer;
    private Maze myMaze;
    private GameController myGameController;
    private Timer myTimerDisplayLoop;

    private boolean myHasSaved;

    public AppController(final JFrame theWindow) {
        myWindow = theWindow;
        myPanelHistory = new Stack<>();

        mySoundManager = new SoundManager();
        String[] songs = {
                "src/sounds/peaceful violin music.wav",
                "src/sounds/we makin it outta unova with this one.wav"
        };
        mySoundManager.loadPlaylist(songs);

        createViews();
        startNewGame();
        createControllers();

        switchView(myMenuView);
    }

    private void createViews() {
        myMenuView = new GameMenuView();
        mySettingsView = new SettingsView();
        mySetupView = new PlayerSetupView();
        myInstructionsView = new InstructionsView();

        myWindow.setJMenuBar(createMenuBar());
    }

    private void createControllers() {
        new MenuController(this, myMenuView);
        new PlayerController(this, mySetupView, myInstructionsView);
        new SettingsController(this, mySettingsView, mySoundManager);
    }

    public void switchView(final JPanel thePanel) {
        if (thePanel == myMenuView) {
            myMenuView.refreshResumeButton();
        }

        Container currentContent = myWindow.getContentPane();
        if (currentContent instanceof JPanel && currentContent != thePanel) {
            myPanelHistory.push((JPanel) currentContent);
        }

        myWindow.setContentPane(thePanel);
        myWindow.revalidate();
        myWindow.repaint();
        thePanel.requestFocusInWindow();
    }

    public void goBack() {
        if (!myPanelHistory.isEmpty()) {
            JPanel previousPanel = myPanelHistory.pop();
            myWindow.setContentPane(previousPanel);
            myWindow.revalidate();
            myWindow.repaint();
            previousPanel.requestFocusInWindow();
        }
    }

    public void startNewGame() {
        stopTimerDisplayLoop();

        myMaze = GenerateMaze.generateMaze();
        myPlayer = new Player();
        myPlayer.setCurrentRoom(myMaze.getEntrance());

        myGameController = new GameController(myMaze, myPlayer, myWindow, this);
        myMazeView = new MazeView(myMaze, myPlayer, myGameController);
        myGameController.setMazeView(myMazeView);

        myHasSaved = false;
        SaveManager.deleteSaveFile();
        myPanelHistory.clear();

        boolean darkModeSelected = mySettingsView != null && mySettingsView.isDarkModeSelected();
        myMazeView.setDarkMode(darkModeSelected);

        if (mySetupView != null) {
            mySetupView.reset(darkModeSelected);
        }

        System.out.println("Started a new game.");
    }

    public void showPlayerSetup() {
        startNewGame();
        switchView(mySetupView);
    }

    public void showSettings() {
        switchView(mySettingsView);
    }

    public void showInstructions() {
        myInstructionsView.setPlayerName(myPlayer.getName());
        switchView(myInstructionsView);
    }

    public void startGameplay() {
        myMazeView.setPlayerSprites(
                mySetupView.getCurrentFlapIcon(),
                mySetupView.getCurrentUnflapIcon()
        );
        myPlayer.startTimer();
        switchView(myMazeView);
        startTimerDisplayLoop();
    }

    public void returnToMainMenuAfterGame() {
        startNewGame();
        switchView(myMenuView);
    }

    public void returnToMainMenuFromMenuItem() {
        if (myHasSaved) {
            switchView(myMenuView);
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                myWindow,
                "Are you sure you want to go back? Any unsaved progress will be lost.",
                "Main Menu",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            startNewGame();
            switchView(myMenuView);
        }
    }

    public void saveGame() {
        if (myPlayer == null || myMaze == null) {
            return;
        }

        myPlayer.saveElapsedTime();
        SaveManager.saveGame(new Memento(myPlayer, myMaze));
        myHasSaved = true;
    }

    public boolean loadGame() {
        Memento loaded = SaveManager.loadGame();

        if (loaded == null) {
            return false;
        }

        stopTimerDisplayLoop();

        myPlayer = loaded.getPlayer();
        myMaze = loaded.getMaze();
        myPlayer.resumeTimer();

        myGameController = new GameController(myMaze, myPlayer, myWindow, this);
        myMazeView = new MazeView(myMaze, myPlayer, myGameController);
        myGameController.setMazeView(myMazeView);

        myPanelHistory.clear();
        switchView(myMazeView);
        startTimerDisplayLoop();

        System.out.println("Loaded player: " + myPlayer.getName());
        return true;
    }

    public void markGameNoLongerSaved() {
        myHasSaved = false;
    }

    public Player getPlayer() {
        return myPlayer;
    }

    public SettingsView getSettingsView() {
        return mySettingsView;
    }

    public PlayerSetupView getPlayerSetupView() {
        return mySetupView;
    }

    public void applyDarkMode(final boolean theDarkModeSelected) {
        myMenuView.setDarkMode(theDarkModeSelected);
        mySetupView.setDarkMode(theDarkModeSelected);
        if (myMazeView != null) {
            myMazeView.setDarkMode(theDarkModeSelected);
        }
    }

    public JFrame getWindow() {
        return myWindow;
    }

    private void startTimerDisplayLoop() {
        stopTimerDisplayLoop();

        myTimerDisplayLoop = new Timer(1000, e -> {
            if (myMazeView != null && myPlayer != null) {
                myMazeView.updateTimer(myPlayer.elapsedTime());
            }
        });
        myTimerDisplayLoop.start();
    }

    private void stopTimerDisplayLoop() {
        if (myTimerDisplayLoop != null) {
            myTimerDisplayLoop.stop();
            myTimerDisplayLoop = null;
        }
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem mainMenuItem = new JMenuItem("Main Menu");
        mainMenuItem.setAccelerator(KeyStroke.getKeyStroke("control R"));

        JMenuItem saveItem = new JMenuItem("Save Game");
        saveItem.setAccelerator(KeyStroke.getKeyStroke("control S"));

        JMenuItem loadItem = new JMenuItem("Load Game");
        loadItem.setAccelerator(KeyStroke.getKeyStroke("control L"));

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke("control E"));

        JMenuItem settingsItem = new JMenuItem("Settings");
        JMenuItem leaderboardItem = new JMenuItem("Leaderboard");

        gameMenu.add(mainMenuItem);
        gameMenu.addSeparator();
        gameMenu.add(saveItem);
        gameMenu.addSeparator();
        gameMenu.add(loadItem);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);
        gameMenu.addSeparator();
        gameMenu.add(settingsItem);
        gameMenu.addSeparator();
        gameMenu.add(leaderboardItem);

        JMenuItem aboutItem = new JMenuItem("About");
        JMenuItem instructionsItem = new JMenuItem("How To Play");

        helpMenu.add(aboutItem);
        helpMenu.add(instructionsItem);

        menuBar.add(gameMenu);
        menuBar.add(helpMenu);

        mainMenuItem.addActionListener(e -> returnToMainMenuFromMenuItem());

        saveItem.addActionListener(e -> {
            saveGame();
            JOptionPane.showMessageDialog(myWindow, "Game saved!");
        });

        loadItem.addActionListener(e -> {
            if (loadGame()) {
                JOptionPane.showMessageDialog(myWindow, "Game loaded!");
            } else {
                JOptionPane.showMessageDialog(myWindow, "No saved game found.");
            }
        });

        exitItem.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    myWindow,
                    "Are you sure you want to exit?",
                    "Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        settingsItem.addActionListener(e -> showSettings());
        leaderboardItem.addActionListener(e -> LeaderboardView.showLeaderboard(myWindow));

        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(
                myWindow,
                "About the game:\n\n"
                        + "Created by: Angelina Christianson, Makani Malawo, and Tifanie Ngo\n"
                        + "Course: TCSS 360\n"
                        + "Version: 2.7\n\n"
                        + "This game is a garden style trivia maze game where you go through\n"
                        + "doors by answering trivia questions correctly.\n",
                "About",
                JOptionPane.INFORMATION_MESSAGE
        ));

        instructionsItem.addActionListener(e -> JOptionPane.showMessageDialog(
                myWindow,
                "How to Play:\n\n"
                        + "Use the buttons on the right side of the screen to traverse through the maze.\n"
                        + "You have 3 tries per door to get it right. If you fail all 3 tries, the door locks permanently.\n"
                        + "Use the hint button if you can't find the exit!\n\n",
                "Instructions",
                JOptionPane.INFORMATION_MESSAGE
        ));

        return menuBar;
    }
}
