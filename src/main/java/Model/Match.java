package Model;

import Global.Configuration;

import java.util.*;

/**
 * Représente l'état d'une partie de jeu et gère toute la logique relative au déroulement d'une partie.
 */
public class Match extends History<Move> implements Cloneable {
    public final static int playerOneIndex = 0;
    public final static int playerTwoIndex = 1;

    PlayerData[] players = new PlayerData[2];
    int currentPlayerIndex;
    int startingPlayer;

    byte[][] boardState; // 0 tile can be played, -1 can't be played by PayerOne, -2 can't be played by playerTwo, -3 can't be player by both players
                        // 1 is occupied by playerOne, 2 by playerTwo

    Set<Critter> critters;

    Set<Critter> previouslyEatenCritters;

    private final int winScore;

    private boolean playing = false;
    private boolean gameOver = false;
    private boolean reviewModeActive;

    private final int boardSize = 9;
    int winner = -1;
    public WinType winType = WinType.PENDING;

    private static final int[][] HEX_DELTAS = {
            {1, 0}, {1, 1}, {0, 1}, {-1, 0}, {-1, -1}, {0, -1}
    };

    public Match(){
        players[0] = new PlayerData("Joueur 1");
        players[1] = new PlayerData("Joueur 2");
        winScore = Configuration.readInt("WinScore");
        initMatch();
    }

    public Match(String name1, String name2){
        players[0] = new PlayerData(name1);
        players[1] = new PlayerData(name2);
        winScore = Configuration.readInt("WinScore");
        currentPlayerIndex = pickStartingPlayerRandom();
        startingPlayer = currentPlayerIndex;
        initMatch();
    }

    public Match(String name1, String name2, int firstPlayer){
        players[0] = new PlayerData(name1);
        players[1] = new PlayerData(name2);
        currentPlayerIndex = firstPlayer < 0 || firstPlayer > 1? pickStartingPlayerRandom() : firstPlayer;
        startingPlayer = currentPlayerIndex;
        winScore = Configuration.readInt("WinScore");
        initMatch();
    }

    /**
     * Change le joueur commençant la partie.
     */
    public void toggleStartingPlayer(){
        startingPlayer = startingPlayer == 0 ? 1 : 0;
        currentPlayerIndex = startingPlayer;
    }

    /**
     * Initialise le match en supprimant l'historique, en initialisant le plateau de jeu et en choisissant un nouveau joueur de manière aléatoire.
     */
    public void initMatch() {
         
        reset();
        InitializeBoard();
        resetScores();
        critters = new HashSet<>();
        previouslyEatenCritters = new HashSet<>();
        gameOver = false;
        winner = -1;
    }

    /**
     * Initialize le plateau de jeu avec des 0 à l'intérieur de l'hexagone de jeu et des MAX_VALUE à l'extérieur.
     */
    private void InitializeBoard() {
        boardState = new byte[boardSize][boardSize];
        for (int l = 0; l < boardSize; l++) {
            for (int c = 0; c < boardSize; c++) {
                boardState[l][c] = MatchUtils.isInsideBoard(new Coordinate(l, c))? 0 : Byte.MAX_VALUE;
            }
        }
    }

    /**
     * Remet les scores à 0.
     */
    void resetScores(){
        players[0].reset();
        players[1].reset();
    }

    /**
     * Choisi un joueur aléatoire.
     */
    int pickStartingPlayerRandom() {
        return new Random().nextInt(2) == 0? 0: 1;
    }

    /**
     * Met fin au tour actuel. Vérifie les conditions de victoires et change de joueur.
     */
    public void endTurn(){
        // Il faut vérifier si le move joué a accordé la victoire au joueur actif
        if(winByScore()){
            gameOver(currentPlayerIndex, WinType.SCORE);
            return;
        }

        toggleCurrentPlayer();

        // Il faut vérifier après avoir changé de joueur si le nouveau joueur a gagné par remplissage
        if(winByFillUp()){
            gameOver(currentPlayerIndex, winType = WinType.FILL);
        }
    }

