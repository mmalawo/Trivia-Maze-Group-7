package view;

import model.SaveManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Represents the main menu view of the game.
 *
 * <p>This view displays the main menu background image and provides buttons
 * for starting a new game, resuming a saved game, opening settings, and
 * exiting the application.</p>
 */
public class GameMenuView extends JPanel {

    private final Dimension myScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private final double myScreenWidth = myScreenSize.getWidth();
    private final double myScreenHeight = myScreenSize.getHeight();

    // Background Image
    private Image myBackgroundMenuImage;

    // Button Initialization
    private final JButton myExitButton;
    private final JButton mySettingsButton;
    private final JButton myPlayButton;
    private final JButton myResumeButton;

    private final int myButtonWidth = 400;
    private final int myButtonHeight = 60;

    private int playY;

    /**
     * Constructs the game menu view and initializes all menu buttons,
     * layout settings, and background image.
     */
    public GameMenuView() {
        this.setPreferredSize(new Dimension((int) myScreenWidth, (int) myScreenHeight));

        ImageIcon background = new ImageIcon("src/images/Day-Mode1.2.png");
        myBackgroundMenuImage = background.getImage();

        this.setDoubleBuffered(true);
        this.setLayout(null);

        int buttonX = (int)(myScreenWidth /2 - (myScreenWidth /7.68));
        int spacing = (int)(myScreenHeight /10.8);

        // Calculate base Y position
        playY = (int)(myScreenHeight /2 - spacing);

        // Play button
        myPlayButton = new JButton("Play Game");
        myPlayButton.setBounds(buttonX, playY, myButtonWidth, myButtonHeight);
        styleButton(myPlayButton);
        this.add(myPlayButton);

        // Resume button
        myResumeButton = new JButton("Resume");
        myResumeButton.setBounds(buttonX, playY + spacing, myButtonWidth, myButtonHeight);
        styleButton(myResumeButton);
        myResumeButton.setVisible(false);
        this.add(myResumeButton);

        // Settings button
        mySettingsButton = new JButton("Settings");
        styleButton(mySettingsButton);
        this.add(mySettingsButton);

        // Exit button
        myExitButton = new JButton("Exit Game");
        styleButton(myExitButton);
        this.add(myExitButton);

        // Initial layout without resume
        updateButtonLayout(false);
    }

    /**
     * Styles a button with transparent background and white text.
     *
     * @param theButton the button to style
     */
    private void styleButton(final JButton theButton) {
        theButton.setForeground(Color.WHITE);
        theButton.setFocusPainted(false);
        theButton.setBorderPainted(true);
        theButton.setContentAreaFilled(false);
        theButton.setOpaque(false);
    }

    /**
     * Updates button positions based on whether Resume button is visible.
     *
     * @param theShowResume true if resume button should be shown
     */
    private void updateButtonLayout(final boolean theShowResume) {
        int buttonX = (int)(myScreenWidth /2 - (myScreenWidth /7.68));
        int spacing = (int)(myScreenHeight /10.8);
        int baseY = (int)(myScreenHeight /2 - spacing);

        myPlayButton.setBounds(buttonX, baseY, myButtonWidth, myButtonHeight);

        if (theShowResume) {
            myResumeButton.setBounds(buttonX, baseY + spacing, myButtonWidth, myButtonHeight);
            myResumeButton.setVisible(true);
            mySettingsButton.setBounds(buttonX, baseY + spacing * 2, myButtonWidth, myButtonHeight);
            myExitButton.setBounds(buttonX, baseY + spacing * 3, myButtonWidth, myButtonHeight);
        } else {
            myResumeButton.setVisible(false);
            mySettingsButton.setBounds(buttonX, baseY + spacing, myButtonWidth, myButtonHeight);
            myExitButton.setBounds(buttonX, baseY + spacing * 2, myButtonWidth, myButtonHeight);
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

    /**
     * Paints the main menu background and custom button backgrounds.
     *
     * @param theGraphics the graphics context used for painting
     */
    @Override
    protected void paintComponent(Graphics theGraphics) {
        super.paintComponent(theGraphics);
        theGraphics.drawImage(myBackgroundMenuImage, 0, 0, getWidth(), getHeight(), this);

        Graphics2D g2 = (Graphics2D) theGraphics.create();
        g2.setColor(new Color(0, 0, 0, 80));

        // Draw backgrounds only for visible buttons
        drawButtonBackground(g2, myPlayButton);
        if (myResumeButton.isVisible()) {
            drawButtonBackground(g2, myResumeButton);
        }
        drawButtonBackground(g2, mySettingsButton);
        drawButtonBackground(g2, myExitButton);

        g2.dispose();
    }

    /**
     * Draws a rounded rectangle background behind a button.
     *
     * @param theGraphics     the graphics context
     * @param button the button to draw behind
     */
    private void drawButtonBackground(final Graphics2D theGraphics, final JButton button) {
        theGraphics.fillRoundRect(button.getX(), button.getY(), button.getWidth(), button.getHeight(), 20, 20);
    }

    /**
     * Adds a listener to the Exit Game button.
     *
     * @param theListener the listener to add
     */
    public void addExitListener(final ActionListener theListener) {
        myExitButton.addActionListener(theListener);
    }

    /**
     * Adds a listener to the Settings button.
     *
     * @param theListener the listener to add
     */
    public void addSettingsListener(final ActionListener theListener) {
        mySettingsButton.addActionListener(theListener);
    }


    /**
     * Adds a listener to the Play Game button.
     *
     * @param theListener the listener to add
     */
    public void addPlayListener(final ActionListener theListener) {
        myPlayButton.addActionListener(theListener);
    }

    /**
     * Adds a listener to the Resume button.
     *
     * @param theListener the listener to add
     */
    public void addResumeListener(final ActionListener theListener) {
        myResumeButton.addActionListener(theListener);
    }

    /**
     * Updates the menu background image based on the selected theme.
     *
     * @param theDarkModeSelected {@code true} to use the dark mode background;
     *                            {@code false} to use the light mode background
     */
    public void setDarkMode(final boolean theDarkModeSelected) {
        String path = theDarkModeSelected
                ? "src/images/Night-Mode1.2.png"
                : "src/images/Day-Mode1.2.png";
        myBackgroundMenuImage = new ImageIcon(path).getImage();
        repaint();
    }
}