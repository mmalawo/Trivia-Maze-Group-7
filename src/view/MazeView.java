package view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import model.*;
import model.Question;
import model.QuestionDAO;
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

    // Player position in maze grid
    private int playerRow;
    private int playerCol;

    // Room info label for dev purposes
    private JLabel coordLabel;

    // Butterfly animation toggle
    private boolean butterflyToggle = false;

    // Zoom scale
    private float scale = 1.0f;

    public MazeView(Maze maze) {
        this.maze = maze;

        // Find player starting position
        int[] startPos = maze.findRoom(MainGUI.player.getCurrentRoom());
        playerRow = startPos[0];
        playerCol = startPos[1];

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

        // butterfly flap animation timer
        new Timer(300, e -> {
            butterflyToggle = !butterflyToggle;
        }).start();

        setLayout(null);
        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        setFocusable(true);

        ImageIcon background = new ImageIcon("src/images/DayGrass.png");
        mazeGrass = background.getImage();

        playerPanel = new JPanel();
        playerPanel.setOpaque(false);
        add(playerPanel);

        // Room info label
        coordLabel = new JLabel(getRoomInfo());
        coordLabel.setBounds(10, 10, 600, 30);
        coordLabel.setForeground(Color.WHITE);
        coordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(coordLabel);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                System.out.println("Clicked at: (" + x + ", " + y + ")");
            }
        });

        // Zoom with scroll wheel
        addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                scale = Math.min(scale + 0.1f, 2.0f);
            } else {
                scale = Math.max(scale - 0.1f, 0.3f);
            }
        });
    }

    public JPanel getPlayerPanel() {
        return playerPanel;
    }

    private String getRoomInfo() {
        Room r = maze.getRoom(playerRow, playerCol);
        return "(" + playerCol + "," + playerRow + ") | " +
                "N:" + (r.getNorthDoor().isLocked() ? "LOCKED" : "OPEN") + " " +
                "S:" + (r.getSouthDoor().isLocked() ? "LOCKED" : "OPEN") + " " +
                "E:" + (r.getEastDoor().isLocked() ? "LOCKED" : "OPEN") + " " +
                "W:" + (r.getWestDoor().isLocked() ? "LOCKED" : "OPEN");
    }

    private float worldCenterX(int col) {
        return col * 770 + 770 / 2f;
    }

    private float worldCenterY(int row) {
        return row * 500 + 500 / 2f;
    }

    private boolean isValidMove(String direction) {
        switch (direction) {
            case "north" -> { return playerRow > 0; }
            case "south" -> { return playerRow < maze.getRows() - 1; }
            case "west"  -> { return playerCol > 0; }
            case "east"  -> { return playerCol < maze.getCols() - 1; }
        }
        return false;
    }

    private void tryMove(String direction) {
        if (!isValidMove(direction)) {
            System.out.println("Can't move that way - wall!");
            return;
        }

        Room currentRoom = maze.getRoom(playerRow, playerCol);
        Door door = null;
        int newRow = playerRow;
        int newCol = playerCol;

        switch (direction) {
            case "north" -> { door = currentRoom.getNorthDoor(); newRow = playerRow - 1; }
            case "south" -> { door = currentRoom.getSouthDoor(); newRow = playerRow + 1; }
            case "west"  -> { door = currentRoom.getWestDoor();  newCol = playerCol - 1; }
            case "east"  -> { door = currentRoom.getEastDoor();  newCol = playerCol + 1; }
        }

        if (door == null) return;

        if (!door.isLocked()) {
            playerRow = newRow;
            playerCol = newCol;
            MainGUI.player.setCurrentRoom(maze.getRoom(playerRow, playerCol));
            coordLabel.setText(getRoomInfo());
            System.out.println("Moved to room [" + playerRow + "][" + playerCol + "]");
        } else {
            Question q = door.getQuestion();
            if (q != null) {
                TriviaPopup popup = new TriviaPopup(q);
                popup.setVisible(true);
                if (popup.isAnsweredCorrectly()) {
                    door.setLocked(false);
                    playerRow = newRow;
                    playerCol = newCol;
                    MainGUI.player.setCurrentRoom(maze.getRoom(playerRow, playerCol));
                    coordLabel.setText(getRoomInfo());
                    JOptionPane.showMessageDialog(MainGUI.window, "Correct! Door unlocked!", "Result", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("Correct! Moved to room [" + playerRow + "][" + playerCol + "]");
                } else {
                    JOptionPane.showMessageDialog(MainGUI.window, "Wrong! Door stays locked.", "Result", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Wrong! Staying in room [" + playerRow + "][" + playerCol + "]");
                }
            }
        }
    }

    private void setupButtons() {
        // Angie's camera pan buttons - kept as is
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

        // Player movement buttons (temporary)
        JButton moveNorth = new JButton("N");
        JButton moveSouth = new JButton("S");
        JButton moveWest = new JButton("W");
        JButton moveEast = new JButton("E");

        moveNorth.setBounds(200, 500, 60, 60);
        moveSouth.setBounds(200, 620, 60, 60);
        moveWest.setBounds(140, 560, 60, 60);
        moveEast.setBounds(260, 560, 60, 60);

        add(moveNorth);
        add(moveSouth);
        add(moveWest);
        add(moveEast);

        moveNorth.addActionListener(e -> tryMove("north"));
        moveSouth.addActionListener(e -> tryMove("south"));
        moveWest.addActionListener(e -> tryMove("west"));
        moveEast.addActionListener(e -> tryMove("east"));
    }

    private void updateCamera() {
        int stepX = (int)(770 * scale);
        int stepY = (int)(500 * scale);

        if(up) { targetCamY -= stepY; up = false; }
        if(down) { targetCamY += stepY; down = false; }
        if(left) { targetCamX -= stepX; left = false; }
        if(right) { targetCamX += stepX; right = false; }

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

        int roomW = (int)(900 * scale);
        int roomH = (int)(900 * scale);
        int stepX = (int)(770 * scale);
        int stepY = (int)(500 * scale);

        for(int r = 1; r <= rows; r++) {
            for(int c = 1; c <= cols; c++) {
                int centerRow = (rows + 1)/2;
                int centerCol = (cols + 1)/2;

                int x = stepX * (centerCol - c);
                int y = stepY * (centerRow - r);

                int screenX = (int)(screenWidth/2 - (450 * scale + x) - camX);
                int screenY = (int)(screenHeight/2 - y - camY);

                g.drawImage(hedgeTest.getImage(), screenX, screenY, roomW, roomH, this);
            }
        }

        // Draw butterfly at player's current room
        int centerRow = (rows + 1) / 2;
        int centerCol = (cols + 1) / 2;

        int px = stepX * (centerCol - (playerCol + 1));
        int py = stepY * (centerRow - (playerRow + 1));

        int playerScreenX = (int)(screenWidth/2 - (450 * scale + px) - camX) + (int)(385 * scale);
        int playerScreenY = (int)(screenHeight/2 - py - camY) + (int)(250 * scale);

        int bfSize = (int)(150 * scale);

        ImageIcon butterfly = butterflyToggle ?
                PlayerSetupView.character2 :
                PlayerSetupView.character1;

        if (butterfly != null && butterfly.getImage() != null) {
            g.drawImage(butterfly.getImage(), playerScreenX, playerScreenY, bfSize, bfSize, this);
        }
    }
}