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
        critters = new HashSet<>();
        currentPlayerIndex = new Random().nextInt(2) == 0? 0: 1;
        Configuration.info("New game: Player " + (currentPlayerIndex + 1) + " starts");
    }

    /**
     * Essaie de jouer un coup pour le joueur actif.
     * @param l La ligne où jouer.
     * @param c La colonne où jouer.
     */
    public void playMove(int l, int c){
        /// 1 - check if Move is valid
        if (isMoveInvalid(l, c)){ // invalid Move
            Configuration.warning("Impossible de jouer à la case " + c + ":" + l);
            return;
        }
        else{
            /// 2 - update corresponding tile
            boardState[l][c] = currentPlayerIndex + 1; // playerOne <-> 1 ; playerTwo <-> 2
            Coordinate newStoneCoordinate = new Coordinate(c, l);

            /// 3 - update Critters : evolve or reproduce, then feed
            var newCritter = updateCritters(newStoneCoordinate);

            // TODO : update list of Critters and currentPlayer score if necessary

            feed(newCritter);
        }
    }

    /**
     * Vérifie si la position du Move est invalide.
     * @param l La ligne du move.
     * @param c La colonne du move.
     * @return true si la position est invalide, faux sinon.
     */
    private boolean isMoveInvalid(int l, int c) {
        return Math.max(Math.abs(l - 4), Math.abs(c - 4)) > 4 || boardState[l][c] > 0 || boardState[l][c] == -(currentPlayerIndex + 1);
    }

    /**
     * Met à jour les critters du joueur actif après la pose d'une nouvelle pierre.
     * Crée un nouveau critter s'il la pierre n'a pas de voisins ou évolu les critters voisins de la pierre.
     * @param coord Les coordonnées de la nouvelle pierre posée.
     * @return Le nouveau critter obtenu soit par création soit par évolution.
     */
    public Critter updateCritters(Coordinate coord){
        var neighbors = getPlayerNeighborsCritters(currentPlayerIndex, coord);
        Critter newCritter;

        if(neighbors.isEmpty()){
            newCritter = new Critter(coord, currentPlayerIndex);
        }
        else{
            newCritter = evolve(neighbors, coord);
        }

        critters.add(newCritter);
        return newCritter;
    }

    /**
     * Retourne le critter aux coordonnées souhaités s'il existe.
     * @param coord Les coordonnées où chercher un critter.
     * @return L'instance du critter s'il existe, null sinon.
     */
    public Critter getCritterAtCoord(Coordinate coord){
        for (Critter critter : critters){
            if(critter.hexagons.contains(coord)){
                return critter;
            }
        }
        return null;
    }

    /**
     * Evolue un ou plusieurs critters. Les critters fusionné sont supprimé et un nouvelle instance de Critter pour le critter évolué est créé.
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

    /**
     * Met à jour le score du joueur correspondant.
     * @param playerIndex L'index du joueur pour lequel il faut mettre à jour le score.
     * @param increaseAmount Le nombre de points à ajouter à son score.
     */
    public void updatePlayerScore(int playerIndex, int increaseAmount){
        players[playerIndex].increaseScore(increaseAmount);
    }

    /**
     * Calcule le nombre de points gagné en mangeant des critters.
     * @param eatenCritters La liste des critters mangés.
     * @return Le nombre de points gagnés.
     */
    public int calculatePointEarned(Set<Critter> eatenCritters){
        int score = 0;
        for (Critter critter : eatenCritters)
            score += critter.hexagons.size();
        return score;
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

    /**
     * Change le joueur actif.
     */
    private void toggleCurrentPlayer(){
        currentPlayerIndex = currentPlayerIndex == 0 ? 1 : 0;
        Configuration.info("Player " + (currentPlayerIndex + 1) + " turn");
    }

    /**
     * Vérifie si la partie est terminée.
     * @return true si les condition de victoires sont remplies, faux sinon.
     */
    public boolean isGameOver(){
        return (players[currentPlayerIndex].getScore() >= 20 || players[currentPlayerIndex].getPlayableTilesNumber() == 0);
    }

    /**
     * Retourne l'index du joueur actif.
     * @return L'index du joueur actif.
     */
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
    private Set<Critter> getPlayerNeighborsCritters(int playerIndex, Coordinate coordinate){
        if(critters.isEmpty()) return Collections.emptySet();
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

    /**
     * Récupère le nombre de critters actuellement présent sur le plateau.
     * @return Le nombre de critters sur le palteau.
     */
    public int getNumberOfCritters(){
        return critters.size();
    }
}
