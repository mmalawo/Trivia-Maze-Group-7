package controller;

import model.*;
import view.*;

import java.awt.*;


public class GameController {

private GameMenuView menu;
private Player player;

public GameController(GameMenuView theMenu, Player thePlayer) {
    this.menu = theMenu;
    this.player = thePlayer;
    addListeners();
}

public void addListeners() {
    menu.addPlayListener(e -> {
        System.out.println("Game in progress...");
        player.startTimer();

        new javax.swing.Timer(1000, evt -> {
            double time = player.elapsedTime();
            menu.updateTimer(time);

//            System.out.println("Time: " + player.elapsedTime());

        }).start();
    });
}

public static void restartGame() {
    MainGUI.startNewGame();
    System.out.println("Game restarted.");
}

// +startGame(thePlayerName : String) : void

// +handleMove(theDirection : String) : void

// +checkGameOver() : boolean

// +loadTriviaQuestion() : void

// +updateStats() : void





}
