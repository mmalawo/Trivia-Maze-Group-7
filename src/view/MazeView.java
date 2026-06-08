package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import controller.GameController;
import model.*;

/**
 * MazeView is responsible for rendering the maze, rooms, doors, and player.
 * All game logic (movement validation, trivia, win/lose) is delegated to GameController.
 */
public class MazeView extends JPanel {

    private final JPanel playerPanel;

    private int startRow;
    private int startCol;

    // Background image
    public static Image mazeGrass;

    // normal hedge and shady hedge icons (rooms)
    public static ImageIcon hedgeTest = new ImageIcon("src/images/Hedge900-675.png");
    public static ImageIcon shadyHedge = new ImageIcon("src/images/ShadyHedge.png");

    public static ImageIcon unlockedDoor = new ImageIcon("src/images/UnlockedHedge.png");

    // Unlocked doors (open path)
    public static ImageIcon eastDoorImage = new ImageIcon("src/images/UnlockedHedge.png");
    public static ImageIcon northDoorImage = new ImageIcon("src/images/NorthDoorUnlocked.png");
    public static ImageIcon southDoorImage = new ImageIcon("src/images/NorthDoorUnlocked.png");
    public static ImageIcon westDoorImage = new ImageIcon("src/images/UnlockedHedge.png");

    // Possible pathways (to unlock)
    public static ImageIcon eastDoorImageLocked = new ImageIcon("src/images/EastDoorLocked.png");
    public static ImageIcon northDoorImageLocked = new ImageIcon("src/images/NorthDoorLocked.png");
    public static ImageIcon southDoorImageLocked = new ImageIcon("src/images/SouthDoorLocked.png");
    public static ImageIcon westDoorImageLocked = new ImageIcon("src/images/WestDoorLocked.png");

    public static ImageIcon character1;
    public static ImageIcon character2;

    private final Maze myMaze;
    private GameController myController;

    private float camX;
    private float camY;
    private float targetCamX;
    private float targetCamY;

    // Player position in maze grid
    private int playerRow;
    private int playerCol;

    private float renderRow;
    private float renderCol;

    // Room info label
    private JLabel coordLabel;

    private boolean butterflyToggle = false;

    // Zoom scale (fixed - no zoom in/out)
    private final float scale = 1.0f;

    // Dynamic scaling base resolution based on original design size
    private static final double BASE_WIDTH = 1536.0;
    private static final double BASE_HEIGHT = 1024.0;

    public static JLabel timerLabel;

    public MazeView(Maze theMaze, GameController theController) {
        this.myMaze = theMaze;
        this.myController = theController;

        // Find player starting position
        int[] startPos = myMaze.findRoom(MainGUI.player.getCurrentRoom());
        playerRow = startPos[0];
        playerCol = startPos[1];

        myMaze.getRoom(playerRow, playerCol).setVisited(true);

        camX = 0;
        camY = 0;
        targetCamX = camX;
        targetCamY = camY;

        renderRow = playerRow;
        renderCol = playerCol;

        startRow = startPos[0];
        startCol = startPos[1];

        setupButtons();

        // game loop
        new Timer(16, e -> {
            updateCamera();
            updatePlayerAnimation();
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
                System.out.println("Clicked at: (" + e.getX() + ", " + e.getY() + ")");
            }
        });

