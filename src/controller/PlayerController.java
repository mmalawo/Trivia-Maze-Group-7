package controller;

import model.*;
import view.*;
import controller.*;

import javax.swing.*;

/**
 * PlayerController handles user interactions on the PlayerSetupView
 * and InstructionsView. It manages character selection, player name input,
 * navigation between views, and starting the game timer.
 */
public class PlayerController {

    /** The player setup view this controller manages. */
    private final PlayerSetupView setupView;

    /**
     * Constructs a PlayerController and registers listeners on the given view.
     *
     * @param theSetupView the player setup view
     */
    public PlayerController(PlayerSetupView theSetupView) {
        this.setupView = theSetupView;
        addListeners();
    }

    /**
     * Registers action listeners for the back, next, previous avatar,
     * next avatar, and start game buttons.
     */
    private void addListeners() {

        /**
         * Navigates back to the previous view.
         */
        setupView.addBackListener(e -> {
            MainGUI.goBack();
        });

        /**
         * Advances to the instructions view with the player's entered name.
         */
        setupView.addNextListener(e -> {
            String playerName = MainGUI.player.getName();
            MainGUI.instructionsView.setPlayerName(playerName);
            MainGUI.switchView(MainGUI.instructionsView);
        });

        /**
         * Advances to the next available butterfly character option.
         */
        setupView.addNextAvatarListener(e -> {
            PlayerSetupView.setSlide(PlayerSetupView.getSlide() + 1);
            setupView.updateCharacter();
        });

        /**
         * Starts the game: begins the timer, switches to the maze view,
         * and starts the timer display update loop.
         */
        MainGUI.instructionsView.addStartGameListener(e -> {
            System.out.println("Game starting...");

            MainGUI.player.startTimer();
            MainGUI.switchView(MainGUI.mazeView);
            MainGUI.player.startTimer();

            new javax.swing.Timer(1000, evt -> {
                double time = MainGUI.player.elapsedTime();
                MainGUI.mazeView.updateTimer(time);
            }).start();
        });

        /**
         * Goes back to the previous butterfly character option.
         */
        setupView.addPrevListener(e -> {
            PlayerSetupView.setSlide(PlayerSetupView.getSlide() - 1);
            setupView.updateCharacter();
        });
    }
}