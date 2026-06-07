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

    private boolean gameFinished = false;

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
        addTimer();
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
        // Check if this direction leads to the exit door before blocking perimeter moves
        Room currentRoom = maze.getRoom(playerRow, playerCol);
        Door potentialDoor = switch (direction) {
            case "north" -> currentRoom.getNorthDoor();
            case "south" -> currentRoom.getSouthDoor();
            case "west"  -> currentRoom.getWestDoor();
            case "east"  -> currentRoom.getEastDoor();
            default -> null;
        };

        boolean isExitDoor = potentialDoor != null && potentialDoor == maze.getExitDoor();

        // Only block perimeter moves if it's NOT the exit door
        if (!isValidMove(direction) && !isExitDoor) {
            JOptionPane.showMessageDialog(
                    MainGUI.window,
                    "That door leads nowhere! Try a different door.",
                    "Dead End",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Door door = potentialDoor;

        int newRow = playerRow;
        int newCol = playerCol;

        switch (direction) {
            case "north" -> newRow = playerRow - 1;
            case "south" -> newRow = playerRow + 1;
            case "west"  -> newCol = playerCol - 1;
            case "east"  -> newCol = playerCol + 1;
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

            if (!checkForPossiblePathways()) {
                loseGame("You are completely blocked!\nAll reachable doors are permanently locked.\nGame Over.");
            }

            return;
        }

        // Already unlocked - free passage (only for non-exit doors)
        if (!door.isLocked() && !isExitDoor) {
            movePlayer(newRow, newCol);
        } else {
            Question q = door.getQuestion();

            if (q != null) {
                TriviaPopup popup = new TriviaPopup(q);
                popup.setVisible(true);

                String playerAnswer = popup.getPlayerAnswer();
                boolean correct = door.attemptAnswer(playerAnswer);

                if (correct) {
                    QuestionDAO.markAsCorrectlyAnswered(q);

                    // Win condition - exit door answered correctly
                    if (isExitDoor) {
                        finishGame();
                        return;
                    }

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

// Lose condition - exit door permanently locked after failed attempts
                    if (isExitDoor && door.isPermanentlyClosed()) {
                        loseGame("You failed to unlock the exit!\nGame Over.");
                        return;
                    }

// Lose condition - player has no accessible pathways left from current room
                    if (!checkForPossiblePathways()) {
                        loseGame("All doors in your vicinity are permanently locked.\nGame Over.");
                        return;
                    }
                }
            }
        }
    }

    private boolean checkForPossiblePathways() {
        boolean[][] visitedRooms = new boolean[maze.getRows()][maze.getCols()];
        return hasAttemptableDoor(playerRow, playerCol, visitedRooms);
    }

    /**
     * Recursively checks whether the player still has at least one reachable door
     * that can be attempted.
     *
     * <p>This method searches through all rooms the player can currently reach by
     * traveling through already-unlocked doors. While searching those reachable
     * rooms, it looks for at least one locked door that is not permanently closed.</p>
     *
     * <p>If no reachable locked door can still be attempted, then the player has
     * no possible progress left and should lose the game.</p>
     *
     * @param row the row of the room currently being checked
     * @param column the column of the room currently being checked
     * @param visitedRooms tracks which rooms have already been searched
     * @return true if at least one reachable door can still be attempted;
     *         false if the player is completely blocked
     */
    private boolean hasAttemptableDoor(int row, int column, boolean[][] visitedRooms) {
        // Stop searching if the row or column is outside the maze.
        if (row < 0 || row >= maze.getRows() || column < 0 || column >= maze.getCols()) {
            return false;
        }

        // Stop searching if this room has already been checked.
        // This prevents infinite recursion when unlocked doors form loops.
        if (visitedRooms[row][column]) {
            return false;
        }

        // Mark the current room as visited before checking its doors.
        visitedRooms[row][column] = true;

        // Get the room object at the current row and column.
        Room room = maze.getRoom(row, column);

        // Check each door in the current room.
        // If any door is locked and still attemptable, the player can still progress.
        if (isAttemptableDoor(room.getNorthDoor(), "north", row, column)) {
            return true;
        }

        if (isAttemptableDoor(room.getSouthDoor(), "south", row, column)) {
            return true;
        }

        if (isAttemptableDoor(room.getEastDoor(), "east", row, column)) {
            return true;
        }

        if (isAttemptableDoor(room.getWestDoor(), "west", row, column)) {
            return true;
        }

        // If no attemptable door was found in this room,
        // continue searching through doors that are already unlocked.
        //
        // These unlocked doors do not count as progress by themselves.
        // They only allow us to reach other rooms that may have attemptable doors.
        if (canTravelThrough(room.getNorthDoor(), "north", row, column)) {
            if (hasAttemptableDoor(row - 1, column, visitedRooms)) {
                return true;
            }
        }

        // Example: Search the room south of the current room if the south door is open.
        if (canTravelThrough(room.getSouthDoor(), "south", row, column)) {
            if (hasAttemptableDoor(row + 1, column, visitedRooms)) {
                return true;
            }
        }

        // Example 2: Search the room east of the current room if the east door is open
        // (and repeat for other directions)
        if (canTravelThrough(room.getEastDoor(), "east", row, column)) {
            if (hasAttemptableDoor(row, column + 1, visitedRooms)) {
                return true;
            }
        }

        if (canTravelThrough(room.getWestDoor(), "west", row, column)) {
            if (hasAttemptableDoor(row, column - 1, visitedRooms)) {
                return true;
            }
        }

        // If this room and all reachable rooms have no attemptable doors,
        // return false. This means the player is blocked from making progress.
        return false;
    }

    /**
     * Determines whether a specific door can still be attempted by the player.
     *
     * <p>A door is attemptable if it is locked, not permanently closed, and leads
     * to a valid room. The exit door is also considered attemptable as long as it
     * is not permanently closed because reaching it means the player can still win.</p>
     *
     * @param door the door being checked
     * @param direction the direction of the door from the current room
     * @param row the row of the current room
     * @param column the column of the current room
     * @return true if the door can still be attempted; false otherwise
     */
    private boolean isAttemptableDoor(Door door, String direction, int row, int column) {
        // A missing door or permanently closed door cannot be attempted.
        if (door == null || door.isPermanentlyClosed()) {
            return false;
        }

        // Check whether this door is the special exit door.
        boolean isExitDoor = door == maze.getExitDoor();

        // If the exit door is reachable and not permanently closed,
        // the player can still win.
        if (isExitDoor) {
            return true;
        }

        // If the door is already unlocked, it is only a pathway.
        // It should not count as an attemptable door because no question remains.
        if (!door.isLocked()) {
            return false;
        }

        // Calculate the neighboring room this door would lead to.
        int updatedRow = row;
        int updatedColumn = column;

        switch (direction) {
            case "north" -> updatedRow--;
            case "south" -> updatedRow++;
            case "east" -> updatedColumn++;
            case "west" -> updatedColumn--;
        }

        // A locked door only counts as attemptable if it leads to
        // a valid room inside the maze.
        return updatedRow >= 0 && updatedRow < maze.getRows()
                && updatedColumn >= 0 && updatedColumn < maze.getCols();
    }

    /**
     * Determines whether the player can travel through a specific door into
     * a neighboring room.
     *
     * <p>This method is used for the recursive search. It allows the search to move
     * through already-unlocked doors so that the game can check the entire reachable
     * area, not just the room the player is currently standing in.</p>
     *
     * @param door the door being checked
     * @param direction the direction of the door from the current room
     * @param row the row of the current room
     * @param column the column of the current room
     * @return true if the player can travel through the door; false otherwise
     */
    private boolean canTravelThrough(Door door, String direction, int row, int column) {
        // A missing door, locked or permanently closed door cannot be attempted.
        if (door == null || door.isLocked() || door.isPermanentlyClosed()) {
            return false;
        }

        // Calculate the neighboring room this door leads to.
        int updatedRow = row;
        int updatedColumn = column;

        switch (direction) {
            case "north" -> updatedRow--;
            case "south" -> updatedRow++;
            case "east" -> updatedColumn++;
            case "west" -> updatedColumn--;
        }

        // The player can only travel through the door if the neighboring
        // room is inside the maze boundaries.
        return updatedRow >= 0 && updatedRow < maze.getRows()
                && updatedColumn >= 0 && updatedColumn < maze.getCols();
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

        if (door.isPermanentlyClosed()) {
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

        if (doorIcon == null) return;

        g2.drawImage(doorIcon.getImage(), x, y, w, h, this);
    }

    private void setupButtons() {
        JButton upB = new JButton("↑");
        JButton downB = new JButton("↓");
        JButton leftB = new JButton("←");
        JButton rightB = new JButton("→");
        JButton hintButton = new JButton("Hint");

        upB.setBounds(1100, 500, 60, 60);
        downB.setBounds(1100, 620, 60, 60);
        leftB.setBounds(1040, 560, 60, 60);
        rightB.setBounds(1160, 560, 60, 60);
        hintButton.setBounds(50, 120, 100, 40);

        add(upB);
        add(downB);
        add(leftB);
        add(rightB);
        add(hintButton);

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
        hintButton.addActionListener(e -> showHint());
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
        // ROOM BASE IMAGE
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
                screenX + (newRoomW - doorWidth/2)-30,
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

    private void showHint() {
        int[] exitPos = maze.findRoom(maze.getExit());

        int exitRow = exitPos[0];
        int exitCol = exitPos[1];

        StringBuilder hint = new StringBuilder("Hint: Try moving ");

        if (exitRow > playerRow) {
            hint.append("south ");
        } else if (exitRow < playerRow) {
            hint.append("north ");
        }

        if (exitCol > playerCol) {
            hint.append("east ");
        } else if (exitCol < playerCol) {
            hint.append("west ");
        }

        if (exitRow == playerRow && exitCol == playerCol) {
            hint = new StringBuilder("You are already at the exit room! Find the exit door.");
        } else {
            hint.append("toward the exit.");
        }

        JOptionPane.showMessageDialog(
                MainGUI.window,
                hint.toString(),
                "Hint",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void finishGame() {
        if (gameFinished) return;

        gameFinished = true;

        MainGUI.player.stopTimer();

        LeaderboardDAO leaderboardDAO = new LeaderboardDAO();
        leaderboardDAO.saveScore(MainGUI.player);

        JOptionPane.showMessageDialog(
                MainGUI.window,
                "You reached the exit!\nTime: " +
                        String.format("%.2f", MainGUI.player.getRecordTime()) +
                        " seconds\nYour score was saved to the leaderboard.",
                "Game Complete",
                JOptionPane.INFORMATION_MESSAGE
        );

        LeaderboardView.showLeaderboard();
    }

    private void loseGame(String message) {
        if (gameFinished) return;

        gameFinished = true;

        MainGUI.player.stopTimer();

        Object[] options = {"Main Menu", "Exit Game"};

        int choice = JOptionPane.showOptionDialog(
                MainGUI.window,
                message,
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice == JOptionPane.YES_OPTION) {
            MainGUI.startNewGame();
            MainGUI.switchView(MainGUI.menuView);
        } else {
            System.exit(0);
        }
    }

    public void resetGame() {
        scale = 1.0f;

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

    public static JLabel timerLabel;

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