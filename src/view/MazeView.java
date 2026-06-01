package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import model.*;

public class MazeView extends JPanel {

    private final JPanel playerPanel;

    private int startRow;
    private int startCol;

    // Background image
    public static Image mazeGrass;

    // normal hedge and shady hedge icons (rooms)
    public static ImageIcon hedgeTest = new ImageIcon("src/images/Hedge900-675.png");
    public static ImageIcon shadyHedge = new ImageIcon("src/images/ShadyHedge.png");

    public static ImageIcon lockedDoor = new ImageIcon("src/images/UnlockedHedge.png");
    public static ImageIcon unlockedDoor = new ImageIcon("src/images/UnlockedHedge.png");
    public static ImageIcon permanentlyLockedDoor = new ImageIcon("src/images/UnlockedHedge.png"); // swap image later

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

    private final Maze maze;

    private float camX;
    private float camY;
    private float targetCamX;
    private float targetCamY;

    private boolean up, down, left, right;

    // Player position in maze grid
    private int playerRow;
    private int playerCol;

    private float renderRow;
    private float renderCol;

    // Room info label for dev purposes
    private JLabel coordLabel;

    private boolean butterflyToggle = false;

    // Zoom scale
    private float scale = 1.0f;

    // Dynamic scaling base resolution based on original design size
    private static final double BASE_WIDTH = 1536.0;
    private static final double BASE_HEIGHT = 1024.0;

    public MazeView(Maze maze) {
        this.maze = maze;

        // Find player starting position
        int[] startPos = maze.findRoom(MainGUI.player.getCurrentRoom());
        playerRow = startPos[0];
        playerCol = startPos[1];

        maze.getRoom(playerRow, playerCol).setVisited(true);

        camX = 0;
        camY = 400;
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

        // Zoom with scroll wheel
        addMouseWheelListener(e -> {
            if (e.getWheelRotation() < 0) {
                scale = Math.min(scale + 0.1f, 2.0f);
            } else {
                scale = Math.max(scale - 0.1f, 0.3f);
            }
        });
    }

    private void updatePlayerAnimation() {
        float speed = 0.02f;

        renderRow += (playerRow - renderRow) * speed;
        renderCol += (playerCol - renderCol) * speed;
    }

    public JPanel getPlayerPanel() {
        return playerPanel;
    }

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

    private String getRoomInfo() {
        Room r = maze.getRoom(playerRow, playerCol);
        return "(" + playerCol + "," + playerRow + ") | " +
                "N:" + (r.getNorthDoor().isLocked() ? "LOCKED" : "OPEN") + " " +
                "S:" + (r.getSouthDoor().isLocked() ? "LOCKED" : "OPEN") + " " +
                "E:" + (r.getEastDoor().isLocked() ? "LOCKED" : "OPEN") + " " +
                "W:" + (r.getWestDoor().isLocked() ? "LOCKED" : "OPEN");
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
            case "north" -> {
                door = currentRoom.getNorthDoor();
                newRow = playerRow - 1;
            }
            case "south" -> {
                door = currentRoom.getSouthDoor();
                newRow = playerRow + 1;
            }
            case "west" -> {
                door = currentRoom.getWestDoor();
                newCol = playerCol - 1;
            }
            case "east" -> {
                door = currentRoom.getEastDoor();
                newCol = playerCol + 1;
            }
        }

        if (door == null) return;

