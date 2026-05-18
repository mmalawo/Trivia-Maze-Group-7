package controller;

import model.*;
import view.*;
import controller.*;

import javax.swing.*;

public class PlayerController {
    private final PlayerSetupView setupView;

    public PlayerController(PlayerSetupView theSetupView) {
        this.setupView = theSetupView;
        addListeners();
    }

    private void addListeners() {
        setupView.addBackListener(e -> {
            JFrame frame = MainGUI.window;
            frame.getContentPane().removeAll();

            frame.getContentPane().add(MainGUI.menuView);

            frame.revalidate();
            frame.repaint();
        });
        setupView.addNextListener(e -> {
            String playerName = MainGUI.player.getName();

            MainGUI.instructionsView.setPlayerName(playerName);

            MainGUI.window.getContentPane().removeAll();
            MainGUI.window.getContentPane().add(MainGUI.instructionsView);

            MainGUI.window.revalidate();
            MainGUI.window.repaint();
        });
        setupView.addNextAvatarListener(e -> {
            PlayerSetupView.setSlide(PlayerSetupView.getSlide() + 1);
            setupView.updateCharacter();
        });
        MainGUI.instructionsView.addStartGameListener(e -> {
            System.out.println("Game starting...");

            MainGUI.player.startTimer();

            MainGUI.window.getContentPane().removeAll();

            // Later replace this with actual MazeView
            MainGUI.window.getContentPane().add(MainGUI.menuView);

            MainGUI.window.revalidate();
            MainGUI.window.repaint();
        });
        setupView.addPrevListener(e -> {
            PlayerSetupView.setSlide(PlayerSetupView.getSlide() - 1);
            setupView.updateCharacter();
        });
    }

}