    /**
     * Met fin à la partie.
     */
    void gameOver(int winner, WinType winType){
        this.winType = winType;
        gameOver = true;
        this.winner = winner;
    }

    /**
     * Active le mode Analyse.
     */
    void enterReviewMode(){
         
        reviewModeActive = true;
    }

    /**
     * Désactive le mode Analyse.
     */
    void exitReviewMode(){
         
        reviewModeActive = false;
    }

    /**
     * Vérifie si la position du Move est valide.
     * @param l La ligne du move.
     * @param c La colonne du move.
     * @return true si la position est valide, faux sinon.
     */
    public boolean isMoveValid(int playerIndex, int l, int c) {
        // Case en dehors du plateau
        if(!MatchUtils.isInsideBoard(new Coordinate(l, c))){
            return false;
        }

        return isBoardTilePlayableForPlayer(playerIndex, l, c);
    }

    public boolean isBoardTilePlayableForPlayer(int playerIndex, int l, int c){
        // Case occupée
        if(boardState[l][c] > 0){
            return false;
        }

        // Case inaccessible interdite pour le joueur
        return isBoardCoordinatesPlayableForPlayer(l, c, playerIndex);
    }

    /**
     * Retourne si une case du plateau est jouable pour un joueur donné.
     * @param l La ligne de la case.
     * @param c La colonne de la case.
     * @param player L'indice du joueur.
     * @return true s'il peut jouer sur la case et false sinon.
     */
    public boolean isBoardCoordinatesPlayableForPlayer(int l, int c, int player){
        return boardState[l][c] != -(player + 1) && boardState[l][c] != -3;
    }

    /**
     * Vérifie si la case est dans le plateau de jeu.
     * @param l La ligne de la case.
     * @param c La colonne de la case.
     * @return true si dans le plateau de jeu, false sinon.
     */
    public boolean isOutsideBoard(int l, int c) {
        return MatchUtils.hexagonalManhattanDistance(new Coordinate(l, c), new Coordinate(4, 4)) > 4;
    }

    @Override
    public void apply(Move newMove) {
        if (!isMoveValid(currentPlayerIndex, newMove.getLine(), newMove.getColumn())){ // invalid Move
            return;
        }
        newMove.setPreviouslyEatenCritters(new HashSet<>(previouslyEatenCritters));
        super.apply(newMove);
    }


    /**
     * Joue un pion du joueur actif sur la case de coordonnées (l, c)
     * @param l La ligne de la case.
     * @param c La colonne de la case.
     */
    public void playMove(int l, int c){
        previouslyEatenCritters.clear();
        // on met la case à jour
        boardState[l][c] = (byte) (currentPlayerIndex + 1); // playerOne <-> 1 ; playerTwo <-> 2
        Coordinate newStoneCoordinate = new Coordinate(c, l);

        // update Critters : evolve or reproduce
        var newCritter = updateCritters(newStoneCoordinate);

        // on nourrit le Critter créé si on peut
        Set<Critter> eatenCritters = feed(newCritter);
        previouslyEatenCritters.addAll(new HashSet<>(eatenCritters));

        if(!eatenCritters.isEmpty()){
            int pointsEarned = MatchUtils.calculatePointEarned(eatenCritters);
            updatePlayerScore(currentPlayerIndex, pointsEarned);
        }

        // mise à jour de l'état du plateau
        updateBoard(newCritter, eatenCritters);

    }

