package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GenerateMaze {

    public static Maze generateMaze() {
        // Reset question pool so each new game gets a fresh random set of questions
        QuestionDAO.resetUsedQuestions();

        int rows = 5;
        int cols = 5;
        Maze maze = new Maze(rows, cols);

        // Create all rooms, every door starts locked by default
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                maze.setRooms(r, c, new Room());
            }
        }

        // Link shared doors between adjacent rooms
        // so unlocking one side unlocks both directions
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Room room = maze.getRoom(r, c);

                if (r + 1 < rows) {
                    Door sharedNS = room.getSouthDoor();
                    maze.getRoom(r + 1, c).setNorthDoor(sharedNS);
                }

                if (c + 1 < cols) {
                    Door sharedEW = room.getEastDoor();
                    maze.getRoom(r, c + 1).setWestDoor(sharedEW);
                }
            }
        }

        // Set entrance at center
        maze.setEntrance(maze.getRoom(2, 2));

        // ----------------------------------------------------------------
        // Randomly pick a perimeter door as the exit
        // Perimeter doors: north doors on row 0, south doors on row 4,
        //                  west doors on col 0, east doors on col 4
        // ----------------------------------------------------------------
        List<int[]> perimeterDoors = new ArrayList<>();

        // North doors (row 0) - direction "north"
        for (int c = 0; c < cols; c++) {
            perimeterDoors.add(new int[]{0, c, 0}); // 0 = north
        }
        // South doors (row 4) - direction "south"
        for (int c = 0; c < cols; c++) {
            perimeterDoors.add(new int[]{rows - 1, c, 1}); // 1 = south
        }
        // West doors (col 0) - direction "west"
        for (int r = 0; r < rows; r++) {
            perimeterDoors.add(new int[]{r, 0, 2}); // 2 = west
        }
        // East doors (col 4) - direction "east"
        for (int r = 0; r < rows; r++) {
            perimeterDoors.add(new int[]{r, cols - 1, 3}); // 3 = east
        }

        // Remove entrance room's doors from candidates
        perimeterDoors.removeIf(d -> d[0] == 2 && d[1] == 2);

        // Pick a random perimeter door
        Random random = new Random();
        int[] chosen = perimeterDoors.get(random.nextInt(perimeterDoors.size()));

        int exitRow = chosen[0];
        int exitCol = chosen[1];
        int dirCode = chosen[2];

        Room exitRoom = maze.getRoom(exitRow, exitCol);
        Door exitDoor;
        String exitDirection;

        switch (dirCode) {
            case 0 -> { exitDoor = exitRoom.getNorthDoor(); exitDirection = "north"; }
            case 1 -> { exitDoor = exitRoom.getSouthDoor(); exitDirection = "south"; }
            case 2 -> { exitDoor = exitRoom.getWestDoor();  exitDirection = "west"; }
            default -> { exitDoor = exitRoom.getEastDoor(); exitDirection = "east"; }
        }

        maze.setExitRoom(exitRoom);
        maze.setExitDoor(exitDoor);
        maze.setExitDoorDirection(exitDirection);

        System.out.println("Exit door: row=" + exitRow + " col=" + exitCol + " direction=" + exitDirection);

        return maze;
    }
}