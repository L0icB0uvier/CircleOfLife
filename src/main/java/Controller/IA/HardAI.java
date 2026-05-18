package Controller.IA;

import Global.Configuration;
import Model.*;

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
        depth = (int) Math.floor(Math.log(maxStateNumber) / Math.log(possibleMoves.size()));
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
            if (eval > maxEval){
                maxEval = eval;
                best = new Move(match, move.line(), move.col());
            }
        }
        return best;
    }
}
