package view;

import javax.swing.*;
import java.awt.*;

import controller.GameController;
import model.*;

/**
 * Represents the maze gameplay view.
 *
 * <p>This view renders the maze background, rooms, doors, player sprite,
 * movement buttons, hint button, compass, and timer display. Gameplay logic,
 * such as movement validation, trivia handling, and win or lose conditions,
 * is delegated to {@link GameController}.</p>
 */
public class MazeView extends JPanel {

    private final JPanel myPlayerPanel;

    private int myStartRow;
    private int myStartCol;

    // Background image
    private Image myMazeGrass;

    // normal hedge and shady hedge icons (rooms)
    private ImageIcon myHedgeTest = new ImageIcon("src/images/Hedge900-675.png");
    private ImageIcon myShadyHedge = new ImageIcon("src/images/ShadyHedge.png");

    private ImageIcon myUnlockedDoor = new ImageIcon("src/images/UnlockedHedge.png");

    // Unlocked doors (open path)
    private ImageIcon myEastDoorImage = new ImageIcon("src/images/UnlockedHedge.png");
    private ImageIcon myNorthDoorImage = new ImageIcon("src/images/NorthDoorUnlocked.png");
    private ImageIcon mySouthDoorImage = new ImageIcon("src/images/NorthDoorUnlocked.png");
    private ImageIcon myWestDoorImage = new ImageIcon("src/images/UnlockedHedge.png");

    // Possible pathways (to unlock)
    private ImageIcon myEastDoorImageLocked = new ImageIcon("src/images/EastDoorLocked.png");
    private ImageIcon myNorthDoorImageLocked = new ImageIcon("src/images/NorthDoorLocked.png");
    private ImageIcon mySouthDoorImageLocked = new ImageIcon("src/images/SouthDoorLocked.png");
    private ImageIcon myWestDoorImageLocked = new ImageIcon("src/images/WestDoorLocked.png");

    private ImageIcon myFirstCharacter;
    private ImageIcon mySecondCharacter;

    private final Maze myMaze;
    private final Player myPlayer;
    private final GameController myController;

    private float myCamX;
    private float myCamY;
    private float myTargetCamX;
    private float myTargetCamY;

    // Player position in maze grid
    private int myPlayerRow;
    private int myPlayerCol;

    private float myRenderRow;
    private float myRenderCol;

    // Room info label
    private JLabel myCoordLabel;

    private boolean myButterflyToggle = false;

    // Zoom scale (fixed - no zoom in/out)
    private final float myScale = 1.0f;

    // Dynamic scaling base resolution based on original design size
    private static final double BASE_WIDTH = 1536.0;
    private static final double BASE_HEIGHT = 1024.0;

    private JLabel myTimerLabel;


    /**
     * Constructs the maze view.
     *
     * <p>This initializes maze rendering, player positioning, camera movement,
     * animation timers, movement buttons, the timer display, and other UI
     * components used during gameplay.</p>
     *
     * @param theMaze the maze being displayed
     * @param thePlayer the player navigating the maze
     * @param theController the controller handling gameplay actions
     */
    public MazeView(final Maze theMaze, final Player thePlayer, final GameController theController) {
        this.myMaze = theMaze;
        this.myPlayer = thePlayer;
        this.myController = theController;

        // Find player starting position
        int[] startPos = myMaze.findRoom(myPlayer.getCurrentRoom());
        myPlayerRow = startPos[0];
        myPlayerCol = startPos[1];

        myMaze.getRoom(myPlayerRow, myPlayerCol).setVisited(true);

        myCamX = 0;
        myCamY = 0;
        myTargetCamX = myCamX;
        myTargetCamY = myCamY;

        myRenderRow = myPlayerRow;
        myRenderCol = myPlayerCol;

        myStartRow = startPos[0];
        myStartCol = startPos[1];

        setupButtons();

        // game loop
        new Timer(16, e -> {
            updateCamera();
            updatePlayerAnimation();
            repaint();
        }).start();

        // butterfly flap animation timer
        new Timer(300, e -> {
            myButterflyToggle = !myButterflyToggle;
        }).start();

        setLayout(null);
        setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        setFocusable(true);

        ImageIcon background = new ImageIcon("src/images/DayGrass.png");
        myMazeGrass = background.getImage();
        myFirstCharacter = new ImageIcon("src/images/MagentaFlap.png");
        mySecondCharacter = new ImageIcon("src/images/MagentaUnflap.png");

        myPlayerPanel = new JPanel();
        myPlayerPanel.setOpaque(false);
        add(myPlayerPanel);

        // Room info label
        myCoordLabel = new JLabel(getRoomInfo());
        myCoordLabel.setBounds(10, 10, 600, 30);
        myCoordLabel.setForeground(Color.WHITE);
        myCoordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        add(myCoordLabel);

        addTimer();
    }

