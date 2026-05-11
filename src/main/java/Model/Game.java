package Model;

import Patterns.Observable;

/**
 * Classe principale du modèle qui gère les matchs.
 */
public class Game extends Observable {
    private Match match;

    /**
     * Crée un nouveau match.
     */
    public void createMatch(){
        match = new Match();
    }

    public int getCurrentPlayerIndex(){
        return match.currentPlayerIndex;
    }

    /**
     * Vérifie si le coup est valide dans le match en cours.
     * @param coordinate Coordonnées du coup.
     * @return true si les coordonnées sont valide, false sinon.
     */
    public boolean isMoveValid(Coordinate coordinate){
        return match.isMoveValid(getCurrentPlayerIndex(), coordinate.line(), coordinate.col());
    }

    /**
     * Retourne l'instance du match en cours.
     * @return Le match en cours.
     */
    public Match getMatch(){
        return match;
    }

    /**
     * Joue un move puis appel update pour notifier les observateurs.
     * @param move
     */
    public void playMove(Move move){
        match.apply(move);
        match.endTurn();
        update();
    }

    /**
     * Annule le dernier Move puis appel update pour notifier les observateurs.
     */
    public void undo(){
        if(!match.canUndo()) return;
        match.undo();
        match.toggleCurrentPlayer();
        update();
    }

    /**
     * Refait le dernier Move annulé puis appel update pour notifier les observateurs.
     */
    public void redo(){
        if(!match.canRedo()) return;
        match.redo();
        match.toggleCurrentPlayer();
        update();
    }
}
