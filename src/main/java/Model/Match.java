package Model;

import Global.Configuration;

import java.util.ArrayList;
import java.util.Random;

public class Match extends History<Move> {
    public final static int playerOneIndex=0;
    public final static int playerTwoIndex=1;

    PlayerData[] players = new PlayerData[2];
    int currentPlayerIndex;

    int[][] boardState; // 0 tile can be played, -1 can't be played by PayerOne, -2 can't be played by playerTwo
                        // 1 is occupied by playerOne, 2 by playerTwo

    ArrayList<Critter> critters;

    public Match(){
        players[0] = new PlayerData();
        players[1] = new PlayerData();

        initMatch();
    }

    public void initMatch() {
        reset();
        boardState = new int[8][8];
        currentPlayerIndex = new Random().nextInt(2) == 0? 0: 1;
        Configuration.info("New game: Player " + (currentPlayerIndex + 1) + " starts");
    }

    public void playMove(int l, int c){
        /// 1 - check if Move is valid
        if (Math.max(Math.abs(l-4), Math.abs(c-4)) > 4 || boardState[l][c] != 0 ){ // invalid Move
            return;
        }
        else{
            /// 2 - update corresponding tile
            boardState[l][c] = currentPlayerIndex + 1; // playerOne <-> 1 ; playerTwo <-> 2
            /// 3 - update Critters : evolve or reproduce, then feed
            // TODO : update list of Critters and currentPlayer score if necessary
            ArrayList<Critter> evolutionCandidates = new ArrayList<>();
            for (Critter C : critters){
                if (C.canEvolve(l, c)){
                    evolutionCandidates.add(C);
                }
            }
            Critter C;
            if (!evolutionCandidates.isEmpty()) {
                C = evolve(evolutionCandidates);
            }
            else{
                C = new Critter(l, c, currentPlayerIndex);
            }
            critters.add(C);
            feed(C);
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
        return (players[currentPlayerIndex].getScore() >= 20 || players[currentPlayerIndex].getPlayableTilesNumber() == 0);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public PlayerData[] getPlayerData(){
        return players;
    }

}
