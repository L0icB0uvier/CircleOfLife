package View.Adapter;
import View.EventCollector;
import View.GraphicalUserInterface;

import java.awt.event.ActionEvent;


public class ContinueGameAdapter implements java.awt.event.ActionListener{
    EventCollector controller;
    GraphicalUserInterface userInterface;

    public ContinueGameAdapter(EventCollector controller, GraphicalUserInterface userInterface){
        this.controller = controller;
        this.userInterface = userInterface;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.performAction("ContinueGame");
        userInterface.startGame();
    }
}
