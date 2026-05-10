package Controller.IA;

import Global.Configuration;
import Model.*;

import java.util.List;
import java.util.Random;

public class EasyAI extends AI{
    int depth = 1;

    public EasyAI(Match match) {
        super(match);
        aiLevel = AILevel.EASY;
    }

    // TODO : trouver une heuristique pour l'IA facile en plus du score
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
        Coordinate random = possibleMoves.get(new Random().nextInt(possibleMoves.size()));
        Move best = new Move(match, random.line(), random.col());
        double maxEval = Double.MIN_VALUE;
        double eval;
        for (Coordinate move : possibleMoves){
            Match newMatch = MatchUtils.copy(match);
            newMatch.playMove(move.line(), move.col());
            eval = minimax(newMatch, this.depth-1, match.getCurrentPlayerIndex(), Double.MIN_VALUE, Double.MAX_VALUE, true);
            if (eval > maxEval){
                maxEval = eval;
                best = new Move(match, move.line(), move.col());
            }
        }
        return best;
    }


}
