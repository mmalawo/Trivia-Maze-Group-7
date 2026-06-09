package controller;

import view.InstructionsView;
import view.PlayerSetupView;

/**
 * PlayerController handles setup-screen input and starting gameplay.
 * The view supplies input; the controller updates the model through AppController.
 */
public class PlayerController {
    private final AppController myApp;
    private final PlayerSetupView mySetupView;
    private final InstructionsView myInstructionsView;

    public PlayerController(final AppController theApp,
                            final PlayerSetupView theSetupView,
                            final InstructionsView theInstructionsView) {
        myApp = theApp;
        mySetupView = theSetupView;
        myInstructionsView = theInstructionsView;
        addListeners();
    }

    private void addListeners() {
        mySetupView.addBackListener(e -> myApp.goBack());

        mySetupView.addNextListener(e -> {
            myApp.getPlayer().setName(mySetupView.getEnteredName());
            myApp.showInstructions();
        });

        mySetupView.addNextAvatarListener(e -> {
            mySetupView.nextCharacter();
            mySetupView.updateCharacter(myApp.getSettingsView().isDarkModeSelected());
        });

        mySetupView.addPrevListener(e -> {
            mySetupView.previousCharacter();
            mySetupView.updateCharacter(myApp.getSettingsView().isDarkModeSelected());
        });

        myInstructionsView.addStartGameListener(e -> {
            System.out.println("Game starting...");
            myApp.startGameplay();
        });
    }
}