    // =====================================================
    // GETTERS FOR CONTROLLER
    // =====================================================

    /**
     * Returns the player's current row in the maze grid.
     *
     * @return the player's current row
     */
    public int getPlayerRow() {
        return myPlayerRow;
    }

    /**
     * Returns the player's current column in the maze grid.
     *
     * @return the player's current column
     */
    public int getPlayerCol() {
        return myPlayerCol;
    }

    // =====================================================
    // PLAYER MOVEMENT (called by GameController)
    // =====================================================

    /**
     * Moves the player to the specified maze location.
     *
     * <p>This updates the player's row and column, marks the new room as visited,
     * updates the player's current room in the model, refreshes the room
     * information label, and allows the camera animation to follow the new
     * position.</p>
     *
     * @param theNewRow the row to move the player to
     * @param theNewCol the column to move the player to
     */
    public void movePlayer(final int theNewRow, final int theNewCol) {
        myPlayerRow = theNewRow;
        myPlayerCol = theNewCol;

        Room currentRoom = myMaze.getRoom(myPlayerRow, myPlayerCol);
        currentRoom.setVisited(true);

        myPlayer.setCurrentRoom(currentRoom);

        myCoordLabel.setText(getRoomInfo());
    }

    // =====================================================
    // ANIMATION
    // =====================================================
    /**
     * Smoothly updates the rendered player position.
     *
     * <p>This creates movement animation between the player's previous and
     * current maze grid positions.</p>
     */
    private void updatePlayerAnimation() {
        float speed = 0.02f;
        myRenderRow += (myPlayerRow - myRenderRow) * speed;
        myRenderCol += (myPlayerCol - myRenderCol) * speed;
    }

    // =====================================================
    // SCALING
    // =====================================================

    /**
     * Calculates the UI scale factor based on the current window size
     * relative to the original design resolution.
     *
     * @return the current UI scale factor
     */
    private double getUIScale() {
        double scaleX = getWidth() / BASE_WIDTH;
        double scaleY = getHeight() / BASE_HEIGHT;
        return Math.min(scaleX, scaleY);
    }

    /**
     * Calculates the world rendering scale used for rooms,
     * doors, and player sprites.
     *
     * @return the world scale factor
     */
    private double getWorldScale() {
        return getUIScale() * myScale;
    }

    /**
     * Scales a coordinate or dimension value according to
     * the current world scale.
     *
     * @param theValue the value to scale
     * @return the scaled value as an integer
     */
    private int scaled(final double theValue) {
        return (int)(theValue * getWorldScale());
    }

    // =====================================================
    // ROOM INFO
    // =====================================================

    /**
     * Builds a string containing the current room coordinates
     * and door status information.
     *
     * @return formatted room information string
     */
    private String getRoomInfo() {
        Room r = myMaze.getRoom(myPlayerRow, myPlayerCol);
        return "(" + myPlayerCol + "," + myPlayerRow + ") | " +
                "N:" + (r.getNorthDoor().isLocked() ? "LOCKED" : "OPEN") + " " +
                "S:" + (r.getSouthDoor().isLocked() ? "LOCKED" : "OPEN") + " " +
                "E:" + (r.getEastDoor().isLocked() ? "LOCKED" : "OPEN") + " " +
                "W:" + (r.getWestDoor().isLocked() ? "LOCKED" : "OPEN");
    }

    // =====================================================
    // BUTTONS
    // =====================================================

