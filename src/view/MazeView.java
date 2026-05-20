package view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class MazeView extends JPanel {
    private final JPanel playerPanel;


    public static Image mazeGrass;

    public static ImageIcon hedgeTest = new ImageIcon("src/images/FixedHedge900.png");
    public static ImageIcon shadyHedge = new ImageIcon("src/images/ShadyHedge.png");

    private JLabel hedge;
    private JLabel hedge2;
    private JLabel hedge3;
    private JLabel hedge4;


    public static ImageIcon character1;
    public static ImageIcon character2;


//    private Timer butterflyAnimation;
//    private JLabel butterflyIcon;

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    double screenWidth = screenSize.getWidth();
    double screenHeight = screenSize.getHeight();

    public MazeView() {
        setLayout(null);
        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        setFocusable(true);

        ImageIcon background = new ImageIcon("src/images/DayGrass.png");
        mazeGrass = background.getImage();

        playerPanel = new JPanel();

        hedge = new JLabel(hedgeTest);
        hedge.setBounds((int)(screenWidth/2 - (screenWidth/5)),(int)(screenHeight/2-(screenHeight/3)),900,900);
        add(hedge);

        hedge2 = new JLabel(shadyHedge);
        hedge2.setBounds((int)(screenWidth/2-390),(int)(screenHeight/2+150),900,900);
        add(hedge2);

        hedge3 = new JLabel(shadyHedge);
        hedge3.setBounds((int)(screenWidth/2+390),(int)(screenHeight/2-screenHeight/3),900,900);
        add(hedge3);

        hedge4 = new JLabel(shadyHedge);
        hedge4.setBounds((int)(screenWidth/2+390),(int)(screenHeight/2+150),900,900);
        add(hedge4);






/*

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
        butterflyAnimation.start(); */


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
//
//
//        addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(java.awt.event.KeyEvent e) {
//
//                switch (e.getKeyCode()) {
//
//                    case KeyEvent.VK_UP ->
//                            System.out.println("UP");
//
//                    case KeyEvent.VK_DOWN ->
//                            System.out.println("DOWN");
//
//                    case KeyEvent.VK_LEFT ->
//                            System.out.println("LEFT");
//
//                    case KeyEvent.VK_RIGHT ->
//                            System.out.println("RIGHT");
//                }
//            }
//        });
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
        g.drawImage(mazeGrass, 0, 0, getWidth(), getHeight(), this);


        /*Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRoundRect((int)(screenWidth/2-screenWidth/(2.25)), (int)(screenHeight/2-screenHeight/(2.35)), 200, 50, 20,20);
        // Next button background
        g2.fillRoundRect((int)(screenWidth - 300), (int)(screenHeight / 2 - screenHeight / 2.35), 200, 50, 20, 20);
        g2.fillRoundRect((int)(screenWidth/2 - (screenWidth/19.2)),(int)(screenHeight/2 + (screenHeight/5.68)), 200, 50, 20,20); */
    }



}



