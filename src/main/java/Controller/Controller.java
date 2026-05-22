package Controller;

import Global.Configuration;
import Model.*;
import Patterns.Observer;
import View.EventCollector;
import Global.Settings;
import View.UserInterface;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class Controller implements EventCollector, Observer {
    Game game;
    UserInterface view;
    Player[] players;
    Player currentPlayer;

    public Controller(Game game){
        this.game = game;
        game.addObserver(this);
        players = new Player[2];
    }

    public void handleClick(int l, int c){
        currentPlayer.handleClick(l, c);
    }

    @Override
    public void performAction(String actionName) {
        switch (actionName){
            case "NewGame":
                createNewGame();
                break;
            case "StartGame":
                startGame();
                break;
            case "ContinueGame":
                continueGame();
                break;
            case "GiveUp":
                giveUp();
                break;
            case "Replay":
                replay();
                break;
            case "Review":
                review();
                break;
            case "Quit":
                break;
            case "Undo":
                handleUndo();
                break;
            case "Redo":
                handleRedo();
                break;
            case "Save":
                try {
                    GameDataManager.saveMatch(game.getMatch(), Configuration.getSettings());
                    Configuration.info("Match successfully saved");
                } catch (Exception e) {
                    Configuration.warning("Error while saving match");
                    throw new RuntimeException(e);
                }
                break;
        }
    }

    /**
     * Gère le undo.
     */
    private void handleUndo(){
        Configuration.info("Undo");
        game.undo();
    }

    /**
     * Gère le redo
     */
    private void handleRedo(){
        Configuration.info("Redo");
        game.redo();
    }

    /**
     * Crée une nouvelle partie à partir des settings défini dans la configuration.
     */
    private void createNewGame() {
        view.updateSettings();
        Settings matchSettings = Configuration.getSettings();
        game.createMatch(matchSettings.getPlayer1Settings().getName(), matchSettings.getPlayer2Settings().getName(), matchSettings.getStartingPlayerSetting());
        players[0] = Player.createPlayer(matchSettings.getPlayer1Settings(), game);
        players[1] = Player.createPlayer(matchSettings.getPlayer2Settings(), game);
        startGame();
    }

    /**
     * Charge une partie à partir des données sauvegardées du joueur.
     */
    private void continueGame(){
        if (!GameDataManager.hasSaveFile()) return;
        try {
            GameDataManager.loadMatch(game, GameDataManager.getSaveFiles().get(0));
            Settings matchSettings = Configuration.getSettings();
            players[0] = Player.createPlayer(matchSettings.getPlayer1Settings(), game);
            players[1] = Player.createPlayer(matchSettings.getPlayer2Settings(), game);
            startGame();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Charge une partie à partir des données sauvegardées du joueur.
     * @param gameFile le nom du fichier de la partie à charger
     */
    public void loadGame(String gameFile){
        try {
            GameDataManager.loadMatch(game, gameFile);
            Settings matchSettings = Configuration.getSettings();
            players[0] = Player.createPlayer(matchSettings.getPlayer1Settings(), game);
            players[1] = Player.createPlayer(matchSettings.getPlayer2Settings(), game);
            startGame();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteGame(String gameFile) {
        if(!GameDataManager.deleteMatch(gameFile)) 
            Configuration.warning("Pas de fichier à supprimer trouve pour " + gameFile + ".save");
    }

    public String renameGame(String fileName, String newName) {
        return GameDataManager.renameMatch(fileName, newName);
    }

    /**
     * Lance la partie.
     */
    private void startGame(){
        updateCurrentPlayer();
        currentPlayer.startTurn();
    }

    private void giveUp(){
        currentPlayer.endTurn();
        game.giveUp();
    }

    private void replay(){
        game.replay();
    }

    private void review(){
        game.enterReviewMode();
    }

    @Override
    public void addUserInterface(UserInterface i) {
        view = i;
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

    /**
     * Met à jour le joueur actif à partir des données du modèle.
     */
    private void updateCurrentPlayer(){
        currentPlayer = players[game.getCurrentPlayerIndex()];
    }
}
