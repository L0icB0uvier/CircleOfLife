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
                        "Nom incorrecte pour joueur " + (i + 1) + ", contient un caractère interdit",
                        graphicalNewGame);
                return;
            } else if (GameDataManager.nameTooLongPlayer(res[i])) {
                userInterface.errorPopup(
                        "Nom incorrecte pour joueur " + (i + 1) + ", le nom donné est trop long (maximum 10 caractères, actuellement " + res[i].length() + " caractères)",
                        graphicalNewGame);
                return;
            }
        }
        controller.performAction("StartGame");
        userInterface.startGame();
    }
}