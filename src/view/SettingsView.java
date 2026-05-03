package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class SettingsView {
    private JToggleButton screenButton;
    private JSlider volumeSlider;
    private JCheckBox darkModeCheck;

    public JPanel create(){
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(3, 2, 10, 10));

        int buttonWidth = 250;
        int buttonHeight = 60;

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

}

// Full Screen / Windowed
// Sound Volume / Toggled
// Brightness? / Dark mode?
