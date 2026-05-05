package Model;

import Global.Configuration;

import java.util.*;

public class Match extends History<Move> {
    public final static int playerOneIndex=0;
    public final static int playerTwoIndex=1;

    PlayerData[] players = new PlayerData[2];
    int currentPlayerIndex;

    int[][] boardState; // 0 tile can be played, -1 can't be played by PayerOne, -2 can't be played by playerTwo
                        // 1 is occupied by playerOne, 2 by playerTwo

    Set<Critter> critters;

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
        if (Math.max(Math.abs(l-4), Math.abs(c-4)) > 4 || boardState[l][c] != -(currentPlayerIndex + 1 )){ // invalid Move
            return;
        }
        else{
            /// 2 - update corresponding tile
            boardState[l][c] = currentPlayerIndex + 1; // playerOne <-> 1 ; playerTwo <-> 2

            /// 3 - update Critters : evolve or reproduce, then feed

            Coordinate newStoneCoordinate = new Coordinate(l, c);

            // TODO : update list of Critters and currentPlayer score if necessary
            var neighbors = getCurrentPlayerNeighborsCritters(currentPlayerIndex, newStoneCoordinate);
            Critter newCritter;

            if(neighbors.isEmpty()){
                newCritter = new Critter(newStoneCoordinate, currentPlayerIndex);
            }
            else{
                newCritter = evolve(neighbors, newStoneCoordinate);
            }

            critters.add(newCritter);

            feed(newCritter);
        }
    }

    /**
     * Evolue un ou plusieurs critters.
     * @param evolutionCandidates Les critters existants à fusionner pour l'évolution.
     * @param newStoneCoord Coordonnées de la dernière pierre posée.
     * @return Le critter evolué.
     */
    public Critter evolve(Set<Critter> evolutionCandidates, Coordinate newStoneCoord){
        Set<Coordinate> evolutionCoords = new HashSet<>();
        for (Critter critter : evolutionCandidates){
            evolutionCoords.addAll(critter.hexagons);
        }

        critters.removeAll(evolutionCandidates);
        evolutionCoords.add(newStoneCoord);

        return new Critter(evolutionCoords, currentPlayerIndex);
    }

    public void feed(Critter critter){

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

    /**
     * Récupère la liste de tous les critters voisins à la position appartenant au joueur.
     * @param coordinate Les coordonnées de la position où chercher des critter voisins.
     * @return Liste des tous les critters voisins appartenant au joueur.
     */
    private Set<Critter> getCurrentPlayerNeighborsCritters(int playerIndex, Coordinate coordinate){
        Set<Critter> neighbors = new HashSet<>();
        for(Critter critter : critters){
            if(critter.player != playerIndex) continue;
            for (Coordinate stoneCoord : critter.hexagons){
                if(isNeighbor(coordinate, stoneCoord)){
                    neighbors.add(critter);
                    break;
                }
            }
        }
        return neighbors;
    }

    /**
     * Vérifie si 2 coordonnées sont voisines.
     * @param first La première coordonnée.
     * @param second La deuxième coordonnée.
     * @return true si les deux coordonnées sont voisine, false sinon.
     */
    public boolean isNeighbor(Coordinate first, Coordinate second){
        int deltaX = Math.abs(first.line() - second.line());
        int deltaY = Math.abs(first.col() - second.col());
        return deltaX <= 1 && deltaY <= 1;
    }

}
