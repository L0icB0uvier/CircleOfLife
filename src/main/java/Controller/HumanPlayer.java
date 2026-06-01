package Controller;

import Global.Configuration;
import Model.Coordinate;
import Model.Game;
import Model.MatchUtils;
import Model.Move;

public class HumanPlayer extends Player {

    public HumanPlayer(Controller controller, Game game, String name){
        this.game = game;
        this.name = name;
        this.controller = controller;
        isAI = false;
        this.canPlay = true;
    }

    /**
     * Envoi le coup du joueur au modèle.
     * @param l Ligne du clic.
     * @param c Colonne du clic.
     */
    @Override
    public void handleClick(int l, int c) {
        if(!game.isMoveValid(new Coordinate(c, l))){
            Configuration.warning(String.format("Move Invalide - %d:%d", c, l));
            if(MatchUtils.isInsideBoard(new Coordinate(l, c))){
                controller.createImpossibleMoveAnimation(l, c);
            }
            return;
        }
        game.playMove(new Move(game.getMatch(), l, c));
    }
}
