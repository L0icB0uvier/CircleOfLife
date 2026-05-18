package Controller.IA;

import Model.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class EasyAI extends AI{
    int depth = 1; // profondeur de la recherche Minimax dans l'arbre des états

    public EasyAI(Match match) {
        super(match);
        aiLevel = AILevel.EASY;
    }

    /**
     * Calcule la valeur de la configuration actuelle du match donné en utilisant l'heuristique de l'IA facile
     * (cherche à augmenter son score et à défaut la taille de ses critters)
     * @param match Le match dont on doit évaluer la configuration actuelle.
     * @param playerID L'index joueur de l'IA dans le match.
     * @return 100 * (différence des scores) + taille moyenne des critters de l'IA
     */
    double evaluate(Match match, int playerID){
        PlayerData[] data = match.getPlayerData();
        double[] score = new double[]{(double)data[playerID].getScore(), (double)data[(playerID+1)%2].getScore()};
        return 100 * (score[0] - score[1]) + match.averageCritterSizePLayer(playerID);
    }

    /**
     * Trouve un coup à l'aide d'un arbre MIN/MAX. Si aucun coup n'est favorable, joue un coup valide aléatoire.
     * @return Soit le Move le plus avantageux pour l'IA trouvé avec l'algorithme Minimax, soit un Move valide aléatoire.
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
            if (eval > maxEval){
                maxEval = eval;
                best = new Move(match, move.line(), move.col());
            }
        }
        return best;
    }
}
