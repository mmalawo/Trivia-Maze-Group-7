package view;

import javax.swing.*;
import java.awt.*;

public class SettingsView {
    public SettingsView(){
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(3, 2, 10, 10));

        // settingsPanel.pack(); // Causes this window to be sized to fit preferred size in gamepanel
        // settingsPanel.setLocationRelativeTo(null); // Puts it at the center
        settingsPanel.setVisible(true);
    }

}

// Full Screen / Windowed
// Sound Volume / Toggled
// Brightness? / Dark mode?

        // Initialize window
       // window.setResizable(false);
       // window.setUndecorated(false); // For Fullscreen mode, make 'true'


        // Show menuView
      //  GameMenuView menuView = new GameMenuView();
      //  MenuController menuController = new MenuController(menuView);
      //  window.add(menuView);



