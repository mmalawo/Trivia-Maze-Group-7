package controller;

import model.*;
import view.*;

import javax.swing.*;

/**
 * GameController handles all game logic for the Trivia Maze game.
 * It manages player movement, trivia question handling, win/lose conditions,
 * and pathway checking — keeping this logic out of MazeView (MVC separation).
 */
public class GameController {

    private final Maze myMaze;
    private final Player myPlayer;
    private MazeView myMazeView;
    private GameMenuView myMenu;

    private boolean myGameFinished = false;

    /**
     * Constructor for game menu setup (called before maze is created).
     *
     * @param theMenu   the game menu view
     * @param thePlayer the player model
     */
    public GameController(GameMenuView theMenu, Player thePlayer) {
        this.myMenu = theMenu;
        this.myPlayer = thePlayer;
        this.myMaze = null;
        addMenuListeners();
    }

    /**
     * Constructor used for active gameplay with maze and player references.
     *
     * @param theMaze   the maze model
     * @param thePlayer the player model
     */
    public GameController(Maze theMaze, Player thePlayer) {
        this.myMaze = theMaze;
        this.myPlayer = thePlayer;
    }

    /**
     * Sets the MazeView reference after construction to avoid circular dependency.
     *
     * @param theMazeView the maze view
     */
    public void setMazeView(MazeView theMazeView) {
        this.myMazeView = theMazeView;
    }

    /**
     * Resets the game finished flag for a new game.
     */
    public void resetGameFinished() {
        myGameFinished = false;
    }

    private void addMenuListeners() {
        if (myMenu != null) {
            myMenu.addPlayListener(e -> System.out.println("Game in progress..."));
        }
    }

    public static void restartGame() {
        MainGUI.startNewGame();
        System.out.println("Game restarted.");
    }

    // =====================================================
    // MOVEMENT
    // =====================================================

