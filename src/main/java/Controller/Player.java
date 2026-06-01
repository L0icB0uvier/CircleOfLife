package Controller;

import Controller.IA.AI;
import Model.Game;
import Global.PlayerSettings;

/**
 * Classe de base pour représenter un joueur.
 */
public class Player {
    Game game;
    Controller controller;
    boolean isAI;
    String name;

    /**
     * Indique si le joueur est une intelligence artificielle.
     * @return true si AI, false sinon.
     */
    public boolean isAI(){
        return isAI;
    }

    public String getName() {
        return name;
    }

    /**
     * Méthode permettant de créer la bonne sous classe en fonction des PlayerSettings du joueur.
     * @param playerSettings Settings du joueur pour lequel créer une instance de Player.
     * @param game Référence à la classe Game nécessaire aux IA.
     * @return Une instance de la sous classe de Player. Un HumanPlayer ou un AIPlayer en fonction des PlayerSettings.
     */
    public static Player createPlayer(Controller controller, PlayerSettings playerSettings, Game game){
        if(playerSettings.isAI()){
            return new AIPlayer(game, AI.createAI(game.getMatch(), playerSettings.getAiLevel()), playerSettings.getName());
        }
        else{
            return new HumanPlayer(controller, game, playerSettings.getName());
        }
    }

    /**
     * Commence le tour d'un joueur. Lance l'IA dans le cas d'un joueur IA.
     */
    public void startTurn(){}

    /**
     * Termine le tour d'un joueur. Arrête l'IA dans le cas d'un joueur IA.
     */
    public void endTurn(){}

    /**
     * Le joueur a reçu un clic de la vue. Ignoré par les AIPlayer et implémenté dans HumanPlayer.
     * @param l Ligne du clic.
     * @param c Colonne du clic.
     */
    public void handleClick(int l, int c){

    }
}
