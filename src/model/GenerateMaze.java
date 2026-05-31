package model;

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

        maze.setEntrance(maze.getRoom(2, 2));
        maze.setExit(maze.getRoom(4, 3));
        return maze;
    }
}