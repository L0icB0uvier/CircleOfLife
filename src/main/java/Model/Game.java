package Model;

import Patterns.Observable;

public class Game extends Observable {
    private Match match;

    public void createMatch(){
        match = new Match();
    }

    public int getCurrentPlayerIndex(){
        return match.currentPlayerIndex;
    }

    public Match getMatch(){
        return match;
    }

    public void playMove(Move move){
        match.apply(move);
        update();
    }

    public void undo(){
        if(!match.canUndo()) return;
        match.undo();
        update();
    }

    public void redo(){
        if(!match.canRedo()) return;
        match.redo();
        update();
    }
}
