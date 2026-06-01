package Controller;

import Controller.Animation.Animation;
import Controller.Animation.ImpossibleMoveAnimation;
import Controller.Animation.ScoreAnimation;
import Global.Configuration;
import Model.*;
import Patterns.Observer;
import Patterns.ScoreEventObserver;
import View.EventCollector;
import Global.Settings;
import View.UserInterface;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Fait l'interface entre la vue et le modèle.
 */
public class Controller implements EventCollector, Observer, ScoreEventObserver {
    Game game;
    UserInterface view;
    Player[] players;
    Player currentPlayer;
    List<Animation> animations;

    public Controller(Game game){
        this.game = game;
        animations = new ArrayList<>();
        game.addUpdateObserver(this);
        game.addUpdateScoreObserver(this);
        players = new Player[2];
    }

    /**
     * Transmet le clic reçu de la vue au joueur actif.
     * @param l La ligne du plateau sur laquelle le joueur a cliqué.
     * @param c La colonne du plateau sur laquelle le joueur a cliqué.
     */
    public void handleClick(int l, int c){
        if(currentPlayer == null)
            return;
        currentPlayer.handleClick(l, c);
    }

    @Override
    public void performAction(String actionName) {
        switch (actionName) {
            case "FullScreen" -> view.toggleFullscreen();
            case "NewGame" -> createNewGame();
            case "StartGame" -> startGame();
            case "ContinueGame" -> continueGame();
            case "QuitGame" -> quitGame();
            case "GiveUp" -> giveUp();
            case "Replay" -> replay();
            case "ToggleReviewMode" -> toggleReviewMode();
            case "Undo" -> handleUndo();
            case "Redo" -> handleRedo();
            case "UndoAll" -> handleUndoAll();
            case "RedoAll" -> handleRedoAll();
            case "Save" -> handleSave();
        }
    }

    private void quitGame() {
        if(currentPlayer != null)
            currentPlayer.endTurn();

        cleanAnimations();
    }

    /**
     * Demande au modèle de faire un Undo.
     */
    private void handleUndo(){
         
        game.undo();
    }

    /**
     * Demande au modèle de faire un Redo
     */
    private void handleRedo(){
         
        game.redo();
    }

    /**
     * Demande au modèle de Undo tout l'historique.
     */
    private void handleUndoAll(){
        game.undoAll();
    }

    /**
     * Demande au modèle de Redo tout l'historique.
     */
    private void handleRedoAll(){
        game.redoAll();
    }

    /**
     * Crée une nouvelle partie à partir des settings défini dans la configuration.
     */
    private void createNewGame() {
        view.updateSettings();
        Settings matchSettings = Configuration.getSettings();
        game.createMatch(matchSettings.getPlayer1Settings().getName(), matchSettings.getPlayer2Settings().getName(), matchSettings.getStartingPlayerSetting());
        players[0] = Player.createPlayer(this, matchSettings.getPlayer1Settings(), game);
        players[1] = Player.createPlayer(this, matchSettings.getPlayer2Settings(), game);
        startGame();
    }

