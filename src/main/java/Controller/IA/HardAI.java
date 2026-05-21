package Controller.IA;

import Global.Configuration;
import Model.*;
import com.sun.jdi.connect.ListeningConnector;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class HardAI extends AI {
    final long maxStateNumber = 10000000;
    int depth;

    public HardAI(Match match) {
        super(match);
        aiLevel = AILevel.HARD;
        depth = 5;
    }

    // TODO : trouver une heuristique pour l'IA difficile en plus du score
    double evaluate(Match match, int playerID){
        PlayerData[] data = match.getPlayerData();
        double[] score = new double[]{(double)data[playerID].getScore(), (double)data[(playerID+1)%2].getScore()};
        return score[0] - score[1];
    }

    /**
     * Trouve un coup à l'aide d'un arbre MIN/MAX.
     * @return Le Move le plus avantageux pour l'IA, trouvé avec l'algorithme Minimax.
     */
    @Override
    public Move findMove() {
        List<Coordinate> possibleMoves = match.getCurrentPlayerPlayableMoves();
        Collections.shuffle(possibleMoves);
        depth = Math.max(4, (int) Math.floor(Math.log(maxStateNumber) / Math.log(possibleMoves.size())));
        double maxEval = -Double.MAX_VALUE;
        double eval;
        Move best = null;

        // on parcourt les coups possibles et on trouve celui qui maximise l'évaluation de l'heuristique à l'aide d'un Minimax
        for (Coordinate move : possibleMoves){
            // copie profonde du match et simulation du coup
            Match newMatch = MatchUtils.copy(match);
            newMatch.playMove(move.line(), move.col());
            newMatch.endTurn();

            // évaluation Minimax et mise à jour du coup optimal
            eval = minimax(newMatch, this.depth-1, match.getCurrentPlayerIndex(), -Double.MAX_VALUE, Double.MAX_VALUE, false);
            if (eval >= maxEval){
                maxEval = eval;
                best = new Move(match, move.line(), move.col());
            }
        }
        return best;
    }

    void sortPossibleMoves(List<Coordinate> possibleMoves, Match match){
        Coordinate[] movesArray = possibleMoves.toArray(new Coordinate[0]);
        int currentPlayer = match.getCurrentPlayerIndex();
        byte[][] boardstate = match.getBoardState();
        int id1 = 0;
        int id2 = movesArray.length - 1;
        int id3 = movesArray.length - 1;
        while (id1 <= id2){
            if (MatchUtils.isOpponentGreyTile(match, movesArray[id1], currentPlayer)){
                id1++;
            }
            else if (MatchUtils.isEvolutionTile(match, movesArray[id1], currentPlayer)){
                switchMoves(movesArray, id1, id2);
                id2--;
            }
            else{
                switchMoves(movesArray, id1, id2);
                switchMoves(movesArray, id2, id3);
                id2--;
                id3--;
            }
        }
        for (int i = 0; i< possibleMoves.size(); i++){
            possibleMoves.set(i, movesArray[i]);
        }
    }

    void switchMoves(Coordinate[] movesArray, int id1, int id2){
        Coordinate temp = movesArray[id1];
        movesArray[id1] = movesArray[id2];
        movesArray[id2] = temp;
    }

}
