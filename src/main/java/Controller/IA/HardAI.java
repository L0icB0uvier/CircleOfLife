package Controller.IA;

import Global.Configuration;
import Model.*;

import java.util.*;

public class HardAI extends AI {

    int depth;
    GamePhase currentPhase;
    int startPhase2 = 3;
    int startPhase3 = 50;

    /**
     * Score de base associées à chaque forme de critter en fonction du stade de la partie.
     */
    public static final Map<Integer, Map<GamePhase, Integer>> crittersBaseScore = Map.ofEntries(
            Map.entry(0, Map.of(GamePhase.Beginning, 50, GamePhase.Middle, 10, GamePhase.End, 5)),
            Map.entry(1, Map.of(GamePhase.Beginning, 5, GamePhase.Middle, 5, GamePhase.End, 0)),
            Map.entry(2, Map.of(GamePhase.Beginning, 10, GamePhase.Middle, 50, GamePhase.End, 50)),
            Map.entry(3, Map.of(GamePhase.Beginning, 10, GamePhase.Middle, 40, GamePhase.End, 40)),
            Map.entry(4, Map.of(GamePhase.Beginning, 10, GamePhase.Middle, 30, GamePhase.End, 30)),
            Map.entry(5, Map.of(GamePhase.Beginning, 10, GamePhase.Middle, 30, GamePhase.End, 30)),
            Map.entry(6, Map.of(GamePhase.Beginning, 10, GamePhase.Middle, 30, GamePhase.End, 30)),
            Map.entry(7, Map.of(GamePhase.Beginning, 10, GamePhase.Middle, 30, GamePhase.End, 30)),
            Map.entry(8, Map.of(GamePhase.Beginning, 20, GamePhase.Middle, 100, GamePhase.End, 20)),
            Map.entry(9, Map.of(GamePhase.Beginning, 20, GamePhase.Middle, 100, GamePhase.End, 20)),
            Map.entry(10, Map.of(GamePhase.Beginning, 20, GamePhase.Middle, 100, GamePhase.End, 20)),
            Map.entry(11, Map.of(GamePhase.Beginning, 40, GamePhase.Middle, 10, GamePhase.End, 10))
    );

    Map<GamePhase, Integer> scoreMultiplier = Map.of(
            GamePhase.Beginning, 1, GamePhase.Middle, 20, GamePhase.End, 10
    );

    Map<GamePhase, Integer> freedomBonus = Map.of(
            GamePhase.Beginning, 20, GamePhase.Middle, 10, GamePhase.End, 5
    );

    public HardAI(Match match) {
        super(match);
        currentPhase = GamePhase.Beginning;
        depth = 4;
    }

    double evaluate(Match match, int playerID){
        PlayerData[] data = match.getPlayerData();
        int otherPlayerID = MatchUtils.getOtherPlayerIndex(playerID);

        double[] score = new double[]{
                (double)data[playerID].getScore() * scoreMultiplier.get(currentPhase) + getCritterScore(playerID, otherPlayerID, currentPhase),
                (double)data[otherPlayerID].getScore() * scoreMultiplier.get(currentPhase) + getCritterScore(otherPlayerID, otherPlayerID, currentPhase)
        };

        return score[0] - score[1];
    }

    double getCritterScore(int player, int otherPlayer, GamePhase gamePhase){
        double score = 0;

        var playerCritters = match.getPlayerCritters(player);

        for (Critter playerCritter : playerCritters) {
            // Attribution d'un score de base en fonction des critters joués.
            int critterScore = crittersBaseScore.get(playerCritter.type()).get(gamePhase);
            int freedoms = match.getPlayerPlayableMovesAroundStones(playerCritter.stonesCoordinates(), otherPlayer).size();
            critterScore += freedomBonus.get(currentPhase) * freedoms;

//            // Il s'agit d'un critter de taille 4, on doit regarder s'il peut se faire manger facilement
//            if(playerCritter.type() > 0 && playerCritter.type() < 8){
//                var evolutionTarget = CritterUtils.getEatingCritterType(playerCritter.type());
//
//                // On regarde d'abord les critters adjacents
//                var adjacentOpponentCritters = match.getOpponentAdjacentCritters(playerCritter);
//                for (Critter adjacentOpponentCritter : adjacentOpponentCritters) {
//                    if(match.canEvolveIn1Move(adjacentOpponentCritter, evolutionTarget)){
//                        critterScore -= predatorEvolutionMalus;
//                    }
//                }

                // Par la suite on peut regarder plus loin
//            }

            score += critterScore;
        }

        return score;
    }

    double getFillScore(){
        return 0;
    }

    /**
     * Trouve un coup à l'aide d'un arbre MIN/MAX.
     * @return Le Move le plus avantageux pour l'IA, trouvé avec l'algorithme Minimax.
     */
    @Override
    public Move findMove() {
        //Configuration.info("Début de recherche de coup.");
        updatePhase( match.getCurrentPlayerIndex());
        List<Coordinate> possibleMoves = match.getCurrentPlayerPlayableMoves();
        //Collections.shuffle(possibleMoves);
        sortPossibleMoves(match, possibleMoves);
//        if (possibleMoves.size()!= 1) {
//            depth = Math.max(4, (int) Math.floor(Math.log(maxStateNumber) / Math.log(possibleMoves.size())));
//        }
        double maxEval = -Double.MAX_VALUE;
        double eval;
        List<Move> bests = new ArrayList<>();

        // on parcourt les coups possibles et on trouve celui qui maximise l'évaluation de l'heuristique à l'aide d'un Minimax
        for (Coordinate move : possibleMoves){
            // copie profonde du match et simulation du coup
            Match newMatch = MatchUtils.copy(match);
            newMatch.playMove(move.line(), move.col());
            newMatch.endTurn();

            // évaluation Minimax et mise à jour du coup optimal
            eval = minimax(newMatch, this.depth-1, match.getCurrentPlayerIndex(), -Double.MAX_VALUE, Double.MAX_VALUE, false);
            if (eval >= maxEval){
                if (eval != maxEval){
                    bests.clear();
                    maxEval = eval;
                }
                bests.add(new Move(match, move.line(), move.col()));
            }
        }
        //Configuration.info("Fin de recherche de coup.");
        Random RNG = new Random();
        return bests.get(RNG.nextInt(bests.size()));
    }

    void updatePhase(int playerID){
        switch (currentPhase){
            case Beginning -> {
                int count = 0;
                var critters = match.getCritters();
                for (Critter critter : critters) {
                    if(critter.type() > 0 && critter.type() < 11){
                        count++;
                    }
                }
                if (count >= startPhase2){
                    Configuration.info("Changement de phase en phase 2");
                    currentPhase = GamePhase.Middle;
                }
            }
            case Middle -> {
                if(getMoveRemainingFactor(playerID) < startPhase3){
                    Configuration.info("Changement de phase en phase 3");
                    currentPhase = GamePhase.End;
                    depth = 8;
                }
            }
            case End -> {
                if(getMoveRemainingFactor(playerID) >= startPhase3){
                    Configuration.info("Changement de phase en phase 2");
                    currentPhase = GamePhase.Middle;
                    depth = 4;
                }
            }
        }
    }

    private int getMoveRemainingFactor(int playerID){
        int playableMovesCount = match.getPlayerPlayableMoves(playerID).size();
        int opponentPlayableMovesCount = match.getPlayerPlayableMoves(MatchUtils.getOtherPlayerIndex(playerID)).size();

        return playableMovesCount * opponentPlayableMovesCount;
    }
}