    /**
     * Charge la dernière partie à partir des données sauvegardées du joueur.
     * @return true si la partie a été chargée avec succès, false sinon
     */
    public boolean continueGame(){
        if (!GameDataManager.hasSaveFile()) return false;
        blockAi();
        try {
            if (!GameDataManager.loadMatch(game, GameDataManager.getSaveFiles().get(0)))
                return false;
            Settings matchSettings = Configuration.getSettings();
            players[0] = Player.createPlayer(this, matchSettings.getPlayer1Settings(), game);
            players[1] = Player.createPlayer(this, matchSettings.getPlayer2Settings(), game);
            startGame();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private void blockAi() {
        for (Player player : players) {
            if (player.isAI())
                player.canPlay = false;
        }
    }

    /**
     * Charge une partie à partir des données sauvegardées du joueur.
     * @param gameFile le nom du fichier de la partie à charger
     * @return true si la partie a été chargée avec succès, false sinon
     */
    public boolean loadGame(String gameFile){
        blockAi();
        try {
            if(!GameDataManager.loadMatch(game, gameFile))
                return false;
            Settings matchSettings = Configuration.getSettings();
            players[0] = Player.createPlayer(this, matchSettings.getPlayer1Settings(), game);
            players[1] = Player.createPlayer(this, matchSettings.getPlayer2Settings(), game);
            startGame();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Demande la suppression d'une sauvegarde.
     * @param gameFile Le nom du fichier à supprimer.
     */
    public void deleteGame(String gameFile) {
        if(!GameDataManager.deleteMatch(gameFile)) 
            Configuration.warning("Pas de fichier à supprimer trouve pour " + gameFile + ".save");
    }

    /**
     * Renomme un fichier de sauvegarde.
     * @param fileName Le nom du fichier original.
     * @param newName Le nouveau nom.
     * @return Le nom du fichier renommé si le renommage a fonctionné. Le nom de l'ancien fichier si le renommage a échoué et un empty String si le fichier à renommer n'existe pas.
     */
    public String renameGame(String fileName, String newName) {
        return GameDataManager.renameMatch(fileName, newName);
    }

    /**
     * Demande au GameDataManager de sauvegarder le match en cours.
     */
    private void handleSave() {
        try {
            GameDataManager.saveMatch(game.getMatch(), Configuration.getSettings());
             
        } catch (Exception e) {
            Configuration.warning("Error while saving match");
            throw new RuntimeException(e);
        }
    }

    /**
     * Lance la partie.
     */
    private void startGame(){
        game.startPlaying();
        updateCurrentPlayer();
        currentPlayer.startTurn();
    }

    /**
     * Demande au modèle l'abandon du match en cours par le joueur actif.
     */
    private void giveUp(){
        currentPlayer.endTurn();
        cleanAnimations();
        game.giveUp();
    }

    private void cleanAnimations(){
        Iterator<Animation> it = animations.iterator();
        while (it.hasNext()) {
            Animation anim = it.next();
            anim.endAnimation();
            it.remove();
        }
    }

    /**
     * Demande au modèle de rejouer le match et relance les PlayerControllers.
     */
    private void replay(){
        game.replay();
        cleanAnimations();
        updateCurrentPlayer();
        currentPlayer.startTurn();
    }

    /**
     * Demande au modèle de toggle le mode review.
     */
    private void toggleReviewMode(){
        game.toggleReviewMode();
    }

    /**
     * Met à jour le joueur actif à partir des données du modèle.
     */
    private void updateCurrentPlayer(){
        currentPlayer = players[game.getCurrentPlayerIndex()];
    }

    @Override
    public void addUserInterface(UserInterface i) {
        view = i;
    }

    @Override
    public void animTic() {
        if(animations.isEmpty())
            return;

        Iterator<Animation> it = animations.iterator();
        while (it.hasNext()) {
            Animation anim = it.next();
            anim.tictac();
            if (anim.isOver()) {
                 
                it.remove();
            }
        }
    }

    public void animateScore(Set<Coordinate> groupCoords, int scoreGained, int player, float progress){
        view.animateScore(groupCoords, scoreGained, player, progress);
    }

    public void animateImpossibleMoveAnimation(String id, int l, int c, float progress){
        view.animateImpossibleMove(id, l, c, progress);
    }

    @Override
    public void update() {
        if(currentPlayer == null) return;

        if(game.isGameOver() || game.isReviewModeActive()) {
            currentPlayer = null;
            return;
        }

        currentPlayer.endTurn();

        updateCurrentPlayer();
        currentPlayer.startTurn();
    }

    @Override
    public void onScoreUpdated(Map<Set<Coordinate>, Integer> eatenInfo, int player) {
        for (Map.Entry<Set<Coordinate>, Integer> entry : eatenInfo.entrySet()) {
             
            animations.add(new ScoreAnimation(0.015f, entry.getKey(), entry.getValue(), player, this));
        }
    }

    public void createImpossibleMoveAnimation(int l, int c){
        animations.add(new ImpossibleMoveAnimation(this, 0.015f, l, c, UUID.randomUUID().toString()));
    }
}
