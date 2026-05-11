package Controller;

import Model.Coordinate;
import Model.Game;
import Model.Move;

public class HumanPlayer extends Player {

    public HumanPlayer(Game game){
        this.game = game;
        isAI = false;
    }

    /**
     * Envoi le coup du joueur au modèle.
     * @param l Ligne du clic.
     * @param c Colonne du clic.
     */
    @Override
    public void handleClick(int l, int c) {
        if(!game.isMoveValid(new Coordinate(c, l)))
            return;
        game.playMove(new Move(game.getMatch(), l, c));
    }
}
