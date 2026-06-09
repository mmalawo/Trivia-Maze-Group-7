package view;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
        String backgroundPath = darkMode
                ? "src/images/Night-Settings.png"
                : "src/images/Day-Settings.png";
        background = new ImageIcon(backgroundPath).getImage();
        repaint();
    }




}