        addTimer();
    }

    // =====================================================
    // GETTERS FOR CONTROLLER
    // =====================================================

    /** Returns the player's current row in the maze grid. */
    public int getPlayerRow() {
        return playerRow;
    }

    /** Returns the player's current column in the maze grid. */
    public int getPlayerCol() {
        return playerCol;
    }

    // =====================================================
    // PLAYER MOVEMENT (called by GameController)
    // =====================================================

    /**
     * Moves the player to the given row/col and updates the camera and room info.
     */
    public void movePlayer(int theNewRow, int theNewCol) {
        playerRow = theNewRow;
        playerCol = theNewCol;

        Room currentRoom = myMaze.getRoom(playerRow, playerCol);
        currentRoom.setVisited(true);

        MainGUI.player.setCurrentRoom(currentRoom);

        coordLabel.setText(getRoomInfo());

        System.out.println("Moved to room [" + playerRow + "][" + playerCol + "]");
    }

    // =====================================================
    // ANIMATION
    // =====================================================

    private void updatePlayerAnimation() {
        float speed = 0.02f;
        renderRow += (playerRow - renderRow) * speed;
        renderCol += (playerCol - renderCol) * speed;
    }

    public JPanel getPlayerPanel() {
        return playerPanel;
    }

    // =====================================================
    // SCALING
    // =====================================================

    private double getUIScale() {
        double scaleX = getWidth() / BASE_WIDTH;
        double scaleY = getHeight() / BASE_HEIGHT;
        return Math.min(scaleX, scaleY);
    }

    private double getWorldScale() {
        return getUIScale() * scale;
    }

    private int scaled(double value) {
        return (int)(value * getWorldScale());
    }

    // =====================================================
    // ROOM INFO
    // =====================================================

    private String getRoomInfo() {
        Room r = myMaze.getRoom(playerRow, playerCol);
        return "(" + playerCol + "," + playerRow + ") | " +
                "N:" + (r.getNorthDoor().isLocked() ? "LOCKED" : "OPEN") + " " +
                "S:" + (r.getSouthDoor().isLocked() ? "LOCKED" : "OPEN") + " " +
                "E:" + (r.getEastDoor().isLocked() ? "LOCKED" : "OPEN") + " " +
                "W:" + (r.getWestDoor().isLocked() ? "LOCKED" : "OPEN");
    }

    // =====================================================
    // BUTTONS
    // =====================================================

    private void setupButtons() {
        JButton hintButton = new JButton("Hint");
        hintButton.setBounds(50, 120, 100, 40);
        add(hintButton);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screen.width;
        int screenHeight = screen.height;

        ImageIcon northIcon = new ImageIcon("src/images/NorthArrow2.png");
        ImageIcon northClicked = new ImageIcon("src/images/NorthArrowClicked2.png");

        ImageIcon southIcon = new ImageIcon("src/images/SouthArrow.png");
        ImageIcon southClicked = new ImageIcon("src/images/SouthArrowClicked.png");

        ImageIcon westIcon = new ImageIcon("src/images/WestArrow.png");
        ImageIcon westClicked = new ImageIcon("src/images/WestArrowClicked.png");

        ImageIcon eastIcon = new ImageIcon("src/images/EastArrow.png");
        ImageIcon eastClicked = new ImageIcon("src/images/EastArrowClicked.png");

        JButton moveNorth = new JButton(northIcon);
        JButton moveSouth = new JButton(southIcon);
        JButton moveWest = new JButton(westIcon);
        JButton moveEast = new JButton(eastIcon);

        JButton[] buttons = { moveNorth, moveSouth, moveWest, moveEast };
        for (JButton button : buttons) {
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.setFocusPainted(false);
            button.setOpaque(false);
        }

        moveNorth.setBounds((screenWidth*6)/7, (screenHeight)/3+50, 150, 150);
        moveSouth.setBounds((screenWidth*6)/7, (screenHeight)/2+75, 150, 150);
        moveWest.setBounds((screenWidth*6)/7-100, screenHeight/2 - 75, 150, 150);
        moveEast.setBounds((screenWidth*6)/7+102, screenHeight/2 - 75, 150, 150);

        add(moveNorth);
        add(moveSouth);
        add(moveWest);
        add(moveEast);

        moveNorth.addActionListener(e -> {
            moveNorth.setIcon(northClicked);
            myController.handleMove("north");
            new javax.swing.Timer(150, evt -> {
                moveNorth.setIcon(northIcon);
                ((javax.swing.Timer) evt.getSource()).stop();
            }).start();
        });
        moveSouth.addActionListener(e -> {
            moveSouth.setIcon(southClicked);
            myController.handleMove("south");
            new javax.swing.Timer(150, evt -> {
                moveSouth.setIcon(southIcon);
                ((javax.swing.Timer) evt.getSource()).stop();
            }).start();
        });
        moveWest.addActionListener(e -> {
            moveWest.setIcon(westClicked);
            myController.handleMove("west");
            new javax.swing.Timer(150, evt -> {
                moveWest.setIcon(westIcon);
                ((javax.swing.Timer) evt.getSource()).stop();
            }).start();
        });
        moveEast.addActionListener(e -> {
            moveEast.setIcon(eastClicked);
            myController.handleMove("east");
            new javax.swing.Timer(150, evt -> {
                moveEast.setIcon(eastIcon);
                ((javax.swing.Timer) evt.getSource()).stop();
            }).start();
        });
        hintButton.addActionListener(e -> myController.showHint(playerRow, playerCol));
    }

    // =====================================================
    // CAMERA
    // =====================================================

    private void updateCamera() {
        int stepX = scaled(770);
        int stepY = scaled(500);

        int centerRow = (myMaze.getRows() + 1) / 2;
        int centerCol = (myMaze.getCols() + 1) / 2;

        float px = stepX * (centerCol - (renderCol + 1));
        float py = stepY * (centerRow - (renderRow + 1));

        targetCamX = -(scaled(450) + px) + scaled(385);
        targetCamY = -py + scaled(400);

        float smooth = 0.08f;

        camX += (targetCamX - camX) * smooth;
        camY += (targetCamY - camY) * smooth;
    }

    // =====================================================
    // PAINTING
    // =====================================================

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        g.drawImage(mazeGrass, 0, 0, panelWidth, panelHeight, this);

        Graphics2D g2 = (Graphics2D) g.create();

        int rows = myMaze.getRows();
        int cols = myMaze.getCols();

        int roomW = scaled(900);
        int roomH = scaled(900);

        int stepX = scaled(770);
        int stepY = scaled(500);

        int centerRow = (rows + 1) / 2;
        int centerCol = (cols + 1) / 2;

        int doorWidth = scaled(165);
        int doorHeight = scaled(220);

        // PASS 1: UNVISITED (BOTTOM LAYER)
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                int rowIndex = r - 1;
                int colIndex = c - 1;
                Room room = myMaze.getRoom(rowIndex, colIndex);
                if (room.isVisited()) continue;
                drawRoom(g, g2, room, rowIndex, colIndex,
                        panelWidth, panelHeight, stepX, stepY,
                        centerRow, centerCol, roomW, roomH, doorWidth, doorHeight);
            }
        }

        // PASS 2: VISITED (TOP LAYER)
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                int newRoomW = scaled(770 * 1.2);
                int newRoomH = scaled(500 * 1.4);
                int rowIndex = r - 1;
                int colIndex = c - 1;
                Room room = myMaze.getRoom(rowIndex, colIndex);
                if (!room.isVisited()) continue;
                drawRoom(g, g2, room, rowIndex, colIndex,
                        panelWidth, panelHeight, stepX, stepY,
                        centerRow, centerCol, newRoomW, newRoomH, doorWidth, doorHeight);
            }
        }

        // PLAYER (TOPMOST)
        float px = stepX * (centerCol - (renderCol + 1));
        float py = stepY * (centerRow - (renderRow + 1));

        int playerScreenX = (int)(panelWidth / 2 - (scaled(450) + px) - camX) + scaled(385);
        int playerScreenY = (int)(panelHeight / 2 - py - camY) + scaled(250);

        ImageIcon butterfly = butterflyToggle
                ? PlayerSetupView.character2
                : PlayerSetupView.character1;

        drawSprite(g, butterfly, playerScreenX, playerScreenY, 150, 150);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screen.width;

        ImageIcon original = new ImageIcon("src/images/Compass.png");
        Image scaledImage = original.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        ImageIcon compass = new ImageIcon(scaledImage);
        g.drawImage(compass.getImage(), (screenWidth*6)/7, (screen.height*1)/2 - 53, 150, 150, this);

        g2.dispose();
    }

    private void drawSprite(Graphics g, ImageIcon sprite, int x, int y,
                            int windowWidth, int windowHeight) {
        int spriteWidth = scaled(windowWidth);
        int spriteHeight = scaled(windowHeight);
        g.drawImage(sprite.getImage(), x, y, spriteWidth, spriteHeight, this);
    }

    private void drawDoor(Graphics2D g2, Door door, String direction,
                          int x, int y, int w, int h) {
        ImageIcon doorIcon;

        if (door.isPermanentlyClosed()) {
            doorIcon = null;
        } else {
            switch (direction) {
                case "north" -> doorIcon = door.isLocked() ? northDoorImageLocked : northDoorImage;
                case "south" -> doorIcon = door.isLocked() ? southDoorImageLocked : southDoorImage;
                case "east"  -> doorIcon = door.isLocked() ? eastDoorImageLocked  : eastDoorImage;
                case "west"  -> doorIcon = door.isLocked() ? westDoorImageLocked  : westDoorImage;
                default      -> doorIcon = unlockedDoor;
            }
        }

        if (doorIcon == null) return;
        g2.drawImage(doorIcon.getImage(), x, y, w, h, this);
    }

    private void drawRoom(Graphics g, Graphics2D g2, Room room,
                          int rowIndex, int colIndex,
                          int panelWidth, int panelHeight,
                          int stepX, int stepY,
                          int centerRow, int centerCol,
                          int roomW, int roomH,
                          int doorWidth, int doorHeight) {

        int r = rowIndex + 1;
        int c = colIndex + 1;

        int x = stepX * (centerCol - c);
        int y = stepY * (centerRow - r);

        int screenX = (int)(panelWidth / 2 - (scaled(450) + x) - camX);
        int screenY = (int)(panelHeight / 2 - y - camY);

        int newRoomW = scaled(770 * 1.2);
        int newRoomH = scaled(500 * 1.4);

        int roomCenterX = screenX + newRoomW / 2;
        int roomCenterY = screenY + newRoomH / 2;

        Image img = room.isVisited() ? hedgeTest.getImage() : shadyHedge.getImage();
        g.drawImage(img, screenX, screenY, roomW, roomH, this);

        drawDoor(g2, room.getNorthDoor(), "north",
                roomCenterX - doorHeight/2, screenY + scaled(20), doorHeight, doorWidth);
        drawDoor(g2, room.getEastDoor(), "east",
                screenX + (newRoomW - doorWidth/2)-30, roomCenterY - doorHeight/2, doorWidth, doorHeight);
        drawDoor(g2, room.getSouthDoor(), "south",
                roomCenterX - doorHeight/2, screenY + newRoomH - doorWidth, doorHeight, doorWidth);
        drawDoor(g2, room.getWestDoor(), "west",
                screenX - scaled(5), roomCenterY - doorHeight/2, doorWidth, doorHeight);
    }

    // =====================================================
    // RESET METHODS
    // =====================================================

    public void resetVisitedRooms() {
        for (int r = 0; r < myMaze.getRows(); r++) {
            for (int c = 0; c < myMaze.getCols(); c++) {
                myMaze.getRoom(r, c).setVisited(false);
            }
        }
        myMaze.getRoom(playerRow, playerCol).setVisited(true);
        repaint();
    }

    public void resetPlayer() {
        playerRow = startRow;
        playerCol = startCol;
        renderRow = startRow;
        renderCol = startCol;
        targetCamX = 0;
        targetCamY = 0;
        camX = targetCamX;
        camY = targetCamY;
        repaint();
    }

    public void resetDoors() {
        for (int r = 0; r < myMaze.getRows(); r++) {
            for (int c = 0; c < myMaze.getCols(); c++) {
                Room room = myMaze.getRoom(r, c);
                room.getNorthDoor().reset();
                room.getSouthDoor().reset();
                room.getEastDoor().reset();
                room.getWestDoor().reset();
            }
        }
    }

    public void resetGame() {
        for (int r = 0; r < myMaze.getRows(); r++) {
            for (int c = 0; c < myMaze.getCols(); c++) {
                Room room = myMaze.getRoom(r, c);
                room.setVisited(false);
                room.getNorthDoor().reset();
                room.getSouthDoor().reset();
                room.getEastDoor().reset();
                room.getWestDoor().reset();
            }
        }
        playerRow = startRow;
        playerCol = startCol;
        renderRow = startRow;
        renderCol = startCol;
        myMaze.getRoom(startRow, startCol).setVisited(true);
        camX = 0;
        camY = 0;
        targetCamX = camX;
        targetCamY = camY;
        repaint();
    }

    // =====================================================
    // TIMER
    // =====================================================

    public void addTimer() {
        timerLabel = new JLabel("Time: 0");
        timerLabel.setBounds(50, 50, 200, 40);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(Color.WHITE);
        this.add(timerLabel);
    }

    public void updateTimer(double time) {
        int totalSeconds = (int) time;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        timerLabel.setText("Time: " + String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }
}