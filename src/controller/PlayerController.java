package controller;

import javax.swing.JOptionPane;
import view.InstructionsView;
import view.PlayerSetupView;

/**
 * Controls the player setup and instructions flow before gameplay begins.
 *
 * <p>This controller connects the setup and instructions views to the
 * application controller. It handles player name entry, avatar selection,
 * navigation, and starting the game.</p>
 */
public class PlayerController {
    private final AppController myApp;
    private final PlayerSetupView mySetupView;
    private final InstructionsView myInstructionsView;

    /**
     * Constructs a PlayerController and connects the setup
     * and instructions views to the application.
     *
     * @param theApp the main application controller
     * @param theSetupView the player setup view
     * @param theInstructionsView the instructions view
     */
    public PlayerController(final AppController theApp,
                            final PlayerSetupView theSetupView,
                            final InstructionsView theInstructionsView) {
        myApp = theApp;
        mySetupView = theSetupView;
        myInstructionsView = theInstructionsView;
        addListeners();
    }

    /**
     * Registers all event listeners for the player setup
     * and instructions screens.
     *
     * <p>Listeners handle navigation, player name entry,
     * avatar selection, and starting gameplay.</p>
     */
    private void addListeners() {
        mySetupView.addBackListener(e -> myApp.goBack());

        mySetupView.addNextListener(e -> {
            final String enteredName = mySetupView.getEnteredName();

            if (enteredName.isBlank()) {
                JOptionPane.showMessageDialog(
                        myApp.getWindow(),
                        "Please enter a player name before continuing.",
                        "Missing Player Name",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            myApp.getPlayer().setName(enteredName);
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
            myApp.startGameplay();
        });
    }
}
