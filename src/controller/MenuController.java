package controller;

import java.util.*;
import java.io.*;
import javax.swing.*;   // for JFrame
import java.awt.*;
import java.awt.event.*;
import view.GameMenuView;

public class MenuController {// Action Listener and events

    private GameMenuView menu;

    public MenuController(GameMenuView theMenu) {
        this.menu = theMenu;
        addListeners();
    }

    private void addListeners() {
        menu.addExitListener(e -> {
            System.out.println("Application Closed.");
            System.exit(0);
        });
    }
}

