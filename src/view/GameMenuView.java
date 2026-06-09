package view;

import model.SaveManager;

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GameMenuView extends JPanel {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    double screenWidth = screenSize.getWidth();
    double screenHeight = screenSize.getHeight();

    // Background Image
    private Image backgroundMenuImage;

    // Button Initialization
    private final JButton exitButton;
    private final JButton settingsButton;
    private final JButton playButton;
    private final JButton resumeButton;

    int buttonWidth = 400;
    int buttonHeight = 60;

    // Y positions for buttons
    private int playY;
    private int resumeY;
    private int settingsY;
    private int exitY;

    public GameMenuView() {
        this.setPreferredSize(new Dimension((int)screenWidth, (int)screenHeight));

        ImageIcon background = new ImageIcon("src/images/Day-Mode1.2.png");
        backgroundMenuImage = background.getImage();

        this.setDoubleBuffered(true);
        this.setLayout(null);

        int buttonX = (int)(screenWidth/2 - (screenWidth/7.68));
        int spacing = (int)(screenHeight/10.8);

        // Calculate base Y position
        playY = (int)(screenHeight/2 - spacing);

        // Play button
        playButton = new JButton("Play Game");
        playButton.setBounds(buttonX, playY, buttonWidth, buttonHeight);
        styleButton(playButton);
        this.add(playButton);

        // Resume button
        resumeButton = new JButton("Resume");
        resumeButton.setBounds(buttonX, playY + spacing, buttonWidth, buttonHeight);
        styleButton(resumeButton);
        resumeButton.setVisible(false);
        this.add(resumeButton);

        // Settings button
        settingsButton = new JButton("Settings");
        styleButton(settingsButton);
        this.add(settingsButton);

        // Exit button
        exitButton = new JButton("Exit Game");
        styleButton(exitButton);
        this.add(exitButton);

        // Initial layout without resume
        updateButtonLayout(false);
    }

    /**
     * Styles a button with transparent background and white text.
     *
     * @param theButton the button to style
     */
    private void styleButton(JButton theButton) {
        theButton.setForeground(Color.WHITE);
        theButton.setFocusPainted(false);
        theButton.setBorderPainted(true);
        theButton.setContentAreaFilled(false);
        theButton.setOpaque(false);
    }

    /**
     * Updates button positions based on whether Resume is visible.
     *
     * @param theShowResume true if resume button should be shown
     */
    private void updateButtonLayout(boolean theShowResume) {
        int buttonX = (int)(screenWidth/2 - (screenWidth/7.68));
        int spacing = (int)(screenHeight/10.8);
        int baseY = (int)(screenHeight/2 - spacing);

        playButton.setBounds(buttonX, baseY, buttonWidth, buttonHeight);

        if (theShowResume) {
            resumeButton.setBounds(buttonX, baseY + spacing, buttonWidth, buttonHeight);
            resumeButton.setVisible(true);
            settingsButton.setBounds(buttonX, baseY + spacing * 2, buttonWidth, buttonHeight);
            exitButton.setBounds(buttonX, baseY + spacing * 3, buttonWidth, buttonHeight);
        } else {
            resumeButton.setVisible(false);
            settingsButton.setBounds(buttonX, baseY + spacing, buttonWidth, buttonHeight);
            exitButton.setBounds(buttonX, baseY + spacing * 2, buttonWidth, buttonHeight);
        }

        repaint();
    }

    /**
     * Refreshes the visibility of the Resume button based on whether a save file exists.
     * Should be called whenever returning to the main menu.
     */
    public void refreshResumeButton() {
        updateButtonLayout(SaveManager.saveFileExists());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundMenuImage, 0, 0, getWidth(), getHeight(), this);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 80));

        // Draw backgrounds only for visible buttons
        drawButtonBackground(g2, playButton);
        if (resumeButton.isVisible()) {
            drawButtonBackground(g2, resumeButton);
        }
        drawButtonBackground(g2, settingsButton);
        drawButtonBackground(g2, exitButton);

        g2.dispose();
    }

    /**
     * Draws a rounded rectangle background behind a button.
     *
     * @param g2     the graphics context
     * @param button the button to draw behind
     */
    private void drawButtonBackground(Graphics2D g2, JButton button) {
        g2.fillRoundRect(button.getX(), button.getY(), button.getWidth(), button.getHeight(), 20, 20);
    }

    public void addExitListener(ActionListener theListener) {
        exitButton.addActionListener(theListener);
    }

    public void addSettingsListener(ActionListener theListener) {
        settingsButton.addActionListener(theListener);
    }

    public void addPlayListener(ActionListener theListener) {
        playButton.addActionListener(theListener);
    }

    public void addResumeListener(ActionListener theListener) {
        resumeButton.addActionListener(theListener);
    }

    public void setDarkMode(boolean theDarkModeSelected) {
        String path = theDarkModeSelected
                ? "src/images/Night-Mode1.2.png"
                : "src/images/Day-Mode1.2.png";
        backgroundMenuImage = new ImageIcon(path).getImage();
        repaint();
    }
}