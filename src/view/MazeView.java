package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import model.*;

public class MazeView extends JPanel {

    private final JPanel playerPanel;

    public static Image mazeGrass;

    public static ImageIcon hedgeTest = new ImageIcon("src/images/FixedHedge900.png");
    public static ImageIcon shadyHedge = new ImageIcon("src/images/ShadyHedge.png");

    public static ImageIcon lockedDoor = new ImageIcon("src/images/lockedDoor.png");
    public static ImageIcon unlockedDoor = new ImageIcon("src/images/unlockedDoor.png");

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

        camX = 0;
        camY = 400;
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

    public JPanel getPlayerPanel() {
        return playerPanel;
    }

    // Calculates responsive scaling factor based on current panel size, which resizes
    // PNG sprites and maze objects resize dynamically with the window
    private double getUIScale() {
        double scaleX = getWidth() / BASE_WIDTH;
        double scaleY = getHeight() / BASE_HEIGHT;

        return Math.min(scaleX, scaleY);
    }

    // Combines responsive UI scaling with zoom scaling
    private double getWorldScale() {
        return getUIScale() * scale;
    }

    // Converts original design pixel values into dynamically scaled values
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

        if (!door.isLocked()) {
            movePlayer(newRow, newCol);
        } else {
            Question q = door.getQuestion();

            if (q != null) {
                TriviaPopup popup = new TriviaPopup(q);
                popup.setVisible(true);

                if (popup.isAnsweredCorrectly()) {
                    door.setLocked(false);
                    movePlayer(newRow, newCol);

                    JOptionPane.showMessageDialog(
                            MainGUI.window,
                            "Correct! Door unlocked!",
                            "Result",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            MainGUI.window,
                            "Wrong! Door stays locked.",
                            "Result",
                            JOptionPane.ERROR_MESSAGE
                    );

                    System.out.println("Wrong! Staying in room [" + playerRow + "][" + playerCol + "]");
                }
            }
        }
    }

    // Helper method created for updating player's position in maze
    private void movePlayer(int newRow, int newCol) {
        playerRow = newRow;
        playerCol = newCol;

        MainGUI.player.setCurrentRoom(maze.getRoom(playerRow, playerCol));
        coordLabel.setText(getRoomInfo());

        System.out.println("Moved to room [" + playerRow + "][" + playerCol + "]");
    }

    // Automatically scales PNG sprites relative to current window size
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
                          int x,
                          int y,
                          int w,
                          int h) {

        ImageIcon doorIcon;

        if (door.isLocked()) {
            doorIcon = lockedDoor;
        } else {
            doorIcon = unlockedDoor;
        }

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

        // Scales background image to always fit current panel size
        g.drawImage(mazeGrass, 0, 0, panelWidth, panelHeight, this);

        Graphics2D g2 = (Graphics2D) g.create();

        int rows = maze.getRows();
        int cols = maze.getCols();

        // Dynamically scales maze room PNG sizes based on window size and zoom
        int roomW = scaled(900);
        int roomH = scaled(900);

        // Dynamically scales spacing between maze rooms
        int stepX = scaled(770);
        int stepY = scaled(500);

        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                int centerRow = (rows + 1) / 2;
                int centerCol = (cols + 1) / 2;

                int x = stepX * (centerCol - c);
                int y = stepY * (centerRow - r);

                int screenX = (int)(panelWidth / 2 - (scaled(450) + x) - camX);
                int screenY = (int)(panelHeight / 2 - y - camY);

                g.drawImage(
                        hedgeTest.getImage(),
                        screenX,
                        screenY,
                        roomW,
                        roomH,
                        this
                );

                Room room = maze.getRoom(r - 1, c - 1);

                // Dynamically scales door sprite sizes relative to screen size
                int doorWidth = scaled(220);
                int doorHeight = scaled(120);

                // NORTH DOOR
                drawDoor(
                        g2,
                        room.getNorthDoor(),
                        screenX + roomW / 2 - doorWidth / 2,
                        screenY + scaled(70),
                        doorWidth,
                        doorHeight
                );

                // EAST DOOR
                drawDoor(
                        g2,
                        room.getEastDoor(),
                        screenX + roomW - scaled(140),
                        screenY + roomH / 2 - doorWidth / 2,
                        doorHeight,
                        doorWidth
                );

                // SOUTH DOOR
                drawDoor(
                        g2,
                        room.getSouthDoor(),
                        screenX + roomW / 2 - doorWidth / 2,
                        screenY + roomH - scaled(95),
                        doorWidth,
                        doorHeight
                );

                // WEST DOOR
                drawDoor(
                        g2,
                        room.getWestDoor(),
                        screenX + scaled(35),
                        screenY + roomH / 2 - doorWidth / 2,
                        doorHeight,
                        doorWidth
                );
            }
        }

        g2.dispose();

        int centerRow = (rows + 1) / 2;
        int centerCol = (cols + 1) / 2;

        int px = stepX * (centerCol - (playerCol + 1));
        int py = stepY * (centerRow - (playerRow + 1));

        int playerScreenX =
                (int)(panelWidth / 2 - (scaled(450) + px) - camX)
                        + scaled(385);

        int playerScreenY =
                (int)(panelHeight / 2 - py - camY)
                        + scaled(250);

        ImageIcon butterfly = butterflyToggle
                ? PlayerSetupView.character2
                : PlayerSetupView.character1;

        if (butterfly != null && butterfly.getImage() != null) {
            // Dynamically scales player butterfly sprite
            drawSprite(
                    g,
                    butterfly,
                    playerScreenX,
                    playerScreenY,
                    150,
                    150
            );
        }
    }
}