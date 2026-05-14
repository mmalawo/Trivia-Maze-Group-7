package view;

import model.Player;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.*;

public class PlayerSetupView extends JPanel{
    private final JPanel playerPanel;
    public static Image setupViewBackground;
    private final JButton backToMenu;
    private final JTextField namePrompt;
    private final JLabel nameLabel;

    private final JButton nextSlide;
    private final JButton prevSlide;

    public static ImageIcon character1;
    public static ImageIcon character2;


    public static int slide = 1;
    private Timer butterflyAnimation;
    private JLabel butterflyIcon;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    double screenWidth = screenSize.getWidth();
    double screenHeight = screenSize.getHeight();

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

        namePrompt.addActionListener(e -> {
            String name = namePrompt.getText();
            MainGUI.player.setName(name);
            System.out.println(MainGUI.player.getName());
        });

        nextSlide = new JButton("Next");
        nextSlide.setBounds((int)(screenWidth/2+(screenWidth/9.6)), (int)(screenHeight/2-(screenHeight/3.6)), 70, 40);
        add(nextSlide);

        prevSlide = new JButton("Previous");
        prevSlide.setBounds((int)(screenWidth/2-(screenWidth/6.4)), (int)(screenHeight/2-(screenHeight/3.6)), 70, 40);
        add(prevSlide);

        butterflyIcon = new JLabel(character1);
        butterflyIcon.setBounds((int)(screenWidth/2 - (screenWidth/9.6)),(int)(screenHeight/2-(screenHeight/2.7)),400,400);

        add(butterflyIcon);
        updateCharacter();
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


        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRoundRect((int)(screenWidth/2-screenWidth/(2.25)), (int)(screenHeight/2-screenHeight/(2.35)), 200, 50, 20,20);
        g2.fillRoundRect((int)(screenWidth/2 - (screenWidth/19.2)),(int)(screenHeight/2 + (screenHeight/5.68)), 200, 50, 20,20);
    }

    public static int getSlide() {
        return slide;
    }
    public static void setSlide(int n) {
        if (n < 1 || n > 2) {
            return;
        } else {
            slide = n;
        }
    }

    public void addBackListener(ActionListener theListener) {
        backToMenu.addActionListener(theListener);
    }
    public void addNextListener(ActionListener theListener) {
        nextSlide.addActionListener(theListener);
    }

    public void updateCharacter() {
        if(slide == 1) {
            character1 = new ImageIcon("src/images/PurpleFlap.png");
            character2 = new ImageIcon("src/images/PurpleUnflap.png");
        } else if(slide == 2) {
            character1 = new ImageIcon("src/images/BlueFlap.png");
            character2 = new ImageIcon("src/images/BlueUnflap.png");
        }
        butterflyIcon.setIcon(character1);
    }
    public void addPrevListener(ActionListener theListener) {
        prevSlide.addActionListener(theListener);
    }
}
