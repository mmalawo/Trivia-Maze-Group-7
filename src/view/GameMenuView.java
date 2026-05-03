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

    private JButton exitButton;

    public GameMenuView() {
        this.setPreferredSize(new Dimension((int)screenWidth, (int)screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true); // helps performance (rendering)

        this.setLayout(null);

        // Exit button
        exitButton = new JButton();
        exitButton.setText("Exit Game");
        exitButton.setBounds((int)screenWidth/2 - 75, (int)screenHeight/2 - 15, 150, 30); // Adjust size later, this puts the button in the middle of screen
        this.add(exitButton);
    }

    public void addExitListener(ActionListener theListener) {
        exitButton.addActionListener(theListener);
    }

}
