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
    private final JLabel myGreetingLabel;
    private final JTextArea myInstructionsText;
    private final JButton myStartGameButton;

    /**
     * Constructs the instructions view.
     *
     * <p>This initializes the greeting label, instructions text area,
     * start game button, layout settings, and preferred screen size.</p>
     */
    public InstructionsView() {
        setLayout(null);
        setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

        myGreetingLabel = new JLabel();
        myGreetingLabel.setBounds(500, 80, 800, 50);
        myGreetingLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 32));
        add(myGreetingLabel);

        myInstructionsText = new JTextArea(
                "How to Play:\n\n" +
                        "- Use the arrow keys to navigate through the maze.\n" +
                        "- Answer trivia questions to unlock doors.\n" +
                        "- Correct answers let you move forward. If you answer a \n" +
                        "question incorrectly-oops, that door is closed permanently!\n" +
                        "- If there are no doors left to unlock, it's game over!\n" +
                        "- Try to reach the exit as fast as possible!"
        );
        myInstructionsText.setBounds(600, 160, 700, 300);
        myInstructionsText.setFont(new Font("Comic Sans MS", Font.PLAIN, 22));
        myInstructionsText.setEditable(false);
        myInstructionsText.setOpaque(false);
        add(myInstructionsText);

        myStartGameButton = new JButton("Start Game");
        myStartGameButton.setBounds(650, 500, 200, 50);
        add(myStartGameButton);
    }

    /**
     * Updates the greeting message with the player's name.
     *
     * @param theName the player's name to display in the greeting
     */
    public void setPlayerName(final String theName) {

        myGreetingLabel.setText("Welcome to the Trivia Maze, " + theName + "!");
    }

    /**
     * Adds an action listener to the Start Game button.
     *
     * @param theListener the listener to invoke when the button is clicked
     */
    public void addStartGameListener(final ActionListener theListener) {
        myStartGameButton.addActionListener(theListener);

    }
}