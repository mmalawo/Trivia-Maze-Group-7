package view;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class SettingsView extends JPanel {
    private JPanel settingsPanel;
    private JToggleButton screenButton;
    private JSlider volumeSlider;
    private JCheckBox darkModeCheck;

    private Image background;

    public JPanel create(){
        settingsPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (background != null) {
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

                }

                //g.dispose();
                //super.paintComponent(g2);
            }
        };

        //settingsPanel.setLayout(new GridLayout(3, 2, 10, 10));

        settingsPanel.add(new JLabel("Fullscreen"));

        screenButton = new JToggleButton("On/Off", false);
        settingsPanel.add(screenButton);
        volumeSlider = new JSlider();
        settingsPanel.add(new JLabel("Volume"));
        settingsPanel.add(volumeSlider);
        darkModeCheck = new JCheckBox();
        settingsPanel.add(new JLabel("Dark Mode"));
        settingsPanel.add(darkModeCheck);
        settingsPanel.setOpaque(false);
        return settingsPanel;
    }

    public void addFullscreenListener(ActionListener theListener) {
        screenButton.addActionListener(theListener);
    }

    public void addVolumeListener(ChangeListener theListener) {
        volumeSlider.addChangeListener(theListener);
    }

    public void addDarkModeListener(ActionListener theListener) {
        darkModeCheck.addActionListener(theListener);
    }

    public boolean isFullscreenSelected() {
        return screenButton.isSelected();
    }

    public boolean isDarkModeSelected() {
        return darkModeCheck.isSelected();
    }

    public int getVolumeValue() {
        return volumeSlider.getValue();
    }

    public void setDarkMode(boolean darkMode) {
        if(darkMode) {
            ImageIcon nightSettingsBackground = new ImageIcon("C:/Users/Angie/Desktop/GitHub/Official GitHub Project/Trivia-Maze-Group-7/src/images/Night-Settings.png");
            background = nightSettingsBackground.getImage();

        } else {
            ImageIcon daySettingsBackground = new ImageIcon("C:/Users/Angie/Desktop/GitHub/Official GitHub Project/Trivia-Maze-Group-7/src/images/Day-Mode.png");
            background = daySettingsBackground.getImage();
        }


        settingsPanel.setBackground(Color.WHITE);

        settingsPanel.setDoubleBuffered(true);
        settingsPanel.repaint();


        //settingsPanel.setLayout(null);




        /*Color background = darkMode ? Color.DARK_GRAY : Color.WHITE;
        Color foreground = darkMode ? Color.WHITE : Color.BLACK;

        settingsPanel.setBackground(background);

        for (Component component : settingsPanel.getComponents()) {
            component.setBackground(background);
            component.setForeground(foreground);
        } */
        //settingsPanel.repaint();
    }




}
/*ImageIcon background = new ImageIcon("C:/Users/Angie/Desktop/GitHub/Official GitHub Project/Trivia-Maze-Group-7/src/view/Background1.1.png");
backgroundMenuImage = background.getImage();

//this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true); // helps performance (rendering)
        this.setLayout(null); */