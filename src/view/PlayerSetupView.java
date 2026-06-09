package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * PlayerSetupView provides the interface for player customization
 * before the game begins. Players can enter a name, select an avatar,
 * and navigate between the menu and game setup screens.
 */
public class PlayerSetupView extends JPanel{
    private final JPanel playerPanel;
    private Image setupViewBackground;
    private final JButton backToMenu;
    private final JButton nextToGame;
    private final JTextField namePrompt;
    private final JLabel nameLabel;

    private final JButton nextSlide;
    private final JButton prevSlide;

    private ImageIcon character1;
    private ImageIcon character2;

    private int slide = 1;
    private boolean darkModeSelected;
    private Timer butterflyAnimation;
    private JLabel butterflyIcon;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    double screenWidth = screenSize.getWidth();
    double screenHeight = screenSize.getHeight();

    /**
     * Constructs the player setup screen and initializes all UI components,
     * including avatar selection controls, name input field, navigation buttons,
     * and character animation.
     */
    public PlayerSetupView() {
        setLayout(null);
        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        setFocusable(true);

        ImageIcon background = new ImageIcon("src/images/Day-PlayerSetup.png");
        setupViewBackground = background.getImage();

        playerPanel = new JPanel();

        backToMenu = new JButton("<--Back--");
        backToMenu.setBounds((int)(screenWidth/2-screenWidth/(2.25)), (int)(screenHeight/2-screenHeight/(2.35)), 200, 50);
        backToMenu.setForeground(Color.WHITE);
        backToMenu.setFocusPainted(false);
        backToMenu.setBorderPainted(true);
        backToMenu.setContentAreaFilled(false);
        backToMenu.setOpaque(false);
        add(backToMenu);

        nextToGame = new JButton("--Next-->");
        nextToGame.setBounds(
                (int)(screenWidth - 300),
                (int)(screenHeight / 2 - screenHeight / 2.35),
                200,
                50
        );
        nextToGame.setForeground(Color.WHITE);
        nextToGame.setFocusPainted(false);
        nextToGame.setBorderPainted(true);
        nextToGame.setContentAreaFilled(false);
        nextToGame.setOpaque(false);
        add(nextToGame);

        nameLabel = new JLabel("Type player's name:");
        nameLabel.setBounds((int)(screenWidth/2-(screenWidth/19.2)), (int)(screenHeight/2+(screenHeight/6.75)), 200, 30);
        //nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(nameLabel);

        namePrompt = new JTextField();
        namePrompt.setBounds((int)(screenWidth/2-(screenWidth/25.6)), (int)(screenHeight/2+(screenHeight/5.4)), 150, 30);
        namePrompt.setOpaque(false);
        namePrompt.setBackground(new Color(0, 0, 0, 120));
        namePrompt.setForeground(Color.WHITE);
        //namePrompt.setCaretColor(Color.WHITE);
        namePrompt.setBorder(BorderFactory.createEmptyBorder());

        add(namePrompt);

        namePrompt.getDocument().addDocumentListener(new DocumentListener() {

            private void updateName() {
                System.out.println(namePrompt.getText());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                updateName();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateName();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateName();
            }
        });

        nextSlide = new JButton("Next");
        nextSlide.setBounds((int)(screenWidth/2+(screenWidth/9.6)), (int)(screenHeight/2-(screenHeight/3.6)), 70, 40);
        add(nextSlide);

        prevSlide = new JButton("Previous");
        prevSlide.setBounds((int)(screenWidth/2-(screenWidth/6.4)), (int)(screenHeight/2-(screenHeight/3.6)), 70, 40);
        add(prevSlide);





        butterflyIcon = new JLabel(character1);
        butterflyIcon.setBounds((int)(screenWidth/2 - (screenWidth/9.6)),(int)(screenHeight/2-(screenHeight/2.7)),400,400);


        updateCharacter(false);
        add(butterflyIcon);
        Timer butterflyAnimation = new Timer(300, new ActionListener() {
            private boolean toggle = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                if(toggle) {
                    butterflyIcon.setIcon(character1);
                } else {
                    butterflyIcon.setIcon(character2);
                }
                toggle = !toggle;
            }
        });
        butterflyAnimation.start();


