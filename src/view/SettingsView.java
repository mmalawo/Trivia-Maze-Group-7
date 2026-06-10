package view;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Represents the settings screen for the application.
 *
 * <p>This view provides controls for fullscreen mode, audio volume,
 * day/night mode, and navigation back to the previous screen.</p>
 */
public class SettingsView extends JPanel {
    private final JToggleButton myScreenButton;
    private final JSlider myVolumeSlider;
    private final JCheckBox myDarkModeCheck;
    private final JButton myBackToMenu;

    private final JLabel myNightDayMode;

    private final Dimension myScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private final double myScreenWidth = myScreenSize.getWidth();
    private final double myScreenHeight = myScreenSize.getHeight();

    private Image myBackground;

    /**
     * Constructs the settings view.
     *
     * <p>This initializes the background image, fullscreen toggle,
     * volume slider, day/night mode checkbox, back button, layout settings,
     * and debugging mouse listener.</p>
     */
    public SettingsView() {
        setLayout(null);

        ImageIcon nightSettingsBackground = new ImageIcon("src/images/Day-Settings.png");
        myBackground = nightSettingsBackground.getImage();

        myScreenButton = new JToggleButton("On/Off", false);
        myScreenButton.setBounds((int) myScreenWidth /2-(int) myScreenWidth /16, (int)(myScreenHeight /2- myScreenHeight /(3.72)), 300, 50);
        myScreenButton.setForeground(Color.WHITE);
        myScreenButton.setFocusPainted(false);
        myScreenButton.setBorderPainted(true);
        myScreenButton.setContentAreaFilled(false);
        myScreenButton.setOpaque(false);
        add(myScreenButton);


        myVolumeSlider = new JSlider(0, 100, 50);
        myVolumeSlider.setBounds((int)(myScreenWidth /2 - (myScreenWidth /16)),(int)(myScreenHeight /2 - (myScreenHeight /6.75)), 300, 50);
        myVolumeSlider.setForeground(Color.WHITE);
        myVolumeSlider.setOpaque(false);
        add(myVolumeSlider);


        myDarkModeCheck = new JCheckBox();
        myDarkModeCheck.setBounds((int)(myScreenWidth /2-(myScreenWidth /19.2)), (int)(myScreenHeight /2-(myScreenHeight /30.857)), 30, 30);
        myDarkModeCheck.setForeground(Color.WHITE);
        myDarkModeCheck.setFocusPainted(false);
        myDarkModeCheck.setBorderPainted(false);
        myDarkModeCheck.setContentAreaFilled(false);
        myDarkModeCheck.setOpaque(false);
        add(myDarkModeCheck);


        myNightDayMode = new JLabel("Day/Night Mode");
        myNightDayMode.setBounds((int)(myScreenWidth /2-(myScreenWidth /27.43)), (int)(myScreenHeight /2-(myScreenHeight /30.857)), 150, 30);
        myNightDayMode.setForeground(Color.WHITE);
        myNightDayMode.setFont(new Font("Arial", Font.BOLD, 12));
        add(myNightDayMode);


        myBackToMenu = new JButton("<--Back--");
        myBackToMenu.setBounds((int)(myScreenWidth /2 - (myScreenWidth /3.69)),(int)(myScreenHeight /2 - (myScreenHeight /2.7)), 200, 50);
        myBackToMenu.setForeground(Color.WHITE);
        myBackToMenu.setFocusPainted(false);
        myBackToMenu.setBorderPainted(true);
        myBackToMenu.setContentAreaFilled(false);
        myBackToMenu.setOpaque(false);
        add(myBackToMenu);
    }

    /**
     * Paints the settings screen background and decorative UI elements.
     *
     * @param theGraphics the graphics context used for painting
     */
    @Override
    protected void paintComponent(Graphics theGraphics) {
        super.paintComponent(theGraphics);

        if (myBackground != null) {
            theGraphics.drawImage(myBackground, 0, 0, getWidth(), getHeight(), this);

            Graphics2D g2 = (Graphics2D) theGraphics.create();
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillRoundRect((int)(myScreenWidth /2 - (myScreenWidth /3.69)),(int)(myScreenHeight /2 - (myScreenHeight /2.7)), 200, 50, 20,20);
            g2.fillRoundRect((int) myScreenWidth /2-(int) myScreenWidth /16, (int)(myScreenHeight /2- myScreenHeight /(3.72)), 300 , 50, 20,20);
            g2.fillRoundRect((int)(myScreenWidth /2 - (myScreenWidth /16)),(int)(myScreenHeight /2 - (myScreenHeight /6.75)), 300 , 50, 20,20);
            g2.fillRoundRect((int)(myScreenWidth /2-(myScreenWidth /16)), (int)(myScreenHeight /2-(myScreenHeight /24)), 300, 50, 20, 20);

        }
    }






    /**
     * Adds a listener to the Back button.
     *
     * @param theListener the listener to invoke when the button is clicked
     */
    public void addBackListener(final ActionListener theListener) {
        myBackToMenu.addActionListener(theListener);
    }

    /**
     * Adds a listener to the fullscreen toggle button.
     *
     * @param theListener the listener to invoke when the button state changes
     */
    public void addFullscreenListener(final ActionListener theListener) {
        myScreenButton.addActionListener(theListener);
    }

    /**
     * Adds a listener to the volume slider.
     *
     * @param theListener the listener to invoke when the slider value changes
     */
    public void addVolumeListener(final ChangeListener theListener) {
        myVolumeSlider.addChangeListener(theListener);
    }

    /**
     * Adds a listener to the day/night mode checkbox.
     *
     * @param theListener the listener to invoke when the checkbox is clicked
     */
    public void addDarkModeListener(final ActionListener theListener) {
        myDarkModeCheck.addActionListener(theListener);
    }

    /**
     * Returns whether fullscreen mode is selected.
     *
     * @return {@code true} if fullscreen mode is selected;
     *         {@code false} otherwise
     */
    public boolean isFullscreenSelected() {
        return myScreenButton.isSelected();
    }

    /**
     * Returns whether dark mode is selected.
     *
     * @return {@code true} if dark mode is selected;
     *         {@code false} otherwise
     */
    public boolean isDarkModeSelected() {
        return myDarkModeCheck.isSelected();
    }

    /**
     * Returns the current volume slider value.
     *
     * @return the volume level between 0 and 100
     */
    public int getVolumeValue() {
        return myVolumeSlider.getValue();
    }

    /**
     * Returns the current volume slider value.
     *
     * @return the volume level from {@code 0} to {@code 100}
     */
    public void setDarkMode(final boolean theDarkModeSelected) {
        String backgroundPath = theDarkModeSelected
                ? "src/images/Night-Settings.png"
                : "src/images/Day-Settings.png";
        myBackground = new ImageIcon(backgroundPath).getImage();
        repaint();
    }




}
