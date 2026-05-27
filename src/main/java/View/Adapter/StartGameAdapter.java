package View.Adapter;

import Global.Configuration;
import Model.GameDataManager;
import View.EventCollector;
import View.GraphicalNewGame;
import View.GraphicalUserInterface;

import java.awt.event.ActionEvent;

public class StartGameAdapter implements java.awt.event.ActionListener {
    EventCollector controller;
    GraphicalUserInterface userInterface;
    GraphicalNewGame graphicalNewGame;

    public StartGameAdapter(EventCollector controller, GraphicalUserInterface userInterface, GraphicalNewGame graphicalNewGame){
        this.controller = controller;
        this.userInterface = userInterface;
        this.graphicalNewGame = graphicalNewGame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
         
        String[] res = graphicalNewGame.getTextFields();
        for (int i = 0; i < res.length; i++) {
            if (GameDataManager.newNameContainsSeparator(res[i])) {
                userInterface.errorPopup(
                        "Nom incorrecte pour joueur " + (i + 1) + ", les underscores ('_') sont interdits",
                        graphicalNewGame);
                return;
            }
        }
        controller.performAction("StartGame");
        userInterface.startGame();
    }
}