    /**
     * Handles a move attempt in the given direction.
     * Called by MazeView buttons.
     *
     * @param theDirection the direction to move ("north", "south", "east", "west")
     */
    public void handleMove(String theDirection) {
        int playerRow = myMazeView.getPlayerRow();
        int playerCol = myMazeView.getPlayerCol();

        Room currentRoom = myMaze.getRoom(playerRow, playerCol);

        Door potentialDoor = switch (theDirection) {
            case "north" -> currentRoom.getNorthDoor();
            case "south" -> currentRoom.getSouthDoor();
            case "west"  -> currentRoom.getWestDoor();
            case "east"  -> currentRoom.getEastDoor();
            default -> null;
        };

        boolean isExitDoor = potentialDoor != null && potentialDoor == myMaze.getExitDoor();

        if (!isValidMove(theDirection, playerRow, playerCol) && !isExitDoor) {
            JOptionPane.showMessageDialog(
                    MainGUI.getWindow(),
                    "That door leads nowhere! Try a different door.",
                    "Dead End",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Door door = potentialDoor;

        int newRow = playerRow;
        int newCol = playerCol;

        switch (theDirection) {
            case "north" -> newRow = playerRow - 1;
            case "south" -> newRow = playerRow + 1;
            case "west"  -> newCol = playerCol - 1;
            case "east"  -> newCol = playerCol + 1;
        }

        if (door == null) return;

        if (door.isPermanentlyClosed()) {
            JOptionPane.showMessageDialog(
                    MainGUI.getWindow(),
                    "This door is permanently locked!",
                    "Blocked",
                    JOptionPane.ERROR_MESSAGE
            );

            if (!checkForPossiblePathways(playerRow, playerCol)) {
                loseGame("You are completely blocked!\nAll reachable doors are permanently locked.\nGame Over.");
            }

            return;
        }

        if (!door.isLocked() && !isExitDoor) {
            myMazeView.movePlayer(newRow, newCol);
        } else {
            handleTriviaAttempt(door, isExitDoor, newRow, newCol, playerRow, playerCol);
        }
    }

    /**
     * Handles the trivia question popup and result for a door attempt.
     *
     * @param theDoor       the door being attempted
     * @param theIsExitDoor whether this door is the exit door
     * @param theNewRow     the row the player would move to
     * @param theNewCol     the column the player would move to
     * @param thePlayerRow  the player's current row
     * @param thePlayerCol  the player's current column
     */
    private void handleTriviaAttempt(Door theDoor, boolean theIsExitDoor,
                                     int theNewRow, int theNewCol,
                                     int thePlayerRow, int thePlayerCol) {
        Question q = theDoor.getQuestion();

        if (q == null) return;

        TriviaPopup popup = new TriviaPopup(q);
        popup.setVisible(true);

        String playerAnswer = popup.getPlayerAnswer();
        boolean correct = theDoor.attemptAnswer(playerAnswer);

        if (correct) {
            myPlayer.incrementCorrectScore();
            QuestionDAO.markAsCorrectlyAnswered(q);

            if (theIsExitDoor) {
                finishGame();
                return;
            }

            myMazeView.movePlayer(theNewRow, theNewCol);

            JOptionPane.showMessageDialog(
                    MainGUI.getWindow(),
                    "Correct! Door unlocked!",
                    "Result",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            myPlayer.incrementIncorrectScore();

            String attemptsMsg = theDoor.isPermanentlyClosed()
                    ? "Wrong! This door is now permanently locked."
                    : "Wrong! " + theDoor.getAttemptsRemaining() + " attempt(s) remaining.";

            JOptionPane.showMessageDialog(
                    MainGUI.getWindow(),
                    attemptsMsg,
                    "Result",
                    JOptionPane.ERROR_MESSAGE
            );

            System.out.println("Wrong! Staying in room [" + thePlayerRow + "][" + thePlayerCol + "]");

            if (theIsExitDoor && theDoor.isPermanentlyClosed()) {
                loseGame("You failed to unlock the exit!\nGame Over.");
                return;
            }

            if (!checkForPossiblePathways(thePlayerRow, thePlayerCol)) {
                loseGame("All doors in your vicinity are permanently locked.\nGame Over.");
            }
        }
    }

    // =====================================================
    // MOVEMENT VALIDATION
    // =====================================================

    /**
     * Checks if movement in the given direction is within maze bounds.
     *
     * @param theDirection the direction to check
     * @param theRow       the player's current row
     * @param theCol       the player's current column
     * @return true if the move is within bounds
     */
    private boolean isValidMove(String theDirection, int theRow, int theCol) {
        return switch (theDirection) {
            case "north" -> theRow > 0;
            case "south" -> theRow < myMaze.getRows() - 1;
            case "west"  -> theCol > 0;
            case "east"  -> theCol < myMaze.getCols() - 1;
            default -> false;
        };
    }

    // =====================================================
    // PATHWAY CHECKING
    // =====================================================

    /**
     * Checks if the player still has at least one reachable door to attempt.
     *
     * @param thePlayerRow the player's current row
     * @param thePlayerCol the player's current column
     * @return true if at least one reachable door exists
     */
    public boolean checkForPossiblePathways(int thePlayerRow, int thePlayerCol) {
        boolean[][] visitedRooms = new boolean[myMaze.getRows()][myMaze.getCols()];
        return hasAttemptableDoor(thePlayerRow, thePlayerCol, visitedRooms);
    }

    /**
     * Recursively checks whether the player still has at least one reachable door
     * that can be attempted.
     *
     * @param theRow          the row to check
     * @param theColumn       the column to check
     * @param theVisitedRooms tracks which rooms have been visited
     * @return true if an attemptable door exists
     */
    private boolean hasAttemptableDoor(int theRow, int theColumn, boolean[][] theVisitedRooms) {
        if (theRow < 0 || theRow >= myMaze.getRows()
                || theColumn < 0 || theColumn >= myMaze.getCols()) {
            return false;
        }

        if (theVisitedRooms[theRow][theColumn]) return false;

        theVisitedRooms[theRow][theColumn] = true;

        Room room = myMaze.getRoom(theRow, theColumn);

        if (isAttemptableDoor(room.getNorthDoor(), "north", theRow, theColumn)) return true;
        if (isAttemptableDoor(room.getSouthDoor(), "south", theRow, theColumn)) return true;
        if (isAttemptableDoor(room.getEastDoor(), "east",  theRow, theColumn)) return true;
        if (isAttemptableDoor(room.getWestDoor(), "west",  theRow, theColumn)) return true;

        if (canTravelThrough(room.getNorthDoor(), "north", theRow, theColumn)) {
            if (hasAttemptableDoor(theRow - 1, theColumn, theVisitedRooms)) return true;
        }
        if (canTravelThrough(room.getSouthDoor(), "south", theRow, theColumn)) {
            if (hasAttemptableDoor(theRow + 1, theColumn, theVisitedRooms)) return true;
        }
        if (canTravelThrough(room.getEastDoor(), "east", theRow, theColumn)) {
            if (hasAttemptableDoor(theRow, theColumn + 1, theVisitedRooms)) return true;
        }
        if (canTravelThrough(room.getWestDoor(), "west", theRow, theColumn)) {
            if (hasAttemptableDoor(theRow, theColumn - 1, theVisitedRooms)) return true;
        }

        return false;
    }

    /**
     * Determines whether a specific door can still be attempted by the player.
     *
     * @param theDoor      the door to check
     * @param theDirection the direction of the door
     * @param theRow       the room's row
     * @param theColumn    the room's column
     * @return true if the door can be attempted
     */
    private boolean isAttemptableDoor(Door theDoor, String theDirection,
                                      int theRow, int theColumn) {
        if (theDoor == null || theDoor.isPermanentlyClosed()) return false;

        if (theDoor == myMaze.getExitDoor()) return true;

        if (!theDoor.isLocked()) return false;

        int updatedRow = theRow;
        int updatedColumn = theColumn;

        switch (theDirection) {
            case "north" -> updatedRow--;
            case "south" -> updatedRow++;
            case "east"  -> updatedColumn++;
            case "west"  -> updatedColumn--;
        }

        return updatedRow >= 0 && updatedRow < myMaze.getRows()
                && updatedColumn >= 0 && updatedColumn < myMaze.getCols();
    }

    /**
     * Determines whether the player can travel through a door into a neighboring room.
     *
     * @param theDoor      the door to check
     * @param theDirection the direction of the door
     * @param theRow       the room's row
     * @param theColumn    the room's column
     * @return true if the player can pass through
     */
    private boolean canTravelThrough(Door theDoor, String theDirection,
                                     int theRow, int theColumn) {
        if (theDoor == null || theDoor.isLocked() || theDoor.isPermanentlyClosed()) return false;

        int updatedRow = theRow;
        int updatedColumn = theColumn;

        switch (theDirection) {
            case "north" -> updatedRow--;
            case "south" -> updatedRow++;
            case "east"  -> updatedColumn++;
            case "west"  -> updatedColumn--;
        }

        return updatedRow >= 0 && updatedRow < myMaze.getRows()
                && updatedColumn >= 0 && updatedColumn < myMaze.getCols();
    }

    // =====================================================
    // WIN / LOSE
    // =====================================================

    /**
     * Triggers the win condition when the player correctly answers the exit door.
     */
    public void finishGame() {
        if (myGameFinished) return;

        myGameFinished = true;

        myPlayer.stopTimer();

        // Delete save file — game is complete, Resume should no longer show
        SaveManager.deleteSaveFile();
        MainGUI.hasSaved = false;

        LeaderboardDAO leaderboardDAO = new LeaderboardDAO();
        leaderboardDAO.saveScore(myPlayer);

        JOptionPane.showMessageDialog(
                MainGUI.getWindow(),
                "You reached the exit!\nTime: " +
                        formatTime(myPlayer.getRecordTime()) +
                        "\nCorrect answers: " + myPlayer.getCorrectScore() +
                        "\nIncorrect answers: " + myPlayer.getIncorrectScore() +
                        "\nYour score was saved to the leaderboard.",
                "Game Complete",
                JOptionPane.INFORMATION_MESSAGE
        );

        LeaderboardView.showLeaderboard();
    }

    /**
     * Triggers the lose condition with a given message.
     *
     * @param theMessage the message to display to the player
     */
    public void loseGame(String theMessage) {
        if (myGameFinished) return;

        myGameFinished = true;

        myPlayer.stopTimer();

        // Delete save file — game is over, Resume should no longer show
        SaveManager.deleteSaveFile();
        MainGUI.hasSaved = false;

        Object[] options = {"Main Menu", "Exit Game"};

        int choice = JOptionPane.showOptionDialog(
                MainGUI.getWindow(),
                theMessage,
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.YES_OPTION) {
            MainGUI.startNewGame();
            MainGUI.switchView(MainGUI.menuView);
        } else {
            System.exit(0);
        }
    }

    // =====================================================
    // HINT
    // =====================================================

    /**
     * Shows a hint pointing toward the exit room.
     *
     * @param thePlayerRow the player's current row
     * @param thePlayerCol the player's current column
     */
    public void showHint(int thePlayerRow, int thePlayerCol) {
        int[] exitPos = myMaze.findRoom(myMaze.getExit());

        int exitRow = exitPos[0];
        int exitCol = exitPos[1];

        StringBuilder hint = new StringBuilder("Hint: Try moving ");

        if (exitRow > thePlayerRow) {
            hint.append("south ");
        } else if (exitRow < thePlayerRow) {
            hint.append("north ");
        }

        if (exitCol > thePlayerCol) {
            hint.append("east ");
        } else if (exitCol < thePlayerCol) {
            hint.append("west ");
        }

        if (exitRow == thePlayerRow && exitCol == thePlayerCol) {
            hint = new StringBuilder("You are already at the exit room! Find the exit door.");
        } else {
            hint.append("toward the exit.");
        }

        JOptionPane.showMessageDialog(
                MainGUI.getWindow(),
                hint.toString(),
                "Hint",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // UTILITY
    // =====================================================

    /**
     * Formats a time in seconds into a human-readable string.
     *
     * @param theTimeSeconds the time in seconds
     * @return a formatted string like "2 min 05 sec"
     */
    private String formatTime(double theTimeSeconds) {
        int totalSeconds = (int) Math.round(theTimeSeconds);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d min %02d sec", minutes, seconds);
    }
}