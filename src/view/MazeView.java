package view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import model.*;
import view.*;
import controller.*;

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




    private final Maze maze;

    private float camX;
    private float camY;
    private float targetCamX;
    private float targetCamY;
    private boolean up, down, left, right;
    private final float manualSpeed = 25f;



    public MazeView(Maze maze) {
        this.maze = maze;

    //    int rows = maze.getRows();
     //   int cols = maze.getCols();

     //   int[] start = maze.findRoom(MainGUI.player.getCurrentRoom());

        camX = (float)screenWidth/2 - 930;
        camY = (float)screenHeight/2 - 100;


        targetCamX = camX;
        targetCamY = camY;

        setupButtons();

        // game loop
        new Timer(16, e -> {
            updateCamera();
            repaint();
        }).start();

        setLayout(null);
        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        setFocusable(true);

        ImageIcon background = new ImageIcon("src/images/DayGrass.png");
        mazeGrass = background.getImage();

        playerPanel = new JPanel();

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

    }

    public JPanel getPlayerPanel(){
        return playerPanel;
    }


    private float worldCenterX(int col) {
        return col * 770 + 770 / 2f;
    }

    private float worldCenterY(int row) {
        return row * 500 + 500 / 2f;
    }

    private void setupButtons() {

        JButton upB = new JButton("↑");
        JButton downB = new JButton("↓");
        JButton leftB = new JButton("←");
        JButton rightB = new JButton("→");

        upB.setBounds(1100, 500, 60, 60);
        downB.setBounds(1100, 620, 60, 60);
        leftB.setBounds(1040, 560, 60, 60);
        rightB.setBounds(1160, 560, 60, 60);

        add(upB);
        add(downB);
        add(leftB);
        add(rightB);

        // HOLD CONTROLS (THIS is why your old version didn’t work well)
        upB.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { up = true; }
            public void mouseReleased(MouseEvent e) { up = false; }
        });

        downB.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { down = true; }
            public void mouseReleased(MouseEvent e) { down = false; }
        });

        leftB.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { left = true; }
            public void mouseReleased(MouseEvent e) { left = false; }
        });

        rightB.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { right = true; }
            public void mouseReleased(MouseEvent e) { right = false; }
        });
    }

    private void updateCamera() {
/*
        // manual movement changes TARGET (not cam directly)
        if (up) targetCamY -= manualSpeed;
        if (down) targetCamY += manualSpeed;
        if (left) targetCamX -= manualSpeed;
        if (right) targetCamX += manualSpeed;

        // smooth follow
        float smooth = 0.12f;
        camX += (targetCamX - camX) * smooth;
        camY += (targetCamY - camY) * smooth; */

        int stepX = 770;
        int stepY = 500;


        if(up) {
            targetCamY -= stepY;
            up = false;
        }
        if(down) {
            targetCamY += stepY;
            down = false;
        }
        if(left) {
            targetCamX -= stepX;
            left = false;
        }
        if(right) {
            targetCamX += stepX;
            right = false;
        }
        float smooth = 0.02f;
        camX += (targetCamX - camX) * smooth;
        camY += (targetCamY - camY) * smooth;



    }




    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(mazeGrass, 0, 0, getWidth(), getHeight(), this);


        int rows = maze.getRows();
        int cols = maze.getCols();

        for(int r = 1; r <= rows; r++) {
            for(int c = 1; c <= cols; c++) {
                //int x = 770 * (3 - c);
                //int y = 500 * (3 - r);
                int centerRow = (rows + 1)/2;
                int centerCol = (cols + 1)/2;

                int x = 770 * (centerCol - c);
                int y = 500 * (centerRow - r);

                int screenX = (int)(screenWidth/2 - (450 + x) - camX);
                int screenY = (int)(screenHeight/2 - y - camY);

                g.drawImage(hedgeTest.getImage(),
                        screenX, screenY, 900, 900,
                        this);
            }
        }
    }


  //  private void resetMazeView() { }
    // reset the cam view here

// Make a method where if the place has not been visited, make it the shaded hedge

}



