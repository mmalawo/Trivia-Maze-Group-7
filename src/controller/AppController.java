package controller;

import model.*;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

/** Controls application-level navigation, view switching, and shared game state.
 *  <p>This controller owns the main application window, manages the active
 *  player and maze objects, coordinates view transitions, and handles actions
 *  such as starting, saving, loading, and resetting games.</p>
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

    /**
     * Constructs the application controller.
     *
     * <p>This initializes the sound system, creates the main views, starts the
     * initial game state, creates supporting controllers, and displays the main
     * menu.</p>
     *
     * @param theWindow the main application window
     */
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

    /**
     * Creates and initializes all application views.
     */
    private void createViews() {
        myMenuView = new GameMenuView();
        mySettingsView = new SettingsView();
        mySetupView = new PlayerSetupView();
        myInstructionsView = new InstructionsView();

        myWindow.setJMenuBar(createMenuBar());
    }

    /**
     * Creates and initializes all application controllers.
     */
    private void createControllers() {
        new MenuController(this, myMenuView);
        new PlayerController(this, mySetupView, myInstructionsView);
        new SettingsController(this, mySettingsView, mySoundManager);
    }

    /**
     * Switches the displayed view to the specified panel.
     *
     * <p>The current panel is stored in the navigation history
     * so the user can return to it later.</p>
     *
     * @param thePanel the panel to display
     */
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

    /**
     * Returns to the previously displayed view, if one exists.
     */
    public void goBack() {
        if (!myPanelHistory.isEmpty()) {
            JPanel previousPanel = myPanelHistory.pop();
            myWindow.setContentPane(previousPanel);
            myWindow.revalidate();
            myWindow.repaint();
            previousPanel.requestFocusInWindow();
        }
    }

    /**
     * Starts a new game.
     *
     * <p>This stops the timer display loop, generates a new maze, creates a new
     * player, resets saved-game state, deletes any existing save file, clears the
     * navigation history, and prepares the maze view and game controller.</p>
     */
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

    }

    /**
     * Displays the player setup screen after resetting the game.
     */
    public void showPlayerSetup() {
        startNewGame();
        switchView(mySetupView);
    }

    /**
     * Starts gameplay using the selected player sprites and begins updating the game timer display.
     */
    public void showSettings() {
        switchView(mySettingsView);
    }

    /**
     * Displays the game instructions screen.
     */
    public void showInstructions() {
        myInstructionsView.setPlayerName(myPlayer.getName());
        switchView(myInstructionsView);
    }

    /**
     * Starts gameplay and begins updating the game timer display.
     */
    public void startGameplay() {
        myMazeView.setPlayerSprites(
                mySetupView.getCurrentFlapIcon(),
                mySetupView.getCurrentUnflapIcon()
        );
        myPlayer.startTimer();
        switchView(myMazeView);
        startTimerDisplayLoop();
    }

    /**
     * Returns the user to the main menu after a game has ended.
     *
     * <p>This resets the game state before displaying the main menu.</p>
     */
    public void returnToMainMenuAfterGame() {
        startNewGame();
        switchView(myMenuView);
    }

    /**
     * Returns the user to the main menu from a menu-bar action.
     *
     * <p>If the current game contains unsaved progress,
     * the user is prompted for confirmation before game state is reset.</p>
     */
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

    /**
     * Saves the current game state to disk.
     *
     * <p>If the player or maze has not been initialized, no save is performed.</p>
     */
    public void saveGame() {
        if (myPlayer == null || myMaze == null) {
            return;
        }

        myPlayer.saveElapsedTime();
        SaveManager.saveGame(new Memento(myPlayer, myMaze));
        myHasSaved = true;
    }

    /**
     * Loads a previously saved game.
     *
     * <p>If a saved game exists, this restores the player and maze, rebuilds the
     * gameplay controller and maze view, resumes the timer, and displays the maze.</p>
     *
     * @return {@code true} if a saved game was successfully loaded;
     *         {@code false} otherwise
     */
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

    /**
     * Marks the current game as having unsaved changes.
     */
    public void markGameNoLongerSaved() {
        myHasSaved = false;
    }

    /**
     * Returns the current player.
     *
     * @return the active player
     */
    public Player getPlayer() {
        return myPlayer;
    }

    /**
     * Returns the settings view.
     *
     * @return the settings view
     */
    public SettingsView getSettingsView() {
        return mySettingsView;
    }

    /**
     * Applies the selected dark mode setting to all applicable views.
     *
     * <p>Updates the main menu, player setup screen, and maze view
    * (if it has been created) to use either the dark or light theme.</p>
     *
    * @param theDarkModeSelected {@code true} to enable dark mode;
    *                            {@code false} to enable light mode
    */
    public void applyDarkMode(final boolean theDarkModeSelected) {
        myMenuView.setDarkMode(theDarkModeSelected);
        mySetupView.setDarkMode(theDarkModeSelected);
        if (myMazeView != null) {
            myMazeView.setDarkMode(theDarkModeSelected);
        }
    }

    /**
     * Returns the application's main window.
     *
     * @return the main application window
     */
    public JFrame getWindow() {
        return myWindow;
    }

    /**
     * Starts the timer display loop used to refresh
     * the game timer once per second.
     */
    private void startTimerDisplayLoop() {
        stopTimerDisplayLoop();

        myTimerDisplayLoop = new Timer(1000, e -> {
            if (myMazeView != null && myPlayer != null) {
                myMazeView.updateTimer(myPlayer.elapsedTime());
            }
        });
        myTimerDisplayLoop.start();
    }

    /**
     * Stops the timer update loop if it is running.
     */
    private void stopTimerDisplayLoop() {
        if (myTimerDisplayLoop != null) {
            myTimerDisplayLoop.stop();
            myTimerDisplayLoop = null;
        }
    }

    /**
     * Creates and configures the application's menu bar.
     *
     * <p>The menu bar includes actions for returning to the main menu, saving,
     * loading, exiting, opening settings, viewing the leaderboard, and displaying
     * help dialogs.</p>
     *
     * @return the configured menu bar
     */
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
                        + "Version: 1.0\n\n"
                        + "This game is a garden style trivia maze game where you go through\n"
                        + "doors by answering trivia questions correctly.\n",
                "About",
                JOptionPane.INFORMATION_MESSAGE
        ));

        instructionsItem.addActionListener(e -> JOptionPane.showMessageDialog(
                myWindow,
                "How to Play:\n\n"
                        + "Use the buttons on the right side of the screen to traverse through the maze.\n"
                        + "You have 2 tries per door to get it right. If you fail both tries, the door locks permanently.\n"
                        + "Use the hint button if you can't find the exit!\n\n",
                "Instructions",
                JOptionPane.INFORMATION_MESSAGE
        ));

        return menuBar;
    }
}
