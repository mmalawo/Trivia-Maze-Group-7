package view;

import java.util.*;
import java.io.*;
import javax.swing.*;   // for JFrame
import java.awt.*;
import java.awt.event.*;  // Action Listener and events


public class GameMenuView extends JPanel {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    double screenWidth = screenSize.getWidth();
    double screenHeight = screenSize.getHeight();

    // Button Initialization --------------------------------
    private final JButton exitButton;
    private final JButton settingsButton;
    // ------------------------------------------------------

    public GameMenuView() {
        this.setPreferredSize(new Dimension((int)screenWidth, (int)screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true); // helps performance (rendering)

        this.setLayout(null);

// BUTTONS ----------------------------------------------

        // Button sizes:
        int buttonWidth = 250;
        int buttonHeight = 60;

        // Exit button
        exitButton = new JButton();
        exitButton.setText("Exit Game");
        exitButton.setBounds((int)screenWidth/2 - 75, (int)screenHeight/2 + 100, buttonWidth, buttonHeight);
        this.add(exitButton);

        // Settings button
        settingsButton = new JButton();
        settingsButton.setText("Settings");
        settingsButton.setBounds((int)screenWidth/2 - 75, (int)screenHeight/2, buttonWidth, buttonHeight);
        this.add(settingsButton);
        // -------------------------------------------------------

    }

    public void addExitListener(ActionListener theListener) {
        exitButton.addActionListener(theListener);
    }
    public void addSettingsListener(ActionListener theListener) {
        settingsButton.addActionListener(theListener);
    }

}
