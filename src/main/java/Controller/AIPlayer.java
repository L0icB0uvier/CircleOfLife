package Controller;

import Controller.IA.AI;
import Model.Game;

public class AIPlayer extends Player {
    AI ai;
    Game game;

    public AIPlayer(Game game, AI ai){
        super();
        isAI = true;
        this.ai = ai;
        this.game = game;
    }

    @Override
    public void startTurn() {
        ai.play().thenAccept(move -> {
            game.playMove(move);
        });
    }

    @Override
    public void endTurn() {
        ai.cancel();
    }
}