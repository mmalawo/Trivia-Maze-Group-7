package controller;

import com.sun.tools.javac.Main;
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
            //MainGUI.switchView(MainGUI.previousView);
            MainGUI.goBack();
        });
        setupView.addNextListener(e -> {
            String playerName = MainGUI.player.getName();

            MainGUI.instructionsView.setPlayerName(playerName);

            MainGUI.switchView(MainGUI.instructionsView);

        });
        setupView.addNextAvatarListener(e -> {
            PlayerSetupView.setSlide(PlayerSetupView.getSlide() + 1);
            setupView.updateCharacter();
        });

        MainGUI.instructionsView.addStartGameListener(e -> {
            System.out.println("Game starting...");

            MainGUI.player.startTimer();

            MainGUI.switchView(MainGUI.mazeView);

        });
        setupView.addPrevListener(e -> {
            PlayerSetupView.setSlide(PlayerSetupView.getSlide() - 1);
            setupView.updateCharacter();
        });
    }

}
