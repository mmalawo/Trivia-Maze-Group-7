package controller;

import model.*;
import view.*;

import javax.swing.*;

/**
 * Controls gameplay logic for the Trivia Maze game.
 *
 * <p>This controller manages player movement, trivia question attempts,
 * win and lose conditions, pathway checking, hints, and communication
 * between the maze model and maze view.</p>
 */
public class GameController {

    private final Maze myMaze;
    private final Player myPlayer;
    private final JFrame myWindow;
    private final AppController myApp;
    private MazeView myMazeView;

    private boolean myGameFinished = false;

    /**
     * Constructor used for active gameplay with maze and player references.
     *
     * @param theMaze   the maze model
     * @param thePlayer the player model
     * @param theWindow the application window used as dialog owner
     * @param theApp    the app controller used for app-level navigation
     */
    public GameController(final Maze theMaze, final Player thePlayer, final JFrame theWindow, final AppController theApp) {
        this.myMaze = theMaze;
        this.myPlayer = thePlayer;
        this.myWindow = theWindow;
        this.myApp = theApp;
    }

    /**
     * Sets the maze view after construction.
     *
     * <p>This avoids requiring the maze view before the controller has been
     * fully created.</p>
     *
     * @param theMazeView the maze view controlled by this controller
     */
    public void setMazeView(final MazeView theMazeView) {
        this.myMazeView = theMazeView;
    }

    // =====================================================
    // MOVEMENT
    // =====================================================

