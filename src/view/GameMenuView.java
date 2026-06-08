package view;

import com.sun.tools.javac.Main;

import java.util.*;
import java.io.*;
import javax.swing.*;   // for JFrame
import java.awt.*;
import java.awt.event.*;  // Action Listener and events

import static view.MainGUI.settingsView;
import static view.MainGUI.*;


public class GameMenuView extends JPanel {


    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    double screenWidth = screenSize.getWidth();
    double screenHeight = screenSize.getHeight();

    // Background Image
    public static Image backgroundMenuImage;

    // Button Initialization --------------------------------
    private final JButton exitButton;
    private final JButton settingsButton;
    private final JButton playButton;

    int buttonWidth = 400;
    int buttonHeight = 60;
    // ------------------------------------------------------

    public GameMenuView() {
        this.setPreferredSize(new Dimension((int)screenWidth, (int)screenHeight));

        ImageIcon background = new ImageIcon("src/images/Day-Mode1.2.png");
        backgroundMenuImage = background.getImage();

        //this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true); // helps performance (rendering)
        this.setLayout(null);

        // Print monitor/screen size for debugging
        System.out.println("Screen width: " + screenWidth);
        System.out.println("Screen height: " + screenHeight);

// BUTTONS ----------------------------------------------

        // Button sizes:


        // Exit button
        exitButton = new JButton("Exit Game");
        exitButton.setBounds((int)(screenWidth/2 - (screenWidth/7.68)), (int)(screenHeight/2 + (screenHeight/10.8)), buttonWidth, buttonHeight);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(true);
        exitButton.setContentAreaFilled(false);
        exitButton.setOpaque(false);
        this.add(exitButton);

        // Settings button
        settingsButton = new JButton("Settings");
        settingsButton.setBounds((int)(screenWidth/2 - (screenWidth/7.68)), (int)screenHeight/2, buttonWidth, buttonHeight);
        settingsButton.setForeground(Color.WHITE);
        settingsButton.setFocusPainted(false);
        settingsButton.setBorderPainted(true);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setOpaque(false);
        this.add(settingsButton);

        // Play button
        playButton = new JButton("Play Game");
        playButton.setBounds((int)(screenWidth/2 - (screenWidth/7.68)), (int)(screenHeight/2 - (screenHeight/10.8)), buttonWidth, buttonHeight);
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setBorderPainted(true);
        playButton.setContentAreaFilled(false);
        playButton.setOpaque(false);
        this.add(playButton);
        // -------------------------------------------------------



        // -------------------------------------------------------

    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundMenuImage, 0, 0, getWidth(), getHeight(), this);

        // exitButton background
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRoundRect((int)(screenWidth/2 - (screenWidth/7.68)), (int)(screenHeight/2 + (screenHeight/10.8)), buttonWidth, buttonHeight, 20,20);
        g2.fillRoundRect((int)(screenWidth/2 - (screenWidth/7.68)), (int)screenHeight/2, buttonWidth, buttonHeight, 20,20);
        g2.fillRoundRect((int)(screenWidth/2 - (screenWidth/7.68)), (int)(screenHeight/2 - (screenHeight/10.8)), buttonWidth, buttonHeight, 20,20);

        g2.dispose();
        //super.paintComponent(g2);
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








}
