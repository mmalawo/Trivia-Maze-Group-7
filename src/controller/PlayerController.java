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
            MainGUI.window.getContentPane().removeAll();

            MainGUI.window.revalidate();
            MainGUI.window.repaint();
        });
        setupView.addNextAvatarListener(e -> {
            PlayerSetupView.setSlide(PlayerSetupView.getSlide() + 1);
            setupView.updateCharacter();
        });
        setupView.addNextAvatarListener(e -> {
            PlayerSetupView.setSlide(PlayerSetupView.getSlide() + 1);
            setupView.updateCharacter();
        });
        setupView.addPrevListener(e -> {
            PlayerSetupView.setSlide(PlayerSetupView.getSlide() - 1);
            setupView.updateCharacter();
        });
    }

}
