package Controller.IA;

import Model.*;

import java.util.Collections;
import java.util.List;

public class MediumAI extends AI {
    int depth = 2;

    public MediumAI(Match match) {
        super(match);
    }

    double evaluate(Match match, int playerID){
        int[] ids = new int[]{playerID, (playerID+1)%2};
        PlayerData[] data = match.getPlayerData();
        double evaluation = 100 * ((double)data[ids[0]].getScore() - (double)data[ids[1]].getScore());
        evaluation += 5 * ((double) MatchUtils.countPlayerCritterSize(match, ids[0], 4) - (double) MatchUtils.countPlayerCritterSize(match, ids[1], 4));
        return evaluation;
    }

    /**
     * Trouve un coup à l'aide d'un arbre MIN/MAX.
     * @return Le Move le plus avantageux pour l'IA, trouvé avec l'algorithme Minimax.
     */
    @Override
    public Move findMove() {
        List<Coordinate> possibleMoves = match.getCurrentPlayerPlayableMoves();
        Collections.shuffle(possibleMoves);
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
}
