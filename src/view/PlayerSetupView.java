package view;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.*;

public class PlayerSetupView extends JPanel{
    private final JPanel playerPanel;
    private final Image setupViewBackground;

    public PlayerSetupView() {
        setLayout(null);
        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        setFocusable(true);

        ImageIcon background = new ImageIcon("src/images/PlayerSetupFlowers.png");
        setupViewBackground = background.getImage();

        playerPanel = new JPanel();
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