        //playerPanel.setPreferredSize(new Dimension(50, 50));
        playerPanel.setOpaque(false);
        add(playerPanel);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println("Clicked at: (" + x + ", " + y + ")");
            }
        });


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
     * Returns the panel used to display the player's avatar.
     *
     * @return the player panel
     */
    public JPanel getPlayerPanel(){
        return playerPanel;
    }


    /**
     * Paints the player setup background and decorative UI elements.
     *
     * @param g the graphics context used for rendering
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(setupViewBackground, 0, 0, getWidth(), getHeight(), this);


        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRoundRect((int)(screenWidth/2-screenWidth/(2.25)), (int)(screenHeight/2-screenHeight/(2.35)), 200, 50, 20,20);
        // Next button background
        g2.fillRoundRect((int)(screenWidth - 300), (int)(screenHeight / 2 - screenHeight / 2.35), 200, 50, 20, 20);
        g2.fillRoundRect((int)(screenWidth/2 - (screenWidth/19.2)),(int)(screenHeight/2 + (screenHeight/5.68)), 200, 50, 20,20);
    }

    /**
     * Retrieves the name entered by the player.
     *
     * @return the trimmed player name entered in the text field
     */
    public String getEnteredName() {
        return namePrompt.getText().trim();
    }

    /**
     * Advances the avatar selection to the next available character.
     */
    public void nextCharacter() {
        setSlide(slide + 1);
    }

    /**
     * Moves the avatar selection to the previous available character.
     */
    public void previousCharacter() {
        setSlide(slide - 1);
    }

  /**
   * Returns the currently selected avatar slide number.
   *
   * @return the current slide number
   */
    public int getSlide() {
        return slide;
    }

  /**
   * Sets the current avatar slide number if it is within
   * the valid range of available slides.
   *
   * @param n the slide number to select
   */
    private void setSlide(int n) {
        if (n >= 1 && n <= 3) {
            slide = n;
        }
    }
  
   /**
    * Returns the currently displayed butterfly flap animation icon.
    *
    * @return the flap animation icon
    */
    public ImageIcon getCurrentFlapIcon() {
        return character1;
    }

   /**
    * Returns the currently displayed butterfly unflap animation icon.
    *
    * @return the unflap animation icon
    */
    public ImageIcon getCurrentUnflapIcon() {
        return character2;
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
    public void setDarkMode(boolean theDarkModeSelected) {
        darkModeSelected = theDarkModeSelected;
        String backgroundPath = darkModeSelected
                ? "src/images/Night-PlayerSetup.png"
                : "src/images/Day-PlayerSetup.png";
        setupViewBackground = new ImageIcon(backgroundPath).getImage();
        updateCharacter(darkModeSelected);
        repaint();
    }
    
   /**
    * Registers a listener for the Back button.
    *
    * @param theListener the listener to invoke when the Back button is clicked
    */
    public void addBackListener(ActionListener theListener) {
        backToMenu.addActionListener(theListener);
    }

    /**
     * Adds a listener to the Next button used to continue to the game.
     *
     * @param theListener the listener to invoke when the button is clicked
     */
    public void addNextListener(ActionListener theListener) {
        nextToGame.addActionListener(theListener);
    }

    /**
     * Adds a listener to the Next Avatar button.
     *
     * @param theListener the listener to invoke when the button is clicked
     */
    public void addNextAvatarListener(ActionListener theListener) {
        nextSlide.addActionListener(theListener);
    }

    /**
     * Updates the displayed character images based on the selected avatar
     * and current theme mode.
     *
     * @param theDarkModeSelected true if dark mode assets should be used;
     *                            false for standard assets
     */
    public void updateCharacter(boolean theDarkModeSelected) {
        if(theDarkModeSelected) {
            if (slide == 1) {
                character1 = new ImageIcon("src/images/NightMagentaFlap.png");
                character2 = new ImageIcon("src/images/NightMagentaUnflap.png");
            } else if (slide == 2) {
                character1 = new ImageIcon("src/images/NightBlueFlap.png");
                character2 = new ImageIcon("src/images/NightBlueUnflap.png");
            } else if (slide == 3) {
                character1 = new ImageIcon("src/images/NightAuburnFlap.png");
                character2 = new ImageIcon("src/images/NightAuburnUnflap.png");
            }
            butterflyIcon.setIcon(character1);
        } else {
            if (slide == 1) {
                character1 = new ImageIcon("src/images/MagentaFlap.png");
                character2 = new ImageIcon("src/images/MagentaUnflap.png");
            } else if (slide == 2) {
                character1 = new ImageIcon("src/images/BlueFlap.png");
                character2 = new ImageIcon("src/images/BlueUnflap.png");
            } else if (slide == 3) {
                character1 = new ImageIcon("src/images/AuburnFlap.png");
                character2 = new ImageIcon("src/images/AuburnUnflap.png");
            }
            butterflyIcon.setIcon(character1);
        }
    }

   /**
    * Adds a listener to the Previous Avatar button.
    *
    * @param theListener the listener to invoke when the button is clicked
    */
    public void addPrevListener(ActionListener theListener) {
        prevSlide.addActionListener(theListener);
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
    public void reset(boolean theDarkModeSelected) {
        namePrompt.setText("");
        slide = 1;
        setDarkMode(theDarkModeSelected);
    }
}
