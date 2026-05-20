package model;
import view.*;
import model.*;
import controller.*;

import java.util.Arrays;


public class GenerateMaze {


    public static Maze generateMaze() {
        int rows = 5;
        int cols = 5;
        Maze maze = new Maze(rows, cols);


        // "." = dead end
        // "*" = open door
        // Order: "left, top, right, bottom"
        String[][] layout = {
                {"..*.", "*..*", "...*", "..**", "*..*"},
                {"..**", "**.*", ".*.*", ".***", "**.*"},
                {".**.", "****", "****", "**.*", ".*.*"},
                {"..**", "****", "**..", ".*.*", ".***"},
                {".*..", ".*...", "..*.", "**..", ".*.."}
        };

        for(int r = 0; r < rows; r++) {
            for(int c = 0; c < cols; c++) {
                Room room = new Room();
                maze.setRooms(r, c, room);

                String pattern = layout[r][c];

                if(pattern.charAt(0) == '*') {
                    room.getWestDoor().setLocked(false);
                }
                if(pattern.charAt(1) == '*') {
                    room.getNorthDoor().setLocked(false);
                }
                if(pattern.charAt(2) == '*') {
                    room.getEastDoor().setLocked(false);
                }
                if(pattern.charAt(3) == '*') {
                    room.getSouthDoor().setLocked(false);
                }

            }
        }

        maze.setEntrance(maze.getRoom(3,3));
        maze.setExit(maze.getRoom(4,3));
        return maze;
    }



}