    /**
     * Creates and configures the movement and hint buttons.
     *
     * <p>The movement buttons delegate movement requests to the game controller,
     * while the hint button asks the controller to display a directional hint.</p>
     */
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
        hintButton.addActionListener(e -> myController.showHint(myPlayerRow, myPlayerCol));
    }

    // =====================================================
    // CAMERA
    // =====================================================

    /**
     * Updates the camera position to smoothly follow the player
     * as they move through the maze.
     */
    private void updateCamera() {
        int stepX = scaled(770);
        int stepY = scaled(500);

        int centerRow = (myMaze.getRows() + 1) / 2;
        int centerCol = (myMaze.getCols() + 1) / 2;

        float px = stepX * (centerCol - (myRenderCol + 1));
        float py = stepY * (centerRow - (myRenderRow + 1));

        myTargetCamX = -(scaled(450) + px) + scaled(385);
        myTargetCamY = -py + scaled(400);

        float smooth = 0.08f;

        myCamX += (myTargetCamX - myCamX) * smooth;
        myCamY += (myTargetCamY - myCamY) * smooth;
    }

    // =====================================================
    // PAINTING
    // =====================================================

    /**
     * Paints the maze view.
     *
     * <p>This renders the background, unvisited rooms, visited rooms, doors,
     * player sprite, compass, and other gameplay visuals.</p>
     *
     * @param theGraphics the graphics context used for painting
     */
    @Override
    protected void paintComponent(Graphics theGraphics) {
        super.paintComponent(theGraphics);

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        theGraphics.drawImage(myMazeGrass, 0, 0, panelWidth, panelHeight, this);

        Graphics2D g2 = (Graphics2D) theGraphics.create();

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
                drawRoom(theGraphics, g2, room, rowIndex, colIndex,
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
                drawRoom(theGraphics, g2, room, rowIndex, colIndex,
                        panelWidth, panelHeight, stepX, stepY,
                        centerRow, centerCol, newRoomW, newRoomH, doorWidth, doorHeight);
            }
        }

        // PLAYER (TOPMOST)
        float px = stepX * (centerCol - (myRenderCol + 1));
        float py = stepY * (centerRow - (myRenderRow + 1));

        int playerScreenX = (int)(panelWidth / 2 - (scaled(450) + px) - myCamX) + scaled(385);
        int playerScreenY = (int)(panelHeight / 2 - py - myCamY) + scaled(250);

        ImageIcon butterfly = myButterflyToggle ? mySecondCharacter : myFirstCharacter;
        drawSprite(theGraphics, butterfly, playerScreenX, playerScreenY, 150, 150);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screen.width;

        ImageIcon original = new ImageIcon("src/images/Compass.png");
        Image scaledImage = original.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        ImageIcon compass = new ImageIcon(scaledImage);
        theGraphics.drawImage(compass.getImage(), (screenWidth*6)/7, (screen.height*1)/2 - 53, 150, 150, this);

        g2.dispose();
    }

    /**
     * Draws a sprite image at the specified screen position.
     *
     * @param theGraphics the graphics context
     * @param theSprite the sprite image to draw
     * @param theX the x-coordinate
     * @param theY the y-coordinate
     * @param theWindowWidth the sprite width before scaling
     * @param theWindowHeight the sprite height before scaling
     */
    private void drawSprite(final Graphics theGraphics, final ImageIcon theSprite, final int theX,
                            final int theY,
                            final int theWindowWidth, final int theWindowHeight) {
        int spriteWidth = scaled(theWindowWidth);
        int spriteHeight = scaled(theWindowHeight);
        theGraphics.drawImage(theSprite.getImage(), theX, theY, spriteWidth, spriteHeight, this);
    }

    /**
     * Draws a door image based on its direction and current state.
     *
     * @param theGraphics the graphics context
     * @param theDoor the door to render
     * @param theDirection the door direction (north, south, east, west)
     * @param theX the x-coordinate
     * @param theY the y-coordinate
     * @param theW the door width
     * @param theH the door height
     */
    private void drawDoor(Graphics2D theGraphics, Door theDoor, String theDirection,
                          int theX, int theY, int theW, int theH) {
        ImageIcon doorIcon;

        if (theDoor.isPermanentlyClosed()) {
            doorIcon = null;
        } else {
            switch (theDirection) {
                case "north" -> doorIcon = theDoor.isLocked() ? myNorthDoorImageLocked : myNorthDoorImage;
                case "south" -> doorIcon = theDoor.isLocked() ? mySouthDoorImageLocked : mySouthDoorImage;
                case "east"  -> doorIcon = theDoor.isLocked() ? myEastDoorImageLocked : myEastDoorImage;
                case "west"  -> doorIcon = theDoor.isLocked() ? myWestDoorImageLocked : myWestDoorImage;
                default      -> doorIcon = myUnlockedDoor;
            }
        }

        if (doorIcon == null) return;
        theGraphics.drawImage(doorIcon.getImage(), theX, theY, theW, theH, this);
    }

    /**
     * Draws a room and its associated doors at the correct
     * screen position.
     *
     * @param theGraphics the graphics context
     * @param theGraphics2D the graphics2D context
     * @param theRoom the room to draw
     * @param theRowIndex the room row index
     * @param theColIndex the room column index
     * @param thePanelWidth the panel width
     * @param thePanelHeight the panel height
     * @param theStepX horizontal room spacing
     * @param theStepY vertical room spacing
     * @param theCenterRow maze center row
     * @param theCenterCol maze center column
     * @param theRoomW room width
     * @param theRoomH room height
     * @param theDoorWidth door width
     * @param theDoorHeight door height
     */
    private void drawRoom(final Graphics theGraphics, final Graphics2D theGraphics2D, final Room theRoom,
                          final int theRowIndex, final int theColIndex,
                          final int thePanelWidth, final int thePanelHeight,
                          final int theStepX, final int theStepY,
                          final int theCenterRow, final int theCenterCol,
                          final int theRoomW, final int theRoomH,
                          final int theDoorWidth, final int theDoorHeight) {

        int r = theRowIndex + 1;
        int c = theColIndex + 1;

        int x = theStepX * (theCenterCol - c);
        int y = theStepY * (theCenterRow - r);

        int screenX = (int)(thePanelWidth / 2 - (scaled(450) + x) - myCamX);
        int screenY = (int)(thePanelHeight / 2 - y - myCamY);

        int newRoomW = scaled(770 * 1.2);
        int newRoomH = scaled(500 * 1.4);

        int roomCenterX = screenX + newRoomW / 2;
        int roomCenterY = screenY + newRoomH / 2;

        Image img = theRoom.isVisited() ? myHedgeTest.getImage() : myShadyHedge.getImage();
        theGraphics.drawImage(img, screenX, screenY, theRoomW, theRoomH, this);

        drawDoor(theGraphics2D, theRoom.getNorthDoor(), "north",
                roomCenterX - theDoorHeight/2, screenY + scaled(20), theDoorHeight, theDoorWidth);
        drawDoor(theGraphics2D, theRoom.getEastDoor(), "east",
                screenX + (newRoomW - theDoorWidth/2)-30, roomCenterY - theDoorHeight/2, theDoorWidth, theDoorHeight);
        drawDoor(theGraphics2D, theRoom.getSouthDoor(), "south",
                roomCenterX - theDoorHeight/2, screenY + newRoomH - theDoorWidth, theDoorHeight, theDoorWidth);
        drawDoor(theGraphics2D, theRoom.getWestDoor(), "west",
                screenX - scaled(5), roomCenterY - theDoorHeight/2, theDoorWidth, theDoorHeight);
    }

    /**
     * Sets the player's animation sprites.
     *
     * <p>If both icons are non-null, the current flap and unflap sprites are
     * replaced and the view is repainted.</p>
     *
     * @param theFlapIcon the sprite used for one animation frame
     * @param theUnflapIcon the sprite used for the alternate animation frame
     */
    public void setPlayerSprites(final ImageIcon theFlapIcon, final ImageIcon theUnflapIcon) {
        if (theFlapIcon != null && theUnflapIcon != null) {
            myFirstCharacter = theFlapIcon;
            mySecondCharacter = theUnflapIcon;
            repaint();
        }
    }

    /**
     * Updates maze images based on the selected theme.
     *
     * <p>This changes the background, hedge, and unlocked door images used
     * when rendering the maze.</p>
     *
     * @param theDarkModeSelected {@code true} to use dark mode images;
     *                            {@code false} to use light mode images
     */
    public void setDarkMode(final boolean theDarkModeSelected) {
        if (theDarkModeSelected) {
            myMazeGrass = new ImageIcon("src/images/NightGrass.png").getImage();
            myHedgeTest = new ImageIcon("src/images/NightHedge.png");
            myNorthDoorImage = new ImageIcon("src/images/NightNorthDoorUnlocked.png");
            myEastDoorImage = new ImageIcon("src/images/NightEastDoorUnlocked.png");
            myWestDoorImage = new ImageIcon("src/images/NightWestUnlockedHedge.png");
        } else {
            myMazeGrass = new ImageIcon("src/images/DayGrass.png").getImage();
            myHedgeTest = new ImageIcon("src/images/Hedge900-675.png");
            myNorthDoorImage = new ImageIcon("src/images/NorthDoorUnlocked.png");
            myEastDoorImage = new ImageIcon("src/images/UnlockedHedge.png");
            myWestDoorImage = new ImageIcon("src/images/UnlockedHedge.png");
        }
        repaint();
    }
    // =====================================================
    // TIMER
    // =====================================================

    /**
     * Creates and displays the game timer label.
     */
    public void addTimer() {
        myTimerLabel = new JLabel("Time: 0");
        myTimerLabel.setBounds(50, 50, 200, 40);
        myTimerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        myTimerLabel.setForeground(Color.WHITE);
        this.add(myTimerLabel);
    }

    /**
     * Updates the timer display using the elapsed game time.
     *
     * @param theTime the elapsed time in seconds
     */
    public void updateTimer(final double theTime) {
        int totalSeconds = (int) theTime;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        myTimerLabel.setText("Time: " + String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }
}