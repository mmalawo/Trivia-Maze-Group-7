package view;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.*;

public class PlayerSetupView extends JPanel{
    private final JPanel playerPanel;
    private final Image setupViewBackground;
    //private final JButton backToMenu;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    double screenWidth = screenSize.getWidth();
    double screenHeight = screenSize.getHeight();

    public PlayerSetupView() {
        setLayout(null);
        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        setFocusable(true);

        ImageIcon background = new ImageIcon("src/images/PlayerSetupFlowers.png");
        setupViewBackground = background.getImage();

        playerPanel = new JPanel();

        /*backToMenu = new JButton("<--Back--");
        backToMenu.setBounds((int)screenWidth/2-520, (int)screenHeight/2-400, 200, 50);
        backToMenu.setForeground(Color.WHITE);
        backToMenu.setFocusPainted(false);
        backToMenu.setBorderPainted(true);
        backToMenu.setContentAreaFilled(false);
        backToMenu.setOpaque(false);
        playerPanel.add(backToMenu); */

        //playerPanel.setPreferredSize(new Dimension(50, 50));
        playerPanel.setOpaque(false);
        add(playerPanel);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {

                switch (e.getKeyCode()) {

                    case KeyEvent.VK_UP ->
                            System.out.println("UP");

                    case KeyEvent.VK_DOWN ->
                            System.out.println("DOWN");

                    case KeyEvent.VK_LEFT ->
                            System.out.println("LEFT");

                    case KeyEvent.VK_RIGHT ->
                            System.out.println("RIGHT");
                }
            }
        });
    }

    public JPanel getPlayerPanel(){
        return playerPanel;
    }

    /*public void addFullscreenListener(ActionListener theListener) {
        screenButton.addActionListener(theListener);
    } */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(setupViewBackground, 0, 0, getWidth(), getHeight(), this);

    }

}
