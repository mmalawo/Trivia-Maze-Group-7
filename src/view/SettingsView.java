package view;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * SettingsView provides the user interface for configuring
 * application settings such as fullscreen mode, volume,
 * and day/night mode preferences.
 */
public class SettingsView extends JPanel {
    //private SettingsView settingsView;
    //private JPanel settingsPanel;
    private JToggleButton screenButton;
    private JSlider volumeSlider;
    private JCheckBox darkModeCheck;
    private JButton backToMenu;

    private JLabel nightDayMode;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    double screenWidth = screenSize.getWidth();
    double screenHeight = screenSize.getHeight();

    private Image background;

    /**
     * Constructs the settings screen and initializes all UI components,
     * including controls for fullscreen mode, volume, dark mode,
     * and navigation back to the main menu.
     */
    public SettingsView() {
        setLayout(null);

        ImageIcon nightSettingsBackground = new ImageIcon("src/images/Day-Settings.png");
        background = nightSettingsBackground.getImage();

        screenButton = new JToggleButton("On/Off", false);
        screenButton.setBounds((int)screenWidth/2-(int)screenWidth/16, (int)(screenHeight/2-screenHeight/(3.72)), 300, 50);
        screenButton.setForeground(Color.WHITE);
        screenButton.setFocusPainted(false);
        screenButton.setBorderPainted(true);
        screenButton.setContentAreaFilled(false);
        screenButton.setOpaque(false);
        add(screenButton);


        volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setBounds((int)(screenWidth/2 - (screenWidth/16)),(int)(screenHeight/2 - (screenHeight/6.75)), 300, 50);
        volumeSlider.setForeground(Color.WHITE);
        volumeSlider.setOpaque(false);
        add(volumeSlider);


        darkModeCheck = new JCheckBox();
        darkModeCheck.setBounds((int)(screenWidth/2-(screenWidth/19.2)), (int)(screenHeight/2-(screenHeight/30.857)), 30, 30);
        darkModeCheck.setForeground(Color.WHITE);
        darkModeCheck.setFocusPainted(false);
        darkModeCheck.setBorderPainted(false);
        darkModeCheck.setContentAreaFilled(false);
        darkModeCheck.setOpaque(false);
        add(darkModeCheck);


        nightDayMode = new JLabel("Day/Night Mode");
        nightDayMode.setBounds((int)(screenWidth/2-(screenWidth/27.43)), (int)(screenHeight/2-(screenHeight/30.857)), 150, 30);
        nightDayMode.setForeground(Color.WHITE);
        nightDayMode.setFont(new Font("Arial", Font.BOLD, 12));
        add(nightDayMode);


        backToMenu = new JButton("<--Back--");
        backToMenu.setBounds((int)(screenWidth/2 - (screenWidth/3.69)),(int)(screenHeight/2 - (screenHeight/2.7)), 200, 50);
        backToMenu.setForeground(Color.WHITE);
        backToMenu.setFocusPainted(false);
        backToMenu.setBorderPainted(true);
        backToMenu.setContentAreaFilled(false);
        backToMenu.setOpaque(false);
        add(backToMenu);






        // TESTING PURPOSES ONLY
        // Prints coords when clicked
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();  // X coordinate of click
                int y = e.getY();  // Y coordinate of click
                System.out.println("Clicked at: (" + x + ", " + y + ")");
            }
        });




    }

    /**
     * Paints the settings screen background and decorative UI elements.
     *
     * @param g the graphics context used for rendering
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(0, 0, 0, 80));
            g2.fillRoundRect((int)(screenWidth/2 - (screenWidth/3.69)),(int)(screenHeight/2 - (screenHeight/2.7)), 200, 50, 20,20);
            g2.fillRoundRect((int)screenWidth/2-(int)screenWidth/16, (int)(screenHeight/2-screenHeight/(3.72)), 300 , 50, 20,20);
            g2.fillRoundRect((int)(screenWidth/2 - (screenWidth/16)),(int)(screenHeight/2 - (screenHeight/6.75)), 300 , 50, 20,20);
            g2.fillRoundRect((int)(screenWidth/2-(screenWidth/16)), (int)(screenHeight/2-(screenHeight/24)), 300, 50, 20, 20);

        }
    }






    /**
     * Adds a listener to the Back button.
     *
     * @param theListener the listener to invoke when the button is clicked
     */
    public void addBackListener(ActionListener theListener) {
        backToMenu.addActionListener(theListener);
    }

    /**
     * Adds a listener to the fullscreen toggle button.
     *
     * @param theListener the listener to invoke when the button state changes
     */
    public void addFullscreenListener(ActionListener theListener) {
        screenButton.addActionListener(theListener);
    }

    /**
     * Adds a listener to the volume slider.
     *
     * @param theListener the listener to invoke when the slider value changes
     */
    public void addVolumeListener(ChangeListener theListener) {
        volumeSlider.addChangeListener(theListener);
    }

    /**
     * Adds a listener to the day/night mode checkbox.
     *
     * @param theListener the listener to invoke when the checkbox is clicked
     */
    public void addDarkModeListener(ActionListener theListener) {
        darkModeCheck.addActionListener(theListener);
    }

    /**
     * Determines whether fullscreen mode is currently selected.
     *
     * @return true if fullscreen mode is enabled; false otherwise
     */
    public boolean isFullscreenSelected() {
        return screenButton.isSelected();
    }

    /**
     * Determines whether dark mode is currently selected.
     *
     * @return true if dark mode is enabled; false otherwise
     */
    public boolean isDarkModeSelected() {
        return darkModeCheck.isSelected();
    }

    /**
     * Returns the current volume slider value.
     *
     * @return the volume level between 0 and 100
     */
    public int getVolumeValue() {
        return volumeSlider.getValue();
    }

    /**
     * Updates the application's visual assets and backgrounds
     * to match the selected theme mode.
     *
     * <p>This method updates images used by the settings screen,
     * game menu, player setup screen, and maze view before
     * repainting the interface.</p>
     *
     * @param darkMode true to apply the dark theme; false to apply the light theme
     */
    public void setDarkMode(boolean darkMode) {
        String backgroundPath = darkMode
                ? "src/images/Night-Settings.png"
                : "src/images/Day-Settings.png";
        background = new ImageIcon(backgroundPath).getImage();
        repaint();
    }




}
