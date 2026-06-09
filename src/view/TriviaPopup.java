package view;

import model.Question;
import javax.swing.*;
import java.awt.*;

/**
 * A modal dialog that displays a trivia question and collects
 * the player's answer. The dialog supports multiple-choice,
 * true/false, and text-entry question types.
 */
public class TriviaPopup extends JDialog {

    private String playerAnswer = "";

    /**
     * Constructs a trivia question dialog and displays the appropriate
     * answer input controls based on the question type.
     *
     * <p>For multiple-choice questions, answer buttons are generated
     * from the available options. For true/false questions, True and
     * False buttons are displayed. For other question types, a text
     * field is provided for answer entry.</p>
     *
     * @param owner the parent window that owns this dialog
     * @param question the trivia question to display
     */
    public TriviaPopup(Window owner, Question question) {
        super(owner, ModalityType.APPLICATION_MODAL);
        setTitle("Trivia Question");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(true);
        setAlwaysOnTop(true);

        JLabel questionLabel = new JLabel("<html><p style='width:620px'>" + question.getQuestionText() + "</p></html>");
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(questionLabel, BorderLayout.NORTH);

        String type = question.getQuestionType();

        if (type.equalsIgnoreCase("multiple choice")) {
            JPanel answersPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            answersPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
            String[] options = {
                    question.getOptionA(),
                    question.getOptionB(),
                    question.getOptionC(),
                    question.getOptionD()
            };
            for (String option : options) {
                if (option != null) {
                    JButton btn = new JButton(option);
                    final String captured = option; // explicitly capture for lambda
                    btn.addActionListener(e -> {
                        playerAnswer = captured;
                        dispose();
                    });
                    answersPanel.add(btn);
                }
            }
            add(answersPanel, BorderLayout.CENTER);

        } else if (type.equalsIgnoreCase("true/false")) {
            JPanel answersPanel = new JPanel(new GridLayout(1, 2, 10, 10));
            answersPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
            for (String option : new String[]{"True", "False"}) {
                JButton btn = new JButton(option);
                final String captured = option; // explicitly capture for lambda
                btn.addActionListener(e -> {
                    playerAnswer = captured;
                    dispose();
                });
                answersPanel.add(btn);
            }
            add(answersPanel, BorderLayout.CENTER);

        } else {
            JPanel answerPanel = new JPanel();
            answerPanel.setLayout(new BoxLayout(answerPanel, BoxLayout.Y_AXIS));
            answerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel hint = new JLabel("Type your answer:");
            hint.setAlignmentX(Component.CENTER_ALIGNMENT);
            hint.setFont(new Font("Arial", Font.PLAIN, 14));

            JTextField textField = new JTextField();
            textField.setMaximumSize(new Dimension(600, 40));
            textField.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton submitBtn = new JButton("Submit");
            submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            submitBtn.setMaximumSize(new Dimension(200, 40));
            submitBtn.addActionListener(e -> {
                playerAnswer = textField.getText().trim();
                dispose();
            });

            answerPanel.add(hint);
            answerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            answerPanel.add(textField);
            answerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            answerPanel.add(submitBtn);
            add(answerPanel, BorderLayout.CENTER);
        }
    }

    /**
     * Returns the answer submitted by the player.
     *
     * @return the player's selected or entered answer; an empty string
     *         if no answer has been provided
     */
    public String getPlayerAnswer() {
        return playerAnswer;
    }
}