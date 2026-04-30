package Controller;

import Controller.IA.AI;
import Model.Game;
import Model.PlayerData;
import View.PlayerSettings;

public class Player {
    PlayerData playerData;

    boolean isAI;

    public boolean isAI(){
        return isAI;
    }

    public static Player createPlayer(PlayerSettings playerSettings, Game game){
        if(playerSettings.isAI()){
            return new AIPlayer(game, AI.createAI(game.getMatch(), playerSettings.getAiLevel()));
        }
        else{
            return new HumanPlayer();
        }
    }

    public void startTurn(){}

    public void endTurn(){}
}
