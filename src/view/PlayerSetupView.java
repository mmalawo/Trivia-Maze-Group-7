package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.*;

/**
 * Represents the player setup screen displayed before gameplay begins.
 *
 * <p>This view allows the player to enter a name, select an avatar,
 * navigate back to the main menu, continue to the instructions screen,
 * and preview the selected avatar animation.</p>
 */
public class PlayerSetupView extends JPanel{
    private final JPanel myPlayerPanel;
    private Image mySetupViewBackground;
    private final JButton myBackToMenu;
    private final JButton myNextToGame;
    private final JTextField myNamePrompt;
    private final JLabel myNameLabel;

    private final JButton myNextSlide;
    private final JButton myPrevSlide;

    private ImageIcon myFirstCharacter;
    private ImageIcon mySecondCharacter;

    private int mySlide = 1;
    private boolean myDarkModeSelected;
    private Timer myButterflyAnimation;
    private JLabel myButterflyIcon;

    private final Dimension myScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private final double myScreenWidth = myScreenSize.getWidth();
    private final double myScreenHeight = myScreenSize.getHeight();

    /**
     * Constructs the player setup view.
     *
     * <p>This initializes the background image, name input field, avatar
     * selection controls, navigation buttons, character preview animation,
     * and debugging input listeners.</p>
     */
    public PlayerSetupView() {
        setLayout(null);
        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        setFocusable(true);

        ImageIcon background = new ImageIcon("src/images/Day-PlayerSetup.png");
        mySetupViewBackground = background.getImage();

        myPlayerPanel = new JPanel();

        myBackToMenu = new JButton("<--Back--");
        myBackToMenu.setBounds((int)(myScreenWidth /2- myScreenWidth /(2.25)), (int)(myScreenHeight /2- myScreenHeight /(2.35)), 200, 50);
        myBackToMenu.setForeground(Color.WHITE);
        myBackToMenu.setFocusPainted(false);
        myBackToMenu.setBorderPainted(true);
        myBackToMenu.setContentAreaFilled(false);
        myBackToMenu.setOpaque(false);
        add(myBackToMenu);

        myNextToGame = new JButton("--Next-->");
        myNextToGame.setBounds(
                (int)(myScreenWidth - 300),
                (int)(myScreenHeight / 2 - myScreenHeight / 2.35),
                200,
                50
        );
        myNextToGame.setForeground(Color.WHITE);
        myNextToGame.setFocusPainted(false);
        myNextToGame.setBorderPainted(true);
        myNextToGame.setContentAreaFilled(false);
        myNextToGame.setOpaque(false);
        add(myNextToGame);

        myNameLabel = new JLabel("Type player's name:");
        myNameLabel.setBounds((int)(myScreenWidth /2-(myScreenWidth /19.2)), (int)(myScreenHeight /2+(myScreenHeight /6.75)), 200, 30);
        //nameLabel.setForeground(Color.WHITE);
        myNameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(myNameLabel);

        myNamePrompt = new JTextField();
        myNamePrompt.setBounds((int)(myScreenWidth /2-(myScreenWidth /25.6)), (int)(myScreenHeight /2+(myScreenHeight /5.4)), 150, 30);
        myNamePrompt.setOpaque(false);
        myNamePrompt.setBackground(new Color(0, 0, 0, 120));
        myNamePrompt.setForeground(Color.WHITE);
        //namePrompt.setCaretColor(Color.WHITE);
        myNamePrompt.setBorder(BorderFactory.createEmptyBorder());

        add(myNamePrompt);

        myNextSlide = new JButton("Next");
        myNextSlide.setBounds((int)(myScreenWidth /2+(myScreenWidth /9.6)), (int)(myScreenHeight /2-(myScreenHeight /3.6)), 70, 40);
        add(myNextSlide);

        myPrevSlide = new JButton("Previous");
        myPrevSlide.setBounds((int)(myScreenWidth /2-(myScreenWidth /6.4)), (int)(myScreenHeight /2-(myScreenHeight /3.6)), 70, 40);
        add(myPrevSlide);





        myButterflyIcon = new JLabel(myFirstCharacter);
        myButterflyIcon.setBounds((int)(myScreenWidth /2 - (myScreenWidth /9.6)),(int)(myScreenHeight /2-(myScreenHeight /2.7)),400,400);


        updateCharacter(false);
        add(myButterflyIcon);
        Timer butterflyAnimation = new Timer(300, new ActionListener() {
            private boolean toggle = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if(toggle) {
                    myButterflyIcon.setIcon(myFirstCharacter);
                } else {
                    myButterflyIcon.setIcon(mySecondCharacter);
                }
                toggle = !toggle;
            }
        });
        butterflyAnimation.start();


        //playerPanel.setPreferredSize(new Dimension(50, 50));
        myPlayerPanel.setOpaque(false);
        add(myPlayerPanel);

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


    /**
     * Paints the player setup background and decorative UI elements.
     *
     * @param theGraphics the graphics context used for rendering
     */
    @Override
    protected void paintComponent(Graphics theGraphics) {
        super.paintComponent(theGraphics);
        theGraphics.drawImage(mySetupViewBackground, 0, 0, getWidth(), getHeight(), this);


        Graphics2D g2 = (Graphics2D) theGraphics.create();
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRoundRect((int)(myScreenWidth /2- myScreenWidth /(2.25)), (int)(myScreenHeight /2- myScreenHeight /(2.35)), 200, 50, 20,20);
        // Next button background
        g2.fillRoundRect((int)(myScreenWidth - 300), (int)(myScreenHeight / 2 - myScreenHeight / 2.35), 200, 50, 20, 20);
        g2.fillRoundRect((int)(myScreenWidth /2 - (myScreenWidth /19.2)),(int)(myScreenHeight /2 + (myScreenHeight /5.68)), 200, 50, 20,20);
    }

