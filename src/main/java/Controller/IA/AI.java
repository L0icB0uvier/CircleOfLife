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

    public void cancel(){
        if(futureMove != null)
            futureMove.cancel(true);
    }

    abstract public Move findMove();
}
