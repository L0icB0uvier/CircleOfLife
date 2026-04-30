package Model;

import Global.Configuration;

import java.util.Random;

public class Match extends History<Move> {
    public final static int playerOneIndex=0;
    public final static int playerTwoIndex=1;

    PlayerData[] players = new PlayerData[2];
    int currentPlayerIndex;

    int[][] boardState;
    Critter[] critters;

    public Match(){
        players[0] = new PlayerData();
        players[1] = new PlayerData();

        initMatch();
    }

    public void playMove(int l, int c){
        if (Math.max(Math.abs(l-4), Math.abs(c-4)) > 4 || boardState[l][c] != 0){ // invalid Move
            return;
        }
        else{
            boardState[l][c] = currentPlayerIndex + 1; // playerOne <-> 1 ; playerTwo <-> 2
            // TODO : update list of Shapes and currentPlayer score if necessary
            updateCritters(l, c);
        }
    }

    @Override
    public void apply(Move newMove) {
        super.apply(newMove);
        if(isGameOver()){
            Configuration.info("Player " + currentPlayerIndex + " won!");
            players[currentPlayerIndex].score += 1;
            initMatch();
        }
    }

    public void restoreState(int[] state){
        toggleCurrentPlayer();
    }

    private void toggleCurrentPlayer(){
        currentPlayerIndex = currentPlayerIndex == 0 ? 1 : 0;
        Configuration.info("Player " + (currentPlayerIndex + 1) + " turn");
    }

    public boolean isGameOver(){
        return (players[0].getScore() >= 20 || players[1].getScore() >= 20 || false); // TODO : add "can't play" condition
    }

    public void initMatch() {
        reset();
        boardState = new int[8][8];
        currentPlayerIndex = new Random().nextInt(2) == 0? 0: 1;
        Configuration.info("New game: Player " + (currentPlayerIndex + 1) + " starts");
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public PlayerData[] getPlayerData(){
        return players;
    }

    private void updateCritters(int l, int c){
        for (Critter C : critters){
            // TODO : 1/search for the critter the active player evolved, if none create new one of type 0
            //        2/feed the critter in question and increase the active player's score accordingly
        }
    }
}
