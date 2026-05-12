package view;

import controller.SettingsController;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SettingsView extends JPanel {
    //private SettingsView settingsView;
    private JPanel settingsPanel;
    private JToggleButton screenButton;
    private JSlider volumeSlider;
    private JCheckBox darkModeCheck;
    private JButton backToMenu;

    private JLabel nightDayMode;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    double screenWidth = screenSize.getWidth();
    double screenHeight = screenSize.getHeight();

    private Image background;

    public JPanel create(){
        settingsPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (background != null) {
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);

                }

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRoundRect((int)screenWidth/2 - 520,(int)screenHeight/2 -400, 200, 50, 20,20);
                g2.fillRoundRect((int)screenWidth/2 - 120,(int)screenHeight/2-290, 300 , 50, 20,20);
                g2.fillRoundRect((int)screenWidth/2 - 120,(int)screenHeight/2-160, 300 , 50, 20,20);
                g2.fillRoundRect((int)screenWidth/2-120, (int)screenHeight/2-45, 300, 50, 20, 20);

                // More buttons:
                //g2.fillRoundRect((int)screenWidth/2 - 250,(int)screenHeight/2, buttonWidth, buttonHeight, 20,20);
                //g2.fillRoundRect((int)screenWidth/2 - 250,(int)screenHeight/2 - 100, buttonWidth, buttonHeight, 20,20);

                //g.dispose();
                //super.paintComponent(g2);
            }
        };


        ImageIcon nightSettingsBackground = new ImageIcon("src/images/Day-Settings.png");
        background = nightSettingsBackground.getImage();

        settingsPanel.setLayout(null);

        //settingsPanel.add(new JLabel("Fullscreen"));
        screenButton = new JToggleButton("On/Off", false);
        screenButton.setBounds((int)screenWidth/2-120, (int)screenHeight/2-290, 300, 50);
        screenButton.setForeground(Color.WHITE);
        screenButton.setFocusPainted(false);
        screenButton.setBorderPainted(true);
        screenButton.setContentAreaFilled(false);
        screenButton.setOpaque(false);
        settingsPanel.add(screenButton);

        //settingsPanel.add(new JLabel("Volume"));
        volumeSlider = new JSlider(0, 100, 50);
        volumeSlider.setBounds((int)screenWidth/2-120, (int)screenHeight/2-160, 300, 50);
        volumeSlider.setForeground(Color.WHITE);
        volumeSlider.setOpaque(false);
        settingsPanel.add(volumeSlider);

        darkModeCheck = new JCheckBox();
        darkModeCheck.setBounds((int)screenWidth/2-100, (int)screenHeight/2-35, 30, 30);
        darkModeCheck.setForeground(Color.WHITE);
        darkModeCheck.setFocusPainted(false);
        darkModeCheck.setBorderPainted(false);
        darkModeCheck.setContentAreaFilled(false);
        darkModeCheck.setOpaque(false);
        //settingsPanel.add(new JLabel("Dark Mode"));
        settingsPanel.add(darkModeCheck);

        nightDayMode = new JLabel("Day/Night Mode");
        nightDayMode.setBounds((int)screenWidth/2-70, (int)screenHeight/2-35, 150, 30);
        nightDayMode.setForeground(Color.WHITE);
        nightDayMode.setFont(new Font("Arial", Font.BOLD, 12));
        settingsPanel.add(nightDayMode);



        backToMenu = new JButton("<--Back--");
        backToMenu.setBounds((int)screenWidth/2-520, (int)screenHeight/2-400, 200, 50);
        backToMenu.setForeground(Color.WHITE);
        backToMenu.setFocusPainted(false);
        backToMenu.setBorderPainted(true);
        backToMenu.setContentAreaFilled(false);
        backToMenu.setOpaque(false);
        settingsPanel.add(backToMenu);


        settingsPanel.setOpaque(false);

        // TESTING PURPOSES
        // THIS CODE PRINTS THE COORDINATES THAT YOU CLICK ON THE SCREEN
        settingsPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();  // X coordinate of click
                int y = e.getY();  // Y coordinate of click
                System.out.println("Clicked at: (" + x + ", " + y + ")");
            }
        });





        return settingsPanel;
    }




    public void addBackListener(ActionListener theListener) {
        backToMenu.addActionListener(theListener);
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
        // Settings Screen Background
        if(darkMode) {
            ImageIcon nightSettingsBackground = new ImageIcon("src/images/Night-Settings.png");
            background = nightSettingsBackground.getImage();
        } else {
            ImageIcon daySettingsBackground = new ImageIcon("src/images/Day-Settings.png");
            background = daySettingsBackground.getImage();
        }

        // Game Menu Background
        if(darkMode) {
            ImageIcon nightMenuBackground = new ImageIcon("src/images/Night-Mode1.2.png");
            GameMenuView.backgroundMenuImage = nightMenuBackground.getImage();
        } else {
            ImageIcon dayMenuBackground = new ImageIcon("src/images/Day-Mode1.2.png");
            GameMenuView.backgroundMenuImage = dayMenuBackground.getImage();
        }


        //settingsPanel.setBackground(Color.WHITE);

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
