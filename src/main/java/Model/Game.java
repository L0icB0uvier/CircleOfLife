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

    /**
     * Joue un move puis appel update pour notifier les observateurs.
     * @param move
     */
    public void playMove(Move move){
        match.apply(move);
        update();
    }

    /**
     * Annule le dernier Move puis appel update pour notifier les observateurs.
     */
    public void undo(){
        if(!match.canUndo()) return;
        match.undo();
        update();
    }

    /**
     * Refait le dernier Move annulé puis appel update pour notifier les observateurs.
     */
    public void redo(){
        if(!match.canRedo()) return;
        match.redo();
        update();
    }
}
