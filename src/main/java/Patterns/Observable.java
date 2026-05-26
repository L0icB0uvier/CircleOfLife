package Patterns;

import Model.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Observable {
    List<Observer> updateObserver;
    List<ScoreEventObserver> scoreEventObserver;

    public Observable() {
        updateObserver = new ArrayList<>();
        scoreEventObserver = new ArrayList<>();
    }

    /**
     * Ajoute un observateur.
     * @param observer Observateur à ajouter.
     */
    public void addUpdateObserver(Observer observer) {
        updateObserver.add(observer);
    }

    public void addUpdateScoreObserver(ScoreEventObserver scoreEventObserver){
        this.scoreEventObserver.add(scoreEventObserver);
    }

    /**
     * Retire un observateur.
     * @param observer Observateur à retirer.
     */
    public void removeUpdateObserver(Observer observer) {
        updateObserver.remove(observer);
    }

    /**
     * Appel update sur tous les observateurs enregistrés.
     */
    public void update() {
        var it = updateObserver.iterator();

        while (it.hasNext()) {
            Observer o = it.next();
            o.update();
        }
    }

    public void updateScore(Map<Coordinate, Integer> crittersEaten, int player){
        var it = scoreEventObserver.iterator();

        while (it.hasNext()) {
            ScoreEventObserver o = it.next();
            o.onScoreUpdated(crittersEaten, player);
        }
    }
}
