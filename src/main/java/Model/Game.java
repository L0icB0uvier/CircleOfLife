package Model;

import Global.Configuration;
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

    /**
     * Crée un nouveau match.
     */
    public void createMatch(String name1, String name2, int startingPlayer){
        match = new Match(name1, name2, startingPlayer);
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
        if(match.isGameOver()){
            Configuration.info(String.format("Player %d won!", match.winner + 1));
            return;
        }
        match.apply(move);
        match.endTurn();
        if(match.isGameOver()){
            Configuration.info(String.format("Player %d won!", match.winner + 1));
        }
        update();
    }

    /**
     * Annule le dernier Move puis appel update pour notifier les observateurs.
     */
    public void undo(){
        Configuration.info("Game received Undo.");
        if(canUndo() == false) return;

        match.undo();
        match.toggleCurrentPlayer();
        Configuration.info("Player " + (match.currentPlayerIndex + 1) + " turn");
        update();
    }

    public boolean canUndo(){
        if (match.isReviewModeActive()) {
            return match.getPastCount() > 1;
        }

        else{
            return match.canUndo();
        }
    }

    /**
     * Refait le dernier Move annulé puis appel update pour notifier les observateurs.
     */
    public void redo(){
        Configuration.info("Game received Redo.");
        if(!match.canRedo()) return;

        if(match.isReviewModeActive()){
            if(match.wonByScore){
                match.toggleCurrentPlayer();
                Configuration.info("Player " + (match.currentPlayerIndex + 1) + " turn");
                match.redo();
            }
            else{
                match.redo();
                match.toggleCurrentPlayer();
                Configuration.info("Player " + (match.currentPlayerIndex + 1) + " turn");
            }
        }
        else{
            match.redo();
            match.toggleCurrentPlayer();
            Configuration.info("Player " + (match.currentPlayerIndex + 1) + " turn");
        }
        update();
    }

    public void undoAll(){
        Configuration.info("Game received Undo all.");
        while (canUndo())
            undo();
    }

    public void redoAll(){
        Configuration.info("Game received Redo all");
        while (match.canRedo())
            redo();
    }

    public void giveUp(){
        match.gameOver(match.getOpponentPlayerIndex());
        update();
    }

    public void toggleReviewMode(){
        if(match.isReviewModeActive()){
            exitReviewMode();
        }
        else
            enterReviewMode();
    }

    private void enterReviewMode(){
        match.enterReviewMode();
        update();
    }

    private void exitReviewMode(){
        match.exitReviewMode();
        update();
    }

    public void replay(){
        match.initMatch();
        match.toggleStartingPlayer();
        update();
    }

    public int getNumberOfMovePlayed(){
        return match.getPastCount();
    }
}
