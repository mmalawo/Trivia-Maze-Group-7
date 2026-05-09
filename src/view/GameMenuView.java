package view;

import java.util.*;
import java.io.*;
import javax.swing.*;   // for JFrame
import java.awt.*;
import java.awt.event.*;  // Action Listener and events

import static view.MainGUI.window;


public class GameMenuView extends JPanel {


    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    double screenWidth = screenSize.getWidth();
    double screenHeight = screenSize.getHeight();

    // Background Image
    private final Image backgroundMenuImage;

    // Button Initialization --------------------------------
    private final JButton exitButton;
    private final JButton settingsButton;
    private final JButton playButton;

    int buttonWidth = 400;
    int buttonHeight = 60;
    // ------------------------------------------------------

    public GameMenuView() {
        this.setPreferredSize(new Dimension((int)screenWidth, (int)screenHeight));

        ImageIcon background = new ImageIcon("C:/Users/Angie/Desktop/GitHub/Official GitHub Project/Trivia-Maze-Group-7/src/images/Night-Mode.png");
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
        exitButton.setBounds((int)screenWidth/2 - 250, (int)screenHeight/2 + 100, buttonWidth, buttonHeight);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(true);
        exitButton.setContentAreaFilled(false);
        exitButton.setOpaque(false);
        this.add(exitButton);

        // Settings button
        settingsButton = new JButton("Settings");
        settingsButton.setBounds((int)screenWidth/2 - 250, (int)screenHeight/2, buttonWidth, buttonHeight);
        settingsButton.setForeground(Color.WHITE);
        settingsButton.setFocusPainted(false);
        settingsButton.setBorderPainted(true);
        settingsButton.setContentAreaFilled(false);
        settingsButton.setOpaque(false);
        this.add(settingsButton);

        // Play button
        playButton = new JButton("Play Game");
        playButton.setBounds((int)screenWidth/2 - 250, (int)screenHeight/2 - 100, buttonWidth, buttonHeight);
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setBorderPainted(true);
        playButton.setContentAreaFilled(false);
        playButton.setOpaque(false);
        this.add(playButton);
        // -------------------------------------------------------

        addMenubar();
        addTimer();
        // -------------------------------------------------------

    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundMenuImage, 0, 0, getWidth(), getHeight(), this);

        // exitButton background
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRoundRect((int)screenWidth/2 - 250,(int)screenHeight/2 + 100, buttonWidth, buttonHeight, 20,20);
        g2.fillRoundRect((int)screenWidth/2 - 250,(int)screenHeight/2, buttonWidth, buttonHeight, 20,20);
        g2.fillRoundRect((int)screenWidth/2 - 250,(int)screenHeight/2 - 100, buttonWidth, buttonHeight, 20,20);

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




    // ----------------------------------------------------------------------
    // THIS CODE IS FOR A GAME BAR THAT GOES ON TOP OF THE SCREEN
    //
    // I PUT IT HERE FOR NOW BUT IT'LL BE ON THE ACTUAL MAZE GAME VIEW.
    // ----------------------------------------------------------------------


    public static JMenuItem itemRestartGame;
    public static JMenuItem itemSettings;
    public static JMenuItem itemExitGame;

    public static void addMenubar() {

        // The actual bar at the top
        JMenuBar menuBar = new JMenuBar();

        // "Game" drop down menu
        JMenu gameMenu  = new JMenu("Game");

        // _____________________________________________________
        // Options you can click in the dropdown menu of Game.
        // _____________________________________________________
        itemRestartGame = new JMenuItem("Restart Game");
        // Shortcut to restart game with keyboard
        itemRestartGame.setAccelerator(KeyStroke.getKeyStroke("control R"));

        itemExitGame = new JMenuItem("Exit");
        itemExitGame.setAccelerator(KeyStroke.getKeyStroke("control E"));

        itemSettings = new JMenuItem("Settings");
        // CAN ADD A SHORTCUT TO SETTINGS HERE IF WE WANT

        gameMenu.add(itemRestartGame);
        gameMenu.addSeparator();
        gameMenu.add(itemExitGame);
        gameMenu.addSeparator();
        gameMenu.add(itemSettings);

        menuBar.add(gameMenu);

        // CHANGE THIS TO BE THE FRAME OR PANEL OF THE GAME VIEW
        window.setJMenuBar(menuBar);

        itemExitGame.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to exit?", "Error",
                    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE) == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });


        itemSettings.addActionListener(e -> {
            window.getContentPane().removeAll();
            SettingsView settingsView = new SettingsView();
            window.add(settingsView.create());
            window.revalidate();
            window.repaint();
        });



    }

    public static JLabel timerLabel;
public void addTimer() {
    // ----------------------------------------------------------------
    // THIS CODE IS FOR TESTING PURPOSES. ADD IT TO THE MAZE GAME VIEW
    // ----------------------------------------------------------------


    timerLabel = new JLabel("Time: 0");
    timerLabel.setBounds(50,50,200,40);
    timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
    this.add(timerLabel);
}
public void updateTimer(double time) {
    int totalSeconds = (int) time;
    int hours = totalSeconds / 3600;
    int minutes = (totalSeconds % 3600) / 60;
    int seconds = totalSeconds % 60;


    timerLabel.setText("Time: " + String.format("%02d:%02d:%02d", hours, minutes, seconds));
}



}
