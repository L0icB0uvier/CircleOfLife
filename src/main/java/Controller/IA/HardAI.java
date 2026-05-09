package Controller.IA;

import Global.Configuration;
import Model.*;

import java.util.List;
import java.util.Random;

public class HardAI extends AI {
    int depth;

    public HardAI(Match match) {
        super(match);
        aiLevel = AILevel.HARD;
        Configuration.readInt("DepthAI");
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
        int maxEval = Integer.MIN_VALUE;
        int eval;
        for (Coordinate move : possibleMoves){
            Match newMatch = MatchUtils.copy(match);
            newMatch.playMove(move.line(), move.col());
            eval = minimax(newMatch, this.depth, match.getCurrentPlayerIndex(), true);
            if (eval > maxEval){
                maxEval = eval;
                best = new Move(match, move.line(), move.col());
            }
        }
        return best;
    }

    /**
     * Calcule la valeur d'évaluation maximale pouvant être espérée par l'IA
     * après un nombre de coups maximal en utilisant un arbre MIN/MAX.
     * @param match Le match sur lequel le coup doit être calculé.
     * @param depth Le nombre de coups d'avance que l'IA doit calculer.
     * @param playerID L'index joueur de l'IA dans le match.
     * @param isMax Un booléen signifiant si la fonction doit maximiser (tour de l'IA) ou
     *              minimiser (tour de l'adversaire) les résultats d'évaluation sur les
     *              coups possibles.
     * @return La valeur de l'évaluation la plus avantageuse pour l'IA après le nombre de coups spécifié.
     */
    private int minimax(Match match, int depth, int playerID, boolean isMax){
        PlayerData[] data = match.getPlayerData();
        int[] score = new int[]{data[playerID].getScore(), data[(playerID+1)%2].getScore()};

        if (depth == 0 || match.isGameOver()){
            return score[playerID] - score[(playerID+1)%2];
        }

        int eval;

        if (isMax){
            int maxEval = Integer.MIN_VALUE;
            List<Coordinate> possibleMoves = match.getCurrentPlayerPlayableMoves();
            for (Coordinate move : possibleMoves){
                Match newMatch = MatchUtils.copy(match);
                newMatch.playMove(move.line(), move.col());
                eval = minimax(newMatch, depth - 1, playerID, false);
                maxEval = Math.max(eval, maxEval);
            }
            return maxEval;
        }

        else{
            int minEval = Integer.MAX_VALUE;
            List<Coordinate> possibleMoves = match.getCurrentPlayerPlayableMoves();
            for (Coordinate move : possibleMoves){
                Match newMatch = MatchUtils.copy(match);
                newMatch.playMove(move.line(), move.col());
                eval = minimax(newMatch, depth - 1, (playerID+1)%2, true);
                minEval = Math.min(eval, minEval);
            }
            return minEval;
        }
    }
}
