package Model;

import Global.Configuration;
import Patterns.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Classe principale du modèle qui gère les matchs.
 */
public class Game extends Observable {
    private Match match;
    private boolean skipAnimations = false;

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
     * @return true si les coordonnées sont valides, false sinon.
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
             
            return;
        }
        match.apply(move);
        checkScoreChange();
        match.endTurn();
        if(match.isGameOver()){
             
        }
        update();
    }

    private void checkScoreChange() {
        if(!match.isPlaying() || skipAnimations) return;
        if(!match.previouslyEatenCritters.isEmpty()){
            Map<Set<Coordinate>, Integer> eatenData = new HashMap<>();
            for (Critter previouslyEatenCritter : match.previouslyEatenCritters) {
                eatenData.put(previouslyEatenCritter.stonesCoordinates(), MatchUtils.calculatePointEarned(Set.of(previouslyEatenCritter)));
            }
            updateScore(eatenData, match.getCurrentPlayerIndex());
        }
    }

    /**
     * Annule le dernier Move puis appel update pour notifier les observateurs.
     */
    public void undo(){
         
        if (match.isGameOver() && !match.isReviewModeActive())
            return;
        if(!canUndo()) return;

        match.undo();
        match.toggleCurrentPlayer();
         
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
         
        if (match.isGameOver() && !match.isReviewModeActive())
            return;
        if(!canRedo()) return;

        if(match.isReviewModeActive()){
            switch (match.winType){
                case SCORE -> {
                    match.toggleCurrentPlayer();
                    match.redo();
                    checkScoreChange();
                }
                case FILL, GIVE_UP -> {
                    match.redo();
                    checkScoreChange();
                    match.toggleCurrentPlayer();
                              
                }
            }
            
        }
        else{
            match.redo();
            checkScoreChange();
            match.toggleCurrentPlayer();
             
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
         
        while (canUndo())
            undo();
    }

    /**
     * Refaire tous les coups jusqu'à la fin de l'historique.
     */
    public void redoAll(){
         
        skipAnimations = true;
        while (match.canRedo())
            redo();
        skipAnimations = false;
    }

    /**
     * Termine le match et déclare le joueur adverse du joueur actif vainqueur.
     */
    public void giveUp(){
        match.gameOver(match.getOpponentPlayerIndex(), WinType.GIVE_UP);
        update();
    }

    /**
     * Toggle le mode analyse.
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

    public void startPlaying() {
        match.startPlaying();
    }
}
