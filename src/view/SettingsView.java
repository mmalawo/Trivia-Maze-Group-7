package view;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class SettingsView {
    private JPanel settingsPanel;
    private JToggleButton screenButton;
    private JSlider volumeSlider;
    private JCheckBox darkModeCheck;

    public JPanel create(){
        settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(3, 2, 10, 10));

        settingsPanel.add(new JLabel("Fullscreen"));

        screenButton = new JToggleButton("On/Off", false);
        settingsPanel.add(screenButton);
        volumeSlider = new JSlider();
        settingsPanel.add(new JLabel("Volume"));
        settingsPanel.add(volumeSlider);
        darkModeCheck = new JCheckBox();
        settingsPanel.add(new JLabel("Dark Mode"));
        settingsPanel.add(darkModeCheck);
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
        Color background = darkMode ? Color.DARK_GRAY : Color.WHITE;
        Color foreground = darkMode ? Color.WHITE : Color.BLACK;

        settingsPanel.setBackground(background);

        for (Component component : settingsPanel.getComponents()) {
            component.setBackground(background);
            component.setForeground(foreground);
        }
        settingsPanel.repaint();
    }
}
