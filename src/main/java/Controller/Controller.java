package Controller;

import Global.Configuration;
import Model.Game;
import Model.GameDataManager;
import Model.Move;
import Patterns.Observateur;
import View.EventCollector;
import View.Settings;
import View.UserInterface;

import java.io.FileNotFoundException;

public class Controller implements EventCollector, Observateur {
    Game game;
    UserInterface view;
    Player[] players;
    Player currentPlayer;

    public Controller(Game game){
        this.game = game;
        game.ajouteObservateur(this);
        players = new Player[2];
    }

    public void handleClic(int l, int c){
        if(currentPlayer.isAI() || !isClicValid(l, c)) return;
        playMove(l, c);
    }


    private boolean isClicValid(int l, int c){
        // A compléter
        return true;
    }

    public void playMove(int l, int c) {
        game.playMove(new Move(game.getMatch(), l, c));
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

    private void handleUndo(){
        Configuration.info("Undo");
        game.undo();
    }

    private void handleRedo(){
        Configuration.info("Redo");
        game.redo();
    }

    private void createNewGame() {
        view.updateSettings();
        Settings matchSettings = Configuration.getSettings();
        game.createMatch();
        players[0] = Player.createPlayer(matchSettings.getPlayer1Settings(), game);
        players[1] = Player.createPlayer(matchSettings.getPlayer2Settings(), game);
    }

    private void continueGame(){
        try {
            GameDataManager.loadMatch(game);
            Settings matchSettings = Configuration.getSettings();
            players[0] = Player.createPlayer(matchSettings.getPlayer1Settings(), game);
            players[1] = Player.createPlayer(matchSettings.getPlayer2Settings(), game);
            startGame();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void startGame(){
        updateCurrentPlayer();
        currentPlayer.startTurn();
    }

    @Override
    public void addUserInterface(UserInterface i) {
        view = i;
    }

    @Override
    public void update() {
        if(currentPlayer == null) return;
        currentPlayer.endTurn();
        updateCurrentPlayer();
        currentPlayer.startTurn();
    }

    private void updateCurrentPlayer(){
        currentPlayer = players[game.getCurrentPlayerIndex()];
    }
}