    /**
     * Retrieves the name entered by the player.
     *
     * @return the trimmed player name entered in the text field
     */
    public String getEnteredName() {
        return myNamePrompt.getText().trim();
    }

    /**
     * Advances the avatar selection to the next available character.
     */
    public void nextCharacter() {
        setSlide(mySlide + 1);
    }

    /**
     * Moves the avatar selection to the previous available character.
     */
    public void previousCharacter() {
        setSlide(mySlide - 1);
    }

  /**
   * Sets the current avatar slide number if it is within
   * the valid range of available slides.
   *
   * @param theSlide the slide number to select
   */
    private void setSlide(final int theSlide) {
        if (theSlide >= 1 && theSlide <= 3) {
            mySlide = theSlide;
        }
    }
  
   /**
    * Returns the currently displayed butterfly flap animation icon.
    *
    * @return the flap animation icon
    */
    public ImageIcon getCurrentFlapIcon() {
        return myFirstCharacter;
    }

   /**
    * Returns the currently displayed butterfly unflap animation icon.
    *
    * @return the unflap animation icon
    */
    public ImageIcon getCurrentUnflapIcon() {
        return mySecondCharacter;
    }

   /**
    * Applies the selected theme to the player setup screen.
    *
    * <p>Updates the background image, refreshes the character
    * preview images, and repaints the view.</p>
    *
    * @param theDarkModeSelected {@code true} to enable dark mode;
    *                            {@code false} to enable light mode
    */
    public void setDarkMode(final boolean theDarkModeSelected) {
        myDarkModeSelected = theDarkModeSelected;
        String backgroundPath = myDarkModeSelected
                ? "src/images/Night-PlayerSetup.png"
                : "src/images/Day-PlayerSetup.png";
        mySetupViewBackground = new ImageIcon(backgroundPath).getImage();
        updateCharacter(myDarkModeSelected);
        repaint();
    }
    
   /**
    * Registers a listener for the Back button.
    *
    * @param theListener the listener to invoke when the Back button is clicked
    */
    public void addBackListener(final ActionListener theListener) {
        myBackToMenu.addActionListener(theListener);
    }

    /**
     * Adds a listener to the Next button used to continue to the game.
     *
     * @param theListener the listener to invoke when the button is clicked
     */
    public void addNextListener(final ActionListener theListener) {
        myNextToGame.addActionListener(theListener);
    }

    /**
     * Adds a listener to the Next Avatar button.
     *
     * @param theListener the listener to invoke when the button is clicked
     */
    public void addNextAvatarListener(final ActionListener theListener) {
        myNextSlide.addActionListener(theListener);
    }

    /**
     * Updates the displayed character images based on the selected avatar
     * and current theme mode.
     *
     * @param theDarkModeSelected true if dark mode assets should be used;
     *                            false for standard assets
     */
    public void updateCharacter(final boolean theDarkModeSelected) {
        if(theDarkModeSelected) {
            if (mySlide == 1) {
                myFirstCharacter = new ImageIcon("src/images/NightMagentaFlap.png");
                mySecondCharacter = new ImageIcon("src/images/NightMagentaUnflap.png");
            } else if (mySlide == 2) {
                myFirstCharacter = new ImageIcon("src/images/NightBlueFlap.png");
                mySecondCharacter = new ImageIcon("src/images/NightBlueUnflap.png");
            } else if (mySlide == 3) {
                myFirstCharacter = new ImageIcon("src/images/NightAuburnFlap.png");
                mySecondCharacter = new ImageIcon("src/images/NightAuburnUnflap.png");
            }
            myButterflyIcon.setIcon(myFirstCharacter);
        } else {
            if (mySlide == 1) {
                myFirstCharacter = new ImageIcon("src/images/MagentaFlap.png");
                mySecondCharacter = new ImageIcon("src/images/MagentaUnflap.png");
            } else if (mySlide == 2) {
                myFirstCharacter = new ImageIcon("src/images/BlueFlap.png");
                mySecondCharacter = new ImageIcon("src/images/BlueUnflap.png");
            } else if (mySlide == 3) {
                myFirstCharacter = new ImageIcon("src/images/AuburnFlap.png");
                mySecondCharacter = new ImageIcon("src/images/AuburnUnflap.png");
            }
            myButterflyIcon.setIcon(myFirstCharacter);
        }
    }

   /**
    * Adds a listener to the Previous Avatar button.
    *
    * @param theListener the listener to invoke when the button is clicked
    */
    public void addPrevListener(ActionListener theListener) {
        myPrevSlide.addActionListener(theListener);
    }

    /**
    * Resets the player setup screen to its default state.
    *
    * <p>Clears the entered player name, resets the avatar
    * selection to the first slide, and applies the specified
    * theme.</p>
    *
    * @param theDarkModeSelected {@code true} to apply dark mode;
    *                            {@code false} to apply light mode
    */
    public void reset(final boolean theDarkModeSelected) {
        myNamePrompt.setText("");
        mySlide = 1;
        setDarkMode(theDarkModeSelected);
    }
}