        // Permanently closed - no more attempts allowed
        if (door.isPermanentlyClosed()) {
            JOptionPane.showMessageDialog(
                    MainGUI.window,
                    "This door is permanently locked!",
                    "Blocked",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Already unlocked - free passage
        if (!door.isLocked()) {
            movePlayer(newRow, newCol);
        } else {
            Question q = door.getQuestion();

            if (q != null) {
                TriviaPopup popup = new TriviaPopup(q);
                popup.setVisible(true);

                String playerAnswer = popup.getPlayerAnswer();
                boolean correct = door.attemptAnswer(playerAnswer);

                if (correct) {
                    // Mark this question as correctly answered so it won't repeat
                    QuestionDAO.markAsCorrectlyAnswered(q);
                    movePlayer(newRow, newCol);
                    JOptionPane.showMessageDialog(
                            MainGUI.window,
                            "Correct! Door unlocked!",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    String attemptsMsg = door.isPermanentlyClosed()
                            ? "Wrong! This door is now permanently locked."
                            : "Wrong! " + door.getAttemptsRemaining() + " attempt(s) remaining.";

                    JOptionPane.showMessageDialog(
                            MainGUI.window,
                            attemptsMsg,
                            "Result",
                            JOptionPane.ERROR_MESSAGE
                    );

                    System.out.println("Wrong! Staying in room [" + playerRow + "][" + playerCol + "]");
                }
            }
        }
    }

    private void movePlayer(int newRow, int newCol) {
        int rowDiff = newRow - playerRow;
        int colDiff = newCol - playerCol;

        playerRow = newRow;
        playerCol = newCol;

        int stepX = scaled(770);
        int stepY = scaled(500);

        targetCamX += colDiff * stepX;
        targetCamY += rowDiff * stepY;

        Room currentRoom = maze.getRoom(playerRow, playerCol);
        currentRoom.setVisited(true);


        MainGUI.player.setCurrentRoom(currentRoom);

        coordLabel.setText(getRoomInfo());

        System.out.println("Moved to room [" + playerRow + "][" + playerCol + "]");
    }

    private void drawSprite(Graphics g,
                            ImageIcon sprite,
                            int x,
                            int y,
                            int windowWidth,
                            int windowHeight) {

        int spriteWidth = scaled(windowWidth);
        int spriteHeight = scaled(windowHeight);

        g.drawImage(sprite.getImage(), x, y, spriteWidth, spriteHeight, this);
    }

    private void drawDoor(Graphics2D g2,
                          Door door,
                          String direction,
                          int x,
                          int y,
                          int w,
                          int h) {

        ImageIcon doorIcon;

        if(door.isPermanentlyClosed()) {
            doorIcon = null;
        } else {
            switch (direction) {
                case "north" -> {
                    if (door.isLocked()) {
                        doorIcon = northDoorImageLocked;
                    } else {
                        doorIcon = northDoorImage;
                    }
                }

                case "south" -> {
                    if (door.isLocked()) {
                        doorIcon = southDoorImageLocked;
                    } else {
                        doorIcon = southDoorImage;
                    }
                }

                case "east" -> {
                    if (door.isLocked()) {
                        doorIcon = eastDoorImageLocked;
                    } else {
                        doorIcon = eastDoorImage;
                    }

                }

                case "west" -> {
                    if (door.isLocked()) {
                        doorIcon = westDoorImageLocked;
                    } else {
                        doorIcon = westDoorImage;
                    }

                }

                default -> doorIcon = unlockedDoor;
            }
        }


        /*
        if (door.isPermanentlyClosed()) {
            doorIcon = permanentlyLockedDoor;
        } else if (door.isLocked()) {
            doorIcon = lockedDoor;
        } else {
            doorIcon = unlockedDoor;
        } */


        if (doorIcon == null) return;

        g2.drawImage(doorIcon.getImage(), x, y, w, h, this);
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
        int stepX = scaled(770);
        int stepY = scaled(500);

        if (up) {
            targetCamY -= stepY;
            up = false;
        }

        if (down) {
            targetCamY += stepY;
            down = false;
        }

        if (left) {
            targetCamX -= stepX;
            left = false;
        }

        if (right) {
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

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        g.drawImage(mazeGrass, 0, 0, panelWidth, panelHeight, this);

        Graphics2D g2 = (Graphics2D) g.create();

        int rows = maze.getRows();
        int cols = maze.getCols();

        int roomW = scaled(900);
        int roomH = scaled(900);



        int stepX = scaled(770);
        int stepY = scaled(500);

        int centerRow = (rows + 1) / 2;
        int centerCol = (cols + 1) / 2;

        int doorWidth = scaled(165);
        int doorHeight = scaled(220);

        // =====================================================
        // PASS 1: UNVISITED (BOTTOM LAYER)
        // =====================================================
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {

                int rowIndex = r - 1;
                int colIndex = c - 1;

                Room room = maze.getRoom(rowIndex, colIndex);

                if (room.isVisited()) continue;

                drawRoom(g, g2, room,
                        rowIndex, colIndex,
                        panelWidth, panelHeight,
                        stepX, stepY,
                        centerRow, centerCol,
                        roomW, roomH,
                        doorWidth, doorHeight);
            }
        }

        // =====================================================
        // PASS 2: VISITED (TOP LAYER)
        // =====================================================
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {

                int newRoomW = scaled(770 * 1.2);
                int newRoomH = scaled(500 * 1.4);

                int rowIndex = r - 1;
                int colIndex = c - 1;

                Room room = maze.getRoom(rowIndex, colIndex);

                if (!room.isVisited()) continue;

                drawRoom(g, g2, room,
                        rowIndex, colIndex,
                        panelWidth, panelHeight,
                        stepX, stepY,
                        centerRow, centerCol,
                        newRoomW, newRoomH,
                        doorWidth, doorHeight);
            }
        }

        // =====================================================
        // PLAYER (TOPMOST)
        // =====================================================

        float px = stepX * (centerCol - (renderCol + 1));
        float py = stepY * (centerRow - (renderRow + 1));

        int playerScreenX =
                (int)(panelWidth / 2 - (scaled(450) + px) - camX)
                        + scaled(385);

        int playerScreenY =
                (int)(panelHeight / 2 - py - camY)
                        + scaled(250);

        ImageIcon butterfly = butterflyToggle
                ? PlayerSetupView.character2
                : PlayerSetupView.character1;

        drawSprite(g, butterfly, playerScreenX, playerScreenY, 150, 150);

        g2.dispose();
    }




    private void drawRoom(Graphics g,
                          Graphics2D g2,
                          Room room,
                          int rowIndex,
                          int colIndex,
                          int panelWidth,
                          int panelHeight,
                          int stepX,
                          int stepY,
                          int centerRow,
                          int centerCol,
                          int roomW,
                          int roomH,
                          int doorWidth,
                          int doorHeight) {

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

        // =====================================================
        // ROOM BASE IMAGE (ONLY STATE-BASED LOGIC)
        // =====================================================
        Image img = room.isVisited()
                ? hedgeTest.getImage()
                : shadyHedge.getImage();

        g.drawImage(img, screenX, screenY, roomW, roomH, this);

        // =====================================================
        // DOORS
        // =====================================================

        drawDoor(g2, room.getNorthDoor(), "north",
                roomCenterX - doorHeight/2,
                screenY + scaled(20),
                doorHeight, doorWidth);

        drawDoor(g2, room.getEastDoor(), "east",
                screenX + (newRoomW - doorWidth/2),
                roomCenterY - doorHeight/2,
                doorWidth, doorHeight);

        drawDoor(g2, room.getSouthDoor(), "south",
                roomCenterX - doorHeight/2,
                screenY + newRoomH - doorWidth,
                doorHeight, doorWidth);

        drawDoor(g2, room.getWestDoor(), "west",
                screenX - scaled(5),
                roomCenterY - doorHeight/2,
                doorWidth, doorHeight);
    }
    public void resetVisitedRooms() {
        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {
                maze.getRoom(r, c).setVisited(false);
            }
        }

        maze.getRoom(playerRow, playerCol).setVisited(true);

        repaint();
    }
    public void resetPlayer() {

        playerRow = startRow;
        playerCol = startCol;

        renderRow = startRow;
        renderCol = startCol;

        targetCamX = 0;
        targetCamY = 400;

        camX = targetCamX;
        camY = targetCamY;

        repaint();
    }
    public void resetDoors() {
        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {

                Room room = maze.getRoom(r, c);

                room.getNorthDoor().reset();
                room.getSouthDoor().reset();
                room.getEastDoor().reset();
                room.getWestDoor().reset();
            }
        }
    }
    public void resetGame() {

        scale = 1.0f; // <-- RESET SCROLL WHEEL ZOOM

        // reset rooms
        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {

                Room room = maze.getRoom(r, c);

                room.setVisited(false);

                room.getNorthDoor().reset();
                room.getSouthDoor().reset();
                room.getEastDoor().reset();
                room.getWestDoor().reset();
            }
        }

        // move player to start
        playerRow = startRow;
        playerCol = startCol;

        renderRow = startRow;
        renderCol = startCol;

        maze.getRoom(startRow, startCol).setVisited(true);

        // reset camera
        camX = 0;
        camY = 400;
        targetCamX = camX;
        targetCamY = camY;

        repaint();
    }
}