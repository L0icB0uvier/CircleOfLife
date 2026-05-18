package Controller;

import Controller.IA.AI;
import Model.Game;

public class AIPlayer extends Player {
    AI ai;

    public AIPlayer(Game game, AI ai, String name){
        super();
        isAI = true;
        this.ai = ai;
        this.game = game;
        this.name = name;
    }

    @Override
    public void startTurn() {
        if (!game.getMatch().canRedo()) {
            ai.play().thenAccept(move -> {
                game.playMove(move);
            });
        }
    }

    @Override
    public void endTurn() {
        ai.cancel();
    }
}