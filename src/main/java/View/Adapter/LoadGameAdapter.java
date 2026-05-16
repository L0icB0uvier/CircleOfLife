package View.Adapter;

import Controller.Controller;
import Model.GameDataManager;
import View.EventCollector;
import View.GraphicalUserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoadGameAdapter extends MouseAdapter {
    Controller controller;
    GraphicalUserInterface userInterface;
    String gameFile;

    public LoadGameAdapter(Controller controller, GraphicalUserInterface userInterface, String gameFile){
        this.controller = controller;
        this.userInterface = userInterface;
        this.gameFile = gameFile;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        controller.loadGame(gameFile);
        userInterface.startGame();
    }
}