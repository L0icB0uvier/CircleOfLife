package View.Adapter;
import View.EventCollector;
import View.GraphicalUserInterface;

import java.awt.event.ActionEvent;

import Controller.Controller;


public class ContinueGameAdapter implements java.awt.event.ActionListener{
    EventCollector controller;
    GraphicalUserInterface userInterface;

    public ContinueGameAdapter(EventCollector controller, GraphicalUserInterface userInterface){
        this.controller = controller;
        this.userInterface = userInterface;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!((Controller)controller).continueGame()) {
            userInterface.errorPopup("Erreur, impossible de charger le dernier match" ,userInterface.getGraphicalMainMenu());
            return;
        }
        userInterface.startGame();
    }
}
