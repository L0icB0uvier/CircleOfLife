package Controller.IA;

import Global.Configuration;
import Model.Coordinate;
import Model.Match;
import Model.MatchUtils;
import Model.Move;

import java.util.List;
import java.util.concurrent.*;

public abstract class AI {
    protected Match match;
    int minWait = 200, maxWait = 600;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    ScheduledFuture<?> scheduledTask;
    CompletableFuture<Move> futureMove;

    public AI(Match match){
        this.match = match;
    }

    public enum GamePhase{
        Beginning,
        Middle,
        End
    }

    /**
     * Méthode helper permettant de créer une instance de l'IA correspondant à la difficulté voulue.
     * @param match Une instance de match sur laquelle l'IA doit opérer.
     * @param aiLevel La difficulté de l'IA souhaité.
     * @return Une instance d'une sous classe de AI de la difficulté demandée.
     */
    public static AI createAI(Match match, AILevel aiLevel){
        AI ai = null;

        switch (aiLevel){
            case EASY -> ai = new EasyAI(match);
            case MEDIUM -> ai = new MediumAI(match);
            case HARD -> ai = new HardAI(match);
        }

        return ai;
    }

    /**
     * Exécute sur un autre Thread la logique de l'IA. Simule la réflexion de l'IA à l'aide d'un scheduler puis calcule le prochain mouvement que doit faire l'IA.
     * @return Le Move joué par l'IA.
     */
    public CompletableFuture<Move> play() {
        futureMove = new CompletableFuture<>();

        long randomWait = ThreadLocalRandom.current().nextLong(minWait, maxWait + 1);
        //Configuration.info("AI waiting " + randomWait + " milliseconds");

        scheduledTask = scheduler.schedule(() -> {
            try {
                if (futureMove.isCancelled()) return;

                Move move = findMove();
                futureMove.complete(move);
            } catch (Exception e) {
                futureMove.completeExceptionally(e);
            }
        }, randomWait, TimeUnit.MILLISECONDS);

        // 2. Link the CompletableFuture cancellation to the ScheduledTask
        futureMove.whenComplete((result, exception) -> {
            if (futureMove.isCancelled() && scheduledTask != null) {
                scheduledTask.cancel(true);
                Configuration.info("AI Task and Timer cancelled.");
            }
        });

        return futureMove;
    }

    /**
     * Annule le calcul du coup de l'IA en annulant le CompletableFuture qui sera géré directement dans le Thread.
     */
    public void cancel(){
        if(futureMove != null)
            futureMove.cancel(true);
    }

    /**
     * Lance la logique de calcul du coup. À implémenter dans chaque sous classes avec la logique spécifique à l'IA souhaitée.
     * @return Le Move joué par l'IA à ce tour.
     */
    abstract public Move findMove();

    /**
     * Calcule la valeur de la configuration actuelle du plateau pour l'algorithme de Minimax.
     * À implémenter dans chaque sous classes avec la logique spécifique à l'IA souhaitée.
     * @param match Le match dont on doit évaluer la configuration actuelle.
     * @param playerID L'index joueur de l'IA dans le match.
     * @return La valeur de la configuration actuelle.
     */
    abstract double evaluate(Match match, int playerID);

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
    double minimax(Match match, int depth, int playerID, double alpha, double beta, boolean isMax){
        // cas de base : si la partie est terminée ou la profondeur max atteinte, on évalue l'état du match
        if (depth == 0){
            return evaluate(match, playerID);
        }
        if (match.isGameOver()) {
            if (match.getWinner() == playerID){
                return Double.MAX_VALUE;
            }
            else if (match.getWinner() == (playerID + 1)%2){
                return -Double.MAX_VALUE;
            }
        }

        double eval;
        // cas récursif : si c'est le tour de l'IA, on sélectionne le coup qui maximise l'évaluation
        if (isMax){
            double maxEval = -Double.MAX_VALUE;
            List<Coordinate> possibleMoves = match.getCurrentPlayerPlayableMoves();
//            Collections.shuffle(possibleMoves);
            sortPossibleMoves(match, possibleMoves);
            for (Coordinate move : possibleMoves){
                // copie profonde du match et simulation d'un coup
                Match newMatch = MatchUtils.copy(match);
                newMatch.playMove(move.line(), move.col());
                newMatch.endTurn();

                // appel récursif : on suppose que l'adversaire jouera le pire coup selon l'évaluation choisie par l'IA
                eval = minimax(newMatch, depth - 1, playerID, alpha, beta, false);
                maxEval = Math.max(eval, maxEval);

                // mise à jour du maximum des évaluations jusqu'ici pour élaguer si possible
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        }

        // cas récursif : si c'est le tour de l'adversaire, on suppose que le coup choisi minimisera l'évaluation
        else{
            double minEval = Double.MAX_VALUE;
            List<Coordinate> possibleMoves = match.getCurrentPlayerPlayableMoves();
//            Collections.shuffle(possibleMoves);
            sortPossibleMoves(match, possibleMoves);
            for (Coordinate move : possibleMoves){
                // copie profonde du match et simulation d'un coup
                Match newMatch = MatchUtils.copy(match);
                newMatch.playMove(move.line(), move.col());
                newMatch.endTurn();

                // appel récursif : l'IA cherche à nouveau à maximiser l'évaluation
                eval = minimax(newMatch, depth - 1, playerID, alpha, beta, true);
                minEval = Math.min(eval, minEval);

                // mise à jour du minimum des évaluations jusqu'ici pour élaguer si possible
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }


    /**
     * Trie les coups possibles pour le joueur actif d'un match donné, en utilisant une méthode de drapeau multicolore
     * @param match Le match sur lequel les coups possibles peuvent être joués.
     * @param possibleMoves Une List contenant l'ensemble des coups jouables par le joueur actif.
     */
    void sortPossibleMoves(Match match, List<Coordinate> possibleMoves){
        Coordinate[] movesArray = possibleMoves.toArray(new Coordinate[0]);
        int id1 = 0;
        int id2 = movesArray.length - 1;
        int id3 = movesArray.length - 1;
        while (id1 <= id2){
            // Cas le plus prioritaire
            if (MatchUtils.isOpponentGreyTile(match, movesArray[id1])){ // l'adversaire ne peut pas jouer ici
                id1++;
            }

            // Deuxième cas le plus prioritaire
            else if (MatchUtils.isEvolutionTile(match, movesArray[id1])){ // jouer ici fera évoluer un(des) critter(s)
                switchMoves(movesArray, id1, id2);
                id2--;
            }

            // Cas le moins prioritaire
            else{ // choix par défaut
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

    /**
     * Échange deux coups dans une table des coups donnée.
     * @param movesArray La table de coups.
     * @param id1 L'indice du premier coup dans la table.
     * @param id2 L'indice du deuxième coup dans la table.
     */
    void switchMoves(Coordinate[] movesArray, int id1, int id2){
        Coordinate temp = movesArray[id1];
        movesArray[id1] = movesArray[id2];
        movesArray[id2] = temp;
    }
}
