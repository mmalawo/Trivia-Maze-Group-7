package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Represents the instructions screen displayed before the game begins.
 * This view shows a personalized greeting, game instructions,
 * and a button to start the game.
 */
public class InstructionsView extends JPanel {
    private final JLabel greetingLabel;
    private JTextArea instructionsText;
    private final JButton startGameButton;

    /**
     * Constructs the instructions view and initializes all UI components,
     * including the greeting label, instructions text area, and start game button.
     */
    public InstructionsView() {
        setLayout(null);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

        greetingLabel = new JLabel();
        greetingLabel.setBounds(500, 80, 800, 50);
        greetingLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 32));
        add(greetingLabel);

        instructionsText = new JTextArea(
                "How to Play:\n\n" +
                        "- Use the arrow keys to navigate through the maze.\n" +
                        "- Answer trivia questions to unlock doors.\n" +
                        "- Correct answers let you move forward. If you answer a \n" +
                        "question incorrectly-oops, that door is closed permanently!\n" +
                        "- If there are no doors left to unlock, it's game over!\n" +
                        "- Try to reach the exit as fast as possible!"
        );
        instructionsText.setBounds(600, 160, 700, 300);
        instructionsText.setFont(new Font("Comic Sans MS", Font.PLAIN, 22));
        instructionsText.setEditable(false);
        instructionsText.setOpaque(false);
        add(instructionsText);

        startGameButton = new JButton("Start Game");
        startGameButton.setBounds(650, 500, 200, 50);
        add(startGameButton);
    }

    /**
     * Updates the greeting message to include the player's name.
     *
     * @param theName the player's name
     */
    public void setPlayerName(String theName) {

        greetingLabel.setText("Welcome to the Trivia Maze, " + theName + "!");
    }

    /**
     * Adds an action listener to the Start Game button.
     *
     * @param theListener the listener to invoke when the button is clicked
     */
    public void addStartGameListener(ActionListener theListener) {
        startGameButton.addActionListener(theListener);

    }
}