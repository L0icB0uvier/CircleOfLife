package View.Adapter;

import Global.Configuration;
import View.EventCollector;
import View.GraphicalUserInterface;

import java.awt.event.ActionEvent;

public class StartGameAdapter implements java.awt.event.ActionListener {
    EventCollector controller;
    GraphicalUserInterface userInterface;

    public StartGameAdapter(EventCollector controller, GraphicalUserInterface userInterface){
        this.controller = controller;
        this.userInterface = userInterface;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Configuration.info("Starting a new game");
        controller.performAction("StartGame");
        userInterface.startGame();
    }
}