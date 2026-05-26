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
    public void createMatch(String name1, String name2, int startingPlayer){
        match = new Match(name1, name2, startingPlayer);
    }

    /**
     * Retourne l'indice du joueur actif.
     * @return L'indice du joueur actif.
     */
    public int getCurrentPlayerIndex(){
        return match.currentPlayerIndex;
    }

    /**
     * Retourne l'indice de l'adversaire du joueur actif.
     * @return L'indice de l'adversaire du joueur actif.
     */
    public int getOpponentPlayerIndex() {return match.getOpponentPlayerIndex();}

    /**
     * Retourne le gagnant de la partie s'il existe.
     * @return L'indice du gagnant s'il existe, sinon -1.
     */
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

    /**
     * Indique si la partie est terminée.
     * @return true si la partie est terminée, false sinon.
     */
    public boolean isGameOver(){
        return match.isGameOver();
    }

    /**
     * Indique si le mode analyse est actif.
     * @return true si le mode analyse et actif, false sinon.
     */
    public boolean isReviewModeActive(){
        return match.isReviewModeActive();
    }

    /**
     * Joue un move puis appel update pour notifier les observateurs.
     * @param move Le Move à jouer.
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

    /**
     * Indique s'il est possible d'annuler la dernière action.
     * @return true s'il est possible d'annuler la dernière action, false sinon.
     */
    public boolean canUndo(){
        return match.canUndo();
    }

    /**
     * Refait le dernier Move annulé puis appel update pour notifier les observateurs.
     */
    public void redo(){
        Configuration.info("Game received Redo.");
        if(!canRedo()) return;

        if(match.isReviewModeActive()){
            switch (match.winType){
                case SCORE -> {
                    match.toggleCurrentPlayer();
                    Configuration.info("Player " + (match.currentPlayerIndex + 1) + " turn");
                    match.redo();
                }
                case FILL, GIVE_UP -> {
                    match.redo();
                    match.toggleCurrentPlayer();
                    Configuration.info("Player " + (match.currentPlayerIndex + 1) + " turn");
                }
            }
        }
        else{
            match.redo();
            match.toggleCurrentPlayer();
            Configuration.info("Player " + (match.currentPlayerIndex + 1) + " turn");
        }
        update();
    }

    public boolean canRedo(){
        return match.canRedo();
    }

    /**
     * Annuler tous les coups jusqu'au début de l'historique.
     */
    public void undoAll(){
        Configuration.info("Game received Undo all.");
        while (canUndo())
            undo();
    }

    /**
     * Refaire tous les coups jusqu'à la fin de l'historique.
     */
    public void redoAll(){
        Configuration.info("Game received Redo all");
        while (match.canRedo())
            redo();
    }

    /**
     * Termine le match et déclare le joueur adverse du joueur actif vainqueur.
     */
    public void giveUp(){
        match.gameOver(match.getOpponentPlayerIndex(), WinType.GIVE_UP);
        update();
    }

    /**
     * Toogle le mode analyse.
     */
    public void toggleReviewMode(){
        if(match.isReviewModeActive()){
            exitReviewMode();
        }
        else
            enterReviewMode();
    }

    /**
     * Rentre en mode analyse.
     */
    private void enterReviewMode(){
        match.enterReviewMode();
        update();
    }

    /**
     * Sort du mode analyse.
     */
    private void exitReviewMode(){
        redoAll();
        match.exitReviewMode();
        update();
    }

    /**
     * Rejouer le match en changeant le joueur qui commence.
     */
    public void replay(){
        match.initMatch();
        match.toggleStartingPlayer();
        update();
    }

    /**
     * Récupère le nombre de coups joués depuis le début de la partie sans compter les coups annulés.
     * @return Le nombre de coups joués depuis le début de la partie.
     */
    public int getNumberOfMovePlayed(){
        return match.getPastCount();
    }
}