    /**
     * Restore l'état du plateau du tour précédent.
     * @param previousBoardState L'état du plateau au tour précédent.
     */
    public void restoreState(byte[][] previousBoardState, Set<Critter> critters, PlayerData[] previousPlayerData, Set<Critter> previouslyEatenCritters){
        this.boardState = MatchUtils.copyBoard(previousBoardState);
        this.critters = new HashSet<>(critters);
        this.players = MatchUtils.copyPlayerData(previousPlayerData);
        this.previouslyEatenCritters = previouslyEatenCritters;
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
            newCritter = new Critter(Set.of(coord), currentPlayerIndex);
             
        }
        else{
            newCritter = evolve(neighbors, coord);
            if(neighbors.size() == 1){
                 
            }
            else{
                 
            }
        }

        critters.add(newCritter);
        return newCritter;
    }

    /**
     * Évolue un ou plusieurs Critter.
     * @param evolutionCandidates Les Critter existants à fusionner pour l'évolution.
     * @param newStoneCoord Coordonnées de la dernière pierre posée.
     * @return Le Critter évolué.
     */
    public Critter evolve(Set<Critter> evolutionCandidates, Coordinate newStoneCoord){
        Set<Coordinate> evolutionCoords = new HashSet<>();
        for (Critter critter : evolutionCandidates){
            evolutionCoords.addAll(critter.stonesCoordinates());
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

        for (Coordinate coord : critter.stonesCoordinates()){
            opponentNeighbors.addAll(getPlayerNeighborsCritters((critter.player()+1)%2, coord));
        }

        for (Critter c : opponentNeighbors){
            if (canEat(critter.type(), c.type())){
                eatCritter(c);
                eatenCritters.add(c);
            }
        }
        return eatenCritters;
    }

    /**
     * Retourne si un critter peut en manger un autre.
     * @param evolvingCritterType Le type du critter prédateur.
     * @param targetCritterType Le type du critter ciblé.
     * @return true si le critter prédateur peut manger le critter ciblé, faux sinon.
     */
    public boolean canEat(int evolvingCritterType, int targetCritterType){
        return targetCritterType == (evolvingCritterType + 1) % 12;
    }

    /**
     * Efface un critter du plateau.
     * @param c Le Critter à effacer.
     */
    private void eatCritter(Critter c) {
//         
        for (Coordinate coord : c.stonesCoordinates()){
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
        Set<Coordinate> updatedTiles = new HashSet<>(getStonesNeighborTiles(newCritter.stonesCoordinates()));

        if(!eatenCritters.isEmpty()){
            for (Critter critter : eatenCritters){
                updatedTiles.addAll(getStonesNeighborTiles(critter.stonesCoordinates()));
                updatedTiles.addAll(critter.stonesCoordinates());
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
     * @param stones Le critter autour duquel chercher.
     * @return Un HashSet de Coordinate contenant les coordonnées des cases voisines.
     */
    private Set<Coordinate> getStonesNeighborTiles(Set<Coordinate> stones) {
        Set<Coordinate> result = new HashSet<>();
        for (Coordinate coordinate : stones) {
            result.addAll(GetNeighborTiles(coordinate));
        }
        return result;
    }

    /**
     * Retourne toutes les coordonnées voisines de la coordonnée passée en argument.
     * @param coordinate La coordonnée pour laquelle on veut toutes les coordonnées voisines.
     * @return La liste des coordonnées voisines.
     */
    private Set<Coordinate> GetNeighborTiles(Coordinate coordinate) {
        Set<Coordinate> result = new HashSet<>();
        for (int[] delta : HEX_DELTAS) {
            int x = coordinate.line() + delta[0];
            int y = coordinate.col() + delta[1];

            if(x >= boardState.length || x < 0 || y >= boardState[0].length || y < 0)
                continue;

            if (boardState[x][y] <= 0){
                result.add(new Coordinate(y,x));
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
            if (playerIndex == critter.player()){
                result += critter.stonesCoordinates().size();
            }
        }
        return result;
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
     * Change le joueur actif.
     */
    void toggleCurrentPlayer(){
        currentPlayerIndex = currentPlayerIndex == 0 ? 1 : 0;
    }

    /**
     * Vérifie si le joueur actif a gagné au score.
     * @return true si le joueur actif a atteint ou dépassé 20 points, faux sinon.
     */
    public boolean winByScore(){
        return (players[currentPlayerIndex].getScore() >= winScore);
    }

    /**
     * Vérifie si le joueur actif gagne par remplissage.
     * @return true si le joueur actif ne peut plus poser de pierre, false sinon.
     */
    public boolean winByFillUp(){
        return getCurrentPlayerPlayableMoves().isEmpty();
    }

    /**
     * Récupère l'ensemble de tous les critters du joueur voisins aux coordonnées voulu.
     * @param coordinate Les coordonnées de la position où chercher des critter voisins.
     * @return Set des tous les critters voisins appartenant au joueur.
     */
    public Set<Critter> getPlayerNeighborsCritters(int playerIndex, Coordinate coordinate){
        if(critters.isEmpty()) return Collections.emptySet();
        Set<Critter> neighbors = new HashSet<>();
        for(Critter critter : critters){
            if(critter.player() != playerIndex) continue;
            for (Coordinate stoneCoord : critter.stonesCoordinates()){
                if(MatchUtils.isNeighbor(coordinate, stoneCoord)){
                    neighbors.add(critter);
                    break;
                }
            }
        }
        return neighbors;
    }

    /**
     * Retourne le critter aux coordonnées souhaitées s'il existe.
     * @param coord Les coordonnées où chercher un critter.
     * @return L'instance du critter s'il existe, null sinon.
     */
    public Critter getCritterAtCoord(Coordinate coord){
        for (Critter critter : critters){
            if(critter.stonesCoordinates().contains(coord)){
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

    /**
     * Retourne l'index de l'adversaire du joueur actif.
     * @return L'index de l'adversaire du joueur actif.
     */
    public int getOpponentPlayerIndex(){
        return (currentPlayerIndex + 1) % 2;
    }

    public PlayerData[] getPlayerData(){
        return MatchUtils.copyPlayerData(players);
    }

    /**
     * Retourne le score d'un joueur.
     * @param playerIndex L'indice du joueur pour lequel on veut récupérer le score.
     * @return Le score du joueur si l'indice est valide, sinon -1.
     */
    public int getPlayerScore(int playerIndex){
        if(playerIndex > 1 || playerIndex < 0)
            return -1;
        return players[playerIndex].getScore();
    }

    /**
     * Retourne la taille du plateau.
     * @return La taille du plateau.
     */
    public int getBoardSize(){
        return boardSize;
    }

    /**
     * Récupère une copie profonde de l'état du plateau.
     * @return Copier profonde de l'état du plateau.
     */
    public byte[][] getBoardState(){
        return MatchUtils.copyBoard(boardState);
    }

    public Set<Critter> getCritters(){
        return new HashSet<>(critters);
    }

    /**
     * Retourne le contenu d'une case.
     * @param l La ligne de la case.
     * @param c La colonne de la case.
     * @return La valeur du contenu de la case.
     */
    public int getContentAt(int l, int c) {
        if(isOutsideBoard(l, c))
            return Integer.MAX_VALUE;
        return boardState[l][c];
    }

    /**
     * Retourne la liste des coordonnées du plateau correspondant aux pièces mangées au tour précédent.
     * @return Liste des coordonnées du plateau correspondant aux pièces mangées au tour précédent. Liste vide si aucune pièce mangée au tour précédent.
     */
    public List<Coordinate> getPreviouslyEatenCrittersCoordinates(){
        if(previouslyEatenCritters.isEmpty())
            return Collections.emptyList();

        List<Coordinate> eatenCrittersCoordinates = new ArrayList<>();

        for (Critter critter : previouslyEatenCritters){
            eatenCrittersCoordinates.addAll(critter.stonesCoordinates());
        }

        return eatenCrittersCoordinates;
    }

    /**
     * Récupère la liste de tous les Moves jouables par le joueur actif.
     * @return Liste des Moves jouable par le joueur actif.
     */
    public List<Coordinate> getCurrentPlayerPlayableMoves(){
        return getPlayerPlayableMoves(currentPlayerIndex);
    }

    /**
     * Récupère la liste de tous les Moves jouable par un joueur donné.
     * @param playerIndex L'index du joueur.
     * @return Liste des Moves jouables par le joueur.
     */
    public List<Coordinate> getPlayerPlayableMoves(int playerIndex){
        List<Coordinate> playableMoves = new ArrayList<>();
        for (int l = 0; l < boardSize; l++) {
            for (int c = 0; c < boardSize; c++) {
                if(isMoveValid(playerIndex, l, c)){
                    playableMoves.add(new Coordinate(c, l));
                }
            }
        }
        return playableMoves;
    }

    /**
     * Calcule la taille moyenne des critters du joueur donné dans la configuration actuelle du match.
     * @param playerID L'index du joueur dont les critters seront mesurés.
     * @return La taille moyenne des critters du joueur.
     */
    public double averageCritterSizePLayer(int playerID){
        double total = 0;
        double critterNumber = 0;
        for (Critter critter : critters){
            if (critter.player() == playerID){
                critterNumber += 1.;
                total += critter.stonesCoordinates().size();
            }
        }
        return total / critterNumber;
    }

    /**
     * Retourne la liste de tous les critters appartenant à un joueur.
     * @param playerId Le joueur pour lequel on souhaite récupérer les critters
     * @return La liste des critters appartenant au joueur.
     */
    public Set<Critter> getPlayerCritters(int playerId){
        Set<Critter> playerCritters = new HashSet<>();
        for (Critter critter : critters) {
            if(critter.player() == playerId)
                playerCritters.add(critter);
        }
        return playerCritters;
    }

    /**
     * Donne la liste des coordonnées jouables par le joueur autour d'un ensemble de position donné.
     * @param stones Les pierres autour desquelles on cherche des positions jouables.
     * @param player L'indice de joueur pour lequel on cherche des positions jouables.
     * @return La liste des coordonnées jouables par le joueur autour du critter donné. Retourne un EmptySet si aucune position n'est jouable.
     */
    public Set<Coordinate> getPlayerPlayableMovesAroundStones(Set<Coordinate> stones, int player){
        Set<Coordinate> result = new HashSet<>();
        Set<Coordinate> neighbors = getStonesNeighborTiles(stones);
        for (Coordinate neighbor : neighbors) {
            if(isBoardCoordinatesPlayableForPlayer(neighbor.line(), neighbor.col(), player))
                result.add(neighbor);
        }
        return result;
    }

    public void startPlaying() {
        playing = true;
    }

    public boolean isGameOver(){
        return gameOver;
    }

    public int getWinner() {
        return winner;
    }

    public boolean isReviewModeActive() {
        return reviewModeActive;
    }

    public boolean isPlaying() {
        return playing;
    }

    /**
     * Récupère le nombre de critters actuellement présent sur le plateau.
     * @return Le nombre de critters sur le plateau.
     */
    public int getNumberOfCritters(){
        return critters.size();
    }


    @Override
    public Match clone() {
        try {
            Match clone = (Match) super.clone();

            clone.players = new PlayerData[2];
            if (this.players[0] != null) clone.players[0] = this.players[0].clone();
            if (this.players[1] != null) clone.players[1] = this.players[1].clone();

            if (this.boardState != null) {
                clone.boardState = new byte[this.boardSize][];
                for (int i = 0; i < this.boardSize; i++) {
                    clone.boardState[i] = this.boardState[i].clone();
                }
            }

            if (this.critters != null) {
                clone.critters = new HashSet<>(this.critters);
            }

            if (this.previouslyEatenCritters != null) {
                clone.previouslyEatenCritters = new HashSet<>(this.previouslyEatenCritters);
            }

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Ne doit jamais arriver puisque Match implémente Cloneable
        }
    }
}