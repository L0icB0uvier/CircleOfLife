package Controller.IA;

import Global.Configuration;
import Model.Match;
import Model.Move;

import java.util.concurrent.*;

public abstract class AI {
    protected Match match;
    public AILevel aiLevel;
    int minWait = 1000, maxWait = 3000;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    ScheduledFuture<?> scheduledTask;
    CompletableFuture<Move> futureMove;

    public AI(Match match){
        this.match = match;
    }

    /**
     * Méthode helper permettant de créer une instance de l'IA correpondant à la difficulté voulue.
     * @param match Une instance de match sur laquelle l'IA doit opérer.
     * @param aiLevel La difficulté de l'IA souhaité.
     * @return Une instance d'une sous classe de AI de la difficulté demandée.
     */
    public static AI createAI(Match match, AILevel aiLevel){
        AI ai = null;

        switch (aiLevel){
            case EASY -> {
                ai = new EasyAI(match);
            }
            case MEDIUM -> {
                ai = new MediumAI(match);
            }
            case HARD -> {
                ai = new HardAI(match);
            }
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
        Configuration.info("AI waiting " + randomWait + " milliseconds");

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
}