    /**
     * Handles a player move attempt in the specified direction.
     *
     * <p>This checks whether the move is valid, determines whether the door
     * can be entered, handles locked or permanently closed doors, and starts
     * a trivia attempt when necessary.</p>
     *
     * @param theDirection the direction to move; expected values are
     *                     {@code "north"}, {@code "south"},
     *                     {@code "east"}, or {@code "west"}
     */
    public void handleMove(final String theDirection) {
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
                    myWindow,
                    "That door leads nowhere! Try a different door.",
                    "Dead End",
                    JOptionPane.WARNING_MESSAGE
            );
            // Mark door as permanently closed so it disappears visually
            if (potentialDoor != null) {
                potentialDoor.setPermanentlyClosed(true);
            }
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
                    myWindow,
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
     * Handles the trivia question attempt for a selected door.
     *
     * <p>This displays the trivia popup, checks the player's answer, updates
     * the player's score, unlocks or permanently locks the door as needed,
     * moves the player after a correct answer, and checks for win or lose
     * conditions.</p>
     *
     * @param theDoor the door being attempted
     * @param theIsExitDoor {@code true} if the attempted door is the exit door;
     *                      {@code false} otherwise
     * @param theNewRow the row the player moves to after a correct answer
     * @param theNewCol the column the player moves to after a correct answer
     * @param thePlayerRow the player's current row before the attempt
     * @param thePlayerCol the player's current column before the attempt
     */
    private void handleTriviaAttempt(final Door theDoor, final boolean theIsExitDoor,
                                     final int theNewRow, final int theNewCol,
                                     final int thePlayerRow, final int thePlayerCol) {
        Question q = theDoor.getQuestion();

        if (q == null) return;

        TriviaPopup popup = new TriviaPopup(myWindow, q);
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
                    myWindow,
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
                    myWindow,
                    attemptsMsg,
                    "Result",
                    JOptionPane.ERROR_MESSAGE
            );

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
     * Determines whether movement in the specified direction stays within
     * the maze boundaries.
     *
     * @param theDirection the direction to check
     * @param theRow the player's current row
     * @param theCol the player's current column
     * @return {@code true} if the move stays within the maze boundaries;
     *         {@code false} otherwise
     */
    private boolean isValidMove(final String theDirection, final int theRow, final int theCol) {
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
     * Determines whether the player still has at least one reachable door
     * that can be attempted.
     *
     * @param thePlayerRow the player's current row
     * @param thePlayerCol the player's current column
     * @return {@code true} if at least one reachable attemptable door exists;
     *         {@code false} otherwise
     */
    public boolean checkForPossiblePathways(final int thePlayerRow, final int thePlayerCol) {
        boolean[][] visitedRooms = new boolean[myMaze.getRows()][myMaze.getCols()];
        return hasAttemptableDoor(thePlayerRow, thePlayerCol, visitedRooms);
    }

    /**
     /**
     * Recursively searches reachable rooms for at least one door that can
     * still be attempted.
     *
     * <p>This method avoids revisiting rooms by tracking visited locations
     * in the provided two-dimensional array.</p>
     *
     * @param theRow the row currently being checked
     * @param theColumn the column currently being checked
     * @param theVisitedRooms tracks which rooms have already been checked
     * @return {@code true} if an attemptable door is found;
     *         {@code false} otherwise
     */
    private boolean hasAttemptableDoor(final int theRow, final int theColumn, final boolean[][] theVisitedRooms) {
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
     * <p>A door is attemptable if it exists, is not permanently closed, and
     * either leads to the exit or is a locked door within the maze boundaries.</p>
     *
     * @param theDoor the door to check
     * @param theDirection the direction of the door from the current room
     * @param theRow the current room's row
     * @param theColumn the current room's column
     * @return {@code true} if the door can still be attempted;
     *         {@code false} otherwise
     */
    private boolean isAttemptableDoor(final Door theDoor, final String theDirection,
                                      final int theRow, final int theColumn) {
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
     * Determines whether the player can travel through a door into a
     * neighboring room.
     *
     * <p>The player can travel through a door only if the door exists, is
     * unlocked, is not permanently closed, and leads to a room within the maze
     * boundaries.</p>
     *
     * @param theDoor the door to check
     * @param theDirection the direction of the door from the current room
     * @param theRow the current room's row
     * @param theColumn the current room's column
     * @return {@code true} if the player can travel through the door;
     *         {@code false} otherwise
     */
    private boolean canTravelThrough(final Door theDoor, final String theDirection,
                                     final int theRow, final int theColumn) {
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
     * Finishes the game after the player successfully unlocks the exit door.
     *
     * <p>This stops the timer, deletes the saved game file, records the player's
     * score on the leaderboard, displays the win message and leaderboard, and
     * prompts the player to return to the main menu or exit the game.</p>
     */
    public void finishGame() {
        if (myGameFinished) return;

        myGameFinished = true;

        myPlayer.stopTimer();

        // Delete save file — game is complete, Resume should no longer show
        SaveManager.deleteSaveFile();
        myApp.markGameNoLongerSaved();

        LeaderboardDAO leaderboardDAO = new LeaderboardDAO();
        leaderboardDAO.saveScore(myPlayer);

        // Show win popup
        JOptionPane.showMessageDialog(
                myWindow,
                "You reached the exit!\nTime: " +
                        formatTime(myPlayer.getRecordTime()) +
                        "\nCorrect answers: " + myPlayer.getCorrectScore() +
                        "\nIncorrect answers: " + myPlayer.getIncorrectScore() +
                        "\nYour score was saved to the leaderboard.",
                "Game Complete",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Show leaderboard — code continues after dialog closes
        LeaderboardView.showLeaderboard(myWindow);

        // After leaderboard closes, show Main Menu / Exit Game popup
        Object[] options = {"Main Menu", "Exit Game"};
        int choice = JOptionPane.showOptionDialog(
                myWindow,
                "What would you like to do next?",
                "Game Complete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.YES_OPTION) {
            myApp.returnToMainMenuAfterGame();
        } else {
            System.exit(0);
        }
    }

     /**
     * Ends the game with a loss using the specified message.
     *
     * <p>This stops the timer, deletes the saved game file, marks the game as
     * no longer saved, and prompts the player to return to the main menu or
     * exit the game.</p>
     *
     * @param theMessage the message to display to the player
     */
    public void loseGame(final String theMessage) {
        if (myGameFinished) return;

        myGameFinished = true;

        myPlayer.stopTimer();

        // Delete save file — game is over, Resume should no longer show
        SaveManager.deleteSaveFile();
        myApp.markGameNoLongerSaved();

        Object[] options = {"Main Menu", "Exit Game"};

        int choice = JOptionPane.showOptionDialog(
                myWindow,
                theMessage,
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.YES_OPTION) {
            myApp.returnToMainMenuAfterGame();
        } else {
            System.exit(0);
        }
    }

    // =====================================================
    // HINT
    // =====================================================

    /**
     * Displays a directional hint that points the player toward the exit room.
     *
     * @param thePlayerRow the player's current row
     * @param thePlayerCol the player's current column
     */
    public void showHint(final int thePlayerRow, final int thePlayerCol) {
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
                myWindow,
                hint.toString(),
                "Hint",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =====================================================
    // UTILITY
    // =====================================================

    /**
     * Formats a time value in seconds as minutes and seconds.
     *
     * @param theTimeSeconds the time value in seconds
     * @return a formatted time string in the form {@code "2 min 05 sec"}
     */
    private String formatTime(final double theTimeSeconds) {
        int totalSeconds = (int) Math.round(theTimeSeconds);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d min %02d sec", minutes, seconds);
    }
}