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
    public void createMatch(String name1, String name2){
        match = new Match(name1, name2);
    }

    public int getCurrentPlayerIndex(){
        return match.currentPlayerIndex;
    }

    public int getOpponentPlayerIndex() {return match.getOpponentPlayerIndex();}

    public int getWinningPlayer(){
        return match.getWinner();
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

    public boolean isGameOver(){
        return match.isGameOver();
    }

    public boolean isReviewModeActive(){
        return match.isReviewModeActive();
    }

    /**
     * Joue un move puis appel update pour notifier les observateurs.
     * @param move
     */
    public void playMove(Move move){
        if(match.isGameOver())
            return;
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

    public void giveUp(){
        match.toggleCurrentPlayer();
        match.gameOver();
        update();
    }

    public void enterReviewMode(){
        match.enterReviewMode();
        update();
    }

    public void replay(){
        match.initMatch();
        update();
    }
}
