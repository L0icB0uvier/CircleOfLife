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

    private final int boardSize = 9;

    public Match(){
        players[0] = new PlayerData();
        players[1] = new PlayerData();

        initMatch();
    }

    public void initMatch() {
        reset();
        InitializeBoard();
        critters = new HashSet<>();
        currentPlayerIndex = new Random().nextInt(2) == 0? 0: 1;
        Configuration.info("New game: Player " + (currentPlayerIndex + 1) + " starts");
    }

    private void InitializeBoard() {
        boardState = new int[boardSize][boardSize];
        for (int l = 0; l < boardSize; l++) {
            for (int c = 0; c < boardSize; c++) {
                boardState[l][c] = CoordinateUtils.isInsideBoard(new Coordinate(l, c))? 0 : Integer.MAX_VALUE;
            }
        }
    }

    @Override
    public void apply(Move newMove) {
        if (!isMoveValid(newMove.line, newMove.column)){ // invalid Move
            return;
        }

        super.apply(newMove);

        if(isGameOver()){
            Configuration.info("Player " + currentPlayerIndex + " won!");
            players[currentPlayerIndex].score += 1;
            initMatch();
        }
        else{
            toggleCurrentPlayer();
        }
    }

    /**
     * Vérifie si la position du Move est valide.
     * @param l La ligne du move.
     * @param c La colonne du move.
     * @return true si la position est valide, faux sinon.
     */
    private boolean isMoveValid(int l, int c) {
        if(!CoordinateUtils.isInsideBoard(new Coordinate(l, c))){
            Configuration.warning(String.format("Move impossible en %s - en dehors du plateau.", new Coordinate(c, l)));
            return false;
        }
        if(boardState[l][c] > 0){
            Configuration.warning(String.format("Move impossible en %s - case occupée.", new Coordinate(c, l)));
            return false;
        }

        if(boardState[l][c] == -(currentPlayerIndex + 1)){
            Configuration.warning(String.format("Move impossible %s en - case interdite pour le joueur actif.", new Coordinate(c, l)));
            return false;
        }

        return true;
    }

    private static boolean isOutsideBoard(int l, int c) {
        return CoordinateUtils.hexagonalManhattanDistance(new Coordinate(l, c), new Coordinate(4, 4)) > 4;
    }

    /**
     * Joue un pion du joueur actif sur la case de coordonnées (l, c)
     *
     * @param l La ligne de la case.
     * @param c La colonne de la case.
     */
    public void playMove(int l, int c){
        // on met la case à jour
        boardState[l][c] = currentPlayerIndex + 1; // playerOne <-> 1 ; playerTwo <-> 2
        Coordinate newStoneCoordinate = new Coordinate(c, l);
        Configuration.info(String.format("Joueur %d joue sur la case %s", currentPlayerIndex + 1, newStoneCoordinate));

        // update Critters : evolve or reproduce
        var newCritter = updateCritters(newStoneCoordinate);

        // on nourrit le Critter créé si on peut
        Set<Critter> eatenCritters = feed(newCritter);

        if(!eatenCritters.isEmpty()){
            int pointsEarned = calculatePointEarned(eatenCritters);
            updatePlayerScore(currentPlayerIndex, pointsEarned);
        }

        // mise à jour de l'état du plateau
        updateBoard(newCritter, eatenCritters);
    }



    /**
     * Met à jour les critters du joueur actif après la pose d'une nouvelle pierre.
     * Crée un nouveau critter si la pierre n'a pas de voisins, ou évolue les critters voisins de la pierre.
     * @param coord Les coordonnées de la nouvelle pierre posée.
     * @return Le nouveau critter obtenu soit par création, soit par évolution.
     */
    public Critter updateCritters(Coordinate coord){
        var neighbors = getPlayerNeighborsCritters(currentPlayerIndex, coord);
        Critter newCritter;

        if(neighbors.isEmpty()){
            newCritter = new Critter(coord, currentPlayerIndex);
            Configuration.info(String.format("Création d'un nouveau critter de type %d", newCritter.type));
        }
        else{
            newCritter = evolve(neighbors, coord);
            if(neighbors.size() == 1){
                Configuration.info(String.format("Evolution d'un critter de type %d en critter de type %d", neighbors.iterator().next().type, newCritter.type));
            }
            else{
                Configuration.info(String.format("Evolution de plusieurs critters en critter de type %d", newCritter.type));
            }
        }

        critters.add(newCritter);
        return newCritter;
    }

    /**
     * Evolue un ou plusieurs Critter.
     * @param evolutionCandidates Les Critter existants à fusionner pour l'évolution.
     * @param newStoneCoord Coordonnées de la dernière pierre posée.
     * @return Le Critter evolué.
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

    /**
     * Mange un ou plusieurs Critter.
     * @param critter Le critter à nourrir.
     * @return L'ensemble des Critter mangés par le Critter en argument.
     */
    public Set<Critter> feed(Critter critter){
        HashSet<Critter> opponentNeighbors = new HashSet<>();
        HashSet<Critter> eatenCritters = new HashSet<>();

        for (Coordinate coord : critter.hexagons){
            opponentNeighbors.addAll(getPlayerNeighborsCritters((critter.player+1)%2, coord));
        }

        for (Critter c : opponentNeighbors){
            if (c.type == (critter.type + 1)%12){
                eatCritter(c);
                eatenCritters.add(c);
            }
        }
        return eatenCritters;
    }

    /**
     * Efface un critter du plateau.
     * @param c Le Critter à effacer.
     */
    private void eatCritter(Critter c) {
        Configuration.info(String.format("Player %d eats critter of type %d", currentPlayerIndex + 1, c.type));
        for (Coordinate coord : c.hexagons){
            boardState[coord.line()][coord.col()] = 0;
        }
        critters.remove(c);
    }

    /**
     * Mise à jour des cases du plateau qui ont été affecté par le Move.
     * @param newCritter Le nouveau critter ajouté pendant ce tour.
     * @param eatenCritters Les critters mangé pendant ce tour.
     */
    private void updateBoard(Critter newCritter, Set<Critter> eatenCritters) {
        // on fait la liste des tuiles qui peuvent avoir besoin d'être mises à jour
        Set<Coordinate> updatedTiles = new HashSet<>(freeNeighborTiles(newCritter));

        if(!eatenCritters.isEmpty()){
            for (Critter critter : eatenCritters){
                updatedTiles.addAll(freeNeighborTiles(critter));
                updatedTiles.addAll(critter.hexagons);
            }
        }

        // on parcoure la liste et on met les cases à jour
        for (Coordinate coordinate : updatedTiles) {
            if (boardState[coordinate.line()][coordinate.col()] <= 0) {
                boardState[coordinate.line()][coordinate.col()] = 0;
                int playerOneSum = sumPlayerNeighborCritters(playerOneIndex, coordinate);
                int playerTwoSum = sumPlayerNeighborCritters(playerTwoIndex, coordinate);
                if (playerOneSum >= 4) { // playerOne ne peut pas jouer ici
                    boardState[coordinate.line()][coordinate.col()] -= 1;
                }
                if (playerTwoSum >= 4) { // playerTwo ne peut pas jouer ici
                    boardState[coordinate.line()][coordinate.col()] -= 2;
                }
            }
        }
    }

    /**
     * Trouve toutes les case vides (pas forcément jouables !) autour d'un critter donné.
     * @param critter Le critter autour duquel chercher.
     * @return Un HashSet de Coordinate contenant les coordonnées des cases voisines.
     */
    private Set<Coordinate> freeNeighborTiles(Critter critter) {
        Set<Coordinate> result = new HashSet<>();
        for (Coordinate coordinate : critter.hexagons) {
            for (int[] delta : new int[][]{{1, 0}, {1, 1}, {0, 1}, {-1, 0}, {-1, -1}, {0, -1}}) {
                int x = coordinate.line() + delta[0];
                int y = coordinate.col() + delta[1];

                //TODO Rajouter un check de bounds pour ne pas lire en dehors des cases du plateau.
                if(x >= boardState.length || x < 0 || y >= boardState[0].length || y < 0)
                    continue;

                if (boardState[x][y] <= 0){
                    result.add(new Coordinate(y,x));
                }
            }

        }
        return result;
    }

    /**
     * Calcule la somme des tailles des Critter appartenant à un joueur donné et voisins d'une case donnée.
     * @param playerIndex Le numéro du joueur.
     * @param coordinate Les coordonnées de la case.
     * @return La somme des tailles calculée.
     *
     */
    private int sumPlayerNeighborCritters(int playerIndex, Coordinate coordinate) {
        Set<Critter> critterNeighbors = getPlayerNeighborsCritters(playerIndex, coordinate);
        int result = 0;
        for (Critter critter : critterNeighbors){
            if (playerIndex == critter.player){
                result += critter.hexagons.size();
            }
        }
        return result;
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

    /**
     * Met à jour le score du joueur correspondant.
     * @param playerIndex L'index du joueur pour lequel il faut mettre à jour le score.
     * @param increaseAmount Le nombre de points à ajouter à son score.
     */
    public void updatePlayerScore(int playerIndex, int increaseAmount){
        players[playerIndex].increaseScore(increaseAmount);
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
     * @return true si les conditions de victoires sont remplies, faux sinon.
     */
    public boolean isGameOver(){
        return (players[currentPlayerIndex].getScore() >= 20 || players[currentPlayerIndex].getPlayableTilesNumber() == 0);
    }

    /**
     * Récupère l'ensemble de tous les critters voisins à la position appartenant au joueur.
     * @param coordinate Les coordonnées de la position où chercher des critter voisins.
     * @return Set des tous les critters voisins appartenant au joueur.
     */
    private Set<Critter> getPlayerNeighborsCritters(int playerIndex, Coordinate coordinate){
        if(critters.isEmpty()) return Collections.emptySet();
        Set<Critter> neighbors = new HashSet<>();
        for(Critter critter : critters){
            if(critter.player != playerIndex) continue;
            for (Coordinate stoneCoord : critter.hexagons){
                if(CoordinateUtils.isNeighbor(coordinate, stoneCoord)){
                    neighbors.add(critter);
                    break;
                }
            }
        }
        return neighbors;
    }

    /**
     * Récupère le nombre de critters actuellement présent sur le plateau.
     * @return Le nombre de critters sur le palteau.
     */
    public int getNumberOfCritters(){
        return critters.size();
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
     * Retourne l'index du joueur actif.
     * @return L'index du joueur actif.
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public PlayerData[] getPlayerData(){
        return players;
    }

    public int getBoardSize(){
        return boardSize;
    }

    public int getContentAt(int l, int c) {
        return boardState[l][c];
    }
}
