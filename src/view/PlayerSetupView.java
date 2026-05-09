package view;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class PlayerSetupView extends JPanel{
    private JPanel playerPanel;

    public PlayerSetupView() {
        playerPanel = new JPanel();
        playerPanel.setPreferredSize(new Dimension(50, 50));
        add(playerPanel);
    }

    public JPanel getPlayerPanel(){
        return playerPanel;
    }

    /*public void addFullscreenListener(ActionListener theListener) {
        screenButton.addActionListener(theListener);
    } */

}
