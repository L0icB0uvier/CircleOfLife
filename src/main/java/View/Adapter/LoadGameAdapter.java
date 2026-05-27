package View.Adapter;

import Controller.Controller;
import View.GraphicalUserInterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadGameAdapter implements ActionListener {
    Controller controller;
    GraphicalUserInterface userInterface;
    String gameFile;

    public LoadGameAdapter(Controller controller, GraphicalUserInterface userInterface, String gameFile){
        this.controller = controller;
        this.userInterface = userInterface;
        this.gameFile = gameFile;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.loadGame(gameFile);
        userInterface.startGame();
    }
}